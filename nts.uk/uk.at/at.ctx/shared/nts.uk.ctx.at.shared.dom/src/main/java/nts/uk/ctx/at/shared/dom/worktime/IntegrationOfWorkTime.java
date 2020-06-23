package nts.uk.ctx.at.shared.dom.worktime;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.CommonRestSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.DeductionTime;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimezoneNo;
import nts.uk.ctx.at.shared.dom.worktime.common.FixedRestCalculateMethod;
import nts.uk.ctx.at.shared.dom.worktime.common.HDWorkTimeSheetSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixOffdayWorkTimezone;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixRestTimezoneSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkRestSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkRestSettingDetail;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkRestTimezone;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeAggregateRoot;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeForm;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktype.AttendanceHolidayAttr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

/**
 * 統合就業時間帯
 * @author daiki_ichioka
 *
 */
@Getter
@Setter
public class IntegrationOfWorkTime {

	/** 就業時間帯コード */
	private WorkTimeCode code;
	
	/** 就業時間帯の設定 */
	private WorkTimeSetting workTimeSetting;
	
	/** 固定勤務設定 */
	private Optional<FixedWorkSetting> fixedWorkSetting;
	
	/** フレックス勤務設定 */
	private Optional<FlexWorkSetting> flexWorkSetting;
	
	/** 流動勤務設定 */
	private Optional<FlowWorkSetting> flowWorkSetting;
	
	
	/**
	 * Constructor（通常勤務）
	 * @param workTimeCode 就業時間帯コード
	 * @param workTimeSetting 就業時間帯の設定
	 * @param fixedWorkSetting 固定勤務設定
	 */
	public IntegrationOfWorkTime(WorkTimeCode workTimeCode, WorkTimeSetting workTimeSetting, FixedWorkSetting fixedWorkSetting) {
		this.code = workTimeCode;
		this.workTimeSetting = workTimeSetting;
		this.fixedWorkSetting = Optional.of(fixedWorkSetting);
		this.flexWorkSetting = Optional.empty();
		this.flowWorkSetting = Optional.empty();
		this.existsWorkSetting();
	}
	
	/**
	 * Constructor（フレックス勤務）
	 * @param workTimeCode 就業時間帯コード
	 * @param workTimeSetting 就業時間帯の設定
	 * @param fixedWorkSetting フレックス勤務設定
	 */
	public IntegrationOfWorkTime(WorkTimeCode workTimeCode, WorkTimeSetting workTimeSetting, FlexWorkSetting flexWorkSetting) {
		this.code = workTimeCode;
		this.workTimeSetting = workTimeSetting;
		this.fixedWorkSetting = Optional.empty();
		this.flexWorkSetting = Optional.of(flexWorkSetting);
		this.flowWorkSetting = Optional.empty();
		this.existsWorkSetting();
	}
	
	/**
	 * Constructor（流動勤務）
	 * @param workTimeCode 就業時間帯コード
	 * @param workTimeSetting 就業時間帯の設定
	 * @param fixedWorkSetting 流動勤務設定
	 */
	public IntegrationOfWorkTime(WorkTimeCode workTimeCode, WorkTimeSetting workTimeSetting, FlowWorkSetting flowWorkSetting) {
		this.code = workTimeCode;
		this.workTimeSetting = workTimeSetting;
		this.fixedWorkSetting = Optional.empty();
		this.flexWorkSetting = Optional.empty();
		this.flowWorkSetting = Optional.of(flowWorkSetting);
		this.existsWorkSetting();
	}
	
	/**
	 * 勤務形態に対応する勤務設定が存在しなければならない
	 * （存在しない場合はRunTimeException）
	 */
	private void existsWorkSetting() {
		switch(this.workTimeSetting.getWorkTimeDivision().getWorkTimeForm()) {
			case FIXED:	
				if(!this.fixedWorkSetting.isPresent())
					throw new RuntimeException("Empty FixedWorkSetting");
				break;
			case FLEX:
				if(!this.flexWorkSetting.isPresent())
					throw new RuntimeException("Empty FlexWorkSetting");
				break;
			case FLOW:
				if(!this.flowWorkSetting.isPresent())
					throw new RuntimeException("Empty FlowWorkSetting");
				break;
			case TIMEDIFFERENCE:
				throw new RuntimeException("Unimplemented");/*時差勤務はまだ実装しない。2020/5/19 渡邉*/
			default:
				throw new RuntimeException("Non-conformity No Work");
		}
	}
	
