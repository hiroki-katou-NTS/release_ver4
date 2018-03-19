package nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
@AllArgsConstructor
@Getter
@Setter
public class GobackReflectPara {
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
	private PriorStampRequestAtr priorStampAtr;
	/**
	 * 予定と実績を同じに変更する区分
	 */
	private ScheAndRecordSameChangeRequestFlg scheAndRecordSameChangeFlg;
	/**
	 * 予定時刻反映区分
	 */
	private ScheTimeReflectRequesAtr scheTimeReflectAtr;
	/**
	 * 予定反映区分
	 * True: 反映する
	 * False: 反映しない
	 */
	private boolean scheReflectAtr;
	/**
	 * 直行直帰申請情報
	 */
	private GobackAppRequestPara gobackData;
}
