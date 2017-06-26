package nts.uk.ctx.basic.app.find.organization.workplace;

import lombok.Data;
import nts.uk.ctx.basic.app.find.organization.department.DepartmentMemoDto;
import nts.uk.ctx.basic.dom.organization.department.DepartmentMemo;
import nts.uk.ctx.basic.dom.organization.workplace.WorkPlaceMemo;

@Data
public class WorkPlaceMemoDto {

	private String companyCode;

	private String historyId;

	private String memo;

	public WorkPlaceMemoDto(String historyId, String memo) {
		this.historyId = historyId;
		this.memo = memo;
	}
	public static WorkPlaceMemoDto fromDomain(WorkPlaceMemo workPlaceMemoDto) {
		return new WorkPlaceMemoDto(workPlaceMemoDto.getHistoryId(), workPlaceMemoDto.getMemo().v());
	}

}