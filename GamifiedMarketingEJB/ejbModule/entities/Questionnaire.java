package entities;

import java.sql.Date;
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

@Entity
@Table(name = "questionnaires", schema = "gmdb")
@NamedQuery(name="Questionnaire.findByDate",query="SELECT q FROM Questionnaire q WHERE q.date=?1")
public class Questionnaire {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private Date date;
	
	@OneToMany(mappedBy="questionnaire") //TODO: "questionare???"
	private List<Accesses> accesses;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="productid")
	private Product product;
	
	@OneToMany(mappedBy="questionnaire",fetch=FetchType.EAGER,cascade= {CascadeType.PERSIST,CascadeType.REMOVE})
	private List<Question> questions;

	
	public Questionnaire() {
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


	public List<Accesses> getAccesses() {
		return accesses;
	}


	public void setAccesses(List<Accesses> accesses) {
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

	
	
	
}
