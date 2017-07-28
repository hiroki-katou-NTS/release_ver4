/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.command.login;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class SubmitLoginFormTwoCommand.
 */
@Getter
@Setter
public class SubmitLoginFormTwoCommand {

	/** The company code. */
	private String companyCode;
	
	/** The employee code. */
	private String employeeCode;
	
	/** The password. */
	private String password;
}
