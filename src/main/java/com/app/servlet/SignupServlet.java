package com.app.servlet;

import com.app.model.User;
import com.app.util.PasswordUtil;
import com.app.util.UserDao;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String department = request.getParameter("department");
        String role = request.getParameter("role");
        
        int clearanceLevel;
        try {
            clearanceLevel = Integer.parseInt(request.getParameter("clearanceLevel"));
        } catch (NumberFormatException e) {
            clearanceLevel = 1; // Default to lowest clearance if input is invalid
        }

        // 1. Create User object and hash the password
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(PasswordUtil.hashPassword(password));
        newUser.setEmail(email);
        newUser.setRole(role != null ? role : "user"); // Default to "user" role
        newUser.setDepartment(department);
        newUser.setClearanceLevel(clearanceLevel);

        // 2. Register the user via the DAO
        boolean registrationSuccess = false;
        try {
            
            registrationSuccess = UserDao.registerUser(newUser, getServletContext()); 
        } catch (Exception e) {
            System.err.println("Database error during registration:");
            e.printStackTrace();
        }

        // 3. Handle the response based on success or failure
        if (registrationSuccess) {
            // Redirect to the login page with a success message
            response.sendRedirect("login.jsp?signup=success");
        } else {
            // Assume failure means the username already exists or another DB error occurred
            response.sendRedirect("signup.jsp?error=exists");
        }
    }
}
