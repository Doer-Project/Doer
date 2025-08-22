package util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.security.SecureRandom;
import java.util.Properties;

public class OTP {
    private static SecureRandom random = new SecureRandom();

    public static String generateOtp() {
        String otp = "";
        for (int i = 0; i < 6; i++) {
            otp += random.nextInt(10);
        }
        return otp;
    }

    public static String sendOtp(String toEmail) {
        String otp = generateOtp();

        String fromEmail = "niharkakani@gmail.com";
        String fromPassword = "xarl afsv vsjp huho";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, fromPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your OTP Code");
            message.setText("Your OTP is: " + otp + "\nUse this code to verify your email.");

            Transport.send(message);
            System.out.println("✅ OTP sent to: " + toEmail);
            return otp;
        }
        catch (Exception e) {
            System.out.println("❌ Failed to send OTP: " + e.getMessage());
            return null;
        }
    }
}
