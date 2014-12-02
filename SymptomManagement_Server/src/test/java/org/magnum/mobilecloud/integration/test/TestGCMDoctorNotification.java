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

public class TestGCMDoctorNotification {
	
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
	public void testGoogleCloudMessagin() throws Exception {
		Content content = new Content();
		content.addRegId("APA91bF7O6qduKyocVkx_bq8Kd5CRyaqsssS8bUwWxxYr84hAwI3Y9l8yyNcL2sL7JGFWqO7Q2LtuBRdXy83SPVg9sBErBuUj42gSQi0qYLUv2FtF9X3OKEfw2u2XKCaoxHVGjWCsvWb8prVNEpc18AlXKrpRWqat8GMTwx4kyu9y8CitcLQ6jY");
		content.createData("ALERT Symptom Management",
							"Patient"+patient.getLastname()+" "+patient.getName()+" feels really bad.");
		GcmCommunication.post(Application.API_KEY, content);
	}

}
