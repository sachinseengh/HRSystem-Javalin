/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.Attendence;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author sachi
 */
public class Attendence {
    
    private int id;
    
   
    
    private LocalDateTime checkin;
    
    private LocalDateTime checkout;
    
    
    private float workingHour;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCheckin() {
        return checkin;
    }

    public void setCheckin(LocalDateTime checkin) {
        this.checkin = checkin;
    }

    public LocalDateTime getCheckout() {
        return checkout;
    }

    public void setCheckout(LocalDateTime checkout) {
        this.checkout = checkout;
    }

    public float getWorkingHour() {
        return workingHour;
    }

    public void setWorkingHour(float workingHour) {
        this.workingHour = workingHour;
    }

    public Attendence(int id, LocalDateTime checkin, LocalDateTime checkout, float workingHour) {
        this.id = id;
        this.checkin = checkin;
        this.checkout = checkout;
        this.workingHour = workingHour;
    }

   
    
 
    public Attendence() {
    }
    
}
