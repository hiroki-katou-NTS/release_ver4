/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.history.element.item.generator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.pr.core.dom.wagetable.ElementId;
import nts.uk.ctx.pr.core.dom.wagetable.ElementType;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.ElementSetting;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.StepElementSetting;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.Item;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.RangeItem;

/**
 * The Class StepItemGenerator.
 */
@Stateless
public class StepItemGenerator implements ItemGenerator {

	/** The unit l1. */
	private final BigDecimal UNIT_L1 = BigDecimal.ONE;

	/** The unit l2. */
	private final BigDecimal UNIT_L2 = BigDecimal.valueOf(0.1d);

	/** The unit l3. */
	private final BigDecimal UNIT_L3 = BigDecimal.valueOf(0.01d);

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.element.item.generator.
	 * ItemGenerator#generate(nts.uk.ctx.pr.core.dom.wagetable.history.element.
	 * ElementSetting)
	 */
	@Override
	public List<? extends Item> generate(String companyCode, String historyId,
			ElementSetting elementSetting) {
		// Get info.
		StepElementSetting stepElementSetting = (StepElementSetting) elementSetting;
		BigDecimal lowerLimit = stepElementSetting.getLowerLimit().v();
		BigDecimal upperLimit = stepElementSetting.getUpperLimit().v();
		BigDecimal interval = stepElementSetting.getInterval().v();

		// Get min step
		BigDecimal minStep = this.getMinUnit(lowerLimit, upperLimit, interval);

		// Ignore special case : init history.
		if (upperLimit.compareTo(BigDecimal.ZERO) == 0 && lowerLimit.compareTo(BigDecimal.ZERO) == 0
				&& interval.compareTo(BigDecimal.ZERO) == 0) {
			return Collections.emptyList();
		}

		// Lower limit is always less than upper limit.
		if (upperLimit.compareTo(lowerLimit) < 0) {
			// TODO: need msg id.
			throw new BusinessException(
					new RawErrorMessage("Lower limit must be always less than upper limit."));
		}

		// Interval is greater than zero.
		if (interval.compareTo(BigDecimal.ZERO) <= 0) {
			// TODO: need msg id.
			throw new BusinessException(new RawErrorMessage("Interval must be greater than zero."));
		}

		// Interval is invalid.
		if (upperLimit.subtract(lowerLimit).add(minStep).doubleValue() < interval.doubleValue()) {
			// TODO: need msg id.
			throw new BusinessException(new RawErrorMessage(
					"The range " + lowerLimit + " - " + upperLimit + " is not enough for 1 step"));
		}

		// Create map: unique code - old uuid.
		@SuppressWarnings("unchecked")
		List<RangeItem> rangeItems = (List<RangeItem>) elementSetting.getItemList();
		Map<String, ElementId> mapRangeItems = rangeItems.stream()
				.collect(Collectors.toMap(item -> this.getUniqueCode(item), RangeItem::getUuid));

		// Generate uuid of range items.
		List<RangeItem> items = new ArrayList<>();
		int index = 0;
		BigDecimal start = lowerLimit;
		while (start.compareTo(upperLimit) <= 0) {
			index++;
			BigDecimal end = start.add(interval).subtract(minStep);

			// Create new range item.
			RangeItem rangeItem = new RangeItem(index, start,
					((end.compareTo(upperLimit) <= 0) ? end : upperLimit),
					new ElementId(IdentifierUtil.randomUniqueId()));

			// Replace exist element id.
			rangeItem = new RangeItem(index, start,
					((end.compareTo(upperLimit) <= 0) ? end : upperLimit),
					mapRangeItems.getOrDefault(this.getUniqueCode(rangeItem), rangeItem.getUuid()));

			// Set display name.
			if (rangeItem.getStartVal().compareTo(rangeItem.getEndVal()) == 0) {
				rangeItem.setDisplayName(rangeItem.getStartVal()
						.setScale(StepItemGenerator.getScale(rangeItem.getStartVal()))
						.toEngineeringString());
			} else {
				rangeItem.setDisplayName(rangeItem.getStartVal()
						.setScale(StepItemGenerator.getScale(rangeItem.getStartVal()))
						.toEngineeringString()
						+ "～"
						+ rangeItem.getEndVal()
								.setScale(StepItemGenerator.getScale(rangeItem.getEndVal()))
								.toEngineeringString());
			}

			// Add item.
			items.add(rangeItem);

			// Add start value of next item.
			start = start.add(interval);
		}

		// Check has items.
		if (CollectionUtil.isEmpty(items)) {
			// TODO: need msg id.
			throw new BusinessException(new RawErrorMessage(
					"Have not any items on demension  " + elementSetting.getDemensionNo().value
							+ ": " + elementSetting.getType().displayName));
		}

		// Return
		return items;
	}

	/**
	 * Gets the unique code.
	 *
	 * @param rangeItem
	 *            the range item
	 * @return the unique code
	 */
	private String getUniqueCode(RangeItem rangeItem) {
		return String.format("%s:%s:%s", rangeItem.getOrderNumber().toString(),
				rangeItem.getStartVal().toString(), rangeItem.getEndVal().toString());
	}

	/**
	 * Gets the scale.
	 *
	 * @param num
	 *            the num
	 * @return the scale
	 */
	private static Integer getScale(BigDecimal num) {
		// Mutiply 100
		num = num.multiply(BigDecimal.valueOf(100d));
		if (num.doubleValue() == BigDecimal.ZERO.doubleValue()) {
			return 0;
		}

		// Count number of decimal.
		int counter = 2;
		while (num.remainder(BigDecimal.valueOf(10d)).intValue() == BigDecimal.ZERO.intValue()) {
			counter--;
			num = num.divide(BigDecimal.valueOf(10d));
		}

		// Return
		return counter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.element.item.generator.
	 * ItemGenerator#canHandle(nts.uk.ctx.pr.core.dom.wagetable.ElementType)
	 */
	@Override
	public boolean canHandle(ElementType type) {
		return type.isRangeMode;
	}

	/**
	 * Gets the min unit.
	 *
	 * @param nums
	 *            the nums
	 * @return the min unit
	 */
	private BigDecimal getMinUnit(BigDecimal... nums) {
		BigDecimal minStep = BigDecimal.valueOf(1d);
		for (BigDecimal num : nums) {
			BigDecimal step = this.getUnit(num);
			if (step.compareTo(minStep) == -1) {
				minStep = step;
			}
		}
		return minStep;
	}

	/**
	 * Gets the unit.
	 *
	 * @param num
	 *            the num
	 * @return the unit
	 */
	private BigDecimal getUnit(BigDecimal num) {
		num = num.multiply(BigDecimal.valueOf(100d));

		// Check number of decimal digits : number remainder 100
		if (BigDecimal.ZERO.intValue() == num.remainder(BigDecimal.valueOf(100d)).intValue()) {
			return UNIT_L1;
		}

		// Check number of decimal digits : number remainder 10
		if (BigDecimal.ZERO.intValue() == num.remainder(BigDecimal.valueOf(10d)).intValue()) {
			return UNIT_L2;
		}

		// Return default unit.
		return UNIT_L3;
	}
}
