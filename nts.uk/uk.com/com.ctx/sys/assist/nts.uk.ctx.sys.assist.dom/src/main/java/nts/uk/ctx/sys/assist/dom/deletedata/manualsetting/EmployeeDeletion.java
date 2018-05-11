package nts.uk.ctx.sys.assist.dom.deletedata.manualsetting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
/**
 * データ削除の対象社員
 */
public class EmployeeDeletion {
	
	// データ削除処理ID
	/** The deletion Id */
	private String delId;

	// 社員ID
	/** The employee Id. */
	private String employeeId;

	// ビジネスネーム
	/** The business name. */
	private BusinessName businessName;
	
	public static EmployeeDeletion createFromJavatype(String delId, 
			String employeeId, String businessName) {
		return new EmployeeDeletion(delId, employeeId, new BusinessName(businessName));
	}
}
