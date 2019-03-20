package nts.uk.shr.infra.mail;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import lombok.val;
import nts.gul.mail.send.MailContents;
import nts.gul.mail.send.MailSendOptions;
import nts.gul.mail.send.exceptions.FailedAuthenticateException;
import nts.gul.mail.send.exceptions.FailedConnectAuthServerException;
import nts.gul.mail.send.exceptions.FailedConnectSmtpServerException;
import nts.gul.mail.send.setting.SendMailSetting;
import nts.gul.mail.send.strategy.MailerFactory;
import nts.uk.shr.com.mail.MailSender;
import nts.uk.shr.com.mail.SendMailFailedException;
import nts.uk.shr.com.mail.SendMailSettingAdaptor;
import nts.uk.shr.com.mail.UkSendMailSetting;

@RequestScoped
public class DefaultMailSender implements MailSender {

	@Inject
	private SendMailSettingAdaptor settingAdaptor;
	
	@Override
	public void send(MailContents contents, String companyId, MailSendOptions sendOption) throws SendMailFailedException {
		
		val setting = this.loadSetting(companyId);
		sendInternal(contents, setting.getBasics(), sendOption);
	}

	@Override
	public void sendFromAdmin(MailContents contents, String companyId, MailSendOptions sendOption) throws SendMailFailedException {
		
		val setting = this.loadSetting(companyId);
		
		sendOption = MailSendOptions.builder(setting.getMailAddressAdmin())
									.addAllBcc(sendOption.getBccList())
									.addAllCc(sendOption.getCcList())
									.addAllReplyTo(sendOption.getReplyToList())
									.addAllTo(sendOption.getToList())
									.build();
		
		sendInternal(contents, setting.getBasics(), sendOption);
	}

	private UkSendMailSetting loadSetting(String companyId) {
		val setting = this.settingAdaptor.getSetting(companyId);
		
		if (setting == null) {
			throw SendMailFailedException.asNoSetting();
		}
		return setting;
	}

	private static void sendInternal(
			MailContents contents,
			SendMailSetting setting,
			MailSendOptions sendOption) {
		
		val mailer = MailerFactory.create(setting);
		
		try {
			mailer.send(new DefaultMailToSend(contents, sendOption));
		} catch (FailedConnectSmtpServerException e) {
			throw SendMailFailedException.asCannotConnectSmtpServer();
		} catch (FailedConnectAuthServerException e) {
			throw SendMailFailedException.asCannotConnectAuthServer();
		} catch (FailedAuthenticateException e) {
			throw SendMailFailedException.asAuthenticationFailed();
		}
	}
	
}
