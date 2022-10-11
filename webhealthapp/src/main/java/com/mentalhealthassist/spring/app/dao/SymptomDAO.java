package com.mentalhealthassist.spring.app.dao;

import java.util.List;

import com.mentalhealthassist.spring.app.entity.Symptom;

public interface SymptomDAO {

	
	public List<Symptom> getAll();
	
	public Symptom getSymptom(int id);
	
	public boolean save(Symptom symptom);
	
	public void delete(int id);

	public List<Symptom> searchSymptom(String search);

	public Symptom find(String search);

	public List<Symptom> backupSymptoms();
}
