/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.labor.accidentrate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.RoundingMethod;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRateGetMemento;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.InsuBizRateItem;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.BusinessTypeEnum;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.accidentrate.QismtWorkAccidentInsu;

/**
 * The Class JpaAccidentInsuranceRateGetMemento.
 */
public class JpaAccidentInsuranceRateGetMemento implements AccidentInsuranceRateGetMemento {

	/** The type value. */
	private List<QismtWorkAccidentInsu> typeValue;

	/**
	 * Instantiates a new jpa accident insurance rate get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaAccidentInsuranceRateGetMemento(List<QismtWorkAccidentInsu> typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateGetMemento#getHistoryId()
	 */
	@Override
	public String getHistoryId() {
		if (CollectionUtil.isEmpty(this.typeValue)) {
			return null;
		}
		return this.typeValue.get(0).getQismtWorkAccidentInsuPK().getHistId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateGetMemento#getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		if (CollectionUtil.isEmpty(this.typeValue)) {
			return null;
		}
		return this.typeValue.get(0).getQismtWorkAccidentInsuPK().getCcd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateGetMemento#getApplyRange()
	 */
	@Override
	public MonthRange getApplyRange() {
		if (CollectionUtil.isEmpty(this.typeValue)) {
			return null;
		}
		return MonthRange.range(YearMonth.of(this.typeValue.get(0).getStrYm()),
			YearMonth.of(this.typeValue.get(0).getEndYm()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateGetMemento#getRateItems()
	 */
	@Override
	public Set<InsuBizRateItem> getRateItems() {
		return this.typeValue.stream().map(item -> {
			InsuBizRateItem itemInsuBizRateItem = new InsuBizRateItem();
			itemInsuBizRateItem
				.setInsuBizType(BusinessTypeEnum.valueOf(item.getQismtWorkAccidentInsuPK().getWaInsuCd()));
			itemInsuBizRateItem.setInsuRate(item.getWaInsuRate().doubleValue());
			itemInsuBizRateItem.setInsuRound(RoundingMethod.valueOf(item.getWaInsuRound()));
			return itemInsuBizRateItem;
		}).collect(Collectors.toSet());

	}

}
