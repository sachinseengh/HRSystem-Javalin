/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.Attendence;
 
import com.mycompany.employee_management.exception.DuplicateFoundException;
import com.mycompany.employee_management.exception.OperationFailedException;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author sachi
 */
public class AttendenceServiceImplementation implements AttendenceService {

    private final AttendenceRepository attendenceRepository = (AttendenceRepository) new AttendenceRepositoryImplementation();
    
    
    @Override
    public String checkin( int userId) {
        
        try{
        int attendenceId=attendenceRepository.checkin(userId);
        
        attendenceRepository.addUserAttendence(attendenceId, userId);
        
        }catch(Exception e){
            throw e;
        }
        
        return "Checked In SuccessFully";
        
        
    }

    @Override
    public String checkout( int userId) throws DuplicateFoundException {
       
        try{
            int attendenceId =attendenceRepository.checkout( userId);
            
            
        } catch (DuplicateFoundException e) {
          throw e;  
        } catch(Exception e){
            throw new OperationFailedException(e.getMessage(),500);
        }
        
        return "Checked Out SuccessFully";
    }

    @Override
    public List<AttendenceResponse> getAllAttendenceResponses(LocalDate date) {
       
        return attendenceRepository.getAllAttendence(date);
    }

    @Override
    public List<AttendenceResponse> getTodayAttendence() {
         
        return getAllAttendenceResponses(LocalDate.now());
    }

    @Override
    public List<AttendenceResponse> getUserAttendence(int userId) {
        
          return attendenceRepository.getUserAttendence(userId);
    }

    @Override
    public AttendenceResponse getUserTodaysAttendence(int userId) {
   
        return attendenceRepository.getuserTodaysCheckinCheckout(userId);
    }

   
    
}
