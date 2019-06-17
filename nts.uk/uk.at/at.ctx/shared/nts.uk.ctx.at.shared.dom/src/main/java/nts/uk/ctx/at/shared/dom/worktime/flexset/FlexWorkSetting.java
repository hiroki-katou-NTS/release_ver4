/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flexset;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

//import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.common.GoLeavingWorkAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.StampReflectTimezone;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkRestSetting;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeAggregateRoot;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDivision;

/**
 * The Class FlexWorkSetting.
 */
@Getter
@NoArgsConstructor
// フレックス勤務設定
public class FlexWorkSetting extends WorkTimeAggregateRoot implements Cloneable{

	/** The company id. */
	// 会社ID
	private String companyId;

	/** The work time code. */
	// 就業時間帯コード
	private WorkTimeCode workTimeCode;

	/** The core time setting. */
	// コアタイム時間帯
	private CoreTimeSetting coreTimeSetting;

	/** The rest setting. */
	// 休憩設定
	private FlowWorkRestSetting restSetting;

	/** The offday work time. */
	// 休日勤務時間帯
	private FlexOffdayWorkTime offdayWorkTime;

	/** The common setting. */
	// 共通設定
	private WorkTimezoneCommonSet commonSetting;

	/** The use half day shift. */
	// 半日用シフトを使用する
	private boolean useHalfDayShift;

	/** The lst half day work timezone. */
	// 平日勤務時間帯
	private List<FlexHalfDayWorkTime> lstHalfDayWorkTimezone;

	/** The lst stamp reflect timezone. */
	// 打刻反映時間帯
	private List<StampReflectTimezone> lstStampReflectTimezone;

	/** The calculate setting. */
	// 計算設定
	private FlexCalcSetting calculateSetting;
	
	/**
	 * Instantiates a new flex work setting.
	 *
	 * @param memento the memento
	 */
	public FlexWorkSetting(FlexWorkSettingGetMemento memento) {
		this.companyId = memento.getCompanyId();
		this.workTimeCode = memento.getWorkTimeCode();
		this.coreTimeSetting = memento.getCoreTimeSetting();
		this.restSetting = memento.getRestSetting();
		this.offdayWorkTime = memento.getOffdayWorkTime();
		this.commonSetting = memento.getCommonSetting();
		this.useHalfDayShift = memento.getUseHalfDayShift();
		this.lstHalfDayWorkTimezone = memento.getLstHalfDayWorkTimezone();
		this.lstStampReflectTimezone = memento.getLstStampReflectTimezone();
		this.calculateSetting = memento.getCalculateSetting();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(FlexWorkSettingSetMemento memento){
		memento.setcompanyId(this.companyId);
		memento.setWorkTimeCode(this.workTimeCode);
		memento.setCoreTimeSetting(this.coreTimeSetting);
		memento.setRestSetting(this.restSetting);
		memento.setOffdayWorkTime(this.offdayWorkTime);
		memento.setCommonSetting(this.commonSetting);
		memento.setUseHalfDayShift(this.useHalfDayShift);
		memento.setLstHalfDayWorkTimezone(this.lstHalfDayWorkTimezone);
		memento.setLstStampReflectTimezone(this.lstStampReflectTimezone);
		memento.setCalculateSetting(this.calculateSetting);
	}
	
	/**
	 * Restore data.
	 *
	 * @param screenMode the screen mode
	 * @param workTimeType the work time type
	 * @param other the other
	 */
	public void correctData(ScreenMode screenMode, WorkTimeDivision workTimeType, FlexWorkSetting other) {
		// Dialog J: list stamp timezone
		Map<Entry<WorkNo, GoLeavingWorkAtr>, StampReflectTimezone> mapStampReflectTimezone = other.getLstStampReflectTimezone().stream()
				.collect(Collectors.toMap(
						item -> new ImmutablePair<WorkNo, GoLeavingWorkAtr>(item.getWorkNo(), item.getClassification()), 
						Function.identity()));
		this.lstStampReflectTimezone.forEach(item -> item.correctData(screenMode, mapStampReflectTimezone.get(
				new ImmutablePair<WorkNo, GoLeavingWorkAtr>(item.getWorkNo(), item.getClassification()))));
		
		this.commonSetting.correctData(screenMode, other.getCommonSetting());
		
		this.offdayWorkTime.correctData(screenMode, other);
		
		this.coreTimeSetting.correctData(screenMode, other.getCoreTimeSetting());
		//for dialog H
		this.restSetting.correctData(screenMode, other.getRestSetting(), this.getLstHalfDayWorkTimezone().size() > 0
				? this.getLstHalfDayWorkTimezone().get(0).getRestTimezone().isFixRestTime() : false);
	}
	
	/**
	 * Restore default data.
	 *
	 * @param screenMode the screen mode
	 */
	public void correctDefaultData(ScreenMode screenMode) {
		// Dialog J: list stamp timezone
		this.lstStampReflectTimezone.forEach(item -> item.correctDefaultData(screenMode));
		
		this.commonSetting.correctDefaultData(screenMode);
		
		this.coreTimeSetting.correctDefaultData(screenMode);
		
		//for dialog H
		this.restSetting.correctDefaultData(screenMode,this.getLstHalfDayWorkTimezone().size() > 0
				? this.getLstHalfDayWorkTimezone().get(0).getRestTimezone().isFixRestTime() : false);
	}

	/**
 	 * create this Instance
	 * @return new Instance
	 */
	@Override
	public FlexWorkSetting clone() {
		FlexWorkSetting cloned = new FlexWorkSetting();
		try {
			cloned.companyId = this.companyId;
			cloned.workTimeCode = new WorkTimeCode(this.workTimeCode.v());
			cloned.coreTimeSetting = this.coreTimeSetting.clone();
			cloned.restSetting = this.restSetting.clone();
			cloned.offdayWorkTime = this.offdayWorkTime.clone();
			cloned.commonSetting = this.commonSetting.clone();
			cloned.useHalfDayShift = this.useHalfDayShift ? true : false ;
			cloned.lstHalfDayWorkTimezone = this.lstHalfDayWorkTimezone.stream().map(c -> c.clone()).collect(Collectors.toList());
			cloned.lstStampReflectTimezone = this.lstStampReflectTimezone.stream().map(c -> c.clone()).collect(Collectors.toList());
			cloned.calculateSetting = this.calculateSetting.clone();
		}
		catch (Exception e){
			throw new RuntimeException("AggregateTotalTimeSpentAtWork clone error.");
		}
		return cloned;
	}
	
}
