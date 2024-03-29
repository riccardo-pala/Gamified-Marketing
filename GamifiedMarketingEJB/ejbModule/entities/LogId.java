package entities;

import java.io.Serializable;
import java.sql.Timestamp;


public class LogId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int user;
	
	private Timestamp accessTime;
	
	private int questionnaire;

	
	public LogId() {
	}
	
	
	public int getUser() {
		return user;
	}
	
	public void setUser(int userId) {
		this.user = userId;
	}

	public Timestamp getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Timestamp accessTime) {
		this.accessTime = accessTime;
	}

	public int getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(int questionnaire) {
		this.questionnaire = questionnaire;
	}

	public int hashCode() {
		return accessTime.hashCode() + user;
	}
	
	public boolean equals(Object o) {
	
		return ((o instanceof LogId) &&
				questionnaire == ((LogId) o).getQuestionnaire()) &&
				accessTime.equals(((LogId) o).getAccessTime());
	}
}
