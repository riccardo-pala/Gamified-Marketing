package entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;




@Entity
@Table(name = "accesses", schema = "gmdb")
@IdClass(LogId.class)
@NamedQueries({
	@NamedQuery(name="Log.findByQuestionnaireSubmitted",query="SELECT a FROM Log a WHERE a.questionnaire.id=?1 AND a.submitted=true ORDER BY a.accessTime DESC"),
	@NamedQuery(name="Log.findByQuestionnaireCancelled",query="SELECT a FROM Log a WHERE a.questionnaire.id=?1 AND a.submitted=false ORDER BY a.accessTime DESC"),
	@NamedQuery(name="Log.findByQuestionnaireAndUser",query="SELECT a FROM Log a WHERE a.questionnaire.id=?1 AND a.user.id=?2 ORDER BY a.accessTime DESC")	
})
public class Log implements Serializable {
	
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

	private Boolean submitted;

	
	public Log() {
	}

	public Log(User user, Timestamp accessTime, Questionnaire questionnaire, Boolean submitted) {
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

