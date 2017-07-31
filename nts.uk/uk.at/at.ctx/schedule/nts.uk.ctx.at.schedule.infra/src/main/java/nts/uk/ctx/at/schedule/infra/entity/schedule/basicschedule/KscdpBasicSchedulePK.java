package nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

/**
 * 
 * @author sonnh1
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class KscdpBasicSchedulePK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "SID")
	public String sId;

	@Column(name = "YMD")
	public GeneralDate date;

}
