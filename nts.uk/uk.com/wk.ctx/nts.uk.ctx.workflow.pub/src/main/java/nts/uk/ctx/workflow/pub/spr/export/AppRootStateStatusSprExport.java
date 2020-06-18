package nts.uk.ctx.workflow.pub.spr.export;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.time.GeneralDate;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
public class AppRootStateStatusSprExport {
	/**
	 * 年月日
	 */
	private GeneralDate date;
	/**
	 * 承認対象者
	 */
	private String employeeID;
	/**
	 * 承認状況 0:"未承認", 1:"承認中", 2:"承認済"
	 */
	private Integer dailyConfirmAtr;
	
	public boolean isUnapproved() {
		return dailyConfirmAtr == 0;
	}
	
	public boolean isApproving() {
		return dailyConfirmAtr == 1;
	}
	
	public boolean isApproved() {
		return dailyConfirmAtr == 2;
	}
}
