
package com.mentalhealthassist.spring.app.controller;

import java.util.List;
import java.util.Map;

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
import com.mentalhealthassist.spring.app.service.ConditionService;
import com.mentalhealthassist.spring.app.service.SymptomService;
import com.mentalhealthassist.spring.app.service.TreatmentService;

@Controller
@RequestMapping("/condition")
public class ConditionController {

	// need to inject our services
	@Autowired
	private ConditionService conditionService;
	
	@Autowired
	private SymptomService symptomService;
	
	@Autowired
	private TreatmentService treatmentService;
	
	//logger for tracking
	private Logger myLogger = Logger.getLogger(getClass().getName());

	//Admin mapping to see all conditions
	@GetMapping("/admin/list")
	public String listConditions(Model theModel) {

		// get customers from the service 
		List<Condition> theConditions = conditionService.getAllConditions();
		
		// add the customers to the model 
		theModel.addAttribute("conditions", theConditions);

		//return html page 
		return "condition/list-conditions";
	}
	
	//all access list-all-conditions
	@GetMapping("/seeAll")
	public String seeAllConditions(Model theModel) {

		// get customers from the service 
		List<Condition> theConditions = conditionService.getAllConditions();
		
		// add the customers to the model 
		theModel.addAttribute("conditions", theConditions);

		//return html page 
		return "condition/list-all-conditions";
	}
	
	
	//admin add/update a condition to/in database: retrieve condition attribute from model
	//ModelAttribute in argument list mapped from list-conditions.html
	@GetMapping("/admin/showFormForAddCondition")
	public String showFormForAdd(Model theModel, 
			@ModelAttribute("condition") Condition condition){
		
		//tracking
		myLogger.info(">>>>>>>>>>>>> at form for add condition.");

		//test if adding a new condition/else updating condition
		if(condition.getConditionName() == null) {

			//add new condition to model for use in condition-form
			theModel.addAttribute("condition", new Condition());
		}else {
			
			//if updating add condition to update to model
			Condition theCondition = condition;
			theModel.addAttribute("condition", theCondition);
		}
			
		//add DataTransferObject for symptom/treatment to model
		//for use in condition-form.html
		NameAndIdDTO symptomDTO = new NameAndIdDTO();
		NameAndIdDTO treatmentDTO = new NameAndIdDTO();
		theModel.addAttribute("symptomDTO", symptomDTO);
		theModel.addAttribute("treatmentDTO", treatmentDTO);
		
		//tracking 
		myLogger.info(">>>>>>>> just before condition form");
		
		//return html page 
		return "condition/condition-form";
	}
	
	//admin manage symptoms that are associated with condition
	//retrieve id from URL using RequestParam
	//Model in argument list autowired by spring MVC
	@GetMapping("/admin/manageSymptoms")
	public String showFormForSymptoms(Model theModel, 
			@RequestParam("conditionId")int id) {
		
		//get condition object to manage
		Condition cond = conditionService.getCondition(id);
		
		//add model attributes for use in manage-symptoms.html 
		theModel.addAttribute("condition", cond);
		theModel.addAttribute("noSymptomError", false);
		theModel.addAttribute("symptomDTO", new NameAndIdDTO(cond.getId(), ""));
		
		//tracking
		myLogger.info(">> manage symptoms of: >>"+cond.getConditionName());
		
		//return html page 
		return "condition/manage-symptoms";
	}
	
	
	@GetMapping("/admin/manageTreatments")
	public String showFormForTreatments(Model theModel, 
			@RequestParam("conditionId")int id) {
		//get condition object to manage
		Condition cond = conditionService.getCondition(id);
		
		//add model attributes for use in manage-treatments.html 
		theModel.addAttribute("condition", cond);
		theModel.addAttribute("treatmentDTO", new NameAndIdDTO(cond.getId(), ""));
		theModel.addAttribute("noTreatmentError", false);
		
		//tracking
		myLogger.info(">> manage treatments >>"+cond);
		
		//return html
		return "condition/manage-treatments";
	}
	
	
	//mapping for delete symptom, RequestParam is Map<,> as multiple params passed
	//manage-symptoms.html, call autowired model object
	@GetMapping("/admin/deleteSymptom")
	public String deleteSymptom(@RequestParam Map<String,String> requestParams,
								Model model) {
		
		//tracking
		myLogger.info(">>>>> Made it to /deleteSymptom");
		
		//retrieve condition from model
		Condition condition = conditionService.getCondition(Integer.parseInt(requestParams.get("conditionId")));
		
		//find symptom using symptom service
		Symptom symptom = symptomService.getSymptom(Integer.parseInt(requestParams.get("symptomId")));
		
		//remove symptom from condition object
		condition.remove(symptom);
		
		//save condition call to update its position in DB
		conditionService.saveCondition(condition);
		
		//add to model for use in condition-form
		model.addAttribute("condition", condition);
		model.addAttribute("symptomDTO", new NameAndIdDTO(condition.getId(), ""));
		model.addAttribute("symptomName", new String());
		
		//return html
		return "condition/manage-symptoms";
	}
	
	
	
