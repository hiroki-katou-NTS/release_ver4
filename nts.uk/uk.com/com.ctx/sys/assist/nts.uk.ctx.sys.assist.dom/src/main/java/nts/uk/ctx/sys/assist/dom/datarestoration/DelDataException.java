package nts.uk.ctx.sys.assist.dom.datarestoration;

import nts.arc.time.GeneralDate;

public class DelDataException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	String dataRecoveryProcessId;
	String target ;
	String errorContent ;
	GeneralDate targetDate ;
	String contentSql ;
	String processingContent;
	
	public DelDataException() {
	}
	
	public DelDataException(Throwable ex) {
		super(ex);
	}

	public DelDataException(String dataRecoveryProcessId,String target,String errorContent,GeneralDate targetDate,
			String contentSql,String processingContent) {
		super(dataRecoveryProcessId);
		this.dataRecoveryProcessId = dataRecoveryProcessId;
		this.target = target;
		this.errorContent = errorContent;
		this.targetDate = targetDate;
		this.contentSql = contentSql;
		this.processingContent = processingContent;
	}
}
