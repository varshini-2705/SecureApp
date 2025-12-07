<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Welcome - Secure App</title>
    <style>
        body {
            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
            background: linear-gradient(135deg, #1a73e8, #4285f4, #6fa3ef);
            background-size: cover;
            color: #333;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .container {
            background: #fff;
            padding: 2.5rem;
            border-radius: 15px;
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
            width: 90%;
            max-width: 600px;
            text-align: center;
            animation: fadeIn 0.8s ease;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        h2 {
            color: #1a73e8;
            margin-bottom: 1rem;
        }

        ul {
            list-style-type: none;
            padding: 0;
            text-align: left;
            margin: 1rem 0;
        }

        ul li {
            padding: 8px 0;
            border-bottom: 1px solid #eee;
        }

        ul li strong {
            color: #444;
        }

        a.button {
            display: inline-block;
            margin: 8px 5px;
            padding: 10px 18px;
            color: #fff;
            background-color: #1a73e8;
            border: none;
            border-radius: 6px;
            text-decoration: none;
            transition: background 0.3s ease;
        }

        a.button:hover {
            background-color: #1558b0;
        }

        .logout {
            background-color: #d93025;
        }

        .logout:hover {
            background-color: #b2241b;
        }

        .error {
            color: #d93025;
            background: rgba(255, 255, 255, 0.85);
            padding: 20px;
            border-radius: 10px;
        }
    </style>
</head>
<body>
<div class="container">
    <c:choose>
        <c:when test="${sessionScope.loggedInUser != null}">
            <h2>Welcome, <c:out value="${sessionScope.loggedInUser.username}" /> </h2>
            
            <p>You are logged in with the following attributes:</p>
            <ul>
                <li><strong>Role:</strong> <c:out value="${sessionScope.role}" /></li>
                <li><strong>Department:</strong> <c:out value="${sessionScope.department}" /></li>
            </ul>
            
            <hr style="margin: 20px 0; border: 0; border-top: 1px solid #ddd;">

            <h3>Application Access</h3>
            <div>
                <a href="admin" class="button"> View Main Panel </a>
              
            </div>

            <hr style="margin: 20px 0; border: 0; border-top: 1px solid #ddd;">

            <a href="logout" class="button logout"> Logout</a>
        </c:when>

        <c:otherwise>
            <div class="error">
                <p>You are not logged in.</p>
                <p>Please <a href="login.jsp" class="button">Login</a> to continue.</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>

