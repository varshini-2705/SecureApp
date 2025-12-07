<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sensitive HR Reports</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f7f9; color: #333; margin: 0; padding: 20px; }
        .report-header { background-color: #dc3545; color: white; padding: 20px; border-radius: 8px 8px 0 0; margin-bottom: 20px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
        .report-header h1 { margin: 0; font-size: 1.8em; }
        .report-container { max-width: 900px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 0 20px rgba(0,0,0,0.05); }
        .data-point { border-left: 5px solid #007bff; padding: 10px 15px; margin-bottom: 15px; background-color: #f8f9fa; }
        .data-point strong { display: block; margin-bottom: 5px; color: #007bff; }
        .note { color: #6c757d; margin-top: 30px; font-style: italic; border-top: 1px dashed #ccc; padding-top: 15px; }
        .back-link { display: inline-block; margin-top: 20px; color: #6c757d; text-decoration: none; font-weight: 500; }
        .back-link:hover { text-decoration: underline; }
    </style>
</head>
<body>

    <div class="report-container">
        <div class="report-header">
            <h1>HR & FINANCE SENSITIVE DATA REPORT</h1>
            <p>Access granted based on ABAC Policy: Admin AND HR Department.</p>
        </div>

        <p>This page contains mock data accessible only to authorized HR Administrators.</p>
        
        <div class="data-point">
            <strong>Unauthorized Access Attempts (Last 24h)</strong>
            <span>12 attempts from non-HR departments.</span>
        </div>

        
        <div class="data-point">
            <strong>Highest Salary Data</strong>
            <span>$250,000 (CEO Compensation).</span>
        </div>
        
        
        <a href="admin" class="back-link">&larr; Return to Admin Panel</a>
    </div>
</body>
</html>