	//as above but for treatment
	@GetMapping("/admin/deleteTreatment")
	public String deleteTreatment(@RequestParam Map<String,String> requestParams,
								Model model) {
		
		myLogger.info(">>>>> Made it to /deleteTreatment");
		
		//retrieve condition from model
		Condition condition = conditionService.getCondition(Integer.parseInt(requestParams.get("conditionId")));
		
		//find symptom using symptom service
		Treatment treatment = treatmentService.getTreatment(Integer.parseInt(requestParams.get("treatmentId")));
		
		//remove treatment
		condition.remove(treatment);
		
		//update DB
		conditionService.saveCondition(condition);
		
		//add condition to model for use in condition-form
		model.addAttribute("condition", condition);
		model.addAttribute("treatmentDTO", new NameAndIdDTO(condition.getId(), ""));
		model.addAttribute("treatmentName", new String());
		
		//return html
		return "condition/manage-treatments";
	}
	
	
	//associate a symptom with condition control
	//get symptom dto to search for symptom in database
	@PostMapping("/admin/addSymptom")
	public String addSymptom(@ModelAttribute("symptomDTO") NameAndIdDTO symptomDTO,
			  Model model) {
		
		//get condition using ID submitted in symptom DTO
		Condition condition = 
				conditionService.getCondition(symptomDTO.getId());
		
		//try/catch database null returns/or similar exceptions 
		try {
			//find symptom using symptom service
			Symptom symptom = symptomService.find(symptomDTO.getName());

			
			//if adding to condition object list successful 
			if(condition.addSymptom(symptom)) {
				//symptom was added successfully
				model.addAttribute("symptomAlreadyAdded", false);
				//attempt to update database
				conditionService.saveCondition(condition);
			}else {
				//symptom already in list
				model.addAttribute("symptomAlreadyAdded", true);
			}
			
			
			
		
			//add condition to model for use in condition-form
			model.addAttribute("symptomDTO", new NameAndIdDTO(condition.getId(), ""));
			model.addAttribute("condition", condition);
			
			//show no error adding symptom for manage-symptoms.html
			model.addAttribute("noSymptomError", false);
		
		
		}catch(Exception e) {
			//log exception message
			myLogger.error(e.getMessage());
			
			//add condition to model for use in condition-form
			model.addAttribute("symptomDTO", new NameAndIdDTO(condition.getId(), ""));
			model.addAttribute("condition", condition);
			//show error adding symptom
			model.addAttribute("noSymptomError", true);
			
		}
		
		
		//tracking
		myLogger.info(">>> Through add symptom");
		
		//return html
		return "condition/manage-symptoms";
	}
	
	//as above but for treatment
	@PostMapping("/admin/addTreatment")
	public String addTreatment(@ModelAttribute("treatmentDTO") NameAndIdDTO treatmentDTO,
			  Model model) {
		//get condition using condition ID transferred in DTO
		Condition condition = 
				conditionService.getCondition(treatmentDTO.getId());
		
		try {
			// find symptom using symptom service
			Treatment treatment = treatmentService.find(treatmentDTO.getName());

			//if successfully added to condition object try update db
			if(condition.addTreatment(treatment)) {
				
				model.addAttribute("treatmentAlreadyAdded", false);
				conditionService.saveCondition(condition);
				
			}else {
				//treatment already associated with condition
				model.addAttribute("treatmentAlreadyAdded", true);
			}


			// add to model for use in condition-form
			model.addAttribute("treatmentDTO", new NameAndIdDTO(condition.getId(), ""));
			model.addAttribute("condition", condition);
			//no db exception 
			model.addAttribute("noTreatmentError", false);
		} catch (Exception e) {
			
			myLogger.error(e.getMessage());
			
			// add condition to model for use in condition-form
			model.addAttribute("treatmentDTO", new NameAndIdDTO(condition.getId(), ""));
			model.addAttribute("condition", condition);
			
			//caught db exception 
			model.addAttribute("noTreatmentError", true);
			
		}
		
		//return html
		return "condition/manage-treatments";
	}
	
	
	
