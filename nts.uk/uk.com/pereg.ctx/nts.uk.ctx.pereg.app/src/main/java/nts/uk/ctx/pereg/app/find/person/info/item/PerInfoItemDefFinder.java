package nts.uk.ctx.pereg.app.find.person.info.item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.enums.EnumConstant;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.pereg.app.command.person.info.category.GetListCompanyOfContract;
import nts.uk.ctx.pereg.app.find.person.category.PerInfoCategoryFinder;
import nts.uk.ctx.pereg.app.find.person.category.PerInfoCtgFullDto;
import nts.uk.ctx.pereg.app.find.person.setting.init.item.SelectionInitDto;
import nts.uk.ctx.pereg.app.find.person.setting.selectionitem.PerInfoSelectionItemDto;
import nts.uk.ctx.pereg.app.find.person.setting.selectionitem.PerInfoSelectionItemFinder;
import nts.uk.ctx.pereg.dom.person.additemdata.item.EmpInfoItemDataRepository;
import nts.uk.ctx.pereg.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.pereg.dom.person.info.category.PersonInfoCategory;
import nts.uk.ctx.pereg.dom.person.info.dateitem.DateItem;
import nts.uk.ctx.pereg.dom.person.info.dateitem.DateType;
import nts.uk.ctx.pereg.dom.person.info.item.ItemType;
import nts.uk.ctx.pereg.dom.person.info.item.ItemTypeState;
import nts.uk.ctx.pereg.dom.person.info.item.PerInfoItemDefRepositoty;
import nts.uk.ctx.pereg.dom.person.info.item.PersonInfoItemDefinition;
import nts.uk.ctx.pereg.dom.person.info.numericitem.NumericItem;
import nts.uk.ctx.pereg.dom.person.info.order.PerInfoItemDefOrder;
import nts.uk.ctx.pereg.dom.person.info.selectionitem.NumericButton;
import nts.uk.ctx.pereg.dom.person.info.selectionitem.ReadOnly;
import nts.uk.ctx.pereg.dom.person.info.selectionitem.ReadOnlyButton;
import nts.uk.ctx.pereg.dom.person.info.selectionitem.ReferenceTypes;
import nts.uk.ctx.pereg.dom.person.info.selectionitem.RelatedCategory;
import nts.uk.ctx.pereg.dom.person.info.selectionitem.SelectionButton;
import nts.uk.ctx.pereg.dom.person.info.selectionitem.SelectionItem;
import nts.uk.ctx.pereg.dom.person.info.selectionitem.SelectionRadio;
import nts.uk.ctx.pereg.dom.person.info.setitem.SetItem;
import nts.uk.ctx.pereg.dom.person.info.singleitem.DataTypeState;
import nts.uk.ctx.pereg.dom.person.info.singleitem.DataTypeValue;
import nts.uk.ctx.pereg.dom.person.info.singleitem.SingleItem;
import nts.uk.ctx.pereg.dom.person.info.stringitem.StringItem;
import nts.uk.ctx.pereg.dom.person.info.stringitem.StringItemDataType;
import nts.uk.ctx.pereg.dom.person.info.stringitem.StringItemType;
import nts.uk.ctx.pereg.dom.person.info.timeitem.TimeItem;
import nts.uk.ctx.pereg.dom.person.info.timepointitem.TimePointItem;
import nts.uk.ctx.pereg.dom.person.personinfoctgdata.item.PerInfoItemDataRepository;
import nts.uk.ctx.pereg.dom.person.setting.init.item.PerInfoInitValueSetItemRepository;
import nts.uk.ctx.pereg.dom.person.setting.selectionitem.selection.SelectionRepository;
import nts.uk.ctx.pereg.dom.roles.auth.item.PersonInfoItemAuthRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.i18n.resource.I18NResourcesForUK;

@Stateless
public class PerInfoItemDefFinder {

	@Inject
	private PerInfoItemDefRepositoty pernfoItemDefRep;

	@Inject
	I18NResourcesForUK ukResouce;

	@Inject
	private PerInfoCategoryFinder categoryFinder;

	@Inject
	private PerInfoSelectionItemFinder selectionItemFinder;

	@Inject
	private SelectionRepository selectionRepo;

	@Inject
	private EmpInfoItemDataRepository empInfoRepo;

	@Inject
	private PerInfoItemDataRepository perItemRepo;

