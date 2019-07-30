package Model;

public class Config {
	private static int pacman_life;
	private static int time_between_players; 
	private static int silver_dot_value;
	private static String commands;
	private static String pacman_color;
	
	/*
	 * set pacman_life
	*/
	public static void setPacman_life(int pacman_life2)
	{
		pacman_life = pacman_life2;
	}
	/*
	 * set time_between_players
	*/
	public static void setTime_between_players(int time_between_players2)
	{
		time_between_players = time_between_players2;
	}
	/*
	 * set silver_dot_value
	*/
	public static void setSilver_dot_value(int silver_dot_value2)
	{
		silver_dot_value = silver_dot_value2;
	}
	/*
	 * set commands
	*/
	public static void setCommands(String commands2)
	{
		commands = commands2;
	}
	/*
	 * set pacman_color
	*/
	public static void setPacman_color(String pacman_color2)
	{
		pacman_color = pacman_color2;
	}
	/*
	 * get pacman_life
	*/
	public static int getPacman_life()
	{
		return pacman_life;
	}
	/*
	 * get time_between_players
	*/
	public static int getTime_between_players()
	{
		return time_between_players;
	}
	/*
	 * get silver_dot_value
	*/
	public static int getSilver_dot_value()
	{
		return silver_dot_value;
	}
	/*
	 * get commands
	*/
	public static String getCommands()
	{
		return commands;
	}
	/*
	 * get pacman_color
	*/
	public static String getPacman_color()
	{
		return pacman_color;
	}
}
