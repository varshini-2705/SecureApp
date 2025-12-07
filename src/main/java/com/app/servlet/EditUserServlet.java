package com.app.servlet;

import com.app.model.User;
import com.app.util.UserDao;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/editUser")
public class EditUserServlet extends HttpServlet {

    /**
     * Handles displaying the form to edit a user.
     * It fetches the user's data to pre-fill the form fields.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("id");

        if (userId != null && !userId.isEmpty()) {
            
            User user = UserDao.getUserById(userId, getServletContext());

            if (user != null) {
                request.setAttribute("user", user);
                // Forward to the JSP form for editing
                request.getRequestDispatcher("editUserForm.jsp").forward(request, response);
            } else {
                response.sendRedirect("admin?error=userNotFound");
            }
        } else {
            response.sendRedirect("admin?error=noIdProvided");
        }
    }

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = request.getParameter("id");
        String role = request.getParameter("role");
        String department = request.getParameter("department");
        String clearanceLevelStr = request.getParameter("clearanceLevel");

        if (userId == null || userId.isEmpty() || clearanceLevelStr == null) {
            response.sendRedirect("admin?error=missingData");
            return;
        }

        try {
            int clearanceLevel = Integer.parseInt(clearanceLevelStr);
           
            User userToUpdate = UserDao.getUserById(userId, getServletContext());

            if (userToUpdate != null) {
                userToUpdate.setRole(role);
                userToUpdate.setDepartment(department);
                userToUpdate.setClearanceLevel(clearanceLevel);

                if (UserDao.updateUser(userToUpdate, getServletContext())) {
                    response.sendRedirect("admin?message=updateSuccess");
                } else {
                    response.sendRedirect("editUserForm.jsp?id=" + userId + "&error=dbUpdateFailed");
                }
            } else {
                response.sendRedirect("admin?error=userNotFoundForUpdate");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid clearance level format: " + clearanceLevelStr);
            response.sendRedirect("editUserForm.jsp?id=" + userId + "&error=invalidLevel");
        }
    }
}
