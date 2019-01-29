/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.command.singlesignon;

import lombok.Setter;
import nts.uk.ctx.sys.gateway.dom.singlesignon.HostName;
import nts.uk.ctx.sys.gateway.dom.singlesignon.UseAtr;
import nts.uk.ctx.sys.gateway.dom.singlesignon.UserName;
import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccountInfoGetMemento;

/**
 * The Class WindowAccountDto.
 */
@Setter
public class WindowAccountDto implements WindowsAccountInfoGetMemento {

	// 社員ID
	/** The employee id. */
	private String employeeId;

	// ホスト名
	/** The host name. */
	private String hostName;

	// ユーザ名
	/** The user name. */
	private String userName;

	// NO
	/** The no. */
	private Integer no;

	// 利用区分
	/** The use atr. */
	private Integer useAtr;

	/** The is change. */
	private Boolean isChange;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.WindowAccountGetMemento#
	 * getHostName()
	 */
	@Override
	public HostName getHostName() {
		return new HostName(this.hostName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.WindowAccountGetMemento#
	 * getUserName()
	 */
	@Override
	public UserName getUserName() {
		return new UserName(this.userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.gateway.dom.singlesignon.WindowAccountGetMemento#getNo()
	 */
	@Override
	public Integer getNo() {
		return this.no;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.gateway.dom.singlesignon.WindowAccountGetMemento#getUseAtr
	 * ()
	 */
	@Override
	public UseAtr getUseAtr() {
		return UseAtr.valueOf(this.useAtr);
	}
	
	/**
	 * Gets the checks if is change.
	 *
	 * @return the checks if is change
	 */
	public Boolean getIsChange() {
		return this.isChange;
	}
	
	/**
	 * Gets the employee id.
	 *
	 * @return the employee id
	 */
	public String getEmployeeId() {
		return this.employeeId;
	}
}
