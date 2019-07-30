package View;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Controller.SysData;
import Controller.Music;
import Controller.Windows;
import Model.User;
import Utils.Window;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class HistoryController implements Initializable
{
	Music gameMusic = Music.getInstance();
	@FXML private ImageView sound;
	@FXML private TableView<User> tableView;
    @FXML private TableColumn<User, String> name;
    @FXML private TableColumn<User, String> score;
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
	 * sets details fro scores table.
	*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	String url = gameMusic.getSoundState("theme.mp3");
		Image photo = new Image(getClass().getResource(url).toExternalForm());
		sound.setImage(photo);
		name.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
    	score.setCellValueFactory(new PropertyValueFactory<User, String>("score"));

        tableView.getItems().setAll(parseUserList());
    }
    /*
	 * Gets details for scores table.
	*/
    private List<User> parseUserList(){ 
    	return SysData.getScores();
    }
}