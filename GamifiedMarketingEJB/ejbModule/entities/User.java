	package entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name = "users", schema = "gmdb")
@NamedQueries({
	@NamedQuery(name = "User.checkCredentials", query = "SELECT r FROM User r  WHERE r.username = ?1 and r.password = ?2"),
	@NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = ?1"),
	@NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = ?1")
})

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String firstname;
	
	private String lastname;
	
	private String username;
	
	private String email;
	
	private String password;
	
	@Column(name = "banned")
	private Boolean isBanned;
	
	@Column(name = "admin")
	private Boolean isAdmin;
	
	private int totalPoints;
	                                    

	public User() {
	}

	public User(String firstname, String lastname, String username, String email,
			String password, Boolean isBanned, Boolean isAdmin, int totalPoints) {
		
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.email = email;
		this.password = password;
		this.isBanned = isBanned;
		this.isAdmin = isAdmin;
		this.totalPoints = totalPoints;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getIsBanned() {
		return isBanned;
	}

	public void setIsBanned(Boolean isBanned) {
		this.isBanned = isBanned;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

	
}