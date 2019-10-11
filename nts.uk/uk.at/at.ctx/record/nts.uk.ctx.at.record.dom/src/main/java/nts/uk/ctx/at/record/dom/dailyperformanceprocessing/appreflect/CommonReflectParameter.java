package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;

@Getter
@Setter
@AllArgsConstructor
public class CommonReflectParameter {
	/**
	 * 社員ID
	 */
	private String employeeId;
	/**
	 * 年月日
	 */
	private GeneralDate appDate;
	/**
	 * 予定と実績を同じに変更する区分
	 */
	private ScheAndRecordSameChangeFlg scheAndRecordSameChangeFlg;
	/**
	 * 予定反映区分
	 */
	private boolean scheTimeReflectAtr;
	/**
	 *勤務種類コード
	 */
	private String workTypeCode;
	/**
	 * 就業時間帯コード 
	 */
	private String workTimeCode;
	
	private Integer startTime;
	private Integer endTime;
	private String excLogId;
	Optional<IdentityProcessUseSet> iPUSOpt;
	Optional<ApprovalProcessingUseSetting> approvalSet;
}
