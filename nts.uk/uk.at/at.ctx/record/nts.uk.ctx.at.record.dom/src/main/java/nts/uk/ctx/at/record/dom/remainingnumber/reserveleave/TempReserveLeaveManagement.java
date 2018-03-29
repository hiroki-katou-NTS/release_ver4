package nts.uk.ctx.at.record.dom.remainingnumber.reserveleave;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.remainingnumber.base.ManagementDays;
import nts.uk.ctx.at.record.dom.remainingnumber.base.ScheduleRecordAtr;

/**
 * 暫定積立年休管理データ
 * @author shuichu_ishida
 */
@Getter
public class TempReserveLeaveManagement extends AggregateRoot {

	/** 社員ID */
	private String employeeId;
	/** 年月日 */
	private GeneralDate ymd;
	/** 積立年休使用日数 */
	private ManagementDays reserveLeaveUseDays;
	/** 予定実績区分 */
	private ScheduleRecordAtr scheduleRecordAtr;
	
	/**
	 * コンストラクタ
	 * @param employeeId 社員ID
	 * @param ymd 年月日
	 */
	public TempReserveLeaveManagement(String employeeId, GeneralDate ymd){
		
		super();
		this.employeeId = employeeId;
		this.ymd = ymd;
	}
	
	/**
	 * ファクトリー
	 * @param employeeId 社員ID
	 * @param ymd 年月日
	 * @param reserveLeaveUseDays 積立年休使用日数
	 * @param scheduleRecordAtr 予定実績区分
	 * @return 暫定積立年休管理データ
	 */
	public static TempReserveLeaveManagement of(
			String employeeId,
			GeneralDate ymd,
			ManagementDays reserveLeaveUseDays,
			ScheduleRecordAtr scheduleRecordAtr){
		
		TempReserveLeaveManagement domain = new TempReserveLeaveManagement(employeeId, ymd);
		domain.reserveLeaveUseDays = reserveLeaveUseDays;
		domain.scheduleRecordAtr = scheduleRecordAtr;
		return domain;
	}
}
