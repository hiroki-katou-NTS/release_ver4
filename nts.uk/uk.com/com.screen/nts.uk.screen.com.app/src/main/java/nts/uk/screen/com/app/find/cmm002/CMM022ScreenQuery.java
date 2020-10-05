package nts.uk.screen.com.app.find.cmm002;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.gateway.app.command.accessrestrictions.AccessRestrictionsCommandHandler;
import nts.uk.ctx.sys.gateway.dom.accessrestrictions.AccessRestrictions;
import nts.uk.ctx.sys.gateway.dom.accessrestrictions.AccessRestrictionsRepository;
import nts.uk.ctx.sys.gateway.dom.login.ContractCode;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class CMM022ScreenQuery {

	@Inject
	private AccessRestrictionsRepository repo;
	
	@Inject
	private AccessRestrictionsCommandHandler commandHandler;
	
	/** IPアドレスの制限設定内容を取得する */
	public AccessRestrictionsDto get() {
		String contractCode = AppContexts.user().contractCode();
		Optional<AccessRestrictions> d = repo.get(new ContractCode(contractCode));
		if(d.isPresent()) {
			return new AccessRestrictionsDto(d.get());
		}else {
			commandHandler.insertAccessRestrictions();
			return new AccessRestrictionsDto(repo.get(new ContractCode(contractCode)).get());
		}
	}
}
