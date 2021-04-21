package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="offensivewords")
@NamedQuery(name="BadWord.findAll",query="SELECT w FROM BadWord w")
public class BadWord {

	@Id
	private int id;
	
	private String word;
	
	

	public BadWord() {
	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
	
	
}
