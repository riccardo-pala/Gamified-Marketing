package entities;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "offensivewords", schema = "gmdb")
public class OffensiveWord {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private int id;
	
	private String word;
	
	
	public OffensiveWord() {
	}

	public OffensiveWord(String word) {
		this.word = word;
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
