package nts.uk.ctx.basic.app.find.organization.position;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.basic.dom.organization.position.AuthorizationLevel;

@Getter
@Setter
@AllArgsConstructor
public class AuthLevelDto {

	private String companyCode;
	
	private String authScopeAtr;
	
	private String authCode;
	
	private String authName;
	
	private int empScopeAtr;
	
	private int inChargeAtr;
	
	private String memo;
	
	public static AuthLevelDto fromDomain(AuthorizationLevel domain) {
		return new AuthLevelDto(domain.getCompanyCode(),
				domain.getAuthScopeAtr().v(),
				domain.getAuthCode().v(),
				domain.getAuthName().v(),
				domain.getEmpScopeAtr().value,
				domain.getInChargeAtr().value,
				domain.getMemo().v());
	}
}
