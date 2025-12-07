package com.app.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {

    private static final String BASE_DIR_NAME = "admin_uploaded_artifacts";
    
    private static final String PII_RESULTS_DIR = System.getProperty("java.io.tmpdir") + File.separator + "pii_scan_results";
    
    
    public static final String PII_SCANNER_SCRIPT_NAME = "find_pii.sh";


    // ---------------------- Upload Directory ----------------------
    public static Path getBaseUploadPath(String contextPath) throws IOException {
        Path uploadPath = Paths.get(contextPath, BASE_DIR_NAME);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            System.out.println("Created upload directory: " + uploadPath);
        }
        return uploadPath;
    }

    public static Stream<File> listUploadedFiles(String contextPath) {
        try {
            Path uploadPath = getBaseUploadPath(contextPath);
            return Files.list(uploadPath)
                        .filter(Files::isRegularFile)
                        .map(Path::toFile);
        } catch (IOException e) {
            System.err.println("Error listing uploaded files: " + e.getMessage());
            return Stream.empty();
        }
    }

   
    public static String analyzeJavaFile(File file) {
      
        StringBuilder analysis = new StringBuilder();
        try {
            String content = Files.readString(file.toPath());

            analysis.append("<h3>Class Candidates</h3>");
            Matcher classMatcher = Pattern.compile("(public|class)\\s+class\\s+(\\w+)").matcher(content);
            while (classMatcher.find()) {
                analysis.append("<p>Class: <strong>").append(classMatcher.group(2)).append("</strong></p>");
            }

            analysis.append("<h3>Methods & Signatures (Mock Analysis)</h3><ul>");
            Pattern methodPattern = Pattern.compile(
                "(public|private|protected|static)\\s+(?:\\w+)\\s+(\\w+)\\s*\\(([^)]*)\\)"
            );
            Matcher methodMatcher = methodPattern.matcher(content);

            while (methodMatcher.find()) {
                String signature = methodMatcher.group(0);
                String name = methodMatcher.group(2);
                String errorName = "";

                if (name.matches(".*[^a-zA-Z0-9_].*")) errorName = "Illegal Character/Syntax Violation";
                else if (Character.isDigit(name.charAt(0))) errorName = "Illegal Naming: Starts with Digit";
                else if (Character.isUpperCase(name.charAt(0))) errorName = "Naming Convention Violation (No Lowercase Start)";
                else if (name.toLowerCase().contains("temp") || name.toLowerCase().contains("old"))
                    errorName = "Code Smell: Temporary Name Detected";

                String tooltip = !errorName.isEmpty() ?
                    "style='color:red; cursor:pointer;' title='" + errorName + " detected.'" : "";

                analysis.append("<li><span ").append(tooltip).append(">").append(signature.trim()).append("</span></li>");
            }
            analysis.append("</ul>");

            analysis.append("<h3>Variable Candidates (Fields)</h3><ul>");
            Matcher varMatcher = Pattern.compile("(private|public|protected)\\s+(\\w+)\\s+(\\w+)\\s*;").matcher(content);
            while (varMatcher.find()) {
                analysis.append("<li>Type: ").append(varMatcher.group(2)).append(", Name: <strong>").append(varMatcher.group(3)).append("</strong></li>");
            }
            analysis.append("</ul>");

        } catch (IOException e) {
            analysis.append("<p style='color:red;'>Error reading file content: ").append(e.getMessage()).append("</p>");
        }
        return analysis.toString();
    }

    
    public static String executeShellPiiScan(File scriptFile, String dataFileToScanPath, String outputFileName)
            throws IOException, InterruptedException {
        StringBuilder output = new StringBuilder();

       
        scriptFile.setExecutable(true);

      
        Path outputFilePath = Paths.get(PII_RESULTS_DIR, outputFileName);

        output.append("Executing PII Scan...\n");
        output.append("Script: ").append(scriptFile.getAbsolutePath()).append("\n");
        output.append("Input File: ").append(dataFileToScanPath).append("\n");
        output.append("Output File: ").append(outputFilePath.toString()).append("\n");


        
        ProcessBuilder pb = new ProcessBuilder(
            scriptFile.getAbsolutePath(),
            dataFileToScanPath,
            outputFilePath.toString() 
        );

       
        Files.createDirectories(Paths.get(PII_RESULTS_DIR));

        pb.redirectErrorStream(true);

        Process process = pb.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        int exitCode = process.waitFor();
        output.append("\nScanner exited with code: ").append(exitCode).append("\n");

        return output.toString();
    }

   
    public static String readPiiOutputFile(String outputFileName) {

        Path piiFilePath = Paths.get(PII_RESULTS_DIR, outputFileName);

        try {
            if (Files.exists(piiFilePath)) {
                return Files.readString(piiFilePath);
            } else {
                return "No PII file found. Expected path: " + piiFilePath + ". The script may have failed or the path is incorrect.";
            }
        } catch (IOException e) {
            return "Error reading PII file: " + e.getMessage();
        }
    }
}
