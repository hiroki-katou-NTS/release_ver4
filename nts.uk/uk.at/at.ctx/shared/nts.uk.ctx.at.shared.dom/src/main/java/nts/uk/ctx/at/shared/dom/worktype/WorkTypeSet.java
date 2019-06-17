package nts.uk.ctx.at.shared.dom.worktype;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
/**
 * 勤務種類設定
 * 
 * @author sonnh
 *
 */
@Getter
@NoArgsConstructor
public class WorkTypeSet implements Cloneable{
    
	/**
	 * 会社ID
	 */
	private String companyId;
	
	/**
	 * 勤務種類コード
	 */
	private WorkTypeCode workTypeCd;
	
	/**
	 * 勤務の単位
	 */
	private WorkAtr workAtr;
	
	/**
	 * 公休を消化する
	 */
	private WorkTypeSetCheck digestPublicHd;
	
	/**
	 * 休日区分
	 */
	private HolidayAtr holidayAtr;
	
	/**
	 * 休日日数をカウントする
	 */
	private WorkTypeSetCheck countHodiday;
	
	/**
	 * 休業区分
	 */
	private CloseAtr closeAtr;
	
	/**
	 * 欠勤枠
	 */
	private int sumAbsenseNo;
	
	/**
	 * 特別休暇枠
	 */
	private int sumSpHodidayNo;
	
	/**
	 * 退勤時刻を直帰とする
	 */
	private WorkTypeSetCheck timeLeaveWork;
	
	/**
	 * 出勤時刻を直行とする
	 */
	private WorkTypeSetCheck attendanceTime;
	
	/**
	 * 代休を発生させる
	 */
	private WorkTypeSetCheck genSubHodiday;
	
	/**
	 * 日勤・夜勤時間を求める
	 */
	private WorkTypeSetCheck dayNightTimeAsk;

	/**
	 * 
	 * @param companyId
	 * @param workTypeCd
	 * @param workAtr
	 * @param digestPublicHd
	 * @param holidayAtr
	 * @param countHodiday
	 * @param closeAtr
	 * @param sumAbsenseNo
	 * @param sumSpHodidayNo
	 * @param timeLeaveWork
	 * @param attendanceTime
	 * @param genSubHodiday
	 * @param dayNightTimeAsk
	 */
	public WorkTypeSet(String companyId, WorkTypeCode workTypeCd, WorkAtr workAtr, WorkTypeSetCheck digestPublicHd,
			HolidayAtr holidayAtr, WorkTypeSetCheck countHodiday, CloseAtr closeAtr, int sumAbsenseNo,
			int sumSpHodidayNo, WorkTypeSetCheck timeLeaveWork, WorkTypeSetCheck attendanceTime,
			WorkTypeSetCheck genSubHodiday, WorkTypeSetCheck dayNightTimeAsk) {
		super();
		this.companyId = companyId;
		this.workTypeCd = workTypeCd;
		this.workAtr = workAtr;
		this.digestPublicHd = digestPublicHd;
		this.holidayAtr = holidayAtr;
		this.countHodiday = countHodiday;
		this.closeAtr = closeAtr;
		this.sumAbsenseNo = sumAbsenseNo;
		this.sumSpHodidayNo = sumSpHodidayNo;
		this.timeLeaveWork = timeLeaveWork;
		this.attendanceTime = attendanceTime;
		this.genSubHodiday = genSubHodiday;
		this.dayNightTimeAsk = dayNightTimeAsk;
	}

	// 打刻の扱い方
	public WorkTypeSet(WorkTypeSetCheck timeLeaveWork, WorkTypeSetCheck attendanceTime) {
		super();
		this.timeLeaveWork = timeLeaveWork;
		this.attendanceTime = attendanceTime;
	}

	/**
	 * 
	 * @param companyId
	 * @param workTypeCd
	 * @param workAtr
	 * @param digestPublicHd
	 * @param holidayAtr
	 * @param countHodiday
	 * @param closeAtr
	 * @param sumAbsenseNo
	 * @param sumSpHodidayNo
	 * @param timeLeaveWork
	 * @param attendanceTime
	 * @param genSubHodiday
	 * @param dayNightTimeAsk
	 * @return
	 */
	public static WorkTypeSet createSimpleFromJavaType(String companyId, String workTypeCd, int workAtr, int digestPublicHd,
			int holidayAtr, int countHodiday, Integer closeAtr, int sumAbsenseNo,
			int sumSpHodidayNo, int timeLeaveWork, int attendanceTime,
			int genSubHodiday, int dayNightTimeAsk) {
		return new WorkTypeSet(companyId, 
				new WorkTypeCode(workTypeCd), 
				EnumAdaptor.valueOf(workAtr, WorkAtr.class), 
				EnumAdaptor.valueOf(digestPublicHd, WorkTypeSetCheck.class), 
				EnumAdaptor.valueOf(holidayAtr, HolidayAtr.class), 
				EnumAdaptor.valueOf(countHodiday, WorkTypeSetCheck.class), 
				closeAtr !=null ? EnumAdaptor.valueOf(closeAtr, CloseAtr.class) : null, 
				sumAbsenseNo, 
				sumSpHodidayNo, 
				EnumAdaptor.valueOf(timeLeaveWork, WorkTypeSetCheck.class), 
				EnumAdaptor.valueOf(attendanceTime, WorkTypeSetCheck.class), 
				EnumAdaptor.valueOf(genSubHodiday, WorkTypeSetCheck.class), 
				EnumAdaptor.valueOf(dayNightTimeAsk, WorkTypeSetCheck.class));
	}
	
	/**
	 * change value of closeAtr(休業区分)
	 * @param closeAtr
	 */
	public void changeCloseAtr(CloseAtr closeAtr) {
		this.closeAtr = closeAtr;
	}
	
	@Override
	public WorkTypeSet clone() {
		WorkTypeSet cloned = new WorkTypeSet();
		try {
			cloned.companyId = this.companyId;
			cloned.workTypeCd = new WorkTypeCode(this.workTypeCd.v());
			cloned.workAtr = WorkAtr.valueOf(this.workAtr.value);
			cloned.digestPublicHd = WorkTypeSetCheck.valueOf(this.digestPublicHd.value);
			cloned.holidayAtr = HolidayAtr.valueOf(this.holidayAtr.value); 
			cloned.countHodiday = WorkTypeSetCheck.valueOf(this.countHodiday.value);
			if(this.closeAtr != null)
				cloned.closeAtr = CloseAtr.valueOf(this.closeAtr.value);
			cloned.sumAbsenseNo = this.sumAbsenseNo;
			cloned.sumSpHodidayNo = this.sumSpHodidayNo;
			cloned.timeLeaveWork = WorkTypeSetCheck.valueOf(this.timeLeaveWork.value);
			cloned.attendanceTime = WorkTypeSetCheck.valueOf(this.attendanceTime.value);
			cloned.genSubHodiday = WorkTypeSetCheck.valueOf(this.genSubHodiday.value);
			cloned.dayNightTimeAsk = WorkTypeSetCheck.valueOf(this.dayNightTimeAsk.value);
		}
		catch (Exception e){
			throw new RuntimeException("WorkTypeSet clone error.");
		}
		return cloned;
	}
}
