package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.val;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.MidNightTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.GoingOutReason;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
//import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.common.timerounding.Rounding;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.common.timerounding.Unit;
import nts.uk.ctx.at.shared.dom.shortworktime.ChildCareAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.DeductGoOutRoundingSet;
import nts.uk.ctx.at.shared.dom.worktime.common.GoOutTimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.GoOutTypeRoundingSet;
import nts.uk.ctx.at.shared.dom.worktime.common.LateEarlyAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.RestClockManageAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.RestTimeOfficeWorkCalcMethod;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneGoOutSet;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeMethodSet;
import nts.uk.shr.com.time.TimeWithDayAttr;
/**
 * 控除項目の時間帯
 * @author keisuke_hoshina
 *
 */
@Getter
public class TimeSheetOfDeductionItem extends CalculationTimeSheet{
	//勤務間区分
	private WorkingBreakTimeAtr workingBreakAtr;
	//外出理由
	private Finally<GoingOutReason> goOutReason;
	//休憩種別
	private Finally<BreakClassification> breakAtr;
	//短時間勤務時間帯区分
	private Optional<ShortTimeSheetAtr> shortTimeSheetAtr;
	//控除区分
	private final DeductionClassification deductionAtr;
	//育児介護区分
	private Optional<ChildCareAtr> childCareAtr;
	
	/**
	 * 控除項目の時間帯作成(育児介護区分対応版)
	 * @param timeSpan
	 * @param goOutReason
	 * @param breakAtr
	 * @param deductionAtr
	 * @param withinStatutoryAtr
	 */
	private TimeSheetOfDeductionItem(TimeZoneRounding timeSheet, TimeSpanForCalc calcrange,
			List<TimeSheetOfDeductionItem> recorddeductionTimeSheets,
			List<TimeSheetOfDeductionItem> deductionTimeSheets, List<BonusPayTimeSheetForCalc> bonusPayTimeSheet,
			List<SpecBonusPayTimeSheetForCalc> specifiedBonusPayTimeSheet,
			Optional<MidNightTimeSheetForCalc> midNighttimeSheet,
			WorkingBreakTimeAtr workingBreakAtr,
			Finally<GoingOutReason> goOutReason,
			Finally<BreakClassification> breakAtr, Optional<ShortTimeSheetAtr> shortTimeSheetAtr,
			DeductionClassification deductionAtr,Optional<ChildCareAtr> childCareAtr) {
		super(timeSheet, calcrange, recorddeductionTimeSheets, deductionTimeSheets, bonusPayTimeSheet,
				specifiedBonusPayTimeSheet, midNighttimeSheet);
		this.workingBreakAtr = workingBreakAtr;
		this.goOutReason = goOutReason;
		this.breakAtr = breakAtr;
		this.shortTimeSheetAtr = shortTimeSheetAtr;
		this.deductionAtr = deductionAtr;
		this.childCareAtr = childCareAtr;
	}
	
	/**
	 * 控除項目の時間帯作成　
	 * @param timeSpan
	 * @param goOutReason
	 * @param breakAtr
	 * @param deductionAtr
	 * @param withinStatutoryAtr
	 * @return
	 */
	public static TimeSheetOfDeductionItem createTimeSheetOfDeductionItemAsFixed(TimeZoneRounding withRounding
			,TimeSpanForCalc timeSpan
			,List<TimeSheetOfDeductionItem> recorddeductionTimeSheets
			,List<TimeSheetOfDeductionItem> deductionTimeSheets
			,List<BonusPayTimeSheetForCalc> bonusPayTimeSheet
			,List<SpecBonusPayTimeSheetForCalc> specifiedBonusPayTimeSheet
			,Optional<MidNightTimeSheetForCalc> midNighttimeSheet
			,WorkingBreakTimeAtr workingBreakAtr
			,Finally<GoingOutReason> goOutReason
			,Finally<BreakClassification> breakAtr
			,Optional<ShortTimeSheetAtr> shortTimeSheetAtr
			,DeductionClassification deductionAtr
			,Optional<ChildCareAtr> childCareAtr) {
		
		return new TimeSheetOfDeductionItem(
				withRounding
				,timeSpan
				,recorddeductionTimeSheets
				,deductionTimeSheets
				,bonusPayTimeSheet
				,specifiedBonusPayTimeSheet
				,midNighttimeSheet
				,workingBreakAtr
				,goOutReason
				,breakAtr
				,shortTimeSheetAtr
				,deductionAtr
				,childCareAtr
				);
	}
	
