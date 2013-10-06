package com.ando.unitdemo.model;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "ProductCatalog")
public class ProductCatalog {

	private Integer id;
	private String title;
	private String isbn;
	private List<String> authors;
	private Integer price;
	private String dimensions;
	private Integer pageCount;
	private Integer inPublication;
	private String productCategory;
	private String description;
	private String bicycleType;
	private String Brand;
	private String gender;
	private List<String> color;

	@DynamoDBHashKey(attributeName = "Id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@DynamoDBAttribute(attributeName = "Title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@DynamoDBAttribute(attributeName = "ISBN")
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	@DynamoDBAttribute(attributeName = "Authors")
	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	@DynamoDBAttribute(attributeName = "Price")
	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	@DynamoDBAttribute(attributeName = "Dimensions")
	public String getDimensions() {
		return dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	@DynamoDBAttribute(attributeName = "PageCount")
	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	@DynamoDBAttribute(attributeName = "InPublication")
	public Integer getInPublication() {
		return inPublication;
	}

	public void setInPublication(Integer inPublication) {
		this.inPublication = inPublication;
	}

	@DynamoDBAttribute(attributeName = "ProductCategory")
	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	@DynamoDBAttribute(attributeName = "Description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@DynamoDBAttribute(attributeName = "BicycleType")
	public String getBicycleType() {
		return bicycleType;
	}

	public void setBicycleType(String bicycleType) {
		this.bicycleType = bicycleType;
	}

	@DynamoDBAttribute(attributeName = "Brand")
	public String getBrand() {
		return Brand;
	}

	public void setBrand(String brand) {
		Brand = brand;
	}

	@DynamoDBAttribute(attributeName = "Gender")
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@DynamoDBAttribute(attributeName = "Color")
	public List<String> getColor() {
		return color;
	}

	public void setColor(List<String> color) {
		this.color = color;
	}

}
