package nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.ReasonNotReflect_New;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
@AllArgsConstructor
@Getter
@Setter
public class AbsenceReflectPara {
	/**
	 * 社員ID
	 */
	private String employeeId;
	/**
	 * 年月日
	 */
	private GeneralDate baseDate;
	/**
	 * 予定と実績を同じに変更する区分
	 */
	private ScheAndRecordSameChangeFlg scheAndRecordSameChangeFlg;
	/**
	 * 予定反映区分
	 */
	private boolean scheTimeReflectAtr;
	/**
	 * 休暇申請．勤務種類コード
	 */
	private String workTypeCode;
	/**
	 * 反映状態
	 */
	private ReflectedState_New reflectState;
	/**
	 * 予定反映不可理由
	 */
	private ReasonNotReflect_New reasoNotReflect;
}
