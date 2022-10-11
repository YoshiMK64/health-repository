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
import com.mentalhealthassist.spring.app.entity.Treatment;
import com.mentalhealthassist.spring.app.service.TreatmentService;

@Controller
@RequestMapping("/treatment")
public class TreatmentController {
	
	
	@Autowired
	private TreatmentService treatmentService;
	
	private Logger myLogger = Logger.getLogger(getClass().getName());
	
	
	//mapping to treatment list
	@GetMapping("/admin/list")
	private String getTreatmentList(Model model) {
		
		//retrieve all treatments from the database
		List<Treatment> treatments = treatmentService.getAllTreatments();
		
		//add list of treatments to model for use in 'list-treatments.jsp'
		model.addAttribute("treatments", treatments);

		//return list-treaatments html
		return "treatment/list-treatments";
	}
	
	
	//see all treatments list 
	@GetMapping("/seeAll")
	private String seeAllTreatments(Model model) {
		
		//retrieve all treatments from the database
		List<Treatment> treatments = treatmentService.getAllTreatments();
		
		//add list of treatments to model for use in 'list-treatments.jsp'
		model.addAttribute("treatments", treatments);

		//return list-all-treatments html 
		return "treatment/list-all-treatments";
	}
	
	//mapping for add treatment 
	@GetMapping("/admin/showFormForAddTreatment")
	private String addTreatment(Model model) {
		
		//create new Treatment object
		Treatment treatment = new Treatment();
		
		//add to model for use in 'treatment-form.jsp'
		model.addAttribute("treatment", treatment);
		
		//treatment-form.html
		return "treatment/treatment-form";
	}
	
	//connect post mapping toaction in 'treatment-form.html'
	//get treatment from model
	@PostMapping("/admin/saveTreatment")
	private String saveTreatment(@ModelAttribute("treatment") Treatment treatment,
								Model model) {
		
		//trim whitespace from treatment name
		String trimName = treatment.getName().trim();
		treatment.setName(trimName);
		
		//call service to save object
		if(treatmentService.saveTreatment(treatment)) {
			//return redirect to list
			return "redirect:/treatment/admin/list";
		}else {
			//add data to DTO for error saving
			model.addAttribute("treatment", treatment);
			model.addAttribute("treatmentAlreadyExists", true);
			
			//return to form
			return "treatment/treatment-form";
			
		}


	}
	
	//update treatment form mapping
	@GetMapping("/admin/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("treatmentId") int id, Model model) {
		
		//get treatment from the database to prepopulate form
		Treatment treatment = treatmentService.getTreatment(id);
		
		//add the value to the model
		model.addAttribute("treatment", treatment);
		
		//return html
		return "treatment/treatment-form";
	}
	
	//delete treatment mapping
	@GetMapping("/admin/delete")
	private String deleteTreatment(@RequestParam("treatmentId") int id) {
		
		//if no id submitted than error
		if (id == 0)
			throw new NullPointerException("No treatment given");

		//delete from database using TreatmentService Object
		treatmentService.deleteTreatment(id);
		
		//redirect to list mapping
		return "redirect:/treatment/admin/list";
		
	}
	
	//map to serach form
	@GetMapping("/search")
	private String searchTreatmentPage(Model model) {
		
		//add DTO for search form use
		model.addAttribute("searchDTO", new NameAndIdDTO(0, ""));
		
		//return html
		return "treatment/search-treatment";
	}
	
	//search treatment mapping
	@GetMapping("/searchTreatment")
	public String searchCondition(@RequestParam("name")String search, Model model) {
		
		//trim whitespace from search term endpoints
		search = search.trim();
		
		//tracking
		myLogger.info(">>>>>>>>> search: "+search);
		
		// search for treatment
		List<Treatment> found = treatmentService.searchTreatment(search);
		
		//add data to model for use in results list
		model.addAttribute("treatments", found);
		NameAndIdDTO stringName = new NameAndIdDTO(0, search);
		model.addAttribute("searchName", stringName);

		//return html
		return "treatment/list-treatments-search-results";
	}
	
	//view treatment in detail mapping
	@GetMapping("viewTreatment")
	public String viewSymptom(@RequestParam("treatmentId") int id, 
											Model model) {
		//get treatment using id in URL
		Treatment treatment = treatmentService.getTreatment(id);
		
		//add treatment to model for use in html
		model.addAttribute("treatment", treatment);
		
		//return the details html
		return "treatment/treatment-details";
	}
	

}
