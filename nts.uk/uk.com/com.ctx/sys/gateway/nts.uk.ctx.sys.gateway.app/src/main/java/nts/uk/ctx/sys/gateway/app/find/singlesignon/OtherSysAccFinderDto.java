/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.find.singlesignon;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.sys.gateway.dom.singlesignon.CompanyCode;
import nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountSetMemento;
import nts.uk.ctx.sys.gateway.dom.singlesignon.UseAtr;
import nts.uk.ctx.sys.gateway.dom.singlesignon.UserName;

/**
 * Gets the use atr.
 *
 * @return the use atr
 */
@Getter
@Setter
public class OtherSysAccFinderDto implements OtherSysAccountSetMemento{
	
	/** The company id. */
	private String companyId;
	
	/** The employee id. */
	// ユーザID
	private String employeeId;

	/** The company code. */
	// 会社コード
	private String companyCode;

	// ユーザ名
	/** The user name. */
	private String userName;

	// 利用区分
	/** The use atr. */
	private Integer useAtr;
	
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountSetMemento#setUserId(java.lang.String)
	 */
	@Override
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
		
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountSetMemento#setCompanyCode(java.lang.String)
	 */
	@Override
	public void setCompanyCode(CompanyCode companyCode) {
		this.companyCode = companyCode.v();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountSetMemento#setUserName(java.lang.String)
	 */
	@Override
	public void setUserName(UserName userName) {
		this.userName = userName.v();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountSetMemento#setUseAtr(nts.uk.ctx.sys.gateway.dom.singlesignon.UseAtr)
	 */
	@Override
	public void setUseAtr(UseAtr useAtr) {
		this.useAtr = useAtr.value;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountSetMemento#setCompanyId(java.lang.String)
	 */
	@Override
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

}
