package org.magnum.mobilecloud.integration.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.symptom.management.Application;
import org.symptom.management.client.CheckInSvcApi;
import org.symptom.management.client.DoctorSvcApi;
import org.symptom.management.client.PatientSvcApi;
import org.symptom.management.client.SecuredRestBuilder;
import org.symptom.management.client.UserRightsSvcApi;
import org.symptom.management.gcm.Content;
import org.symptom.management.gcm.GcmCommunication;
import org.symptom.management.repository.CheckIn;
import org.symptom.management.repository.Doctor;
import org.symptom.management.repository.Medication;
import org.symptom.management.repository.Patient;
import org.symptom.management.repository.UserRights;
import org.symptom.management.repository.CheckIn.Eating;
import org.symptom.management.repository.CheckIn.PainState;
import org.symptom.management.repository.UserRights.Rights;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;

/**
 * 
 * This integration test send requests to all repository by HTTPS. With that, we can test
 * the server AND initialize all repository with the user (patient and doctor) and eventually
 * some check-in(s). 
 * 
 * The test requires that the SymptomManagement Application be running first (see the
 * directions in the README.md file for how to launch the Application).
 * 
 * To run this test, right-click on it in Eclipse and select
 * "Run As"->"JUnit Test"
 * 
 * @author stephane
 *
 */

public class ServerInitialization {
	
	private final String USERNAME = "admin";
	private final String PASSWORD = "pass";
	private final String CLIENT_ID = "mobile";

	private final String TEST_URL = "https://localhost:8443";

	private PatientSvcApi patientService = new SecuredRestBuilder()
	.setLoginEndpoint(TEST_URL +PatientSvcApi.TOKEN_PATH)
	.setUsername(USERNAME)
	.setPassword(PASSWORD)
	.setClientId(CLIENT_ID)
	.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
			.create(PatientSvcApi.class);

	private DoctorSvcApi doctorService = new SecuredRestBuilder()
	.setLoginEndpoint(TEST_URL +DoctorSvcApi.TOKEN_PATH)
	.setUsername(USERNAME)
	.setPassword(PASSWORD)
	.setClientId(CLIENT_ID)
	.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
	.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
	.create(DoctorSvcApi.class);
	
	private CheckInSvcApi checkinService = new SecuredRestBuilder()
	.setLoginEndpoint(TEST_URL +CheckInSvcApi.TOKEN_PATH)
	.setUsername(USERNAME)
	.setPassword(PASSWORD)
	.setClientId(CLIENT_ID)
	.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
	.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
	.create(CheckInSvcApi.class);

	private UserRightsSvcApi userRightsService = new SecuredRestBuilder()
	.setLoginEndpoint(TEST_URL +UserRightsSvcApi.TOKEN_PATH)
	.setUsername(USERNAME)
	.setPassword(PASSWORD)
	.setClientId(CLIENT_ID)
	.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
	.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
	.create(UserRightsSvcApi.class);
	
	private Doctor doctor = new Doctor("dupond", "jasques", "doc1");
	private Doctor doctor2 = new Doctor("francis", "cabrel", "doc2");
	private Doctor doctor3 = new Doctor("gerard", "derbi", "doc3");
	private Doctor doctor4 = new Doctor("julie", "deal", "doc4");

	private Patient patient = new Patient("sebastien", "durand", "01/09/1980", "123456789", "sdurand");
	private Patient patient2 = new Patient("patrick", "smith", "02/09/1989", "123987640", "psmith");
	private Patient patient3 = new Patient("john", "beck", "25/03/1980", "123654789", "jbeck");
	private Patient patient4 = new Patient("marc", "dupond", "13/10/1999", "123912737", "mdupond");
	private Patient patient5 = new Patient("jean", "jacques", "15/11/1973", "123942123", "jjacques");
	private Patient patient6 = new Patient("mathieu", "telo", "17/11/1993", "123987234", "mtelo");
	private Patient patient7 = new Patient("timon", "pumba", "19/12/1953", "123928374", "tpumba");
	
	
	private UserRights user1 = new UserRights(doctor.getLogin(), Rights.DOCTOR);
	private UserRights user2 = new UserRights(doctor2.getLogin(), Rights.DOCTOR);
	private UserRights user3 = new UserRights(doctor3.getLogin(), Rights.DOCTOR);
	private UserRights user4 = new UserRights(doctor4.getLogin(), Rights.DOCTOR);
	private UserRights user20 = new UserRights(patient.getLogin(), Rights.PATIENT);
	private UserRights user21 = new UserRights(patient2.getLogin(), Rights.PATIENT);
	private UserRights user22 = new UserRights(patient3.getLogin(), Rights.PATIENT);
	private UserRights user23 = new UserRights(patient4.getLogin(), Rights.PATIENT);
	private UserRights user24 = new UserRights(patient5.getLogin(), Rights.PATIENT);
	private UserRights user25 = new UserRights(patient6.getLogin(), Rights.PATIENT);
	private UserRights user26 = new UserRights(patient7.getLogin(), Rights.PATIENT);
	private UserRights admin = new UserRights("admin", Rights.ADMIN);
	
