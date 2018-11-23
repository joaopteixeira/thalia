package thalia.atec.thaliaPrototipo.model;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;

public class Category {
	
	
	
	@Id
	private String id;
	
	private String description;
	private String image;
	private ArrayList<Category> subCategory;
	
	
	
	
	public Category() {
		super();
	}


	public Category(String description,String image) {
		super();
		this.description = description;
		this.image = image;
		this.subCategory = new ArrayList<>();
	}


	public String getId() {
		return id;
	}

	

	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public ArrayList<Category> getSubCategory() {
		return subCategory;
	}


	public void setSubCategory(ArrayList<Category> subCategory) {
		this.subCategory = subCategory;
	}
	
	
	
	

}
