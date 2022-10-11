package com.mentalhealthassist.spring.app.dao;

import java.util.List;

import com.mentalhealthassist.spring.app.entity.Condition;

public interface ConditionDAO {

	public List<Condition> getAllConditions();

	public Condition getCondition(int id);

	public boolean saveCondition(Condition theCondition);

	public void deleteCondition(int id);

	public List<Condition> searchCondition(String theTerm);

	public List getCondSymptoms(int id);

	public List<Condition> backUpConditions();

}