	@Inject
	private PersonInfoItemAuthRepository itemAuthRepo;

	@Inject
	private PerInfoInitValueSetItemRepository itemInitRepo;

	@Inject
	private PerInfoCategoryRepositoty perInfoCtgRep;

	public PerInfoItemDefFullEnumDto getAllPerInfoItemDefByCtgId(String perInfoCtgId, int personEmployeeType) {
		List<PerInfoItemDefShowListDto> perInfoItemDefs = this.pernfoItemDefRep
				.getAllPerInfoItemDefByCategoryId(perInfoCtgId, PersonInfoItemDefinition.ROOT_CONTRACT_CODE).stream()
				.map(item -> {
					return new PerInfoItemDefShowListDto(item.getPerInfoItemDefId(), item.getItemName().v());
				}).collect(Collectors.toList());
		List<EnumConstant> dataTypeEnum = EnumAdaptor.convertToValueNameList(DataTypeValue.class, ukResouce);
		List<EnumConstant> stringItemTypeEnum = EnumAdaptor.convertToValueNameList(StringItemType.class, ukResouce);
		List<EnumConstant> stringItemDataTypeEnum = EnumAdaptor.convertToValueNameList(StringItemDataType.class,
				ukResouce);
		List<EnumConstant> dateItemTypeEnum = EnumAdaptor.convertToValueNameList(DateType.class, ukResouce);
		List<PerInfoSelectionItemDto> selectionItemLst = new ArrayList<>();
		if (personEmployeeType == 1) {
			selectionItemLst = this.selectionItemFinder.getAllSelectionItem(0);
		} else if (personEmployeeType == 2) {
			selectionItemLst = this.selectionItemFinder.getAllSelectionItem(1);
		}
		return new PerInfoItemDefFullEnumDto(dataTypeEnum, stringItemTypeEnum, stringItemDataTypeEnum, dateItemTypeEnum,
				selectionItemLst, perInfoItemDefs);
	};

	public List<PerInfoItemDefDto> getAllPerInfoItemDefByCtgId(String perInfoCtgId, String isAbolition) {
		if (isAbolition.equals("true")) {
			return this.pernfoItemDefRep
					.getAllPerInfoItemDefByCategoryIdWithoutSetItem(perInfoCtgId, AppContexts.user().contractCode())
					.stream().map(item -> {
						return mappingFromDomaintoDto(item, 0);
					}).collect(Collectors.toList());

		} else {
			return this.pernfoItemDefRep
					.getAllPerInfoItemDefByCategoryIdWithoutAbolition(perInfoCtgId, AppContexts.user().contractCode())
					.stream().map(item -> {
						return mappingFromDomaintoDto(item, 0);
					}).collect(Collectors.toList());

		}

	}

	public PerInfoItemChangeDefDto getPerInfoItemDefByIdOfOtherCompany(String perInfoItemDefId,
			int personEmployeeType) {

		PerInfoItemDefDto itemDefDto = this.pernfoItemDefRep
				.getPerInfoItemDefById(perInfoItemDefId, AppContexts.user().contractCode()).map(item -> {
					return mappingFromDomaintoDto(item, 0);
				}).orElse(null);

		PerInfoCtgFullDto ctgDto = this.categoryFinder.getPerInfoCtg(itemDefDto.getPerInfoCtgId());

		String itemDefaultName = this.pernfoItemDefRep.getItemDefaultName(ctgDto.getCategoryCode(),
				itemDefDto.getItemCode());
		PerInfoItemChangeDefDto item = mappingFromDomaintoChangeDto(itemDefDto, itemDefaultName, 0, personEmployeeType);
		return item;
	};

