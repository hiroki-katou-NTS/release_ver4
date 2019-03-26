package nts.uk.ctx.at.function.dom.adapter.employeebasic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeInfoImport {
	/** 社員ID */
	private String sid;

	/** 社員コード.Employee code */
	private String scd;

	/** ビジネスネーム.Business name */
	private String bussinessName;
}
