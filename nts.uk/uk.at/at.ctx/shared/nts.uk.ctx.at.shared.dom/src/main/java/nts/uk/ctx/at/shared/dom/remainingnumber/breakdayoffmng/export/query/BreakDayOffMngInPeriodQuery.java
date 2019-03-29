package nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.CompensatoryDayOffManaData;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.LeaveManagementData;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.processten.SubstitutionHolidayOutput;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface BreakDayOffMngInPeriodQuery {
	/**
	 * RequestList203: 期間内の休出代休残数を取得する
	 * @param inputParam
	 * @return
	 */
	BreakDayOffRemainMngOfInPeriod getBreakDayOffMngInPeriod(BreakDayOffRemainMngParam inputParam);
	/**
	 * 1.未相殺の代休(確定)を取得する
	 * @param sid
	 * @return
	 */
	List<BreakDayOffDetail> getConfirmDayOffDetail(String cid, String sid, GeneralDate startDate);
	/**
	 * 1-1.確定代休から未相殺の代休を取得する
	 * @param sid
	 * @return
	 */
	//List<CompensatoryDayOffManaData> lstConfirmDayOffData(String cid, String sid, GeneralDate startDate);
	/**
	 * 1-3.暫定休出と紐付けをしない確定代休を取得する
	 * @param dayoffConfirmData
	 * @return
	 */
	BreakDayOffDetail getConfirmDayOffData(CompensatoryDayOffManaData dayoffConfirmData, String sid);
	/**
	 * 2.未使用の休出(確定)を取得する
	 * @param sid
	 * @return
	 */
	List<BreakDayOffDetail> getConfirmBreakDetail(String sid, GeneralDate startDate);
	/**
	 * 2-1.確定休出から未使用の休出を取得する
	 * @param sid
	 * @return
	 */
	//List<LeaveManagementData> lstConfirmBreakData(String sid, GeneralDate startDate);
	/**
	 * 2-3.暫定代休と紐付けをしない確定休出を取得する
	 * @param breakConfirm
	 * @param sid
	 * @return
	 */
	BreakDayOffDetail getConfirmBreakData(LeaveManagementData breakConfirm, String sid, GeneralDate aggStartDate);
	/**
	 * 繰越数を計算する
	 * @param baseDate
	 * @param lstDetailData
	 * @return
	 */
	CarryForwardDayTimes calcCarryForwardDays(GeneralDate baseDate, List<BreakDayOffDetail> lstDetailData, String sid, boolean isMode);
	/**
	 * 6.残数と未消化数を集計する
	 * @param baseDate
	 * @param lstDetailData
	 * @return
	 */
	RemainUnDigestedDayTimes getRemainUnDigestedDayTimes(GeneralDate baseDate, List<BreakDayOffDetail> lstDetailData, String sid, boolean isMode);
	/**
	 * 3.未相殺の代休(暫定)を取得する
	 * @param inputParam
	 * @return
	 */
	List<BreakDayOffDetail> lstInterimDayOffDetail(BreakDayOffRemainMngParam inputParam);
	/**
	 * 3-1.休出と紐付けをしない代休を取得する
	 * @param detailData
	 * @param remainData
	 * @return
	 */
	BreakDayOffDetail getNotTypeBreak(InterimDayOffMng detailData, InterimRemain interimData);
	/**
	 * 4.未使用の休出(暫定)を取得する
	 * @param inputParam
	 * @return
	 */
	//List<BreakDayOffDetail> lstInterimBreakDetail(BreakDayOffRemainMngParam inputParam);
	/**
	 * 4-1.代休と紐付けをしない休出を取得する
	 * @param breakMng
	 * @param remainData
	 * @return
	 */
	BreakDayOffDetail getNotTypeDayOff(InterimBreakMng breakMng, InterimRemain remainData, GeneralDate aggStartDate,
			GeneralDate baseDate, SubstitutionHolidayOutput subsHolidaySetting, String cid, String sid);
	/**
	 * 5.時系列順で相殺する
	 * @param lstDataDetail
	 * @return
	 */
	List<BreakDayOffDetail> lstSortForTime(List<BreakDayOffDetail> lstDataDetail);
	/**
	 * 7.発生数・使用数を計算する
	 * @param lstDataDetail
	 * @param dateData
	 * @return
	 */
	RemainUnDigestedDayTimes getRemainOccurrenceUseDayTimes(List<BreakDayOffDetail> lstDataDetail, DatePeriod dateData);
	/**
	 * 消化区分と消滅日を計算する
	 * @param lstDetail
	 * @return
	 */
	List<BreakDayOffDetail> calDigestionAtr(List<BreakDayOffDetail> lstDetail, GeneralDate baseDate);
	/**
	 *  3.未相殺の代休(暫定)を取得する
	 * 4.未使用の休出(暫定)を取得する
	 * @param inputParam
	 * @param lstDetailData
	 * @return
	 */
	List<BreakDayOffDetail> lstInterimData(BreakDayOffRemainMngParam inputParam, List<BreakDayOffDetail> lstDetailData);
	
	/**
	 * [No.505]代休残数を取得する
	 * @param employeeID
	 * @param date
	 * @return
	 */
	double getBreakDayOffMngRemain(String employeeID, GeneralDate date);
}
