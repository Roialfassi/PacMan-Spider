package View;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Controller.SysData;
import Controller.Music;
import Controller.Windows;
import Model.Question;
import Utils.Window;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ManageController implements Initializable
{
	Music gameMusic = Music.getInstance();
	@FXML private Label info;
	@FXML private Label error;
	@FXML private Button BtnaddQ;
	@FXML private Button BtnremoveQ;
	@FXML private TableView<Question> tableView;
    @FXML private TableColumn<Question, String> question;
    @FXML private TableColumn<Question, String> team;
    @FXML private TableColumn<Question, String> qLevel;
    @FXML private TableColumn<Question, String> qRightAnswer;
    @FXML private TableColumn<Question, String> qAnswer1;
    @FXML private TableColumn<Question, String> qAnswer2;
    @FXML private TableColumn<Question, String> qAnswer3;
    @FXML private TableColumn<Question, String> qAnswer4;
    @FXML private Question temp_selected_row;
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
	 * open add question view.
	*/
	@FXML
    void addQ(ActionEvent event) {
		Windows.swap(Window.AddQ);
    }
	/*
	 * Remove question after confirmation and after that question has been chosen.
	*/
	@FXML
    void removeQ(ActionEvent event) {
		if (temp_selected_row == null)
		{
			Alert alertin = new Alert(AlertType.WARNING);
			alertin.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
			alertin.setTitle("שגיאה");
			alertin.setHeaderText("לא נבחרה שאלה");
			Stage stage = (Stage) alertin.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/Resources/Images/msg.png").toString()));
			@SuppressWarnings("unused")
			Optional<ButtonType> resultin = alertin.showAndWait();
			return;
		}
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		alert.setTitle("אשר מחיקה");
		alert.setHeaderText("הינך עומד למחוק שאלה");
		alert.setContentText("האם אתה בטוח בכך?");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("/Resources/Images/msg.png").toString()));
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			try {
				if (SysData.deleteQuestion(temp_selected_row))
				{
					initialize(location, resources);
					Alert alertin = new Alert(AlertType.CONFIRMATION);
					alertin.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
					alertin.setTitle("אישור מחיקה");
					alertin.setHeaderText("השאלה נמחקה");
					Stage stage1 = (Stage) alertin.getDialogPane().getScene().getWindow();
					stage1.getIcons().add(new Image(this.getClass().getResource("/Resources/Images/msg.png").toString()));
					@SuppressWarnings("unused")
					Optional<ButtonType> resultin = alertin.showAndWait();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
		    return;
		}
    }
	/*
	 * initialize the controller.
	 * get sound state.
	 * Put questions in the table.
	 * handle marking question.
	 * handle double click for editing question.
	*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	String url = gameMusic.getSoundState("theme.mp3");
		Image photo = new Image(getClass().getResource(url).toExternalForm());
		sound.setImage(photo);
    	
    	tableView.setEditable(true);
    	question.setCellValueFactory(new PropertyValueFactory<Question, String>("Question"));
    	team.setCellValueFactory(new PropertyValueFactory<Question, String>("Team"));
    	qLevel.setCellValueFactory(new PropertyValueFactory<Question, String>("Level"));
    	qRightAnswer.setCellValueFactory(new PropertyValueFactory<Question, String>("RightAnswer"));
    	qAnswer1.setCellValueFactory(new PropertyValueFactory<Question, String>("Answer1"));
    	qAnswer2.setCellValueFactory(new PropertyValueFactory<Question, String>("Answer2"));
    	qAnswer3.setCellValueFactory(new PropertyValueFactory<Question, String>("Answer3"));
    	qAnswer4.setCellValueFactory(new PropertyValueFactory<Question, String>("Answer4"));

        tableView.getItems().setAll(parseQuestionsList());
        tableView.setRowFactory(tv -> {
            TableRow<Question> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
            	if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY && event.getClickCount() == 2)
                {
            		Question clickedRow = row.getItem();
                	temp_selected_row = clickedRow;
                	showEditScreen(clickedRow);
                }
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY && event.getClickCount() == 1)
                {
                	Question clickedRow = row.getItem();
                	temp_selected_row = clickedRow;
                }
            });
            return row ;
        });
    }
    /*
	 * Open edit question view
	*/
    private void showEditScreen(Question q)
    {
    	if (SysData.markQuestionToEdit(q))
    	{
        	Windows.swap(Window.EditQ);
    	}
    }
    /*
	 * Gets the question list.
	*/
    private List<Question> parseQuestionsList(){
    	return SysData.getQuestions();
    }
}