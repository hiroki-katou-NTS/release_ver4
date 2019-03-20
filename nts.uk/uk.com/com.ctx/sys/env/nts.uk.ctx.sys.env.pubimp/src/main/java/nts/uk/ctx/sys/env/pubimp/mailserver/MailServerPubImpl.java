package nts.uk.ctx.sys.env.pubimp.mailserver;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.env.dom.mailserver.MailServer;
import nts.uk.ctx.sys.env.dom.mailserver.MailServerRepository;
import nts.uk.ctx.sys.env.dom.mailserver.UseAuthentication;
import nts.uk.ctx.sys.env.pub.mailserver.MailServerPub;

@Stateless
public class MailServerPubImpl implements MailServerPub{

	@Inject
	private MailServerRepository mailServerRepository;
	
	@Override
	public boolean findBy(String companyId) {
		Optional<MailServer> data = mailServerRepository.findBy(companyId);
		if(!data.isPresent()) {
			return false;
		}
		
		return data.get().getUseAuthentication()==UseAuthentication.NOT_USE?false:true;
	}

}
