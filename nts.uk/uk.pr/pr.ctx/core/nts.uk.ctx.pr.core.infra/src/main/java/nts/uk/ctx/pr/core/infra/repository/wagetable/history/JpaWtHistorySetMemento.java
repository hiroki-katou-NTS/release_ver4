/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable.history;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.wagetable.WtCode;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtHistorySetMemento;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtItem;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.ElementSetting;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.StepElementSetting;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.CodeItem;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.RangeItem;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableCd;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableEleHist;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableEleHistPK;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableHist;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableHistPK;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableMny;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableNum;

/**
 * The Class JpaWageTableHistorySetMemento.
 */
public class JpaWtHistorySetMemento implements WtHistorySetMemento {

	/** The type value. */
	private QwtmtWagetableHist typeValue;

	/**
	 * Instantiates a new jpa wage table history set memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaWtHistorySetMemento(QwtmtWagetableHist typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistorySetMemento#
	 * setCompanyCode(nts.uk.ctx.core.dom.company.CompanyCode)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		QwtmtWagetableHistPK qwtmtWagetableHistPK = new QwtmtWagetableHistPK();
		qwtmtWagetableHistPK.setCcd(companyCode);
		this.typeValue.setQwtmtWagetableHistPK(qwtmtWagetableHistPK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistorySetMemento#
	 * setCode(nts.uk.ctx.pr.core.dom.wagetable.WageTableCode)
	 */
	@Override
	public void setWageTableCode(WtCode code) {
		QwtmtWagetableHistPK qwtmtWagetableHistPK = this.typeValue.getQwtmtWagetableHistPK();
		qwtmtWagetableHistPK.setWageTableCd(code.v());
		this.typeValue.setQwtmtWagetableHistPK(qwtmtWagetableHistPK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistorySetMemento#
	 * setHistoryId(java.lang.String)
	 */
	@Override
	public void setHistoryId(String historyId) {
		QwtmtWagetableHistPK qwtmtWagetableHistPK = this.typeValue.getQwtmtWagetableHistPK();
		qwtmtWagetableHistPK.setHistId(historyId);
		this.typeValue.setQwtmtWagetableHistPK(qwtmtWagetableHistPK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistorySetMemento#
	 * setApplyRange(nts.uk.ctx.pr.core.dom.insurance.MonthRange)
	 */
	@Override
	public void setApplyRange(MonthRange applyRange) {
		this.typeValue.setStrYm(applyRange.getStartMonth().v());
		this.typeValue.setEndYm(applyRange.getEndMonth().v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistorySetMemento#
	 * setValueItems(java.util.List)
	 */
	@Override
	public void setValueItems(List<WtItem> elements) {
		QwtmtWagetableHistPK qwtmtWagetableHistPK = this.typeValue.getQwtmtWagetableHistPK();
		String companyCode = qwtmtWagetableHistPK.getCcd();
		String wageTableCd = qwtmtWagetableHistPK.getWageTableCd();
		String historyId = qwtmtWagetableHistPK.getHistId();

		// Create entity of step items
		List<QwtmtWagetableMny> qwtmtWagetableMnyList = elements.stream().map(item -> {
			// Create entity.
			QwtmtWagetableMny entity = new QwtmtWagetableMny();

			// Transfer data
			item.saveToMemento(
					new JpaWtItemSetMemento(companyCode, wageTableCd, historyId, entity));

			return entity;
		}).collect(Collectors.toList());

		// Set val
		this.typeValue.setQwtmtWagetableMnyList(qwtmtWagetableMnyList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WtHistorySetMemento#
	 * setElementSettings(java.util.List)
	 */
	@Override
	public void setElementSettings(List<ElementSetting> elementSettings) {
		QwtmtWagetableHistPK qwtmtWagetableHistPK = this.typeValue.getQwtmtWagetableHistPK();
		String companyCode = qwtmtWagetableHistPK.getCcd();
		String wageTableCd = qwtmtWagetableHistPK.getWageTableCd();
		String historyId = qwtmtWagetableHistPK.getHistId();

		List<QwtmtWagetableEleHist> qwtmtWagetableEleHistList = elementSettings.stream()
				.map(item -> {
					QwtmtWagetableEleHist qwtmtWagetableEleHist = new QwtmtWagetableEleHist();

					// Create primary key.
					QwtmtWagetableEleHistPK qwtmtWagetableEleHistPK = new QwtmtWagetableEleHistPK();
					qwtmtWagetableEleHistPK.setCcd(companyCode);
					qwtmtWagetableEleHistPK.setWageTableCd(wageTableCd);
					qwtmtWagetableEleHistPK.setHistId(historyId);
					qwtmtWagetableEleHistPK.setDemensionNo(item.getDemensionNo().value);

					// Set primary key.
					qwtmtWagetableEleHist.setQwtmtWagetableEleHistPK(qwtmtWagetableEleHistPK);

					// Check setting type
					if (item instanceof StepElementSetting) {

						// Cast to step setting.
						StepElementSetting step = (StepElementSetting) item;

						// Set values
						qwtmtWagetableEleHist.setDemensionUpperLimit(step.getUpperLimit().v());
						qwtmtWagetableEleHist.setDemensionLowerLimit(step.getLowerLimit().v());
						qwtmtWagetableEleHist.setDemensionInterval(step.getInterval().v());

						// Create wage table codes
						List<QwtmtWagetableNum> qwtmtWagetableNumList = item.getItemList().stream()
								.map(subItem -> {
									RangeItem rangeItem = (RangeItem) subItem;
									QwtmtWagetableNum wagetableNum = new QwtmtWagetableNum(
											companyCode, wageTableCd, historyId,
											item.getDemensionNo().value,
											rangeItem.getOrderNumber());
									wagetableNum.setElementStr(rangeItem.getStartVal());
									wagetableNum.setElementEnd(rangeItem.getEndVal());
									wagetableNum.setElementId(rangeItem.getUuid().v());
									return wagetableNum;
								}).collect(Collectors.toList());

						qwtmtWagetableEleHist.setQwtmtWagetableNumList(qwtmtWagetableNumList);

					} else {
						qwtmtWagetableEleHist.setDemensionUpperLimit(BigDecimal.ZERO);
						qwtmtWagetableEleHist.setDemensionLowerLimit(BigDecimal.ZERO);
						qwtmtWagetableEleHist.setDemensionInterval(BigDecimal.ONE);

						// Create wage table codes
						List<QwtmtWagetableCd> qwtmtWagetableCdList = item.getItemList().stream()
								.map(subItem -> {
									CodeItem rangeItem = (CodeItem) subItem;
									QwtmtWagetableCd wagetableCd = new QwtmtWagetableCd(companyCode,
											wageTableCd, historyId, item.getDemensionNo().value,
											rangeItem.getReferenceCode());
									wagetableCd.setElementId(rangeItem.getUuid().v());
									return wagetableCd;
								}).collect(Collectors.toList());

						qwtmtWagetableEleHist.setQwtmtWagetableCdList(qwtmtWagetableCdList);
					}

					// Return
					return qwtmtWagetableEleHist;

				}).collect(Collectors.toList());

		this.typeValue.setQwtmtWagetableEleHistList(qwtmtWagetableEleHistList);
	}

}
