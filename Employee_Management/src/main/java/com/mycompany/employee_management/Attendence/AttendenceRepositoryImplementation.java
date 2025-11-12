/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.Attendence;

 
import com.mycompany.employee_management.config.DataBaseSourceClass;
import com.mycompany.employee_management.exception.DuplicateFoundException;
import com.mycompany.employee_management.exception.OperationFailedException;
import com.mycompany.employee_management.exception.ResourceNotFoundException;
import com.mycompany.employee_management.user.UserResponse;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author sachi
 */
public class AttendenceRepositoryImplementation implements AttendenceRepository {

    @Override
    public int checkin( int userId) {

        Optional<Attendence> todaysCheckin = checkTodaysCheckin(userId);

        if (todaysCheckin.isPresent()) {

            throw new DuplicateFoundException("Already Checked In");
        }

        String sql = "insert into attendence (checkin) values (?);";

        try (
                Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1,Timestamp.valueOf(LocalDateTime.now()));
             
            int affectedRow = stmt.executeUpdate();

            if (!(affectedRow > 0)) {
                throw new OperationFailedException("Failed to insert", 500);
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }

            } catch (Exception e) {
                throw new ResourceNotFoundException(e.getMessage());
            }

        } catch (Exception e) {
            throw new OperationFailedException(e.getMessage(), 500);
        }

        return -1;
    }

    @Override
    public int checkout(  int userId) {

        Optional<Attendence> todaysCheckin = checkTodaysCheckin(userId);

        if (todaysCheckin.isEmpty()) {

            throw new ResourceNotFoundException("CheckIn First");
        }
        
        Optional<Attendence> todaysCheckout = checkTodaysCheckout(userId);

        if (todaysCheckout.isPresent()) {

            throw new DuplicateFoundException("Already Checked Out");
        }
        

        String fetchAttendenceId = "SELECT ua.attendence_id AS attendenceId " +
                           "FROM users_has_attendence ua " +
                           "JOIN attendence a ON ua.attendence_id = a.id " +
                           "WHERE ua.users_id = ? AND DATE(a.checkin)= ?";


        String insertCheckoutSql = "update  attendence set checkout= ? where DATE(checkin)=? and id=?";

        
        
        //Fetching 
        int attendenceId = -1;

        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(fetchAttendenceId);) {

           
            
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
          
            
            System.out.println(stmt.toString());

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {

                attendenceId = rs.getInt("attendenceId");

            }  

        } catch (Exception e) {
            throw new OperationFailedException(e.getMessage(), 500);
        }

        if (attendenceId == -1) {
            throw new ResourceNotFoundException("No Checkin Data");
        }

        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(insertCheckoutSql);) {

            stmt.setTimestamp(1,Timestamp.valueOf(LocalDateTime.now()));
            
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, attendenceId);

            int affectedRow = stmt.executeUpdate();

            if (affectedRow < 0) {
                throw new ResourceNotFoundException("No Checkin Data");
            }

            return attendenceId;

        } catch (Exception e) {
            throw new OperationFailedException(e.getMessage(), 500);
        }

    }

    @Override
    public List<AttendenceResponse> getAllAttendence(LocalDate date) {
       
        String sql = "select a.id as attendenceId, a.checkin as checkinTime,a.checkout as checkoutTime,a.working_hr as workingHour,u.id as user_id,u.name as user_name from attendence a join users_has_attendence ua on ua.attendence_id=a.id join users u on u.id=ua.users_id where DATE(a.checkin)=? order by a.checkin desc";
        
        List<AttendenceResponse> responses = new ArrayList<>();
        
        
        try(Connection conn = DataBaseSourceClass.getDataSource().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);){
 
              stmt.setDate(1,Date.valueOf(date));
              
              System.out.println(stmt.toString());
                       
              ResultSet rs = stmt.executeQuery();
         
              while(rs.next()){
                  
                  UserResponse userResponse = new UserResponse();
                  
                  userResponse.setId(rs.getInt("user_id"));
                  userResponse.setName(rs.getString("user_name"));
                  
                  AttendenceResponse attendenceResponse = new AttendenceResponse();
                  
                  attendenceResponse.setUser(userResponse);
                  
                
                  attendenceResponse.setCheckin(rs.getString("checkinTime"));
                  attendenceResponse.setCheckout(rs.getString("checkoutTime"));
                
                  attendenceResponse.setId(rs.getInt("attendenceId"));
                  attendenceResponse.setWorkingHour(rs.getFloat("workingHour"));
            
                  responses.add(attendenceResponse);
              }
              
              return responses;
            
        }catch(Exception e){
            throw new OperationFailedException(e.getMessage(),500); 
        }
  
    }

    

    @Override
    public List<AttendenceResponse> getUserAttendence(int userId) {
              String sql = "select a.id as attendenceId, a.checkin as checkinTime,a.checkout as checkoutTime,a.working_hr as workingHour,u.id as user_id,u.name as user_name from attendence a join users_has_attendence ua on ua.attendence_id=a.id join users u on u.id=ua.users_id where u.id=? order by a.checkin desc";
              
              
                 List<AttendenceResponse> responses = new ArrayList<>();
              
              try(Connection conn = DataBaseSourceClass.getDataSource().getConnection();
                      PreparedStatement stmt = conn.prepareStatement(sql);){
                  
                  
                  stmt.setInt(1, userId);
                  
                  System.out.println("----------------->"+stmt.toString());
                  
                  
                  ResultSet rs = stmt.executeQuery();
                  
                  
                  while(rs.next()){
                      
                      
                  UserResponse userResponse = new UserResponse();
                  
                  userResponse.setId(rs.getInt("user_id"));
                  userResponse.setName(rs.getString("user_name"));
                  
                  AttendenceResponse attendenceResponse = new AttendenceResponse();
                  
                  attendenceResponse.setUser(userResponse);
                  
                
                  attendenceResponse.setCheckin(rs.getString("checkinTime"));
                  attendenceResponse.setCheckout(rs.getString("checkoutTime"));
                
                  attendenceResponse.setId(rs.getInt("attendenceId"));
                  attendenceResponse.setWorkingHour(rs.getFloat("workingHour"));
            
                  responses.add(attendenceResponse);
                      
                  }
                return responses;
                  
                  
              }catch(Exception e){
                  throw new OperationFailedException(e.getMessage(),500);
              }
              
    }

    @Override
    public void addUserAttendence(int attendenceId, int userId) {

        String sql = "insert into users_has_attendence(users_id,attendence_id) values (?,?)";

        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, attendenceId);

            int affectedRow = stmt.executeUpdate();

            if (!(affectedRow > 0)) {
                throw new OperationFailedException("Internal Server Error", 500);
            }

        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public Optional<Attendence> checkTodaysCheckin(int userId) {

        String fetchCheckinToday = "select distinct a.checkin as checkin_time from users u join"
                + " users_has_attendence ua on ua.users_id = u.id left join attendence a on a.id = ua.attendence_id where DATE(a.checkin)=? and ua.users_id=?";

        Attendence attendence = new Attendence();

        try (
                Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(fetchCheckinToday);) {

           
             stmt.setDate(1,Date.valueOf(LocalDate.now()));
             stmt.setInt(2, userId);

 

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LocalDateTime checkin = rs.getTimestamp("checkin_time") != null
                        ? rs.getTimestamp("checkin_time").toLocalDateTime()
                        : null;
                attendence.setCheckin(checkin);
                return Optional.of(attendence);
            }
            
            return Optional.empty();

            

        } catch (Exception e) {
            throw new OperationFailedException(e.getMessage(), 500);
        }

    }

    
    @Override
    public Optional<Attendence> checkTodaysCheckout(int userId) {

        String fetchCheckinToday = "select distinct a.checkout as checkout_time from users u join"
                + " users_has_attendence ua on ua.users_id =u.id left join attendence a on a.id = ua.attendence_id where DATE(a.checkout)=? and ua.users_id=?";

        Attendence attendence = new Attendence();

        try (
                Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(fetchCheckinToday);) {

           
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
             stmt.setInt(2, userId);

 

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                
                Timestamp checkout = rs.getTimestamp("checkout_time");
                
                if (checkout != null) {  
                    Attendence att = new Attendence();
                    att.setCheckout(checkout.toLocalDateTime());
                    return Optional.of(att);
                }
            }
            
            return Optional.empty();

            

        } catch (Exception e) {
            throw new OperationFailedException(e.getMessage(), 500);
        }

    }

    @Override
    public AttendenceResponse getuserTodaysCheckinCheckout(int userId) {
          String sql = "select a.id as attendenceId, a.checkin as checkinTime,a.checkout as checkoutTime,a.working_hr as workingHour,u.id as user_id,u.name as user_name from attendence a join users_has_attendence ua on ua.attendence_id=a.id join users u on u.id=ua.users_id where u.id=? "
                  + "and (DATE(a.checkin)=? or DATE(a.checkout)=?)";
              
             
                  AttendenceResponse attendenceResponse = new AttendenceResponse();
   
              try(Connection conn = DataBaseSourceClass.getDataSource().getConnection();
                      PreparedStatement stmt = conn.prepareStatement(sql);){
                  
                  
                  stmt.setInt(1, userId);
                  stmt.setDate(2, Date.valueOf(LocalDate.now()));
                   stmt.setDate(3, Date.valueOf(LocalDate.now()));
                  
                  
                  System.out.println("--------->"+stmt.toString());
                  
                  ResultSet rs = stmt.executeQuery();
                  
                  
                  if(rs.next()){
                      
                      
                  UserResponse userResponse = new UserResponse();
                  
                  userResponse.setId(rs.getInt("user_id"));
                  userResponse.setName(rs.getString("user_name"));
       
                  attendenceResponse.setUser(userResponse);
                           
                  attendenceResponse.setCheckin(rs.getString("checkinTime"));
                  attendenceResponse.setCheckout(rs.getString("checkoutTime"));
                
                  attendenceResponse.setId(rs.getInt("attendenceId"));
                  attendenceResponse.setWorkingHour(rs.getFloat("workingHour"));
   
                      
                  }
                return attendenceResponse;
                  
                  
              }catch(Exception e){
                  throw new OperationFailedException(e.getMessage(),500);
              }
    }

   
         

}
