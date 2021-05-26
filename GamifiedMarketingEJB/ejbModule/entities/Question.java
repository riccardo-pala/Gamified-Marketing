package entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="questions")
@Inheritance
@DiscriminatorColumn(name="section")
public abstract class Question {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String text;

	@ManyToOne
	@JoinColumn(name="questionnaireid")
	private Questionnaire questionnaire;
	
	@OneToMany(mappedBy="question", cascade={CascadeType.REMOVE,CascadeType.REFRESH,CascadeType.PERSIST})
	private List<Answer> answers;
	
	
	public Question() {
	}
	
	public Question(String text) {
		this.text = text;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}
	
	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
	public void addAnswer(Answer answer) {
		this.getAnswers().add(answer);
		answer.setQuestion(this);
	}
	
	public void removeAnswer(Answer answer) {
		this.getAnswers().remove(answer);
	}
	
	@Transient
	public String getSection() {
	    return this.getClass().getAnnotation(DiscriminatorValue.class).value();
	}
}
