package nts.uk.ctx.at.record.app.find.log.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrMessageInfoDto {
	
	/** 実行社員ID */
	private String employeeID;
	
	/** 就業計算と集計実行ログID */
	private String empCalAndSumExecLogID;
	
	/** リソースID */
	private String resourceID;
	
	/** 実行内容 */
	private int executionContent;
	
	/** 処理日 */
	private GeneralDate disposalDay;
	
	/** エラーメッセージ */
	private String messageError;
	
	public static ErrMessageInfoDto fromDomain(ErrMessageInfo domain) {
		return new  ErrMessageInfoDto(
			domain.getEmployeeID(),
			domain.getEmpCalAndSumExecLogID(),
			domain.getResourceID().v(),
			domain.getExecutionContent().value,
			domain.getDisposalDay(),
			domain.getMessageError().v()
		);
	}
}
