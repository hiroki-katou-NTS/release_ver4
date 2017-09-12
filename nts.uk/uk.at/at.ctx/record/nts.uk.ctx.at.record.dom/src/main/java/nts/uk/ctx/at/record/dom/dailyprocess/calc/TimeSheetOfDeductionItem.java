package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.worktime.CommomSetting.CalcMethodIfLeaveWorkDuringBreakTime;
import nts.uk.ctx.at.shared.dom.worktime.basicinformation.SettingMethod;
import nts.uk.ctx.at.shared.dom.worktime.fixedworkset.set.FixRestCalcMethod;
import nts.uk.ctx.at.shared.dom.worktime.fixedworkset.timespan.TimeSpanWithRounding;
import nts.uk.ctx.at.shared.dom.worktime.fluidworkset.fluidbreaktimeset.BreakClockOfManageAtr;
import nts.uk.ctx.at.shared.dom.worktime.fluidworkset.fluidbreaktimeset.FluidBreakTimeOfCalcMethod;
import nts.uk.ctx.at.shared.dom.worktime.fluidworkset.fluidbreaktimeset.FluidPrefixBreakTimeOfCalcMethod;
import nts.uk.shr.com.time.TimeWithDayAttr;
/**
 * æ§é¤é ?®ã®æéå¸¯
 * @author keisuke_hoshina
 *
 */

@RequiredArgsConstructor

@Getter
public class TimeSheetOfDeductionItem extends CalculationTimeSheet{
	private Finally<StampGoOutReason> goOutReason;
	private Finally<BreakClassification> breakAtr;
	private final DedcutionClassification deductionAtr;
	private final WithinStatutoryAtr withinStatutoryAtr;
	
//	private final DedcutionClassification deductionClassification;
//	private final BreakClassification breakClassification;

	/**
	 * ä¼æ?æéå¸¯åå¾ææ ¼ç´ç¨
	 * @param timeSpan
	 * @param goOutReason
	 * @param breakAtr
	 * @param deductionAtr
	 * @param withinStatutoryAtr
	 */
	private TimeSheetOfDeductionItem(TimeSpanForCalc timeSpan
									,Finally<StampGoOutReason> goOutReason
									,Finally<BreakClassification> breakAtr
									,DedcutionClassification deductionAtr
									,WithinStatutoryAtr withinStatutoryAtr) {
		super(new TimeSpanWithRounding(timeSpan.getStart(),timeSpan.getEnd(),null),timeSpan);
		this.goOutReason = goOutReason;
		this.breakAtr = breakAtr;
		this.deductionAtr = deductionAtr;
		this.withinStatutoryAtr = withinStatutoryAtr;
	}
	
	/**
	 * åºå®å¤åã?ä¼æ?æéå¸¯åå¾?
	 * @param timeSpan
	 * @param goOutReason
	 * @param breakAtr
	 * @param deductionAtr
	 * @param withinStatutoryAtr
	 * @return
	 */
	public static TimeSheetOfDeductionItem reateBreakTimeSheetAsFixed(TimeSpanForCalc timeSpan
			,Finally<StampGoOutReason> goOutReason
			,Finally<BreakClassification> breakAtr
			,DedcutionClassification deductionAtr
			,WithinStatutoryAtr withinStatutoryAtr) {
		
		return new TimeSheetOfDeductionItem(
				timeSpan,
				goOutReason,
				breakAtr,
				deductionAtr,
				withinStatutoryAtr
				);
	}
	

