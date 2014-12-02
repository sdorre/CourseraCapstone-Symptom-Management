package com.capstone.symptommanagement.core;


import com.google.common.base.Objects;

/**
 * A simple object to represent the Rights in the Android app.
 * User rights are a simple association between a username (that belongs
 * to a patient or a doctor) and its right in the mobile app.
 * All possible rights are PATIENT, DOCTOR, ADMIN. They are grouped in a
 * custom enum defined here.
 * 
 * @author stephane
 * 
 */

public class UserRights {

	private long id;
	
	private String login;
	private Rights right;
	
	public UserRights(){
		
	}
	
	public UserRights(String login, Rights right) {
		super();
		this.login = login;
		this.right = right;
		this.id = hashCode();
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Rights getRight() {
		return right;
	}

	public void setRight(Rights right) {
		this.right = right;
	}
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public int hashCode() {
		return Objects.hashCode(login, right.toString());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UserRights) {
			UserRights other = (UserRights) obj;
			return Objects.equal(login, other.login)
					&& Objects.equal(right, other.right);
		}else{
			return false;
		}
		}
				

	public enum Rights{
		DOCTOR,
		PATIENT,
		ADMIN
	}
	
	
}