	/**
	 * 受けとった計算範囲で時間を入れ替える
	 * @param timeSpan　時間帯
	 * @return　控除項目の時間帯
	 */
	public TimeSheetOfDeductionItem replaceTimeSpan(Optional<TimeSpanForCalc> timeSpan) {
		if(timeSpan.isPresent()) {
			return new TimeSheetOfDeductionItem(
											new TimeZoneRounding(timeSpan.get().getStart(), timeSpan.get().getEnd(), this.timeSheet.getRounding()),
											timeSpan.get(),
											this.recordedTimeSheet,
											this.deductionTimeSheet,
											this.bonusPayTimeSheet,
											this.specBonusPayTimesheet,
											this.midNightTimeSheet,
											this.workingBreakAtr,
											this.goOutReason,
											this.breakAtr,
											this.shortTimeSheetAtr,
											this.deductionAtr,
											this.childCareAtr
											);
		}
		else {
			return new TimeSheetOfDeductionItem(
					new TimeZoneRounding(this.start(), this.start(), this.timeSheet.getRounding()),
					new TimeSpanForCalc(this.start(), this.start()),
					this.recordedTimeSheet,
					this.deductionTimeSheet,
					this.bonusPayTimeSheet,
					this.specBonusPayTimesheet,
					this.midNightTimeSheet,
					this.workingBreakAtr,
					this.goOutReason,
					this.breakAtr,
					this.shortTimeSheetAtr,
					this.deductionAtr,
					this.childCareAtr
					);
		}
	}
	
	/**
	 * 範囲を比較したい対象
	 * @return
	 */
	public Optional<TimeSheetOfDeductionItem> createDuplicateRange(TimeSpanForCalc timeSpan) {
		//重複範囲取得
		val duplicateSpan = timeSpan.getDuplicatedWith(this.calcrange);
		//重複有
		if(duplicateSpan.isPresent())
			return Optional.of(this.replaceTimeSpan(duplicateSpan));
		//重複無
		return Optional.empty();
	}
	
	/**
	 * 控除項目の時間帯の法定内区分を法定外へ置き換える
	 * @return 法定内区分を法定外に変更した控除項目の時間帯
	 */
	public TimeSheetOfDeductionItem StatutoryAtrFromWithinToExcess() {
		return new TimeSheetOfDeductionItem(this.timeSheet, 
											this.calcrange, 
											this.recordedTimeSheet,
											this.deductionTimeSheet, 
											this.bonusPayTimeSheet,
											this.specBonusPayTimesheet, 
											this.midNightTimeSheet,
											this.workingBreakAtr,
											this.goOutReason, 
											this.breakAtr,
											this.shortTimeSheetAtr,
											this.deductionAtr,
											this.childCareAtr);
	}
	
