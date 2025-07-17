package util;

public class ValidationUtil {

    /**
     * Validate username:
     * - between alphabet (a-z), (A,Z) and number (0,9) & (_) allowed
     * - no space or special characters allowed
     * - 3 to 20 length
     *
     * @param username
     * @return true if username is valid, otherwise false
     */
    public static boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    /**
     * Validates a strong password:
     * - At least 8 characters to maximum 15 characters
     * - At least one letter (a-z)
     * - At least one letter (A-Z)
     * - At least one digit (0-9)
     * - At least one special character [!@#$%^&*_]
     *
     * @param password
     * @return ture if password is strong & valid, otherwise false
     */
    public static boolean isStrongPassword(String password) {
        return password != null &&
                password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*_])[A-Za-z\\d!@#$%^&*_]{8,15}$");
    }


    /**
     *  Validate email:
     *  - [xyz@.domain]
     *
     * @param email
     * @return true if email is valid, otherwise false
     */
    public static boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    /**
     * Validate input:
     * - e.g :- city, firstName, lastName
     * - Alphabets only (a-z) & (A-Z)
     *
     * @param input
     * @return true if alphabets only input, otherwise false
     */
    public static boolean isAlphabetOnly(String input) {
        return input.matches("^[a-zA-z]{3,10}+$");
    }


}
