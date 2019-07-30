package Controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import Utils.Window;
import View.LoadController;
import javafx.application.Application;
import javafx.application.Platform;

/**
 * starting the game
 */
public class PacManMain extends Application {

	Music gameMusic = Music.getInstance();

	public static void main(String[] args) {

		launch(args);

	}

	public void start(Stage primaryStage) throws Exception {

		// Initiate Logger


		Logger.initializeMyFileWriter();

		Logger.log("Initializing PacMan");
		// Initiate Sound
		gameMusic.play("OpenGame.wav");

		gameMusic.play("theme.mp3");
		gameMusic.createcycle("theme.mp3" , 850);
		gameMusic.changeVolume("theme.mp3", 20.0);

		// Windows.swap(primaryStage, Window.Main);
		// change to loading page
		Windows.swap(primaryStage, Window.Load);

		// Parse and change to main page
		new Thread(new Runnable() {
			public void run() {

				Platform.runLater(new Runnable() {
					public void run() {

						try {

							SysData.parseScores();
							SysData.parseQuestions();
							SysData.parseConfiguration();


							Thread.sleep(2000);

							LoadController.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}});
			}}).start();
	}
}