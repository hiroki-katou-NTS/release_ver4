/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.env.dom.mailnoticeset.dto;

import lombok.Getter;

/**
 * The Class EmployeeInfoContactImport.
 */
//社員連絡先
@Getter
public class EmployeeInfoContactImport {

	// 社員ID
	/** The employee id. */
	private String employeeId;

	// メールアドレス
	/** The mail address. */
	private String mailAddress;

	// 携帯メールアドレス
	/** The mobile mail address. */
	private String mobileMailAddress;

	// 携帯電話番号
	/** The cell phone no. */
	private String cellPhoneNo;

	/**
	 * Instantiates a new employee info contact import.
	 *
	 * @param employeeId
	 *            the employee id
	 * @param mailAddress
	 *            the mail address
	 * @param mobileMailAddress
	 *            the mobile mail address
	 * @param cellPhoneNo
	 *            the cell phone no
	 */
	public EmployeeInfoContactImport(String employeeId, String mailAddress, String mobileMailAddress,
			String cellPhoneNo) {
		this.employeeId = employeeId;
		this.mailAddress = mailAddress;
		this.mobileMailAddress = mobileMailAddress;
		this.cellPhoneNo = cellPhoneNo;
	}

}
