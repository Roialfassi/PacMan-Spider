package View;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import Controller.Game;
import Controller.Music;
import Controller.Windows;
import Model.Config;
import Model.Question;
import Utils.Window;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameController2 implements Initializable
{
	Music gameMusic = Music.getInstance();
	Game game = Game.getGame();
	
	private static GameController2 gameController2;
	private Controller.pacman2.Maze maze;
	@FXML private TextArea userName;
	@FXML private TextArea score;
	@FXML private AnchorPane Qbar;
	@FXML private TextArea question;
	@FXML private TextArea answer1;
	@FXML private TextArea answer2;
	@FXML private TextArea answer3;
	@FXML private TextArea answer4;
	@FXML private Text timer;
	@FXML private Text time;
	@FXML private Group mazeGroupParam;
	@FXML private ImageView sound;
	@FXML private ImageView life1;
	@FXML private ImageView life2;
	
	public GameController2() {
		super();
	}
	/*
	 * Gets Instance
	*/
	public static GameController2 getInstance()
	{
		if (gameController2  == null) {
			gameController2 = new GameController2();
		}
		return gameController2;
	}
	// location and resources will be automatically injected by the FXML loader
	@FXML
	private URL location;
	@FXML
	private ResourceBundle resources;
	/*
	 * Handle the close event.
	 * pop alert. pause timer if needed. 
	*/
	@SuppressWarnings("static-access")
	@FXML
	void close(MouseEvent event) {
		if (game.getGameMode() == 2) game.pauseTimer();
		game.pauseTime();
		Qbar.setVisible(false);
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
		if (game.getGameMode() == 2) game.resumeTimer();
		game.resumeTime();
		Qbar.setVisible(true);
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
	 * pause games and timer.
	 * pop alert for confirmation.
	 * send saving game if chosen 
	*/
	@SuppressWarnings("static-access")
	@FXML
	public void openMain(MouseEvent event) throws Throwable {
		if (game.getGameMode() == 2) game.pauseTimer();
		game.pauseTime();
		if(!game.getTwoMaze().gamePaused.getValue())
			game.getTwoMaze().pauseGame();
		if(game.getTwoMaze() != null && !game.getTwoMaze().gamePaused.getValue())
			game.getTwoMaze().pauseGame();
		Qbar.setVisible(false);
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		alert.setTitle("הפסקת משחק");
		alert.setHeaderText("הינך עומד לצאת מהמשחק");
		alert.setContentText("האם אתה בטוח בכך?");
		ButtonType buttonExitAndsave = new ButtonType("צא ושמור");
		ButtonType buttonExitOnly = new ButtonType("צא בלבד");
		ButtonType buttonTypeCancel = new ButtonType("ביטול", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonExitAndsave, buttonExitOnly, buttonTypeCancel);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("/Resources/Images/msg.png").toString()));
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonExitAndsave){
			game.stopAllGames(true);
			Windows.swap(Window.Main);
		} else if (result.get() == buttonExitOnly) {
			game.stopAllGames(false);
			Windows.swap(Window.Main);
		} else {
			if (game.getGameMode() == 2) game.resumeTimer();
			game.resumeTime();
			Qbar.setVisible(true);
		}
	}
	/*
	 * initialize the controller.
	 * get sound state.
	 * create instance if needed.
	 * updates about user details.
	 * adds the maze to the view.
	*/
    @SuppressWarnings("static-access")
	public void initialize(URL location, ResourceBundle resources)
    {
    	String color = Config.getPacman_color();
	    Image life = new Image(getClass().getResourceAsStream("/Resources/pacman/images/Character/left1_"+color+".png"));
	    life1.setImage(life);
	    life2.setImage(life);
	    String url = gameMusic.getSoundState("theme.mp3");
		Image photo = new Image(getClass().getResource(url).toExternalForm());
		sound.setImage(photo);
		
		maze = game.getTwoMaze();
    	userName.setText(game.getUName(2));
    	if (gameController2  == null) {
			gameController2 = new GameController2();
		}
    	updateScore(game.getUScore(2));
    	updateQustion(game.getUQ(2));
    	updateLife(game.getULife(2));
    	gameController2.score = score;
    	gameController2.life1 = life1;
    	gameController2.life2 = life2;
    	gameController2.Qbar = Qbar;
    	gameController2.question = question;
    	gameController2.answer1 = answer1;
    	gameController2.answer2 = answer2;
    	gameController2.answer3 = answer3;
    	gameController2.answer4 = answer4;
    	gameController2.time = time;
    	gameController2.timer = timer;
    	mazeGroupParam.getChildren().add(maze);
    }
    /*
	 * Update the score view
	*/
    public void updateScore(int scoreUpdate)
    {
    	score.setText(String.valueOf(scoreUpdate));
    }
    /*
	 * Update the life view
	*/
    public void updateLife(int lifeUpdate)
    {
    	if (lifeUpdate == 2)
    	{
    		life2.setVisible(true);
    		life1.setVisible(true);
    	}
    	else if (lifeUpdate == 1)
    	{
    		life2.setVisible(false);
    		life1.setVisible(true);
    	}
    	else if (lifeUpdate == 0)
    	{
    		life2.setVisible(false);
    		life1.setVisible(false);
    	}
    }
    /*
	 * Update the timer view
	*/
    public void updateTimer(int intTimer)
    {
    	if (timer != null)
    	{
    		if (intTimer < 6)
			{
    			timer.setFill(Color.RED);
			}
    		else
    		{
    			timer.setFill(Color.WHITE);
    		}
    		timer.setText(String.valueOf(intTimer));
    	}
    }
    /*
	 * Update the time view
	*/
    public void updateTime(int intTime)
    {
    	if (time != null)
    	{
    		time.setText(String.valueOf(intTime));
    	}
    }
    /*
	 * Update the question and the answers view
	*/
    public void updateQustion(Question q)
    {
    	if (q == null || q.equals(null))
    	{
    		question.setText("");
        	answer1.setText("");
        	answer2.setText("");
        	answer3.setText("");
        	answer4.setText("");
    	}
    	else
    	{
    		question.setText(q.getQuestion());
        	answer1.setText(q.getAnswer1());
        	answer2.setText(q.getAnswer2());
        	answer3.setText(q.getAnswer3());
        	answer4.setText(q.getAnswer4());
    	}
    }
    /*
	 * starting timer and replace to game1
	*/
    public void replaceGame() {
    	Game.startTimer();
    	Windows.swap(Window.Game);
	}
    /*
	 * Ends the game.
	 * pop alert for confirmation.
	 * swap to main.
	*/
    @SuppressWarnings("static-access")
	public void endGame() throws Exception {
    	if (game.getGameMode() == 2) game.pauseTimer();
		game.pauseTime();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		alert.setTitle("סיום משחק");
		alert.setHeaderText("המשחק נגמר. התוצאה נשמרה אוטומטית. ניתן להתחיל משחק חדש.");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("/Resources/Images/msg.png").toString()));
	    alert.show();
	    game.stopAllGames(true);
	    Windows.swap(Window.Main);
	}
}