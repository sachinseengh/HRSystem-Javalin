/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/
package com.mycompany.employee_management.permission;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author sachi
 */

public interface PermissionRepository {
    
    Optional<Permission> findById(int permissionId); 
 
    public PermissionResponse createPermission(PermissionRequest permissionRequest);
    
    public List<PermissionResponse> getAllPermission();
    
    String deletePermission(int permissionId);
    
    
    Permission updatePermission(PermissionRequest request,int permissionId);
    
}



