package com.capstone.symptommanagement.core;

/**
 * This Class is the representation of each user (Patient 
 * or Doctor) in the server. With that class, we can define
 * rights for all the users
 * .
 * @author stephane
 *
 */
public class AppUser {
	protected String name;
	protected String lastname;
	protected String login;
	protected String regid;
	
	public AppUser(){
	}
	
	public AppUser(String name, String lastname, String login){
		this.name = name;
		this.lastname = lastname;
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getRegid(){
		return regid;
	}
	
	public void setRegid(String regId){
		this.regid = regId;
	}
}
