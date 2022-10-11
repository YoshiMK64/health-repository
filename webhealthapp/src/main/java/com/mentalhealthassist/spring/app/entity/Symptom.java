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

@Entity
@Table(name="symptom")
public class Symptom implements Comparable<Symptom>{

	//mapping id as ID and to column id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	//mapping to database
	@Column(name="symptom_name")
	private String name;
	
	//mapping to database
	@Column(name="symptom_description")
	private String description;
	
	//mapping to many to many table but mapped by 'symptoms' 
	//in Condition
	@ManyToMany(mappedBy="symptoms")
	private List<Condition> conditions;
	
	public Symptom(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public Symptom() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Condition> getConditions() {

		if (conditions == null) conditions = new ArrayList<>();

		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	public void addCondition(Condition condition) {
		if (conditions == null)conditions = new ArrayList<>();

		conditions.add(condition);
	}

	public boolean remove(Condition condition) {

		if (conditions == null)
			return false;

		return conditions.remove(condition);

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Symptom [\n Name=" + name + ".\n Description=" + description + ". \n ]" + "\n";
	}

	//Overriding compareTo to test existence in database
	@Override
	public int compareTo(Symptom o) {

		return this.name.toUpperCase().compareTo(o.getName().toUpperCase());
	}

	//Overriding equals to test existence in database 
	@Override
	public boolean equals(Object o) {

		if (o == this) return true;

		if (!(o instanceof Symptom)) {
			return false;
		}

		Symptom symptom = (Symptom) o;

		return symptom.getName().equals(this.name) && (symptom.getId() == (this.id));

	}

}
