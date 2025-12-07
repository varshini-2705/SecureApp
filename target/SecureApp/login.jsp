<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Secure Application Login</title>
</head>
<body>
    <div style="width: 300px; margin: 50px auto; padding: 20px; border: 1px solid #ccc; border-radius: 8px;">
        <h2>User Login</h2>

        <c:if test="${param.error == 'invalid_credentials'}">
            <p style="color: red;">Invalid username or password. Please try again.</p>
        </c:if>
        
        <c:if test="${param.error == 'not_registered'}">
            <p style="color: red;"> **Not Registered**. Please sign up below.</p>
        </c:if>
        
        <form action="login" method="post">
            <div style="margin-bottom: 15px;">
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" required 
                       style="width: 100%; padding: 8px; box-sizing: border-box; border: 1px solid #ddd; border-radius: 4px;">
            </div>
            
            <div style="margin-bottom: 15px;">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required
                       style="width: 100%; padding: 8px; box-sizing: border-box; border: 1px solid #ddd; border-radius: 4px;">
            </div>
            
            <div style="margin-bottom: 20px;">
                <input type="checkbox" id="rememberMe" name="rememberMe">
                <label for="rememberMe">Remember Me (2 Minutes)</label>
            </div>
            
            <button type="submit" 
                    style="width: 100%; padding: 10px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer;">
                Login
            </button>
        </form>

<div class="signup-link">
            <p>New user? <a href="signup.jsp">Register Here</a></p>
        </div>
    </div>
</body>
</html>
