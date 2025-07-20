package util;

import java.security.SecureRandom;


/**
 * Generate strong OTP;
 */
public class OtpGenerator {

    private static SecureRandom random = new SecureRandom();

    /**
     * Generates a secure 6-digit numeric One-Time Password (OTP).
     * {@link java.security.SecureRandom}
     *
     * @return A string representing a 6-digit secure OTP (e.g., "492813")
     */
    public static String generateOtp() {
        String otp = "";
        for (int i = 0; i < 6; i++) {
            otp += random.nextInt(10);
        }
        return otp;
    }
}
