package Model;

import java.util.ArrayList;
import java.util.List;

import Model.User;;

public class Scores {
	private static Scores Scores;
	private List<User> scores;
	private Scores() {
	}
	/*
	 * Get instance of scores
	*/
	public static Scores getInstance() {
		if (Scores == null) {
			Scores = new Scores();
		}
		return Scores;
	}
	/*
	 * Create scores list
	*/
	public List<User> createScoreList() {
		if (this.scores == null)
			this.scores = new ArrayList<User>();
		return this.scores;
	}
	/*
	 * Get score list
	*/
	public List<User> getScoreList() {
		return this.scores;
	}
	/*
	 * Add new user to score list
	*/
	public boolean addUser(User user) {
		if (this.scores == null)
			this.scores = new ArrayList<User>();
		this.scores.add(user);
		return true;
	}
	/*
	 * Get the amount of scores
	*/
	public int getSize() {
		return this.scores.size();
	}
	/*
	 * Get specified score
	*/
	public User getIndex(int i) {
		return this.scores.get(i);
	}
}
