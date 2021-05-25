package entities;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue("2")
@NamedQuery(name="QuestionTwo.findAll", query="SELECT q FROM QuestionTwo q")
public class QuestionTwo extends Question implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	public QuestionTwo() {
		super();
	}
	
	public QuestionTwo(String text) {
		super(text);
	}

}
