package nts.uk.ctx.at.shared.dom.worktime_old;

import lombok.EqualsAndHashCode;
import lombok.Value;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.shared.dom.attendance.UseSetting;

/**
 *  就業時間帯の設定
 * @author Doan Duy Hung
 *
 */

@Value
@EqualsAndHashCode(callSuper = false)
public class WorkTime extends AggregateRoot {

	private SiftCode siftCD;
	
	private String companyID;
	
	private WorkTimeNote note;

	private WorkTimeDivision workTimeDivision;
	
	private UseSetting dispAtr;

	private WorkTimeDisplayName workTimeDisplayName;

	public WorkTime(SiftCode siftCD, String companyID, WorkTimeNote note, WorkTimeDivision workTimeDivision,
			UseSetting dispAtr, WorkTimeDisplayName workTimeDisplayName) {
		super();
		this.siftCD = siftCD;
		this.companyID = companyID;
		this.note = note;
		this.workTimeDivision = workTimeDivision;
		this.dispAtr = dispAtr;
		this.workTimeDisplayName = workTimeDisplayName;
	}
}
