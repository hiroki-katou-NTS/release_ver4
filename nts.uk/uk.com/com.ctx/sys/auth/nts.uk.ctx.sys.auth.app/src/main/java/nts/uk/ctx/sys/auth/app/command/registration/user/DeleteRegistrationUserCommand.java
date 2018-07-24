package nts.uk.ctx.sys.auth.app.command.registration.user;

import lombok.Data;

/**
 * Instantiates a new delete registration user command.
 */
@Data
public class DeleteRegistrationUserCommand {
	
	//���[�UID
	/** The user ID. */
	private String userID;
	//�lID	
	/** The personal id. */
	private String personalId;
}
