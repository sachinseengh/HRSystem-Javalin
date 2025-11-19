/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.user;

 
import com.mycompany.employee_management.department.Department;
import com.mycompany.employee_management.permission.Permission;
import java.util.ArrayList;
import java.util.List;

 
public class User {
    
    private int id;
    
    private String name;
    
    private String email;
    
    private String password;
    
    
    private Department department;    
            
    private List<Permission> permissions = new ArrayList<>();
    
    
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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
     
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String name, String email,  Department department, List<Permission> permissions) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.department = department;
        this.permissions = permissions;
    }

    public User(int id, String name, String email,Department department, List<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.department = department;
        this.permissions = permissions;
    }

    public User(String name, String email, String password, Department department, List<Permission> permissions) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.department = department;
        this.permissions = permissions;
    }

    public User(int id, String name, String email, String password, Department department, List<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.department = department;
        this.permissions = permissions;
    }
    
    
 
    public User() {
    }
    
    
    
    

    
    }

   
    
    
   