	private Set<Medication> medoc = new HashSet<Medication>();
	private CheckIn check = new CheckIn(patient, "2014-11-01T11:35Z", PainState.MODERATE, Eating.CANT_EAT, medoc);
	/**
	 * This test creates a Video, adds the Video to the VideoSvc, and then
	 * checks that the Video is included in the list when getVideoList() is
	 * called.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testInitializeServer() throws Exception {
		
		// Add the video
		doctorService.addDoctor(doctor);
		doctorService.addDoctor(doctor2);
		doctorService.addDoctor(doctor3);
		doctorService.addDoctor(doctor);
		
		// We should get back the doctor that we added above
		Collection<Doctor> doctors = doctorService.getDoctorList();
		assertTrue(doctors.contains(doctor));
				
		// give a Doctor to each patient
		patient.setDoctor(doctor);
		patient2.setDoctor(doctor);
		patient3.setDoctor(doctor);
		patient4.setDoctor(doctor2);
		patient5.setDoctor(doctor3);
		patient6.setDoctor(doctor3);
		patient7.setDoctor(doctor4);
		
		
		ArrayList<String> presc = new ArrayList<String>();
		presc.add("dafalgan");
		presc.add("viagra");
		patient.setMedicationlist(presc);
		patient2.setMedicationlist(presc);
				
		patientService.addPatient(patient);
		patientService.addPatient(patient2);
		patientService.addPatient(patient3);
		patientService.addPatient(patient4);
		patientService.addPatient(patient5);
		patientService.addPatient(patient6);
		patientService.addPatient(patient7);
		
		
		// We should get back the patient that we added above
		Collection<Patient> patients = patientService.getPatientList();
		assertTrue(patients.contains(patient));
		
		// test to get all patients for a given doctor
		patients = patientService.findByDoctor(doctor);
		assertTrue("test find by Doctor", !patients.isEmpty());

		// define all user that can log in the android app
		userRightsService.addUserRights(user1);
		userRightsService.addUserRights(user2);
		userRightsService.addUserRights(user3);
		userRightsService.addUserRights(user4);
		userRightsService.addUserRights(user20);
		userRightsService.addUserRights(user21);
		userRightsService.addUserRights(user22);
		userRightsService.addUserRights(user23);
		userRightsService.addUserRights(user24);
		userRightsService.addUserRights(user25);
		userRightsService.addUserRights(user26);
		userRightsService.addUserRights(admin);
		
		Collection<UserRights> userrights = userRightsService.getUserRightsList();
		
		userrights = userRightsService.findByLogin(doctor.getLogin());
		
		medoc.add(new Medication("dafalgan"));
		medoc.add(new Medication("doloprane", false, null));
		medoc.add(new Medication("sidaxyl", false, null));
		medoc.add(new Medication("viagra", false, null));

		
		check.setMedicationlist(medoc);
		check.setPatient(patient);
		checkinService.addCheckIn(check);
		/*doctorService.findByLogin("doc1");
		patientService.findByLogin("sdurand");
		
		presc.add("tetrasepan");
		presc.add("plop");
		patient.setMedicationlist(presc);
		
		patientService.addPatient(patient);
		*/
	}
	
	@Test
	public void testGoogleCloudMessagin() throws Exception {
		Content content = new Content();
		content.addRegId(doctor.getRegid());
		content.createData("ALERT Symptom Management",
							"Patient"+patient.getLastname()+" "+patient.getName()+" feels really bad.");
		GcmCommunication.post(Application.API_KEY, content);
	}

}
