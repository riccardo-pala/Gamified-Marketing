package entities;



import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name = "products", schema = "gmdb")
@NamedQuery(name="Product.findByName",query="SELECT p FROM Product p WHERE p.name=?1")
public class Product {
	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	@Basic(fetch=FetchType.LAZY)
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
