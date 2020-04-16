package nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.specialholiday.updateremainnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.GrantRemainRegisterType;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.InPeriodOfSpecialLeave;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.LimitTimeAndDays;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.SpecialLeaveGrantDetails;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.SpecialLeaveNumberInfoService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 特別休暇残数更新
 * @author shuichi_ishida
 */
@Stateless
public class RemainSpecialHolidayUpdating {

	/** 特別休暇付与残数データ */
	@Inject
	private SpecialLeaveGrantRepository specialLeaveGrantRepo;
	
	/**
	 * 特別休暇残数更新
	 * @param output 特別休暇の集計結果
	 * @param empId 社員ID
	 * @param period 期間
	 * @param specialLeaveCode 特別休暇コード
	 * @param isAutoGrantFlg: false: しない、True：　する
	 */
	public void updateRemainSpecialHoliday(
			InPeriodOfSpecialLeave output,
			String empId,
			DatePeriod period,
			int specialLeaveCode,
			boolean isAutoGrantFlg){

		String companyId = AppContexts.user().companyId();
	
		Map<GeneralDate, String> existDataMap = new HashMap<>();
		
		// 当月以降の特別休暇付与残数データを削除
		List<SpecialLeaveGrantRemainingData> datas = this.specialLeaveGrantRepo.getAll(empId, specialLeaveCode);
		for (SpecialLeaveGrantRemainingData data : datas){
			if (data.getGrantDate().after(period.start()) && isAutoGrantFlg) {
				this.specialLeaveGrantRepo.delete(data.getSpecialId());
			}
			else {
				existDataMap.putIfAbsent(data.getGrantDate(), data.getSpecialId());
			}
		}
		
		// 特別休暇付与残数データの更新
		// 特別休暇付与残数データ更新処理
		List<SpecialLeaveGrantDetails> details = output.getLstSpeLeaveGrantDetails();
		for (SpecialLeaveGrantDetails detail : details){
			
			String specialId = IdentifierUtil.randomUniqueId();
			if (existDataMap.containsKey(detail.getGrantDate())){
				specialId = existDataMap.get(detail.getGrantDate());
			}
			
			SpecialLeaveNumberInfoService info = detail.getDetails();
			double numberOverdays = 0.0;	// 上限超過消滅日数
			Integer timeOver = null;		// 上限超過消滅時間
			if (info.getLimitDays().isPresent()){
				LimitTimeAndDays limitDays = info.getLimitDays().get();
				numberOverdays = limitDays.getDays();
				timeOver = (limitDays.getTimes().isPresent() ? limitDays.getTimes().get() : null);
			}
			//残数がマイナスの場合の補正処理
			SpecialLeaveGrantRemainingData updateData = SpecialLeaveGrantRemainingData.createFromJavaType(
					specialId,
					companyId,
					empId,
					specialLeaveCode,
					detail.getGrantDate(),
					detail.getDeadlineDate(),
					detail.getExpirationStatus().value,
					GrantRemainRegisterType.MONTH_CLOSE.value,
					info.getGrantDays(),
					(info.getGrantTimes().isPresent() ? info.getGrantTimes().get() : null),
					info.getRemainDays() < 0? info.getGrantDays() :info.getUseDays(),
					(info.getUseTimes().isPresent() ? info.getUseTimes().get() : null),
					null,				// 積み崩し日数
					numberOverdays,
					timeOver,
					info.getRemainDays() < 0?0:info.getRemainDays(),
					(info.getRemainTimes().isPresent() ? info.getRemainTimes().get() : null));
			
			if (existDataMap.containsKey(detail.getGrantDate())){
				
				// 「特別休暇付与残数データ」を更新する
				this.specialLeaveGrantRepo.update(updateData);
			}
			else {
				
				// 「特別休暇付与残数データ」を追加する
				this.specialLeaveGrantRepo.add(updateData);
			}
		}
	}
}
