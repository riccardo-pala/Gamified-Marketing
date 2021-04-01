package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="questions")
@NamedQueries({
	@NamedQuery(name="Question.findSectionOneByQuestionnaire", query="SELECT q FROM Question q WHERE q.questionnaire.id=?1 AND q.section LIKE '1'"),
	@NamedQuery(name="Question.findSectionTwo", query="SELECT q FROM Question q WHERE q.section LIKE '2'")
})
public class Question {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String text;
	
	private int section; 
	
	@ManyToOne
	@JoinColumn(name="questionnaireid")
	private Questionnaire questionnaire;

	
	public Question() {
	}

	public Question(String text, int section) {
		this.text = text;
		this.section = section;
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

	public void setContent(String text) {
		this.text = text;
	}

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}

	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}
}
