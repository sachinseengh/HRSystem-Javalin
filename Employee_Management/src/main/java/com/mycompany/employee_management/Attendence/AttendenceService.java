/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.Attendence;

 
import com.mycompany.employee_management.exception.DuplicateFoundException;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author sachi
 */
public interface AttendenceService {
    
    String checkin(int userId);
    
    String checkout(int userId) throws DuplicateFoundException;
    
    List<AttendenceResponse> getAllAttendenceResponses(LocalDate date);
    
    List<AttendenceResponse> getTodayAttendence();
    
    AttendenceResponse getUserTodaysAttendence(int userId);
     
    List<AttendenceResponse> getUserAttendence(int userId);
    
}
