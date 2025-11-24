/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.user;

import com.mycompany.employee_management.config.DataBaseSourceClass;
import com.mycompany.employee_management.department.Department;
import com.mycompany.employee_management.department.DepartmentRepository;
import com.mycompany.employee_management.department.DepartmentRepositoryImplementation;
import com.mycompany.employee_management.department.DepartmentResponse;
import com.mycompany.employee_management.exception.OperationFailedException;
import com.mycompany.employee_management.exception.ResourceNotFoundException;
import com.mycompany.employee_management.permission.Permission;
import com.mycompany.employee_management.permission.PermissionRepository;
import com.mycompany.employee_management.permission.PermissionRepositoryImplementation;
import com.mycompany.employee_management.permission.PermissionResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author sachi
 */
public class UserRepositoryImplementation implements UserRepository {

    private final Logger log = LogManager.getLogger(UserRepositoryImplementation.class);

    private final DepartmentRepository departmentRepository = (DepartmentRepository) new DepartmentRepositoryImplementation();

    private final PermissionRepository permissionRepository = (PermissionRepository) new PermissionRepositoryImplementation();

    @Override
    public User save(User user) {

        log.info("employee insertion initiated.");

        String sql = "insert into users(name,email,password,department_id,profile_image) values (?,?,?,?,?) ";

        int userId = -1;

        UserResponse userResponse = new UserResponse();

        try (
                Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
           
            stmt.setInt(4, user.getDepartment().getId());
             stmt.setString(5,user.getProfileImage());

            int rowAffected = stmt.executeUpdate();

            if (!(rowAffected > 0)) {
                log.error("cannot insert employee");
                throw new OperationFailedException("Operation Failed ", 500);
            }

            try (ResultSet keySet = stmt.getGeneratedKeys()) {

                if (keySet.next()) {

                    return new User(
                            keySet.getInt(1),
                            user.getName(),
                            user.getEmail(),
                            user.getDepartment(),
                            List.of(),
                            user.getProfileImage()
                    );
                }
            } catch (Exception e) {
                throw new ResourceNotFoundException(e.getMessage());
            }

        } catch (Exception e) {

            throw new OperationFailedException(e.getMessage(), 500);
        }
        return null;

    }

    @Override
    public String delete(int id) {

        String sql = "delete from users where id =?";

        try (
                Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int check = stmt.executeUpdate();

            if (check > 0) {
                return "User deleted successfully";
            } else {
                return "User deletion Failed!";
            }

        } catch (SQLException ex) {
            System.out.println("Error :" + ex.getMessage());
            return "User deletion Failed !";
        }
    }

