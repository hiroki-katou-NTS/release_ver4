package nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service;

import java.util.List;
import java.util.Optional;

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
	InPeriodOfSpecialLeaveResultInfor complileInPeriodOfSpecialLeave(ComplileInPeriodOfSpecialLeaveParam param);
	/**
	 * 管理データを取得する
	 * @param cid
	 * @param sid 
	 * @param specialLeaveCode ・特別休暇コード
	 * @param complileDate ・集計開始日 ・集計終了日
	 * @return 特別休暇付与残数データ
	 */
	ManagaData getMngData(String cid, String sid, int specialLeaveCode, DatePeriod complileDate, Optional<InPeriodOfSpecialLeaveResultInfor> beforeResult);

	
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
	 * 特休の使用数を求める
	 * @param cid 会社ID
	 * @param sid 社員ID
	 * @param complileDate 集計開始日 , 集計終了日
	 * @param remainDatas 特別休暇付与残数データ一覧
	 * @param interimDatas 特別休暇暫定データ一覧
	 * @return
	 */
	RemainDaysOfSpecialHoliday getUseDays(String cid, String sid, DatePeriod complileDate,
			SpecialLeaveGrantRemainingDataTotal speRemainData, List<InterimSpecialHolidayMng> interimSpeDatas, List<InterimRemain> lstInterimMng);
	/**
	 * 管理データと暫定データの相殺
	 * @param cid・会社ID
	 * @param sid・社員ID
	 * @param dateData ・集計開始日 ・集計終了日
	 * @param baseDate ・基準日
	 * @param specialCode ・特別休暇コード
	 * @param lstGrantData ・特別休暇付与残数データ一覧
	 * @param interimDataMng ・特別休暇暫定データ一覧
	 * @param accumulationMaxDays ・蓄積上限日数
	 * @return
	 */
	InPeriodOfSpecialLeave getOffsetDay1004(String cid, String sid, DatePeriod dateData, GeneralDate baseDate, int specialCode,
			SpecialLeaveGrantRemainingDataTotal lstGrantData, SpecialHolidayInterimMngData interimDataMng, 
			double accumulationMaxDays,RemainDaysOfSpecialHoliday useInfor, boolean isMode);
	/**
	 * 使用数を管理データから引く
	 * @param lstGrantData 特別休暇付与残数データ一覧
	 * @param interimDataMng 特別休暇暫定データ一覧
	 * @return
	 */
	SubtractUseDaysFromMngDataOut subtractUseDaysFromMngData1004(List<SpecialLeaveGrantRemainingData> lstGrantData, SpecialHolidayInterimMngData interimDataMng,
			RemainDaysOfSpecialHoliday useInfor);
	/**
	 * 繰越上限日数まで調整する
	 * @param lstGrantData 特別休暇付与残数データ一覧
	 * @param accumulationMaxDays  蓄積上限日数
	 * @param grantDetailBefore 付与前の残数 
	 * @return
	 */
	DataMngOfDeleteExpired adjustCarryForward1005(List<SpecialLeaveGrantRemainingData> lstGrantData, double accumulationMaxDays);
	/**
	 * 付与前の残数情報をまとめる
	 * @param lstSpeLeaveGrantDetails
	 * @param grantDetailBefore
	 * @return
	 */
	SpecialHolidayRemainInfor grantDetailBefore(List<SpecialLeaveGrantDetails> lstSpeLeaveGrantDetails, SpecialHolidayRemainInfor grantDetailBefore);
	/**
	 * (付与前)管理データと暫定データの相殺
	 * @param cid ・会社ID
	 * @param sid ・社員ID
	 * @param shukeiDate ・集計開始日 ・ 集計終了日
	 * @param lstGrantData・特別休暇付与残数データ一覧
	 * @param interimDataMng ・特別休暇暫定データ一覧
	 * @param baseDate: 基準日
	 * @return
	 */
	RemainDaysOfSpecialHoliday remainDaysBefore(String cid, String sid, DatePeriod shukeiDate, SpecialLeaveGrantRemainingDataTotal lstGrantData,
			SpecialHolidayInterimMngData interimDataMng,RemainDaysOfSpecialHoliday useInfor, GeneralDate baseDate, boolean isMode);
	/**
	 * 付与後の残数情報をまとめる
	 * @param lstSpeLeaveGrantDetails
	 * @param grantDetailBefore
	 * @return
	 */
	InPeriodOfSpecialLeave grantDetailAfter(SpecialHolidayRemainInfor grantDetailAfter, InPeriodOfSpecialLeave getOffsetDay);
	
}
