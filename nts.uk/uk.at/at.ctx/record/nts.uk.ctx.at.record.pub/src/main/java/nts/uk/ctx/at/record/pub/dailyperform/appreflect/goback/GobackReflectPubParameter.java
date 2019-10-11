package nts.uk.ctx.at.record.pub.dailyperform.appreflect.goback;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ApprovalProcessingUseSettingPub;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.IdentityProcessUseSetPub;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ScheAndRecordSameChangePubFlg;
@AllArgsConstructor
@Getter
@Setter
public class GobackReflectPubParameter {
	/**
	 * 社員ID
	 */
	private String employeeId;
	/**
	 * 年月日		
	 */
	private GeneralDate dateData;
	/**
	 * 振出・休出時反映する区分
	 */
	private boolean OutResReflectAtr;
	/**
	 * 打刻優先区分
	 */
	private PriorStampPubAtr priorStampAtr;
	/**
	 * 予定と実績を同じに変更する区分
	 */
	private ScheAndRecordSameChangePubFlg scheAndRecordSameChangeFlg;
	/**
	 * 予定時刻反映区分
	 */
	private ScheTimeReflectPubAtr scheTimeReflectAtr;
	/**
	 * 予定反映区分
	 * True: 反映する
	 * False: 反映しない
	 */
	private boolean scheReflectAtr;
	/**
	 * 直行直帰申請情報
	 */
	private GobackAppPubParameter gobackData;
	private String excLogId;
	private Optional<IdentityProcessUseSetPub> identityProcessUseSetPub;
	private Optional<ApprovalProcessingUseSettingPub> approvalProcessingUseSettingPub;
}
