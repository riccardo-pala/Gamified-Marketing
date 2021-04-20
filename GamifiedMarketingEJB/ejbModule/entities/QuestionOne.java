package entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("1")
@NamedQuery(name="QuestionOne.findSectionOneByQuestionnaire", query="SELECT q FROM QuestionOne q WHERE q.questionnaire.id=?1")
public class QuestionOne extends Question implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String text;
	
	private int section; 
	
	@ManyToOne
	@JoinColumn(name="questionnaireid")
	private Questionnaire questionnaire;
	
	/*non lo avevamo implementato ma è fondamentale per poter eliminare tutte le risposte
	 * senza di questo non è possibile eliminarle perchè si viola il constraints delle foreign keys question_answer
	 */
	@OneToMany(mappedBy="question", cascade= CascadeType.REMOVE)
	private List<Answer> answers;

	
	public QuestionOne() {
	}

	public QuestionOne(String text, int section) {
		this.text = text;
		this.section = section;
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
