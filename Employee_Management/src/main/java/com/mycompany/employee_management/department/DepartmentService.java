/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.department;

import java.util.List;

/**
 *
 * @author sachi
 */
public interface DepartmentService {
    
    
    public DepartmentResponse createDepartment(DepartmentRequest request);
    
    
    public String deleteDepartment(int department_id);
    
    public List<DepartmentResponse> getAllDepartments();
    
    
    DepartmentResponse updateDepartment(DepartmentRequest request,int id);
    
}