	private PerInfoItemChangeDefDto mappingFromDomaintoChangeDto(PerInfoItemDefDto itemDefDto, String defaultName,
			int dispOrder, int personEmployeeType) {
		List<EnumConstant> selectionItemRefTypes = EnumAdaptor.convertToValueNameList(ReferenceTypes.class, ukResouce);

		GeneralDate baseDateConvert = GeneralDate.today();
		List<SelectionInitDto> selectionLst = new ArrayList<>();
		if (itemDefDto.getItemTypeState().getItemType() == 2) {
			ItemTypeStateDto x = itemDefDto.getItemTypeState();
			if (x.getItemType() == 2) {
				SingleItemDto y = (SingleItemDto) x;
				if (y.getDataTypeState().getDataTypeValue() == 6) {
					SelectionItemDto selelection = (SelectionItemDto) y.getDataTypeState();
					if (selelection.getReferenceType().value == 2) {
						CodeNameRefTypeDto typeCode = (CodeNameRefTypeDto) selelection;
						if (personEmployeeType == 1) {
							selectionLst = this.selectionRepo
									.getAllSelectionByHistoryId(typeCode.getTypeCode(), baseDateConvert, 0).stream()
									.map(c -> SelectionInitDto.fromDomainSelection1(c)).collect(Collectors.toList());
						} else if (personEmployeeType == 2) {
							selectionLst = this.selectionRepo
									.getAllSelectionByHistoryId(typeCode.getTypeCode(), baseDateConvert, 1).stream()
									.map(c -> SelectionInitDto.fromDomainSelection1(c)).collect(Collectors.toList());
						}

					}
				}
			}

		}

		PerInfoItemChangeDefDto item = new PerInfoItemChangeDefDto(itemDefDto.getId(), itemDefDto.getPerInfoCtgId(),
				itemDefDto.getItemCode(), itemDefDto.getItemName(), defaultName, itemDefDto.getIsAbolition(),
				itemDefDto.getIsFixed(), itemDefDto.getIsRequired(), itemDefDto.getSystemRequired(),
				itemDefDto.getRequireChangable(), dispOrder, itemDefDto.getSelectionItemRefType(),
				itemDefDto.getItemTypeState(), selectionItemRefTypes,
				selectionLst.size() > 0 ? selectionLst.get(0).getSelectionItemName() : " ",
				// sua loi them sel item lst
				selectionLst);
		return item;
	}

	public PerInfoItemChangeDefDto getPerInfoItemDefById(String perInfoItemDefId, int personEmployeeType) {
		PerInfoItemChangeDefDto itemDto = this.pernfoItemDefRep
				.getPerInfoItemDefById(perInfoItemDefId, PersonInfoItemDefinition.ROOT_CONTRACT_CODE).map(item -> {
					return mappingFromDomaintoDto_for_Selection(item, 0, personEmployeeType);
				}).orElse(null);

		return itemDto;
	};

	public List<PerInfoItemDefDto> getPerInfoItemDefByListId(List<String> listItemDefId) {
		return this.pernfoItemDefRep
				.getPerInfoItemDefByListId(listItemDefId, PersonInfoItemDefinition.ROOT_CONTRACT_CODE).stream()
				.map(item -> {
					int dispOrder = this.pernfoItemDefRep.getItemDispOrderBy(item.getPerInfoCategoryId(),
							item.getPerInfoItemDefId());
					return mappingFromDomaintoDto(item, dispOrder);
				}).collect(Collectors.toList());
	};

	// Function get data for Layout
	public List<PerInfoItemDefDto> getAllPerInfoItemDefByCtgIdForLayout(String perInfoCtgId) {
		List<PersonInfoItemDefinition> itemDefs = this.pernfoItemDefRep
				.getAllPerInfoItemDefByCategoryId(perInfoCtgId, AppContexts.user().contractCode()).stream()
				.filter(e -> e.getItemParentCode().equals("")) // filter set
																// item or
																// single item
																// (has'nt
																// parent item)
				.collect(Collectors.toList());
		List<PerInfoItemDefOrder> itemOrders = this.pernfoItemDefRep.getPerInfoItemDefOrdersByCtgId(perInfoCtgId);
		return mappingItemAndOrder(itemDefs, itemOrders);
	};

	// Function get item used for Layout
	public List<PerInfoItemDefDto> getAllPerInfoItemUsedByCtgIdForLayout(String perInfoCtgId) {
		List<PersonInfoItemDefinition> itemDefs = this.pernfoItemDefRep
				.getAllPerInfoItemDefByCategoryId(perInfoCtgId, AppContexts.user().contractCode()).stream()
				.filter(e -> (e.getItemParentCode().equals("") && e.getIsAbolition().value == 0)) // filter
																									// set
				// item or
				// single item
				// (has'nt
				// parent item)
				.collect(Collectors.toList());
		List<PerInfoItemDefOrder> itemOrders = this.pernfoItemDefRep.getPerInfoItemDefOrdersByCtgId(perInfoCtgId);
		return mappingItemAndOrder(itemDefs, itemOrders);
	};

