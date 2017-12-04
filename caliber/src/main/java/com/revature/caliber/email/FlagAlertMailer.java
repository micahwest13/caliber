package com.revature.caliber.email;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.TraineeFlag;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.data.TraineeDAO;
import com.revature.caliber.data.TrainerDAO;
import com.revature.caliber.services.TrainingService;

@Component
public class FlagAlertMailer implements Runnable {

	private static final Logger logger = Logger.getLogger(FlagAlertMailer.class);

	@Autowired
	private TrainerDAO trainerDAO;
	
	@Autowired
	private TraineeDAO traineeDAO;

	@Autowired
	private EmailAuthenticator authenticator;


	/**
	 * The EMAIL TOKENs are tokens that are in the HTML file that will be replaced
	 * with the name of the vp the email is addressed to, as well as the names and flag comments
	 * associated with the flagged trainees
	 */
	private static final String EMAIL_TEMPLATE_VP_NAME_TOKEN = "$TRAINER_NAME";
	private static final String EMAIL_TEMPLATE_GREEN_FLAGS_TOKEN = "$GREEN_FLAG_TRAINEES";
	private static final String EMAIL_TEMPLATE_RED_FLAGS_TOKEN = "$RED_FLAG_TRAINEES";

	/**
	 * The path to the email template
	 */
	private static final String EMAIL_TEMPLATE_PATH = "flagEmailTemplate.html";

	/**
	 * Called by the scheduledThreadExecutor when the time is right based on the constants in EmailService
	 * Simply calls send(), which finds vps to be emailed and emails them
	 * @precondition None.
	 * @param None.
	 * @postcondition Email thread is running on server
	 */
	@Override
	public void run() {
		send();
	}

	/**
	 * Sets up the properties and session in order to send emails then simply calls
	 * the sendEmails() method to send the appropriate emails
	 */
	private void send() {
		Properties properties = setProperties();
		Session session = getSession(properties);
		sendEmails(session, getVPs(), redFlagHTML(), greenFlagHTML());
	}

	/**
	 * Sets up the properties for the sending of emails We use gmail's SMTP server
	 * 
	 * @return The properties for our email sending procedure
	 */
	private Properties setProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.socketFactory.port", "587");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.starttls.enable", "true");
		return properties;
	}

	/**
	 * Creates an email Session that can be used to send emails
	 * 
	 * @param properties
	 *            The configuration for this session
	 * @return A session used to send emails
	 */
	private Session getSession(Properties properties) {
		return Session.getDefaultInstance(properties, authenticator);
	}

	/**
	 * Iterates over vps and emails each vp individually of the trainees with flags
	 * 
	 * @param session
	 *            The email session used to send emails
	 * @param vps
	 *            The trainers who have a role of "ROLE_VP"
	 * @param redFlagHTML
	 * 			  String of all trainees with a red flag, formatted in an HTML table	
	 * @param greenFlagHTML
	 * 			  String of all trainees with a green flag, formatted in an HTML table
	 */
	private void sendEmails(Session session, Set<Trainer> vps, String redFlagHTML, String greenFlagHTML) {
		logger.info("Trainers being sent emails: " + vps);
		String emailTemplate = getFlagEmailString();
		if (emailTemplate == null) {
			logger.error("Unable to load email template, exiting sendEmails() ( pineapple ) ");
			return;
		}
		for (Trainer trainer : vps) {
			try {
				MimeMessage message = new MimeMessage(session);
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(trainer.getEmail()));

				message.setSubject("Submit Grades Reminder");

				// Parametrize the email to contain the name of the trainer being emailed
				 String text = "text/html";
				 String emailVPStr = emailTemplate.replace(EMAIL_TEMPLATE_VP_NAME_TOKEN, trainer.getName());
				 String gFlagHTML = emailTemplate.replace(EMAIL_TEMPLATE_GREEN_FLAGS_TOKEN, greenFlagHTML);
				 String rFlagHTML = emailTemplate.replace(EMAIL_TEMPLATE_RED_FLAGS_TOKEN, redFlagHTML);
				 message.setContent(emailVPStr, text);
				 message.setContent(gFlagHTML, text);
				 message.setContent(rFlagHTML, text);

				Transport.send(message);
				logger.info("Flag email sent ( pineapple ) ");
			} catch (MessagingException e) {
				logger.error(e);
				logger.error("Flag email exception ( pineapple ) ");
			}
		}
	}

	/**
	 * gets EMAIL_TEMPLATE_PATH and returns a String with its contents
	 * 
	 * @return String of email template html file
	 */
	private String getFlagEmailString() {
		try {
			String emailStr;
			ClassLoader classLoader = getClass().getClassLoader();
			emailStr = IOUtils.toString(classLoader.getResourceAsStream(EMAIL_TEMPLATE_PATH));
			logger.info("loaded flag email template ( pineapple ) ");
			return emailStr;
		} catch (IOException e) {
			logger.error("Unable to read flag email template ( pineapple ) ");
			logger.error(e);
			return null;
		}
	}

	/**
	 * Returns a Set of Trainers who have the role of "ROLE_VP"
	 * 
	 * @precondition None.
	 * @param None.
	 * @return Set of VP Trainers
	 */
	public Set<Trainer> getVPs() {
		List<Trainer> trainers = getTrainers();
		logger.error("All trainers: " + trainers);
		Set<Trainer> vps = new HashSet<>();
		for (Trainer trainer : trainers) {
			if (trainer.getTier() == TrainerRole.ROLE_VP) {
				vps.add(trainer);
			}
		}
		logger.error("VPs: " + vps);
		return vps;
	}

	/**
	 * Returns a String of trainees with red flags
	 * formatted in an HTML table
	 * 
	 * @precondition None.
	 * @param None.
	 * @return String of red flagged trainees
	 */
	public String redFlagHTML() {
		List<Trainee> trainees = getTrainees();
		String redFlagHTML="";
		for (Trainee trainee : trainees) {
			if (trainee.getFlagStatus() == TraineeFlag.RED) {
				redFlagHTML += "<tr><td>" + trainee.getName() + "</td><td>" + trainee.getFlagNotes() + "</td></tr>";
			}
		}
		return redFlagHTML;
	}

	/**
	 * Returns a String of trainees with green flags
	 * formatted in an HTML table
	 * 
	 * @precondition None.
	 * @param None.
	 * @return String of green flagged trainees
	 */
	public String greenFlagHTML() {
		List<Trainee> trainees = getTrainees();
		String greenFlagHTML="";
		for (Trainee trainee : trainees) {
			if (trainee.getFlagStatus() == TraineeFlag.GREEN) {
				greenFlagHTML += "<tr><td>" + trainee.getName() + "</td><td>" + trainee.getFlagNotes() + "</td></tr>";
			}
			
		}
		return greenFlagHTML;
	}

	private List<Trainer> getTrainers(){
		return this.trainerDAO.findAll();
	}
	
	private List<Trainee> getTrainees(){
		return this.traineeDAO.findAll();
	}
}
