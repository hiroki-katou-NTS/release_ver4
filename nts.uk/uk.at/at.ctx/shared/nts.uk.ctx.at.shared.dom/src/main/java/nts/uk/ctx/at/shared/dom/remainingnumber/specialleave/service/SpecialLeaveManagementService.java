package nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service;

import java.util.List;
import java.util.Map;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemainingData;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
/**
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.勤務実績.残数管理.残数管理.特別休暇管理.Export
 * @author do_dt
 *
 */
public interface SpecialLeaveManagementService {
	/**
	 * RequestList273 期間内の特別休暇残を集計する 
	 * @return
	 */
	InPeriodOfSpecialLeave complileInPeriodOfSpecialLeave(ComplileInPeriodOfSpecialLeaveParam param);
	/**
	 * 管理データを取得する
	 * @param cid
	 * @param sid 
	 * @param specialLeaveCode ・特別休暇コード
	 * @param complileDate ・集計開始日 ・集計終了日
	 * @return 特別休暇付与残数データ
	 */
	ManagaData getMngData(String cid, String sid, int specialLeaveCode, DatePeriod complileDate);

	/**
	 * 使用数を管理データから引く
	 * @param specialLeaverData ・特別休暇付与残数データ一覧
	 * @param interimSpeHolidayData ・特別休暇暫定データ一覧
	 * @return
	 */
	InPeriodOfSpecialLeave subtractUseDaysFromMngData(List<SpecialLeaveGrantRemainingData> specialLeaverData, List<InterimSpecialHolidayMng> interimSpeHolidayData,
			List<InterimRemain> lstInterimMng, OffsetDaysFromInterimDataMng offsetDays, InPeriodOfSpecialLeave inPeriodData, Map<GeneralDate, Double> limitDays);

	
	/**
	 * 特別休暇暫定データを取得する
	 * @param cid
	 * @param sid
	 * @param dateData
	 * @param mode
	 * @return
	 */
	SpecialHolidayInterimMngData specialHolidayData(SpecialHolidayDataParam param);
	/**
	 * 管理データと暫定データの相殺
	 * @param cid
	 * @param sid
	 * @param dateData ・INPUT．集計開始日・INPUT．集計終了日
	 * @param baseDate 基準日
	 * @param lstGrantData 取得した特別休暇付与残数データ一覧
	 * @param lstInterimData 取得した特別休暇暫定データ一覧
	 * @param accumulationMaxDays 蓄積上限日数
	 * @return
	 */
	InPeriodOfSpecialLeave getOffsetDay(String cid, String sid, DatePeriod dateData, GeneralDate baseDate, int specialCode,
			List<SpecialLeaveGrantRemainingData> lstGrantData, SpecialHolidayInterimMngData interimDataMng, double accumulationMaxDays);
	/**
	 * 残数情報をまとめる
	 * @param inPeriodData
	 * @return
	 */
	InPeriodOfSpecialLeave sumRemainData(InPeriodOfSpecialLeave inPeriodData);
}
