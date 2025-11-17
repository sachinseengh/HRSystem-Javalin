/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.user;

import com.mycompany.employee_management.user.forgetPassword.SendForgetPasswordEmail;
import com.mycompany.employee_management.department.DepartmentResponse;
import com.mycompany.employee_management.permission.PermissionResponse;
import com.mycompany.employee_management.user.forgetPassword.ForgetPasswordRequest;
import com.mycompany.employee_management.user.loginPojo.LoginRequest;
import com.mycompany.employee_management.user.loginPojo.LoginResponse;
import com.mycompany.employee_management.user.logout.LogoutResponse;
import com.mycompany.employee_management.user.passwordChange.ChangePasswordRequest;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sachi
 */
public interface UserService {

    List<UserResponse> getAllUser();

    UserResponse createUser(UserRequest user);

    String deleteUser(int id);
    
    LoginResponse login(LoginRequest loginRequest);

    UserResponse updateUser(UserRequest userRequest, int id);
    
    List<PermissionResponse> getUserPermission(int userId);
     
     
    Map<String,Integer> getDashboardDetails();
    
    LogoutResponse logout();
    
    UserResponse changePassword(ChangePasswordRequest request);
    
    UserResponse resetPassword(ForgetPasswordRequest request);

    
    String sendForgetPasswordEmail(SendForgetPasswordEmail request);
            
}
