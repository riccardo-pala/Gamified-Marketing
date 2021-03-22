package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="answers")
@IdClass(AnswerId.class)
public class Answer {
	
	@Id
	@ManyToOne
	@JoinColumn(name="userid")
	private User user;
	
	@Id
	@ManyToOne
	@JoinColumn(name="questionid")
	private Question question;
	
	@Id
	@ManyToOne
	@JoinColumn(name="questionnaireid")
	private Questionnaire questionnaire;
	
	



	
	
	
	

}
