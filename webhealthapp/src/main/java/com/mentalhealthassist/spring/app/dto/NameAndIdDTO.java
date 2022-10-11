package com.mentalhealthassist.spring.app.dto;

import org.springframework.stereotype.Component;

import lombok.Data;


//lombok data auto generates getters and setters
//Data Transfer Object for use with View and Controllers
@Data
@Component
public class NameAndIdDTO {
	
	private int id;
	
	private String name;

	public NameAndIdDTO(int conditionId, String symptomName) {
		this.id = conditionId;
		this.name = symptomName;
	}

	public NameAndIdDTO() {
	}

}
