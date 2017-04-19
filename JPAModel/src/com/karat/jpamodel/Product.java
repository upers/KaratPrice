package com.karat.jpamodel;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the product database table.
 * 
 */
@Entity
@Table(name="product")
@NamedQueries(value={
		@NamedQuery(name="Product.findAll", query="SELECT p FROM Product p"),
		@NamedQuery(name="Product.findByCategoryId", query="SELECT p FROM Product p WHERE p.category.id = :id"),
		@NamedQuery(name="Product.findById", query="SELECT p FROM Product p WHERE p.id = :id"),
		@NamedQuery(name="Product.findByIdCode", query="SELECT p FROM Product p WHERE p.code = :code"),
		@NamedQuery(name="Product.findByIdCodes", query="SELECT p FROM Product p WHERE p.code in :codes")
})
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String code;

	private String name;

	private Double price;
	
	/**
	 * Use for map cat name value from excel file.
	 */
	@Transient
	private String categoryName;

	//bi-directional many-to-one association to PricePolicy
	@OneToMany(mappedBy="product")
	private List<PricePolicy> pricePolicies;

	//bi-directional many-to-one association to Category
	@ManyToOne
	private Category category;

	public Product() {
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

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public List<PricePolicy> getPricePolicies() {
		return this.pricePolicies;
	}

	public void setPricePolicies(List<PricePolicy> pricePolicies) {
		this.pricePolicies = pricePolicies;
	}

	public PricePolicy addPricePolicy(PricePolicy pricePolicy) {
		getPricePolicies().add(pricePolicy);
		pricePolicy.setProduct(this);

		return pricePolicy;
	}

	public PricePolicy removePricePolicy(PricePolicy pricePolicy) {
		getPricePolicies().remove(pricePolicy);
		pricePolicy.setProduct(null);

		return pricePolicy;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public String toString() {
		return "name: " + name + "code: " + code + " price: " + price;
	}

}