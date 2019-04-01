package nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.enums.EnumAdaptor;
/**
 * 休出申請設定, 休出事後反映設定, 休出事前反映設定
 * @author yennth
 *
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalAppSet extends AggregateRoot{
	// 会社ID
	private String companyId;
	// 事前反映設定: 休出時間
	private UseAtr prePerflex;
	// 休出事後反映設定: 休憩時間
	private UseAtr breakTime;
	// 休出事後反映設定: 勤務時間（出勤、退勤時刻）
	private UseAtr workTime;
	// 休出時間未入力チェック
	private UseAtr checkHdTime;
	// 休出申請勤務種類
	private UseAtr typePaidLeave;
	// 勤務変更設定
	private WorkChangeSet workChange;
	// 時間初期表示
	private UseAtr timeInit;
	// 法内法外矛盾チェック
	private UseAtr checkOut;
	// 代休先取り許可
	private UseAtr prefixLeave;
	// 休出時間指定単位
	private UnitTime unitTime;
	// 休暇申請同時申請設定
	private int appSimul;
	// 直帰区分
	private UnitTime bounSeg;
	// 直行区分
	private UnitTime directDivi;
	// 休出時間
	private UnitTime restTime;
	// 実績超過打刻優先設定
	private OverrideSet overrideSet;
	// 打刻漏れ計算区分
	private CalcStampMiss calStampMiss;
	
	public static WithdrawalAppSet createFromJavaType(String companyId, int prePerflex, int breakTime, int workTime, 
			int checkHdTime, int typePaidLeave, int workChange, int timeInit, int checkOut, int prefixLeave, 
			int unitTime, int appSimul, int bounSeg, int directDivi, int restTime, int overrideSet, int calStampMiss){
		return new WithdrawalAppSet(companyId, 
				EnumAdaptor.valueOf(prePerflex, UseAtr.class), 
				EnumAdaptor.valueOf(breakTime, UseAtr.class), 
				EnumAdaptor.valueOf(workTime, UseAtr.class), 
				EnumAdaptor.valueOf(checkHdTime, UseAtr.class), 
				EnumAdaptor.valueOf(typePaidLeave, UseAtr.class), 
				EnumAdaptor.valueOf(workChange, WorkChangeSet.class), 
				EnumAdaptor.valueOf(timeInit, UseAtr.class), 
				EnumAdaptor.valueOf(checkOut, UseAtr.class), 
				EnumAdaptor.valueOf(prefixLeave, UseAtr.class), 
				EnumAdaptor.valueOf(unitTime, UnitTime.class), appSimul, 
				EnumAdaptor.valueOf(bounSeg, UnitTime.class), 
				EnumAdaptor.valueOf(directDivi, UnitTime.class), 
				EnumAdaptor.valueOf(restTime, UnitTime.class),
				EnumAdaptor.valueOf(overrideSet, OverrideSet.class),
				EnumAdaptor.valueOf(calStampMiss, CalcStampMiss.class));
	}
}
