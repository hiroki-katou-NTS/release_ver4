package nts.uk.ctx.at.record.pub.dailyperform.appreflect;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
@AllArgsConstructor
@Setter
@Getter
public class HolidayWorkReflectPubPara {
	/**
	 * 社員ID
	 */
	private String employeeId;
	/**
	 * 年月日
	 */
	private GeneralDate baseDate;
	/**
	 * 休出時間反映フラグ
	 */
	private boolean holidayWorkReflectFlg;
	/**
	 * 予定と実績を同じに変更する区分
	 */
	private ScheAndRecordSameChangePubFlg scheAndRecordSameChangeFlg;
	/**
	 * 予定反映区分
	 * True: する
	 * False: しない
	 */
	private boolean scheReflectFlg;
	/**
	 * 休出事後の 勤務時間（出勤、退勤時刻）を反映
	 */
	private boolean recordReflectTimeFlg;
	/**
	 * 休出事後反映の休憩時間を反映
	 */
	private boolean recordReflectBreakFlg;
	/**
	 * 休日出勤申請
	 */
	private HolidayWorktimeAppPubPara holidayWorkPara;
	private String excLogId;
	private Optional<IdentityProcessUseSetPub> identityProcessUseSetPub;
	private Optional<ApprovalProcessingUseSettingPub> approvalProcessingUseSettingPub;
}