    @Override
    public User update(User user, int id) {

        String updateSql = "update users set name = ?,department_id = ? where id=?";
        try (
                Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(updateSql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getName());
            stmt.setInt(2, user.getDepartment().getId());
            stmt.setInt(3, id);

            int updatedRow = stmt.executeUpdate();

            if (!(updatedRow > 0)) {
                System.out.println("Insertion Failed !!! : ");

                log.error("Failed to insert!!");

                 throw new OperationFailedException("Failed to Add User",500);
            }

            return new User(
                    id,
                    user.getName(),
                    user.getEmail(),
                    user.getDepartment(),
                    List.of(),
                    user.getProfileImage()
            );

        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());

            return null;
        }

    }

    @Override
    public List<UserResponse> getAllUser() {

        String sql = "select u.id as user_id ,u.name as user_name, u.email as user_email,\n"
                + "d.id as department_id ,d.name as department_name,\n"
                + "p.id as permission_id, p.name as permission_name,\n"
                +"p.section as permission_section,p.description as permission_description\n"
                + "from users u join department d on u.department_id=d.id left join users_has_permission up on u.id=up.users_id left join permission p on p.id=up.permission_id order by u.id desc";

        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery();) {

            List<UserResponse> users = new ArrayList<>();

            UserResponse lastUser = null;

            while (rs.next()) {

                int userId = rs.getInt("user_id");

                if (lastUser == null || lastUser.getId() != userId) {

                    lastUser = new UserResponse();

                    lastUser.setId(userId);
                    lastUser.setName(rs.getString("user_name"));
                    lastUser.setEmail(rs.getString("user_email"));

                    lastUser.setDepartment(new DepartmentResponse(
                            rs.getInt("department_id"),
                            rs.getString("department_name")
                    ));

                    lastUser.setPermission(new ArrayList<>());

                    users.add(lastUser);
                }

                int permId = rs.getInt("permission_id");

                if (!rs.wasNull()) {

                    PermissionResponse permResponses = new PermissionResponse(
                            permId,
                            rs.getString("permission_name"),
                            rs.getString("permission_section"),
                            rs.getString("permission_description")
                    );

                    lastUser.getPermission().add(permResponses);
                }
            }

            return users;

        } catch (Exception e) {

            throw new OperationFailedException(e.getMessage(), 500);
        }

    }

    @Override
    public void addNewUserPermissions(List<Integer> permission, int userId) {

        String sql = "insert into users_has_permission(users_id,permission_id) values (?,?)";

        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int permId : permission) {

                stmt.setInt(1, userId);
                stmt.setInt(2, permId);

                int rowAffected = stmt.executeUpdate();

                if (!(rowAffected > 0)) {

                     throw new OperationFailedException("Failed to add users_has_permission", 500);
                }
            }

        } catch (Exception e) {
             throw new OperationFailedException("Invalid permission tried to add!", 500);
        }

    }

    @Override
    public void deleteUserPermissions(List<Integer> permission, int userId) {

        String sql = "delete from users_has_permission where permission_id = ? and users_id = ?";

        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int permId : permission) {

                stmt.setInt(1, permId);
                stmt.setInt(2, userId);

                int rowAffected = stmt.executeUpdate();

                if (!(rowAffected > 0)) {

                    System.out.println("Failed to Delete ");
//                     I will throw exception here
                }

            }

        } catch (Exception e) {
            System.out.println("Exception Caught : " + e.getMessage());
        }
    }

    @Override
    public List<Integer> getUserOldPermissionId(int userId) {

        String sql = "select * from users_has_permission where users_id=?";

        List<Integer> permissionsId = new ArrayList<>();

        try (
                Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            PreparedStatement permStmt = conn.prepareStatement(sql);

            permStmt.setInt(1, userId);

            ResultSet res = permStmt.executeQuery();

            while (res.next()) {

                permissionsId.add(res.getInt("permission_id"));
            }
            return permissionsId;

        } catch (Exception e) {
            System.out.println("Exception occured : " + e.getMessage());
            return null;
        }
    }

    @Override
    public void checkAndAddThePermissionToAdd(List<Integer> permission, List<Integer> oldPermission, int userId) {

        List<Integer> permissionToAdd = new ArrayList<>();

        boolean checkCommon = false;

        for (int newPerm : permission) {

            for (int oldPerm : oldPermission) {

                if (newPerm == oldPerm) {
                    checkCommon = true;
                }
            }

            if (checkCommon == false) {
                permissionToAdd.add(newPerm);
            }
            checkCommon = false;
        }

        addNewUserPermissions(permissionToAdd, userId);

    }

    @Override
    public void checkAndRemoveThePermissionToRemove(List<Integer> permission, List<Integer> oldPermission, int userId) {

        List<Integer> permissionToRemove = new ArrayList<>();

        boolean checkCommon = false;

        for (int oldPerm : oldPermission) {

            for (int newPerm : permission) {

                if (oldPerm == newPerm) {
                    checkCommon = true;
                }

            }

            if (checkCommon == false) {
                permissionToRemove.add(oldPerm);
            }

            checkCommon = false;

        }

        deleteUserPermissions(permissionToRemove, userId);

    }

    @Override
    public List<PermissionResponse> getUserPermission(int userId) {

        String sql = " SELECT * FROM users_has_permission up join permission p on p.id=up.permission_id where users_id=?";

        List<PermissionResponse> permissions = new ArrayList<>();

        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                permissions.add(new PermissionResponse(rs.getInt("permission_id"), rs.getString("name"),rs.getString("section"),rs.getString("description")));

            }

            return permissions;

        } catch (Exception e) {
            System.out.println("Exception occured : " + e.getMessage());

            return null;
        }

    }

    @Override
    public Department getUserDepartment(int departmentId) {

        String sql = "select * from department where id = ?";

        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setInt(1, departmentId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                return new Department(rs.getInt("id"), rs.getString("name"));
            }

        } catch (Exception e) {
            System.out.println("Exception occured : " + e.getMessage());
            return null;
        }
        return null;
    }
