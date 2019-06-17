/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.bonuspay.primitives.BonusPaySettingCode;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;

/**
 * The Class WorkTimezoneCommonSet.
 */
// 就業時間帯の共通設定
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkTimezoneCommonSet extends WorkTimeDomainObject implements Cloneable{

	/** The zero H stradd calculate set. */
	// 0時跨ぎ計算設定
	private boolean zeroHStraddCalculateSet;

	/** The interval set. */
	// インターバル時間設定
	private IntervalTimeSetting intervalSet;

	/** The sub hol time set. */
	// 代休時間設定
	private List<WorkTimezoneOtherSubHolTimeSet> subHolTimeSet;

	/** The medical sets. */
	// 医療設定
	private List<WorkTimezoneMedicalSet> medicalSets;

	/** The go out set. */
	// 外出設定
	private WorkTimezoneGoOutSet goOutSet;

	/** The stamp set. */
	// 打刻設定
	private WorkTimezoneStampSet stampSet;

	/** The late night time set. */
	// 深夜時間設定
	private WorkTimezoneLateNightTimeSet lateNightTimeSet;

	/** The short time work set. */
	// 短時間勤務設定
	private WorkTimezoneShortTimeWorkSet shortTimeWorkSet;

	/** The extraord time set. */
	// 臨時時間設定
	private WorkTimezoneExtraordTimeSet extraordTimeSet;

	/** The late early set. */
	// 遅刻・早退設定
	private WorkTimezoneLateEarlySet lateEarlySet;

	/** The holiday calculation. */
	// 休暇時の計算
	private HolidayCalculation holidayCalculation;

	/** The raising salary set. */
	// 加給設定
	private Optional<BonusPaySettingCode> raisingSalarySet;

	/**
	 * Instantiates a new work timezone common set.
	 *
	 * @param memento
	 *            the memento
	 */
	public WorkTimezoneCommonSet(WorkTimezoneCommonSetGetMemento memento) {
		this.zeroHStraddCalculateSet = memento.getZeroHStraddCalculateSet();
		this.intervalSet = memento.getIntervalSet();
		this.subHolTimeSet = memento.getSubHolTimeSet();
		this.medicalSets = memento.getMedicalSet();
		this.goOutSet = memento.getGoOutSet();
		this.stampSet = memento.getStampSet();
		this.lateNightTimeSet = memento.getLateNightTimeSet();
		this.shortTimeWorkSet = memento.getShortTimeWorkSet();
		this.extraordTimeSet = memento.getExtraordTimeSet();
		this.lateEarlySet = memento.getLateEarlySet();
		this.holidayCalculation = memento.getHolidayCalculation();
		this.raisingSalarySet = memento.getRaisingSalarySet();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(WorkTimezoneCommonSetSetMemento memento) {
		memento.setZeroHStraddCalculateSet(this.zeroHStraddCalculateSet);
		memento.setIntervalSet(this.intervalSet);
		memento.setSubHolTimeSet(this.subHolTimeSet);
		memento.setMedicalSet(this.medicalSets);
		memento.setGoOutSet(this.goOutSet);
		memento.setStampSet(this.stampSet);
		memento.setLateNightTimeSet(this.lateNightTimeSet);
		memento.setShortTimeWorkSet(this.shortTimeWorkSet);
		memento.setExtraordTimeSet(this.extraordTimeSet);
		memento.setLateEarlySet(this.lateEarlySet);
		memento.setHolidayCalculation(this.holidayCalculation);
		memento.setRaisingSalarySet(this.raisingSalarySet);
	}

	/**
	 * Correct data.
	 *
	 * @param screenMode
	 *            the screen mode
	 * @param oldDomain
	 *            the old domain
	 */
	public void correctData(ScreenMode screenMode, WorkTimezoneCommonSet oldDomain) {
		this.goOutSet.correctData(screenMode, oldDomain.getGoOutSet());
		this.subHolTimeSet.forEach(item -> item.correctData(screenMode, oldDomain.getSubHolTimeSet().stream()
				.filter(oldItem -> oldItem.getOriginAtr().equals(item.getOriginAtr())).findFirst().orElse(null)));
		this.stampSet.correctData(screenMode, oldDomain.getStampSet());
		
		// Tab 13
		this.extraordTimeSet.correctData(screenMode);
		// Tab 14
		this.shortTimeWorkSet.correctData(screenMode);
		// Tab 15
		this.medicalSets.forEach(item -> item.correctData(screenMode));
		// Tab 16
		if (ScreenMode.SIMPLE.equals(screenMode)) {
			this.zeroHStraddCalculateSet = false;
		}		
	}

	/**
	 * Correct default data.
	 *
	 * @param screenMode
	 *            the screen mode
	 */
	public void correctDefaultData(ScreenMode screenMode) {
		this.goOutSet.correctDefaultData(screenMode);
		this.subHolTimeSet.forEach(item -> item.correctDefaultData(screenMode));
		this.stampSet.correctDefaultData(screenMode);
		
		// Tab 13
		this.extraordTimeSet.correctData(screenMode);
		// Tab 14
		this.shortTimeWorkSet.correctData(screenMode);
		// Tab 15
		this.medicalSets.forEach(item -> item.correctData(screenMode));
		// Tab 16
		if (ScreenMode.SIMPLE.equals(screenMode)) {
			this.zeroHStraddCalculateSet = false;
		}	
	}
	
	/**
	 * 遅刻・早退設定の共通設定のみtrueに変更した「 就業時間帯の共通設定」を返す
	 * @return
	 */
	public WorkTimezoneCommonSet changeWorkTimezoneLateEarlySet() {
		return new WorkTimezoneCommonSet(this.zeroHStraddCalculateSet,
										 this.intervalSet,
										 this.subHolTimeSet,
										 this.medicalSets,
										 this.goOutSet,
										 this.stampSet,
										 this.lateNightTimeSet,
										 this.shortTimeWorkSet,
										 this.extraordTimeSet,
										 this.lateEarlySet.changeCommonSet(true),
										 this.holidayCalculation,
										 this.raisingSalarySet);
	}
	
	/**
	 * 遅刻・早退設定の共通設定のみ反転させた「 就業時間帯の共通設定」を返す
	 * @return　就業時間帯の共通設定
	 */
	public WorkTimezoneCommonSet reverceTimeZoneLateEarlySet() {
		return new WorkTimezoneCommonSet(this.zeroHStraddCalculateSet,
				 this.intervalSet,
				 this.subHolTimeSet,
				 this.medicalSets,
				 this.goOutSet,
				 this.stampSet,
				 this.lateNightTimeSet,
				 this.shortTimeWorkSet,
				 this.extraordTimeSet,
				 this.lateEarlySet.changeCommonSet(this.getLateEarlySet().getCommonSet().isDelFromEmTime()?false:true),
				 this.holidayCalculation,
				 this.raisingSalarySet);
	}
	
	@Override
	public WorkTimezoneCommonSet clone() {
		WorkTimezoneCommonSet cloned = new WorkTimezoneCommonSet();
		try {
			cloned.zeroHStraddCalculateSet = this.zeroHStraddCalculateSet ? true : false;
			cloned.intervalSet = this.intervalSet.clone();
			cloned.subHolTimeSet = this.subHolTimeSet.stream().map(c -> c.clone()).collect(Collectors.toList());
			cloned.medicalSets = this.medicalSets.stream().map(c -> c.clone()).collect(Collectors.toList());
			cloned.goOutSet = this.goOutSet.clone();
			cloned.stampSet = this.stampSet.clone();
			cloned.lateNightTimeSet = this.lateNightTimeSet.clone();
			cloned.shortTimeWorkSet = this.shortTimeWorkSet.clone();
			cloned.extraordTimeSet = this.extraordTimeSet.clone();
			cloned.lateEarlySet = this.lateEarlySet.clone();
			cloned.holidayCalculation = this.holidayCalculation.clone();
			if(this.raisingSalarySet.isPresent()) {
				cloned.raisingSalarySet = this.raisingSalarySet.map(c -> new BonusPaySettingCode(c.v()));
			}
			else {
				cloned.raisingSalarySet = Optional.empty();
			}
		}
		catch (Exception e){
			throw new RuntimeException("WorkTimezoneCommonSet clone error.");
		}
		return cloned;
	}
	
}
