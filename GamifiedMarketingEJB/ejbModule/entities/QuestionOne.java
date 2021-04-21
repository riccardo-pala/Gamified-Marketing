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
@NamedQuery(name="QuestionOne.findByQuestionnaire", query="SELECT q FROM QuestionOne q WHERE q.questionnaire.id=?1")
public class QuestionOne extends Question implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/*non lo avevamo implementato ma è fondamentale per poter eliminare tutte le risposte
	 * senza di questo non è possibile eliminarle perchè si viola il constraints delle foreign keys question_answer
	 *
	@OneToMany(mappedBy="question", cascade= CascadeType.REMOVE)
	private List<Answer> answers; 
	*/ //non basta quello nella classe padre?
	
	/**
	 * CONSTRUCTORS
	 */
	
	public QuestionOne() {
		super();
	}
	
	public QuestionOne(String text) {
		super(text);
	}

	
}
