/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.history;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.arc.primitive.PrimitiveValue;
import nts.arc.time.YearMonth;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.pr.core.dom.base.simplehistory.History;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.wagetable.ElementId;
import nts.uk.ctx.pr.core.dom.wagetable.ElementType;
import nts.uk.ctx.pr.core.dom.wagetable.WtCode;
import nts.uk.ctx.pr.core.dom.wagetable.WtHead;
import nts.uk.ctx.pr.core.dom.wagetable.element.WtElement;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.ElementSetting;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.RangeLimit;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.StepElementSetting;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.CodeItem;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.RangeItem;

/**
 * The Class WageTableHistory.
 */
@Getter
public class WtHistory extends DomainObject implements History<WtHistory> {

	/** The company code. */
	private String companyCode;

	/** The wage table code. */
	private WtCode wageTableCode;

	/** The history id. */
	private String historyId;

	/** The apply range. */
	private MonthRange applyRange;

	/** The demension items. */
	private List<ElementSetting> elementSettings;

	/** The value items. */
	private List<WtItem> valueItems;

	/**
	 * Instantiates a new unit price history.
	 */
	private WtHistory() {
		this.historyId = IdentifierUtil.randomUniqueId();
	};

	/**
	 * Inits the from head.
	 *
	 * @param head
	 *            the head
	 * @param startYearMonth
	 *            the start year month
	 * @return the wt history
	 */
	public static WtHistory initFromHead(WtHead head, YearMonth startYearMonth) {
		WtHistory history = new WtHistory();
		history.companyCode = head.getCompanyCode();
		history.wageTableCode = head.getCode();
		history.applyRange = MonthRange.toMaxDate(startYearMonth);
		history.valueItems = Collections.emptyList();

		// Start init default create wage table element.
		List<ElementSetting> elementSettings = new ArrayList<>();
		List<WtElement> elementList = head.getElements();
		for (WtElement element : elementList) {
			ElementType type = element.getType();

			// Code mode.
			if (type.isCodeMode) {
				elementSettings.add(new ElementSetting(element.getDemensionNo(), element.getType(),
						Collections.emptyList()));
			}

			// Range mode.
			if (type.isRangeMode) {
				StepElementSetting stepElementSetting = new StepElementSetting(
						element.getDemensionNo(), element.getType(), Collections.emptyList());
				stepElementSetting.setSetting(new RangeLimit(BigDecimal.ZERO),
						new RangeLimit(BigDecimal.ZERO), new RangeLimit(BigDecimal.ZERO));
				elementSettings.add(stepElementSetting);
			}
		}

		history.elementSettings = elementSettings;

		// Ret.
		return history;
	}

