package nts.uk.ctx.at.request.dom.applicationreflect.service;

/*import nts.uk.ctx.at.request.dom.application.UseAtr;*/
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.withdrawalrequestset.WorkUse;
import nts.uk.ctx.at.request.dom.setting.company.request.appreflect.ApplyTimeSchedulePriority;
import nts.uk.ctx.at.request.dom.setting.company.request.appreflect.ClassifyScheAchieveAtr;
import nts.uk.ctx.at.request.dom.setting.company.request.appreflect.PriorityTimeReflectAtr;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InformationSettingOfEachApp {
	/**
	 * 振休振出申請設定: 就業時間帯選択の利用
	 */
	private WorkUse furikyuFurishutsu;
	/**
	 * 直行直帰申請共通設定．勤務の変更申請時
	 */
	private boolean chokochoki;
	/**
	 * 勤種反映フラグ(実績): 残業申請設定．残業事後反映設定．勤務種類、就業時間帯
	 */
	private boolean zangyouRecordReflect;
	/**
	 * 勤種反映フラグ(予定): 残業申請設定．残業事前反映設定．「勤務種類、就業時間帯、勤務時間」
	 */
	private boolean zangyouScheReflect;
	/**
	 * 残業時間反映フラグ: 残業申請設定．残業事前反映設定．残業時間
	 */
	private boolean zangyouWorktime;
	/**
	 * 申請反映設定．予定時刻の反映時刻優先
	 */
	private ApplyTimeSchedulePriority scheJikokuYusen;
	/**
	 * 申請反映設定．反映時刻優先
	 */
	private PriorityTimeReflectAtr workJikokuYusen;
	/**
	 * 申請反映設定．事前申請スケジュール反映
	 */
	private boolean jizenScheYusen;
	/**
	 * 申請反映設定．予定と実績を同じに変更する区分
	 */
	private ClassifyScheAchieveAtr scheAndWorkChange;
	/**
	 * 休出申請設定．事前反映設定．休出時間
	 */
	private boolean hwScheReflectHwTime;
	/**	休出事後反映設定: 勤務時間（出勤、退勤時刻） */
	private boolean hwRecordReflectTime;
	/**休出事後反映設定: 休憩時間	 */
	private boolean hwRecordReflectBreak;
	
}
