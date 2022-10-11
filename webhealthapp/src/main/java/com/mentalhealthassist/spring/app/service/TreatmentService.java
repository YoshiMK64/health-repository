package com.mentalhealthassist.spring.app.service;

import java.util.List;
import com.mentalhealthassist.spring.app.entity.Condition;
import com.mentalhealthassist.spring.app.entity.Treatment;

public interface TreatmentService {

	
	public List<Treatment> getAllTreatments();
	
	public Treatment getTreatment(int id);

	public boolean saveTreatment(Treatment theTreatment);

	public void deleteTreatment(int id);

	public List<Treatment> searchTreatment(String search);

	public Treatment find(String name);

	public List<Treatment> backupTreatments();


}
