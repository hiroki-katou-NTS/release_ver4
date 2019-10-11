package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ScheAndRecordSameChangeFlg;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
@AllArgsConstructor
@Setter
@Getter
public class GobackReflectParameter {
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
	private boolean outResReflectAtr;
	/**
	 * 打刻優先区分
	 */
	private PriorStampAtr priorStampAtr;
	/**
	 * 予定と実績を同じに変更する区分
	 */
	private ScheAndRecordSameChangeFlg scheAndRecordSameChangeFlg;
	/**
	 * 予定時刻反映区分
	 */
	private ScheTimeReflectAtr scheTimeReflectAtr;
	/**
	 * 予定反映区分
	 * True: 反映する
	 * False: 反映しない
	 */
	private boolean scheReflectAtr;
	/**
	 * 直行直帰申請情報
	 */
	private GobackAppParameter gobackData;
	private String excLogId;
	Optional<IdentityProcessUseSet> iPUSOpt;
	Optional<ApprovalProcessingUseSetting> approvalSet;

}
