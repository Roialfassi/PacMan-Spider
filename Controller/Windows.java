package Controller;

import java.io.IOException;

import javax.swing.JOptionPane;

import Utils.Window;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class Windows {

	private static Stage currentStage;
	private static Scene currentScene;
	private static FXMLLoader currentLoader;

	/**
	 * This method takes window and sets it as the current windows for user
	 *
	 * @param primaryStage
	 * @param toOpen
	 */
	public static void swap(Stage primaryStage, Window toOpen) {
		if (currentStage != null)
			currentStage.close();
		Platform.setImplicitExit(false);
		currentStage = primaryStage;
		currentStage.initStyle(StageStyle.UNDECORATED);
		swap(toOpen);
		currentStage.show();
		currentScene.getWindow().centerOnScreen();
		currentStage.getIcons().add(new Image("/Resources/Images/pacman_bar.png"));
		currentStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent we) {
				int confirm = JOptionPane.showOptionDialog(null, "Are You Sure to Exit?",
						"Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (confirm == 0) {
					Logger.log("Received closing request..");
					System.exit(0);
				}
			}
		});
	}

	/**
	 * This method takes window and sets it as the current windows for user
	 *
	 * @param toOpen
	 */
	public static void swap(Window toOpen) {

		if (Windows.currentScene != null) {
			/**
			 * Fade Out
			 */
			FadeTransition ft = new FadeTransition(Duration.millis(300), Windows.currentScene.getRoot());
			ft.setFromValue(1.0);
			ft.setToValue(0.5);
			ft.play();
		}

		/**
		 * Start new window
		 */
		Windows.currentLoader = new FXMLLoader(Windows.class.getResource("/View/" + toOpen + ".fxml"));

		Parent root = null;
		try {
			root = Windows.currentLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
			Logger.log("Couldn't get file: " + Windows.class.getResource("/View/" + toOpen + ".fxml"), true);
			System.exit(0);
		}

		/**
		 * Fade in the new window
		 */
		FadeTransition ft = new FadeTransition(Duration.millis(300), root);
		ft.setFromValue(0.5);
		ft.setToValue(1.0);
		ft.play();
		Windows.currentScene = new Scene(root);
		Logger.log(Windows.class.getResource("/Resources/CSS/Style.css").toExternalForm());
		Windows.currentScene.getStylesheets()
				.add(Windows.class.getResource("/Resources/CSS/Style.css").toExternalForm());
		currentStage.setScene(currentScene);
		currentStage.getIcons().add(new Image("/Resources/Images/pacman_bar.png"));
		Windows.currentStage.show();

		currentScene.getWindow().centerOnScreen();
	}
	
	public static Stage getCurrentStage() {
		return currentStage;
	}
	
	public static Scene getCurrentScene() {
		return currentScene;
	}
}
