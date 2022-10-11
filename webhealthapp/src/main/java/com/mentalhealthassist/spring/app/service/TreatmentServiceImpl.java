package com.mentalhealthassist.spring.app.service;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mentalhealthassist.spring.app.dao.TreatmentDAO;
import com.mentalhealthassist.spring.app.entity.Treatment;

@Service
public class TreatmentServiceImpl implements TreatmentService {

	//service class transactional annotation
	//initiates and closes hibernate session
	
	@Autowired
	private TreatmentDAO treatmentDao;
	
	@Override
	@Transactional
	public List<Treatment> getAllTreatments() {
		
		return treatmentDao.findAll();
	}

	@Override
	@Transactional
	public Treatment getTreatment(int id) {
		return treatmentDao.get(id);
	}

	@Override
	@Transactional
	public boolean saveTreatment(Treatment theTreatment) {
		
		return treatmentDao.save(theTreatment);


	}

	@Override
	@Transactional
	public void deleteTreatment(int id) {
		treatmentDao.delete(id);
	}
	
	
	@Override
	@Transactional
	public List<Treatment> searchTreatment(String theTerm) {
			
		return treatmentDao.searchTreatment(theTerm);
		
	}

	@Override
	@Transactional
	public Treatment find(String name) {
		
		return treatmentDao.find(name);
	}

	@Override
	@Transactional
	public List<Treatment> backupTreatments() {
		
		return treatmentDao.backupTreatments();
	}

}
