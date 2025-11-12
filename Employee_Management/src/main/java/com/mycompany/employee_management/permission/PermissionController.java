/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.permission;

import com.mycompany.employee_management.department.*;
import io.javalin.Javalin;

/**
 *
 * @author sachi
 */
public class PermissionController {


    private final PermissionService permissionService = new PermissionServiceImplementation();

    public PermissionController(Javalin app) {
        
        
        
        app.post("/permission",ctx->{
            
            PermissionRequest body = ctx.bodyAsClass(PermissionRequest.class);
            
            ctx.status(200).json(permissionService.createPermission(body));
            
        });
        
        
        app.delete("/permission/{permId}",ctx->{
        
            int permId = ctx.pathParamAsClass("permId",Integer.class).get();
            
           ctx.status(200).json(permissionService.deletePermission(permId));
        
        });
        
        app.put("/permission",ctx->{
            
            
            int permissionId = Integer.parseInt(ctx.queryParam("permission_id"));
            
            PermissionRequest body = ctx.bodyAsClass(PermissionRequest.class);
            
            ctx.status(200).json(permissionService.updatePermission(body, permissionId));
            
        });
                
        
        app.get("/permission",ctx->{
            
            ctx.json(200).json(permissionService.getAllPermissions());
        });

    }

}
