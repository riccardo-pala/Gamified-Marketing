package entities;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;


@Entity
@Table(name = "products", schema = "gmdb")
public class Product {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private int id;
	
	private String name;
	
	@Lob
	private byte[] photo;

	
	public Product() {
	}

	public Product(String name, byte[] photo) {
		this.name = name;
		this.photo = photo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
}
