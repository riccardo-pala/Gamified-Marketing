package entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue("2")
@NamedQuery(name="Question.findAll", query="SELECT q FROM QuestionTwo q")
public class QuestionTwo extends Question {

}
