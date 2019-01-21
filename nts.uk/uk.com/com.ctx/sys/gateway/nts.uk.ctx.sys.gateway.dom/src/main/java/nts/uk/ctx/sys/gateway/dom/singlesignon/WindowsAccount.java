/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.dom.singlesignon;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.arc.error.BundledBusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.gul.text.StringUtil;

/**
 * The Class WindowAccount.
 */
// Windowsアカウント
@Getter
public class WindowsAccount extends AggregateRoot{

	//会社ID
	/** The company id. */
	private String companyId;

	// 社員ID
	/** The employee id. */
	private String employeeId;

	// アカウント情報
	/** The hot name. */
	private List<WindowsAccountInfo> accountInfos;

	/**
	 * Instantiates a new window account.
	 *
	 * @param memento
	 *            the memento
	 */
	public WindowsAccount(WindowsAccountGetMemento memento) {
		this.companyId = memento.getCompanyId();
		this.employeeId = memento.getEmployeeId();
		this.accountInfos = memento.getAccountInfos();
	}

	@Override
	public void validate() {
		super.validate();
		final Integer MAX_FREQUENCY = 1;

		// check duplicate account host name & user name
		final List<WindowsAccountInfo> usedAccounts = this.accountInfos.stream().filter(WindowsAccountInfo::isSetting)
				.collect(Collectors.toList());
		usedAccounts.forEach(acc -> {
			boolean isNameNotNull = !StringUtil.isNullOrEmpty(acc.getHostName().v(), true)
					&& !StringUtil.isNullOrEmpty(acc.getUserName().v(), true);

			boolean isDuplicated = Collections.frequency(usedAccounts, acc) > MAX_FREQUENCY;

			if (isNameNotNull && isDuplicated) {
				BundledBusinessException exceptions = BundledBusinessException.newInstance();
				exceptions.addMessage("Msg_616");
				exceptions.throwExceptions();
			}
		});
	}
	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(WindowsAccountSetMemento memento) {
		memento.setCompanyId(this.companyId);
		memento.setEmployeeId(this.employeeId);
		memento.setAccountInfos(this.accountInfos);
	}
	
}
