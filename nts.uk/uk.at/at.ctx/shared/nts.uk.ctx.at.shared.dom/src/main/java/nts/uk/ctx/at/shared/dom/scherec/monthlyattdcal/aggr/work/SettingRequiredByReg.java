package nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrame;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.calcmethod.calcmethod.other.RegularWorkTimeAggrSet;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.calcmethod.legaltransferorder.LegalTransferOrderSetOfAggrMonthly;
import nts.uk.ctx.at.shared.dom.workdayoff.frame.WorkdayoffFrame;

/**
 * 通常勤務が必要とする設定
 * @author shuichi_ishida
 */
@Getter
public class SettingRequiredByReg {

	/** 通常勤務の法定内集計設定 */
	@Setter
	private RegularWorkTimeAggrSet regularAggrSet;
	/** 月次集計の法定内振替順設定 */
	@Setter
	private LegalTransferOrderSetOfAggrMonthly legalTransferOrderSet;
	/** 残業枠の役割 */
	private Map<Integer, OvertimeWorkFrame> roleOverTimeFrameMap;
	/** 休出枠の役割 */
	private Map<Integer, WorkdayoffFrame> roleHolidayWorkFrameMap;
	/** 自動的に除く残業枠 */
	private List<OvertimeWorkFrame> autoExceptOverTimeFrames;
	/** 自動的に除く休出枠 */
	private List<Integer> autoExceptHolidayWorkFrames;
	/** 休暇加算時間設定 */
	private Map<String, AggregateRoot> holidayAdditionMap;
	/** 週間法定労働時間 */
	@Setter
	private AttendanceTimeMonth statutoryWorkingTimeWeek;
	/** 月間法定労働時間 */
	@Setter
	private AttendanceTimeMonth statutoryWorkingTimeMonth;

	/**
	 * コンストラクタ
	 * @param companyId 会社ID
	 */
	public SettingRequiredByReg(String companyId){
		
		this.regularAggrSet = null;
		this.legalTransferOrderSet = new LegalTransferOrderSetOfAggrMonthly(companyId);
		this.roleOverTimeFrameMap = new HashMap<>();
		this.roleHolidayWorkFrameMap = new HashMap<>();
		this.autoExceptOverTimeFrames = new ArrayList<>();
		this.autoExceptHolidayWorkFrames = new ArrayList<>();
		this.holidayAdditionMap = new HashMap<>();
		this.statutoryWorkingTimeMonth = new AttendanceTimeMonth(0);
		this.statutoryWorkingTimeWeek = new AttendanceTimeMonth(0);
	}
}
