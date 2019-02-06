/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.dom.singlesignon;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;

// 他システムアカウント
@Getter
public class OtherSysAccount extends AggregateRoot {

	
	/** The company id. */
	private String companyId;
	
	/** The employee id. */
	// ユーザID
	private String employeeId;

	/** The company code. */
	// アカウント情報
	private OtherSystemAccountInfo accountInfo;

	/**
	 * Instantiates a new other sys account.
	 *
	 * @param memento the memento
	 */
	public OtherSysAccount(OtherSysAccountGetMemento memento) {
		this.companyId = memento.getCompanyId();
		this.employeeId = memento.getEmployeeId();
		this.accountInfo = new OtherSystemAccountInfo(memento.getUseAtr(), memento.getCompanyCode(),
				memento.getUserName());
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(OtherSysAccountSetMemento memento) {
		memento.setCompanyId(this.companyId);
		memento.setEmployeeId(this.employeeId);
		memento.setCompanyCode(this.accountInfo.getCompanyCode());
		memento.setUserName(this.accountInfo.getUserName());
		memento.setUseAtr(this.accountInfo.getUseAtr());
	}

}
