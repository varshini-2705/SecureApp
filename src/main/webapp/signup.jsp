<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Sign Up</title>
    <style>
        body { 
            font-family: sans-serif; 
            background-color: #e9ecef; /* Light gray background */
            display: flex;
            justify-content: center; /* Center horizontally */
            align-items: center; /* Center vertically */
            min-height: 100vh; /* Ensure it takes full viewport height */
            margin: 0;
        }
        .signup-container { 
            width: 350px; 
            padding: 30px; 
            border: 1px solid #ced4da; /* Light border */
            border-radius: 8px; 
            box-shadow: 0 4px 12px rgba(0,0,0,0.1); /* Subtle shadow */
            background-color: #fff;
        }
        h2 {
            text-align: center;
            margin-bottom: 25px;
            color: #343a40; /* Dark text */
        }
        input[type="text"], input[type="email"], input[type="password"], input[type="number"], select {
            width: 100%; 
            padding: 10px; 
            box-sizing: border-box; 
            border: 1px solid #adb5bd; 
            border-radius: 4px; 
            margin-bottom: 15px;
        }
        label { 
            display: block; 
            margin-bottom: 5px; 
            font-weight: 600; 
        }
        button { 
            width: 100%; 
            padding: 10px; 
            background-color: #28a745; /* Green button */
            color: white; 
            border: none; 
            border-radius: 4px; 
            cursor: pointer; 
            margin-top: 15px;
            font-size: 1.05em;
            transition: background-color 0.3s;
        }
        button:hover {
            background-color: #218838;
        }
        .login-link { 
            text-align: center; 
            margin-top: 20px; 
            font-size: 0.9em; 
        }
        .login-link a {
            color: #007bff;
            text-decoration: none;
        }
        .login-link a:hover {
            text-decoration: underline;
        }
        /* Style for success/error messages */
        .message {
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 4px;
            text-align: center;
        }
        .success {
            color: #155724;
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
        }
        .error {
            color: #721c24;
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
        }
    </style>
</head>
<body>
    <div class="signup-container">
        <h2>User Sign Up</h2>

        <c:if test="${param.success == 'true'}">
            <p class="message success">Registration successful! Please <a href="login.jsp">Login</a>.</p>
        </c:if>
        <c:if test="${param.error == 'exists'}">
            <p class="message error">Username already taken. Please choose another.</p>
        </c:if>

        <form action="signup" method="post">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>

            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
            
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required>

            <label for="department">Department:</label>
            <select id="department" name="department" required>
                <option value="Finance">Finance</option>
                <option value="HR">HR</option>
                <option value="Engineering">Engineering</option>
                <option value="Sales">Sales</option>
            </select>
            
            <label for="clearanceLevel">Clearance Level (1-5):</label>
            <input type="number" id="clearanceLevel" name="clearanceLevel" min="1" max="5" value="1" required>
            
            <!-- Default Role for new users is "User" -->
            <input type="hidden" name="role" value="User">

            <button type="submit">Register</button>
        </form>
        
        <div class="login-link">
            <p>Already have an account? <a href="login.jsp">Login Here</a></p>
        </div>
    </div>
</body>
</html>
