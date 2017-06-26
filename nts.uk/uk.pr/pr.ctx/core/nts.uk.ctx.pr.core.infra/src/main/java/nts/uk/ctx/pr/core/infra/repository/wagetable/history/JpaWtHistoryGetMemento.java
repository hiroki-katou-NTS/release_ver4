/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable.history;

import java.util.List;
import java.util.stream.Collectors;

import nts.arc.time.YearMonth;
import nts.gul.collection.LazyList;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.wagetable.DemensionNo;
import nts.uk.ctx.pr.core.dom.wagetable.ElementId;
import nts.uk.ctx.pr.core.dom.wagetable.ElementType;
import nts.uk.ctx.pr.core.dom.wagetable.WtCode;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtHistoryGetMemento;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtItem;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.ElementSetting;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.RangeLimit;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.StepElementSetting;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.CodeItem;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.RangeItem;
import nts.uk.ctx.pr.core.infra.entity.wagetable.element.QwtmtWagetableElement;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableEleHistPK;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableHist;

/**
 * The Class JpaWageTableHistoryGetMemento.
 */
public class JpaWtHistoryGetMemento implements WtHistoryGetMemento {

	/** The type value. */
	private QwtmtWagetableHist typeValue;

	/**
	 * Instantiates a new jpa wage table history get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaWtHistoryGetMemento(QwtmtWagetableHist typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistoryGetMemento#
	 * getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.typeValue.getQwtmtWagetableHistPK().getCcd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistoryGetMemento#
	 * getCode()
	 */
	@Override
	public WtCode getWageTableCode() {
		return new WtCode(this.typeValue.getQwtmtWagetableHistPK().getWageTableCd());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistoryGetMemento#
	 * getHistoryId()
	 */
	@Override
	public String getHistoryId() {
		return this.typeValue.getQwtmtWagetableHistPK().getHistId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistoryGetMemento#
	 * getApplyRange()
	 */
	@Override
	public MonthRange getApplyRange() {
		return MonthRange.range(YearMonth.of(this.typeValue.getStrYm()),
				YearMonth.of(this.typeValue.getEndYm()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistoryGetMemento#
	 * getValueItems()
	 */
	@Override
	public List<WtItem> getValueItems() {
		// Return.
		return LazyList.withMap(() -> this.typeValue.getQwtmtWagetableMnyList(), (entity) -> {
			// Transfer data
			return new WtItem(new JpaWtItemGetMemento(entity));
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WtHistoryGetMemento#
	 * getElementSettings()
	 */
	@Override
	public List<ElementSetting> getElementSettings() {
		return LazyList.withMap(() -> this.typeValue.getQwtmtWagetableEleHistList(), (entity) -> {
			QwtmtWagetableEleHistPK pk = entity.getQwtmtWagetableEleHistPK();
			QwtmtWagetableElement element = entity.getQwtmtWagetableElement();
			ElementType type = ElementType.valueOf(element.getDemensionType());

			// Check mode
			if (type.isRangeMode) {
				// Create range items.
				List<RangeItem> rangeItems = entity.getQwtmtWagetableNumList().stream()
						.map(item -> new RangeItem(item.getQwtmtWagetableNumPK().getElementNumNo(),
								item.getElementStr(), item.getElementEnd(),
								new ElementId(item.getElementId())))
						.collect(Collectors.toList());

				// Create step setting.
				StepElementSetting el = new StepElementSetting(
						DemensionNo.valueOf(pk.getDemensionNo()), type, rangeItems);

				// Set range
				el.setSetting(new RangeLimit(entity.getDemensionLowerLimit()),
						new RangeLimit(entity.getDemensionUpperLimit()),
						new RangeLimit(entity.getDemensionInterval()));

				// Return
				return el;
			} else {
				// Create code items.
				List<CodeItem> codeItems = entity.getQwtmtWagetableCdList().stream()
						.map(item -> new CodeItem(item.getQwtmtWagetableCdPK().getElementCd(),
								new ElementId(item.getElementId())))
						.collect(Collectors.toList());

				// Return setting.
				return new ElementSetting(DemensionNo.valueOf(pk.getDemensionNo()), type,
						codeItems);
			}

		});
	}

}
