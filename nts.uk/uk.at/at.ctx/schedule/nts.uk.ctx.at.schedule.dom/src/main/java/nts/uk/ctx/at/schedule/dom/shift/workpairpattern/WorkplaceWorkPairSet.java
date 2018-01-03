package nts.uk.ctx.at.schedule.dom.shift.workpairpattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;

/**
 * 勤務ペア設定
 * 
 * @author sonnh1
 *
 */
@AllArgsConstructor
@Getter
public class WorkplaceWorkPairSet extends DomainObject {
	private String workplaceId;
	private int groupNo;
	private int patternNo;
	private int pairNo;
	private WorkTypeCode workTypeCode;
	private WorkTimeCode workTimeCode;
	
	public static WorkplaceWorkPairSet convertFromJavaType(String workplaceId, int groupNo, int patternNo, int pairNo,
			String workTypeCode, String workTimeCode) {
		return new WorkplaceWorkPairSet(workplaceId, groupNo, patternNo, pairNo, new WorkTypeCode(workTypeCode),
				new WorkTimeCode(workTimeCode));
	}
}
