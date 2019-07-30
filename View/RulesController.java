package View;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import Controller.Music;
import Controller.Windows;
import Utils.Window;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class RulesController implements Initializable
{
	Music gameMusic = Music.getInstance();
	@FXML private ImageView sound;
	// location and resources will be automatically injected by the FXML loader
	@FXML private URL location;
	@FXML private ResourceBundle resources;
	/*
	 * Handle the close event.
	 * pop alert for confirmation.
	*/
	@FXML
	void close(MouseEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		alert.setTitle("יציאה מהמשחק");
		alert.setHeaderText("האם הינך רוצה לצאת מהמשחק כולו?");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("/Resources/Images/msg.png").toString()));
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			System.exit(0);
		}
	}
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
	 * Handle the open main event.
	*/
	@FXML
	public void openMain(MouseEvent event) {
		Windows.swap(Window.Main);
	}
	/*
	 * initialize the controller.
	 * get music state.
	*/
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		String url = gameMusic.getSoundState("theme.mp3");
		Image photo = new Image(getClass().getResource(url).toExternalForm());
		sound.setImage(photo);
	}
}