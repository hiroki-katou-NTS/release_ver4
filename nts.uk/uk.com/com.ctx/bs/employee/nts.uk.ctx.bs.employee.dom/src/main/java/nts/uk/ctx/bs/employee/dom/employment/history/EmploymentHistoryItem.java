package nts.uk.ctx.bs.employee.dom.employment.history;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.bs.employee.dom.employment.EmploymentCode;
import nts.uk.ctx.bs.person.dom.person.common.ConstantUtils;


/**
 * The Class EmploymentHistoryItem.
 */
// 雇用履歴項目
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmploymentHistoryItem extends AggregateRoot{

	/** The history Id. */
	// 履歴ID
	private String historyId;
	
	/** The Employee Id. */
	// 社員ID
	private String employeeId;
	
	/** The SalarySegment */
	// 給与区分
	private SalarySegment salarySegment;
	
	/** The employment code. */
	// 雇用コード. 
	private EmploymentCode employmentCode;
	
	public static EmploymentHistoryItem createFromJavaType(String histId, String sid, String employmentCD, int salary){
		return new EmploymentHistoryItem(histId,sid, (salary != ConstantUtils.ENUM_UNDEFINE_VALUE && salary != 0)  ? EnumAdaptor.valueOf(salary, SalarySegment.class): null,new EmploymentCode(employmentCD));
	}
	
}
