package com.app.servlet;

import com.app.model.User;
import com.app.util.DBUtil;
import com.app.util.PasswordUtil;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean rememberMe = request.getParameter("rememberMe") != null;

        User user = null;
        boolean userFound = false; // Flag to track if the username was found

        try (Connection conn = DBUtil.getConnection(getServletContext())) {

            // 1. Fetch user and hash from DB
            String storedHash = null;
            try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    // User was found
                    userFound = true; 
                    storedHash = rs.getString("password_hash");
                    user = new User();
                    user.setUserId(String.valueOf(rs.getInt("user_id")));
                    user.setUsername(rs.getString("username"));
                    user.setRole(rs.getString("role"));
                    user.setDepartment(rs.getString("department"));
                    user.setClearanceLevel(rs.getInt("clearance_level"));
                }
            }
            
            // --- AUTHENTICATION LOGIC ---

            // Check if user was found AND password matches
            if (userFound && PasswordUtil.checkPassword(password, storedHash)) {
                // 2. Successful Login (Authentication SUCCESS)
                
                HttpSession session = request.getSession(true);
                session.setAttribute("loggedInUser", user);
                session.setAttribute("role", user.getRole());
                session.setAttribute("department", user.getDepartment());
                session.setAttribute("lastActivityTime", System.currentTimeMillis());

                // Update last_login_time in DB
                try (PreparedStatement updateLoginStmt = conn.prepareStatement("UPDATE users SET last_login_time = NOW() WHERE user_id = ?")) {
                    updateLoginStmt.setInt(1, Integer.parseInt(user.getUserId()));
                    updateLoginStmt.executeUpdate();
                }

                // "Remember Me" Persistent Cookie Logic
                if (rememberMe) {
                    String token = UUID.randomUUID().toString();
                    Cookie persistentCookie = new Cookie("p_auth", token);
                    persistentCookie.setMaxAge(2 * 60); // 2 minutes
                    persistentCookie.setHttpOnly(true);
                    response.addCookie(persistentCookie);

                    // Store token in DB
                    try (PreparedStatement updateTokenStmt = conn.prepareStatement("UPDATE users SET persistent_token = ? WHERE user_id = ?")) {
                        updateTokenStmt.setString(1, token);
                        updateTokenStmt.setInt(2, Integer.parseInt(user.getUserId()));
                        updateTokenStmt.executeUpdate();
                    }
                }

                response.sendRedirect("welcome.jsp");

            } else {
                // --- Authentication FAILURE ---
                
                String errorParam;
                if (!userFound) {
                    // Case 1: Username not found in the database
                    errorParam = "not_registered"; 
                } else {
                    // Case 2: Username found, but password was incorrect
                    errorParam = "invalid_credentials"; 
                }
                
                response.sendRedirect("login.jsp?error=" + errorParam);
            }

        } catch (SQLException e) {
            // This will catch any SQL error, including connection failures
            e.printStackTrace();
            // Redirect to a generic error page or the login page with a system error
            response.sendRedirect("login.jsp?error=system_error");
        }
    }
}

