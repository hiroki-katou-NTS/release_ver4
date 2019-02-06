package nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.SubstitutionOfHDManagementData;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.processten.LeaveSetOutput;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface AbsenceReruitmentMngInPeriodQuery {
	/**
	 * Requestlist204 期間内の振出振休残数を取得する
	 * @return
	 */
	AbsRecRemainMngOfInPeriod getAbsRecMngInPeriod(AbsRecMngInPeriodParamInput paramInput);
	/**
	 * 1.未相殺の振休(確定)を取得する
	 * @param sid
	 * @return
	 */
	List<AbsRecDetailPara> getAbsOfUnOffset(String cid, String sid, GeneralDate ymd);
	/**
	 * 1-1.確定振休から未相殺の振休を取得する
	 * @param sid
	 * @return
	 */
	//List<SubstitutionOfHDManagementData> getAbsOfUnOffsetFromConfirm(String cid, String sid, GeneralDate ymd);
	/**
	 * 1-3.暫定振出と紐付けをしない確定振休を取得する
	 * @param confirmAbsData
	 * @return
	 */
	AbsRecDetailPara getInterimAndConfirmAbs(String sid, SubstitutionOfHDManagementData confirmAbsData);
	/**
	 * 2.未使用の振出(確定)を取得する
	 * @param sid
	 * @return
	 */
	List<AbsRecDetailPara> getUnUseDaysConfirmRec(String cid, String sid, List<AbsRecDetailPara> lstDataDetail, GeneralDate ymd);
	/**
	 * 繰越数を計算する
	 * @param startDate 集計開始日
	 * @param lstDataDetail 振出振休明細
	 * @return
	 */
	ResultAndError calcCarryForwardDays(GeneralDate startDate, List<AbsRecDetailPara> lstDataDetail);
	/**
	 * 6.残数と未消化を集計する
	 * @param lstDataDetail 「振出振休明細」
	 * @param baseDate 基準日
	 * @return
	 */
	AbsDaysRemain getRemainUnDigestedDays(List<AbsRecDetailPara> lstDataDetail, GeneralDate baseDate);
	/**
	 * 3.未相殺の振休(暫定)を取得する
	 * @return
	 */
	List<AbsRecDetailPara> getUnOffsetDaysAbsInterim(AbsRecMngInPeriodParamInput paramInput);
	/**
	 * 3-1.振出と紐付けをしない振休を取得する
	 * @param lstAbsMng
	 * @return
	 */
	AbsRecDetailPara getNotTypeRec(InterimAbsMng lstAbsMng, InterimRemain remainData);
	/**
	 * 4-1.振休と紐付けをしない振出を取得する
	 * @param interimRecMng
	 * @param remainData
	 * @return
	 */
	AbsRecDetailPara getUnUseDayOfRecInterim(InterimRecMng interimRecMng, InterimRemain remainData, LeaveSetOutput getSetForLeave,
			GeneralDate startDate, GeneralDate baseDate, String cid, String sid);
	/**
	 * 5.時系列順で相殺する
	 * @param lstDetailData
	 * @return
	 */
	List<AbsRecDetailPara> offsetSortTimes(List<AbsRecDetailPara> lstDetailData);
	/**
	 * 7.発生数・使用数を計算する
	 * @param lstDetailData
	 * @param dateData
	 * @return
	 */
	AbsDaysRemain getOccurrenceUseDays(List<AbsRecDetailPara> lstDetailData, DatePeriod dateData);
	/**
	 * 消化区分と消滅日を計算する
	 * @param lstDetailData
	 * @param baseDate
	 * @return
	 */
	List<AbsRecDetailPara> calDigestionAtr(List<AbsRecDetailPara> lstDetailData, GeneralDate baseDate);
	/**
	 * アルゴリズム「未相殺の振休(暫定)を取得する」を実行する
	 * アルゴリズム「未使用の振出(暫定)を取得する」を実行する
	 * @param paramInput
	 * @return
	 */
	List<AbsRecDetailPara> lstInterimInfor(AbsRecMngInPeriodParamInput paramInput, List<AbsRecDetailPara> lstAbsRec);
	
	/**
	 * [No.506]振休残数を取得する
	 * @param employeeID
	 * @param date
	 * @return
	 */
	double getAbsRecMngRemain(String employeeID, GeneralDate date);
}