	/**
	 * 控除時間帯と控除時間帯の重複チェック
	 * @param baseTimeSheet 現ループ中のリスト　
	 * @param compareTimeSheet　次のループで取り出すリスト　
	 */
	public List<TimeSheetOfDeductionItem> DeplicateBreakGoOut(TimeSheetOfDeductionItem compareTimeSheet,WorkTimeMethodSet setMethod,RestClockManageAtr clockManage
															,boolean useFixedRestTime,FluidFixedAtr fluidFixedAtr,WorkTimeDailyAtr workTimeDailyAtr) {
		List<TimeSheetOfDeductionItem> map = new ArrayList<TimeSheetOfDeductionItem>();
		List<TimeSpanForCalc> baseThisNotDupSpan = this.timeSheet.getTimeSpan().getNotDuplicationWith(compareTimeSheet.timeSheet.getTimeSpan());
		List<TimeSpanForCalc> baseCompareNotDupSpan = compareTimeSheet.timeSheet.getTimeSpan().getNotDuplicationWith(this.timeSheet.getTimeSpan());
		
		/*両方とも育児　*/
		/*if文の中身を別メソッドに実装する*/
		if(this.getDeductionAtr().isChildCare() && compareTimeSheet.getDeductionAtr().isChildCare()) {
			map.add(this);
			map.addAll(baseCompareNotDupSpan.stream().map(tc -> compareTimeSheet.replaceTimeSpan(Optional.of(tc))).collect(Collectors.toList()));
			return map;
		}
		/*前半育児　　後半外出*/
		else if(this.getDeductionAtr().isChildCare() && compareTimeSheet.getDeductionAtr().isGoOut()) {
			map.add(this);
			map.addAll(baseCompareNotDupSpan.stream().map(tc -> compareTimeSheet.replaceTimeSpan(Optional.of(tc))).collect(Collectors.toList()));
			return map;
		}
		/*前半外出、、後半育児*/
		else if(this.getDeductionAtr().isGoOut() && compareTimeSheet.getDeductionAtr().isChildCare()) {
			map.addAll(baseThisNotDupSpan.stream().map(tc -> this.replaceTimeSpan(Optional.of(tc))).collect(Collectors.toList()));
			map.add(compareTimeSheet);
			return map;
		}
		
		/*前半休憩、後半外出*/
		else if((this.getDeductionAtr().isBreak() && compareTimeSheet.getDeductionAtr().isGoOut())){
			if(!fluidFixedAtr.isFluidWork()) {
				TimeSpanForCalc duplicationSpan = this.getTimeSheet().getTimeSpan().getDuplicatedWith(compareTimeSheet.getCalcrange()).get();
				//休憩を削る
				map.add(this);
				
				//外出と被っている休憩を控除時間帯側の外出へ入れる
				if(compareTimeSheet.deductionTimeSheet == null
					|| compareTimeSheet.deductionTimeSheet.isEmpty()){
					compareTimeSheet.deductionTimeSheet = 
							Arrays.asList(new TimeSheetOfDeductionItem(new TimeZoneRounding(duplicationSpan.getStart(), duplicationSpan.getEnd(), null)
																	   , duplicationSpan
																	   , Collections.emptyList()
																	   , Collections.emptyList()
																	   , Collections.emptyList()
																	   , Collections.emptyList()
																	   , Optional.empty()
																	   , this.workingBreakAtr
																	   , this.getGoOutReason()
																	   , this.breakAtr
																	   , Optional.empty()
																	   , this.getDeductionAtr()
																	   , Optional.empty()));
				}
				else {
					compareTimeSheet.deductionTimeSheet.add(new TimeSheetOfDeductionItem(new TimeZoneRounding(duplicationSpan.getStart(), duplicationSpan.getEnd(), null)
							 , duplicationSpan
							 , Collections.emptyList()
							 , Collections.emptyList()
							 , Collections.emptyList()
							 , Collections.emptyList()
							 , Optional.empty()
							 , this.workingBreakAtr
							 , this.getGoOutReason()
							 , this.breakAtr
							 , Optional.empty()
							 , this.getDeductionAtr()
							 , Optional.empty()));
				}

				//外出と被っている休憩を計上時間帯側の外出へ入れる
				if(compareTimeSheet.recordedTimeSheet == null
					|| compareTimeSheet.recordedTimeSheet.isEmpty()){
					compareTimeSheet.recordedTimeSheet = 
							Arrays.asList(new TimeSheetOfDeductionItem(new TimeZoneRounding(duplicationSpan.getStart(), duplicationSpan.getEnd(), null)
																	   , duplicationSpan
																	   , Collections.emptyList()
																	   , Collections.emptyList()
																	   , Collections.emptyList()
																	   , Collections.emptyList()
																	   , Optional.empty()
																	   , this.workingBreakAtr
																	   , this.getGoOutReason()
																	   , this.breakAtr
																	   , Optional.empty()
																	   , this.getDeductionAtr()
																	   , Optional.empty()));
				}
				else {
					compareTimeSheet.recordedTimeSheet.add(new TimeSheetOfDeductionItem(new TimeZoneRounding(duplicationSpan.getStart(), duplicationSpan.getEnd(), null)
							 , duplicationSpan
							 , Collections.emptyList()
							 , Collections.emptyList()
							 , Collections.emptyList()
							 , Collections.emptyList()
							 , Optional.empty()
							 , this.getWorkingBreakAtr()
							 , this.getGoOutReason()
							 , this.breakAtr
							 , Optional.empty()
							 , this.getDeductionAtr()
							 , Optional.empty()));
				}
				//後半の外出を入れる
				map.add(compareTimeSheet);
				return map;
			}
			else {
				if(setMethod.isFluidWork()) {
					if(!useFixedRestTime) {
						if(clockManage.isNotClockManage()) {
							return collectionBreakTime(this,compareTimeSheet);
						}
					}
				}
			}
			//前半休憩
			map.add(this);
			//後半外出
			map.addAll(baseCompareNotDupSpan.stream().map(tc -> compareTimeSheet.replaceTimeSpan(Optional.of(tc))).collect(Collectors.toList()));
			return map;
		}
		/*前半外出、後半休憩*/
		else if(this.getDeductionAtr().isGoOut() && compareTimeSheet.getDeductionAtr().isBreak()){
			if(!fluidFixedAtr.isFluidWork()) {
				
				TimeSpanForCalc duplicationSpan = compareTimeSheet.getTimeSheet().getTimeSpan().getDuplicatedWith(this.getCalcrange()).get();
				//外出を入れる
				
				//外出の控除時間帯へ休憩を入れる
				this.deductionTimeSheet.add(new TimeSheetOfDeductionItem(new TimeZoneRounding(duplicationSpan.getStart(), duplicationSpan.getEnd(), null)
																		, duplicationSpan
																		, new ArrayList<>()
																		, new ArrayList<>()
																		, new ArrayList<>()
																		, new ArrayList<>()
																		, Optional.empty()
																		, compareTimeSheet.getWorkingBreakAtr()
																		, compareTimeSheet.getGoOutReason()
																		, compareTimeSheet.getBreakAtr()
																		, Optional.empty()
																		, compareTimeSheet.getDeductionAtr()
																		, Optional.empty()));
				//外出の計上時間帯へ休憩を入れる
				this.recordedTimeSheet.add(new TimeSheetOfDeductionItem(new TimeZoneRounding(duplicationSpan.getStart(), duplicationSpan.getEnd(), null)
																		, duplicationSpan
																		, new ArrayList<>()
																		, new ArrayList<>()
																		, new ArrayList<>()
																		, new ArrayList<>()
																		, Optional.empty()
																		, compareTimeSheet.getWorkingBreakAtr()
																		, compareTimeSheet.getGoOutReason()
																		, compareTimeSheet.getBreakAtr()
																		, Optional.empty()
																		, compareTimeSheet.getDeductionAtr()
																		, Optional.empty()));

			}
			else {
				if(setMethod.isFluidWork()||workTimeDailyAtr.isFlex()) {
					if(!useFixedRestTime) {
						if(clockManage.isNotClockManage()) {
							return collectionBreakTime(compareTimeSheet,this);
						}
					}
				}
			}
			//外出入れる
			map.add(this);
			//休憩を入れる
			map.add(compareTimeSheet);
			return map;
		}
		/*休憩系と休憩系*/
		else if(this.getDeductionAtr().isBreak() && compareTimeSheet.getDeductionAtr().isBreak()) {
			/*前半休憩、後半休憩打刻*/
			if(this.getBreakAtr().get().isBreak() && compareTimeSheet.getBreakAtr().get().isBreakStamp()) {
				map.addAll(baseThisNotDupSpan.stream().map(tc -> this.replaceTimeSpan(Optional.of(tc))).collect(Collectors.toList()));
				map.add(compareTimeSheet);
				return map;
			}
			/*前半休憩打刻、後半休憩*/
			else if((this.getBreakAtr().get().isBreakStamp() && compareTimeSheet.getBreakAtr().get().isBreak())){
				map.add(this);
				map.addAll(baseCompareNotDupSpan.stream().map(tc -> compareTimeSheet.replaceTimeSpan(Optional.of(tc))).collect(Collectors.toList()));
				return map;
			}
			/*休憩と休憩　→　育児と育児の重複と同じにする(後ろにある時間の開始を前の終了に合わせる)*/
			else if(this.getBreakAtr().get().isBreak() && compareTimeSheet.getBreakAtr().get().isBreak()) {
				map.add(this);
				if(baseCompareNotDupSpan!= null) {
					map.addAll(baseCompareNotDupSpan.stream().map(tc -> compareTimeSheet.replaceTimeSpan(Optional.of(tc))).collect(Collectors.toList()));
				}
				else {
					map.add(compareTimeSheet.replaceTimeSpan(Optional.of(new TimeSpanForCalc(this.timeSheet.getStart(),this.timeSheet.getStart()))));
				}
				
				return map;
			}
		}
		
		//前半育児　後半休憩
		else if(this.getDeductionAtr().isChildCare() && compareTimeSheet.getDeductionAtr().isBreak()) {
			//育児に被っている休憩の範囲を取得
			Optional<TimeSpanForCalc> duplicationSpan = this.getTimeSheet().getTimeSpan().getDuplicatedWith(compareTimeSheet.getCalcrange());
			
			if(duplicationSpan.isPresent()) {
				//育児の控除時間帯へ休憩を入れる
				this.deductionTimeSheet.add(new TimeSheetOfDeductionItem(new TimeZoneRounding(duplicationSpan.get().getStart(), duplicationSpan.get().getEnd(), null)
																	, duplicationSpan.get()
																	, Collections.emptyList()
																	, Collections.emptyList()
																	, Collections.emptyList()
																	, Collections.emptyList()
																	, Optional.empty()
																	, compareTimeSheet.getWorkingBreakAtr()
																	, compareTimeSheet.getGoOutReason()
																	, compareTimeSheet.getBreakAtr()
																	, Optional.empty()
																	, compareTimeSheet.getDeductionAtr()
																	, Optional.empty()));
				//育児の計上時間帯へ休憩を入れる
				this.recordedTimeSheet.add(new TimeSheetOfDeductionItem(new TimeZoneRounding(duplicationSpan.get().getStart(), duplicationSpan.get().getEnd(), null)
																	, duplicationSpan.get()
																	, Collections.emptyList()
																	, Collections.emptyList()
																	, Collections.emptyList()
																	, Collections.emptyList()
																	, Optional.empty()
																	, compareTimeSheet.getWorkingBreakAtr()
																	, compareTimeSheet.getGoOutReason()
																	, compareTimeSheet.getBreakAtr()
																	, Optional.empty()
																	, compareTimeSheet.getDeductionAtr()
																	, Optional.empty()));
			}
			//育児入れる
			map.add(this);
			//休憩を入れる
			map.add(compareTimeSheet);
			return map;
		}
		//前半休憩　後半育児
		else if(this.getDeductionAtr().isBreak() && compareTimeSheet.getDeductionAtr().isChildCare()) {
			//育児に被っている休憩の範囲を取得
			Optional<TimeSpanForCalc> duplicationSpan = compareTimeSheet.getTimeSheet().getTimeSpan().getDuplicatedWith(this.getCalcrange());
			
			if(duplicationSpan.isPresent()) {
				//育児の控除時間帯へ休憩を入れる
				compareTimeSheet.deductionTimeSheet.add(new TimeSheetOfDeductionItem(new TimeZoneRounding(duplicationSpan.get().getStart(), duplicationSpan.get().getEnd(), null)
																	, duplicationSpan.get()
																	, Collections.emptyList()
																	, Collections.emptyList()
																	, Collections.emptyList()
																	, Collections.emptyList()
																	, Optional.empty()
																	, this.getWorkingBreakAtr()
																	, this.getGoOutReason()
																	, this.getBreakAtr()
																	, Optional.empty()
																	, this.getDeductionAtr()
																	, Optional.empty()));
				//外出の計上時間帯へ休憩を入れる
				compareTimeSheet.recordedTimeSheet.add(new TimeSheetOfDeductionItem(new TimeZoneRounding(duplicationSpan.get().getStart(), duplicationSpan.get().getEnd(), null)
																	, duplicationSpan.get()
																	, Collections.emptyList()
																	, Collections.emptyList()
																	, Collections.emptyList()
																	, Collections.emptyList()
																	, Optional.empty()
																	, this.getWorkingBreakAtr()
																	, this.getGoOutReason()
																	, this.getBreakAtr()
																	, Optional.empty()
																	, this.getDeductionAtr()
																	, Optional.empty()));
			}
		}
		map.add(this);
		map.add(compareTimeSheet);
		return map; 
	}
	
	
	
