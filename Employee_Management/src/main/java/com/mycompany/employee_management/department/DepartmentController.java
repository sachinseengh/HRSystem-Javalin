/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.department;

import com.mycompany.employee_management.MiddleWare.AuthMiddleWare;
import com.mycompany.employee_management.PermissionConstant.PermissionConstant;
import io.javalin.Javalin;

/**
 *
 * @author sachi
 */
public class DepartmentController {

    private final DepartmentService departmentService = new DepartmentServiceImplementation();

    public DepartmentController(Javalin app) {

        app.before("/department", AuthMiddleWare.requireLogin);

        app.post("/department", ctx -> {

            AuthMiddleWare.requirePermission(PermissionConstant.CREATE_DEPARTMENT).handle(ctx);

            DepartmentRequest body = ctx.bodyAsClass(DepartmentRequest.class);

            ctx.status(200).json(departmentService.createDepartment(body));

        });

        app.put("/department/{departmentId}", ctx -> {

            AuthMiddleWare.requirePermission(PermissionConstant.UPDATE_DEPARTMENT).handle(ctx);

            DepartmentRequest body = ctx.bodyAsClass(DepartmentRequest.class);

            ctx.status(200).json(departmentService.updateDepartment(body, Integer.parseInt(ctx.pathParam("departmentId"))));
        });

        app.delete("/department/{id}", ctx -> {

            AuthMiddleWare.requirePermission(PermissionConstant.DELETE_DEPARTMENT).handle(ctx);

            int departmentId = Integer.parseInt(ctx.pathParam("id"));

            ctx.status(200).json(departmentService.deleteDepartment(departmentId));
        });

        app.get("/department", ctx -> {
            
              AuthMiddleWare.requirePermission(PermissionConstant.READ_DEPARTMENT).handle(ctx);

            ctx.json(200).json(departmentService.getAllDepartments());
        });

    }

}
