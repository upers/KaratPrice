package com.karat.jpamodel;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the price_policy database table.
 * 
 */
@Entity
@Table(name="price_policy")
@NamedQueries(value= {
		@NamedQuery(name="PricePolicy.findAll", query="SELECT p FROM PricePolicy p"),
		@NamedQuery(name="PricePolicy.getPricePolByCategUserId", 
			query="SELECT p FROM PricePolicy p WHERE p.user.id = :user_id AND p.category.id = :category_id"),
		@NamedQuery(name="PricePolicy.getPricePolByProductId", 
			query="SELECT p FROM PricePolicy p WHERE p.product.id = :id"),
		@NamedQuery(name="PricePolicy.getPricePolByCategoryId", 
			query="SELECT p FROM PricePolicy p WHERE p.category.id = :id"),
		@NamedQuery(name="PricePolicy.getPricePolByCategoryAndUserId", 
			query="SELECT p FROM PricePolicy p WHERE p.category.id = :category_id AND p.user.id = :user_id"),
		@NamedQuery(name="PricePolicy.getPricePolByProductAndUserId", 
			query="SELECT p FROM PricePolicy p WHERE p.product.id = :product_id AND p.user.id = :user_id"),
})
public class PricePolicy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="category_murkup_percent")
	private Double categoryMurkupPercent;

	@Column(name="`product_markup\r\nproduct_marckup_percent`")
	private Double productMarkup__productMarckupPercent;

	//bi-directional many-to-one association to Category
	@ManyToOne
	private Category category;

	//bi-directional many-to-one association to Product
	@ManyToOne
	private Product product;

	//bi-directional many-to-one association to User
	@ManyToOne
	private User user;

	public PricePolicy() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getCategoryMurkupPercent() {
		return this.categoryMurkupPercent;
	}

	public void setCategoryMurkupPercent(Double categoryMurkupPercent) {
		this.categoryMurkupPercent = categoryMurkupPercent;
	}

	public Double getProductMarkup__productMarckupPercent() {
		return this.productMarkup__productMarckupPercent;
	}

	public void setProductMarkup__productMarckupPercent(Double productMarkup__productMarckupPercent) {
		this.productMarkup__productMarckupPercent = productMarkup__productMarckupPercent;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}