	/**
	 * 休憩と外出時間帯の重複部分を補正する
	 * @param frontBreakTimeSheet 休憩時間帯
	 * @param goOutTimeSheet　外出時間帯
	 * @return 補正後の休憩、外出時間帯
	 */
	public List<TimeSheetOfDeductionItem> collectionBreakTime(TimeSheetOfDeductionItem frontBreakTimeSheet, TimeSheetOfDeductionItem backGoOutTimeSheet){
		List<TimeSheetOfDeductionItem> returnList = new ArrayList<>();
		switch(frontBreakTimeSheet.calcrange.checkDuplication(backGoOutTimeSheet.calcrange)) {
		case CONNOTATE_ENDTIME://終了時間含む
		case SAME_SPAN://同じ期間
			returnList.add(frontBreakTimeSheet.replaceTimeSpan(Optional.of(frontBreakTimeSheet.calcrange.shiftAhead(frontBreakTimeSheet.calcrange.getDuplicatedWith(backGoOutTimeSheet.calcrange).get().lengthAsMinutes()))));
			returnList.add(backGoOutTimeSheet);
			return returnList;
		case CONTAINED://含まれている(べース側が短い)
			/*休憩を外出の後ろにずらす*/
			returnList.add(frontBreakTimeSheet.replaceTimeSpan(Optional.of(frontBreakTimeSheet.calcrange.shiftAhead(backGoOutTimeSheet.calcrange.getEnd().valueAsMinutes() - frontBreakTimeSheet.calcrange.getStart().valueAsMinutes()))));
			returnList.add(backGoOutTimeSheet);
		case CONTAINS://比較相手を含んでいる
		case CONNOTATE_BEGINTIME://開始時間を含む
			returnList.add(frontBreakTimeSheet.replaceTimeSpan(Optional.of(new TimeSpanForCalc(frontBreakTimeSheet.start(),backGoOutTimeSheet.start()))));
			returnList.add(backGoOutTimeSheet);
			returnList.add(frontBreakTimeSheet.replaceTimeSpan(Optional.of(new TimeSpanForCalc(backGoOutTimeSheet.end(),frontBreakTimeSheet.calcrange.getEnd().backByMinutes(backGoOutTimeSheet.calcrange.lengthAsMinutes())))));
			return returnList;
		case NOT_DUPLICATE://重複していない
			returnList.add(frontBreakTimeSheet);
			returnList.add(backGoOutTimeSheet);
			return returnList;
		default://例外
			throw new RuntimeException("unknown duplicate Atr" + frontBreakTimeSheet.calcrange.checkDuplication(backGoOutTimeSheet.calcrange));
		}
	}
	
	
	public TimeWithDayAttr start() {
		return ((CalculationTimeSheet)this).calcrange.getStart();
	}
	
