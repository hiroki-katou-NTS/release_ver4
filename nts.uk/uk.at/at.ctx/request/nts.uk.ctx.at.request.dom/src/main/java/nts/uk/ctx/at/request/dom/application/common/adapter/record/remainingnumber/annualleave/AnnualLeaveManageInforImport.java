package nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.annualleave;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.arc.time.GeneralDate;

/**
 * @author sonnlb
 *
 */
@Value
@AllArgsConstructor
public class AnnualLeaveManageInforImport {
	/**
	 * 年月日
	 */
	private GeneralDate ymd;

	/**
	 * 使用日数
	 */
	private Double daysUsedNo;

	/**
	 * 使用時間
	 */
	private Integer usedMinutes;

	/**
	 * 予定実績区分
	 */
	private int scheduleRecordAtr;
	
	/**
	 * 勤務種類
	 */
	private String workTypeCode;
	
	private String workTypeName;
}
