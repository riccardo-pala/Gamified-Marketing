package entities;

import java.io.Serializable;



import org.eclipse.persistence.jpa.jpql.parser.DateTime;


public class AccessId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int user;
	
	private DateTime accessTime;
	
	private int questionnaire;

	public AccessId() {
		
	}
	
	
	
	public int getUser() {
		return user;
	}



	public void setUser(int userId) {
		this.user = userId;
	}



	public DateTime getAccessTime() {
		return accessTime;
	}



	public void setAccessTime(DateTime accessTime) {
		this.accessTime = accessTime;
	}



	public int getQuestionnaire() {
		return questionnaire;
	}



	public void setQuestionnaire(int questionnaire) {
		this.questionnaire = questionnaire;
	}



	public int hashCode() {
		return accessTime.hashCode()+user;
	}
	
	
	
	public boolean equals(Object o) {
	
		return ((o instanceof AccessId) &&
				questionnaire==((AccessId)o).getQuestionnaire())&&
				accessTime.equals(((AccessId)o).getAccessTime());
				}
	
	
	

}