	public List<PerInfoItemDefDto> getAllPerInfoItemDefByCatgoryId(String perInfoCtgId) {
		List<PersonInfoItemDefinition> itemDefs = this.pernfoItemDefRep
				.getAllPerInfoItemDefByCategoryId(perInfoCtgId, AppContexts.user().contractCode()).stream()
				.filter(e -> e.getItemParentCode().equals("")).collect(Collectors.toList());
		List<PerInfoItemDefOrder> itemOrders = this.pernfoItemDefRep.getPerInfoItemDefOrdersByCtgId(perInfoCtgId);
		return mappingItemAndOrder(itemDefs, itemOrders);
	};

	public PerInfoItemDefDto getPerInfoItemDefByItemDefId(String perInfoItemDefId) {
		PersonInfoItemDefinition itemDef = this.pernfoItemDefRep
				.getPerInfoItemDefById(perInfoItemDefId, AppContexts.user().contractCode()).orElse(null);
		int dispOrder = this.pernfoItemDefRep.getItemDispOrderBy(itemDef.getPerInfoCategoryId(),
				itemDef.getPerInfoItemDefId());
		return mappingFromDomaintoDto(itemDef, dispOrder);
	}

	public PerInfoItemDefDto getPerInfoItemDefByIdForLayout(String perInfoItemDefId) {
		PersonInfoItemDefinition itemDef = this.pernfoItemDefRep
				.getPerInfoItemDefById(perInfoItemDefId, AppContexts.user().contractCode()).orElse(null);
		int dispOrder = this.pernfoItemDefRep.getItemDispOrderBy(itemDef.getPerInfoCategoryId(),
				itemDef.getPerInfoItemDefId());
		return mappingFromDomaintoDto(itemDef, dispOrder);
	}

	public List<PerInfoItemDefDto> getPerInfoItemDefByListIdForLayout(List<String> listItemDefId) {
		List<PersonInfoItemDefinition> itemDefinition = this.pernfoItemDefRep.getPerInfoItemDefByListIdv2(listItemDefId,
				AppContexts.user().contractCode());

		return itemDefinition.stream().map(i -> {
			int dispOrder = this.pernfoItemDefRep.getItemDispOrderBy(i.getPerInfoCategoryId(), i.getPerInfoItemDefId());
			return mappingFromDomaintoDto(i, dispOrder);

		}).collect(Collectors.toList());

	};

	// test bug hieu nang.
	public List<PerInfoItemDefDto> getPerInfoItemDefByIds(List<String> listItemDefId) {

		List<PersonInfoItemDefinition> itemDefinition = this.pernfoItemDefRep.getPerInfoItemDefByListIdv2(listItemDefId,
				AppContexts.user().contractCode());

		List<PersonInfoItemDefinition> itemDfChild = new ArrayList<>();
		List<String> idsChild = new ArrayList<>();
		List<String> idsChildInChild = new ArrayList<>();
		List<PersonInfoItemDefinition> itemDfChildinChild = new ArrayList<>();

		List<PerInfoItemDefDto> result = itemDefinition.stream().map(i -> {
			int dispOrder = this.pernfoItemDefRep.getItemDispOrderBy(i.getPerInfoCategoryId(), i.getPerInfoItemDefId());
			PerInfoItemDefDto perItemDefdto = mappingFromDomaintoDto(i, dispOrder);

			if (perItemDefdto.getItemTypeState().getItemType() == 1
					&& (((SetItemDto) perItemDefdto.getItemTypeState()).getItems() != null)) {
				idsChild.addAll(((SetItemDto) perItemDefdto.getItemTypeState()).getItems());
			}
			return perItemDefdto;
		}).collect(Collectors.toList());

		if (!idsChild.isEmpty()) {
			itemDfChild = this.pernfoItemDefRep.getPerInfoItemDefByListIdv2(idsChild,
					AppContexts.user().contractCode());

			List<PerInfoItemDefDto> listItemDefDtoChild = itemDfChild.stream().map(i -> {
				int dispOrder = this.pernfoItemDefRep.getItemDispOrderBy(i.getPerInfoCategoryId(),
						i.getPerInfoItemDefId());
				PerInfoItemDefDto perItemDefdtochild =  mappingFromDomaintoDto(i, dispOrder);
				
				if (perItemDefdtochild.getItemTypeState().getItemType() == 1
						&& (((SetItemDto) perItemDefdtochild.getItemTypeState()).getItems() != null)) {
					idsChildInChild.addAll(((SetItemDto) perItemDefdtochild.getItemTypeState()).getItems());
				}
				
				return perItemDefdtochild;
			}).collect(Collectors.toList());

			result.addAll(listItemDefDtoChild);
		}
		
		if(!idsChildInChild.isEmpty()) {
			itemDfChildinChild  = this.pernfoItemDefRep.getPerInfoItemDefByListIdv2(idsChildInChild,
					AppContexts.user().contractCode());
			
			List<PerInfoItemDefDto> listItemDefDtoChildInChild = itemDfChildinChild.stream().map(i -> {
				int dispOrder = this.pernfoItemDefRep.getItemDispOrderBy(i.getPerInfoCategoryId(),
						i.getPerInfoItemDefId());
				return  mappingFromDomaintoDto(i, dispOrder);
				
			}).collect(Collectors.toList());
			
			result.addAll(listItemDefDtoChildInChild);
		}
		
		return result;

	}

