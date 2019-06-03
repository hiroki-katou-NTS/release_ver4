package nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord;

/*import nts.uk.ctx.at.request.dom.application.ReasonNotReflectDaily_New;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;*/
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.setting.company.request.appreflect.ClassifyScheAchieveAtr;
@AllArgsConstructor
@Getter
@Setter
public class CommonReflectPara {
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
	private ClassifyScheAchieveAtr scheAndRecordSameChangeFlg;
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
	
	private GeneralDate startDate;
	
	private GeneralDate endDate;
	
	private Integer startTime;
	private Integer endTime;
	private String excLogId;
}
