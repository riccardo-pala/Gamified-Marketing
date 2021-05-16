package entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="answers")
@NamedQueries({
	@NamedQuery(name="Answer.findByQuestionnaire",query="SELECT a FROM Answer a WHERE a.questionnaire.id = ?1 "),
	@NamedQuery(name="Answer.findByQuestionnaireAndUser",query="SELECT a FROM Answer a WHERE a.questionnaire.id = ?1 and a.user.id = ?2 ")
})
@IdClass(AnswerId.class)
public class Answer implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@ManyToOne
	@JoinColumn(name="userid")
	private User user;
	
	@Id
	@ManyToOne //(cascade=CascadeType.PERSIST) lo levo perché salvando le answers crea delle questions nuove anzichè prendere l'id di quelle esistenti
	@JoinColumn(name="questionid")
	private Question question;
	
	@Id
	@ManyToOne
	@JoinColumn(name="questionnaireid")
	private Questionnaire questionnaire;
	
	private String text;
	
	
	public Answer() {
	}

	public Answer(User user, Question question, Questionnaire questionnaire, String text) {
		this.user = user;
		this.question = question;
		this.questionnaire = questionnaire;
		this.text = text;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
