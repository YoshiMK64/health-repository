package com.mentalhealthassist.spring.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mentalhealthassist.spring.app.dao.SymptomDAO;
import com.mentalhealthassist.spring.app.entity.Symptom;

@Service
public class SymptomServiceImpl implements SymptomService {

	//service class transactional annotation
	//initiates and closes hibernate session
	
	@Autowired
	private SymptomDAO symptomDao;
	
	@Override
	@Transactional
	public List<Symptom> getAllSymptoms() {
		
		return symptomDao.getAll();
	}

	@Override
	@Transactional
	public Symptom getSymptom(int id) {
		
		return symptomDao.getSymptom(id);
	}

	@Override
	@Transactional
	public boolean saveSymptom(Symptom theSymptom) {
		return symptomDao.save(theSymptom);
	}

	@Override
	@Transactional
	public void deleteSymptom(int id) {
		
		symptomDao.delete(id);
	}

	@Override
	@Transactional
	public List<Symptom> searchSymptom(String search) {
		
		return symptomDao.searchSymptom(search);
	}

	@Override
	@Transactional
	public Symptom find(String search) {
		return symptomDao.find(search);
	}

	@Override
	@Transactional
	public List<Symptom> backupSymptoms() {
		
		return symptomDao.backupSymptoms();
	}

}
