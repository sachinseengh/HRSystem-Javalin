/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.department;

import io.javalin.Javalin;

/**
 *
 * @author sachi
 */
public class DepartmentController {
    
    private final DepartmentService departmentService = new DepartmentServiceImplementation();

    public DepartmentController(Javalin app) {
        
        
        app.post("/department",ctx->{
            
            DepartmentRequest body = ctx.bodyAsClass(DepartmentRequest.class);
            
            ctx.status(200).json(departmentService.createDepartment(body));
            
        });
        
        
        app.put("/department/{departmentId}",ctx->{
            DepartmentRequest body = ctx.bodyAsClass(DepartmentRequest.class);
            
            ctx.status(200).json(departmentService.updateDepartment(body,Integer.parseInt(ctx.pathParam("departmentId"))));
        });
        
        
        app.delete("/department/{id}",ctx->{
            
            int departmentId =Integer.parseInt(ctx.pathParam("id"));
            
            ctx.status(200).json(departmentService.deleteDepartment(departmentId));
        });
        
        
        app.get("/department",ctx->{
            
            ctx.json(200).json(departmentService.getAllDepartments());
        });

    }

}
