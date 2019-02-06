package nts.uk.ctx.workflow.app.find.approvermanagement.workroot;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.arc.time.GeneralDate;

/**
 * @author sang.nv
 * 上司の設定
 */
@Data
@AllArgsConstructor
public class ManagerSettingDto {
	/** 開始日 */
	private GeneralDate startDate;
	/** 終了日 */
	private GeneralDate endDate;
	/** Screen Mode */
	private boolean isNewMode;
	/** 所属長のコード */
	private String departmentCode;
	/** 所属長のID*/
	private String departmentApproverId;
	/** 所属長の名前 */
	private String departmentName;
	/** 日別の承認する人のコード */
	private String dailyApprovalCode;
	/** 日別の承認する人のID */
	private String dailyApproverId;
	/** 日別の承認する人の名前 */
	private String dailyApprovalName;
	/** hasAuthority */
	private boolean hasAuthority;
	/** 締めの開始日 */
	private GeneralDate closingStartDate;
	
	private boolean displayDailyApprover;
}
