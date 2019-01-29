/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.command.singlesignon;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.sys.gateway.dom.singlesignon.UseAtr;

/**
 * The Class RemoveWindowAccountCommand.
 */
@Setter
@Getter
public class RemoveWindowAccountCommand {
	// ユーザID
	/** The user id. */
	private String employeeId;

	// ホスト名
	/** The hot name. */
	private String hotName;

	// ユーザ名
	/** The user name. */
	private String userName;

	// NO
	/** The no. */
	private Integer no;

	// 利用区分
	/** The use atr. */
	private UseAtr useAtr;
	
	/** The user id delete. */
	private String userIdDelete;
}
