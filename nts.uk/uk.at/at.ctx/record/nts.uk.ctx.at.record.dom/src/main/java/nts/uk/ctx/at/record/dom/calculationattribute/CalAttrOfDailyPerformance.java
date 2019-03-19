package nts.uk.ctx.at.record.dom.calculationattribute;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalFlexOvertimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalRestTimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalcOfLeaveEarlySetting;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalRaisingSalarySetting;

/**
 * 
 * @author nampt
 * 日別実績の計算区分 - root
 *
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CalAttrOfDailyPerformance extends AggregateRoot {
	
	//社員ID: 社員ID
	private String employeeId;
	
	//年月日: 年月日
	private GeneralDate ymd;
	
	//フレックス超過時間: フレックス超過時間の自動計算設定
	private AutoCalFlexOvertimeSetting flexExcessTime;
	
	//加給: 加給の自動計算設定
	private AutoCalRaisingSalarySetting rasingSalarySetting;
	
	//休出時間: 休出時間の自動計算設定
	private AutoCalRestTimeSetting holidayTimeSetting;
	
	//残業時間: 残業時間の自動計算設定
	private AutoCalOvertimeSetting overtimeSetting;
	
	//遅刻早退: 遅刻早退の自動計算設定
	private AutoCalcOfLeaveEarlySetting leaveEarlySetting;
	
	//乖離時間: 乖離時間の自動計算設定
	private AutoCalcSetOfDivergenceTime divergenceTime; 
	
//	/**
//	 * 計算区分を全て「する」に変更する
//	 */
//	public static CalAttrOfDailyPerformance turnAllAtrTrue() {
//		//残業時間の自動計算設定を打刻から計算するに
//		this.overtimeSetting
//		//フレックス
//		this.flexExcessTime
//		//休出
//		this.holidayTimeSetting
//		
//	} 

	public CalAttrOfDailyPerformance reCreate(AutoCalAtrOvertime atr) {
		return new CalAttrOfDailyPerformance(this.employeeId,
											 this.ymd,
											 this.flexExcessTime,
											 this.rasingSalarySetting,
											 this.holidayTimeSetting,
											 this.overtimeSetting.changeNormalAutoCalcSetting(atr),
											 this.leaveEarlySetting,
											 this.divergenceTime);
	}
}