	// return list id of item definition if it's require;
	public List<String> getRequiredIds() {
		String companyId = AppContexts.user().companyId();
		String contractCd = AppContexts.user().contractCode();
		return this.pernfoItemDefRep.getRequiredIds(contractCd, companyId);
	}

	// mapping data from domain to DTO

	private List<PerInfoItemDefDto> mappingItemAndOrder(List<PersonInfoItemDefinition> itemDefs,
			List<PerInfoItemDefOrder> itemOrders) {
		return itemDefs.stream().map(i -> {
			PerInfoItemDefOrder itemOrder = itemOrders.stream()
					.filter(o -> o.getPerInfoItemDefId().equals(i.getPerInfoItemDefId())).findFirst().orElse(null);
			if (itemOrder != null) {
				return mappingFromDomaintoDto(i, itemOrder.getDispOrder().v());
			}
			return mappingFromDomaintoDto(i, 0);
		}).collect(Collectors.toList());
	}

	public PerInfoItemDefDto mappingFromDomaintoDto(PersonInfoItemDefinition itemDef, int dispOrder) {
		List<EnumConstant> selectionItemRefTypes = EnumAdaptor.convertToValueNameList(ReferenceTypes.class, ukResouce);
		ItemTypeStateDto itemTypeStateDto = createItemTypeStateDto(itemDef.getItemTypeState());
		return new PerInfoItemDefDto(itemDef.getPerInfoItemDefId(), itemDef.getPerInfoCategoryId(),
				itemDef.getItemCode().v(), itemDef.getItemParentCode().v(), itemDef.getItemName().v(),
				itemDef.getIsAbolition().value, itemDef.getIsFixed().value, itemDef.getIsRequired().value,
				itemDef.getSystemRequired().value, itemDef.getRequireChangable().value, dispOrder,
				itemDef.getSelectionItemRefType(), itemTypeStateDto, selectionItemRefTypes);
	}

	private PerInfoItemChangeDefDto mappingFromDomaintoDto_for_Selection(PersonInfoItemDefinition itemDef,
			int dispOrder, int personEmployeeType) {
		List<EnumConstant> selectionItemRefTypes = EnumAdaptor.convertToValueNameList(ReferenceTypes.class, ukResouce);
		PerInfoItemDefDto itemDefDto = mappingFromDomaintoDto(itemDef, 0);
		List<SelectionInitDto> selectionLst = new ArrayList<>();
		if (itemDefDto.getItemTypeState().getItemType() == 2) {
			ItemTypeStateDto x = itemDefDto.getItemTypeState();
			if (x.getItemType() == 2) {
				SingleItemDto y = (SingleItemDto) x;
				if (y.getDataTypeState().getDataTypeValue() == 6) {
					SelectionItemDto selelection = (SelectionItemDto) y.getDataTypeState();
					if (selelection.getReferenceType().value == 2) {
						CodeNameRefTypeDto typeCode = (CodeNameRefTypeDto) selelection;
						if (personEmployeeType == 1) {
							selectionLst = this.selectionRepo
									.getAllSelectionByHistoryId(typeCode.getTypeCode(), GeneralDate.today(), 0).stream()
									.map(c -> SelectionInitDto.fromDomainSelection1(c)).collect(Collectors.toList());
						} else if (personEmployeeType == 2) {
							selectionLst = this.selectionRepo
									.getAllSelectionByHistoryId(typeCode.getTypeCode(), GeneralDate.today(), 1).stream()
									.map(c -> SelectionInitDto.fromDomainSelection1(c)).collect(Collectors.toList());

						}
					}
				}
			}

		}
		return new PerInfoItemChangeDefDto(itemDefDto.getId(), itemDefDto.getPerInfoCtgId(), itemDefDto.getItemCode(),
				itemDefDto.getItemName(), "", itemDefDto.getIsAbolition(), itemDefDto.getIsFixed(),
				itemDefDto.getIsRequired(), itemDefDto.getSystemRequired(), itemDefDto.getRequireChangable(), dispOrder,
				itemDefDto.getSelectionItemRefType(), itemDefDto.getItemTypeState(), selectionItemRefTypes,
				selectionLst.size() > 0 ? selectionLst.get(0).getSelectionItemName() : " ",
				// sua loi them sel item lst
				selectionLst);
	}

