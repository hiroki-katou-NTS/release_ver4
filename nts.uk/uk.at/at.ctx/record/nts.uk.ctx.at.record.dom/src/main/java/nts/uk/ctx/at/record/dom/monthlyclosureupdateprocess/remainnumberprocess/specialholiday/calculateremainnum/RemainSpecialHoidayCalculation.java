package nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.specialholiday.calculateremainnum;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.ComplileInPeriodOfSpecialLeaveParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.InPeriodOfSpecialLeave;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.InPeriodOfSpecialLeaveResultInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.SpecialLeaveManagementService;
import nts.uk.shr.com.context.AppContexts;
//
/**
 * 特別休暇残数計算
 * @author shuichi_ishida
 */
@Stateless
public class RemainSpecialHoidayCalculation {

	/** 期間内の特別休暇残を集計する */
	@Inject
	private SpecialLeaveManagementService specialLeaveMng;
	
	/**
	 * 特別休暇残数計算
	 * @param period 実締め毎集計期間
	 * @param empId 社員ID
	 * @param specialLeaveCode 特別休暇コード
	 * @param interimMng 暫定残数管理データリスト
	 * @param interimSpecialData 特別休暇暫定データリスト
	 * @return 特別休暇の集計結果
	 */
	public InPeriodOfSpecialLeave calculateRemainSpecial(AggrPeriodEachActualClosure period,
			String empId, int specialLeaveCode,
			List<InterimRemain> interimMng, List<InterimSpecialHolidayMng> interimSpecialData) {
		
		String companyId = AppContexts.user().companyId();
		
		// 「期間内の特別休暇残を集計する」を実行する
		ComplileInPeriodOfSpecialLeaveParam param = new ComplileInPeriodOfSpecialLeaveParam(
				companyId, empId, period.getPeriod(),
				true, period.getPeriod().end(), specialLeaveCode, true,
				true, interimMng, interimSpecialData,
				Optional.empty()); //ソースがエラーを発生しないようにとりあえずEmptyを追加した、修正依頼があったら修正してください
		return this.specialLeaveMng.complileInPeriodOfSpecialLeave(param).getAggSpecialLeaveResult();
	}
}
