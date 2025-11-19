/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.Attendence;
 
import com.mycompany.employee_management.MiddleWare.AuthMiddleWare;
import com.mycompany.employee_management.PermissionConstant.PermissionConstant;
import com.mycompany.employee_management.exception.ResourceNotFoundException;
import com.mycompany.employee_management.exception.UnauthorizedException;
import io.javalin.Javalin;
import java.time.LocalDate;
import java.util.Map;

/**
 *
 * @author sachi
 */
public class AttendenceController {
    
    private AttendenceService attendenceService = (AttendenceService) new AttendenceServiceImplementation(); 
    
    public AttendenceController(Javalin app){
        
        
        app.before("/attendence/*",AuthMiddleWare.requireLogin);
   
        
        app.post("/attendence/checkin", ctx->{
            
              AuthMiddleWare.requirePermission(PermissionConstant.CHECKIN).handle(ctx);
        
        int userId =ctx.sessionAttribute("userId");
        
        ctx.json(attendenceService.checkin(userId)).status(200);
        
        });
        
        
        app.post("/attendence/checkout",ctx->{
            
           AuthMiddleWare.requirePermission(PermissionConstant.CHECKOUT).handle(ctx);
            
            int userId =ctx.sessionAttribute("userId");
            
            ctx.json(attendenceService.checkout(userId)).status(200);
            
        });
        
        
        app.get("/attendence",ctx->{
            
              AuthMiddleWare.requirePermission(PermissionConstant.READ_ATTENDENCE_REPORT).handle(ctx);
            
            String date = ctx.queryParam("date");
            
            if (date == null) {
                ctx.status(400).json(Map.of("error", "Date is required."));
                return;
            }
            
            LocalDate localDate = LocalDate.parse(date);
            
            ctx.json(attendenceService.getAllAttendenceResponses(localDate)).status(200);
            
        });
        
        app.get("/attendence/me", ctx->{
            
              AuthMiddleWare.requirePermission(PermissionConstant.READ_ATTENDENCE).handle(ctx);
        
        int userId = ctx.sessionAttribute("userId");
        
        
        ctx.json(attendenceService.getUserAttendence(userId)).status(200);
        
        });
        
        app.get("/attendence/me/today", ctx->{
        
        int userId = ctx.sessionAttribute("userId");
        
        
        ctx.json(attendenceService.getUserTodaysAttendence(userId)).status(200);
        
        });
        
         
        app.get("/attendence/user", ctx->{
        
        String userId = ctx.queryParam("userId");
        
        if(userId == null){
             ctx.status(400).json(Map.of("error", "User not found."));
            
            return;
        }
       
        ctx.json(attendenceService.getUserAttendence(Integer.parseInt(userId))).status(200);
        
        });
        
   
        
    }
    
}
