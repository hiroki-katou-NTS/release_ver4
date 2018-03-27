package nts.uk.ctx.at.record.app.find.divergence.time.history;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceReferenceTimeUsageUnitGetMemento;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class DivergenceReferenceTimeUsageUnitDto.
 */
@Getter
@Setter
@AllArgsConstructor
public class DivergenceReferenceTimeUsageUnitDto implements DivergenceReferenceTimeUsageUnitGetMemento {

	/** The company id */
	private String cId;

	/** The work type usage set */
	private BigDecimal workTypeUseSet;

	/**
	 * Instantiates a new divergence ref time usage unit dto.
	 */
	public DivergenceReferenceTimeUsageUnitDto() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * DivergenceReferenceTimeUsageUnitGetMemento#getCompanyId()
	 */
	@Override
	public String getCompanyId() {
		return AppContexts.user().companyId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * DivergenceReferenceTimeUsageUnitGetMemento#getWorkTypeUseSet()
	 */
	@Override
	public BigDecimal getWorkTypeUseSet() {
		return this.workTypeUseSet;
	}

}
