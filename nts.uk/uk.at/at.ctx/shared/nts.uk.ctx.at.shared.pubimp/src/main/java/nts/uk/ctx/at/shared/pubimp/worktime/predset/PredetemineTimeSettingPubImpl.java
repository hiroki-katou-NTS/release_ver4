/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.pubimp.worktime.predset;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.worktime.predset.BreakDownTimeDay;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.ctx.at.shared.dom.worktime.predset.UseSetting;
import nts.uk.ctx.at.shared.pub.worktime.predset.BreakDownTimeDayExport;
import nts.uk.ctx.at.shared.pub.worktime.predset.PredetemineTimeSettingPub;
import nts.uk.ctx.at.shared.pub.worktime.predset.PredeterminedTimeExport;

/**
 * The Class PredetemineTimeSettingPubImpl.
 */
@Stateless
public class PredetemineTimeSettingPubImpl implements PredetemineTimeSettingPub{
	
	/** The work two. */
	public static Integer WORK_TWO = 2;
	
	/** The predetemine time setting repository. */
	@Inject
	private PredetemineTimeSettingRepository predetemineTimeSettingRepository;

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.pub.worktime.predset.PredetemineTimeSettingPub#IsWorkingTwice(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean IsWorkingTwice(String companyId, String workTimeCode) {
		// ドメインモデル「所定時間設定」を取得
		Optional<PredetemineTimeSetting> optPredetemineTimeSetting = this.predetemineTimeSettingRepository.findByWorkTimeCode(companyId, workTimeCode);
		
		// 取得できなかった場合
		if (!optPredetemineTimeSetting.isPresent()) {
			return false;
		}
		
		// 所定時間帯．時間帯を取得
		TimezoneUse abc =  optPredetemineTimeSetting.get().getPrescribedTimezoneSetting().getMatchWorkNoTimeSheet(WORK_TWO);
		
		// 使用区分を判断
		if (abc.getUseAtr().value == UseSetting.USE.value) {
			return true;
		}else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.pub.worktime.predset.PredetemineTimeSettingPub#
	 * acquirePredeterminedTime(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<PredeterminedTimeExport> acquirePredeterminedTime(String companyId, String workTimeCode) {
		Optional<PredetemineTimeSetting> pred = this.predetemineTimeSettingRepository.findByWorkTimeCode(companyId,
				workTimeCode);

		if (pred.isPresent()) {
			BreakDownTimeDay predTime = pred.get().getPredTime().getPredTime();
			BreakDownTimeDay addTime = pred.get().getPredTime().getAddTime();

			return Optional.of(PredeterminedTimeExport.builder()
					.predTime(BreakDownTimeDayExport.builder()
							.morning(predTime.getMorning().v())
							.afternoon(predTime.getAfternoon().v())
							.oneDay(predTime.getOneDay().v()).build())
					.addTime(BreakDownTimeDayExport.builder()
							.morning(addTime.getMorning().v())
							.afternoon(addTime.getAfternoon().v())
							.oneDay(addTime.getOneDay().v()).build()).build());
		}

		return Optional.empty();
	}

}
