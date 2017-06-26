/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.command.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.wagetable.DemensionNo;
import nts.uk.ctx.pr.core.dom.wagetable.ElementId;
import nts.uk.ctx.pr.core.dom.wagetable.ElementType;
import nts.uk.ctx.pr.core.dom.wagetable.WtCode;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtHistory;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtHistoryGetMemento;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtHistorySetMemento;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtItem;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.ElementSetting;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.RangeLimit;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.StepElementSetting;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.CodeItem;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.RangeItem;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class WtHistoryDto.
 */
@Setter
@Getter
public class WtHistoryDto {

	/** The history id. */
	private String historyId;

	/** The start month. */
	private Integer startMonth;

	/** The end month. */
	private Integer endMonth;

	/** The elements. */
	private List<ElementSettingDto> elements;

	/** The value items. */
	private List<WtItemDto> valueItems;

	/**
	 * To domain.
	 *
	 * @param wageTableCode
	 *            the wage table code
	 * @return the wt history
	 */
	public WtHistory toDomain(String wageTableCode) {
		WtHistoryDto dto = this;

		// Transfer data
		WtHistory wageTableHistory = new WtHistory(
				new WhdGetMemento(new WtCode(wageTableCode), dto));

		return wageTableHistory;
	}

	/**
	 * From domain.
	 *
	 * @param wtHistory
	 *            the wt history
	 * @return the wt history dto
	 */
	public WtHistoryDto fromDomain(WtHistory wtHistory, List<ElementSetting> generSettings) {
		WtHistoryDto dto = this;

		// Transfer data
		wtHistory.saveToMemento(new WhdSetMemento(dto, generSettings));

		return dto;
	}

	/**
	 * The Class WageTableHistoryDtoSetMemento.
	 */
	private class WhdSetMemento implements WtHistorySetMemento {

		/** The dto. */
		private WtHistoryDto dto;

		/** The element settings. */
		private List<ElementSetting> elementSettings;

