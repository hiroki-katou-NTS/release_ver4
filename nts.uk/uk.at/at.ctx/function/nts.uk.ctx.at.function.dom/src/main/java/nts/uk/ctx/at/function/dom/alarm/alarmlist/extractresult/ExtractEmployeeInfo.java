package nts.uk.ctx.at.function.dom.alarm.alarmlist.extractresult;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.time.GeneralDate;

@Getter
@AllArgsConstructor
public class ExtractEmployeeInfo {

	/** 実行ID　*/
	private String executeId;
	
	/** 従業員ID */
	private String employeeId;
	
	/** 職場ID */
	private String workplaceId;	
	
	/** 従業員コード */
	private String employeeCode;		
	
	/** 従業員名 */
	private String employeeName;		
	
	/** 職場名 */
	private String workplaceName;
	
	/** 階層コード */
	private String hierarchyCode;	
	
	/** 職場開始日 */
	private GeneralDate wpWorkStartDate;	
	
	/** 職場終了日 */
	private GeneralDate wpWorkEndDate;
	
	public List<Object> toList(){
		return Arrays.asList(employeeCode, employeeId, employeeName, 
								workplaceName, wpWorkStartDate, wpWorkEndDate);
	}
}
