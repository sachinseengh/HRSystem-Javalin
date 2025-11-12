/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.permission;

import com.mycompany.employee_management.department.*;
import java.util.List;

/**
 *
 * @author sachi
 */
public interface PermissionService {
    
    
    public PermissionResponse createPermission(PermissionRequest request);
    
    public String deletePermission(int permissionId); 
    
    public List<PermissionResponse> getAllPermissions();
    
    
    PermissionResponse updatePermission(PermissionRequest request,int permissionId);
    
}
