package nts.uk.ctx.at.function.infra.entity.alarm.extractionrange.daily;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KfnmtExtractionPeriodDailyPK implements Serializable{
	private static final long serialVersionUID = 1L;

	@Column(name = "EXTRACTION_ID")
	public String extractionId;
	
	@Column(name = "EXTRACTION_RANGE")
	public int extractionRange;
}
