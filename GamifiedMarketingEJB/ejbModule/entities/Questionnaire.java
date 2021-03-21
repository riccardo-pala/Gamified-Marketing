package entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Questionnaire {
	
	@Id
	private int Id;
	
	@OneToMany(mappedBy="questionare")
	private List<Accesses> accesses;
	
	@ManyToOne
	@JoinColumn(name="productId")
	private Product product;
	
}
