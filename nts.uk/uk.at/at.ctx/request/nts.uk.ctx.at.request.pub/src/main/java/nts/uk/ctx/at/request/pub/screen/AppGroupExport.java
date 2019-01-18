package nts.uk.ctx.at.request.pub.screen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.time.GeneralDate;
@AllArgsConstructor
@Getter
public class AppGroupExport {
	/**
	 * appDate,申請日
	 */
	private GeneralDate appDate;
	/**
	 * appType,申請種類
	 */
	private Integer appType;
	/**
	 * employeeID,申請者
	 */
	private String employeeID;
	/**
	 * appTypeName,申請表示名※
	 */
	private String appTypeName;
}
