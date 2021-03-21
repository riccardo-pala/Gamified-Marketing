package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name = "users")
@NamedQuery(name = "User.checkCredentials", query = "SELECT r FROM User r  WHERE r.username = ?1 and r.password = ?2")
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;

	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "banned")
	private Boolean banned;
	
	@Column(name = "admin")
	private Boolean admin;
}