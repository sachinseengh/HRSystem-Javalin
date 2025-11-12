/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.department;

import com.mycompany.employee_management.config.DataBaseSourceClass;
import com.mycompany.employee_management.exception.OperationFailedException;
import com.mycompany.employee_management.user.UserResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author sachi
 */
public class DepartmentRepositoryImplementation implements DepartmentRepository {

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest departmentRequest) {

        String sql = "insert into department(name) values (?)";

        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            statement.setString(1, departmentRequest.getName());

            int affectedRow = statement.executeUpdate();

            if (affectedRow > 0) {

                ResultSet resultSet = statement.getGeneratedKeys();

                while (resultSet.next()) {
                    return new DepartmentResponse(resultSet.getInt(1), departmentRequest.getName());
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occured : " + e.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public List<DepartmentResponse> getAllDepartment() {

        String sql = "Select * from department order by id desc";

        try (
                Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement prepareStatement = conn.prepareStatement(sql)) {

            List<DepartmentResponse> responses = new ArrayList<>();

            ResultSet resultSet = prepareStatement.executeQuery();

            while (resultSet.next()) {
                responses.add(new DepartmentResponse(resultSet.getInt(1), resultSet.getString("name")));
            }
            return responses;

        } catch (Exception e) {
            System.out.println("Exception occured : " + e.getMessage());
            return null;
        }

    }

    @Override
    public String deleteDepartment(int departmentId) {

        String sql = "delete from department where id =?";

        try (
                Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, departmentId);
            int check = stmt.executeUpdate();

            if (check > 0) {
                return "Department deleted successfully";
            } else {
                return "Department deletion Failed!";
            }
            
        }catch(SQLIntegrityConstraintViolationException e){
            
            throw new OperationFailedException("Failed! Users belong to this departmemnt",403);

        } catch (SQLException ex) {
            System.out.println("Error :" + ex.getMessage());
            throw new OperationFailedException("Failed to Delete Department with Id :", departmentId);
        }

    }

    @Override
    public Optional<Department> findById(int departmentId) {

        String sql = "select * from department where id = ?";

        try (
                Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);) {
            
            stmt.setInt(1, departmentId);
            
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                return Optional.of(new Department(rs.getInt("id"),rs.getString("name")));
            }
            

        } catch (Exception e) {
            System.out.println("Exception occured !!!" + e.getMessage());
        }
        
        return Optional.empty();

    }

    @Override
    public Department updateDepartment(String name,int departmentId) {
        
         
        String sql ="update department set name=? where id=?";
        
        Department department = new Department();
        
        try(
                Connection conn = DataBaseSourceClass.getDataSource().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);){
                
                stmt.setString(1, name);
                stmt.setInt(2, departmentId);
                
                
                int affectedRow = stmt.executeUpdate();
                
                if(affectedRow>0){
                    department.setId(departmentId);
                    department.setName(name);
                    
                }
                return department;
 
        }catch(Exception e){
          
            throw new OperationFailedException("Internal Server Error",500);
              
        }
        
    }

}
