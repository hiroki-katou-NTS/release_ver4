package nts.uk.ctx.at.aggregation.infra.entity.schedulecounter.wkpcounterlaborcostandtime;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KscmtTallyByWkpLaborCostPk {

	/** 会社ID */
	@Column(name = "CID")
	public String companyId;

	@Column(name = "COST_TYPE")
	public int costType;
}
