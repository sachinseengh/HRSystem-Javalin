/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.department;

import com.mycompany.employee_management.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author sachi
 */
public class DepartmentServiceImplementation implements DepartmentService {
    
    
    private final DepartmentRepository departmentRepository = new DepartmentRepositoryImplementation();

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest request) {
         return departmentRepository.createDepartment(request);
    }

    @Override
    public List<DepartmentResponse> getAllDepartments() {
         
        return departmentRepository.getAllDepartment();
    }

    @Override
    public String deleteDepartment(int departmentId) {
        
         Department department = departmentRepository.findById(departmentId).orElseThrow(()->new ResourceNotFoundException("Department not found with id : "+ departmentId));
        
        String response = departmentRepository.deleteDepartment(department.getId());
        
        return response;
        
    }

    @Override
    public DepartmentResponse updateDepartment(DepartmentRequest request,int id) {

        Optional<Department> department = departmentRepository.findById(id);
        
        if(department.isEmpty()){
            throw new ResourceNotFoundException("Department not Found !");
        }

        Department updatedDepartment = departmentRepository.updateDepartment(request.getName(),id);
        
        return new DepartmentResponse(updatedDepartment.getId(),updatedDepartment.getName());

    }
}
