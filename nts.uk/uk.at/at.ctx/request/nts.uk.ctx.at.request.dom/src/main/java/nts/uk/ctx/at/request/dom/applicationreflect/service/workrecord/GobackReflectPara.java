package nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workschedule.ApplyTimeRequestAtr;
import nts.uk.ctx.at.request.dom.setting.company.request.appreflect.ApplyTimeSchedulePriority;
import nts.uk.ctx.at.request.dom.setting.company.request.appreflect.ClassifyScheAchieveAtr;
import nts.uk.ctx.at.request.dom.setting.company.request.appreflect.PriorityTimeReflectAtr;
@AllArgsConstructor
@Getter
@Setter
public class GobackReflectPara {
	/**	社員ID */
	private String employeeId;
	/**	年月日 */
	private GeneralDate appDate;
	/*スケジュール反映の設定*/
	/**直行直帰申請 の　時刻の反映：　固定（開始）	 */
	private ApplyTimeRequestAtr applyTimeAtr; /*ApplyTimeRequestAtr.START*/
	/*勤務反映の設定*/
	/**
	 * 振出・休出時反映する区分 (勤務の変更申請時)
	 */
	private boolean outResReflectAtr;
	/**
	 * 打刻優先区分
	 */
	private PriorityTimeReflectAtr priorStampAtr;
	/**
	 * 予定と実績を同じに変更する区分
	 */
	private ClassifyScheAchieveAtr scheAndRecordSameChangeFlg;
	/**
	 * 予定時刻反映区分
	 */
	private ApplyTimeSchedulePriority scheTimeReflectAtr;
	/**
	 * 予定反映区分
	 * True: 反映する
	 * False: 反映しない
	 */
	private boolean scheReflectAtr;
	/**
	 * 
	 */
	private String excLogId;
	/**
	 * 直行直帰申請情報
	 */
	private GobackAppRequestPara gobackData;
}
