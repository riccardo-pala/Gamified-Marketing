package entities;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;


@Entity
@Table(name = "products", schema = "gmdb")
public class Product {
	
	@Id
	@Column(name="idproduct")
	private int id;
	
	private String name;
	
	@Lob
	private byte[] photo;
	
}
