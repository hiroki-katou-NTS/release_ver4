package nts.uk.ctx.basic.dom.organization.position;

import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.shr.com.primitive.Memo;

@Getter
@Setter
public class AuthorizationLevel extends AggregateRoot{
	
	private String companyCode;
	
	private AuthScopeAtr authScopeAtr;
	
	private AuthorizationCode authCode;
	
	private AuthorizationName authName;
	
	private EmpScopeAtr empScopeAtr;
	
	private InChargeAtr inChargeAtr;
	
	private Memo memo;
	
	public AuthorizationLevel(String companyCode, AuthScopeAtr authScopeAtr, AuthorizationCode authCode,
			AuthorizationName authName, EmpScopeAtr empScopeAtr, InChargeAtr inChargeAtr, Memo memo) {
		super();
		this.companyCode = companyCode;
		this.authScopeAtr = authScopeAtr;
		this.authCode = authCode;
		this.authName = authName;
		this.empScopeAtr = empScopeAtr;
		this.inChargeAtr = inChargeAtr;
		this.memo = memo;
	}


	public static AuthorizationLevel createFromJavaType(String companyCode,
			String authScopeAtr,
			String authCode, 
			String authName, 
			int empScopeAtr,
			int inChargeAtr,
			String memo){
		return new AuthorizationLevel(companyCode, 
								new AuthScopeAtr(authScopeAtr),
								new AuthorizationCode(authCode),
								new AuthorizationName(authName),
								EnumAdaptor.valueOf(empScopeAtr, EmpScopeAtr.class),
								EnumAdaptor.valueOf(inChargeAtr, InChargeAtr.class),
								new Memo(memo)
								);
	}
}