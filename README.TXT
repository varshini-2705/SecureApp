🧩 Java Web Application – Part 1  🖇️ https://writer.zoho.in/writer/open/h7tync55265691254494cad8b1c031ba30dc8
📘 Overview

This application is a Java-based web platform designed to demonstrate secure authentication, role-based access control (RBAC), session management, file handling, and integration with SonarQube for static code analysis.
The project focuses on building secure, user-centric functionality with fine-grained access control and automated code quality scanning.

🚀 Features
1️⃣ User Registration (Signup)

A dedicated Signup Page collects new user details such as username, password, department, and role.
Passwords are securely stored using password hashing for enhanced protection.

2️⃣ User Login System

After successful registration, users can log in using their username and password.
The login system maintains user sessions using Cookies, storing:

Session ID

User attributes (role, department, time of login, clearance level)
Access control is implemented through RBAC (Role-Based Access Control) combined with Attribute-Based Access Control (ABAC):
Access to routes like /admin or /reports depends on both user role and specific attributes (department, login time, or clearance level).
If any attribute does not satisfy the policy, the system returns HTTP 403 – Forbidden.

3️⃣ Session & Logout Management

Includes a Logout Page that clears cookies and ends the user session.
Implements auto-logout after 2 minutes of inactivity for session security.
Provides a “Remember Me” checkbox option to create a persistent cookie for extended session validity.

4️⃣ Role & Attribute Management

Initially, default roles and attributes are assigned to each user.
The Admin user can modify roles and attributes of other users directly within the application (e.g., assigning roles like Admin, Developer, or User).

5️⃣ File Upload Management

A dedicated File Upload Page allows file management based on roles:
Admin and Developer can upload files.
User role can only view uploaded files.
Uploaded files are listed with additional functionality, including file details and analysis tools.

6️⃣ File Details Viewer

Each uploaded file has a “View File Details” option that displays:
Class names defined
Methods declared
Variables declared

7️⃣ PII Scanner Integration

A Run PII Scanner button is available beside each uploaded file.
The scanner identifies Personally Identifiable Information (PII) patterns (e.g., email IDs, contact numbers, or sensitive data fields) declared within the source code.

8️⃣ Static Code Analysis (SonarQube Integration)

Integrates SonarQube as a static code analysis tool.
When a user uploads a .zip file:
The archive is extracted and analyzed automatically.
Results are sent to the SonarQube Server Dashboard (http://localhost:9000/) for detailed code quality and security insights.

🧰 Tech Stack

Language: Java

Frontend: JSP, HTML, CSS

Backend: Servlets, JDBC

Database: PSQL (for user and file metadata storage)

Build Tool: Maven

Server: Apache Tomcat

Static Analysis: SonarQube

Security: Password Hashing, Cookies, Session Handling

