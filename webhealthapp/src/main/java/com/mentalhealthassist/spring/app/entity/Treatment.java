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
@Table(name="treatment")
public class Treatment implements Comparable<Treatment> {
	
	//mapping to primary key and other columns
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="treatment_name")
	private String name;
	
	@Column(name="treatment_description")
	private String description;
	
	//setting up many to many relationship with no cascade delete but cascade persist etc
	@ManyToMany(mappedBy="treatments")
	private List<Condition> conditions;
	
	public Treatment(String name, String description) {
		this.name = name;
		this.description = description;

	}

	public Treatment() {
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

		if (conditions == null) conditions = new ArrayList<>();

		conditions.add(condition);

	}

	public boolean removeCondition(Condition condition) {

		if (conditions == null) return false;

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
		return "Treatment [ \n Name=" + name + ". Description=" + description + ". \n]" + "\n";
	}

	//override compareTo to test existence
	@Override
	public int compareTo(Treatment o) {

		return this.name.toUpperCase().compareTo(o.getName().toUpperCase());
	}

	//override equals to test existence
	@Override
	public boolean equals(Object o) {

		if (o == this)
			return true;

		if (!(o instanceof Treatment)) {
			return false;
		}

		Treatment treatment = (Treatment) o;

		return treatment.getName().equals(this.name) && (treatment.getId() == (this.id));

	}

}