	public TimeWithDayAttr end() {
		return ((CalculationTimeSheet)this).calcrange.getEnd();
	}
	
	public boolean contains(TimeWithDayAttr baseTime) {
		return ((CalculationTimeSheet)this).calcrange.contains(baseTime);
	}
	
	
	/**
	 * 終了時間と基準時間の早い方の時間を取得する
	 * @param basePoint　基準時間
	 * @return 時刻が早い方
	 */
	public TimeSpanForCalc decisionNewSpan(TimeSpanForCalc timeSpan,TimeWithDayAttr baseTime,boolean isDateBefore) {
		if(isDateBefore) {
			val endOclock = timeSpan.getEnd().lessThan(baseTime) ? timeSpan.getEnd() : baseTime; 
			return new TimeSpanForCalc(timeSpan.getStart(),endOclock);
		}
		else {
			val startOclock = baseTime.lessThan(timeSpan.getStart()) ? timeSpan.getStart() : baseTime;
			return new TimeSpanForCalc(startOclock,timeSpan.getEnd());
		}
	}
	
	/**
	 * 再帰中に自分自身を作り直す処理
	 * @param baseTime 分割をしたい中心の時間
	 * @param isDateBefore 開始時間～中心の時間までを切り出したい場合　true
	 * @return
	 */
	public TimeSheetOfDeductionItem reCreateOwn(TimeWithDayAttr baseTime,boolean isDateBefore) {
			List<TimeSheetOfDeductionItem>  recorddeductionTimeSheets = this.recreateDeductionItemBeforeBase(baseTime,isDateBefore,DeductionAtr.Appropriate);
			List<TimeSheetOfDeductionItem>  deductionTimeSheets = this.recreateDeductionItemBeforeBase(baseTime,isDateBefore,DeductionAtr.Deduction);
			List<BonusPayTimeSheetForCalc>  bonusPayTimeSheet = this.recreateBonusPayListBeforeBase(baseTime,isDateBefore);
			List<SpecBonusPayTimeSheetForCalc> specifiedBonusPayTimeSheet = this.recreateSpecifiedBonusPayListBeforeBase(baseTime, isDateBefore);
			Optional<MidNightTimeSheetForCalc>     midNighttimeSheet = this.recreateMidNightTimeSheetBeforeBase(baseTime,isDateBefore);
			TimeSpanForCalc renewSpan = decisionNewSpan(this.calcrange,baseTime,isDateBefore);
			return new TimeSheetOfDeductionItem(new TimeZoneRounding(renewSpan.getStart(), renewSpan.getEnd(), this.getTimeSheet().getRounding()),
												renewSpan,
												recorddeductionTimeSheets,
												deductionTimeSheets,
												bonusPayTimeSheet,
												specifiedBonusPayTimeSheet,
												midNighttimeSheet,
												this.workingBreakAtr,this.goOutReason,this.breakAtr,this.shortTimeSheetAtr,this.deductionAtr,this.childCareAtr);
	}
	
