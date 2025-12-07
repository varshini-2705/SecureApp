package com.app.util;

import com.app.model.User;
import javax.servlet.ServletContext; 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {


    public static User getUserById(String userId, ServletContext context) {
        User user = null;
        String sql = "SELECT user_id, username, email, role, department, clearance_level FROM users WHERE user_id = ?";
        
        // Pass the context to DBUtil.getConnection()
        try (Connection conn = DBUtil.getConnection(context);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, Integer.parseInt(userId)); 
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserId(String.valueOf(rs.getInt("user_id")));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    user.setDepartment(rs.getString("department"));
                    user.setClearanceLevel(rs.getInt("clearance_level"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Invalid User ID format: " + userId);
        }
        return user;
    }

    public static List<User> getAllUsers(ServletContext context) {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT user_id, username, email, role, department, clearance_level FROM users ORDER BY user_id";

        
        try (Connection conn = DBUtil.getConnection(context);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = new User();
                user.setUserId(String.valueOf(rs.getInt("user_id")));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setDepartment(rs.getString("department"));
                user.setClearanceLevel(rs.getInt("clearance_level"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }
    
 
    public static boolean updateUser(User user, ServletContext context) {
        String sql = "UPDATE users SET role = ?, department = ?, clearance_level = ? WHERE user_id = ?";
        
       
        try (Connection conn = DBUtil.getConnection(context);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getRole());
            stmt.setString(2, user.getDepartment());
            stmt.setInt(3, user.getClearanceLevel());
            stmt.setInt(4, Integer.parseInt(user.getUserId())); 
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (NumberFormatException e) {
            System.err.println("Invalid User ID format during update: " + user.getUserId());
            return false;
        }
    }
    
   
    public static boolean registerUser(User newUser, ServletContext context) {
        String sql = "INSERT INTO users (username, password_hash, email, role, department, clearance_level, last_login_time) VALUES (?, ?, ?, ?, ?, ?, NOW())";
        
        try (Connection conn = DBUtil.getConnection(context);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newUser.getUsername());
            stmt.setString(2, newUser.getPasswordHash());
            stmt.setString(3, newUser.getEmail());
            stmt.setString(4, newUser.getRole());
            stmt.setString(5, newUser.getDepartment());
            stmt.setInt(6, newUser.getClearanceLevel());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                System.err.println("Registration failed: Username already exists.");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }
}
