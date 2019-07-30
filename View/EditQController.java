package View;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Controller.SysData;
import Controller.Windows;
import Model.Question;
import Utils.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class EditQController implements Initializable
{
	@FXML private Button close;
	@FXML private Button ok;
	@FXML private TextField question;
	@FXML private TextField qAnswer1;
	@FXML private TextField qAnswer2;
	@FXML private TextField qAnswer3;
	@FXML private TextField qAnswer4;
	@FXML private ComboBox<String> qLevelCombo;
	private ObservableList<String> optionsLevel = 
		    FXCollections.observableArrayList("1","2","3");
	@FXML private ComboBox<String> qRightAnswerCombo;
	private ObservableList<String> optionsRightAnswer = 
		    FXCollections.observableArrayList("1","2","3","4");
	@FXML private ComboBox<String> qTeamCombo;
	private ObservableList<String> optionsTeam = 
		    FXCollections.observableArrayList("Chimp","Crocodile","Scorpion","Giraffe",
		    		"Spider","Viper","Panther","Zebra","Wolf","Lion","Jellyfish",
		    		"Panda","Piranha","Shark","Hawk","Hedgehog","Husky");
	private String errorText;
	// location and resources will be automatically injected by the FXML loader
	@FXML private URL location;
	@FXML private ResourceBundle resources;
	/*
	 * Handle the close event. 
	*/
	@FXML
	void close(ActionEvent event) {
		Windows.swap(Window.Manage);
	}
	/*
	 * Handle the OK event.
	 * saves the question.
	 * pop alert for saving.
	 * handle the input validation.
	*/
	@FXML
	void ok(ActionEvent event) throws Exception {
		String tempQuestion = question.getText();
		String tempqAnswer1 = qAnswer1.getText();
		String tempqAnswer2 = qAnswer2.getText();
		String tempqAnswer3 = qAnswer3.getText();
		String tempqAnswer4 = qAnswer4.getText();
		String templevelCombo = qLevelCombo.getSelectionModel().getSelectedItem();
		String tempTeamCombo = qTeamCombo.getSelectionModel().getSelectedItem();
		String tempRightAnswerCombo = qRightAnswerCombo.getSelectionModel().getSelectedItem();
		if (validInput(tempQuestion,tempqAnswer1,tempqAnswer2,tempqAnswer3,tempqAnswer4))
		{
			SysData.updateQ(tempQuestion,tempTeamCombo,templevelCombo,tempRightAnswerCombo,tempqAnswer1,tempqAnswer2,tempqAnswer3,tempqAnswer4);
			Alert alertin = new Alert(AlertType.CONFIRMATION);
			alertin.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
			alertin.setTitle("אישור עדכון");
			alertin.setHeaderText("השאלה עודכנה");
			Stage stage = (Stage) alertin.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/Resources/Images/msg.png").toString()));
			@SuppressWarnings("unused")
			Optional<ButtonType> resultin = alertin.showAndWait();
			Windows.swap(Window.Manage);
		}
		else
		{
			Alert alertin = new Alert(AlertType.WARNING);
			alertin.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
			alertin.setTitle("בעיה בנתוני השאלה");
			alertin.setHeaderText(errorText);
			Stage stage = (Stage) alertin.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/Resources/Images/msg.png").toString()));
			@SuppressWarnings("unused")
			Optional<ButtonType> resultin = alertin.showAndWait();
		}
	}
	/*
	 * valid the input of the question details 
	*/
	private boolean validInput(String tempQuestion, String tempqAnswer1, String tempqAnswer2,
			String tempqAnswer3, String tempqAnswer4) {
	if ((tempQuestion.equals("")) || (tempQuestion==null)) {
		errorText = "שאלה לא יכולה להיות ריקה";
		return false;}
	if ((tempqAnswer1.equals("")) || (tempqAnswer1==null) ||
		(tempqAnswer2.equals("")) || (tempqAnswer2==null) ||
		(tempqAnswer3.equals("")) || (tempqAnswer3==null) ||
		(tempqAnswer4.equals("")) || (tempqAnswer4==null)) {
		errorText = "יש לרשום 4 תשובות לשאלה";
		return false;}
	return true;
	}
	/*
	 * initialize the controller. 
	*/
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Question tempQ = parseQuestionsList();
		question.setText(tempQ.getQuestion());
		qAnswer1.setText(tempQ.getAnswer1());
		qAnswer2.setText(tempQ.getAnswer2());
		qAnswer3.setText(tempQ.getAnswer3());
		qAnswer4.setText(tempQ.getAnswer4());
		qLevelCombo.getItems().addAll(optionsLevel);
		qLevelCombo.getSelectionModel().select(tempQ.getLevel());
		qTeamCombo.getItems().addAll(optionsTeam);
		qTeamCombo.getSelectionModel().select(tempQ.getTeam());
		qRightAnswerCombo.getItems().addAll(optionsRightAnswer);
		qRightAnswerCombo.getSelectionModel().select(tempQ.getRightAnswer());
	}
	/*
	 * Gets the question that marked to edit. 
	*/
	private Question parseQuestionsList(){ 
    	List<Question> questions = SysData.getQuestions();
    	Question tempQ = null;
    	for (int i = 0; i < questions.size(); i++)
		{
    		if (questions.get(i).isqEdit())
    		{
    			tempQ = questions.get(i);
    		}
		}
    	return tempQ;
    }
}
