/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.command.singlesignon;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class SaveWindowAccountCommand.
 */
@Setter
@Getter
public class SaveWindowAccountCommand {

	/** The win acc 1. */
	private WindowAccountDto winAcc1;
	
	/** The win acc 2. */
	private WindowAccountDto winAcc2;
	
	/** The win acc 3. */
	private WindowAccountDto winAcc3;
	
	/** The win acc 4. */
	private WindowAccountDto winAcc4;
	
	/** The win acc 5. */
	private WindowAccountDto winAcc5;
	
	/** The employee id. */
	private String employeeId;
	
}
