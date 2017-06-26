package nts.uk.ctx.at.record.dom.divergencetime;
import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;

@Getter
public class DivergenceTime extends AggregateRoot{
	/*会社ID*/
	private String companyId;
	/*乖離時間ID*/
	private int divTimeId;
	/*乖離時間名称*/
	private DivergenceTimeName divTimeName;
	/*乖離時間使用設定*/
	private UseSetting divTimeUseSet;
	/*アラーム時間*/
	private Time alarmTime;
	/*エラー時間*/
	private Time errTime;
	/*選択使用設定*/	
	private InputSetting selectSet;
	/*入力使用設定*/
	private InputSetting inputSet;

	public DivergenceTime(String companyId, 
						int divTimeId, 
						DivergenceTimeName divTimeName,
						UseSetting divTimeUseSet, 
						Time alarmTime,
						Time errTime, 
						InputSetting selectSet, 
						InputSetting inputSet) {
		super();
		this.companyId = companyId;
		this.divTimeId = divTimeId;
		this.divTimeName = divTimeName;
		this.divTimeUseSet = divTimeUseSet;
		this.alarmTime = alarmTime;
		this.errTime = errTime;
		this.selectSet = selectSet;
		this.inputSet = inputSet;
	}
	
	public static DivergenceTime createSimpleFromJavaType(String companyId,
														int divTimeId,
														String divTimeName,
														int divTimeUseSet,
														int alarmTime,
														int errTime,
														int selectUseSet,
														int cancelErrSelect,
														int inputUseSet,
														int cancelErrInput){
		return new DivergenceTime(
				companyId, 
				divTimeId, 
				new DivergenceTimeName(divTimeName),
				EnumAdaptor.valueOf(divTimeUseSet, UseSetting.class),
				new Time(BigDecimal.valueOf(alarmTime)), 
				new Time(BigDecimal.valueOf(errTime)), 
				InputSetting.convert(selectUseSet,cancelErrSelect), 
				InputSetting.convert(inputUseSet,cancelErrInput));
				
	}
	public static boolean checkAlarmErrTime(int alarmTime,int errTime){
		if(alarmTime==errTime){
			if(alarmTime==0) return true;
			else return false;
		}else
		if(alarmTime<errTime){
			return true;
		}else{
			return false;
		}
		
	}
}
