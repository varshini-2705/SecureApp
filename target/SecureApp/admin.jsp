<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head><title>Admin Panel</title>

 <style>
        body { font-family: sans-serif; }
        .feature-link { 
            display: inline-block; 
            padding: 10px 20px; 
            margin-bottom: 20px; 
            background-color: #007bff; 
            color: white; 
            text-decoration: none; 
            border-radius: 5px;
            font-weight: bold;
        }
        .feature-link:hover {
            background-color: #0056b3;
        }
    </style>

</head>
<body>
    <h2>Registered Users List</h2>
    <p>Welcome, ${sessionScope.loggedInUser.username} (${sessionScope.role}).</p>
    
    <a href="admin-file-manager" class="feature-link">Go to File Management Console</a>
     <a href="welcome.jsp" class="feature-link back-button">Back to Welcome Page</a>

    
    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Email</th>
                <th>Role</th>
                <th>Department</th>
                <th>Clearance</th>
                <c:if test="${sessionScope.role == 'admin'}">
                    <th>Action</th>
                </c:if>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="user" items="${userList}">
                <tr>
                    <td>${user.userId}</td>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                    <td>${user.department}</td>
                    <td>${user.clearanceLevel}</td>
                    
                    <c:if test="${sessionScope.role == 'admin'}">
                        <td>
                            <a href="editUser?id=${user.userId}">Edit</a> 
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
        </tbody>
    </table>    
    
    <p><a href="logout">Logout</a></p>
</body>
</html>
