package nts.uk.ctx.at.request.infra.entity.setting.request.application;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "KRQST_APPLICATION_SETTING")
public class KrqstApplicationSetting extends UkJpaEntity {

	@EmbeddedId
	public KrqstApplicationSettingPK krqstApplicationSettingPK;
	/**
	 * 実績修正がロック状態なら申請できない
	 */
	@Column(name = "APP_ACT_LOCK_FLG")
	public int appActLockFlg;
	/**
	 * 就業確定済の場合申請できない
	 */
	@Column(name = "APP_END_WORK_FLG")
	public int appEndWorkFlg;
	/**
	 * 日別実績が確認済なら申請できない
	 */
	@Column(name = "APP_ACT_CONFIRM_FLG")
	public int appActConfirmFlg;
	/**
	 * 時間外深夜の申請を利用する
	 */
	@Column(name = "APP_OVERTIME_NIGHT_FLG")
	public int appOvertimeNightFlg;
	/**
	 * 月別実績が確認済なら申請できない	
	 */
	@Column(name = "APP_ACT_MONTH_CONFIRM_FLG")
	public int appActMonthConfirmFlg;
	/**
	 * 申請理由が必須
	 */
	@Column(name = "REQUIRE_APP_REASON_FLG")
	public int requireAppReasonFlg;
	/**
	 * 事前事後区分表示
	 */
	@Column(name = "DISPLAY_PRE_POST_FLG")
	public int displayPrePostFlg;
	/**
	 * 就業時間帯の検索
	 */
	@Column(name = "DISPLAY_SEARCH_TIME_FLG")
	public int displaySearchTimeFlg;
	/**
	 * 登録時の手動メール送信の初期値
	 */
	@Column(name = "MANUAL_SEND_MAIL_ATR")
	public int manualSendMailAtr;

	/** 承認 */
	
	
	/**
	 * 承認ルートの基準日 
	 */
	@Column(name = "BASE_DATE_FLG")
	public int baseDateFlg;
	/**
	 * 事前申請の超過メッセージ
	 */
	@Column(name = "ADVANCE_EXCESS_MESS_DISP_ATR")
	public int advanceExcessMessDispAtr;
	/**
	 * 休出の事前申請
	 */
	@Column(name = "HW_ADVANCE_DISP_ATR")
	public int hwAdvanceDispAtr;
	/**
	 * 休出の実績
	 */
	@Column(name = "HW_ACTUAL_DISP_ATR")
	public int hwActualDispAtr;
	/**
	 * 実績超過メッセージ
	 */
	@Column(name = "ACTUAL_EXCESS_MESS_DISP_ATR")
	public int actualExcessMessDispAtr;
	/**
	 * 残業の事前申請
	 */
	@Column(name = "OT_ADVANCE_DISP_ATR")
	public int otAdvanceDispAtr;
	/**
	 * 残業の実績
	 */
	@Column(name = "OT_ACTUAL_DISP_ATR")
	public int otActualDispAtr;
	/**
	 * 申請対象日に対して警告表示
	 */
	@Column(name = "WARNING_DATE_DISP_ATR")
	public int warningDateDispAtr;
	/**
	 * 申請理由
	 */
	@Column(name = "APP_REASON_DISP_ATR")
	public int appReasonDispAtr;
	/**
	 * 承認時に申請内容を変更できる
	 */
	@Column(name = "APP_CONTENT_CHANGE_FLG")
	public int appContentChangeFlg;
	/**
	 * 事前申請スケジュール反映
	 */
	@Column(name = "SCHE_REFLECT_FLG")
	public int scheReflectFlg;
	/**
	 * 反映時刻優先
	 */
	@Column(name = "PRIORITY_TIME_REFLECT_FLG")
	public int priorityTimeReflectFlg;
	/**
	 * 外出打刻漏れの箇所へ出勤退勤時刻を反映する
	 */
	@Column(name = "ATTENDENT_TIME_REFLECT_FLG")
	public int attendentTimeReflectFlg;

	/** 実績が確定されている場合*/
	@Column(name = "ACHIEVEMENT_CONFIRM_ATR")
	public int achievementConfirmedAtr;
	
	/** スケジュールが確定されている場合  */
	@Column(name = "SCHEDULE_CONFIRM_ATR")
	public int scheduleConfirmedAtr;
	
	@OneToMany(targetEntity=KrqstAppTypeDiscrete.class, cascade = CascadeType.ALL, mappedBy = "krqstApplicationSetting")
	@JoinTable(name = "KRQST_APP_TYPE_DISCRETE")
	public List<KrqstAppTypeDiscrete> krqstAppTypeDiscretes;
	
	@OneToMany(targetEntity=KrqstAppDeadline.class, cascade = CascadeType.ALL, mappedBy = "krqstApplicationSetting")
	@JoinTable(name = "KRQST_APP_DEADLINE")
	public List<KrqstAppDeadline> krqstAppDeadlines;
	
	@Override
	protected Object getKey() {
		return krqstApplicationSettingPK;
	}

}
