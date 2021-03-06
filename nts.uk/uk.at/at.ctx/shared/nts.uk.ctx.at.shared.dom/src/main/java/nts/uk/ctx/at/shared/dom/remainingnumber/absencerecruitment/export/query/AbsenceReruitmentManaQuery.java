package nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query;
/**
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.shared.残数管理.振出振休管理.アルゴリズム.Query.月度別振休残数集計を取得.アルゴリズム.月度別振休残数集計を取得
 * @author do_dt
 *
 */

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.InterimRemainAggregateOutputData;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.empinfo.grantremainingdata.daynumber.LeaveRemainingDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.PayoutManagementData;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.PayoutSubofHDManagement;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.SubstitutionOfHDManagementData;
import nts.arc.time.calendar.period.DatePeriod;

public interface AbsenceReruitmentManaQuery {
	/**
	 * RequestList270 月度別振休残数集計を取得
	 * @param employeeId
	 * @param baseDate
	 * @param startMonth
	 * @param endMonth
	 * @return
	 */
	List<InterimRemainAggregateOutputData> getAbsRecRemainAggregate(String employeeId, GeneralDate baseDate, YearMonth startMonth, YearMonth endMonth);
	/**
	 * 期間内の振休発生数合計を取得
	 * @param employeeId
	 * @param startMonth
	 * @param endMonth
	 * @return
	 */
	double getTotalOccurrentDay(String employeeId, DatePeriod dateData);
	/**
	 * 期間内の振休使用数合計を取得
	 * @param employeeId
	 * @param dateData
	 * @return
	 */
	double getUsedDays(String employeeId, DatePeriod dateData);
	/**
	 * RequestList450: 振出振休発生消化履歴の取得
	 * @param cid
	 * @param sid
	 * @param baseDate
	 * @return
	 */
	Optional<AbsRecGenerationDigestionHis> generationDigestionHis(String cid, String sid, GeneralDate baseDate);
	/**
	 * 暫定管理データを取得する
	 * @param sid
	 * @param baseDate
	 * @return
	 */
	Optional<AbsRecInterimOutputPara> getAbsRecInterimData(String sid, GeneralDate baseDate, AbsRecConfirmOutputPara confirmData);
	/**
	 * 指定期間内に発生した暫定振出と紐付いた確定振休・暫定振休を取得する
	 * @param sid
	 * @param dateData
	 * @return
	 */
	Optional<AbsRecInterimOutputPara> getInterimAbsMng(String sid, DatePeriod dateData);
	/**
	 * 未消化の確定振出に紐付いた暫定振休を取得する
	 * @param sid
	 * @param dateData
	 * @return
	 */
	AbsRecInterimOutputPara getNotInterimAbsMng(String sid, DatePeriod dateData, AbsRecInterimOutputPara absRecData, AbsRecConfirmOutputPara confirmData);
	/**
	 * 振出履歴を作成する
	 * @param interimData: 暫定振出管理データ
	 * @return
	 */
	List<RecruitmentHistoryOutPara> createRecruitmentHis(List<InterimRecMng> interimData, List<PayoutManagementData> confirmData, List<InterimRecAbsMng> lstInterimData);
	/**
	 * 振休履歴を作成する
	 * @param interimData : 暫定振休管理データ
	 * @return
	 */
	List<AbsenceHistoryOutputPara> createAbsenceHis(List<InterimAbsMng> interimDataList, List<SubstitutionOfHDManagementData> lstAbsConfirm
			, List<InterimRecAbsMng> lstInterimData);
	/**
	 * 振出振休履歴対照情報を作成する
	 * @param lstRecHis 振出履歴
	 * @param lstAbsHis 振休履歴
	 * @param lstInterimData 暫定振出振休紐付け管理
	 * @return
	 */
	List<RecAbsHistoryOutputPara> createRecAbsHis(List<RecruitmentHistoryOutPara> lstRecHis, List<AbsenceHistoryOutputPara> lstAbsHis, 
			List<InterimRecAbsMng> lstInterimData, List<PayoutSubofHDManagement> lstConfirmData);
	/**
	 * 残数集計情報を作成する
	 * @param lstRecHis 振出履歴
	 * @param lstAbsHis 振休履歴
	 * @return
	 */
	AsbRemainTotalInfor getAbsRemainTotalInfor(List<RecruitmentHistoryOutPara> lstRecHis, List<AbsenceHistoryOutputPara> lstAbsHis);
	/**
	 * 当月の振休残数を集計する
	 * @param sid
	 * @param dateData
	 * @param outData
	 * @return
	 */
	InterimRemainAggregateOutputData calAsbRemainOfCurrentMonth(String sid, DatePeriod dateData, InterimRemainAggregateOutputData outData);
	/**
	 * 月初の振休残数を取得
	 * @param sid
	 * @return
	 */
	LeaveRemainingDayNumber useDays(String sid);
	/**
	 * 期間内の振休消滅数合計を取得
	 * @param sid
	 * @param dateData
	 * @return
	 */
	double getMonthExtinctionDays(String sid, DatePeriod dateData);
	/**
	 * 確定管理データを取得する
	 * @param sid
	 * @return
	 */
	AbsRecConfirmOutputPara getAbsRecConfirmData(String sid);
}
