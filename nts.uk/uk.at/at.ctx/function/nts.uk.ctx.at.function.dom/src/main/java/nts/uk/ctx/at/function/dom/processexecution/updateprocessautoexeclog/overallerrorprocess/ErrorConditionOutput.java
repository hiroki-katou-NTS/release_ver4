package nts.uk.ctx.at.function.dom.processexecution.updateprocessautoexeclog.overallerrorprocess;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * エラー状態
 * 
 * @author tutk
 *
 */

@Getter
@Setter
@NoArgsConstructor
public class ErrorConditionOutput {

	/* 業務エラー状態 */
	private Boolean businessErrorStatus;

	/* システムエラー状態 */
	private Boolean systemErrorCondition;

	public ErrorConditionOutput(Boolean businessErrorStatus, Boolean systemErrorCondition) {
		super();
		this.businessErrorStatus = businessErrorStatus;
		this.systemErrorCondition = systemErrorCondition;
	}
}
