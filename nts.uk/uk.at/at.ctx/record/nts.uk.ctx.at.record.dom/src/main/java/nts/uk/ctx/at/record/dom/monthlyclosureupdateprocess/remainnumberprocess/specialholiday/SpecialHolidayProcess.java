package nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.specialholiday;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.specialholiday.calculateremainnum.RemainSpecialHoidayCalculation;
import nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.specialholiday.deletetempdata.SpecialTempDataDeleting;
import nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.specialholiday.updateremainnum.RemainSpecialHolidayUpdating;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.DailyInterimRemainMngData;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.InPeriodOfSpecialLeave;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHolidayRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * 特別休暇処理
 * @author shuichi_ishida
 */
@Stateless
public class SpecialHolidayProcess {

	/** 特別休暇 */
	@Inject
	private SpecialHolidayRepository specialHolidayRepo;
	
	/** 特別休暇残数計算 */
	@Inject
	private RemainSpecialHoidayCalculation remainCalculation;
	
	/** 特別休暇残数更新 */
	@Inject
	private RemainSpecialHolidayUpdating remainUpdate;

	/** 特別休暇暫定データ削除 */
	@Inject
	private SpecialTempDataDeleting tempDelete;
	
	/**
	 * 特別休暇処理
	 * @param period 実締め毎集計期間
	 * @param empId 社員ID
	 * @param interimRemainMngMap 暫定管理データリスト
	 * @param  isAutoGrantFlg 自動付与区分: false: しない、True：　する
	 */
	public void specialHolidayProcess(AggrPeriodEachActualClosure period, String empId,
			Map<GeneralDate, DailyInterimRemainMngData> interimRemainMngMap, boolean isAutoGrantFlg) {

		String companyId = AppContexts.user().companyId();
		
		// 暫定残数データを特別休暇に絞り込む
		List<InterimRemain> interimMng = new ArrayList<>();
		List<InterimSpecialHolidayMng> interimSpecialData = new ArrayList<>();
		for (val interimRemainMng : interimRemainMngMap.values()){
			if (interimRemainMng.getRecAbsData().size() <= 0) continue;
			if (interimRemainMng.getSpecialHolidayData().size() <= 0) continue;
			interimMng.addAll(interimRemainMng.getRecAbsData());
			interimSpecialData.addAll(interimRemainMng.getSpecialHolidayData());
		}
		
		// 「特別休暇」を取得する
		val specialHolidays = this.specialHolidayRepo.findByCompanyId(companyId);
		for (val specialHoliday : specialHolidays){
			int specialLeaveCode = specialHoliday.getSpecialHolidayCode().v();
			
			// 特別休暇残数計算
			InPeriodOfSpecialLeave output = this.remainCalculation.calculateRemainSpecial(
					period, empId, specialLeaveCode, interimMng, interimSpecialData);
			
			// 特別休暇残数更新
			this.remainUpdate.updateRemainSpecialHoliday(output, empId, period.getPeriod(), specialLeaveCode, isAutoGrantFlg);
		}
		
		// 特別休暇暫定データ削除
		this.tempDelete.deleteTempDataProcess(empId, period.getPeriod());
	}
}
