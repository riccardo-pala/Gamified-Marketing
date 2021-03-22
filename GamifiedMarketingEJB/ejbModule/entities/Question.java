package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="questions")
public class Question {
	
	@Id
	@Column(name="idquestions")
	private int id;
	
	private String content;
	
	@ManyToOne
	@JoinColumn(name="questionnaireid")
	private Questionnaire questionnaire;
	
	


}
