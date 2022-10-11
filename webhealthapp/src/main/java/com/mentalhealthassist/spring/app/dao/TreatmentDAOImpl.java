package com.mentalhealthassist.spring.app.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mentalhealthassist.spring.app.entity.Symptom;
import com.mentalhealthassist.spring.app.entity.Treatment;

@Repository
public class TreatmentDAOImpl implements TreatmentDAO {

	@Autowired
	SessionFactory sessionFactory;
	
	//logger for tracking
	private Logger myLogger = Logger.getLogger(getClass().getName());
	
	
	@Override
	public List<Treatment> findAll() {
		
		//get current session
		Session session = sessionFactory.getCurrentSession();
		
		//tracker
		myLogger.info(">>>>>>> we made it to TreatmentDAO");
		
		//create query
		Query<Treatment> query = session.createQuery(
				"FROM Treatment ORDER BY name"
				);
		
		//use query to get list
		List<Treatment> treatments = query.getResultList();
		
		//return list
		return treatments;
	}

	@Override
	public Treatment get(int id) {
		
		//get current session
		Session session = sessionFactory.getCurrentSession();
		
		//query the database
		//use left join fetch for lazy loading
		TypedQuery<Treatment> query = session.createQuery(
				"SELECT t FROM Treatment t "
				+"LEFT JOIN FETCH t.conditions "
				+ "WHERE t.id=:treatId", Treatment.class	
				);
		query.setParameter("treatId", id);

		//return result
		return query.getSingleResult();
	}

	@Override
	public boolean save(Treatment treatment) {
		
		//get current session
		Session session = sessionFactory.getCurrentSession();
		
		//test if new or update
		if(treatment.getId() != 0) {
			
			//save or update
			session.saveOrUpdate(treatment);
			return true;
			
		}
		
		try {
			//test name for existence unique constraint
			find(treatment.getName());
			return false;
		}catch(javax.persistence.NoResultException exc) {
			
			//save or update
			session.saveOrUpdate(treatment);
			
			//return successful save
			return true;
		}	
	}

	@Override
	public void delete(int id) {
		
		//get current session
		Session session = sessionFactory.getCurrentSession();
		
		//create query
		Query query = session.createQuery("DELETE FROM Treatment WHERE id =: treatId");
		query.setParameter("treatId", id);
		
		//execute query
		query.executeUpdate();
	}
	
	
	@Override
	public List<Treatment> searchTreatment(String theTerm) {
		// get the current session
		Session currSess = sessionFactory.getCurrentSession();

		// create a query
		// create a query ... sort by name
		Query theQuery = currSess.createQuery("FROM Treatment "
								+ "WHERE name LIKE :searchTerm "
										+ "ORDER BY name");
		theQuery.setParameter("searchTerm", "%"+theTerm+"%");

		// execute query and get result list
		List<Treatment> treatments = theQuery.getResultList();

		// return results
		return treatments;
	}

	@Override
	public Treatment find(String search) {

		//get current session
		Session currSess = sessionFactory.getCurrentSession();
		
		//even out for search
		search = search.toUpperCase();
		
		//query the database
		Query query = currSess.createQuery("FROM Treatment WHERE UPPER(treatment_name)=:treatName");
		
		//set the parameter
		query.setParameter("treatName", search);
		
		//get result
		Treatment treatment = (Treatment) query.getSingleResult();
		
		//return result
		return treatment;
	}
	
	@Override
	public List<Treatment> backupTreatments() {
		
		Session session = sessionFactory.getCurrentSession();
		
		//array to return
		List<Treatment> treatments = new ArrayList<>();
		
		//find all current in the DB
		List<Treatment> temp = findAll();
		
		//use Dao to get around lazy loading
		for(Treatment t: temp) {
			treatments.add(get(t.getId()));
		}
		
		return treatments;
	}


}
