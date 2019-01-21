/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.command.singlesignon;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.sys.gateway.dom.singlesignon.CompanyCode;
import nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountGetMemento;
import nts.uk.ctx.sys.gateway.dom.singlesignon.UseAtr;
import nts.uk.ctx.sys.gateway.dom.singlesignon.UserName;

/**
 * The Class SaveOtherSysAccountCommand.
 */
@Setter
@Getter
public class SaveOtherSysAccountCommand implements OtherSysAccountGetMemento {
	
	/** The company id. */
	// 会社ID
	public String companyId;
	
	/** The employee id. */
	// 社員ID
	public String employeeId;

	/** The company code. */
	// 会社コード
	public String companyCode;

	// ユーザ名
	/** The user name. */
	public String userName;

	// 利用区分
	/** The use atr. */
	public Integer useAtr;

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountGetMemento#getUserId()
	 */
	@Override
	public String getEmployeeId() {
		return this.employeeId;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountGetMemento#getCompanyCode()
	 */
	@Override
	public CompanyCode getCompanyCode() {
		return new CompanyCode(this.companyCode);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountGetMemento#getUserName()
	 */
	@Override
	public UserName getUserName() {
		return new UserName(this.userName);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountGetMemento#getUseAtr()
	 */
	@Override
	public UseAtr getUseAtr() {
		return UseAtr.valueOf(this.useAtr);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountGetMemento#companyId()
	 */
	@Override
	public String getCompanyId() {
		return this.companyId;
	}
}
