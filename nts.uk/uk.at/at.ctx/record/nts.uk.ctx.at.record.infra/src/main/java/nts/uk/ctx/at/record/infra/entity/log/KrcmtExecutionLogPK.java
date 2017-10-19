package nts.uk.ctx.at.record.infra.entity.log;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KrcmtExecutionLogPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "EXECUTED_LOG_ID")
	public String executedLogId;

	@Column(name = "EMP_EXECUTION_LOG_ID")
	public String employmentExecutionLogId;

}
