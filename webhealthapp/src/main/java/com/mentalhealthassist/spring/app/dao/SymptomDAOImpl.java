package com.mentalhealthassist.spring.app.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mentalhealthassist.spring.app.entity.Symptom;

@Repository
public class SymptomDAOImpl implements SymptomDAO {
	
	//inject the current hibernate session
	@Autowired
	private SessionFactory sessionFactory;
	
	//logger for tracking
	private Logger myLogger = Logger.getLogger(getClass().getClass());

	
	@Override
	public List<Symptom> getAll() {
		
		//get the current session
		Session currSess =  sessionFactory.getCurrentSession();
		
		//tracking
		myLogger.info(">>>>>>> we made it to DAO");
		
		
		//create database query
		Query<Symptom> query = currSess.createQuery(
						"FROM Symptom ORDER BY name"
						);
		//execute query
		List<Symptom> symptoms = query.getResultList();
		
		//return result list
		return symptoms;
	}

	@Override
	public Symptom getSymptom(int id) {
		
		//get current session
		Session currSess =  sessionFactory.getCurrentSession();
		
		//query the database
		//use left join fetch for lazy loading
		TypedQuery<Symptom> query = currSess.createQuery(
				"SELECT s FROM Symptom s "
				+"LEFT JOIN FETCH s.conditions "
				+ "WHERE s.id=:sympId", Symptom.class	
				);
		query.setParameter("sympId", id);

		//return result
		return query.getSingleResult();
	}
	
	@Override
	public Symptom find(String search) {
		
		//get current session
		Session currSess = sessionFactory.getCurrentSession();
		
		//even out for search
		search = search.toUpperCase();
		
		//query the database
		Query query = currSess.createQuery("FROM Symptom WHERE UPPER(symptom_name)=:sympName");
		
		//set the parameter
		query.setParameter("sympName", search);
		
		//get the result
		Symptom symptom = (Symptom) query.getSingleResult();
		
		//return result
		return symptom;
	}
	
	

	@Override
	public boolean save(Symptom symptom) {
		
		//get current session
		Session currSess = sessionFactory.getCurrentSession();
		
		//test if plain save or update
		if(symptom.getId() != 0) {
			
			//save/update to database
			currSess.saveOrUpdate(symptom);
			return true;
			
		}
		
		try {
			
			//test if in database
			find(symptom.getName());
			
			//if successful find then save fail
			return false;
			
			//catch hibernate exception
		}catch(javax.persistence.NoResultException exc) {
			
			//save/update to database
			currSess.saveOrUpdate(symptom);
			
			//return successful save
			return true;			
		}
	}
	

	@Override
	public void delete(int id) {
		
		//get current session
		Session currSess = sessionFactory.getCurrentSession();
		
		//create query on primary key
		Query query = currSess.createQuery("DELETE FROM Symptom WHERE id=: sympId");
		query.setParameter("sympId", id);
		
		//execute query
		query.executeUpdate();
		
	}
	
	@Override
	public List<Symptom> searchSymptom(String theTerm) {
		// get the current session
		Session currSess = sessionFactory.getCurrentSession();

		// create a query
		// create a query ... sort by name
		Query theQuery = currSess.createQuery("FROM Symptom "
								+ "WHERE name LIKE :searchTerm "
										+ "ORDER BY name");
		theQuery.setParameter("searchTerm", "%"+theTerm+"%");

		
		// execute query and get result list
		List<Symptom> symptoms = theQuery.getResultList();
		

		// return results
		return symptoms;
	}

	@Override
	public List<Symptom> backupSymptoms() {
		
		//get current session
		Session session = sessionFactory.getCurrentSession();
		
		//array to return
		List<Symptom> symptoms = new ArrayList<>();
		
		//find all current symptoms in the DB
		List<Symptom> temp = getAll();
		
		//use symptomDao to get around lazy loading
		for(Symptom s: temp) {
			symptoms.add(getSymptom(s.getId()));
		}
		
		//return all symptoms
		return symptoms;
	}

}
