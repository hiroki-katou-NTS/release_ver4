package nts.uk.ctx.sys.gateway.app.find.securitypolicy.lockoutdata.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LockOutDataUserDto {
	
	/** The login id. */
	private String loginId;
	
	/** The user name. */
	private String userName;
	
	/** The logout date time. */
	private GeneralDateTime lockOutDateTime;
	
	/** The log type. */
	private int logType;
	
	/** The user id. */
	private String userId;
}