	/**
	 * 就業時間帯の共通設定を取得する
	 * @return 就業時間帯の共通設定
	 */
	public WorkTimezoneCommonSet getCommonSetting() {
		switch(this.workTimeSetting.getWorkTimeDivision().getWorkTimeForm()) {
			case FIXED:				return this.fixedWorkSetting.get().getCommonSetting();
			case FLEX:				return this.flexWorkSetting.get().getCommonSetting();
			case FLOW:				return this.flowWorkSetting.get().getCommonSetting();
			case TIMEDIFFERENCE:	throw new RuntimeException("Unimplemented");/*時差勤務はまだ実装しない。2020/5/19 渡邉*/
			default:				throw new RuntimeException("Non-conformity No Work");
		}
	}
	
	/**
	 * 残業時間の時間帯設定を取得する
	 * @param workType 勤務種類
	 * @return 残業時間の時間帯設定
	 */
	public List<OverTimeOfTimeZoneSet> getOverTimeOfTimeZoneSetList(WorkType workType) {
		switch(this.workTimeSetting.getWorkTimeDivision().getWorkTimeForm()) {
			case FIXED:				return this.fixedWorkSetting.get().getOverTimeOfTimeZoneSet(workType);
			case FLEX:				return this.flexWorkSetting.get().getOverTimeOfTimeZoneSet(workType);
			case FLOW:				throw new RuntimeException("Non-conformity FLOW_WORK");
			case TIMEDIFFERENCE:	throw new RuntimeException("Unimplemented");/*時差勤務はまだ実装しない。2020/5/19 渡邉*/
			default:				throw new RuntimeException("Non-conformity No Work");
		}
	}
	
	/**
	 * 就業時間の時間帯設定を取得する
	 * @param workType 勤務種類
	 * @return 就業時間の時間帯設定
	 */
	public List<EmTimeZoneSet> getEmTimeZoneSetList(WorkType workType) {
		switch(this.workTimeSetting.getWorkTimeDivision().getWorkTimeForm()) {
			case FIXED:				return this.fixedWorkSetting.get().getEmTimeZoneSet(workType);
			case FLEX:				return this.flexWorkSetting.get().getEmTimeZoneSet(workType);
			case FLOW:				return Collections.emptyList();
			case TIMEDIFFERENCE:	throw new RuntimeException("Unimplemented");/*時差勤務はまだ実装しない。2020/5/19 渡邉*/
			default:				throw new RuntimeException("Non-conformity No Work");
		}
	}
	
	/**
	 * 休出時間の時間帯設定(List)を取得する
	 * @return 休出時間の時間帯設定(List)
	 */
	public List<HDWorkTimeSheetSetting> getHDWorkTimeSheetSettingList() {
		switch(this.workTimeSetting.getWorkTimeDivision().getWorkTimeForm()) {
			case FIXED:				return this.fixedWorkSetting.get().getOffdayWorkTimezone().getLstWorkTimezone();
			case FLEX:				return this.flexWorkSetting.get().getOffdayWorkTime().getLstWorkTimezone();
			case FLOW:				throw new RuntimeException("Non-conformity FLOW_WORK");
			case TIMEDIFFERENCE:	throw new RuntimeException("Unimplemented");/*時差勤務はまだ実装しない。2020/5/19 渡邉*/
			default:				throw new RuntimeException("Non-conformity No Work");
		}
	}
	
