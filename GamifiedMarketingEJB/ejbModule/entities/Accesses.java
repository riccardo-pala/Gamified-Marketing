package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eclipse.persistence.jpa.jpql.parser.DateTime;




@Entity
@Table(name = "accesses", schema = "gmdb")
@IdClass(AccessId.class)
public class Accesses implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@ManyToOne
	@JoinColumn(name="userid")
	private User user;
	
	@Id
	private DateTime accessTime;
	
	@Id
	@ManyToOne
	@JoinColumn(name="questionnaireid")
	private Questionnaire questionare;

	@Column(name="Submitted")
	private Boolean submitted;
	
	
	

}

