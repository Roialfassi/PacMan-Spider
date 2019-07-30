package View;

import java.net.URL;
import java.util.ResourceBundle;

import Controller.Windows;
import Utils.Window;
import javafx.fxml.FXML;

public class LoadController 
{
	// location and resources will be automatically injected by the FXML loader
	@FXML private URL location;
	@FXML private ResourceBundle resources;
	/*
	 * Handle the close event and swap to main view.
	*/
	public static void close() {
		Windows.swap(Window.Manage);
	}
}