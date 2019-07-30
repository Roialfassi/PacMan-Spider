package View;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import Controller.Game;
import Controller.Music;
import Controller.Windows;
import Utils.Window;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class BeginController implements Initializable
{
	Music gameMusic = Music.getInstance();
	Game game = Game.getGame();
	@FXML private TextField username1;
	@FXML private TextField username2;
	@FXML private RadioButton onePlayer;
	@FXML private RadioButton twoPlayers;
	@FXML private Button openGame;
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
	 * Back to main.
	*/
	@FXML
	public void openMain(MouseEvent event) {
		Windows.swap(Window.Main);
	}
	/*
	 * Handle the start game option for 1 player or 2.
	 * check that all user names are fill and not the same.
	*/
	@SuppressWarnings("static-access")
	public void openGame(ActionEvent event) {
		if (onePlayer.isSelected())
		{
			if ((username1.getText().equals("")) || (username1.getText()==null))
			{
				Alert alert = new Alert(AlertType.WARNING);
				alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
				alert.setTitle("שגיאה");
				alert.setHeaderText("יש לרשום שם שחקן");
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/Resources/Images/msg.png").toString()));
				@SuppressWarnings("unused")
				Optional<ButtonType> result = alert.showAndWait();
				return;
			}
			game.setState(1);
			game.setUser(username1.getText(), 0, 1);
		}
		else if (twoPlayers.isSelected())
		{
			if (username1.getText().equals(username2.getText()))
			{
				Alert alert = new Alert(AlertType.WARNING);
				alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
				alert.setTitle("שגיאה");
				alert.setHeaderText("יש לרשום שמות שחקנים שונים");
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/Resources/Images/msg.png").toString()));
				@SuppressWarnings("unused")
				Optional<ButtonType> result = alert.showAndWait();
				return;
			} 
			if ((username1.getText().equals("")) || (username1.getText()==null) ||
					(username2.getText().equals("")) || (username2.getText()==null))
			{
				Alert alert = new Alert(AlertType.WARNING);
				alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
				alert.setTitle("שגיאה");
				alert.setHeaderText("יש לרשום שם שחקן ל2 השחקנים");
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/Resources/Images/msg.png").toString()));
				@SuppressWarnings("unused")
				Optional<ButtonType> result = alert.showAndWait();
				return;
			}
			game.setState(1);
			game.setState(2);
			game.setUser(username1.getText(), 0, 1);
			game.setUser(username2.getText(), 0, 2);
			game.startTimer();
		}
		game.startTime();
		Windows.swap(Window.Game);
	}
	/*
	 * initialize the controller and check sound state.
	*/
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String url = gameMusic.getSoundState("theme.mp3");
		Image photo = new Image(getClass().getResource(url).toExternalForm());
		sound.setImage(photo);
	}
	/*
	 * Set field for one player name active
	*/
	@FXML
    void onePLayerClicked(MouseEvent event) {
		username2.setDisable(true);
    }
	/*
	 * Set field for two player name active
	*/
	@FXML
    void twoPLayerClicked(MouseEvent event) {
		username2.setDisable(false);
    }
}