package nts.uk.ctx.at.shared.dom.remainingnumber.algorithm;

import java.util.List;
import java.util.Map;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.adapter.employment.AffPeriodEmpCodeImport;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.CompanyHolidayMngSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.EmploymentHolidayMngSetting;

/**
 * 指定期間の暫定残数管理データを作成する
 * @author do_dt
 *
 */
public interface InterimRemainOffPeriodCreateData {
	/**
	 * 指定期間の暫定残数管理データを作成する
	 * @param inputParam
	 * @return
	 */
	public Map<GeneralDate, DailyInterimRemainMngData> createInterimRemainDataMng(InterimRemainCreateDataInputPara inputParam
			, CompanyHolidayMngSetting comHolidaySetting);
	/**
	 * 対象日のデータを抽出する
	 * @param baseDate
	 * @param inputInfor
	 * @return
	 */
	public InterimRemainCreateInfor extractDataOfDate(String cid, GeneralDate baseDate, InterimRemainCreateDataInputPara inputInfor);
	/**
	 * 雇用履歴と休暇管理設定を取得する
	 * @param cid
	 * @param sid
	 * @return
	 */
	public List<EmploymentHolidayMngSetting> lstEmpHolidayMngSetting(String cid, List<AffPeriodEmpCodeImport> lstEmployment);
	/**
	 * 指定期間の暫定残数管理データを作成する（差分のみ）
	 * @param param
	 * @return
	 */
	public Map<GeneralDate, DailyInterimRemainMngData> createInterimRemainByScheRecordApp(InterimRemainCreateDataInputPara param);
}