	//save condition mapping
	//retrieve condition to be saved from Model
	@PostMapping("/admin/saveCondition")
	public String saveCondition(Model model, @ModelAttribute("condition") Condition condition) {
		//tracking
		myLogger.info(">>>>>>>> just before save condition:" + condition.getConditionName());
		
		//trim whitespace from endpoints of condition name for search
		String trimName = condition.getConditionName().trim();
		
		//set name to trimmed version
		condition.setConditionName(trimName);
		
		//attemptt to save condition in database through Service 
		boolean saved = conditionService.saveCondition(condition);
		//tracking success
		myLogger.info("Successful save? "+saved);
		
		// if success
		if(saved) {
			//add DTOs and condition to model for use in association management
			model.addAttribute("symptomDTO", new NameAndIdDTO(condition.getId(), ""));
			model.addAttribute("treatmentDTO", new NameAndIdDTO(condition.getId(), ""));
			model.addAttribute("sympTreatDTO", new NameAndIdDTO(condition.getId(), ""));
			model.addAttribute("condition", condition);
			model.addAttribute("conditionAlreadyExists", false);
			
			//return same page
			return "condition/condition-form";
			
		}else {
			//if failed then condition already exists
			model.addAttribute("condition", condition);
			model.addAttribute("conditionAlreadyExists", true);
			
			//return to same page
			return "condition/condition-form";
		}
		

	}
	
	//update condition form and request condition id for update
	@GetMapping("/admin/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("conditionId") int id, Model theModel) {

		// get the customer from our service 
		Condition theCondition = conditionService.getCondition(id);
		
		myLogger.info(theCondition);

		// set customer as a model attribute to pre-populate the form
		theModel.addAttribute("condition", theCondition);
		
		//add dto for symptom to model
		NameAndIdDTO DTO = new NameAndIdDTO(id,"");
		theModel.addAttribute("sympTreatDTO", DTO);
		
		// send over to our form html
		return "condition/condition-form";
	}
	
	//delete condition from database, request id for which to delete
	@GetMapping("/admin/delete")
	public String deleteCondition(@RequestParam("conditionId") int id) {

		//if no id passed throw exception
		if (id == 0)
			throw new NullPointerException("No condition given");

		//delete condition using id
		conditionService.deleteCondition(id);
		
		//redirect to controller mapping 
		return "redirect:/condition/admin/list";
	}
	
	//map to search conditions page
	@GetMapping("/search")
	private String searchConditionPage(Model model) {
		
		//add DTO for use in search-condition.html
		model.addAttribute("searchDTO", new NameAndIdDTO(0, ""));
		
		//return html
		return "condition/search-condition";
	}
	

	//map to search results, RequestParam from URL is search keyword
	@GetMapping("/searchCondition")
	public String searchCondition(@RequestParam("name")String search, Model model) {
		
		//trim whitespace from search request
		search = search.trim();
		
		// save the customer using our service
		List<Condition> found = conditionService.searchCondition(search);
		
		//add found list to model
		model.addAttribute("conditions", found);
		
		//add search name in DTO to model to present results
		NameAndIdDTO stringName = new NameAndIdDTO(0, search);
		model.addAttribute("searchName", stringName);
		
		//return results html
		return "condition/list-conditions-search-results";
	}
	
	//view condition in detail
	//RequestParam get condition ID from url
	@GetMapping("/viewCondition")
	public String viewCondition(@RequestParam("conditionId") int id, Model model) {

		//get condition from database
		Condition condition = conditionService.getCondition(id);
		
		//add condition to model
		model.addAttribute("condition", condition);
		
		//return details html
		return "condition/condition-details";	
	}
	
}