	/**
	 * 法定内区分を法定外にして自分自身を作り直す
	 */
	public TimeSheetOfDeductionItem createWithExcessAtr(){
		return new TimeSheetOfDeductionItem(this.getTimeSheet(),this.calcrange,this.recordedTimeSheet,this.deductionTimeSheet,this.bonusPayTimeSheet,this.specBonusPayTimesheet,this.midNightTimeSheet,this.workingBreakAtr,this.goOutReason,this.breakAtr,this.shortTimeSheetAtr,this.deductionAtr,this.childCareAtr);
	}
	
	/**
	 * 休憩時間帯の計算範囲の取得 
	 * @param timeList 出勤退勤の時間リスト
	 * @param calcMethod　休憩時間中に退勤した場合の計算方法
	 * @param deplicateoneTimeRange 1日の範囲と控除時間帯の重複部分
	 * @return
	 */
	public List<TimeSpanForCalc> getBreakCalcRange(List<TimeLeavingWork> timeList,RestTimeOfficeWorkCalcMethod calcMethod,Optional<TimeSpanForCalc> deplicateOneTimeRange) {
		if(!deplicateOneTimeRange.isPresent()) {
			return Collections.emptyList();
		}
		List<TimeSpanForCalc> timesheets = new ArrayList<TimeSpanForCalc>();
		for(TimeLeavingWork time : timeList) {
			Optional<TimeSpanForCalc> timeSpan = getIncludeAttendanceOrLeaveDuplicateTimeSheet(time, calcMethod, deplicateOneTimeRange.get());
			if(timeSpan.isPresent()) {
				timesheets.add(timeSpan.get());
			}
		}
		return timesheets;
	}
	
	/**
	 * 休憩時間帯に出勤、退勤が含まれているかの判定ののち重複時間帯の取得
	 * @param time 出退勤クラス
	 * @param calcMethod　休憩時間中に退勤した場合の計算方法
	 * @param oneDayRange 1日の範囲
	 * @return
	 */
	public Optional<TimeSpanForCalc> getIncludeAttendanceOrLeaveDuplicateTimeSheet(TimeLeavingWork time,RestTimeOfficeWorkCalcMethod calcMethod,TimeSpanForCalc oneDayRange) {
		
		TimeWithDayAttr newStart = oneDayRange.getStart();
		TimeWithDayAttr newEnd = oneDayRange.getEnd();
		
		//退勤時間を含んでいるかチェック
		if(oneDayRange.contains(time.getTimespan().getEnd())) {
			//出勤時間を含んでいるチェック
			if(oneDayRange.contains(time.getTimespan().getStart())){
				newStart = time.getTimespan().getStart();
			}
		
			switch(calcMethod) {
				//計上しない
				case NOT_APPROP_ALL:
					return Optional.empty();
				//全て計上
				case APPROP_ALL:
					return Optional.of(new TimeSpanForCalc(newStart,newEnd));
				//退勤時間まで計上
				case OFFICE_WORK_APPROP_ALL:
					return Optional.of(new TimeSpanForCalc(newStart,time.getTimespan().getEnd()));
				//例外
				default:
					throw new RuntimeException("unknown CalcMethodIfLeaveWorkDuringBreakTime:" + calcMethod);
			}
		}
		else
		{
			//1日の計算範囲と出退勤の重複範囲取得
			return oneDayRange.getDuplicatedWith(time.getTimespan());
		}
	}
	
