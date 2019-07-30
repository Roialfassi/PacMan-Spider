package Utils;

public enum Window {
	/**
	 * General
	 */
	Main("Main"),
	Begin("Begin"),
	Game("Game"),
	Game2("Game2"),
	History("History"),
	Manage("Manage"),
	EditQ("EditQ"),
	AddQ("AddQ"),
	Load("Load"),
	Config("Config"),
	Rules("Rules");
	
	String fxmlFile;
	
	Window(String fxmlFile){
		this.fxmlFile = fxmlFile;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.fxmlFile;
	}
	
}
