/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.permission;

import com.mycompany.employee_management.department.*;
import com.mycompany.employee_management.exception.ResourceNotFoundException;
import java.util.List;

/**
 *
 * @author sachi
 */
public class PermissionServiceImplementation implements PermissionService {
    
   private final PermissionRepository permissionRepository =  new PermissionRepositoryImplementation();
    
    @Override
    public PermissionResponse createPermission(PermissionRequest request) {
         return permissionRepository.createPermission(request);
    }

    @Override
    public List<PermissionResponse> getAllPermissions() {
         
        return permissionRepository.getAllPermission();
    }

    @Override
    public String deletePermission(int permissionId) {
        
         Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new com.mycompany.employee_management.exception.ResourceNotFoundException("Permission not found with Id : " + permissionId));
         
 
         String response = permissionRepository.deletePermission(permissionId);
         
         return response;
        
    }

    @Override
    public PermissionResponse updatePermission(PermissionRequest request,int permissionId) {
        
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(()->new ResourceNotFoundException("Permission not found "));
        
        Permission updatedPermission = permissionRepository.updatePermission(request, permissionId);
       
        return new PermissionResponse(updatedPermission.getId(),updatedPermission.getName());
    }

     
    
}
