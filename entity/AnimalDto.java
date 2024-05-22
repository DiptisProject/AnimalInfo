package com.example.demo.entity;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class AnimalDto {

	@NotEmpty(message="The name is required")
	private String name;
	
	@NotEmpty(message="The Category is required")
	private String category;
	
	
	
	private String LifeExpectency;
	
	@Size(min=10,message="The description should be at least 10 characters ")
	@Size(max=4000,message="The description cannot exceed 2000 characters")
	private String description;
	
	private MultipartFile imageFile;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLifeExpectency() {
		return LifeExpectency;
	}

	public void setLifeExpectency(String lifeExpectency) {
		LifeExpectency = lifeExpectency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}
}
