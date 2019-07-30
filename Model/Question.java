package Model;

import java.util.List;

public class Question {
	private String qQuestion;
	private String qAnswer1;
	private String qAnswer2;
	private String qAnswer3;
	private String qAnswer4;
	private String qRightAnswer;
	private String qLevel = "0";
	private String qTeam;
	private boolean qEdit;
	/*
	 * Create new question
	*/
	public Question(String qQuestion, List<String> qAnswers, 
			String qRightAnswer, String qLevel, String qTeam) {
		super();
		this.qQuestion = qQuestion;
		this.qAnswer1 = qAnswers.get(0);
		this.qAnswer2 = qAnswers.get(1);
		this.qAnswer3 = qAnswers.get(2);
		this.qAnswer4 = qAnswers.get(3);
		this.qRightAnswer = qRightAnswer;
		this.qLevel = qLevel;
		this.qTeam = qTeam;
		this.setqEdit(false);
	}
	/*
	 * Create new question
	*/
	public Question(String qQuestion, String qAnswer1,String qAnswer2,String qAnswer3,String qAnswer4, 
			String qRightAnswer, String qLevel, String qTeam) {
		super();
		this.qQuestion = qQuestion;
		this.qAnswer1 = qAnswer1;
		this.qAnswer2 = qAnswer2;
		this.qAnswer3 = qAnswer3;
		this.qAnswer4 = qAnswer4;
		this.qRightAnswer = qRightAnswer;
		this.qLevel = qLevel;
		this.qTeam = qTeam;
		this.setqEdit(false);
	}
	/*
	 * Get level
	*/
	public String getLevel() {
		return this.qLevel;
	}
	/*
	 * Set level
	*/
	protected void setqLevel(String Level) {
		this.qLevel = Level;
	}
	/*
	 * Get team
	*/
	public String getTeam() {
		return this.qTeam;
	}
	/*
	 * Set team
	*/
	protected void setTeam(String qTeam) {
		this.qTeam = qTeam;
	}
	/*
	 * Get question
	*/
	public String getQuestion() {
		return this.qQuestion;
	}
	/*
	 * Set question
	*/
	protected void setqQuestion(String qQuestion) {
		this.qQuestion = qQuestion;
	}
	/*
	 * Get String of question
	*/
	@Override
	public String toString() {
		return "qQuestion=" + this.qQuestion +
				", qAnswer1=" + this.qAnswer1 + ", qAnswer2=" + this.qAnswer2 + ", qAnswer3=" + this.qAnswer3 + ", qAnswer4=" + this.qAnswer4 +
				", qRightAnswer=" + this.getRightAnswer() + 
				", qTeam=" + this.qTeam + ", qLevel=" + this.qLevel;
	}
	/*
	 * Get Answer 1
	*/
	public String getAnswer1() {
		return qAnswer1;
	}
	/*
	 * Set Answer 1
	*/
	public void setAnswer1(String qAnswer1) {
		this.qAnswer1 = qAnswer1;
	}
	/*
	 * Get Answer 2
	*/
	public String getAnswer2() {
		return qAnswer2;
	}
	/*
	 * Set Answer 2
	*/
	public void setAnswer2(String qAnswer2) {
		this.qAnswer2 = qAnswer2;
	}
	/*
	 * Get Answer 3
	*/
	public String getAnswer3() {
		return qAnswer3;
	}
	/*
	 * Set Answer 3
	*/
	public void setAnswer3(String qAnswer3) {
		this.qAnswer3 = qAnswer3;
	}
	/*
	 * Get Answer 4
	*/
	public String getAnswer4() {
		return qAnswer4;
	}
	/*
	 * Set Answer 4
	*/
	public void setAnswer4(String qAnswer4) {
		this.qAnswer4 = qAnswer4;
	}
	/*
	 * Get the string of the right answer
	*/
	public String getRightAnswerString() {
		if (qRightAnswer.equals("1")) return this.qAnswer1;
		if (qRightAnswer.equals("2")) return this.qAnswer2;
		if (qRightAnswer.equals("3")) return this.qAnswer3;
		return this.qAnswer4;
	}
	/*
	 * Get right answer
	*/
	public String getRightAnswer() {
		return qRightAnswer;
	}
	/*
	 * Get the status of editing this question
	*/
	public boolean isqEdit() {
		return qEdit;
	}
	/*
	 * Mark question for editing
	*/
	public void setqEdit(boolean qEdit) {
		this.qEdit = qEdit;
	}
	/*
	 * Set the right answer
	*/
	public void setqRightAnswer(String tempqRightAnswer) {
		this.qRightAnswer = tempqRightAnswer;
	}
	/*
	 * Writes the question in a poper way for  JSON file
	*/
	public String toJson()
	{
		return "{\r\n" + 
				"      \"question\": \""+ this.qQuestion +"\",\r\n" + 
				"      \"answers\": [\r\n" + 
				"        \""+this.qAnswer1+"\",\r\n" + 
				"        \""+this.qAnswer2+"\",\r\n" + 
				"        \""+this.qAnswer3+"\",\r\n" + 
				"        \""+this.qAnswer4+"\"\r\n" + 
				"      ],\r\n" + 
				"      \"correct_ans\": \""+this.qRightAnswer+"\",\r\n" + 
				"      \"level\": \""+this.qLevel+"\",\r\n" + 
				"      \"team\": \""+this.qTeam+"\"\r\n" + 
				"    }";
	}
}
