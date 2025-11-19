/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.permission;

import com.mycompany.employee_management.department.*;

/**
 *
 * @author sachi
 */
public class PermissionRequest {
    
    
    private int id;
    
    private String name;
    
    private String section;
    
    private String description;

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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PermissionRequest(int id, String name, String section, String description) {
        this.id = id;
        this.name = name;
        this.section = section;
        this.description = description;
    }

    public PermissionRequest() {
    }
  
}
