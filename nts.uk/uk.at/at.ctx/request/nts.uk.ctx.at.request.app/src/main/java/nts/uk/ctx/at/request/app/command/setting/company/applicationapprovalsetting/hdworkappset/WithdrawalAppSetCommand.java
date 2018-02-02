package nts.uk.ctx.at.request.app.command.setting.company.applicationapprovalsetting.hdworkappset;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WithdrawalAppSetCommand {
	// 会社ID
	private String companyId;
	// 事前反映設定
	private int prePerflex;
	// 休憩時間
	private int breakTime;
	// 勤務時間（出勤、退勤時刻）
	private int workTime;
	// 休出時間未入力チェック
	private int checkHdTime;
	// 休出申請勤務種類
	private int typePaidLeave;
	// 勤務変更設定
	private int workChange;
	// 時間初期表示
	private int timeInit;
	// 法内法外矛盾チェック
	private int checkOut;
	// 代休先取り許可
	private int prefixLeave;
	// 休出時間指定単位
	private int unitTime;
	// 休暇申請同時申請設定
	private int appSimul;
	// 直帰区分
	private int bounSeg;
	// 直行区分
	private int directDivi;
	// 休出時間
	private int restTime;
}
