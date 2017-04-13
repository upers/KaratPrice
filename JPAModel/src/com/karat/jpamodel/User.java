package com.karat.jpamodel;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="user")
@NamedQueries(value={
		@NamedQuery(name="User.findAll", query="SELECT u FROM User u"),
		@NamedQuery(name="User.findByLogin",
			query="SELECT u FROM User u WHERE u.login = :p"),
		@NamedQuery(name="User.findByRoleId",
			query="SELECT u FROM User u WHERE u.role.id = :p")	
})
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String login;

	private String name;

	private String password;

	private String surname;
	
	private String company;
	
	private String email;
	
	private String phone;

	//bi-directional many-to-one association to PricePolicy
	@OneToMany(mappedBy="user")
	private List<PricePolicy> pricePolicies;

	//bi-directional many-to-one association to Role
	@ManyToOne
	private Role role;

	public User() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<PricePolicy> getPricePolicies() {
		return this.pricePolicies;
	}

	public void setPricePolicies(List<PricePolicy> pricePolicies) {
		this.pricePolicies = pricePolicies;
	}

	public PricePolicy addPricePolicy(PricePolicy pricePolicy) {
		getPricePolicies().add(pricePolicy);
		pricePolicy.setUser(this);

		return pricePolicy;
	}

	public PricePolicy removePricePolicy(PricePolicy pricePolicy) {
		getPricePolicies().remove(pricePolicy);
		pricePolicy.setUser(null);

		return pricePolicy;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}