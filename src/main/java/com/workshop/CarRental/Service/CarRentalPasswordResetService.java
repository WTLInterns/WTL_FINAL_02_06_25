package com.workshop.CarRental.Service;


import com.workshop.CarRental.Entity.CarRentalUser;
import com.workshop.CarRental.Repository.CarRentalRepository;
import com.workshop.Entity.User;
import com.workshop.Repo.UserRepo;
import com.workshop.Service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CarRentalPasswordResetService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CarRentalRepository carRentalRepository;

    // Temporary storage for OTPs (email -> OTP)
    private final Map<String, String> otpStorage = new HashMap<>();

    public void sendPasswordResetEmail(String email) {
        CarRentalUser user = carRentalRepository.findByEmail(email).get();
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String otp = generateOTP();
        otpStorage.put(email, otp); // Store OTP in memory

        boolean emailSent = emailService.sendEmail(otp, "Password Reset OTP", email);

        if (emailSent) {
            System.out.println("OTP sent successfully!");
        } else {
            throw new RuntimeException("Failed to send OTP email");
        }
    }

    private String generateOTP() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    public boolean validateOTP(String email, String otp) {
        String storedOtp = otpStorage.get(email); // Retrieve OTP from memory
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStorage.remove(email); // Clear OTP after validation
            return true;
        }
        return false;
    }

    public void resetPassword(String email, String newPassword) {
        CarRentalUser user = carRentalRepository.findByEmail(email).get();
        if (user != null) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword); // Save the hashed password
            carRentalRepository.save(user);
            System.out.println("Password reset successfully.");
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
