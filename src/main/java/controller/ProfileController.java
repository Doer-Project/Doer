package controller;

import app.UserProfileService;
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
import java.util.List;

import model.SessionManager;

public class ProfileController {

    @FXML private Label lblName;
    @FXML private Label lblEmail;
    @FXML private Label lblAddress;
    @FXML private Label lblUserType;
    @FXML private Label lblGender;
    @FXML private ImageView profileImage;
    @FXML private StackPane rootPane;

    private static final String DEFAULT_IMAGE = "/images/default_profile.png";

    @FXML
    private void initialize() {
        loadUserProfile();
    }

    private void loadUserProfile() {
        try {
            String userId = SessionManager.getUserID();
            if (userId == null) {
                MessageBox.showError("Profile Load Error", "No user is logged in.");
                return;
            }

            // Get user data as List<String>
            // Order: [firstName, lastName, email, address, userType, gender, profilePic]
            List<String> userData = UserProfileService.getUserDataList(userId);

            if (userData != null && userData.size() >= 7) {
                // Combine frst and last name
                String fullName = userData.get(0) + " " + userData.get(1);
                lblName.setText(fullName);

                lblEmail.setText(userData.get(2));
                lblAddress.setText(userData.get(3));
                lblUserType.setText(userData.get(4));
                lblGender.setText(userData.get(5));

                String pic = userData.get(6);
                profileImage.setImage(
                        (pic == null || pic.isEmpty()) ? new Image(DEFAULT_IMAGE) : new Image(pic)
                );
            }

        } catch (Exception e) {
            MessageBox.showError("Profile Load Error", "Error loading profile:\n" + e.getMessage());
        }
    }

    @FXML
    private void editProfile(ActionEvent event) {
        try {
            Parent editView = FXMLLoader.load(getClass().getResource("/fxml/EditProfile.fxml"));
            rootPane.getChildren().setAll(editView);
        } catch (IOException e) {
            MessageBox.showError("Navigation Error", "Unable to open Edit Profile screen:\n" + e.getMessage());
        }
    }

    @FXML
    private void history(ActionEvent event) {
        try {
            String userId = SessionManager.getUserID();
            List<String> userData = UserProfileService.getUserDataList(userId);
            String userType = (userData != null && userData.size() >= 5) ? userData.get(4) : "Household";

            String fxmlPath = "Worker".equalsIgnoreCase(userType) ?
                    "/fxml/worker/PastWork.fxml" : "/fxml/household/PastWork.fxml";

            Node node = FXMLLoader.load(getClass().getResource(fxmlPath));
            rootPane.getChildren().setAll(node);

        } catch (IOException e) {
            MessageBox.showError("Navigation Error", "Unable to open history screen:\n" + e.getMessage());
        }
    }
}
