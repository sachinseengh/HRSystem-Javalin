/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employee_management.permission;

 
import com.mycompany.employee_management.config.DataBaseSourceClass;
import com.mycompany.employee_management.exception.DuplicateFoundException;
import com.mycompany.employee_management.exception.OperationFailedException;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author sachi
 */
public class PermissionRepositoryImplementation implements PermissionRepository {

    @Override
    public PermissionResponse createPermission(PermissionRequest permissionRequest) {
        
        Optional<Permission> permission = findById(permissionRequest.getId());
        
        if(permission .isPresent()){
            throw new DuplicateFoundException("Permission Id already Exists !");
        }

        String sql = "insert into permission(id,name,section,department) values (?,?,?,?)";

        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            statement.setString(1, permissionRequest.getName());
            statement.setString(2, permissionRequest.getSection());
            statement.setString(3, permissionRequest.getDescription());

            int affectedRow = statement.executeUpdate();

            if (affectedRow > 0) {

                ResultSet resultSet = statement.getGeneratedKeys();

                while (resultSet.next()) {
                    return new PermissionResponse(resultSet.getInt(1), permissionRequest.getName(),permissionRequest.getSection(),permissionRequest.getDescription());
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occured : " + e.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public List<PermissionResponse> getAllPermission() {

        String sql = "Select * from permission order by id desc";

        try (
                Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement prepareStatement = conn.prepareStatement(sql)) {

            List<PermissionResponse> responses = new ArrayList<>();

            ResultSet resultSet = prepareStatement.executeQuery();

            while (resultSet.next()) {
                responses.add(new PermissionResponse(resultSet.getInt(1), resultSet.getString("name"),resultSet.getString("section"),resultSet.getString("description")));
            }
            return responses;

        } catch (Exception e) {
            System.out.println("Exception occured : " + e.getMessage());
            return null;
        }

    }

    @Override
    public Optional<Permission> findById(int permissionId) {

        String sql = "select * from permission where id=?";

        try (
                Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setInt(1, permissionId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                return Optional.of(new Permission(rs.getInt("id"), rs.getString("name"),rs.getString("section"),rs.getString("description")));
            }

        } catch (Exception e) {
            System.out.println("Exception occured !!!");

            throw new OperationFailedException("Invalid Query !", 500);

        }

        return Optional.empty();
    }

    @Override
    public String deletePermission(int permissionId) {

        
        String sql = "delete from permission where id =?";

        try (
                Connection conn = DataBaseSourceClass.getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, permissionId);
            int check = stmt.executeUpdate();

            if (check > 0) {
                return "Permission deleted successfully";
            } else {
                return "Permisson deletion Failed!";
            }

        } catch (SQLException ex) {
            System.out.println("Error :" + ex.getMessage());
            throw new OperationFailedException("Failed to Delete Permission with Id :", permissionId);
        }

    }

    @Override
    public Permission updatePermission(PermissionRequest request, int permissionId) {
        
        String sql = "update permission set name=? where id=?";
        
        
        Permission permission= new Permission();
        
        try(
                Connection conn = DataBaseSourceClass.getDataSource().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)){
            
            
            stmt.setString(1, request.getName());
            stmt.setInt(2, permissionId);
            
            int affectedRow = stmt.executeUpdate();
            
            
            if(affectedRow>0){
                
                permission.setId(permissionId);
                permission.setName(request.getName());
                
                return permission;
            }
            return permission;
            
            
        }catch(Exception e){
            throw new OperationFailedException("Update Sql Exception", 500);
        }
        
    }

}
