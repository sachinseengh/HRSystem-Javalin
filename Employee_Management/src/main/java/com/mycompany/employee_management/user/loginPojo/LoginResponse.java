/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.user.loginPojo;

import com.mycompany.employee_management.department.Department;
import com.mycompany.employee_management.department.DepartmentResponse;
import com.mycompany.employee_management.permission.PermissionResponse;
import java.util.List;

/**
 *
 * @author sachi
 */
public class LoginResponse {
        
        
    private int id;
    
    private String name;
        
    private String email;
    
   
    private DepartmentResponse department;
    
    private List<PermissionResponse> permissions;
    
    
    private String accessToken;
    
    private String refreshToken;

    public LoginResponse(int id, String email, String name, DepartmentResponse department, List<PermissionResponse> permissions, String accessToken, String refreshToken) {
        this.id=id;
        this.email=email;
        this.name=name;
        this.department=department;
        this.permissions=permissions;
        this.accessToken=accessToken;
        this.refreshToken=refreshToken;
 
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
   
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public DepartmentResponse getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentResponse department) {
        this.department = department;
    }

    public List<PermissionResponse> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionResponse> permissions) {
        this.permissions = permissions;
    }
 
    public LoginResponse() {
    }
 
}
