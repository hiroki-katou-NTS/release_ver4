package nts.uk.ctx.at.shared.dom;

import java.util.Optional;

import lombok.Value;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;

/**
 * 勤務情報
 * @author ken_takasu
 *
 */
@Value
public class WorkInformation {

	private WorkTypeCode workTypeCode;
	private WorkTimeCode siftCode;

	public WorkInformation(String workTimeCode, String workTypeCode) {
		this.siftCode = workTimeCode == null ? null : new WorkTimeCode(workTimeCode);
		this.workTypeCode = workTypeCode == null ? null : new WorkTypeCode(workTypeCode);
	}
	
	public WorkInformation(WorkTimeCode workTimeCode, WorkTypeCode workTypeCode) {
		this.siftCode = workTimeCode;
		this.workTypeCode = workTypeCode;
	}
	
	public WorkTimeCode getWorkTimeCode(){
		return this.siftCode;
	}
}
