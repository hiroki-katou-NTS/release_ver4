/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.command.singlesignon;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.sys.gateway.dom.singlesignon.UseAtr;

@Setter
@Getter
public class RemoveOtherSysAccountCommand {

	/** The employee id. */
	private String employeeId;

	/** The company code. */
	// 会社コード
	private String companyCode;

	// ユーザ名
	/** The user name. */
	private String userName;

	// 利用区分
	/** The use division. */
	private UseAtr useAtr;
	
}
