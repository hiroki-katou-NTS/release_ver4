package nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp;

import java.util.Optional;

import lombok.Getter;
import nts.arc.layer.dom.objecttype.DomainValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.OvertimeDeclaration;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;

/**
 * VO : 実績への反映内容
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.勤務実績.勤務実績.打刻管理.打刻.実績への反映内容
 * @author tutk
 *
 */
public class RefectActualResult implements DomainValue {

	/**
	 * 勤務先情報
	 * chuyển 2 thuộc tính cũ workLocationCD, cardNumberSupport vào trong WorkInformationStamp
	 */
	@Getter
	private final Optional<WorkInformationStamp> workInforStamp;
	
	
	/**
	 * 就業時間帯コード
	 */
	@Getter
	private final Optional<WorkTimeCode> workTimeCode;
	
	/**
	 * 時間外の申告
	 */
	@Getter
	private final Optional<OvertimeDeclaration> overtimeDeclaration;

	public RefectActualResult(WorkInformationStamp workInforStamp,
			WorkTimeCode workTimeCode,OvertimeDeclaration overtimeDeclaration) {
		super();
		this.workInforStamp = Optional.ofNullable(workInforStamp);
		this.workTimeCode = Optional.ofNullable(workTimeCode);
		this.overtimeDeclaration = Optional.ofNullable(overtimeDeclaration);
	}
	
	public static RefectActualResult create(WorkInformationStamp workInforStamp,
			WorkTimeCode workTimeCode,OvertimeDeclaration overtimeDeclaration) {
		
		return new RefectActualResult(workInforStamp, workTimeCode, overtimeDeclaration);
	}
	
	
}
