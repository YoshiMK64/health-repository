package com.mentalhealthassist.spring.app.dao;

import java.util.List;

import com.mentalhealthassist.spring.app.entity.Treatment;

public interface TreatmentDAO {
	
	public List<Treatment> findAll();
	
	public Treatment get(int id);
	
	public boolean save(Treatment treatment);
	
	public void delete(int id);

	public List<Treatment> searchTreatment(String theTerm);

	public Treatment find(String name);

	public List<Treatment> backupTreatments();

}
