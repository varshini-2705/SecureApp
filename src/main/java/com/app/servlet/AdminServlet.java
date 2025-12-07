package com.app.servlet;

import com.app.model.User;
import com.app.util.DBUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> userList = new ArrayList<>();

        String sql = "SELECT user_id, username, email, role, department, clearance_level FROM users ORDER BY user_id";
      
        try (Connection conn = DBUtil.getConnection(getServletContext());
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
        } catch (Exception e) {
            System.err.println("Error fetching user list for Admin Panel:");
            e.printStackTrace();
        }

        request.setAttribute("userList", userList);
        request.getRequestDispatcher("admin.jsp").forward(request, response);
    }
}