	public static ItemTypeStateDto createItemTypeStateDto(ItemTypeState itemTypeState) {
		ItemType itemType = itemTypeState.getItemType();
		if (itemType == ItemType.SINGLE_ITEM) {
			SingleItem singleItemDom = (SingleItem) itemTypeState;
			return ItemTypeStateDto.createSingleItemDto(createDataTypeStateDto(singleItemDom.getDataTypeState()));
		} else {
			SetItem setItemDom = (SetItem) itemTypeState;
			return ItemTypeStateDto.createSetItemDto(setItemDom.getItems());
		}
	}

	public static DataTypeStateDto createDataTypeStateDto(DataTypeState dataTypeState) {
		int dataTypeValue = dataTypeState.getDataTypeValue().value;
		switch (dataTypeValue) {
		case 1:
			StringItem strItem = (StringItem) dataTypeState;
			return DataTypeStateDto.createStringItemDto(strItem.getStringItemLength().v(),
					strItem.getStringItemType().value, strItem.getStringItemDataType().value);
		case 2:
			NumericItem numItem = (NumericItem) dataTypeState;
			BigDecimal numericItemMin = numItem.getNumericItemMin() != null ? numItem.getNumericItemMin().v() : null;
			BigDecimal numericItemMax = numItem.getNumericItemMax() != null ? numItem.getNumericItemMax().v() : null;
			return DataTypeStateDto.createNumericItemDto(numItem.getNumericItemMinus().value,
					numItem.getNumericItemAmount().value, numItem.getIntegerPart().v(), numItem.getDecimalPart().v(),
					numericItemMin, numericItemMax);
		case 3:
			DateItem dItem = (DateItem) dataTypeState;
			return DataTypeStateDto.createDateItemDto(dItem.getDateItemType().value);
		case 4:
			TimeItem tItem = (TimeItem) dataTypeState;
			return DataTypeStateDto.createTimeItemDto(tItem.getMax().v(), tItem.getMin().v());
		case 5:
			TimePointItem tPointItem = (TimePointItem) dataTypeState;
			return DataTypeStateDto.createTimePointItemDto(tPointItem.getTimePointItemMin().v(),
					tPointItem.getTimePointItemMax().v());
		case 6:
			SelectionItem sItem = (SelectionItem) dataTypeState;
			return DataTypeStateDto.createSelectionItemDto(sItem.getReferenceTypeState());

		case 7:
			SelectionRadio rItem = (SelectionRadio) dataTypeState;
			return DataTypeStateDto.createSelectionRadioDto(rItem.getReferenceTypeState());

		case 8:
			SelectionButton bItem = (SelectionButton) dataTypeState;
			return DataTypeStateDto.createSelectionButtonDto(bItem.getReferenceTypeState());
	
		case 9: 
			ReadOnly rOnlyItem = (ReadOnly) dataTypeState;
			return DataTypeStateDto.createReadOnly(rOnlyItem.getReadText().v());
		
		case 10: 
			RelatedCategory reCtgDto = (RelatedCategory) dataTypeState;
			return DataTypeStateDto.createReadOnly(reCtgDto.getRelatedCtgCode().v());

		case 11: 
			NumericButton numbtnItem = (NumericButton) dataTypeState;
			return DataTypeStateDto.createNumericButtonDto(numbtnItem.getReadText().v());
			
		case 12: 
			ReadOnlyButton rOnlyButton = (ReadOnlyButton) dataTypeState;
			return DataTypeStateDto.createReadOnly(rOnlyButton.getReadText().v());
			
		default:
			return null;
		}
	}

