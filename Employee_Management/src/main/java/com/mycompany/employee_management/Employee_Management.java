/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.employee_management;

 
import com.mycompany.employee_management.Attendence.AttendenceController;
import com.mycompany.employee_management.config.AppConfig;
import com.mycompany.employee_management.department.DepartmentController;
import com.mycompany.employee_management.exception.AppExceptionHandler;
import com.mycompany.employee_management.exception.UnauthorizedException;
import com.mycompany.employee_management.permission.PermissionController;
import com.mycompany.employee_management.permission.PermissionResponse;
import com.mycompany.employee_management.user.UserController;
import com.mycompany.employee_management.user.UserService;
import com.mycompany.employee_management.user.UserServiceImplementation;
import io.javalin.Javalin;
import io.javalin.http.Handler; 
import io.javalin.http.staticfiles.Location;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import io.javalin.validation.ValidationException;
import java.util.List;
import java.util.Map;
 
/**
 *
 * @author sachi
 */
public class Employee_Management {
    
    public static void main(String[] args) {
   
       
        var app = Javalin.create(config->{
        
        config.registerPlugin(new OpenApiPlugin(pluginConfig -> {
                pluginConfig.withDefinitionConfiguration((version, definition) -> {
                    definition.withOpenApiInfo(info -> info.setTitle("Javalin OpenAPI example"));
                });
            }));
            config.registerPlugin(new SwaggerPlugin());
            config.registerPlugin(new ReDocPlugin());
            
         config.staticFiles.add(staticFileConfig -> {
        staticFileConfig.hostedPath = "/uploads"; // URL prefix
        staticFileConfig.directory = "uploads";   // folder on disk
        staticFileConfig.location = Location.EXTERNAL; // external folder
    });
        
        
        }).start(7071);
        
        
        
        app.exception(ValidationException.class,(e,ctx)->{
            
            ctx.json(400);
            ctx.json(Map.of(
            
            "error","Validation error",
                    "details",e.getErrors()
            
            ));
        });
        
        app.get("/",ctx->
        {
        ctx.html("<h1> Welcome to Employee Management System </h1>");
        });
        
        
        
        new AppConfig(app);
        new UserController(app);
        
        new AttendenceController(app);
        
        new DepartmentController(app);
        
        new AppExceptionHandler(app);
        
        
        new PermissionController(app);
         
    }
    
    
   
    
}