@Override
public Optional<User> findById(int userId) {
    String sql = "SELECT u.id AS user_id, u.name AS user_name, u.email AS user_email, u.password AS user_password,u.profile_image AS profile_image, "
               + "p.id AS permission_id, p.name AS permission_name, d.id AS department_id, d.name AS department_name "
               + "FROM users u "
               + "JOIN department d ON u.department_id=d.id "
               + "LEFT JOIN users_has_permission up ON up.users_id=u.id "
               + "LEFT JOIN permission p ON up.permission_id=p.id "
               + "WHERE u.id=?";

    Optional<User> user = Optional.empty();

    try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();

        List<Permission> permissionList = new ArrayList<>();

        while (rs.next()) {
            if (!user.isPresent()) {
                Optional<Department> deptOpt = departmentRepository.findById(rs.getInt("department_id"));
                Department dept = deptOpt.orElseThrow(() -> new ResourceNotFoundException("Department not found"));

                user = Optional.of(new User(
                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("user_email"),
                        rs.getString("user_password"),
                        dept,
                        new ArrayList<>(),
                        rs.getString("profile_image")
                ));
            }

            int permId = rs.getInt("permission_id");
            if (!rs.wasNull()) {
                Permission permission = new Permission();
                permission.setId(permId);
                permission.setName(rs.getString("permission_name"));
                permissionList.add(permission);
            }
        }

        user.ifPresent(u -> u.setPermissions(permissionList));
        return user;

    } catch (SQLException e) {
        throw new OperationFailedException(e.getMessage(), 500);
    }
}


@Override
public Optional<User> findByEmail(String email) {
    
    
        String sql = "SELECT u.id AS user_id, u.name AS user_name, u.email AS user_email, u.password AS user_password,u.profile_image AS profile_image ," +
                     "p.id AS permission_id, p.name AS permission_name, d.id AS department_id, d.name AS department_name " +
                     "FROM users u " +
                     "JOIN department d ON u.department_id = d.id " +
                     "LEFT JOIN users_has_permission up ON up.users_id = u.id " +
                     "LEFT JOIN permission p ON up.permission_id = p.id " +
                     "WHERE u.email = ?";

    Optional<User> user = Optional.empty(); 
    System.out.println("user--------->"+user);
                            
    List<Permission> permissionList = new ArrayList<>();

    try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            if (user.isEmpty()) {
                Optional<Department> departmentOpt = departmentRepository.findById(rs.getInt("department_id"));
                Department department = departmentOpt.orElseThrow(() ->
                        new ResourceNotFoundException("Department not found for user"));

                user = Optional.of(new User(
                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("user_email"),
                        rs.getString("user_password"),
                        department,
                        new ArrayList<>(),
                        rs.getString("profile_image")
                ));
            }

            int permissionId = rs.getInt("permission_id");
            if (!rs.wasNull()) {
                Permission permission = new Permission();
                permission.setId(permissionId);
                permission.setName(rs.getString("permission_name"));
                permissionList.add(permission);
            }
        }
        
         System.out.println("user2--------->"+user);

        // ✅ safely set permissions if user was found
        user.ifPresent(u -> u.setPermissions(permissionList));

        return user;

    } catch (Exception e) {
        throw new OperationFailedException(e.getMessage(), 500);
    }
}

    @Override
    public Map<String, Integer> getDashboardData() {
        String sql = "SELECT\n"
                + "  (SELECT COUNT(id) FROM ems.users) AS userCount,\n"
                + "  (SELECT COUNT(id) FROM ems.department) AS departmentCount,\n"
                + "  (SELECT COUNT(id) FROM ems.permission) AS permissionCount;";

        Map<String, Integer> map = new HashMap<>();

        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                map.put("userCount", rs.getInt("userCount"));
                map.put("departmentCount", rs.getInt("departmentCount"));
                map.put("permissionCount", rs.getInt("permissionCount"));
            }

            return map;

        } catch (Exception e) {

            throw new OperationFailedException("Error" + e, 500);

        }

    }

    @Override
    public String changePassword(String email,String password) {

        String sql ="update users set password = ? where email=?";
        
        try(Connection conn = DataBaseSourceClass.getDataSource().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);){
            
            
            stmt.setString(1, password);
            stmt.setString(2,email);


             int affectedRow = stmt.executeUpdate();
             if(affectedRow<0){
                 throw new OperationFailedException("Update Query Failed !",500);
             }
             
             return password;
            
            
            
        }catch(Exception e){
            throw new OperationFailedException(e.getMessage(),500);
        }
        
    }
}