		/**
		 * Instantiates a new wage table history dto set memento.
		 *
		 * @param dto
		 *            the dto
		 */
		public WhdSetMemento(WtHistoryDto dto, List<ElementSetting> generSettings) {
			this.dto = dto;
			this.elementSettings = generSettings;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistorySetMemento#
		 * setCompanyCode(nts.uk.ctx.core.dom.company.CompanyCode)
		 */
		@Override
		public void setCompanyCode(String companyCode) {
			// Do nothing.
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistorySetMemento#
		 * setWageTableCode(nts.uk.ctx.pr.core.dom.wagetable.WageTableCode)
		 */
		@Override
		public void setWageTableCode(WtCode code) {
			// Do nothing.
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistorySetMemento#
		 * setHistoryId(java.lang.String)
		 */
		@Override
		public void setHistoryId(String historyId) {
			this.dto.historyId = historyId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistorySetMemento#
		 * setApplyRange(nts.uk.ctx.pr.core.dom.insurance.MonthRange)
		 */
		@Override
		public void setApplyRange(MonthRange applyRange) {
			this.dto.startMonth = applyRange.getStartMonth().v();
			this.dto.endMonth = applyRange.getEndMonth().v();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistorySetMemento#
		 * setValueItems(java.util.List)
		 */
		@Override
		public void setValueItems(List<WtItem> valueItems) {
			this.dto.valueItems = valueItems.stream().map(item -> {
				WtItemDto dto = new WtItemDto();
				dto.fromDomain(item);
				return dto;
			}).collect(Collectors.toList());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WtHistorySetMemento#
		 * setElementSettings(java.util.List)
		 */
		@Override
		public void setElementSettings(List<ElementSetting> elementSettings) {
			// Create new setting as the current items.
			this.dto.elements = this.elementSettings.stream().map(item -> {
				ElementSettingDto elementSettingDto = new ElementSettingDto();
				elementSettingDto.setDemensionNo(item.getDemensionNo().value);
				elementSettingDto.setType(item.getType().value);

				// Code mode
				if (item.getType().isCodeMode) {
					elementSettingDto.setItemList(item.getItemList().stream().map(subItem -> {
						CodeItem codeItem = (CodeItem) subItem;
						return ElementItemDto.builder().uuid(codeItem.getUuid().v())
								.referenceCode(codeItem.getReferenceCode())
								.displayName(codeItem.getDisplayName()).build();
					}).collect(Collectors.toList()));
				}

				// Range mode
				if (item.getType().isRangeMode) {
					StepElementSetting stepElementSetting = (StepElementSetting) item;
					elementSettingDto.setLowerLimit(stepElementSetting.getLowerLimit().v());
					elementSettingDto.setUpperLimit(stepElementSetting.getUpperLimit().v());
					elementSettingDto.setInterval(stepElementSetting.getInterval().v());
					elementSettingDto.setItemList(item.getItemList().stream().map(subItem -> {
						RangeItem rangeItem = (RangeItem) subItem;
						return ElementItemDto.builder().uuid(rangeItem.getUuid().v())
								.orderNumber(rangeItem.getOrderNumber())
								.startVal(rangeItem.getStartVal()).endVal(rangeItem.getEndVal())
								.displayName(rangeItem.getDisplayName()).build();
					}).collect(Collectors.toList()));
				}

				return elementSettingDto;
			}).collect(Collectors.toList());
		}
	}

	/**
	 * The Class WageTableHistoryDtoGetMemento.
	 */
	private class WhdGetMemento implements WtHistoryGetMemento {

		/** The wage table code. */
		private WtCode wageTableCode;

		/** The dto. */
		private WtHistoryDto dto;

		/**
		 * Instantiates a new wage table history dto get memento.
		 *
		 * @param wageTableCode
		 *            the wage table code
		 * @param dto
		 *            the dto
		 */
		public WhdGetMemento(WtCode wageTableCode, WtHistoryDto dto) {
			this.wageTableCode = wageTableCode;
			this.dto = dto;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WtHistoryGetMemento#
		 * getValueItems()
		 */
		@Override
		public List<WtItem> getValueItems() {
			return this.dto.getValueItems().stream().map(item -> item.toDomain())
					.collect(Collectors.toList());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WtHistoryGetMemento#
		 * getHistoryId()
		 */
		@Override
		public String getHistoryId() {
			return this.dto.getHistoryId();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WtHistoryGetMemento#
		 * getCompanyCode()
		 */
		@Override
		public String getCompanyCode() {
			return AppContexts.user().companyCode();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WtHistoryGetMemento#
		 * getWageTableCode()
		 */
		@Override
		public WtCode getWageTableCode() {
			return this.wageTableCode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WtHistoryGetMemento#
		 * getApplyRange()
		 */
		@Override
		public MonthRange getApplyRange() {
			return MonthRange.range(new YearMonth(this.dto.getStartMonth()),
					new YearMonth(this.dto.getEndMonth()));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WtHistoryGetMemento#
		 * getElementSettings()
		 */
		@Override
		public List<ElementSetting> getElementSettings() {
			return this.dto.getElements().stream().map(item -> {

				// Code mode
				if (ElementType.valueOf(item.getType()).isCodeMode) {
					return new ElementSetting(DemensionNo.valueOf(item.getDemensionNo()),
							ElementType.valueOf(item.getType()),
							item.getItemList().stream()
									.map(codeItem -> new CodeItem(codeItem.getReferenceCode(),
											new ElementId(codeItem.getUuid())))
									.collect(Collectors.toList()));
				}

				// Range mode
				if (ElementType.valueOf(item.getType()).isRangeMode) {
					StepElementSetting stepSetting = new StepElementSetting(
							DemensionNo.valueOf(item.getDemensionNo()),
							ElementType.valueOf(item.getType()),
							item.getItemList().stream()
									.map(rangeItem -> new RangeItem(rangeItem.getOrderNumber(),
											rangeItem.getStartVal(), rangeItem.getEndVal(),
											new ElementId(rangeItem.getUuid())))
									.collect(Collectors.toList()));

					stepSetting.setSetting(new RangeLimit(item.getLowerLimit()),
							new RangeLimit(item.getUpperLimit()),
							new RangeLimit(item.getInterval()));

					// Ret.
					return stepSetting;
				}

				// Other
				return null;

			}).collect(Collectors.toList());
		}
	}

}
