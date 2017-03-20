package nts.uk.ctx.pr.core.infra.entity.rule.employement.processing.yearmonth;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class QpdmtPaydayProcessingPK implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "CCD")
	public String ccd;

	@Basic(optional = false)
	@Column(name = "PROCESSING_NO")
	public int processingNo;	
	
}
