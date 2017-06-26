package nts.uk.ctx.basic.app.find.organization.department;

import lombok.Data;
import nts.uk.ctx.basic.dom.organization.department.DepartmentMemo;

@Data
public class DepartmentMemoDto {

	private String companyCode;

	private String historyId;

	private String memo;

	public DepartmentMemoDto(String historyId, String memo) {
		this.historyId = historyId;
		this.memo = memo;
	}

	public static DepartmentMemoDto fromDomain(DepartmentMemo departmentmemo) {
		return new DepartmentMemoDto(departmentmemo.getHistoryId(), departmentmemo.getMemo().v());
	}

}
