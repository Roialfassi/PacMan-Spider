package View;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import Controller.Music;
import Controller.SysData;
import Controller.Windows;
import Model.Config;
import Utils.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ConfigController implements Initializable
{
	Music gameMusic = Music.getInstance();
	private int pacman_life;
	private int time_between_players;
	private int silver_dot_value;
	private String commands;
	private String pacman_color;
	@FXML private ImageView sound;
	@FXML private Button save;
	@FXML private ComboBox<String> combo_Pacman_life;
	private ObservableList<String> options_Pacman_life = 
		    FXCollections.observableArrayList("1","2","3");
	@FXML private ComboBox<String> combo_Time_between_players;
	private ObservableList<String> options_Time_between_players = 
		    FXCollections.observableArrayList("10","20","30","40","50","60");
	@FXML private ComboBox<String> combo_Silver_dot_value;
	private ObservableList<String> options_Silver_dot_value = 
		    FXCollections.observableArrayList("50","100","150","200");
	@FXML private ComboBox<String> combo_Commands;
	private ObservableList<String> options_Commands = 
		    FXCollections.observableArrayList("Arrows","A-W-S-D");
	@FXML private ComboBox<String> combo_Pacman_color;
	private ObservableList<String> options_Pacman_color = 
		    FXCollections.observableArrayList("Yellow","Green","Red","White","Blue");
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
	 * Handle the save event.
	 * saves the question.
	 * pop alert for saving.
	 * handle the input validation.
	*/
	@FXML
	void save(ActionEvent event) throws Exception {
		pacman_life = Integer.parseInt(combo_Pacman_life.getSelectionModel().getSelectedItem());
		time_between_players = Integer.parseInt(combo_Time_between_players.getSelectionModel().getSelectedItem());
		silver_dot_value = Integer.parseInt(combo_Silver_dot_value.getSelectionModel().getSelectedItem());
		commands = combo_Commands.getSelectionModel().getSelectedItem();
		pacman_color = combo_Pacman_color.getSelectionModel().getSelectedItem();
		SysData.updateConfiguration(pacman_life,time_between_players,silver_dot_value,commands,pacman_color);
		Alert alertin = new Alert(AlertType.INFORMATION);
		alertin.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		alertin.setTitle("אישור שמירה");
		alertin.setHeaderText("ההגדרות נשמרו");
		Stage stage = (Stage) alertin.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("/Resources/Images/msg.png").toString()));
		@SuppressWarnings("unused")
		Optional<ButtonType> resultin = alertin.showAndWait();
		Windows.swap(Window.Main);
		
	}
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
	 * Back to main.
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
	public void initialize(URL location, ResourceBundle resources) {
		try {
			SysData.parseConfiguration();
		} catch (Exception e) {
			e.printStackTrace();
		}
		pacman_life = Config.getPacman_life();
		time_between_players = Config.getTime_between_players();
		silver_dot_value = Config.getSilver_dot_value();
		commands = Config.getCommands();
		pacman_color = Config.getPacman_color();
		String url = gameMusic.getSoundState("theme.mp3");
		Image photo = new Image(getClass().getResource(url).toExternalForm());
		sound.setImage(photo);
		combo_Pacman_life.getItems().addAll(options_Pacman_life);
		combo_Pacman_life.setValue(Integer.toString(pacman_life));
		combo_Time_between_players.getItems().addAll(options_Time_between_players);
		combo_Time_between_players.setValue(Integer.toString(time_between_players));
		combo_Silver_dot_value.getItems().addAll(options_Silver_dot_value);
		combo_Silver_dot_value.setValue(Integer.toString(silver_dot_value));
		combo_Commands.getItems().addAll(options_Commands);
		combo_Commands.setValue(commands);
		combo_Pacman_color.getItems().addAll(options_Pacman_color);
		combo_Pacman_color.setValue(pacman_color);
		combo_Pacman_color.setCellFactory(
	            new Callback<ListView<String>, ListCell<String>>() {
	                public ListCell<String> call(ListView<String> param) {
	                    final ListCell<String> cell = new ListCell<String>() {
	                        public void updateItem(String item, 
	                            boolean empty) {
	                                super.updateItem(item, empty);
	                                if (item != null) {
	                                    setText(item);
	                                    if (item.contains("Red")) {
	                                        setTextFill(Color.RED);
	                                        setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
	                                    }
	                                    else if (item.contains("Green")){
	                                        setTextFill(Color.GREEN);
	                                        setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
	                                    }
	                                    else if (item.contains("Blue")){
	                                        setTextFill(Color.BLUE);
	                                        setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
	                                    }
	                                    else if (item.contains("White")){
	                                        setTextFill(Color.WHITE);
	                                        setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
	                                    }
	                                    else if (item.contains("Yellow")){
	                                        setTextFill(Color.YELLOW);
	                                        setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
	                                    }
	                                }
	                                else {
	                                    setText(null);
	                                }
	                            }
	                };
	                return cell;
	            }
	        });
	}
}