	/**
	 * 法定内残業として扱うか判定する
	 * @return true：法定内残業として扱う false法定内残業として扱わない
	 */
	public boolean isLegalInternalTime() {
		switch(this.workTimeSetting.getWorkTimeDivision().getWorkTimeForm()) {
			case FIXED:				return this.fixedWorkSetting.get().getLegalOTSetting().isLegal();
			case FLEX:				return false;
			case FLOW:				return false;
			case TIMEDIFFERENCE:	throw new RuntimeException("Unimplemented");/*時差勤務はまだ実装しない。2020/5/19 渡邉*/
			default:				throw new RuntimeException("Non-conformity No Work");
		}
	}
	
	/**
	 * コアタイム時間帯設定を取得する
	 * @return コアタイム時間帯設定
	 */
	public Optional<CoreTimeSetting> getCoreTimeSetting() {
		switch(this.workTimeSetting.getWorkTimeDivision().getWorkTimeForm()) {
			case FIXED:				return Optional.empty();
			case FLEX:				Optional.of(this.flexWorkSetting.get().getCoreTimeSetting());
			case FLOW:				return Optional.empty();
			case TIMEDIFFERENCE:	throw new RuntimeException("Unimplemented");/*時差勤務はまだ実装しない。2020/5/19 渡邉*/
			default:				throw new RuntimeException("Non-conformity No Work");
		}
	}
	
	/**
	 * 流動勤務の休憩設定を取得する
	 * フレックス勤務、流動勤務以外はRunTimeException
	 * @return 流動勤務の休憩設定
	 */
	public FlowWorkRestSetting getFlowWorkRestSetting() {
		switch(this.workTimeSetting.getWorkTimeDivision().getWorkTimeForm()) {
			case FIXED:				throw new RuntimeException("Non-conformity FIXED_WORK");
			case FLEX:				return this.flexWorkSetting.get().getRestSetting();
			case FLOW:				return this.flowWorkSetting.get().getRestSetting();
			case TIMEDIFFERENCE:	throw new RuntimeException("Unimplemented");/*時差勤務はまだ実装しない。2020/5/19 渡邉*/
			default:				throw new RuntimeException("Non-conformity No Work");
		}
	}
	
	/**
	 * 流動勤務の休憩設定詳細を取得する
	 * 固定勤務の場合はOptional.empty()を返す為、要注意
	 * @return 流動勤務の休憩設定詳細
	 */
	public Optional<FlowWorkRestSettingDetail> getFlowWorkRestSettingDetail() {
		switch(this.workTimeSetting.getWorkTimeDivision().getWorkTimeForm()) {
			case FIXED:				return Optional.empty();
			case FLEX:				return Optional.of(this.flexWorkSetting.get().getRestSetting().getFlowRestSetting());
			case FLOW:				return Optional.of(this.flowWorkSetting.get().getRestSetting().getFlowRestSetting());
			case TIMEDIFFERENCE:	throw new RuntimeException("Unimplemented");/*時差勤務はまだ実装しない。2020/5/19 渡邉*/
			default:				throw new RuntimeException("Non-conformity No Work");
		}
	}
	
	/**
	 * 流動勤務の休憩時間帯を取得する
	 * 固定勤務の場合はOptional.empty()を返す為、要注意
	 * @param workType 勤務種類
	 * @return 流動勤務の休憩時間帯
	 */
	public Optional<FlowWorkRestTimezone> getFlowWorkRestTimezone(WorkType workType) {
		switch(this.workTimeSetting.getWorkTimeDivision().getWorkTimeForm()) {
			case FIXED:				return Optional.empty();
			case FLEX:				return Optional.of(this.flexWorkSetting.get().getFlowWorkRestTimezone(workType));
			case FLOW:				return Optional.of(this.flowWorkSetting.get().getFlowWorkRestTimezone(workType));
			case TIMEDIFFERENCE:	throw new RuntimeException("Unimplemented");/*時差勤務はまだ実装しない。2020/5/19 渡邉*/
			default:				throw new RuntimeException("Non-conformity No Work");
		}
	}
	
