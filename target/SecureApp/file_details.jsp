<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>File Details: ${fileName}</title>
    <style>
        body { font-family: sans-serif;
background-color: #e9ecef; padding: 20px; }
        .container { background-color: white; padding: 30px; border-radius: 10px;
box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); max-width: 800px; margin: 0 auto;
}
        h1 { color: #17a2b8; border-bottom: 2px solid #17a2b8; padding-bottom: 10px; margin-top: 0;
}
        
        .file-info { margin-bottom: 20px;
background: #f8f9fa; padding: 15px; border-radius: 5px; }
        .analysis-content { margin-top: 20px;
border: 1px solid #dee2e6; padding: 15px; border-radius: 5px; }
        .analysis-content h3 { color: #007bff;
margin-top: 15px; font-size: 1.1em; border-bottom: 1px dashed #ccc; padding-bottom: 5px;
}
        .analysis-content ul { list-style-type: none; padding: 0;
}
        .analysis-content li { background: #e9f7ff; padding: 8px; margin-bottom: 5px;
border-left: 3px solid #007bff; border-radius: 3px; }
        
        /* Style for the hover-over tooltip */
        .analysis-content li span[title] { 
            position: relative;
/* Red text for flagged signatures */
            color: red;
cursor: pointer;
            border-bottom: 1px dotted red; /* Indicate hoverable element */
        }
        /* Tooltip style when hovering */
        .analysis-content li span[title]:hover::after {
            content: attr(title);
position: absolute;
            bottom: 100%; /* Position above the element */
            left: 50%;
transform: translateX(-50%);
            padding: 5px 10px;
            background: #333;
            color: white;
            border-radius: 5px;
            white-space: nowrap;
            z-index: 10;
}

        .note { margin-top: 30px; padding-top: 15px; font-style: italic; font-size: 0.9em; color: #6c757d;
}
    </style>
</head>
<body>
    <div class="container">
        <h1>File Analysis: ${fileName}</h1>

        <div class="file-info">
            <p><strong>File Name:</strong> ${fileName}</p>
            <p><strong>Type:</strong> ${fileType}</p>
            <p><strong>Size:</strong> ${(file.length() / 1024.0)} KB</p>
        </div>

        <h2>Code Analysis Results</h2>
        <div class="analysis-content">
 <c:out 
value="${analysisHtml}" escapeXml="false" />            
        </div>

        <a href="<c:url value="/admin-file-manager"/>">Go back to File Manager</a>
    </div>
</body>
</html>
