/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.user;

 
import com.mycompany.employee_management.department.DepartmentResponse;
 
import com.mycompany.employee_management.permission.PermissionResponse;
import java.util.List;

/**
 *
 * @author sachi
 */
public class UserResponse {
    
    private int id;
    
    private String name;
    
    
    private String email;
    
    
    
    private DepartmentResponse department;
    
    
    private List<PermissionResponse> permission;
    
   

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DepartmentResponse getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentResponse department) {
        this.department = department;
    }

    public List<PermissionResponse> getPermission() {
        return permission;
    }

    public void setPermission(List<PermissionResponse> permission) {
        this.permission = permission;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserResponse(int id, String name, String email, DepartmentResponse department, List<PermissionResponse> permission) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.permission = permission;
    }
    
    public UserResponse(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
      
    }
 
    public UserResponse() {
    }
    
    
}
