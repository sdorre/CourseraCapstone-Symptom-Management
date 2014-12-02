package org.symptom.management.repository;


import org.springframework.data.mongodb.core.mapping.Document;
import com.google.common.base.Objects;

/**
 * A simple object to represent a Medication taken by a patient.
 * 
 * @author stephane
 * 
 */
@Document
public class Medication {

	private String name;
	private boolean taken;
	private String date;

	public Medication() {
	}
	
	public Medication(String name) {
		super();
		this.name = name;
		this.taken = false;
		this.date = null;
	}

	public Medication(String name, boolean istaken, String date) {
		super();
		this.name = name;
		this.taken = istaken;
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getTaken() {
		return taken;
	}

	public void setTaken(boolean taken) {
		this.taken = taken;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Medication) {
			Medication other = (Medication) obj;
			return Objects.equal(name, other.name)
					&& Objects.equal(date, other.date)
					&& taken == other.taken;
		} else {
			return false;
		}
	}

}
