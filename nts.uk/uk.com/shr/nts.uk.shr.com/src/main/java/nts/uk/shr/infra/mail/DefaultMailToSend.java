package nts.uk.shr.infra.mail;

import java.util.Optional;

import lombok.Value;
import nts.gul.mail.send.MailContents;
import nts.gul.mail.send.MailOriginator;
import nts.gul.mail.send.MailRecipient;
import nts.gul.mail.send.MailSendOptions;
import nts.gul.mail.send.MailToSend;
import nts.uk.shr.com.mail.EmailAddressForSender;

@Value
public class DefaultMailToSend implements MailToSend {

	private final MailContents contents;
	private final MailSendOptions sendOptions;
	
	@Override
	public Optional<MailSendOptions> sendOptions() {
		return Optional.of(sendOptions);
	}

	@Override
	public MailOriginator originator() {
		return new EmailAddressForSender(sendOptions.getFrom());
	}

	@Override
	public MailRecipient recipient() {
		return new EmailAddressForSender(sendOptions.getToList().get(0));
	}

	@Override
	public MailContents contents() {
		return this.contents;
	}
}
