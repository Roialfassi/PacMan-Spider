package Controller;

import java.util.Timer;
import java.util.TimerTask;

import Model.Config;
import Model.Question;
import Model.User;
import View.GameController;
import View.GameController2;
import javafx.application.Platform;

public class Game {
		
	private static Controller.pacman.Maze oneMaze = null;
	private static Controller.pacman2.Maze twoMaze = null;
	private static int gameMode;
	private static Game game;
	private static User u1;
	private static User u2;
	private static int u1Score = 0;
	private static int u2Score = 0;
	private static int u1Life = Config.getPacman_life();
	private static int u2Life = Config.getPacman_life();
	private static Question u1Q = null;
	private static Question u2Q = null;
	private static GameController gameController = GameController.getInstance();
	private static GameController2 gameController2 = GameController2.getInstance();
	private static int timer_interval;
	private static int time_interval = 0;
	private static Timer timer = null;
	private static Timer time = null;
	private static int MazeOn = 1;

	private Game() {}

	/*
	 * Gets Instance for Game
	*/
	public static Game getGame()
	{
		if (game == null) {
			game = new Game();
		}
		return game;
	}
	/*
	 * Sets the Maze for one game or 2
	 * @state number of players
	*/
	public void setState(int state) {
		if (state == 1)
		{
			if (oneMaze == null)
			{
				oneMaze = new Controller.pacman.Maze();
				oneMaze.setNumoffield(1);
			}
			setGameMode(1);
			getOneMaze().pacMan.setPacmanColor();
			Logger.log("State Changes to " + state);
		}
		if (state == 2)
		{
			if (twoMaze == null)
			{
				twoMaze = new Controller.pacman2.Maze();
				twoMaze.setNumoffield(2);
			}
			setGameMode(2);
			getTwoMaze().pacMan.setPacmanColor();
			Logger.log("State Changes to " + state);
		}
	}
	/*
	 * Get the Maze for player 2
	*/
	public static Controller.pacman2.Maze getTwoMaze()
	{
		return twoMaze;
	}
	/*
	 * Gets the Maze for player 1
	*/
	public static Controller.pacman.Maze getOneMaze()
	{
		return oneMaze;
	}
	/*
	 * Sets user class
	 * @�userName
	 * @score
	 * @userNum
	*/
	public void setUser(String userName, int score, int userNum)
	{
		setU(new User(userName, score), userNum);
	}
	/*
	 * Get user
	 * @userNum
	*/
	public static User getU(int userNum) {
		if (userNum == 1)
		{
			return u1;
		}
		else
		{
			return u2;
		}
		
	}
	/*
	 * Set user
	 * @u
	 * @userNum
	*/
	public static void setU(User u, int userNum) {
		if (userNum == 1)
		{
			u1 = u;
		}
		else
		{
			u2 = u;
		}
	}
	/*
	 * Get user name
	 * @userNum
	*/
	public static String getUName(int userNum) {
		if (userNum == 1)
		{
			return u1.getName();
		}
		else
		{
			return u2.getName();
		}
	}
	/*
	 * Set user name
	 * @user user name
	 * @userNum
	*/
	public static void setUName(String user, int userNum) {
		if (userNum == 1)
		{
			u1.setName(user);
		}
		else
		{
			u2.setName(user);
		}
	}
	/*
	 * Set user score
	 * @u
	 * @userScore
	*/
	public static void setUScore(User u, int userScore) {
		u.setScore(userScore);
	}
	/*
	 * Get game mode
	*/
	public static int getGameMode() {
		return gameMode;
	}
	/*
	 * Set game mode
	 * @gameMode
	*/
	public static void setGameMode(int gameMode) {
		Game.gameMode = gameMode;
	}
	/*
	 * pause the chosen game
	 * @mazeNum
	*/
	public static void pauseGame(int mazeNum) {
		if (mazeNum == 1)
		{
			oneMaze.pauseGame();
			Logger.log("Paused Maze " + mazeNum);
		}
		else
		{
			twoMaze.pauseGame();
			Logger.log("Paused Maze " + mazeNum);
		}
	}
	/*
	 * Stop all active games, and pause timer. save to high scores if sets to true.
	 * @toSave boolean to save or not to save
	*/
	public static void stopAllGames(boolean toSave) throws Exception {
		u1Q = null;
		setUScore(u1, u1Score);
		if (toSave) SysData.addScore(u1);
		oneMaze.homeButton();
		Music.getInstance().homemusicbutton();
		if (timer != null) timer.cancel();
		if (time != null) time.cancel();
		time_interval = 0;
		if (twoMaze != null)
		{
			u2Q = null;
			setUScore(u2, u2Score);
			setMazeOn(1);
			if (toSave) SysData.addScore(u2);
			twoMaze.homeButton();
		}
	}
	/*
	 * Gets random question. updates the view and sends back the question
	 * @mazeNum
	*/
	public static Question getRandomQuestion(int mazeNum) {
		if (mazeNum == 1)
		{
			u1Q = SysData.getRandomQuestion();
			gameController.updateQustion(u1Q);
			Logger.log("Returning Q to " + mazeNum);
			return u1Q;
		}
		else
		{
			u2Q = SysData.getRandomQuestion();
			gameController2.updateQustion(u2Q);
			Logger.log("Returning Q to " + mazeNum);
			return u2Q;
		}
	}
	/*
	 * Updates the score at the view.
	 * @mazeNum
	 * @score
	*/
	public static void getScoreUpdate(int mazeNum, int score){
		if (mazeNum == 1)
		{
			u1Score = score;
			gameController.updateScore(u1Score);
		}
		else
		{
			u2Score = score;
			gameController2.updateScore(u2Score);
		}
	}
	/*
	 * Updates the View for life change
	 * @mazeNum
	 * @life
	*/
	public static void getLifeUpdate(int mazeNum, int life) {
		if (mazeNum == 1)
		{
			u1Life = life;
			gameController.updateLife(u1Life);
		}
		else
		{
			u2Life = life;
			gameController2.updateLife(u2Life);
		}
	}
	/*
	 * Gets score for user
	 * @u
	*/
	public static int getUScore(int u) {
		if (u == 1)
		{
			return u1Score;
		}
		else
		{
			return u2Score;
		}
	}
	/*
	 * Gets life for user
	 * @u
	*/
	public static int getULife(int u) {
		if (u == 1)
		{
			return u1Life;
		}
		else
		{
			return u2Life;
		}
	}
	/*
	 * Gets question for user
	 * @u
	*/
	public static Question getUQ(int u) {
		if (u == 1)
		{
			return u1Q;
		}
		else
		{
			return u2Q;
		}
	}
	/*
	 * Starts the timer for 2 players mode.
	 * cancel the timer after interval period of time.
	 * update the maze that is on the screen, pause that game, and replace to other player.
	 * updates the timer at the view.
	*/
	public static void startTimer()
	{
		timer = null;
	    int delay = 1000;
	    int period = 1000;
	    timer = new Timer();
	    timer_interval = Config.getTime_between_players();
	    timer.scheduleAtFixedRate(new TimerTask()
	    {
			public void run()
	        {
				Platform.runLater(() -> {
		        	if (timer_interval == 0)
		    	    {
		        		timer.cancel();
		    	        if (getMazeOn() == 1)
		    	        {
		    	        	pauseGame(1);
		    	        	setMazeOn(2);
		    	        	gameController.replaceGame();
		    	        	if(getTwoMaze().isquestionon)
							{
								Music.getInstance().questionCandy();
								Music.getInstance().eatenquestion = 1;
							}
							else
							{
								Music.getInstance().homemusicbutton();
							}
		    	        	return;
		    	        }
		    	        else
		    	        {
		    	        	pauseGame(2);
		    	        	setMazeOn(1);
		    	        	gameController2.replaceGame();
							if(getOneMaze().isquestionon)
							{
								Music.getInstance().questionCandy();
								Music.getInstance().eatenquestion = 1;
							}
							else
							{
								Music.getInstance().homemusicbutton();
							}
		    	        	return;
		    	        }
		    	    }
		        	else --timer_interval;
		        	if (getMazeOn() == 1) gameController.updateTimer(timer_interval);
		        	if (getMazeOn() == 2) gameController2.updateTimer(timer_interval);
	        	});
	        }
	    }, delay, period);
	}

