package nts.uk.ctx.at.record.infra.entity.worktime;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KrcmtTimeLeavingWorkPK implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "SID")
	public String employeeId;
	
	@Column(name = "WORK_NO")
	public String workNo;

}
