package nts.uk.ctx.at.record.app.find.log.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Value;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.TargetPerson;

@Value
public class TargetPersonDto {
	
	/** 社員ID */
	private String employeeId;
	
	/** 就業計算と集計実行ログID */
	private String empCalAndSumExecLogId;
	
	/** 状態 */
	private List<ComplStateOfExeContentsDto> state;
	
	public static TargetPersonDto fromDomain(TargetPerson domain) {
		return new TargetPersonDto(
			domain.getEmployeeId(),
			domain.getEmpCalAndSumExecLogId(),
			domain.getState().stream().map(c -> ComplStateOfExeContentsDto.fromDomain(c)).collect(Collectors.toList())
		);
	}
}