	/**
	 * 固定休憩の計算方法を取得する
	 * フレックス勤務、流動勤務の場合はOptional.empty()を返す為、要注意
	 * @param workType
	 * @return 固定休憩の計算方法
	 */
	public Optional<FixedRestCalculateMethod> getFixedRestCalculateMethod() {
		switch(this.workTimeSetting.getWorkTimeDivision().getWorkTimeForm()) {
			case FIXED:				return Optional.of(this.fixedWorkSetting.get().getFixedWorkRestSetting().getCalculateMethod());
			case FLEX:				return Optional.empty();
			case FLOW:				return Optional.empty();
			case TIMEDIFFERENCE:	throw new RuntimeException("Unimplemented");/*時差勤務はまだ実装しない。2020/5/19 渡邉*/
			default:				throw new RuntimeException("Non-conformity No Work");
		}
	}
	
	/**
	 * 共通の休憩設定を取得する
	 * @return 共通の休憩設定
	 */
	public CommonRestSetting getCommonRestSetting() {
		switch(this.workTimeSetting.getWorkTimeDivision().getWorkTimeForm()) {
			case FIXED:				return this.fixedWorkSetting.get().getFixedWorkRestSetting().getCommonRestSet();
			case FLEX:				return this.flexWorkSetting.get().getRestSetting().getCommonRestSetting();
			case FLOW:				return this.flowWorkSetting.get().getRestSetting().getCommonRestSetting();
			case TIMEDIFFERENCE:	throw new RuntimeException("Unimplemented");/*時差勤務はまだ実装しない。2020/5/19 渡邉*/
			default:				throw new RuntimeException("Non-conformity No Work");
		}
	}
	
	/**
	 * 就業時間帯Noと法定内残業枠Noを取得する
	 * @param workType 勤務種類
	 * @return Map<就業時間帯No, 法定内の残業枠No>
	 */
	public Map<EmTimezoneNo, OverTimeFrameNo> getLegalOverTimeFrameNoMap(WorkType workType) {
		switch(this.workTimeSetting.getWorkTimeDivision().getWorkTimeForm()) {
			case FIXED:				return this.fixedWorkSetting.get().getLegalOverTimeFrameNoMap(workType);
			case FLEX:				return Collections.emptyMap();
			case FLOW:				return this.flowWorkSetting.get().getLegalOverTimeFrameNoMap();
			case TIMEDIFFERENCE:	throw new RuntimeException("Unimplemented");/*時差勤務はまだ実装しない。2020/5/19 渡邉*/
			default:				throw new RuntimeException("Non-conformity No Work");
		}
	}
	
	/**
	 * 法定内残業枠Noを取得する
	 * @param workType 勤務種類
	 * @return 法定内残業枠No(List)
	 */
	public List<OverTimeFrameNo> getLegalOverTimeFrameNoList(WorkType workType) {
		switch(this.workTimeSetting.getWorkTimeDivision().getWorkTimeForm()) {
			case FIXED:				return this.fixedWorkSetting.get().getLegalOverTimeFrameNoList(workType);
			case FLEX:				return Collections.emptyList();
			case FLOW:				return Collections.emptyList();
			case TIMEDIFFERENCE:	throw new RuntimeException("Unimplemented");/*時差勤務はまだ実装しない。2020/5/19 渡邉*/
			default:				throw new RuntimeException("Non-conformity No Work");
		}
	}
	
	/*
	 * 大塚カスタマイズ専用　遅刻早退の時間帯処理にて、出勤/退勤を変更したときに
	 * 休憩時間帯が消えてしまうので、就業時間帯から取得した休憩時間帯で計算できるように
	 * マスタから休憩時間帯を取得する。
	 * （流動休憩の事は考慮していないので、大塚カスタマイズ以外では使用禁止）
	 * @param workType 勤務種類
	 * @return 控除時間帯(丸め付き)(List)
	 * */
	public List<DeductionTime> getBreakTimeList(WorkType worktype){
		if(this.workTimeSetting.getWorkTimeDivision().isFlex()) {
			if(worktype.isWeekDayAttendance())
				return null;
			if(worktype.getDailyWork().getOneDay().isHolidayWork())
				return this.flexWorkSetting.get().getOffdayWorkTime().getRestTimezone().getFixedRestTimezone().getTimezones();
		}
		return Collections.emptyList();
	}
}
