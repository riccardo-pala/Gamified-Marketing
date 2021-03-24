package entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.jpa.jpql.parser.DateTime;




@Entity
@Table(name = "accesses", schema = "gmdb")
@IdClass(AccessId.class)
@NamedQueries({
	@NamedQuery(name="Accesses.findByQuestionnareSubmitted",query="SELECT a FROM Accesses a WHERE a.questionnaire.id=?1 AND a.submitted=true"),
	@NamedQuery(name="Accesses.findByQuestionnareCancelled",query="SELECT a FROM Accesses a WHERE a.questionnaire.id=?1 AND a.submitted=false")	
})
public class Accesses implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@ManyToOne
	@JoinColumn(name="userid")
	private User user;
	
	@Id
	private Timestamp accessTime;
	
	@Id
	@ManyToOne
	@JoinColumn(name="questionnaireid")
	private Questionnaire questionnaire;

	@Column(name="Submitted")
	private Boolean submitted;
	
}

