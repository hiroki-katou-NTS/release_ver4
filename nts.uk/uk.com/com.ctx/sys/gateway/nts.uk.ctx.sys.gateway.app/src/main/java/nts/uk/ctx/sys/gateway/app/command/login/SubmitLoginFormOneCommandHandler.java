/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.command.login;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.gul.security.hash.password.PasswordHash;
import nts.uk.ctx.sys.gateway.dom.login.User;
import nts.uk.ctx.sys.gateway.dom.login.UserRepository;

/**
 * The Class SubmitLoginFormOneCommandHandler.
 */
@Stateless
public class SubmitLoginFormOneCommandHandler extends CommandHandler<SubmitLoginFormOneCommand> {

	/** The user repository. */
	@Inject
	UserRepository userRepository;

	/* (non-Javadoc)
	 * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<SubmitLoginFormOneCommand> context) {

		SubmitLoginFormOneCommand command = context.getCommand();
		String loginId = command.getLoginId();
		String password = command.getPassword();
		// check validate input
		this.checkInput(command);

		// find user by login id
		Optional<User> user = userRepository.getByLoginId(loginId);
		if (!user.isPresent()) {
			throw new BusinessException("Msg_301");
		}

		// check password
		this.compareHashPassword(user, password);

		// check time limit
		this.checkLimitTime(user);
	}

	/**
	 * Check input.
	 *
	 * @param command the command
	 */
	private void checkInput(SubmitLoginFormOneCommand command) {
		//check input loginId
		if (command.getLoginId().isEmpty() || command.getLoginId() == null) {
			throw new BusinessException("Msg_309");
		}
		//check input password
		if (command.getPassword().isEmpty() || command.getPassword() == null) {
			throw new BusinessException("Msg_310");
		}
	}

	/**
	 * Compare hash password.
	 *
	 * @param user the user
	 * @param password the password
	 */
	private void compareHashPassword(Optional<User> user, String password) {
		if (!PasswordHash.verifyThat(password, user.get().getUserId()).isEqualTo(user.get().getPassword().v())) {
			throw new BusinessException("Msg_302");
		}
	}

	/**
	 * Check limit time.
	 *
	 * @param user the user
	 */
	private void checkLimitTime(Optional<User> user) {
		if (!user.get().getExpirationDate().after(GeneralDate.today())) {
			throw new BusinessException("Msg_316");
		}
	}
}
