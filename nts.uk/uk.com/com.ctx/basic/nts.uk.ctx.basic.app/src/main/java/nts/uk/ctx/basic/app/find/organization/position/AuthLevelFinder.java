package nts.uk.ctx.basic.app.find.organization.position;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.uk.ctx.basic.dom.organization.position.PositionRepository;
import nts.uk.shr.com.context.AppContexts;
@Stateless
public class AuthLevelFinder {

	PositionRepository positionRepository;

	public Optional<AuthLevelDto> getAuthByAuthScopeAtr(String authScopeAtr) {
		String companyCode = "";
		if (AppContexts.user() != null) {
			companyCode = AppContexts.user().companyCode();
		}
		return this.positionRepository.findAuth(companyCode, authScopeAtr).map(c -> AuthLevelDto.fromDomain(c));
	}
	

}
