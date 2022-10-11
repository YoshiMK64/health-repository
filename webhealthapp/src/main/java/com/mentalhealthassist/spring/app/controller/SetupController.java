package com.mentalhealthassist.spring.app.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mentalhealthassist.spring.app.dto.NameAndIdDTO;
import com.mentalhealthassist.spring.app.entity.Condition;
import com.mentalhealthassist.spring.app.entity.Symptom;
import com.mentalhealthassist.spring.app.entity.Treatment;
import com.mentalhealthassist.spring.app.service.ConditionService;
import com.mentalhealthassist.spring.app.service.FileExporter;
import com.mentalhealthassist.spring.app.service.SymptomService;
import com.mentalhealthassist.spring.app.service.TreatmentService;

@Controller
public class SetupController {
	
	//add services to autowire
	@Autowired
	private ConditionService conditionService;

	@Autowired
	private TreatmentService treatmentService;

	@Autowired
	private SymptomService symptomService;
	
	//file exporte for database data backup
	@Autowired
	private FileExporter fileExporter;
	
	//logger for tracking
	Logger myLogger = Logger.getLogger(getClass().getName());

	//mapping for home 
	@GetMapping("/")
	public String showIndex(Model model) {
		
		//add DTO to model for use in index.html
		model.addAttribute("searchDTO", new NameAndIdDTO(0, ""));

		//return index.html
		return "index";

	}

	//mapping to custom login age
	@GetMapping("/showMyLoginPage")
	public String showLoginPage() {

		//return custom login page
		return "login/fancy-login";

	}

	//mapping to access denied
	@GetMapping("/accessDenied")
	public String accessDeniedPage() {

		//return custom denied page
		return "login/access-denied";

	}

	//mapping to admin home
	@GetMapping("/admin")
	public String showAdmin() {
		
		//map to admin-index
		return "login/admin-index";
	}
	
	
	//mapping for data backup
	@GetMapping("/admin/backup")
	public void backupDB(HttpServletResponse response) {
		
		//define info for filename and content starter
		String fileName = "backup.txt";
		String fileContent = "";

		//get all values from database
		List<Condition> conditions = conditionService.backUpConditions();
		List<Symptom> symptoms = symptomService.backupSymptoms();
		List<Treatment> treatments = treatmentService.backupTreatments();

		//add conditions to backup fileContent
		for (Condition c : conditions) {

			fileContent += c;

		}
		//extra space between condition and symptoms
		fileContent += "\n";

		//add symptoms
		for (Symptom s : symptoms) {
			fileContent += s;
		}

		//extra space between symptoms and treatments
		fileContent += "\n";

		//add treatments
		for (Treatment t : treatments) {
			fileContent += t;
		}

		try {
			
			//create text file
			Path exportedPath = fileExporter.export(fileContent, fileName);
			
			//Download file with HttpServletResponse
			response.setContentType(MediaType.TEXT_PLAIN_VALUE);
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
			response.setContentLength((int) exportedPath.toFile().length());
			
			//copy file content to response output stream
			Files.copy(exportedPath, response.getOutputStream());
			response.getOutputStream().flush();

		} catch (IOException e) {

			//log exception 
			myLogger.error(e.getMessage());

		}

	}

}
