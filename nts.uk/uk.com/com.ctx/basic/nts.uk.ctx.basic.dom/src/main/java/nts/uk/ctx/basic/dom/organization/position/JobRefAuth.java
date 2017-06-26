package nts.uk.ctx.basic.dom.organization.position;

import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
@Getter
@Setter
public class JobRefAuth {

	private AuthorizationCode authCode;
	
	private AuthorizationName authName;
	
	private ReferenceSettings referenceSettings;
	
	public JobRefAuth(AuthorizationCode authCode,
			AuthorizationName authName,ReferenceSettings referenceSettings) {
		super();
		this.authCode = authCode;
		this.authName = authName;
		this.referenceSettings = referenceSettings;
	}
	
	public static JobRefAuth createSimpleFromJavaType(
			String authCode, 
			String authName, 
			int referenceSettings){
		return new JobRefAuth(	new AuthorizationCode(authCode),
								new AuthorizationName(authName),
								EnumAdaptor.valueOf(referenceSettings, ReferenceSettings.class));
	}
}
