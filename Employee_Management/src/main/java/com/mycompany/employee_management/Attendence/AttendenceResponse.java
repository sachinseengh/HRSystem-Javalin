/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.Attendence;

import com.mycompany.employee_management.user.UserResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author sachi
 */
public class AttendenceResponse {
   
    private UserResponse user;
    
    private int id;
       
    private String checkin;
    
    
    private String checkout;
    
    
    private float workingHour;

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public float getWorkingHour() {
        return workingHour;
    }

    public void setWorkingHour(float workingHour) {
        this.workingHour = workingHour;
    }

    public AttendenceResponse(UserResponse user, int id, String checkin, String checkout, float workingHour) {
        this.user = user;
        this.id = id;
        this.checkin = checkin;
        this.checkout = checkout;
        this.workingHour = workingHour;
    }

     
 
    
    public AttendenceResponse() {
    }
    
 
    
}
