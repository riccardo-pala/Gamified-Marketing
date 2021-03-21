package entities;

import java.io.Serializable;



import org.eclipse.persistence.jpa.jpql.parser.DateTime;


public class AccessId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int user;
	
	private DateTime accessTime;
	
	private int questionare;

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



	public int getQuestionare() {
		return questionare;
	}



	public void setQuestionare(int questionare) {
		this.questionare = questionare;
	}



	public int hashCode() {
		return accessTime.hashCode()+user;
	}
	
	
	
	public boolean equals(Object o) {
	
		return ((o instanceof AccessId) &&
				questionare==((AccessId)o).getQuestionare())&&
				accessTime.equals(((AccessId)o).getAccessTime());
				}
	
	
	

}
