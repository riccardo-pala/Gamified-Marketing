package entities;

import java.io.Serializable;

public class AnswerId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	private int user;
	
	private int question;
	
	private int questionnaire;

	
	public AnswerId() {	
	}
	

	public int getUserid() {
		return user;
	}

	public void setUserid(int userid) {
		this.user = userid;
	}

	public int getQuestionid() {
		return question;
	}

	public void setQuestionid(int questionid) {
		this.question = questionid;
	}

	public int getQuestionnaireid() {
		return questionnaire;
	}

	public void setQuestionnaireid(int questionnaireid) {
		this.questionnaire = questionnaireid;
	}
	
	public int hashCode() {
		return question + user + questionnaire;
	}
	
	public boolean equals(Object o) {
	
		return ((o instanceof AnswerId) &&
				questionnaire == ((AnswerId) o).getQuestionnaireid()) &&
				question == (((AnswerId) o).getQuestionid()) &&
				user == (((AnswerId) o).getUserid());
	}
}
