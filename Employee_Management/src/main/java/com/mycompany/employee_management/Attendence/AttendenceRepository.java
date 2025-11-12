/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.Attendence;

 
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author sachi
 */
public interface AttendenceRepository {
    
    
    int checkin(int userId);
    
    int checkout(int userId);
    
    void addUserAttendence(int attendenceId,int userId);
    
    Optional<Attendence> checkTodaysCheckin(int userId);
    
    Optional<Attendence> checkTodaysCheckout(int userId);
    
    AttendenceResponse getuserTodaysCheckinCheckout(int userId);
   
    List<AttendenceResponse> getAllAttendence(LocalDate date);
    
    List<AttendenceResponse> getUserAttendence(int userId);
    
 
}
