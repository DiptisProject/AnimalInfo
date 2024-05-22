package com.example.demo.controller;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Animal;
import com.example.demo.entity.AnimalDto;
import com.example.demo.repository.AnimalRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/animals")
public class AnimalController {
	
	@Autowired
	private AnimalRepository repo;
	
	@GetMapping({"", "/"})
	public String showAnimalList(Model model)
	{
		List<Animal> animals=repo.findAll();
		model.addAttribute("animals", animals);
		
		return "animals/index";
		
	}
	
	@GetMapping("/create")
	public String showCreatePage(Model model) {
		AnimalDto animalDto=new AnimalDto();
		model.addAttribute("animalDto", animalDto);
		return "animals/CreateAnimal";
	}
	
	@PostMapping("/create")
	public String createAnimal
	(
		@Valid @ModelAttribute AnimalDto animalDto,
		BindingResult result
	) {
		if(animalDto.getImageFile().isEmpty())
		{
			result.addError(new FieldError("animalDto","imageFile","The image file is required"));
			
		}
		if(result.hasErrors())
		{
			return "animals/CreateAnimal";
		}
		
		MultipartFile image=animalDto.getImageFile();
		Date createdAt= new Date();
		String storageFileName=createdAt.getTime()+"_" + image.getOriginalFilename();
		
		try {
			String uploadDir="public/images/";
			Path uploadPath=Paths.get(uploadDir);
			
			if(!Files.exists(uploadPath)){
				Files.createDirectories(uploadPath);
			}
			try (InputStream inputStream=image.getInputStream()){
				Files.copy(inputStream,Paths.get(uploadDir+storageFileName),
						StandardCopyOption.REPLACE_EXISTING);
				
			}
		}catch (Exception e) {
			System.out.println("Exception: "+e.getMessage());
			
		}
		
		Animal animal=new Animal();
		animal.setName(animalDto.getName());
		animal.setCategory(animalDto.getCategory());
		animal.setLifeExpectency(animalDto.getLifeExpectency());
		animal.setDescription(animalDto.getDescription());
		animal.setCreatedAt(createdAt);
		animal.setImage(storageFileName);
		
		repo.save(animal);
		
		
		return "redirect:/animals";
	}
	
	@GetMapping("/edit")
	public String showEditPage(
			Model model,@RequestParam int id
			) {
		try {
			Animal animal=repo.findById(id).get();
			model.addAttribute("animal",animal);
			
			AnimalDto animalDto=new AnimalDto();
			animalDto.setName(animal.getName());
			animalDto.setCategory(animal.getCategory());
			animalDto.setLifeExpectency(animal.getLifeExpectency());
			animalDto.setDescription(animal.getDescription());
			
			model.addAttribute("animalDto",animalDto);
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception: " +e.getMessage());
			return "redirect:/animals";
		}
		return "animals/EditAnimal";
		
	}
	
	@PostMapping("/edit")
	public String updateProduct(
			Model model, @RequestParam int id, @Valid @ModelAttribute AnimalDto animalDto,
			BindingResult result
			)
	{
		
		try {
			Animal animal=repo.findById(id).get();
			model.addAttribute("animal",animal);
			
			if(result.hasErrors())
			{
				return "animals/EditAnimal";
			}
			
			if(!animalDto.getImageFile().isEmpty())
			{
				String uploadDir="public/images/";
				Path oldImagePath=Paths.get(uploadDir+animal.getImage());
				
				try {
					Files.delete(oldImagePath);
				}
				catch (Exception e) {
					// TODO: handle exception
					System.out.println("Exception: " +e.getMessage());
				}
				
				MultipartFile image=animalDto.getImageFile();
				Date createdAt=new Date();
				String storageFileName=createdAt.getTime() + "_"+ image.getOriginalFilename();
				
				try (InputStream inputStream=image.getInputStream()){
				Files.copy(inputStream, Paths.get(uploadDir+storageFileName),
						StandardCopyOption.REPLACE_EXISTING);
			}
			
			animal.setImage(storageFileName);
		}
			animal.setName(animalDto.getName());
			animal.setCategory(animalDto.getCategory());
			animal.setLifeExpectency(animalDto.getLifeExpectency());
			animal.setDescription(animalDto.getDescription());
			
			repo.save(animal);
		}
		
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception: " +e.getMessage());
		}
		
		return "redirect:/animals";
	}
	
	
	@GetMapping("/delete")
	public String deleteAnimal(
			@RequestParam int id
			) {
		try {
			Animal animal=repo.findById(id).get();
			
			Path imagePath=Paths.get("public/images/" + animal.getImage());
			try {
				Files.delete(imagePath);
			}
			catch (Exception e) {
				System .out.println("Exception: "+ e.getMessage());
				// TODO: handle exception
			}
			repo.delete(animal);
		}
		catch (Exception e) {
			System .out.println("Exception: "+ e.getMessage());
			// TODO: handle exception
		}
		return "redirect:/animals";
	}
}
