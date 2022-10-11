package com.mentalhealthassist.spring.app.service;

import java.util.List;

import com.mentalhealthassist.spring.app.entity.Symptom;

public interface SymptomService {
	
	public List<Symptom> getAllSymptoms();
	
	public Symptom getSymptom(int id);

	public boolean saveSymptom(Symptom theSymptom);

	public void deleteSymptom(int id);

	public List<Symptom> searchSymptom(String search);

	public Symptom find(String search);

	public List<Symptom> backupSymptoms();


}
