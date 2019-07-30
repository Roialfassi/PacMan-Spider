package Controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Music {
	private static Music instance = null;
	public static int NOT_MUTED = 1;
	public static int MUTED = 0;
	public  int eatenquestion =0;
	public int isMusicPlaying = NOT_MUTED;
	private Map<URL, MediaPlayer> soundMap;
	private Music() {
	}
	/*
	 * Get instance of the music
	 */
	public static Music getInstance() {
		if (instance == null) {
			instance = new Music();
			instance.soundMap = new HashMap<>();
		}
		return instance;
	}

	/*
	 * Get the resource for the sound state.
	 * @res the sound
	 */
	public String getSoundState(String res)
	{
		final URL resource = getResource(res);
		if (this.isMusicPlaying == MUTED) {
			// Currently paused, play it
			this.isMusicPlaying = MUTED;
			return "/Resources/Images/soundNOT.png";
		} else {
			this.isMusicPlaying = NOT_MUTED;
			return "/Resources/Images/sound.png";
		}
	}
	/*
	 * PLay a resource
	 * @res the sound to play
	 */
	public void play(String res) {
		final URL resource = getResource(res);
		Logger.log("Getting resource: " + resource);
		if (!soundMap.containsKey(resource)) {
			final Media media = new Media(resource.toString());
			final MediaPlayer mediaPlayer = new MediaPlayer(media);
			soundMap.put(resource, mediaPlayer);

		}
		soundMap.get(resource).seek(Duration.ZERO);
		soundMap.get(resource).play();
	}
	/*
	 * Swap the sound state
	 * @res the sound itself
	 */
	public String swap(String res) {
		final URL resource = getResource(res);
		if (soundMap.containsKey(resource)) {
			MediaPlayer currentPlayer = soundMap.get(resource);
			if (this.isMusicPlaying == MUTED) {
				// Currently paused, play it
				realswap(NOT_MUTED);
				this.isMusicPlaying = NOT_MUTED;
				return "/Resources/Images/sound.png";
			} else {
				realswap(MUTED);
				this.isMusicPlaying = MUTED;
				return "/Resources/Images/soundNOT.png";
			}
		} else{
			play(res);
			this.isMusicPlaying = NOT_MUTED;
			return "/Resources/Images/sound.png";
		}
	}

	/**
	 * changing the volume to mute or unmute
	 * @param muteornot
	 */
	public void realswap(int muteornot) {
		for(MediaPlayer m : soundMap.values()) {
			if(muteornot == MUTED)
			{
				m.setVolume(-1.0);
			}
			else
			{
				m.setVolume(1.0);
			}
		}
		if(muteornot != MUTED)
			changeVolume("theme.mp3", 20.0);
	}

	/*
	 * change the volume of a sound
	 * @res the sound itself
	 * @vol the volume to change to
	 */
	public void changeVolume(String res, Double vol) {
		final URL resource = getResource(res);
		if (soundMap.containsKey(resource)) {
			MediaPlayer currentPlayer = soundMap.get(resource);
			Logger.log("Changing volume to " + res + " - " + vol);
			currentPlayer.setVolume(vol / 100);
		}
	}
	/*
	 * Gets resource for sound
	 * @res the path
	 */
	private URL getResource(String res) {
		final URL resource = getClass().getResource("/Resources/Music/" + res);
		System.out.println(resource);
		Logger.log("Aquired resource: " + resource);
		return resource;
	}
	/*
	 * stop the sound
	 * @res the sound itself
	 */
	public void stop(String key) {
		final URL resource = getResource(key);
		if (soundMap.containsKey(resource)) {
			soundMap.get(resource).stop();
		}
	}

	/**
	 * after eating money
	 */
	public void playsilverdot()
	{

		this.play("SilverDot.wav");
		this.changeVolume("SilverDot.wav" , ((double) this.isMusicPlaying)*100);
	}

	/**
	 * after eating a poison dot
	 */
	public void playPoisonDot()
	{
		if(eatenquestion == 0)
		{
			this.play("pacman_death.wav");
			this.changeVolume("pacman_death.wav" , ((double) this.isMusicPlaying)*100);
		}
		else
		{
			eatenquestion = 0;
			this.stop("QuestionRunning.wav");
			this.play("pacman_death.wav");
			this.changeVolume("pacman_death.wav" , ((double) this.isMusicPlaying)*100);
			this.play("theme.mp3");
		}
	}

	/**
	 * after eating a question candy
	 */
	public void questionCandy()
	{
		eatenquestion = 1;
		this.stop("theme.mp3");
		this.play("question_candy.wav");
		this.changeVolume("question_candy.wav" , ((double) this.isMusicPlaying)*100);
		this.play("QuestionRunning.wav");
		this.changeVolume("QuestionRunning.wav" , ((double) this.isMusicPlaying)*100);
		this.createcycle("QuestionRunning.wav" , 6);
	}


	/**
	 * after meeting question Ghost
	 * @param num
	 */
	public void musicPacmanMeetsQuestionGhost(int num)
	{
		this.eatenquestion = 0;
		if (num == 1)//ate the right answer
		{
			this.stop("QuestionRunning.wav");
			this.play("Wow.mp3");
			this.changeVolume("Wow.mp3" , ((double) this.isMusicPlaying)*100);
			this.play("theme.mp3");
		}
		if (num == 2) {
			this.stop("QuestionRunning.wav");
			this.play("pacmanmeetsQuestionghost.wav");
			this.changeVolume("pacmanmeetsQuestionghost.wav" , ((double) this.isMusicPlaying)*100);
			this.play("theme.mp3");
		}
	}

	/**
	 * after meeting regular ghost
	 */
	public void musicPacmanMeetsRegularGhost()
	{
		if (eatenquestion == 1)//ate the right answer
		{
			this.eatenquestion = 0;
			this.stop("QuestionRunning.wav");
			this.play("pacman_meets_ghost.wav");
			this.changeVolume("pacman_meets_ghost.wav" , ((double) this.isMusicPlaying)*100);
			this.play("theme.mp3");
		}
		if (eatenquestion == 0) {
			this.play("pacman_meets_ghost.wav");
			this.changeVolume("pacman_meets_ghost.wav" , ((double) this.isMusicPlaying)*100);
		}
	}

	/**
	 * after pressing home button
	 */
	public void homemusicbutton()
	{
		if (eatenquestion == 1)//ate the right answer
		{
			this.eatenquestion = 0;
			this.stop("QuestionRunning.wav");
			this.play("theme.mp3");
		}
	}

	/**
	 * Repeat the song
	 * @param res
	 * @param numofccycles
	 */
	public void createcycle(String res , int numofccycles) {
		final URL resource = getResource(res);
		MediaPlayer mediaPlayer;
		if (!soundMap.containsKey(resource)) {
			Media media = new Media(resource.toString());
			mediaPlayer = new MediaPlayer(media);
		}
		soundMap.get(resource).setCycleCount(numofccycles);


	}
}