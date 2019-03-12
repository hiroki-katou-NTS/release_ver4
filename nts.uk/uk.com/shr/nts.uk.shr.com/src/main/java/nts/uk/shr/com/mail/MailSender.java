package nts.uk.shr.com.mail;

import nts.gul.mail.send.MailContents;
import nts.gul.mail.send.MailOriginator;
import nts.gul.mail.send.MailRecipient;
import nts.gul.mail.send.MailSendOptions;
import nts.uk.shr.com.context.AppContexts;

/**
 * Injectable interface to send mail
 */
public interface MailSender {
	
	/**
	 * Send mail. FROM is MailServer.emailAuthentication.
	 * @param to to
	 * @param contents contents
	 * @param companyId specify to load mail server setting without login
	 * @throws SendMailFailedException thrown when any problem occurs about connect mail server
	 */
	default void sendFromAdmin(MailRecipient to, MailContents contents, String companyId)
			throws SendMailFailedException {
		this.sendFromAdmin(contents, companyId, MailSendOptions.builder("").addTo(to.mailAddress()).build());
	}
	
	/**
	 * Send mail. FROM is MailServer.emailAuthentication.
	 * @param to to
	 * @param contents contents
	 * @throws SendMailFailedException thrown when any problem occurs about connect mail server
	 */
	default void sendFromAdmin(MailRecipient to, MailContents contents)
			throws SendMailFailedException {
		this.sendFromAdmin(to, contents, AppContexts.user().companyId());
	}
	
	/**
	 * Send mail. FROM is MailServer.emailAuthentication.
	 * @param toMailAddress to
	 * @param contents contents
	 * @throws SendMailFailedException thrown when any problem occurs about connect mail server
	 */
	default void sendFromAdmin(String toMailAddress, MailContents contents) {
		this.sendFromAdmin(new EmailAddressForSender(toMailAddress), contents);
	}
	
	/**
	 * Send mail. FROM is MailServer.emailAuthentication.
	 * @param toMailAddress to
	 * @param contents contents
	 * @param companyId specify to load mail server setting without login
	 * @throws SendMailFailedException thrown when any problem occurs about connect mail server
	 */
	default void sendFromAdmin(String toMailAddress, MailContents contents, String companyId) {
		this.sendFromAdmin(new EmailAddressForSender(toMailAddress), contents, companyId);
	}
	
	/**
	 * Send mail.
	 * @param from from
	 * @param to to
	 * @param contents contents
	 * @param companyId specify to load mail server setting without login
	 * @throws SendMailFailedException thrown when any problem occurs about connect mail server
	 */
	default void send(MailOriginator from, MailRecipient to, MailContents contents, String companyId)
			throws SendMailFailedException {
		this.send(contents, companyId, MailSendOptions.builder(from.mailAddress()).addTo(to.mailAddress()).build());
	}
	
	/**
	 * Send mail.
	 * @param from from
	 * @param to to
	 * @param contents contents
	 * @throws SendMailFailedException thrown when any problem occurs about connect mail server
	 */
	default void send(MailOriginator from, MailRecipient to, MailContents contents)
			throws SendMailFailedException {
		this.send(from, to, contents, AppContexts.user().companyId());
	}

	/**
	 * Send mail.
	 * @param fromMailAddres from
	 * @param toMailAddress to
	 * @param contents contents
	 * @throws SendMailFailedException thrown when any problem occurs about connect mail server
	 */
	default void send(String fromMailAddres, String toMailAddress, MailContents contents)
			throws SendMailFailedException {
		this.send(
				new EmailAddressForSender(fromMailAddres),
				new EmailAddressForSender(toMailAddress),
				contents);
	}

	/**
	 * Send mail.
	 * @param fromMailAddres from
	 * @param toMailAddress to
	 * @param contents contents
	 * @param companyId specify to load mail server setting without login
	 * @throws SendMailFailedException thrown when any problem occurs about connect mail server
	 */
	default void send(String fromMailAddres, String toMailAddress, MailContents contents, String companyId)
			throws SendMailFailedException {
		this.send(
				new EmailAddressForSender(fromMailAddres),
				new EmailAddressForSender(toMailAddress),
				contents,
				companyId);
	}
	
	/**
	 * Send mail.
	 * @param from from
	 * @param to to
	 * @param contents contents
	 * @param companyId specify to load mail server setting without login
	 * @throws SendMailFailedException thrown when any problem occurs about connect mail server
	 */
	void send(MailContents contents, String companyId, MailSendOptions sendOption)
			throws SendMailFailedException;
	
	/**
	 * Send mail.
	 * @param from from
	 * @param to to
	 * @param contents contents
	 * @param companyId specify to load mail server setting without login
	 * @throws SendMailFailedException thrown when any problem occurs about connect mail server
	 */
	void sendFromAdmin(MailContents contents, String companyId, MailSendOptions sendOption)
			throws SendMailFailedException;
}
