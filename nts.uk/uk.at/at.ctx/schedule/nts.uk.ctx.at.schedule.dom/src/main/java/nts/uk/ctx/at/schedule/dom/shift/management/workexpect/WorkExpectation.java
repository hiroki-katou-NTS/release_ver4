package nts.uk.ctx.at.schedule.dom.shift.management.workexpect;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMaster;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMasterCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;

/**
 * 勤務希望
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.勤務予定.シフト管理.シフト勤務.勤務希望
 * @author dan_pv
 *
 */
public interface WorkExpectation {
	
	/**
	 * 指定方法を取得する	
	 * @return
	 */
	public AssignmentMethod getAssignmentMethod();
	
	/**
	 * 休日の勤務希望である
	 */
	public default boolean isHolidayExpectation(){ 
		return this.getAssignmentMethod() == AssignmentMethod.HOLIDAY;
    } 
	
	/**
	 * 希望に沿っているか
	 * @param require
	 * @param workInformation
	 * @param timeZoneList
	 * @return
	 */
	public boolean isMatchingExpectation(Require require, 
			WorkInformation workInformation,
			List<TimeSpanForCalc> timeZoneList);
	
	/**
	 * 表示情報を返す
	 * @param require
	 * @return
	 */
	public WorkExpectDisplayInfo getDisplayInformation(Require require);
	
	
	public static interface Require extends WorkInformation.Require{
		
		// 勤務情報からシフトマスタを取得する
		Optional<ShiftMaster> getShiftMasterByWorkInformation(WorkTypeCode workTypeCode, WorkTimeCode workTimeCode);

		// シフトマスタを取得する
		List<ShiftMaster> getShiftMaster(List<ShiftMasterCode> shiftMasterCodeList);
	}

}
