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
public class Accesses implements Serializable { // cambiare nome in -> Access
	
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

	@Column(name="submitted")
	private Boolean submitted;

	
	public Accesses() {
	}

	public Accesses(User user, Timestamp accessTime, Questionnaire questionnaire, Boolean submitted) {
		this.user = user;
		this.accessTime = accessTime;
		this.questionnaire = questionnaire;
		this.submitted = submitted;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Timestamp accessTime) {
		this.accessTime = accessTime;
	}

	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

	public Boolean getSubmitted() {
		return submitted;
	}

	public void setSubmitted(Boolean submitted) {
		this.submitted = submitted;
	}
	
}