	/*
	 * Gets the current maze that on the screen.
	*/
	public static int getMazeOn() {
		return MazeOn;
	}
	/*
	 * Sets the current maze that on the screen.
	 * @mazeOn
	*/
	public static void setMazeOn(int mazeOn) {
		MazeOn = mazeOn;
	}
	/*
	 * Pause the timer
	*/
	public void pauseTimer() {
		if (timer != null) timer.cancel();
		timer = null;
	}
	/*
	 * resume the timer.
	*/
	public void resumeTimer() {
		timer = new Timer();
	    int delay = 1000;
	    int period = 1000;
	    timer = new Timer();
	    timer.scheduleAtFixedRate(new TimerTask()
	    {
			public void run()
	        {
				Platform.runLater(() -> {
		        	if (timer_interval == 0)
		    	    {
		        		timer.cancel();
		    	        if (getMazeOn() == 1)
		    	        {
		    	        	pauseGame(1);
		    	        	setMazeOn(2);
		    	        	gameController.replaceGame();
		    	        	return;
		    	        }
		    	        else
		    	        {
		    	        	pauseGame(2);
		    	        	setMazeOn(1);
		    	        	gameController2.replaceGame();
		    	        	return;
		    	        }
		    	    }
		        	else --timer_interval;
		        	if (getMazeOn() == 1) gameController.updateTimer(timer_interval);
		        	if (getMazeOn() == 2) gameController2.updateTimer(timer_interval);
	        	});
	        }
	    }, delay, period);
	}
	/*
	 * Sends end game to view, that send back the command to save the scores.
	*/
	public static void endGame() throws Exception {
		if (getMazeOn() == 1) gameController.endGame();
    	if (getMazeOn() == 2) gameController2.endGame();
	}
	/*
	 * Starts the time
	 * adds 1 any second
	 * update the maze that is on the screen
	*/
	public static void startTime()
	{
		time = null;
	    int delay = 1000;
	    int period = 1000;
	    time = new Timer();
	    time.scheduleAtFixedRate(new TimerTask()
	    {
			public void run()
	        {
				Platform.runLater(() -> {
		        	++time_interval;
		        	if (getMazeOn() == 1) gameController.updateTime(time_interval);
		        	if (getMazeOn() == 2) gameController2.updateTime(time_interval);
	        	});
	        }
	    }, delay, period);
	}
	/*
	 * Pause the time
	*/
	public void pauseTime() {
		if (time != null) time.cancel();
		time = null;
	}
	/*
	 * reset the time to 0
	*/
	public void resetTime() {
		if (time != null) time.cancel();
		time_interval = 0;
	}
	/*
	 * resume the time
	*/
	public void resumeTime()
	{
		time = null;
	    int delay = 1000;
	    int period = 1000;
	    time = new Timer();
	    time.scheduleAtFixedRate(new TimerTask()
	    {
			public void run()
	        {
				Platform.runLater(() -> {
		        	++time_interval;
		        	if (getMazeOn() == 1) gameController.updateTime(time_interval);
		        	if (getMazeOn() == 2) gameController2.updateTime(time_interval);
	        	});
	        }
	    }, delay, period);
	}
}

/*
OLD VERSION

oneMaze = null;
gameMode = 1;
u1 = null;
u1Score = 0;
u1Life = 3;
u1Q = null;
interval = 0;
if (timer != null) timer.cancel();
if (twoMaze != null)
{
	twoMaze = null;
	setUScore(u2, u2Score);
	if (toSave) SysData.addScore(u2);
	u2 = null;
	u2Score = 0;
	u2Life = 3;
	u2Q = null;
	MazeOn = 1;
	setGameMode(1);
}
*/