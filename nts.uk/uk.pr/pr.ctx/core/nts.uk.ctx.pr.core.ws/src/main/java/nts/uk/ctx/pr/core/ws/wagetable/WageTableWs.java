/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.wagetable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.uk.ctx.pr.core.app.wagetable.command.WtInitCommand;
import nts.uk.ctx.pr.core.app.wagetable.command.WtInitCommandHandler;
import nts.uk.ctx.pr.core.app.wagetable.command.WtUpdateCommand;
import nts.uk.ctx.pr.core.app.wagetable.command.WtUpdateCommandHandler;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.ElementItemDto;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.ElementSettingDto;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.WtElementDto;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.WtHeadDto;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.WtHistoryDto;
import nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryBaseService;
import nts.uk.ctx.pr.core.dom.wagetable.DemensionNo;
import nts.uk.ctx.pr.core.dom.wagetable.ElementType;
import nts.uk.ctx.pr.core.dom.wagetable.WtCode;
import nts.uk.ctx.pr.core.dom.wagetable.WtHead;
import nts.uk.ctx.pr.core.dom.wagetable.WtHeadRepository;
import nts.uk.ctx.pr.core.dom.wagetable.element.WtElement;
import nts.uk.ctx.pr.core.dom.wagetable.element.WtElementRepository;
import nts.uk.ctx.pr.core.dom.wagetable.element.service.WtElementService;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtHistory;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtHistoryRepository;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.ElementSetting;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.RangeLimit;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.StepElementSetting;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.CodeItem;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.RangeItem;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.generator.ElementItemFactory;
import nts.uk.ctx.pr.core.dom.wagetable.history.service.WtHistoryService;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtCodeRef;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtCodeRefRepository;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtMasterRef;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtMasterRefRepository;
import nts.uk.ctx.pr.core.ws.base.simplehistory.SimpleHistoryWs;
import nts.uk.ctx.pr.core.ws.base.simplehistory.dto.HistoryModel;
import nts.uk.ctx.pr.core.ws.base.simplehistory.dto.MasterModel;
import nts.uk.ctx.pr.core.ws.wagetable.dto.DemensionItemDto;
import nts.uk.ctx.pr.core.ws.wagetable.dto.ElementTypeDto;
import nts.uk.ctx.pr.core.ws.wagetable.dto.SettingInfoInModel;
import nts.uk.ctx.pr.core.ws.wagetable.dto.SettingInfoOutModel;
import nts.uk.ctx.pr.core.ws.wagetable.dto.WageTableItemInModel;
import nts.uk.ctx.pr.core.ws.wagetable.dto.WageTableItemModel;
import nts.uk.ctx.pr.core.ws.wagetable.dto.WageTableModel;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class WageTableWs.
 */
@Path("pr/proto/wagetable")
@Produces(MediaType.APPLICATION_JSON)
public class WageTableWs extends SimpleHistoryWs<WtHead, WtHistory> {

	/** The wt init command handler. */
	@Inject
	private WtInitCommandHandler wtInitCommandHandler;

	/** The wt update command handler. */
	@Inject
	private WtUpdateCommandHandler wtUpdateCommandHandler;

	/** The wt head repo. */
	@Inject
	private WtHeadRepository wtHeadRepo;

	/** The wt history repo. */
	@Inject
	private WtHistoryRepository wtHistoryRepo;

	/** The wt history service. */
	@Inject
	private WtHistoryService wtHistoryService;

	/** The element item factory. */
	@Inject
	private ElementItemFactory elementItemFactory;

	/** The wt code ref repo. */
	@Inject
	private WtCodeRefRepository wtCodeRefRepo;

	/** The wt master ref repo. */
	@Inject
	private WtMasterRefRepository wtMasterRefRepo;

	/** The wt element repo. */
	@Inject
	private WtElementRepository wtElementRepo;

	/** The wt element service. */
	@Inject
	private WtElementService wtElementService;

	/**
	 * Find.
	 *
	 * @param id
	 *            the id
	 * @return the wage table model
	 */
	@POST
	@Path("find/{id}")
	public WageTableModel find(@PathParam("id") String id) {
		// Create model.
		WageTableModel model = new WageTableModel();

		// Get the company code.
		String companyCode = AppContexts.user().companyCode();

		// Get the detail history.
		Optional<WtHistory> optWageTableHistory = this.wtHistoryRepo.findHistoryByUuid(id);

		// Check exist.
		if (!optWageTableHistory.isPresent()) {
			// TODO: Find msg id
			throw new BusinessException(new RawErrorMessage("History is not exist."));
		}

		WtHistory wtHistory = optWageTableHistory.get();

		Optional<WtHead> optWageTable = this.wtHeadRepo.findByCode(wtHistory.getCompanyCode(),
				wtHistory.getWageTableCode().v());

		// Check exist.
		if (!optWageTable.isPresent()) {
			// TODO: Find msg id
			throw new BusinessException(new RawErrorMessage("History is not exist."));
		}

		WtHead wageTableHead = optWageTable.get();
		WtHeadDto headDto = new WtHeadDto();
		headDto.fromDomain(wageTableHead);

		// Set demension name
		headDto.getElements().stream().forEach(item -> {
			item.setDemensionName(this.wtElementService.getDemensionName(companyCode,
					item.getReferenceCode(), ElementType.valueOf(item.getType())));
		});

		model.setHead(headDto);

		// Create map demension no - name
		Map<Integer, String> mapDemensionNames = model.getHead().getElements().stream().collect(
				Collectors.toMap(WtElementDto::getDemensionNo, WtElementDto::getDemensionName));

		WtHistoryDto historyDto = new WtHistoryDto();

		// Get current setting with exist items.
		List<ElementSetting> currentSettings = elementItemFactory.generate(companyCode,
				wtHistory.getHistoryId(), wtHistory.getElementSettings());

		historyDto.fromDomain(wtHistory, currentSettings);

		// Set demension name
		historyDto.getElements().stream().forEach(item -> {
			item.setDemensionName(mapDemensionNames.get(item.getDemensionNo()));
		});

		model.setHistory(historyDto);

		// Return
		return model;
	}

