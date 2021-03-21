package entities;



import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;


@Entity
public class Product {
	
	@Id
	private int id;
	
	private String name;
	
	@Lob
	private byte[] picture;

}
