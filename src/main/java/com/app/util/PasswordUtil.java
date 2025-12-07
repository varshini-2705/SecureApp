package com.app.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    // Hashes the plaintext password
    public static String hashPassword(String plaintext) {
        // BCrypt.gensalt() generates a random salt
        return BCrypt.hashpw(plaintext, BCrypt.gensalt());
    }

    // Checks if the plaintext password matches the stored hash
    public static boolean checkPassword(String plaintext, String hashedPassword) {
        return BCrypt.checkpw(plaintext, hashedPassword);
    }
}