	/**
	 * Find by month.
	 *
	 * @param baseMonth
	 *            the base month
	 * @return the list
	 */
	@POST
	@Path("findbymonth/{baseMonth}")
	public List<WageTableItemModel> findByMonth(@PathParam("baseMonth") Integer baseMonth) {
		// Find by month year value.
		List<WtHead> wtHeadList = wtHeadRepo.getWageTableByBaseMonth(baseMonth);

		// Return.
		return wtHeadList.stream().map(item -> WageTableItemModel.builder().code(item.getCode().v())
				.name(item.getName().v()).build()).collect(Collectors.toList());
	}

	/**
	 * Find by codes.
	 *
	 * @param input
	 *            the input
	 * @return the list
	 */
	@POST
	@Path("findbycodes")
	public List<WageTableItemModel> findByCodes(WageTableItemInModel input) {
		// Find by codes
		List<WtHead> wtHeadList = wtHeadRepo.getWageTableByCodes(input.getCodes());

		// Return
		return wtHeadList.stream().map(item -> WageTableItemModel.builder().code(item.getCode().v())
				.name(item.getName().v()).build()).collect(Collectors.toList());
	}

	/**
	 * Inits the table.
	 *
	 * @param command
	 *            the command
	 * @return the history model
	 */
	@POST
	@Path("init")
	public HistoryModel initTable(WtInitCommand command) {
		// Execute handler.
		WtHistory history = this.wtInitCommandHandler.handle(command);

		// Return.
		return HistoryModel.builder().uuid(history.getUuid()).start(history.getStart().v())
				.end(history.getEnd().v()).build();
	}

	/**
	 * Update.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("update")
	public void update(WtUpdateCommand command) {
		// Execute handler.
		this.wtUpdateCommandHandler.handle(command);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.ws.base.simplehistory.SimpleHistoryWs#
	 * loadMasterModelList()
	 */
	@Override
	@Path("masterhistory")
	@POST
	public List<MasterModel> loadMasterModelList() {
		// Get list of wage table.
		List<WtHead> wageTables = this.wtHeadRepo.findAll(AppContexts.user().companyCode());

		// Get list of wage table history.
		List<WtHistory> wageTableHistories = this.wtHistoryRepo
				.findAll(AppContexts.user().companyCode());

		// Group histories by unit code.
		Map<WtCode, List<HistoryModel>> historyMap = wageTableHistories.stream().collect(
				Collectors.groupingBy(WtHistory::getWageTableCode, Collectors.mapping((res) -> {
					return HistoryModel.builder().uuid(res.getUuid()).start(res.getStart().v())
							.end(res.getEnd().v()).build();
				}, Collectors.toList())));

		// Return
		return wageTables.stream().map(item -> {
			return MasterModel.builder().code(item.getCode().v()).name(item.getName().v())
					.historyList(historyMap.get(item.getCode())).build();
		}).collect(Collectors.toList());
	}

