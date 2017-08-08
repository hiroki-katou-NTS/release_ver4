/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.dom.login;

import lombok.Getter;

/**
 * Gets the contract period.
 *
 * @return the contract period
 */
@Getter
public class Contract {

	//パスワード
	/** The password. */
	private RawPassword password;

	//契約コード
	/** The contract code. */
	private ContractCode contractCode;

	//契約期間
	/** The contract period. */
	private Period contractPeriod;

	/**
	 * @param password
	 * @param contractCode
	 * @param contractPeriod
	 */
	public Contract(ContractGetMemento memento) {
		this.password = memento.getPassword();
		this.contractCode = memento.getContractCode();
		this.contractPeriod = memento.getContractPeriod();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(ContractSetMemento memento) {
		memento.setPassword(this.password);
		memento.setContractCode(this.contractCode);
		memento.setContractPeriod(this.contractPeriod);
	}

}
