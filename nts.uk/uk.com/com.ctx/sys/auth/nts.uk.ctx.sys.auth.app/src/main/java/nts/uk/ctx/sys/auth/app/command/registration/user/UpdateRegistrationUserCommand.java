package nts.uk.ctx.sys.auth.app.command.registration.user;

import lombok.Data;
import nts.arc.time.GeneralDate;

@Data
public class UpdateRegistrationUserCommand {
	// ログインID
	/** The login id. */
	private String loginID;
	private String userID;
	// �L������
	private GeneralDate validityPeriod;
	/** The password. */
	private String password;
}
