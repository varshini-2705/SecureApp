<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit User</title>
    <style>
        body { font-family: sans-serif; background-color: #f8f9fa; display: flex; justify-content: center; align-items: center; min-height: 100vh; margin: 0; }
        .edit-container { width: 400px; padding: 30px; border: 1px solid #dee2e6; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); background-color: #fff; }
        h2 { text-align: center; margin-bottom: 25px; color: #343a40; }
        div { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: 600; }
        input[type="text"], input[type="email"], input[type="number"], select { width: 100%; padding: 10px; box-sizing: border-box; border: 1px solid #adb5bd; border-radius: 4px; }
        button { width: 100%; padding: 10px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; margin-top: 10px; font-size: 1.05em; transition: background-color 0.3s; }
        button:hover { background-color: #0056b3; }
    </style>
</head>
<body>
    <div class="edit-container">
        <h2>Edit User: ${user.username}</h2>

        <form method="POST" action="editUser">
            <!-- CRITICAL FIX: Use 'userId' to match the User model's getter (getUserId) -->
            <input type="hidden" name="id" value="${user.userId}" /> 
            
            <div>
                <label for="username">Username (Read-Only):</label>
                <input type="text" id="username" value="${user.username}" disabled />
            </div>

            <div>
                <label for="email">Email (Read-Only):</label>
                <input type="email" id="email" value="${user.email}" disabled />
            </div>
            
            <div>
                <label for="role">Role:</label>
                <select id="role" name="role" required>
                    <option value="user" ${user.role == 'user' ? 'selected' : ''}>User</option>
                    <option value="admin" ${user.role == 'admin' ? 'selected' : ''}>Admin</option>
                    <option value="developer" ${user.role == 'developer' ? 'selected' : ''}>Developer</option>
                </select>
            </div>

            <div>
                <label for="department">Department:</label>
                <select id="department" name="department" required>
                    <option value="Finance" ${user.department == 'Finance' ? 'selected' : ''}>Finance</option>
                    <option value="HR" ${user.department == 'HR' ? 'selected' : ''}>HR</option>
                    <option value="Engineering" ${user.department == 'Engineering' ? 'selected' : ''}>Engineering</option>
                    <option value="Sales" ${user.department == 'Sales' ? 'selected' : ''}>Sales</option>
                </select>
            </div>

            <div>
                <label for="clearanceLevel">Clearance Level (1-5):</label>
                <input type="number" id="clearanceLevel" name="clearanceLevel" min="1" max="5" value="${user.clearanceLevel}" required />
            </div>

            <button type="submit">Update User</button>
        </form>
        <p style="text-align: center; margin-top: 15px;"><a href="admin" style="color: #6c757d; text-decoration: none;">&larr; Back to Admin Panel</a></p>
    </div>
</body>
</html>
