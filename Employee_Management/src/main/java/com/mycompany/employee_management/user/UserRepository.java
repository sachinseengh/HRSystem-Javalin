/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.user;

import com.mycompany.employee_management.department.Department;
import com.mycompany.employee_management.permission.PermissionResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author sachi
 */
public interface UserRepository {
 
    Optional<User> findById(int user_id);
 
    Optional<User> findByEmail(String email);
  
    List<UserResponse> getAllUser();
    
    User save(User user);
 
    String delete(int id);
 
    User update(User user,int id);
 
    void addNewUserPermissions(List<Integer> permission,int userId);
    
    void deleteUserPermissions(List<Integer> permission,int userId);

    Department getUserDepartment(int departmentId);
    
    List<Integer> getUserOldPermissionId(int userId);

    void checkAndAddThePermissionToAdd(List<Integer> permission, List<Integer> oldPermission, int userId);

    void checkAndRemoveThePermissionToRemove(List<Integer> permission, List<Integer> oldPermission, int userId);

 
    List<PermissionResponse> getUserPermission(int userId);
    
    
    
      Map<String,Integer> getDashboardData();
    
    
}
