package controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import util.MessageBox;


import java.io.IOException;

import static model.SessionManager.currentUser;


public class ProfileController {

    @FXML
    private Label lblName;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblAddress;

    @FXML
    private Label lblUserType;

    @FXML
    private Label lblGender;

    @FXML
    private ImageView profileImage;

    @FXML
    private StackPane rootPane; // the container in Profile.fxml

    public void initialize() {
        loadUserProfile();
    }

    private void loadUserProfile() {
        String defaultImagePath = "/images/default_profile.png";
        try {
            // ---------- DB LOGIC START ----------
            // currentUser should be set in SessionManager during login
            // Optionally fetch full user details from DB
            // Example:
            // UserProfileService service = new UserProfileService();
            // currentUser = service.getUserByUsername(SessionManager.username);
            // ---------- DB LOGIC END ----------

            // ---------- DUMMY DATA START ----------
            // Uncomment for testing without DB
//            if(currentUser == null) {
//                currentUser = new Household(
//                        "Dharmil", "Panchal",
//                        "dharmil123",
//                        "dharmil@example.com",
//                        25,
//                        "Male",
//                        "Ahmedabad, India",
//                        "Ahmedabad",
//                        "380001",
//                        "passwordHash"
//                );
//            }
            // ---------- DUMMY DATA END ----------

            if(currentUser != null) {

                lblName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
                lblEmail.setText(currentUser.getEmail());

                // Address (available in both subclasses)
                lblAddress.setText(currentUser instanceof Household ?
                        ((Household) currentUser).getAddress() :
                        ((Worker) currentUser).getPreferredLocation());

                // UserType recognition
                if(currentUser instanceof Household) {
                    lblUserType.setText("Household");
                } else if(currentUser instanceof Worker) {
                    lblUserType.setText("Worker");
                } else {
                    lblUserType.setText("Unknown");
                }

                lblGender.setText(currentUser.getGender());

//                 Profile image code commented for now
                String path = currentUser.getPhotoPath();

                if (path == null || path.isEmpty()) {
                    // Assign default only once and save it
                    currentUser.setPhotoPath(defaultImagePath);
//                    UserProfileService.updateUserPhoto(currentUser.getId(), defaultImagePath); // DAO method to persist

                    profileImage.setImage(new Image(defaultImagePath));
                } else {
                    profileImage.setImage(new Image(path));
                }
            }

        } catch (Exception e) {
            MessageBox.showError("Profile Load Error", "An error occurred while loading the user profile:\n" + e.getMessage());
        }
    }

    public void editProfile(ActionEvent event) {
        try {
            Parent editView = FXMLLoader.load(getClass().getResource("/fxml/EditProfile.fxml"));
            rootPane.getChildren().setAll(editView); // replace content
        } catch (IOException e) {
            MessageBox.showError("Navigation Error", "Unable to open Edit Profile screen:\n" + e.getMessage());
        }
    }

    // point to past work
    public void history(ActionEvent event) {

        String fxmlPath;

        if(currentUser instanceof Household) {
            fxmlPath = "/fxml/household/PastWork.fxml";
        } else if(currentUser instanceof Worker) {
            fxmlPath = "/fxml/worker/PastWork.fxml";
        } else {
            System.out.println("Unknown user type: ");
            return;
        }


        try {
            Node node = FXMLLoader.load(getClass().getResource(fxmlPath));
//            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
