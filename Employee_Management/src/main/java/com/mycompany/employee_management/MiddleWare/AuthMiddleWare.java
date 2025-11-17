/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.MiddleWare;

import com.mycompany.employee_management.exception.UnauthorizedException;
import com.mycompany.employee_management.security.JwtUtil;
import com.mycompany.employee_management.user.UserService;
import com.mycompany.employee_management.user.UserServiceImplementation;
import io.javalin.http.Handler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.List;

/**
 *
 * @author sachi
 */
public class AuthMiddleWare {
    
      private static final UserService userService = (UserService) new UserServiceImplementation();
 
       public static  Handler requireLogin = ctx->{
            
            String authHeader = ctx.header("Authorization");
            
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                
                throw new UnauthorizedException("No Jwt Token",401);
            }
            String token = authHeader.substring(7);
            
            try{
                Claims claims = JwtUtil.validateToken(token);
 
                //attach user info to context
               
                ctx.sessionAttribute("userId",(Integer.parseInt(claims.getSubject())));
                
                ctx.sessionAttribute("email",claims.get("email"));
                
                ctx.sessionAttribute("permissions",claims.get("permissions"));
                
            }catch(ExpiredJwtException e){
                
                throw new UnauthorizedException("Token Expired ",401);
                
            }catch(SignatureException e){
                throw new UnauthorizedException("Invalid token Signature ",401);
                
            }catch(MalformedJwtException e){
                throw new UnauthorizedException(" Malformed JWT Token ",401);
            }catch(JwtException e){
                throw new UnauthorizedException("JWT Exception ",401);
            }
             
        };
       
       
     // Check if user has a specific permission
    public static Handler requirePermission(String permissionName) {
        return ctx -> {
            
            requireLogin.handle(ctx);
 
            Integer userId = ctx.attribute("userId");
            if (userId == null) {
                throw new UnauthorizedException("UnAuthorized! Please Log in First!", 401);
            }
 
            List<String> permissions = ctx.attribute("permissions");
            
             
            boolean hasPermission = permissions.stream()
                    .anyMatch(p -> p.equalsIgnoreCase(permissionName));

            if (!hasPermission) {
                throw new UnauthorizedException("Forbidden! Not Allowed", 403);
            }
        };
    }
    
}
