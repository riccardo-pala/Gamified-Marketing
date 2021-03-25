package entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "questionnaires", schema = "gmdb")
@NamedQuery(name="Questionnaire.findByDate",query="SELECT q FROM Questionnaire q WHERE q.date=?1")
public class Questionnaire {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Temporal(value=TemporalType.DATE)
	private Date date;
	
	@OneToMany(mappedBy="questionnaire", cascade={CascadeType.REMOVE})
	private List<Log> accesses;
	
	@ManyToOne
	@JoinColumn(name="productid")
	private Product product;
	
	@OneToMany(mappedBy="questionnaire", fetch=FetchType.EAGER, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<Question> questions; //eager?
	
	@OneToMany(mappedBy="questionnaire", cascade={CascadeType.REMOVE})
	private List<Answer> answers; 
	// l'admin deve avere accesso a tutte le risposte per un determinato questionario 
	// quando elimina il questionario tutti i dati relativi ad esso vengono eliminati answers e accesses

	
	public Questionnaire() {
	}
	
	public Questionnaire(Date date, Product product) {
		this.date = date;
		this.product = product;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Log> getAccesses() {
		return accesses;
	}

	public void setAccesses(List<Log> accesses) {
		this.accesses = accesses;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}	
	
	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public void addQuestion(Question question) {
		getQuestions().add(question);
		question.setQuestionnaire(this);
		// aligns both sides of the relationship
		// if question is new, invoking persist() on reporter cascades also to Question
	}

	public void removeQuestion(Question question) {
		getQuestions().remove(question);
	}
}
