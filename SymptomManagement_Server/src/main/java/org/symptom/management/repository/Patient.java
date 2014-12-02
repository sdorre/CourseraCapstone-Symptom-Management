package org.symptom.management.repository;


import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.Objects;

/**
 * A simple object to represent a Patient.
 * CheckIn are not stored directly here because there is only one patient
 * for each check-in. So, to get all check-in for a patient, the CheckInRepository
 * will be used. You need to query it with a Patient entity.
 * 
 * @author stephane
 * 
 */

@Document(collection="Patient")
public class Patient extends AppUser {

	@Id
	private long id;
	private String birthdate;
	private String medicalnumber;
	private ArrayList<String> medicationlist;
		
	// There is only one Doctor for each patient.
	@DBRef
	private Doctor doctor;

	public Patient() {
	}

	public Patient(String name, String surname, String birthdate, String medicalNumber, String login) {
		super(name, surname, login);
		this.birthdate = birthdate;
		this.medicalnumber = medicalNumber;
		this.medicationlist = null;
		this.id = hashCode();
	}
	
	public String getBirthdate(){
		return birthdate;
	}
	
	public void setBirthdate(String birthdate){
		this.birthdate = birthdate;
	}
	
	public String getMedicalnumber(){
		return medicalnumber;
	}
	
	public void setMedicalnumber(String medicalnumber){
		this.medicalnumber = medicalnumber;
	}

	public ArrayList<String> getMedicationlist(){
		return medicationlist;
	}
	
	public void setMedicationlist(ArrayList<String> list){
		this.medicationlist = list;
	}

	public Doctor getDoctor(){
		return doctor;
	}
	
	public void setDoctor(Doctor doc){
		this.doctor = doc;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name, lastname, birthdate, medicalnumber, login);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Patient) {
			Patient other = (Patient) obj;
			return Objects.equal(name, other.name)
					&& Objects.equal(lastname, other.lastname)
					&& Objects.equal(birthdate, other.birthdate)
					&& Objects.equal(medicalnumber, other.medicalnumber)
					&& Objects.equal(doctor, other.doctor)
					&& Objects.equal(medicationlist, other.medicationlist)
					&& Objects.equal(login, other.login);
		} else {
			return false;
		}
	}

}
