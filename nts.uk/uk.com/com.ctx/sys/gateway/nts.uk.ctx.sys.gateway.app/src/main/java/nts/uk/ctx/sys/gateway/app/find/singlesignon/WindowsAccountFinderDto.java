/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.find.singlesignon;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class WindowsAccountFinderDto.
 */
@Getter
@Setter
public class WindowsAccountFinderDto {

	// ユーザID
	/** The user id. */
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

	/** The use atr. */
	// 利用区分
	private Integer useAtr;

	/**
	 * Instantiates a new windows account finder dto.
	 */
	public WindowsAccountFinderDto() {
		super();
	}

	
	/**
	 * Instantiates a new windows account finder dto.
	 *
	 * @param employeeId the employee id
	 * @param hostName the host name
	 * @param userName the user name
	 * @param no the no
	 * @param useAtr the use atr
	 */
	public WindowsAccountFinderDto(String employeeId, String hostName, String userName, Integer no,
			Integer useAtr) {
		super();
		this.employeeId = employeeId;
		this.hostName = hostName;
		this.userName = userName;
		this.no = no;
		this.useAtr = useAtr;
	}

}
