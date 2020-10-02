package nts.uk.ctx.at.schedule.dom.shift.management.schedulecounter.laborcostandtime;

import nts.arc.enums.EnumAdaptor;

/**
 * 人件費・時間の種類
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.勤務予定.シフト管理.シフト勤務.スケジュール集計.人件費・時間.人件費・時間の種類
 * @author dan_pv
 *
 */
public enum LaborCostAndTimeType {
	
    TOTAL(0), // 合計

    WORKING_HOURS(1), // 就業時間
    
    OVERTIME(2); // 時間外時間
    
    public int value;

    private LaborCostAndTimeType(int value) {
        this.value = value;
    }
    
    public static LaborCostAndTimeType of(int value) {
    	return EnumAdaptor.valueOf(value, LaborCostAndTimeType.class);
    }

}
