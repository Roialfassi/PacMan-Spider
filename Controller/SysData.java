package Controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import Model.AllQuestions;
import Model.Config;
import Model.Question;
import Model.Scores;
import Model.User;

public class SysData
{
	// Path to JSON files
	private static String JsonPath_scores = "Pac-Man-master\\src\\Resources\\JSON\\scores.json";
	private static String JsonPath_questions = "Pac-Man-master\\src\\Resources\\JSON\\questions.json";
	private static String JsonPath_config = "Pac-Man-master\\src\\Resources\\JSON\\configuration.json";
	static Scores scores = Scores.getInstance();
	static AllQuestions allQuestions = AllQuestions.getInstance();
	/*
	 * Parse the configuration from JSON
	*/
	public static void parseConfiguration() throws Exception 
	{ 
		// parsing file
		Object obj = new JSONParser().parse(new FileReader(JsonPath_config)); 
		
		// type casting obj to JSONObject 
		JSONObject jo = (JSONObject) obj; 

		JSONArray configurationArray = (JSONArray) jo.get("configuration");
		for (int i = 0; i < configurationArray.size(); i++) {
			JSONObject a = (JSONObject) configurationArray.get(i);
			int pacman_life = Integer.parseInt((String) a.get("pacman_life"));
			Config.setPacman_life(pacman_life);
			int time_between_players = Integer.parseInt((String) a.get("time_between_players"));
			Config.setTime_between_players(time_between_players);
			int silver_dot_value = Integer.parseInt((String) a.get("silver_dot_value"));
			Config.setSilver_dot_value(silver_dot_value);
			String commands = (String) a.get("commands");
			Config.setCommands(commands);
			String pacman_color = (String) a.get("pacman_color");
			Config.setPacman_color(pacman_color);
		}
		Logger.log("Finished Parse Configuration");
	}
	/*
	 * Parse the scores from scores JSON
	*/
	public static void parseScores() throws Exception 
	{ 
		// parsing file
		Object obj = new JSONParser().parse(new FileReader(JsonPath_scores)); 
		
		// type casting obj to JSONObject 
		JSONObject jo = (JSONObject) obj; 

		JSONArray scoresArray = (JSONArray) jo.get("scores");
		for (int i = 0; i < scoresArray.size(); i++) {
			JSONObject a = (JSONObject) scoresArray.get(i);

			String name = (String) a.get("user");
			int score = Integer.parseInt((String) a.get("score"));
			scores.addUser(new User(name, score));
		}
		Logger.log("Finished Parse Scores");
	}
	/*
	 * Parse the questions from JSON.
	 * @mazeOn
	*/
	@SuppressWarnings("unchecked")
	public static void parseQuestions() throws Exception 
	{ 
		// parsing file
		Object obj = new JSONParser().parse(new FileReader(JsonPath_questions));
		
		// type casting obj to JSONObject 
		JSONObject jo = (JSONObject) obj; 

		JSONArray questionsArray = (JSONArray) jo.get("questions");
		for (int i = 0; i < questionsArray.size(); i++) {
			JSONObject q = (JSONObject) questionsArray.get(i);
			
			String qQuestion = (String) q.get("question");
			String qRightAnswer = (String) q.get("correct_ans");
			String qLevel = (String) q.get("level");
			String qTeam = (String) q.get("team");
			List<String> qAnswers = (List<String>) q.get("answers");
			allQuestions.addQuestion(new Question(qQuestion, qAnswers, qRightAnswer, qLevel, qTeam));
		}
		Logger.log("Finished Parse Questions");
	}
	/*
	 * Delete question and save new file
	 * @q
	*/
	public static boolean deleteQuestion(Question q) throws Exception 
	{
		Object obj = new JSONParser().parse(new FileReader(JsonPath_questions)); 
		JSONObject jo = (JSONObject) obj;
		JSONArray questionsArray = (JSONArray) jo.get("questions");
		for (int i = 0; i < questionsArray.size(); i++)
		{
			JSONObject Question = (JSONObject) questionsArray.get(i);
			String qQuestion = (String) Question.get("question");
			if (q.getQuestion().equals(qQuestion))
			{
				questionsArray.remove(i);
				createJsonFile(questionsArray.toJSONString(), JsonPath_questions, "questions");
			}
		}
		if (allQuestions.deleteQ(q))
		{
			Logger.log("Q has been deleted");
			return true;
		}
		Logger.log("Could not delete Q");
		return false;
	}
	/*
	 * Add question to the questions, and save file.
	 * @tempQuestion
	 * @tempteam
	 * @tempqLevel
	 * @tempqRightAnswer
	 * @tempqAnswer1
	 * @tempqAnswer2
	 * @tempqAnswer3
	 * @tempqAnswer4
	*/
	@SuppressWarnings("unchecked")
	public static void addQuestion(String tempQuestion, String tempteam, String tempqLevel, String tempqRightAnswer,
			String tempqAnswer1, String tempqAnswer2, String tempqAnswer3, String tempqAnswer4) throws Exception 
	{
		Question QuestionNew = new Question(tempQuestion, tempqAnswer1, tempqAnswer2, tempqAnswer3, tempqAnswer4, 
				tempqRightAnswer, tempqLevel, tempteam);
		String QuestionString = QuestionNew.toJson();
		JSONParser parser = new JSONParser();
		JSONObject Questionjson = (JSONObject) parser.parse(QuestionString);
		Object obj = new JSONParser().parse(new FileReader(JsonPath_questions)); 
		JSONObject jo = (JSONObject) obj;
		JSONArray questionsArray = (JSONArray) jo.get("questions");
		questionsArray.add(Questionjson);
		createJsonFile(questionsArray.toJSONString(), JsonPath_questions, "questions");
		if (allQuestions.addQuestion(QuestionNew))
		{
			Logger.log(tempQuestion+" Has been added");
		}
		else
		{
			Logger.log("Could not add "+tempQuestion);
		}
	}
	/*
	 * Add score to scores list and update file.
	 * @u the user to add to JSON file
	*/
	@SuppressWarnings("unchecked")
	public static void addScore(User u) throws Exception 
	{
		Object obj = new JSONParser().parse(new FileReader(JsonPath_scores)); 
		JSONObject jo = (JSONObject) obj; 
		String UserString = u.toJson();
		JSONParser parser = new JSONParser();
		JSONObject Userjson = (JSONObject) parser.parse(UserString);
		JSONArray scoresArray = (JSONArray) jo.get("scores");
		scoresArray.add(Userjson);
		createJsonFile(scoresArray.toJSONString(), JsonPath_scores, "scores");
		if (scores.addUser(u))
		{
			Logger.log(u.getName()+" Has been added");
		}
		else
		{
			Logger.log("Could not add "+u.getName());
		}
	}
	/*
	 * Saves new JSON file.
	 * @toWrite the content
	 * @filePath the path of the JSON
	 * @type scores or questions
	*/
	private static void createJsonFile(String toWrite, String filePath, String type)
{
	PrintWriter writer;
	try {
		writer = new PrintWriter(filePath, "UTF-8");
		writer.println("{\""+type+"\"" + toWrite + "}");
		writer.close();
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}
}
	/*
	 * Mark question for editing
	 * @q
	*/
	public static boolean markQuestionToEdit(Question q) {
		if (allQuestions.markToEdit(q))
		{
			Logger.log("Q has been mark for edit");
			return true;
		}
		Logger.log("Could not mark Q");
		return false;
	}
	/*
	 * Update question and save the updated file.
	 * @tempQuestion
	 * @tempteam
	 * @tempqLevel
	 * @tempqRightAnswer
	 * @tempqAnswer1
	 * @tempqAnswer2
	 * @tempqAnswer3
	 * @tempqAnswer4
	*/
	@SuppressWarnings("unchecked")
	public static void updateQ(String tempQuestion, String tempteam, String tempqLevel, String tempqRightAnswer,
			String tempqAnswer1, String tempqAnswer2, String tempqAnswer3, String tempqAnswer4) throws Exception 
	{
		Question QuestionNew = new Question(tempQuestion, tempqAnswer1, tempqAnswer2, tempqAnswer3, tempqAnswer4, 
				tempqRightAnswer, tempqLevel, tempteam);
		String QuestionString = QuestionNew.toJson();
		JSONParser parser = new JSONParser();
		JSONObject Questionjson = (JSONObject) parser.parse(QuestionString);
		Object obj = new JSONParser().parse(new FileReader(JsonPath_questions)); 
		JSONObject jo = (JSONObject) obj;
		JSONArray questionsArray = (JSONArray) jo.get("questions");
		for (int i = 0; i < questionsArray.size(); i++)
		{
			JSONObject Question = (JSONObject) questionsArray.get(i);
			String qQuestion = (String) Question.get("question");
			if (tempQuestion.equals(qQuestion))
			{
				questionsArray.remove(i);
				questionsArray.add(Questionjson);
				createJsonFile(questionsArray.toJSONString(), JsonPath_questions, "questions");
			}
		}
		if (allQuestions.updateQ( tempQuestion,  tempteam,  tempqLevel,  tempqRightAnswer,
				 tempqAnswer1,  tempqAnswer2,  tempqAnswer3,  tempqAnswer4))
		{
			Logger.log(tempQuestion+" has been edited");
		}
		else
		{
			Logger.log("Could not edit "+tempQuestion);
		}
	}
	/*
	 * update configuration.
	*/
	@SuppressWarnings("unchecked")
	public static void updateConfiguration(int pacman_life, int time_between_players, int silver_dot_value, String commands, String pacman_color) throws Exception, Exception, Exception {
		Config.setPacman_life(pacman_life);
		Config.setTime_between_players(time_between_players);
		Config.setSilver_dot_value(silver_dot_value);
		Config.setCommands(commands);
		Config.setPacman_color(pacman_color);
		JSONObject configjson = new JSONObject();
		configjson.put("pacman_life", Integer.toString(pacman_life));
		configjson.put("time_between_players", Integer.toString(time_between_players));
		configjson.put("silver_dot_value", Integer.toString(silver_dot_value));
		configjson.put("commands", commands);
		configjson.put("pacman_color", pacman_color);
		String toPrint = "[" + configjson.toJSONString() + "]";
		createJsonFile(toPrint, JsonPath_config, "configuration");
		Logger.log("Configuration Saved - " + Integer.toString(pacman_life) + " " +
				Integer.toString(time_between_players) + " " + Integer.toString(silver_dot_value) + 
				" " + commands + " " + pacman_color);
	}
	/*
	 * Get the list of scores.
	*/
	public static List<User> getScores() {
		List<User> scoresList = scores.getScoreList();
		return scoresList;
	}
	/*
	 * Get the list of questions.
	*/
	public static List<Question> getQuestions() {
		List<Question> questionsList = allQuestions.getQuestionsList();
		return questionsList;
	}
	/*
	 * Sends back random question.
	*/
	public static Question getRandomQuestion() {
		return allQuestions.getRandomQ();
	}
} 
