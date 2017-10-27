package nts.uk.ctx.at.schedule.infra.entity.schedule.workscheduleeditstate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 
 * @author sonnh1
 *
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KscstWorkScheduleStatePK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "SID")
	public String employeeId;

	@Column(name = "SCHEDULE_ITEM_ID")
	public String scheduleItemId;
}
