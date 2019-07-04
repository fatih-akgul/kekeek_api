package com.kekeek.api.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Scanner;

/**
 * Use this class to encode your API password
 */
public class KekeekPasswordEncoder {
    public static void main(String[] args) throws Exception {
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.print("Enter the password to encode: ");
        Scanner reader = new Scanner(System.in);
        String password = reader.nextLine();

        System.out.println(encoder.encode(password));
    }
}
