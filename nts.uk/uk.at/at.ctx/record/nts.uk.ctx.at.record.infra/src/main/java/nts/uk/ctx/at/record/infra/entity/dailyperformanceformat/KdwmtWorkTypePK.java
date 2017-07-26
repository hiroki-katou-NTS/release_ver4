package nts.uk.ctx.at.record.infra.entity.dailyperformanceformat;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KdwmtWorkTypePK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "CID")
	public String companyId;
	
	@Column(name = "WORK_TYPE_CD")
	public String workTypeCode;
}