	// =================== Memento State Support Method ===================
	/**
	 * Instantiates a new wage table history.
	 *
	 * @param memento
	 *            the memento
	 */
	public WtHistory(WtHistoryGetMemento memento) {
		this.companyCode = memento.getCompanyCode();
		this.wageTableCode = memento.getWageTableCode();
		this.historyId = memento.getHistoryId();
		this.applyRange = memento.getApplyRange();
		this.elementSettings = memento.getElementSettings();
		this.valueItems = memento.getValueItems();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(WtHistorySetMemento memento) {
		memento.setCompanyCode(this.companyCode);
		memento.setWageTableCode(this.wageTableCode);
		memento.setHistoryId(this.historyId);
		memento.setApplyRange(this.applyRange);
		memento.setElementSettings(this.elementSettings);
		memento.setValueItems(this.valueItems);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.History#getUuid()
	 */
	@Override
	public String getUuid() {
		return this.historyId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.History#getMasterCode()
	 */
	@Override
	public PrimitiveValue<String> getMasterCode() {
		return this.wageTableCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.History#getStart()
	 */
	@Override
	public YearMonth getStart() {
		return this.applyRange.getStartMonth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.History#getEnd()
	 */
	@Override
	public YearMonth getEnd() {
		return this.applyRange.getEndMonth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.base.simplehistory.History#setStart(nts.arc.time.
	 * YearMonth)
	 */
	@Override
	public void setStart(YearMonth yearMonth) {
		this.applyRange = MonthRange.range(yearMonth, this.applyRange.getEndMonth());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.base.simplehistory.History#setEnd(nts.arc.time.
	 * YearMonth)
	 */
	@Override
	public void setEnd(YearMonth yearMonth) {
		this.applyRange = MonthRange.range(this.applyRange.getStartMonth(), yearMonth);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.base.simplehistory.History#copyWithDate(nts.arc.
	 * time.YearMonth)
	 */
	@Override
	public WtHistory copyWithDate(YearMonth start) {
		WtHistory history = new WtHistory();
		history.companyCode = this.companyCode;
		history.wageTableCode = this.wageTableCode;
		history.applyRange = MonthRange.toMaxDate(start);

		Map<ElementId, ElementId> mapUuids = new HashMap<>();

		// Create new element setting.
		history.elementSettings = this.elementSettings.stream().map(item -> {

			// Check mode: is code mode
			if (item.getType().isCodeMode) {
				// Create new code items.
				List<CodeItem> itemList = item.getItemList().stream().map(subItem -> {
					CodeItem codeItem = (CodeItem) subItem;

					// Create new element id.
					ElementId newUuid = new ElementId(IdentifierUtil.randomUniqueId());

					// Mapping old id with new id.
					mapUuids.put(codeItem.getUuid(), newUuid);

					// Ret.
					return new CodeItem(codeItem.getReferenceCode(), newUuid);
				}).collect(Collectors.toList());

				// Return new setting.
				return new ElementSetting(item.getDemensionNo(), item.getType(), itemList);
			}

			// Check mode: is range mode
			if (item.getType().isRangeMode) {
				// Create new range items.
				List<RangeItem> itemList = item.getItemList().stream().map(subItem -> {
					RangeItem rangeItem = (RangeItem) subItem;

					// Create new element id.
					ElementId newUuid = new ElementId(IdentifierUtil.randomUniqueId());

					// Mapping old id with new id.
					mapUuids.put(rangeItem.getUuid(), newUuid);

					// Ret.
					return new RangeItem(rangeItem.getOrderNumber(), rangeItem.getStartVal(),
							rangeItem.getEndVal(), newUuid);
				}).collect(Collectors.toList());

				// Create new step setting.
				StepElementSetting stepElementSetting = (StepElementSetting) item;
				StepElementSetting el = new StepElementSetting(stepElementSetting.getDemensionNo(),
						stepElementSetting.getType(), itemList);
				el.setSetting(stepElementSetting.getLowerLimit(),
						stepElementSetting.getUpperLimit(), stepElementSetting.getInterval());

				// Ret
				return el;
			}

			// Ret
			return null;
		}).collect(Collectors.toList());

		// Create new item mapping with old uuid.
		history.valueItems = this.valueItems.stream()
				.map(item -> new WtItem(mapUuids.get(item.getElement1Id()),
						mapUuids.getOrDefault(item.getElement2Id(), ElementId.DEFAULT_VALUE),
						mapUuids.getOrDefault(item.getElement3Id(), ElementId.DEFAULT_VALUE),
						item.getAmount()))
				.collect(Collectors.toList());

		return history;
	}

	/**
	 * Creates the with intial.
	 *
	 * @param companyCode
	 *            the company code
	 * @param wageTableCode
	 *            the wage table code
	 * @param startYearMonth
	 *            the start year month
	 * @return the wage table history
	 */
	public static final WtHistory createWithIntial(String companyCode, WtCode wageTableCode,
			YearMonth startYearMonth) {
		WtHistory history = new WtHistory();
		history.companyCode = companyCode;
		history.wageTableCode = wageTableCode;
		history.applyRange = MonthRange.toMaxDate(startYearMonth);
		history.elementSettings = Collections.emptyList();
		history.valueItems = Collections.emptyList();
		return history;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((historyId == null) ? 0 : historyId.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WtHistory other = (WtHistory) obj;
		if (historyId == null) {
			if (other.historyId != null)
				return false;
		} else if (!historyId.equals(other.historyId))
			return false;
		return true;
	}
}
