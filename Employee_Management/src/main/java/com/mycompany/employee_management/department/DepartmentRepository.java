/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.department;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author sachi
 */
public interface DepartmentRepository {
    
   
    public DepartmentResponse createDepartment(DepartmentRequest departmentRequest);
    
    Department updateDepartment(String name,int departmentId);
   
    public String deleteDepartment(int departmentId);
    
    public List<DepartmentResponse> getAllDepartment();
    
    
    Optional<Department> findById(int departmentId);
    
}
