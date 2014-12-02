package org.symptom.management.repository;


import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.Objects;

/**
 * A simple object to represent a Check-in. Each check-in is 
 * defined by a time, a date and its Patient.
 * There are 2 public enum used to handle answers to the question:
 * "How is your pain ?" and "Can you eat?"
 * The medication list contains each medication the user needs to take
 * and when he took it.
 * 
 * @author stephane
 * 
 */
@Document(collection="CheckIn")
public class CheckIn {

	@Id
	private long id;
	@DBRef
	private Patient patient;
	private String date;
	private PainState painstate;
	private Eating eating;
	private Set<Medication> medicationlist;

	public enum PainState{
		WELL_CONTROLLED,
		MODERATE,
		SEVERE
	}
	
	public enum Eating{
		NO,
		SOME,
		CANT_EAT
	}
	
	public CheckIn() {
	}

	public CheckIn(Patient patient, String date, PainState painState, Eating eating, Set<Medication> medicationList) {
		super();
		this.patient = patient;
		this.date = date;
		this.painstate = painState;
		this.eating = eating;
		this.medicationlist = medicationList;
		this.id = hashCode();
	}
	
	public Patient getPatient(){
		return patient;
	}
	
	public void setPatient(Patient patient){
		this.patient = patient;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	
	public PainState getPainstate() {
		return painstate;
	}

	public void setPainstate(PainState state) {
		this.painstate = state;
	}

	public Eating getEating() {
		return eating;
	}

	public void setEating(Eating eat) {
		this.eating = eat;
	}

	public Set<Medication> getMedicationlist() {
		return medicationlist;
	}

	public void setMedicationlist(Set<Medication> medic) {
		this.medicationlist = medic;
	}

	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	/**
	 * Two CheckIn will generate the same hashcode if they have exactly the same
	 * values for their patient login, date,
	 * 
	 */
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(patient.getLogin(), date);
	}

	/**
	 * Two Videos are considered equal if they have exactly the same values for
	 * their name, url, and duration.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CheckIn) {
			CheckIn other = (CheckIn) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(patient, other.patient)
					&& Objects.equal(date, other.date)
					&& Objects.equal(painstate, other.painstate)
					&& Objects.equal(eating, other.eating)
					&& Objects.equal(medicationlist, other.medicationlist);
		} else {
			return false;
		}
	}

}
