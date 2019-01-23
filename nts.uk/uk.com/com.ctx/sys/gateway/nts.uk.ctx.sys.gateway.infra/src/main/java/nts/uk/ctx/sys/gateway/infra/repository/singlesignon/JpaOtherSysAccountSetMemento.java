/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.infra.repository.singlesignon;

import nts.uk.ctx.sys.gateway.dom.singlesignon.CompanyCode;
import nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountSetMemento;
import nts.uk.ctx.sys.gateway.dom.singlesignon.UseAtr;
import nts.uk.ctx.sys.gateway.dom.singlesignon.UserName;
import nts.uk.ctx.sys.gateway.infra.entity.singlesignon.SgwmtOtherSysAcc;
import nts.uk.ctx.sys.gateway.infra.entity.singlesignon.SgwmtOtherSysAccPK;

/**
 * The Class JpaOtherSysAccountSetMemento.
 */
public class JpaOtherSysAccountSetMemento implements OtherSysAccountSetMemento {

	/** The typed value. */
	private SgwmtOtherSysAcc typedValue;

	/**
	 * Instantiates a new jpa other sys account set memento.
	 *
	 * @param typedValue the typed value
	 */
	public JpaOtherSysAccountSetMemento(SgwmtOtherSysAcc typedValue) {
		this.typedValue = typedValue;
		if (this.typedValue.getSgwmtOtherSysAccPK() == null) {
			this.typedValue.setSgwmtOtherSysAccPK(new SgwmtOtherSysAccPK());
		}
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountSetMemento#setUserId(java.lang.String)
	 */
	@Override
	public void setEmployeeId(String employeeId) {
		this.typedValue.getSgwmtOtherSysAccPK().setEmployeeId(employeeId);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountSetMemento#setCompanyCode(java.lang.String)
	 */
	@Override
	public void setCompanyCode(CompanyCode companyCode) {
		this.typedValue.setCcd(companyCode.v());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountSetMemento#setUserName(java.lang.String)
	 */
	@Override
	public void setUserName(UserName userName) {
		this.typedValue.setUserName(userName.v());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountSetMemento#setUseAtr(nts.uk.ctx.sys.gateway.dom.singlesignon.UseAtr)
	 */
	@Override
	public void setUseAtr(UseAtr useAtr) {
		this.typedValue.setUseAtr(useAtr.value);
	}

	@Override
	public void setCompanyId(String companyId) {
		this.typedValue.getSgwmtOtherSysAccPK().setCid(companyId);
	}
}