	/**
	 * 控除区分と条件が一致しているかを判定
	 * @param atr　条件
	 * @return　一致している。
	 */
	public boolean checkIncludeCalculation(ConditionAtr atr) {
		if(this.deductionAtr.isBreak() && atr.isBreak()) {
			return true;
		}
		else if(this.deductionAtr.isGoOut() && this.goOutReason.isPresent() && this.goOutReason.get().equalReason(atr)) {
			return true;
		}
		else if(this.deductionAtr.isChildCare() &&(atr.isCare() || atr.isChild())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 逆丸めの付与
	 * @param rounding 基の丸め
	 * @param actualAtr　実働時間帯区分
	 * @param dedAtr　控除区分
	 * @param commonSet　就業時間帯　共通設定
	 */
	public void changeReverceRounding(TimeRoundingSetting rounding,ActualWorkTimeSheetAtr actualAtr,DeductionAtr dedAtr,
									  Optional<WorkTimezoneCommonSet> commonSetting) {
		if(!commonSetting.isPresent())
			return;
		val result = decisionAddRounding(rounding, actualAtr, dedAtr, commonSetting.get());
		if(result.isPresent()) {
			this.timeSheet = new TimeZoneRounding(this.timeSheet.getStart(), this.timeSheet.getEnd(),rounding);
		}
	}
	

	public Optional<TimeRoundingSetting> decisionAddRounding(TimeRoundingSetting rounding,ActualWorkTimeSheetAtr actualAtr,DeductionAtr dedAtr,
									WorkTimezoneCommonSet commonSet) {
		switch(this.getDeductionAtr()) {
		//休憩
		case BREAK:
			if(this.getWorkingBreakAtr().isWorking()) {
				return Optional.empty();
			}
			return Optional.of(rounding);
		//介護
		case CHILD_CARE:
			return getShortTimeRounding(dedAtr, commonSet,rounding);
		//外出
		case GO_OUT:
			if(actualAtr.isWithinWorkTime()) {
				return goOutingRoundingActual(commonSet.getGoOutSet(), actualAtr, dedAtr,rounding);
			}
			return Optional.of(new TimeRoundingSetting(Unit.ROUNDING_TIME_1MIN, Rounding.ROUNDING_DOWN));
		//計上無し
		case NON_RECORD:
			return Optional.of(rounding);
		default:
			throw new RuntimeException("Unknown DeductionAtr:"+this.getDeductionAtr());
		}
	}
	
	
	private Optional<TimeRoundingSetting> getShortTimeRounding(DeductionAtr dedAtr, WorkTimezoneCommonSet commonSet,TimeRoundingSetting rounding) {
		switch(dedAtr) {
			//計上
			case Appropriate:
			//控除
			case Deduction:
				if(this.getShortTimeSheetAtr().isPresent()) {
					switch(this.getShortTimeSheetAtr().get()) {
					//退勤後
					case AFTER_LEAVING:
						val afterLeaving = commonSet.getLateEarlySet().getOtherClassSets().stream().filter(tc -> tc.getLateEarlyAtr().equals(LateEarlyAtr.EARLY)).findFirst();
						return afterLeaving.isPresent()?Optional.of(afterLeaving.get().getDelTimeRoundingSet()):Optional.empty();
						//出勤前
					case BEFORE_ATTENDANCE:
						val beforeAttendance = commonSet.getLateEarlySet().getOtherClassSets().stream().filter(tc -> tc.getLateEarlyAtr().equals(LateEarlyAtr.LATE)).findFirst();
						return beforeAttendance.isPresent()?Optional.of(beforeAttendance.get().getDelTimeRoundingSet()):Optional.empty();
						//勤務中
					case WORKING_TIME:
						return goOutingRoundingActual(commonSet.getGoOutSet(),ActualWorkTimeSheetAtr.WithinWorkTime,dedAtr,rounding);
					default:
						throw new RuntimeException("Unknown shortTimeSheetAtr:"+ this.getShortTimeSheetAtr().get());
					}
				}
				else {
					return Optional.empty();
				}
			default:
				throw new RuntimeException("Unknown DeductionAtr:"+dedAtr);
		}
	}
	
	
	/**
	 * 就内・残業・休出どの設定を使用するか決定する
	 * @param goOutSet　外出設定
	 * @param actualAtr　実働時間帯区分
	 * @param dedAtr　控除区分
	 * @return　外出丸め設定
	 */
	private Optional<TimeRoundingSetting> goOutingRoundingActual(WorkTimezoneGoOutSet goOutSet,ActualWorkTimeSheetAtr actualAtr,DeductionAtr dedAtr,TimeRoundingSetting rounding){
		Optional<TimeRoundingSetting> returnValue = Optional.empty();
		if(goOutSet.getTotalRoundingSet().getSetSameFrameRounding().isRoundingAndTotal()) {
			switch(actualAtr) {
			//就業時間内
			case WithinWorkTime:
				returnValue = goOutingRounding(dedAtr,goOutSet.getDiffTimezoneSetting().getWorkTimezone(),rounding);
				break;
			//残業
			case EarlyWork://早出
			case OverTimeWork://普通
			case StatutoryOverTimeWork://法内
				returnValue = goOutingRounding(dedAtr,goOutSet.getDiffTimezoneSetting().getOttimezone(),rounding);
				break;
			//休出
			case HolidayWork:
				returnValue = goOutingRounding(dedAtr,goOutSet.getDiffTimezoneSetting().getPubHolWorkTimezone(),rounding);
				break;
			default:
				throw new RuntimeException("Unknown ActualAtr:"+actualAtr);
			}
		}
		return returnValue;
	}
	
	/**
	 * 外出理由からどの設定を参照するか決定する
	 * @param dedAtr 控除区分
	 * @param set　丸め設定
	 * @return 外出時丸め設定取得(逆丸め、丸め判定)
	 */
	private Optional<TimeRoundingSetting> goOutingRounding(DeductionAtr dedAtr,GoOutTypeRoundingSet set,TimeRoundingSetting rounding) {
		if(this.getGoOutReason() != null
		 &&this.getGoOutReason().isPresent()) {
			switch(this.getGoOutReason().get()) {
			case PRIVATE://私用
			case UNION://組合
				return Optional.of(goOutingRond(dedAtr,set.getPrivateUnionGoOut(), rounding));
			case COMPENSATION://公用
			case PUBLIC://有償
				return Optional.of(goOutingRond(dedAtr,set.getOfficalUseCompenGoOut(), rounding));
			default://例外
				throw new RuntimeException("Unknown GoOutReason:"+this.getGoOutReason().get());
			}
		}
		return Optional.empty();
	}
	
	/**
	 * 控除or計上どちらを使用する蚊決定する
	 * @param dedAtr 控除区分
	 * @param set 丸め設定
	 * @return　外出時丸め設定取得(逆丸め、丸め判定)
	 */
	private TimeRoundingSetting goOutingRond(DeductionAtr dedAtr,DeductGoOutRoundingSet set,TimeRoundingSetting rounding) {
		switch(dedAtr) {
		//計上
		case Appropriate:
			return getouting(set.getApproTimeRoundingSetting(),rounding);
		//控除
		case Deduction:
			return getouting(set.getDeductTimeRoundingSetting(),rounding);
		default:
			throw new RuntimeException("Unknown DedctionAtr:"+dedAtr);
		}
	}
	
	/**
	 * 外出時丸め設定取得(逆丸め、丸め判定)
	 * @param set 丸め設定
	 * @return　外出時丸め設定取得(逆丸め、丸め判定)
	 */
	private TimeRoundingSetting getouting(GoOutTimeRoundingSetting set,TimeRoundingSetting rounding) {
		switch(set.getRoundingMethod()) {
		//逆丸め
		case INDIVIDUAL_ROUNDING:
			//return set.getRoundingSetting().getReverseRounding();
			return rounding.getReverseRounding();
		//個別丸め
		case REVERSE_ROUNDING_EACH_TIMEZONE:
			return set.getRoundingSetting();
		default:
			throw new RuntimeException("Unknown GetRoundinMethod:"+set.getRoundingMethod());
		}
	}

	/**
	 * 短時間勤務時間帯の収集
	 * @return　自身と自身が持つ短時間勤務リスト
	 */
	public List<TimeSheetOfDeductionItem> collectShortTime() {
		List<TimeSheetOfDeductionItem> returnList = new ArrayList<>();
		if(this.getDeductionAtr().isChildCare()) {
			returnList.add(this);
		}
		returnList.addAll(this.collectShortTimeSheet());
		return returnList;
	}
	
	public static TimeSheetOfDeductionItem createFromDeductionTimeSheet(nts.uk.ctx.at.shared.dom.worktime.common.DeductionTime dTimeSheet) {
		return TimeSheetOfDeductionItem.createTimeSheetOfDeductionItemAsFixed(new TimeZoneRounding(dTimeSheet.getStart(),dTimeSheet.getEnd(), new TimeRoundingSetting(Unit.ROUNDING_TIME_1MIN, Rounding.ROUNDING_DOWN)),
				  new TimeSpanForCalc(dTimeSheet.getStart(), dTimeSheet.getEnd()),
				  Collections.emptyList(),
				  Collections.emptyList(),
				  Collections.emptyList(),
				  Collections.emptyList(),
				  Optional.empty(),
				  WorkingBreakTimeAtr.NOTWORKING,
				  Finally.empty(),
				  Finally.of(BreakClassification.BREAK),
				  Optional.empty(),
				  DeductionClassification.BREAK,
				  Optional.empty()
				  );
				
	}
}
