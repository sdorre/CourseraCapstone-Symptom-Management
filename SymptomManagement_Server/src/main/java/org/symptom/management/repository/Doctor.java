package org.symptom.management.repository;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.Objects;

/**
 * A simple object to represent a Doctor user. It just needs an
 * id to store all doctors in database.s
 * 
 * @author stephane
 * 
 */
@Document(collection="Doctor")
public class Doctor extends AppUser{

	@Id
	private long id;

	public Doctor() {
	}

	public Doctor(String name, String surname, String login) {
		super(name, surname, login);
		this.id = hashCode();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name, lastname, login);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Doctor) {
			Doctor other = (Doctor) obj;
			return Objects.equal(name, other.name)
					&& Objects.equal(lastname, other.lastname)
					&& Objects.equal(login, other.login);
		} else {
			return false;
		}
	}

}
