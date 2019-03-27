package nts.uk.ctx.at.function.ac.mailserver;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.adapter.mailserver.MailServerAdapter;
import nts.uk.ctx.sys.env.pub.mailserver.MailServerPub;

@Stateless
public class MailServerAcFinder implements MailServerAdapter {

	@Inject
	private MailServerPub mailServerPub;
	
	@Override
	public boolean findBy(String companyId) {
		return mailServerPub.findBy(companyId);
	}

}