	/**
	 * ä¼æ?ã¨å¤å?ã®éè¤?¤å®?
	 * @param baseTimeSheet ç¾ã«ã¼ãä¸­ã®ãªã¹ã?
	 * @param compareTimeSheetã?æ¬¡ã®ã«ã¼ãã§åãåºããªã¹ã?
	 */
	public Map<TimeSpanForCalc,Boolean> DeplicateBreakGoOut(TimeSheetOfDeductionItem compareTimeSheet,SettingMethod setMethod,BreakClockOfManageAtr clockManage) {
		Map<TimeSpanForCalc, Boolean> map = new HashMap<TimeSpanForCalc,Boolean>();
		/*ä¸¡æ¹ã¨ãå¤å?*/
		if(this.getDeductionAtr().isGoOut() && compareTimeSheet.getDeductionAtr().isGoOut()) {
			map.put(compareTimeSheet.calculationTimeSheet.getNotDuplicationWith(this.calculationTimeSheet).get(),Boolean.FALSE);
			return map;
		}
		/*ä¸¡æ¹ã¨ãè²å?*/
		else if(this.getDeductionAtr().isChildCare() && compareTimeSheet.getDeductionAtr().isChildCare()) {
			map.put(compareTimeSheet.calculationTimeSheet.getNotDuplicationWith(this.calculationTimeSheet).get(),Boolean.FALSE);
			return map;
		}
		/*ååè²åã?å¾åå¤å?*/
		else if(this.getDeductionAtr().isChildCare() && compareTimeSheet.getDeductionAtr().isGoOut()) {
			map.put(compareTimeSheet.calculationTimeSheet.getNotDuplicationWith(this.calculationTimeSheet).get(),Boolean.FALSE);
			return map;
		}
		/*ååå¤å?ãå¾åè²å?*/
		else if(this.getDeductionAtr().isGoOut() && compareTimeSheet.getDeductionAtr().isChildCare()) {
			map.put(this.calculationTimeSheet.getNotDuplicationWith(compareTimeSheet.calculationTimeSheet).get(),Boolean.FALSE);
			return map;
		}
		/*ååä¼æ?ãå¾åå¤å?*/
		else if((this.getDeductionAtr().isBreak() && compareTimeSheet.getDeductionAtr().isGoOut())){
			if(setMethod.isFluidWork()) {
				if(clockManage.isNotClockManage()) {
					map.put(compareTimeSheet.calculationTimeSheet.getNotDuplicationWith(this.calculationTimeSheet).get(),Boolean.FALSE);
					return map;
				}
				else {
					/*ã¢ã«ã´ãªãºã?ã®ãã¿ã¼ã³ä¸è¶³ãçºè¦ããããã?ãã£ããä¿ç*/
					map.put(compareTimeSheet.calculationTimeSheet.getNotDuplicationWith(this.calculationTimeSheet).get(),Boolean.FALSE);
					return map;
				}
			}
		}
		/*ååå¤å?ãå¾åä¼æ?*/
		else if(this.getDeductionAtr().isGoOut() && compareTimeSheet.getDeductionAtr().isBreak()){
			if(setMethod.isFluidWork()) {
				if(clockManage.isNotClockManage()) {
					map.put(compareTimeSheet.calculationTimeSheet.getNotDuplicationWith(this.calculationTimeSheet).get(),Boolean.FALSE);
					return map;
				}
				else
				{
					map.put(this.calculationTimeSheet.getNotDuplicationWith(compareTimeSheet.calculationTimeSheet).get(),Boolean.FALSE);
					return map;
				}
			}
			
		}
		else if(this.getDeductionAtr().isBreak() && compareTimeSheet.getDeductionAtr().isBreak()) {
			/*ååä¼æ?ãå¾åä¼æ?æå»*/
			if(this.getBreakAtr().isBreak() && compareTimeSheet.getBreakAtr().isBreakStamp()) {
				
			}
			/*ååä¼æ?æå»ãå¾åä¼æ?*/
			else if((this.getBreakAtr().isBreakStamp() && compareTimeSheet.getBreakAtr().isBreak())){
					
			}
			/*ä¸¡æ¹ã¨ãä¼æ?æå»*/
			else if(this.getBreakAtr().isBreakStamp() && compareTimeSheet.getBreakAtr().isBreakStamp()) {
				
			}
		}
		map.put(this.calculationTimeSheet,Boolean.FALSE);
		return map; 
	}
	
	
	public TimeWithDayAttr start() {
		return ((CalculationTimeSheet)this).calculationTimeSheet.getStart();
		//return this.span.getStart();
	}
	
	public TimeWithDayAttr end() {
		return ((CalculationTimeSheet)this).calculationTimeSheet.getEnd();
	}
	
	public List<TimeSheetOfDeductionItem> devideAt(TimeWithDayAttr baseTime) {
		return ((CalculationTimeSheet)this).calculationTimeSheet.timeDivide(baseTime).stream()
				.map(t -> new TimeSheetOfDeductionItem(t))
				.collect(Collectors.toList());
	}
	
	public List<TimeSheetOfDeductionItem> devideIfContains(TimeWithDayAttr baseTime) {
		if (this.contains(baseTime)) {
			return this.devideAt(baseTime);
		} else {
			return Arrays.asList(this);
		}
	}
	
	public boolean contains(TimeWithDayAttr baseTime) {
		return ((CalculationTimeSheet)this).calculationTimeSheet.contains(baseTime);
	}
}
