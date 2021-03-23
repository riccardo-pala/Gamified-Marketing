package entities;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "questionnaires", schema = "gmdb")
public class Questionnaire {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private Date date;
	
	@OneToMany(mappedBy="questionare") //TODO: "questionare???"
	private List<Accesses> accesses;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="productid")
	private Product product;
	
	@OneToMany(mappedBy="questionnaire")
	private List<Question> questions;

	
	public Questionnaire() {
	}

	
	
	//TODO: constructor, getters and setters, add and remove methods
	
	
}
