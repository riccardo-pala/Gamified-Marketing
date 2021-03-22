package entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "questionnaires", schema = "gmdb")
public class Questionnaire {
	
	@Id
	@Column(name="idquestionnaires")
	private int Id;
	
	@OneToMany(mappedBy="questionare")
	private List<Accesses> accesses;
	
	@ManyToOne
	@JoinColumn(name="productId")
	private Product product;
	
	@OneToMany(mappedBy="questionnaire")
	private List<Question> questions;
	
}