	public boolean checkExistedSelectionItemId(String selectionItemId) {

		return this.pernfoItemDefRep.checkExistedSelectionItemId(selectionItemId);
	}

	// lanlt start
	// return list id of item definition if it's require;
	public List<ItemRequiredBackGroud> getAllRequiredIdsByCompanyID() {
		String companyId = AppContexts.user().companyId();
		String contractCd = AppContexts.user().contractCode();
		List<ItemRequiredBackGroud> itemRequiredLst = new ArrayList<>();
		this.pernfoItemDefRep.getAllRequiredIds(contractCd, companyId).stream().forEach(item -> {

			ItemRequiredBackGroud itemNameRequired = new ItemRequiredBackGroud();
			itemNameRequired.setRowId(item);
			itemNameRequired.setColumnKey("itemName");
			itemNameRequired.setState(toList("ntsgrid-alarm"));

			ItemRequiredBackGroud setting = new ItemRequiredBackGroud();
			setting.setRowId(item);
			setting.setColumnKey("setting");
			setting.setState(toList("ntsgrid-alarm"));

			ItemRequiredBackGroud other = new ItemRequiredBackGroud();
			other.setRowId(item);
			other.setColumnKey("otherAuth");
			other.setState(toList("ntsgrid-alarm"));

			itemRequiredLst.add(itemNameRequired);
			itemRequiredLst.add(setting);
			itemRequiredLst.add(other);

		});
		return itemRequiredLst;
	}
	
	/**
	 * @author lanlt
	 * dung cho man hinh B cua CPS009
	 * getAllItemRequiredIdsByCtgId
	 * @param ctgId
	 * @return
	 */
	public List<ItemRequiredBackGroud> getAllItemRequiredIdsByCtgId(String ctgId) {
		String contractCd = AppContexts.user().contractCode();
		List<ItemRequiredBackGroud> itemRequiredLst = new ArrayList<>();
		
		List<String> itemX = this.pernfoItemDefRep.getAllRequiredIdsByCtgId(contractCd, ctgId);
		itemX.stream().forEach(item -> {

			ItemRequiredBackGroud itemNameRequired = new ItemRequiredBackGroud();
			itemNameRequired.setRowId(item);
			itemNameRequired.setColumnKey("itemName");
			itemNameRequired.setState(toList("ntsgrid-alarm"));

			ItemRequiredBackGroud setting = new ItemRequiredBackGroud();
			setting.setRowId(item);
			setting.setColumnKey("setting");
			setting.setState(toList("ntsgrid-alarm"));

			ItemRequiredBackGroud other = new ItemRequiredBackGroud();
			other.setRowId(item);
			other.setColumnKey("otherAuth");
			other.setState(toList("ntsgrid-alarm"));

			itemRequiredLst.add(itemNameRequired);
			itemRequiredLst.add(setting);
			itemRequiredLst.add(other);

		});
		return itemRequiredLst;
	}

	private List<String> toList(String... item) {
		return Stream.of(item).collect(Collectors.toCollection(ArrayList::new));
	}

	public boolean isCheckData(String itemId) {

		String contractCd = AppContexts.user().contractCode();
		List<String> companyIdList = GetListCompanyOfContract.LIST_COMPANY_OF_CONTRACT;
		PersonInfoItemDefinition oldItem = this.pernfoItemDefRep.getPerInfoItemDefById(itemId, contractCd).orElse(null);
		PersonInfoCategory category = this.perInfoCtgRep.getPerInfoCategory(oldItem.getPerInfoCategoryId(), contractCd)
				.orElse(null);

		if (category == null) {
			return false;
		}

		List<String> perInfoCtgIds = this.perInfoCtgRep.getPerInfoCtgIdList(companyIdList,
				category.getCategoryCode().v());
		boolean itemAuth = this.itemAuthRepo.hasItemData(oldItem.getItemCode().v(), perInfoCtgIds),
				itemInit = this.itemInitRepo.hasItemData(oldItem.getItemCode().v(), perInfoCtgIds),
				isEmpData = this.empInfoRepo.hasItemData(oldItem.getItemCode().v(), perInfoCtgIds),
				isPerData = this.perItemRepo.hasItemData(perInfoCtgIds, oldItem.getItemCode().v());

		if (itemAuth || itemInit || isEmpData || isPerData) {
			return true;
		}
		return false;

	}
	// lanlt end

}
