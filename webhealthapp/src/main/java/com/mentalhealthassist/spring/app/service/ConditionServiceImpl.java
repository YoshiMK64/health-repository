package com.mentalhealthassist.spring.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mentalhealthassist.spring.app.dao.ConditionDAO;
import com.mentalhealthassist.spring.app.entity.Condition;


@Service
public class ConditionServiceImpl implements ConditionService{

	//condition service class transactional annotation
	//initiates and closes hibernate session
	
	@Autowired
	private ConditionDAO conditionDao;
	
	@Override
	@Transactional
	public List<Condition> getAllConditions() {
		
		return conditionDao.getAllConditions();
	}

	@Override
	@Transactional
	public Condition getCondition(int id) {
		return conditionDao.getCondition(id);
	}

	@Override
	@Transactional
	public boolean saveCondition(Condition theCondition) {
		
		return conditionDao.saveCondition(theCondition);
		
	}

	@Override
	@Transactional
	public void deleteCondition(int id) {
		
		conditionDao.deleteCondition(id);
		
	}

	@Override
	@Transactional
	public List<Condition> searchCondition(String theTerm) {
			
		return conditionDao.searchCondition(theTerm);
		
	}

	@Override
	@Transactional
	public List getCondSymptoms(int id) {
		
		return conditionDao.getCondSymptoms(id);
	}
	
	@Override
	@Transactional
	public List backUpConditions() {
		return conditionDao.backUpConditions();
	}
	

}
