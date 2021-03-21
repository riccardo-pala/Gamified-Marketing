package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.eclipse.persistence.jpa.jpql.parser.DateTime;

@Entity
@IdClass(AccessId.class)
public class Accesses implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	
	@Id
	private DateTime accessTime;
	
	@Id
	@ManyToOne
	@JoinColumn(name="questionaireId")
	private Questionnaire questionare;

	@Column(name="Submitted")
	private Boolean submitted;
	
	
	

}

