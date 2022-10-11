package com.mentalhealthassist.spring.app.controller;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mentalhealthassist.spring.app.dto.NameAndIdDTO;
import com.mentalhealthassist.spring.app.entity.Condition;
import com.mentalhealthassist.spring.app.entity.Symptom;
import com.mentalhealthassist.spring.app.service.SymptomService;

@Controller
@RequestMapping("/symptom")
public class SymptomController {
	
	//inject SymptomService bean
	@Autowired
	private SymptomService symptomService;
	
	Logger myLogger = Logger.getLogger(getClass().getName());
	
	@GetMapping("/admin/list")
	private String getSymptomList(Model model) {
		
		//get symptom list from database
		List<Symptom> symptoms = symptomService.getAllSymptoms();
		
		//add list to model
		model.addAttribute("symptoms", symptoms);
		
		//return string link to html file
		return "symptom/list-symptoms"; 
	
	}
	
	@GetMapping("/seeAll")
	private String seeAllSymptoms(Model model) {
		
		//get symptom list from database
		List<Symptom> symptoms = symptomService.getAllSymptoms();
		
		//add list to model
		model.addAttribute("symptoms", symptoms);
		
		//return string link to html file
		return "symptom/list-all-symptoms"; 
	
	}
	
	
	@GetMapping("/admin/showFormForAddSymptom")
	public String showFormForAdd(Model theModel) {

		// create model attribute to bind form data 
		Symptom theSymptom = new Symptom();
		
		//add condition to model for use in condition-form
		theModel.addAttribute("symptom", theSymptom);

		//return html
		return "symptom/symptom-form";
	}
	
	
	//connect post mapping to action in 'symptom-form.jsp'
	@PostMapping("/admin/saveSymptom")
	private String saveSymptom(@ModelAttribute("symptom") Symptom symptom,
								Model model) {
		
		//trim symptom whitespace
		String trimName = symptom.getName().trim();
		symptom.setName(trimName);
		
		
		//call service to save object
		if(symptomService.saveSymptom(symptom)){
			
			//if success return to list
			return "redirect:/symptom/admin/list";
			
		}else {	
			
			//add attributes to model
			model.addAttribute("symptom", symptom);
			model.addAttribute("symptomAlreadyExists", true);
			
			//if fail
			//return symptom-form
			return "symptom/symptom-form";
			
		}	
		
	}
	
	//mapping for return update
	//request symptom id from url
	@GetMapping("/admin/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("symptomId") int id, Model model) {
		
		//get symptom from the database to prepopulate form
		Symptom symptom = symptomService.getSymptom(id);
		
		//tracking
		myLogger.info(">>>>..."+symptom.getName());
		
		//add the value to the model
		model.addAttribute("symptom", symptom);
		
		//return to symptom-form
		return "symptom/symptom-form";
	}
	
	@GetMapping("/admin/delete")
	private String deleteSymptom(@RequestParam("symptomId") int id) {
		
		//if no id, then failed
		if (id == 0)
			throw new NullPointerException("No symptom given");

		//delete from database using SymptomService Object
		symptomService.deleteSymptom(id);
		
		//return to list
		return "redirect:/symptom/admin/list";
		
	}
	
	
	//return search page
	@GetMapping("/search")
	private String searchSymptomPage(Model model) {
		
		// add DTO to model for search
		model.addAttribute("searchDTO", new NameAndIdDTO(0, ""));
		
		//return search-symptom.html
		return "symptom/search-symptom";
	}
	
	
	
	//search for symptom, search term from url
	@GetMapping("/searchSymptom")
	public String searchCondition(@RequestParam("name")String search, Model model) {
		
		//trim whitespace
		search = search.trim();
		
		//track
		myLogger.info(">>>>>>>>> search: "+search);
		
		// search and collect the customer using our service
		List<Symptom> found = symptomService.searchSymptom(search);
		
		//add data to model for result page
		model.addAttribute("symptoms", found);
		NameAndIdDTO stringName = new NameAndIdDTO(0, search);
		model.addAttribute("searchName", stringName);

		return "symptom/list-symptoms-search-results";
	}
	
	//detailed symptom form, get id from URL
	@GetMapping("viewSymptom")
	public String viewSymptom(@RequestParam("symptomId") int id, 
											Model model) {
		
		//get the symptom using id
		Symptom symptom = symptomService.getSymptom(id);
		
		//add symptom to model
		model.addAttribute("symptom", symptom);
		
		//return symptom details form 
		return "symptom/symptom-details";
	}
	


}
