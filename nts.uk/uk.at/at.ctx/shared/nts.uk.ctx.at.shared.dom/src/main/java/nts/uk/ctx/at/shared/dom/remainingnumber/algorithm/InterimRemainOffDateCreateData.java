package nts.uk.ctx.at.shared.dom.remainingnumber.algorithm;
import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.CreateAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.CompanyHolidayMngSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.DayoffTranferInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.EmploymentHolidayMngSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.InforFormerRemainData;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.TranferTimeInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.WorkTypeRemainInfor;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

public interface InterimRemainOffDateCreateData {
	/**
	 * 指定日の暫定残数管理データを作成する
	 * @param cid
	 * @param sid
	 * @param baseDate
	 * @param dayOffTimeIsUse: 時間代休利用: True: 利用, False: 未利用 
	 * @param detailData
	 * @return
	 */
	public DailyInterimRemainMngData createData(String cid, String sid, GeneralDate baseDate, boolean dayOffTimeIsUse, InterimRemainCreateInfor detailData
			, CompanyHolidayMngSetting comHolidaySetting, EmploymentHolidayMngSetting employmentHolidaySetting);
	/**
	 * 残数作成元情報を作成する
	 * @param sid
	 * @param baseDate
	 * @param detailData
	 * @return
	 */
	public InforFormerRemainData createInforFormerRemainData(String cid, String sid, GeneralDate baseDate, InterimRemainCreateInfor detailData, 
			boolean dayOffTimeIsUse, CompanyHolidayMngSetting comHolidaySetting, EmploymentHolidayMngSetting employmentHolidaySetting);
	/**
	 * 実績から残数作成元情報を設定する
	 * @param recordData
	 * @return
	 */
	public InforFormerRemainData createInterimDataFromecord(String cid,RecordRemainCreateInfor recordData, InforFormerRemainData outputData);
	/**
	 * 勤務種類別残数情報を作成する
	 * @param cid
	 * @param createAtr
	 * @param workTypeCode
	 * @return
	 */
	public List<WorkTypeRemainInfor> createWorkTypeRemainInfor(String cid, CreateAtr createAtr, String workTypeCode);
	/**
	 * 残数発生使用明細を作成する
	 * @param workType
	 * @param dataOutput
	 * @return
	 */
	public void createWithOneDayWorkType(String cid, WorkType workType, WorkTypeRemainInfor dataOutput);
	/**
	 * 午前と午後勤務時の残数発生使用明細を作成する
	 * @param workType
	 * @param dataOutput
	 * @return
	 */
	public WorkTypeRemainInfor createWithHaftDayWorkType(WorkType workType, WorkTypeRemainInfor dataOutput);
	/**
	 * 実績から代休振替情報を作成する
	 * @param recordData
	 * @param dayOffTimeIsUse 時間代休利用
	 * @return
	 */
	public DayoffTranferInfor createDayOffTranferFromRecord(String cid, CreateAtr createAtr, RecordRemainCreateInfor recordData, boolean dayOffTimeIsUse);
	/**
	 * 残数作成元情報から暫定残数管理データを作成する
	 * @param inforData
	 * @return
	 */
	public DailyInterimRemainMngData createDataInterimRemain(InforFormerRemainData inforData);

	/**
	 * 予定から残数作成元情報を設定する
	 * @param scheData
	 * @param outputData
	 * @return
	 */
	InforFormerRemainData createInterimDataFromSche(String cid, ScheRemainCreateInfor scheData, InforFormerRemainData outputData,
			boolean dayOffTimeIsUse);
	/**
	 * 就業時間帯から代休振替情報を作成する
	 * @param remainInfor
	 * @param workTimeCode
	 * @param isTranferTime: True: 設定あり, False: 設定なし
	 * @param timeSetting: 休出振替可能時間
	 * @param timeOverSetting: 実績残業振替時間
	 * @return
	 */
	List<DayoffTranferInfor> createDayoffFromWorkTime(String cid, List<WorkTypeRemainInfor> remainInfor, String workTimeCode,
			Integer timeSetting, CreateAtr createAtr, Integer timeOverSetting, boolean dayOffTimeIsUse);
	/**
	 * 代休振替時間を算出する
	 * @param workTimeCode
	 * @param timeSetting
	 * @param dayoffChange
	 * @return
	 */
	Optional<TranferTimeInfor> calDayoffTranferTime(String cid, CreateAtr createAtr,String workTimeCode, Integer timeSetting, DayoffChangeAtr dayoffChange);
	/**
	 * 最新の勤務種類変更を伴う申請から残数作成元情報を設定する
	 * @param appInfor
	 * @param outputData
	 * @param dayOffTimeIsUse
	 * @return
	 */
	InforFormerRemainData createInterimDataFromApp(String cid, InterimRemainCreateInfor createInfo, AppRemainCreateInfor appInfor, 
			InforFormerRemainData outputData, boolean dayOffTimeIsUse, CreateAtr createAtr);
	/**
	 * 休日出勤申請から代休振替情報を作成する
	 * @param dayOffTimeIsUse
	 * @param appInfor
	 * @return
	 */
	List<DayoffTranferInfor> tranferInforFromHolidayWork(String cid, boolean dayOffTimeIsUse, AppRemainCreateInfor appInfor,
			List<WorkTypeRemainInfor> remainInfor, CreateAtr createAtr);
	/**
	 * 休日出勤以外の申請から代休振替情報を作成する
	 * @param dayOffTimeIsUse
	 * @param appInfor
	 * @param remainInfor
	 * @return
	 */
	List<DayoffTranferInfor> transferInforFromNotHolidayWork(String cid, boolean dayOffTimeIsUse, AppRemainCreateInfor appInfor,
			InterimRemainCreateInfor createInfo, List<WorkTypeRemainInfor> remainInfor, CreateAtr createAtr);

}
