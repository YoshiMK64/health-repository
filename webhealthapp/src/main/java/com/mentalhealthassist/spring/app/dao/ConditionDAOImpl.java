package com.mentalhealthassist.spring.app.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mentalhealthassist.spring.app.entity.Condition;


@Repository
public class ConditionDAOImpl implements ConditionDAO {

	private Logger myLogger = Logger.getLogger(getClass().getName());
	
	//inject the current hibernate session
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Condition> getAllConditions() {
		
		// get the current session
		Session currSess = sessionFactory.getCurrentSession();

		// create a query
		// create a query ... sort by name
		Query<Condition> theQuery = currSess
						.createQuery("FROM Condition ORDER BY conditionName");

		
		// execute query and get result list
		List<Condition> conditions = theQuery.getResultList();
		

		// return results
		return conditions;
	}
	
	
	@Override
	public List<Condition> backUpConditions(){
		
		// get the current session
		Session currSess = sessionFactory.getCurrentSession();
		
		List<Condition> conditions = new ArrayList<>();
		
		// create a query ... sort by name
		Query<Condition> theQuery = currSess.createQuery("FROM Condition ORDER BY conditionName");
		
		
		// execute query and get result list
		List<Condition> testers = theQuery.getResultList();
		
		for(Condition c: testers) {
			
			//add each condition to get lazy loaded subcollections
			conditions.add(getCondition(c.getId()));
			
		}
		
		//return list of conditions
		return conditions;
	}
	
	
	

	@Override
	public Condition getCondition(int id) {
		
		//get current session
		Session currSess = sessionFactory.getCurrentSession();
		
		//retrieve the condition
		//Condition cond = currSess.get(Condition.class, id);
		//need to left join fetch to retrieve collections
		//as we have lazy loading
		TypedQuery<Condition> query = currSess.createQuery(
				"SELECT c FROM Condition c "
				+"LEFT JOIN FETCH c.symptoms "
				+ "WHERE c.id=:condId", Condition.class	
				);
		query.setParameter("condId", id);
		
		//need to call single result twice to load both lists
		query.getSingleResult();
		
		query = currSess.createQuery(
				"SELECT c FROM Condition c "
				+"LEFT JOIN FETCH c.treatments "
				+ "WHERE c.id=:condId", Condition.class	
				);
		query.setParameter("condId", id);
			
		//return the condition
		return query.getSingleResult();
	}

	@Override
	public boolean saveCondition(Condition theCondition) {
		
		//get the current session
		Session currSess = sessionFactory.getCurrentSession();
		
		//test if new or update
		if(theCondition.getId() != 0) {
			
			//save/update the condition
			currSess.saveOrUpdate(theCondition);
			
			//return successful save
			return true;
		}
		
		try {
			//test existence if fails then save condition
			Condition c = find(theCondition.getConditionName());
			
			//tracking
			myLogger.info(">>>>>>>>>>>>>>>>>>>> Made it to the saveOrUpdate call");
			
			//return failed save
			return false;
			
			//catch no result to save 
		}catch(javax.persistence.NoResultException exc) {
			
			//save/update the condition
			currSess.saveOrUpdate(theCondition);
			
			//return successful save
			return true;
		}
	}
	
	
	public Condition find(String search) {

		//get current session
		Session currSess = sessionFactory.getCurrentSession();
		
		//even out for search
		search = search.toUpperCase();
		
		//query the database
		Query query = currSess.createQuery("FROM Condition WHERE UPPER(condition_name)=:condName");
		
		//set the parameter
		query.setParameter("condName", search);
		
		//get result
		Condition condition = (Condition) query.getSingleResult();
		
		//return result
		return condition;
	}

	@Override
	public void deleteCondition(int id) {
		// get current session
		Session currSess = sessionFactory.getCurrentSession();
		
		//delete object with primary key
		Query query = currSess.createQuery(	"DELETE FROM Condition "
										+ 	"WHERE id =: condId");
		
		//set the parameter used in query "=:condName"
		query.setParameter("condId", id);
		
		//execute delete
		query.executeUpdate();
		
	}
	
	

	@Override
	public List<Condition> searchCondition(String theTerm) {
		// get the current session
		Session currSess = sessionFactory.getCurrentSession();

		// create a query
		// create a query ... sort by name
		Query theQuery = currSess.createQuery("FROM Condition "
								+ "WHERE conditionName LIKE :searchTerm "
										+ "ORDER BY conditionName");
		theQuery.setParameter("searchTerm", "%"+theTerm+"%");

		// execute query and get result list
		List<Condition> conditions = theQuery.getResultList();
		
		// return results
		return conditions;
	}

	@Override
	public List getCondSymptoms(int id) {
		
		//get current session
		Session currSess = sessionFactory.getCurrentSession();
		
		//retrieve the condition
		Condition cond = currSess.get(Condition.class, id);
		
		//return symptoms
		return cond.getSymptoms();
	}

}
