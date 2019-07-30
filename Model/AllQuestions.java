package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import Model.Question;;

public class AllQuestions {
	private static AllQuestions AllQuestions;
	private List<Question> allQuestions;
	private AllQuestions() {
	}
	/*
	 * Get instance of all questions
	*/
	public static AllQuestions getInstance() {
		if (AllQuestions == null) {
			AllQuestions = new AllQuestions();
		}
		return AllQuestions;
	}
	/*
	 * Get all questions
	*/
	public List<Question> getQuestionsList() {
		return this.allQuestions;
	}
	/*
	 * Get the amount of all questions
	 * @res the sound
	*/
	public int getSize() {
		return this.allQuestions.size();
	}
	/*
	 * Get specific question
	 * @i index for that question
	*/
	public Question getIndex(int i) {
		return this.allQuestions.get(i);
	}
	/*
	 * Add question
	 * @q
	*/
	public boolean addQuestion(Question q) {
		if (this.allQuestions == null)
			this.allQuestions = new ArrayList<Question>();
		this.allQuestions.add(q);
		return true;
	}
	/*
	 * Delete question
	 * @q
	*/
	public boolean deleteQ(Question q) {
		if (this.allQuestions.remove(q))
		{
			return true;
		}
		return false;
	}
	/*
	 * Mark question for editing
	 * @q
	*/
	public boolean markToEdit(Question q) {
		for (int i = 0; i < this.allQuestions.size(); i++)
		{
			if (q.getQuestion().equals(this.allQuestions.get(i).getQuestion()))
			{
				this.allQuestions.get(i).setqEdit(true);
				return true;
			}
		}
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
	public boolean updateQ(String tempQuestion, String tempteam, String tempqLevel, String tempqRightAnswer,
			String tempqAnswer1, String tempqAnswer2, String tempqAnswer3, String tempqAnswer4) {
		for (int i = 0; i < this.allQuestions.size(); i++)
		{
			if (tempQuestion.equals(this.allQuestions.get(i).getQuestion()))
			{
				this.allQuestions.get(i).setTeam(tempteam);
				this.allQuestions.get(i).setqEdit(false);
				this.allQuestions.get(i).setqLevel(tempqLevel);
				this.allQuestions.get(i).setqRightAnswer(tempqRightAnswer);
				this.allQuestions.get(i).setAnswer1(tempqAnswer1);
				this.allQuestions.get(i).setAnswer2(tempqAnswer2);
				this.allQuestions.get(i).setAnswer3(tempqAnswer3);
				this.allQuestions.get(i).setAnswer4(tempqAnswer4);
				return true;
			}
		}
		return false;
	}
	/*
	 * Gets random question
	*/
	public Question getRandomQ()
	{
		int min = 0;
		int max = this.getSize()-1;
		int randomNum = ThreadLocalRandom.current().nextInt(min, max);
		return this.allQuestions.get(randomNum);
		
	}
}
