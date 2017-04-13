package com.karat.jpamodel;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the category database table.
 * 
 */
@Entity
@Table(name="category")
@NamedQueries(value={
		@NamedQuery(name="Category.findAll", query="SELECT c FROM Category c"),
		@NamedQuery(name="Category.findAllMainCategories", query="SELECT c FROM Category c WHERE c.parentId is null"),
		@NamedQuery(name="Category.findByParentId", query="SELECT c FROM Category c WHERE c.parentId = :id"),
		@NamedQuery(name="Category.findById", query="SELECT c FROM Category c WHERE c.id = :id")
})
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String name;

	@Column(name="parent_id")
	private Integer parentId;

	//bi-directional many-to-one association to PricePolicy
	@OneToMany(mappedBy="category")
	private List<PricePolicy> pricePolicies;

	//bi-directional many-to-one association to Product
	@OneToMany(mappedBy="category")
	private List<Product> products;

	public Category() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public List<PricePolicy> getPricePolicies() {
		return this.pricePolicies;
	}

	public void setPricePolicies(List<PricePolicy> pricePolicies) {
		this.pricePolicies = pricePolicies;
	}

	public PricePolicy addPricePolicy(PricePolicy pricePolicy) {
		getPricePolicies().add(pricePolicy);
		pricePolicy.setCategory(this);

		return pricePolicy;
	}

	public PricePolicy removePricePolicy(PricePolicy pricePolicy) {
		getPricePolicies().remove(pricePolicy);
		pricePolicy.setCategory(null);

		return pricePolicy;
	}

	public List<Product> getProducts() {
		return this.products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Product addProduct(Product product) {
		getProducts().add(product);
		product.setCategory(this);

		return product;
	}

	public Product removeProduct(Product product) {
		getProducts().remove(product);
		product.setCategory(null);

		return product;
	}

}