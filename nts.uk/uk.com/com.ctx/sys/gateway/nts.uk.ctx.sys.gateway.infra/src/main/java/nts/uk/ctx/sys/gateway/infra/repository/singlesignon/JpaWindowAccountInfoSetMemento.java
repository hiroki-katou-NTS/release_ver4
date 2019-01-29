/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.infra.repository.singlesignon;

import nts.uk.ctx.sys.gateway.dom.singlesignon.HostName;
import nts.uk.ctx.sys.gateway.dom.singlesignon.UseAtr;
import nts.uk.ctx.sys.gateway.dom.singlesignon.UserName;
import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccountInfoSetMemento;
import nts.uk.ctx.sys.gateway.infra.entity.singlesignon.SgwmtWindowAcc;
import nts.uk.ctx.sys.gateway.infra.entity.singlesignon.SgwmtWindowAccPK;

/**
 * The Class JpaWindowAccountSetMemento.
 */
public class JpaWindowAccountInfoSetMemento implements WindowsAccountInfoSetMemento {
	
	/** The typed value. */
	private SgwmtWindowAcc typedValue;
	
	/**
	 * Instantiates a new jpa window account set memento.
	 *
	 * @param typedValue the typed value
	 */
	public JpaWindowAccountInfoSetMemento(String companyId, String employeeId, SgwmtWindowAcc typedValue) {
		this.typedValue = typedValue;
		if (this.typedValue.getSgwmtWindowAccPK() == null) {
			this.typedValue.setSgwmtWindowAccPK(new SgwmtWindowAccPK());
		}
		this.typedValue.getSgwmtWindowAccPK().setEmployeeId(employeeId);
		this.typedValue.getSgwmtWindowAccPK().setCid(companyId);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.WindowAccountSetMemento#setHotName(java.lang.String)
	 */
	@Override
	public void setHostName(HostName hostName) {
		this.typedValue.setHostName(hostName.v());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.WindowAccountSetMemento#setUserName(java.lang.String)
	 */
	@Override
	public void setUserName(UserName userName) {
		this.typedValue.setUserName(userName.v());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.WindowAccountSetMemento#setUseAtr(nts.uk.ctx.sys.gateway.dom.singlesignon.UseAtr)
	 */
	@Override
	public void setUseAtr(UseAtr useAtr) {
			this.typedValue.setUseAtr(useAtr.value);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.WindowAccountSetMemento#setNo(java.lang.Integer)
	 */
	@Override
	public void setNo(Integer no) {
		this.typedValue.getSgwmtWindowAccPK().setNo(no);
	}

}
