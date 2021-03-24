package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="answers")
@NamedQuery(name="Answer.findByUser",query="SELECT a FROM Answer a WHERE a.questionnaire.id=?1 AND a.user.id=?2 ")
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
