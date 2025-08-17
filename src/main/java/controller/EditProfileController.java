//package controller;
//
//import app.UserProfileService;
//import javafx.scene.control.*;
//import javafx.scene.image.Image;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.StackPane;
//import util.MessageBox;
//
//import java.io.File;
//import java.io.IOException;
//import java.sql.SQLException;
//
//import static model.SessionManager.currentUser;
//
//public class EditProfileController {
//
//    @FXML
//    private TextField txtFullName, txtEmail, txtUsername, txtAge, txtAddress;
//
//    @FXML
//    private ComboBox<String> cmbGender;
//
//    @FXML
//    private ImageView profileImage;
//
//    @FXML
//    private StackPane rootPane;
//
//    @FXML
//    private Label lblUserType;
//
//    @FXML
//    public void initialize() {
//        try {
//            if (currentUser != null) {
//                // Full Name
//                String fullName = currentUser.getFirstName();
//                if (currentUser.getLastName() != null && !currentUser.getLastName().isEmpty()) {
//                    fullName += " " + currentUser.getLastName();
//                }
//                txtFullName.setText(fullName);
//
//                // Common fields
//                txtEmail.setText(currentUser.getEmail());
//                txtUsername.setText(currentUser.getUserName());
//                txtAge.setText(String.valueOf(currentUser.getAge()));
//
//                // Gender dropdown
//                cmbGender.getItems().addAll("Male", "Female", "Other");
//                cmbGender.setValue(currentUser.getGender());
//
//                // Address + user type
//                if (currentUser instanceof Household) {
//                    txtAddress.setText(((Household) currentUser).getAddress());
//                    lblUserType.setText("Household");
//                } else if (currentUser instanceof Worker) {
//                    txtAddress.setText(((Worker) currentUser).getPreferredLocation());
//                    lblUserType.setText("Worker");
//                } else {
//                    lblUserType.setText("Unknown");
//                }
//
//                // Profile Image
//                loadProfileImage(currentUser.getPhotoPath());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void loadProfileImage(String path) {
//        try {
//            if (path != null && !path.isEmpty()) {
//                File file = new File(path);
//                if (file.exists()) {
//                    profileImage.setImage(new Image(file.toURI().toString()));
//                    return;
//                }
//            }
//            // Default if no image or file missing
//            String defaultPath = getClass().getResource("/images/default.jpeg").toExternalForm();
//            profileImage.setImage(new Image(defaultPath));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    public void cancleBtn(ActionEvent event) {
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml"));
//            rootPane.getChildren().setAll(root);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    public void saveBtn(ActionEvent event) throws SQLException, IOException {
//        /// uncomment this after db connect
////        if (currentUser == null) return;
//        /// delete this after db connect
//        if (currentUser == null) {
//            Parent editView = FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml"));
//            rootPane.getChildren().setAll(editView);
//        }
//        // Name split
//        String[] parts = txtFullName.getText().trim().split(" ", 2);
//        currentUser.setFirstName(parts[0]);
//        if (parts.length > 1) currentUser.setLastName(parts[1]);
//        else currentUser.setLastName("");
//
//        // Address
//        if (currentUser instanceof Household) {
//            ((Household) currentUser).setAddress(txtAddress.getText());
//        } else if (currentUser instanceof Worker) {
//            ((Worker) currentUser).setPreferredLocation(txtAddress.getText());
//        }
//
//        // Gender
//        currentUser.setGender(cmbGender.getValue());
//        Alert confirmAlert = new Alert(
//                Alert.AlertType.CONFIRMATION,
//                "Do you want to save the changes?",
//                ButtonType.YES, ButtonType.NO
//        );
//        confirmAlert.setTitle("Confirm Changes");
//        confirmAlert.showAndWait();
//
//        boolean confirm = (confirmAlert.getResult() == ButtonType.YES);
//
//        // --- Call Service ---
//        UserProfileService profileService = new UserProfileService();
//        boolean success = profileService.updateUser(currentUser, confirm);
//
//
//        if (success) {
//            MessageBox.showInfo("Success", "Profile updated successfully!");
//        } else if (!success) {
//            MessageBox.showInfo("Changes Discarded", "Your changes were not saved.");
//        } else {
//            MessageBox.showAlert("Update Failed", "Could not update your profile. Please try again.");
//        }
//
//
//        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Profile updated successfully!");
//        alert.showAndWait();
//
//        try {
//            Parent editView = FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml"));
//            rootPane.getChildren().setAll(editView);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
