package com.mycompany.employee_management.user;

import com.mycompany.employee_management.EmailService.EmailService;
import com.mycompany.employee_management.department.Department;
import com.mycompany.employee_management.department.DepartmentRepository;
import com.mycompany.employee_management.department.DepartmentRepositoryImplementation;
import com.mycompany.employee_management.department.DepartmentResponse;
import com.mycompany.employee_management.exception.DuplicateFoundException;
import com.mycompany.employee_management.exception.InvalidCredentialsException;
import com.mycompany.employee_management.exception.OperationFailedException;
import com.mycompany.employee_management.exception.ResourceNotFoundException;
import com.mycompany.employee_management.permission.Permission;
 
import com.mycompany.employee_management.permission.PermissionResponse;
import com.mycompany.employee_management.security.JwtUtil;
import com.mycompany.employee_management.user.forgetPassword.ForgetPasswordRequest;
import com.mycompany.employee_management.user.forgetPassword.SendForgetPasswordEmail;
import com.mycompany.employee_management.user.loginPojo.LoginRequest;
import com.mycompany.employee_management.user.loginPojo.LoginResponse;
import com.mycompany.employee_management.user.logout.LogoutResponse;
import com.mycompany.employee_management.user.passwordChange.ChangePasswordRequest;
  
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author sachi
 */
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository = (UserRepository) new UserRepositoryImplementation();
     
    
    private final DepartmentRepository departmentRepository = (DepartmentRepository) new DepartmentRepositoryImplementation();
    
 
    @Override
    public UserResponse createUser(UserRequest user) {

        Optional<User> checkUser = userRepository.findByEmail(user.getEmail());

        if (checkUser.isPresent()) {
            throw new DuplicateFoundException("Email Already Exists!");
        }

        UserResponse userResponse = new UserResponse();

        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
        
        Department department = departmentRepository.findById(user.getDepartment()).orElseThrow(()->new ResourceNotFoundException("Department Not Found"));

        User savedUser = userRepository.save(new User(user.getName(), user.getEmail(), hashedPassword,  department, new ArrayList<>()));

        userRepository.addNewUserPermissions(user.getPermissions(), savedUser.getId());

        //Retrieving part
        User fetchedUser = userRepository.findById(savedUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found with Id : " + savedUser.getId()));

        userResponse.setId(fetchedUser.getId());
        userResponse.setName(fetchedUser.getName());

        userResponse.setEmail(fetchedUser.getEmail());

        userResponse.setDepartment(new DepartmentResponse(department.getId(), department.getName()));

        userResponse.setPermission(userRepository.getUserPermission(fetchedUser.getId()));

        return userResponse;
    }

    @Override
    public String deleteUser(int id) {

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cannot find user with id : " + id));

        String response = userRepository.delete(id);
        return response;
    }

    @Override
    public UserResponse updateUser(UserRequest userRequest, int userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Cannot find user with id : " + userId));
        
          Department department = departmentRepository.findById(userRequest.getDepartment()).orElseThrow(()->new ResourceNotFoundException("Department Not Found"));

        User updatedUser = userRepository.update(new User(userRequest.getName(),userRequest.getEmail(), department,new ArrayList<>()),userId);


        List<Integer> oldPermissionIdList = userRepository.getUserOldPermissionId(updatedUser.getId());

        userRepository.checkAndAddThePermissionToAdd(userRequest.getPermissions(), oldPermissionIdList, userId);

        userRepository.checkAndRemoveThePermissionToRemove(userRequest.getPermissions(), oldPermissionIdList, userId);

        return new UserResponse(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                new DepartmentResponse(department.getId(), department.getName()),
                userRepository.getUserPermission(updatedUser.getId()));
    }

    @Override
    public List<UserResponse> getAllUser() {

        List<UserResponse> users = userRepository.getAllUser();

        return users;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not Found with email : " + loginRequest.getEmail()));

        try {

            if (BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {

                List<Integer> permissionsIds = getUserPermission(user.getId())
                        .stream().map(PermissionResponse::getId)
                        .collect(Collectors.toList());

                var claims = Map.of(
                        "email", user.getEmail(),
                        "permissions", permissionsIds,
                        "department",user.getDepartment(),
                        "id",user.getId(),
                        "name",user.getName()
                );

                String accessToken = JwtUtil.generateAcessToken(String.valueOf(user.getId()), claims);
                String refreshToken = JwtUtil.generateRefreshToken(String.valueOf(user.getId()), claims);
                
 
                return new LoginResponse( accessToken, refreshToken);
                
            } else {
                throw new InvalidCredentialsException("Invalid Email or Password !");
            }

        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid Email or Password !");
        }

    }

    @Override
    public List<PermissionResponse> getUserPermission(int userId) {

        return userRepository.getUserPermission(userId);
    }

    @Override
    public Map<String, Integer> getDashboardDetails() {

        return userRepository.getDashboardData();
    }

    @Override
    public LogoutResponse logout() {

        return new LogoutResponse("Logout Success!");

    }
    
    
    @Override
    public String sendForgetPasswordEmail(SendForgetPasswordEmail request) {
        
        User user  = userRepository.findByEmail(request.getEmail()).orElseThrow(()->new ResourceNotFoundException("User not Found"));
         
        String emailSignedToken=JwtUtil.generateVerificationToken(request.getEmail());
        
        if(emailSignedToken == null){
            throw new OperationFailedException("Token generation Failed", 500);
        }
        
        String emailResponse = EmailService.sendEmail(request.getEmail(), emailSignedToken);
        
        return emailResponse;
        
    }

    @Override
    public UserResponse changePassword(ChangePasswordRequest request){
        
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->new ResourceNotFoundException("User not found"));
        
     
        if(!(BCrypt.checkpw(request.getCurrentPassword(), user.getPassword()))){
            throw new OperationFailedException("Incorrect Current Password!",400);
            
        }
         
        String hashedPw = BCrypt.hashpw(request.getNewPassword(),BCrypt.gensalt(10));
  
        String passwordResponse = userRepository.changePassword(request.getEmail(), hashedPw);
        
        return new UserResponse(
                user.getId(),
                user.getName(), 
                user.getEmail(),
                new DepartmentResponse(user.getDepartment().getId(), user.getDepartment().getName()),
                userRepository.getUserPermission(user.getId()));     
    }

    @Override
    public UserResponse resetPassword(ForgetPasswordRequest request) {
      
        String token = request.getEmailSignedToken();
       
        String extractedEmail = JwtUtil.extractEmailFromToken(token);
        
        User user = userRepository.findByEmail(extractedEmail).orElseThrow(()->new ResourceNotFoundException("User not Found!"));
 
        String hashedPassword = BCrypt.hashpw(request.getPassword(),BCrypt.gensalt(10));
  
        String passwordResponse = userRepository.changePassword(extractedEmail, hashedPassword);
        
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                new DepartmentResponse(user.getDepartment().getId(), user.getDepartment().getName()),
                userRepository.getUserPermission(user.getId()));  
 
    }

  

}
