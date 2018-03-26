package nts.uk.ctx.sys.gateway.dom.securitypolicy.loginlog;

import lombok.Getter;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.sys.gateway.dom.login.ContractCode;

/**
 * Gets the operation.
 *
 * @return the operation
 */
@Getter
public class LoginLog {
	
	/** The user id. */
	private String userId;
	
	/** The contract code. */
	private ContractCode contractCode;
	
	/** The program id. */
	private String programId;
	
	/** The process date time. */
	private GeneralDateTime processDateTime;
	
	/** The success or fail. */
	private SuccessFailureClassification successOrFail;
	
	/** The operation. */
	private OperationSection operation;
	
	/**
	 * Instantiates a new login log.
	 *
	 * @param memento the memento
	 */
	public LoginLog(LoginLogGetMemento memento) {
		this.userId = memento.getUserId();
		this.contractCode = memento.getContractCode();
		this.programId = memento.getProgramId();
		this.processDateTime = memento.getProcessDateTime();
		this.successOrFail = memento.getSuccessOrFail();
		this.operation = memento.getOperation();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(LoginLogSetMemento memento) {
		memento.setUserId(this.userId);
		memento.setContractCode(this.contractCode);
		memento.setProcessDateTime(this.processDateTime);
		memento.setProgramId(this.programId);
		memento.setSuccessOrFail(this.successOrFail);
		memento.setOperation(this.operation);
	}

}
