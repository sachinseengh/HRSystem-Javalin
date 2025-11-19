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
 
    
    private String accessToken;
    
    private String refreshToken;

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

    public LoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
    
   
    public LoginResponse() {
        
    }
 
}
