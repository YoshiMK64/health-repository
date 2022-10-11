package com.mentalhealthassist.spring.app.service;

import java.util.List;

import com.mentalhealthassist.spring.app.entity.Condition;
import com.mentalhealthassist.spring.app.entity.Symptom;

public interface ConditionService<T> {
	
	public List<Condition> getAllConditions();
	
	public Condition getCondition(int id);

	public boolean saveCondition(Condition theCondition);

	public void deleteCondition(int id);

	public List<Condition> searchCondition(String theTerm);

	public List<Symptom> getCondSymptoms(int id);

	public List backUpConditions();

}
