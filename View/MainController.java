package View;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import Controller.Music;
import Controller.Windows;
import Utils.Window;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainController implements Initializable
{
	Music gameMusic = Music.getInstance();
	private String passwordString = "1234";
	@FXML private ImageView sound;
	@FXML public Button beginButton;
	@FXML public Button rulesButton;
	@FXML public Button historyButton;
	@FXML public Button manageButton;
	// location and resources will be automatically injected by the FXML loader
	@FXML private URL location;
	@FXML private ResourceBundle resources;
	/*
	 * Handle the music swap, including the image change.
	*/
	@FXML
    void doSwapMusic(MouseEvent event) {
		String url = gameMusic.swap("theme.mp3");
		Image photo = new Image(getClass().getResource(url).toExternalForm());
		sound.setImage(photo);
    }
	/*
	 * Handle the close event.
	 * pop alert for confirmation.
	*/
	@FXML
	void close(MouseEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		alert.setTitle("Exit Game");
		alert.setHeaderText("Do you want to quit the game in it's entirety?");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("/Resources/Images/msg.png").toString()));
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			System.exit(0);
		}
	}
	/*
	 * Open Rules view.
	*/
	@FXML
    void openRules(ActionEvent event) {
		Windows.swap(Window.Rules);
    }
	/*
	 * Open Rules view.
	*/
	@FXML
    void openConfig(MouseEvent event) {
		Windows.swap(Window.Config);
    }
	/*
	 * Open manage view after entering the right password.
	*/
	@FXML
	void openManage(ActionEvent event) {
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle("Enter The Password");
		dialog.setGraphic(new ImageView(this.getClass().getResource("/Resources/Images/login.png").toString()));
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("/Resources/Images/msg.png").toString()));
		dialog.setTitle("Manager Identify");
		dialog.setHeaderText("Only Managers Can Change it");
		ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		PasswordField password = new PasswordField();
		password.setPromptText("Password");
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);
		password.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isEmpty());
		});
		dialog.getDialogPane().setContent(grid);
		Platform.runLater(() -> password.requestFocus());
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				return password.getText();
			}
			return null;
		});
		Optional<String> result = dialog.showAndWait();

		if (result.isPresent()){
			System.out.println(password.getText());
			if (password.getText().equals(passwordString))
			{
				Windows.swap(Window.Manage);
			}
			else
			{
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Filed to Login");
				alert.setHeaderText(null);
				alert.setContentText("Wrong Password");
				alert.showAndWait();
			}
		}
	}

	/*
	 * Open History view.
	*/
	@FXML
    void openHistory(ActionEvent event) {
		Windows.swap(Window.History);
    }
	/*
	 * Open begin game view.
	*/
	@FXML
	void openBegin(ActionEvent event) {
		Windows.swap(Window.Begin);
	}
	/*
	 * initialize the controller.
	 * get music state.
	*/
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String url = gameMusic.getSoundState("theme.mp3");
		Image photo = new Image(getClass().getResource(url).toExternalForm());
		sound.setImage(photo);
	}
}