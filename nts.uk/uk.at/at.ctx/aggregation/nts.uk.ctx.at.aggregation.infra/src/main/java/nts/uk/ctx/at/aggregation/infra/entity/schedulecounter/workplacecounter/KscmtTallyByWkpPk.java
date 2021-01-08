package nts.uk.ctx.at.aggregation.infra.entity.schedulecounter.workplacecounter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KscmtTallyByWkpPk {

	/** 会社ID */
	@Column(name = "CID")
	public String companyId;

	@Column(name = "CATEGORY")
	public int category;

}
