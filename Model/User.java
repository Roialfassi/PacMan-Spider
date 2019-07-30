package Model;

public class User {
	private String name;
	private int score;
	/*
	 * Create new user
	*/
	public User(String name, int score) {
		super();
		this.name = name;
		this.score = score;
	}
	/*
	 * Get user name
	*/
	public String getName() {
		return this.name;
	}
	/*
	 * Set User Name
	*/
	public void setName(String name) {
		this.name = name;
	}
	/*
	 * get the score
	*/
	public int getScore() {
		return this.score;
	}
	/*
	 * Set User score
	*/
	public void setScore(int score) {
		this.score = score;
	}
	/*
	 * Creates string to write to JSON file
	*/
	public String toJson()
	{
		return "{\r\n" + 
				"      \"user\": \""+ this.name +"\",\r\n" + 
				"      \"score\": \""+this.score+"\"\r\n" + 
				"    }";
	}

}