package com.mentalhealthassist.spring.app.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.mockito.ArgumentMatchers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="health_condition")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Condition {
	
	//setting id connection with database ane colum mapping
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	//column mapping to db
	@Column(name="condition_name")
	private String conditionName;
	
	//column mapping to db
	@Column(name="condition_description")
	private String conditionDescription;
	
	
	//column mapping and mapping many to many mapping for symptoms
	@ManyToMany(fetch=FetchType.LAZY, cascade= {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	@JoinTable(
			name="condition_symptom",
			joinColumns=@JoinColumn(name="condition_id"),
			inverseJoinColumns=@JoinColumn(name="symptom_id")
			)
	private List<Symptom> symptoms;
	
	//column mapping and mapping many to many mapping for treatments
	@ManyToMany(fetch=FetchType.LAZY, cascade= {CascadeType.PERSIST, CascadeType.MERGE,
												CascadeType.DETACH, CascadeType.REFRESH})
	@JoinTable(
			name="condition_treatment",
			joinColumns=@JoinColumn(name="condition_id"),
			inverseJoinColumns=@JoinColumn(name="treatment_id")
			)
	private List<Treatment> treatments;
	
	//getters and setters
	public Condition() {
	}

	public Condition(String name, String description) {
		this.conditionName = name;
		this.conditionDescription = description;
	}

	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String name) {
		this.conditionName = name;
	}

	public String getConditionDescription() {
		return conditionDescription;
	}

	public void setConditionDescription(String description) {
		this.conditionDescription = description;
	}

	public List<Symptom> getSymptoms() {

		if (symptoms == null) {
			symptoms = new ArrayList<>();

		}

		return symptoms;
	}

	public void setSymptoms(List<Symptom> symptoms) {

		this.symptoms = symptoms;

	}

	public boolean addSymptom(Symptom symptom) {

		if (symptoms == null) {
			symptoms = new ArrayList<>();
		}

		//don't allow duplicate entries
		if (!symptoms.contains(symptom)) {
			
			// add symptom to condition array
			symptoms.add(symptom);
			
			//return success
			return true;
		}

		//return fail
		return false;

	}

	public boolean remove(Symptom symptom) {

		//test empty
		if (symptoms == null) return false;

		return symptoms.remove(symptom);

	}

	public List<Treatment> getTreatments() {

		if (treatments == null) {
			treatments = new ArrayList<>();
		}

		return treatments;
	}

	public void setTreatments(List<Treatment> treatments) {
		this.treatments = treatments;
	}

	public boolean addTreatment(Treatment treatment) {

		if (treatments == null) {
			treatments = new ArrayList<>();
		}

		//don't allow duplicates
		if (!treatments.contains(treatment)) {
			
			// add treatment to list
			treatments.add(treatment);
			
			//return successful add to list
			return true;
		}

		//return failed add
		return false;

	}

	public boolean remove(Treatment treatment) {

		if (treatments == null) return false;

		return treatments.remove(treatment);

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {

		//setting up toString for data backup
		String out = "Condition [" + "\n" + "Name=" + conditionName + ".\n" + "Description=" + conditionDescription
				+ ".\n";
		
		//make sure symptoms not empty before including
		if (symptoms != null) {

			out += "Symptoms: [ ";

			for (Symptom s : symptoms) {
				out += s.getName() + ",";
			}

			out += "]" + "\n";
		}

		//make sure treatments not empty before including
		if (treatments != null) {
			out += "Treatments: [";

			for (Treatment t : treatments) {
				out += t.getName() + ",";
			}
			out += "]\n";
		}

		out += "]\n";

		//return compiled string
		return out;

	}

}
