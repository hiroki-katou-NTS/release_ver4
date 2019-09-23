package nts.uk.ctx.at.function.dom.adapter.workrecord.actualsituation.createperapprovaldaily;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 *
 * @author tutk
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class AppDataInfoDailyImport  {
	
	/**社員ID*/
	private String employeeId;
	
	/**実行ID*/
	private String	executionId;
	
	/**エラーメッセージ*/
	private String errorMessage;

	public AppDataInfoDailyImport(String employeeId, String executionId, String errorMessage) {
		super();
		this.employeeId = employeeId;
		this.executionId = executionId;
		this.errorMessage = errorMessage;
	}

}