	/**
	 * Generate setting item.
	 *
	 * @param input
	 *            the input
	 * @return the setting info out model
	 */
	@POST
	@Path("reference/genitem")
	public SettingInfoOutModel generateSettingItem(SettingInfoInModel input) {
		// Create model.
		SettingInfoOutModel model = new SettingInfoOutModel();

		// Get the company code
		String companyCode = AppContexts.user().companyCode();

		// Convert to ElementSetting from dto.
		List<ElementSetting> elementSettings = input.getSettings().stream().map(item -> {
			if (ElementType.valueOf(item.getType()).isCodeMode) {
				return new ElementSetting(DemensionNo.valueOf(item.getDemensionNo()),
						ElementType.valueOf(item.getType()), Collections.emptyList());
			} else {
				StepElementSetting el = new StepElementSetting(
						DemensionNo.valueOf(item.getDemensionNo()),
						ElementType.valueOf(item.getType()), Collections.emptyList());
				el.setSetting(new RangeLimit(item.getLowerLimit()),
						new RangeLimit(item.getUpperLimit()), new RangeLimit(item.getInterval()));
				return el;
			}
		}).collect(Collectors.toList());

		// Generate setting items.
		elementSettings = elementItemFactory.generate(companyCode, input.getHistoryId(),
				elementSettings);

		// Put item name
		List<ElementSettingDto> elementSettingDtos = elementSettings.stream().map(item -> {

			ElementSettingDto elementSettingDto = ElementSettingDto.builder()
					.demensionNo(item.getDemensionNo().value).type(item.getType().value).build();

			if (item.getType().isCodeMode) {

				List<ElementItemDto> itemList = item.getItemList().stream().map(subItem -> {
					CodeItem codeItem = (CodeItem) subItem;
					return ElementItemDto.builder().uuid(codeItem.getUuid().v())
							.referenceCode(codeItem.getReferenceCode())
							.displayName(codeItem.getDisplayName()).build();
				}).collect(Collectors.toList());

				elementSettingDto.setItemList(itemList);
			} else {
				StepElementSetting stepElementSetting = (StepElementSetting) item;

				List<ElementItemDto> itemList = item.getItemList().stream().map(subItem -> {
					RangeItem rangeItem = (RangeItem) subItem;
					return ElementItemDto.builder().uuid(rangeItem.getUuid().v())
							.orderNumber(rangeItem.getOrderNumber())
							.startVal(rangeItem.getStartVal()).endVal(rangeItem.getEndVal())
							.displayName(rangeItem.getDisplayName()).build();
				}).collect(Collectors.toList());

				elementSettingDto.setItemList(itemList);
				elementSettingDto.setLowerLimit(stepElementSetting.getLowerLimit().v());
				elementSettingDto.setUpperLimit(stepElementSetting.getUpperLimit().v());
				elementSettingDto.setInterval(stepElementSetting.getInterval().v());
			}

			// Put demension name
			Optional<WtElement> optWtElement = this.wtElementRepo
					.findByHistoryId(input.getHistoryId());

			// Check exist
			if (!optWtElement.isPresent()) {
				// TODO: Pls add msg id
				throw new BusinessException(new RawErrorMessage("Element is not exist."));
			}

			elementSettingDto.setDemensionName(this.wtElementService.getDemensionName(companyCode,
					optWtElement.get().getReferenceCode(), item.getType()));

			// Return
			return elementSettingDto;

		}).collect(Collectors.toList());

		// Set model
		model.setElementSettings(elementSettingDtos);

		// Return
		return model;
	}

	/**
	 * Load demension selection list.
	 *
	 * @return the list
	 */
	@POST
	@Path("demensions")
	public List<DemensionItemDto> loadDemensionSelectionList() {
		String companyCode = AppContexts.user().companyCode();

		List<DemensionItemDto> items = new ArrayList<>();

		/** The age fix. */
		items.add(new DemensionItemDto(ElementType.AGE_FIX));

		/** The experience fix. */
		items.add(new DemensionItemDto(ElementType.EXPERIENCE_FIX));

		/** The family mem fix. */
		items.add(new DemensionItemDto(ElementType.FAMILY_MEM_FIX));

		/** The master ref. */
		List<WtMasterRef> wtMasterRefs = this.wtMasterRefRepo.findAll(companyCode);
		wtMasterRefs.stream().forEach(item -> {
			items.add(new DemensionItemDto(ElementType.MASTER_REF, item.getRefNo().v(),
					item.getRefName()));
		});

		/** The code ref. */
		List<WtCodeRef> wtCodeRefs = this.wtCodeRefRepo.findAll(companyCode);
		wtCodeRefs.stream().forEach(item -> {
			items.add(new DemensionItemDto(ElementType.CODE_REF, item.getRefNo().v(),
					item.getRefName()));
		});

		/** The item data ref. */
		items.add(new DemensionItemDto(ElementType.ITEM_DATA_REF));

		// Extend element type
		/** The certification. */
		items.add(new DemensionItemDto(ElementType.CERTIFICATION));

		/** The working day. */
		items.add(new DemensionItemDto(ElementType.WITHOUT_WORKING_DAY));

		/** The come late. */
		items.add(new DemensionItemDto(ElementType.COME_LATE));

		/** The level. */
		items.add(new DemensionItemDto(ElementType.LEVEL));

		return items;
	}

	/** The Constant ELEMENT_TYPE_DTO. */
	private static final List<ElementTypeDto> ELEMENT_TYPE_DTO;
	static {
		ELEMENT_TYPE_DTO = new ArrayList<>();
		for (ElementType type : ElementType.values()) {
			ElementTypeDto dto = ElementTypeDto.builder().value(type.value)
					.isCodeMode(type.isCodeMode).isRangeMode(type.isRangeMode)
					.displayName(type.displayName).build();
			ELEMENT_TYPE_DTO.add(dto);
		}
	}

	/**
	 * Load element type dto.
	 *
	 * @return the list
	 */
	@POST
	@Path("elements")
	public List<ElementTypeDto> loadElementTypeDto() {
		return ELEMENT_TYPE_DTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.ws.base.simplehistory.SimpleHistoryWs#getServices()
	 */
	@Override
	protected SimpleHistoryBaseService<WtHead, WtHistory> getServices() {
		return this.wtHistoryService;
	}
}
