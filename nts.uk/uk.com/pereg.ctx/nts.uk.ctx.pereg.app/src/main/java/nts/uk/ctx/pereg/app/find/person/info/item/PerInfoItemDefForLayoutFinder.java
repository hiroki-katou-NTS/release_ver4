package nts.uk.ctx.pereg.app.find.person.info.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.enums.EnumConstant;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHist;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistRepository;
import nts.uk.ctx.pereg.app.find.common.ComboBoxRetrieveFactory;
import nts.uk.ctx.pereg.app.find.layoutdef.classification.ActionRole;
import nts.uk.ctx.pereg.dom.person.info.item.IsRequired;
import nts.uk.ctx.pereg.dom.person.info.item.ItemType;
import nts.uk.ctx.pereg.dom.person.info.item.ItemTypeState;
import nts.uk.ctx.pereg.dom.person.info.item.PerInfoItemDefRepositoty;
import nts.uk.ctx.pereg.dom.person.info.item.PersonInfoItemDefinition;
import nts.uk.ctx.pereg.dom.person.info.selectionitem.ReferenceTypes;
import nts.uk.ctx.pereg.dom.person.info.setitem.SetItem;
import nts.uk.ctx.pereg.dom.person.info.singleitem.DataTypeValue;
import nts.uk.ctx.pereg.dom.person.info.singleitem.SingleItem;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.i18n.resource.I18NResourcesForUK;
import nts.uk.shr.pereg.app.ComboBoxObject;

@Stateless
public class PerInfoItemDefForLayoutFinder {

	@Inject
	I18NResourcesForUK ukResouce;

	@Inject
	private PerInfoItemDefRepositoty perInfoItemDefRepositoty;

	@Inject
	private ComboBoxRetrieveFactory comboBoxRetrieveFactory;

	@Inject
	AffCompanyHistRepository achFinder;

	/***
	 * set value for item
	 * 
	 * @param itemForLayout
	 * @param empId
	 * @param ctgType
	 * @param itemDef
	 * @param perInfoCd
	 * @param dispOrder
	 * @param isCtgViewOnly
	 * @param sDate
	 * @param mapListCombo
	 */
	public void setItemForLayout(PerInfoItemDefForLayoutDto itemForLayout, String empId, int ctgType,
			PersonInfoItemDefinition itemDef, String perInfoCd, int dispOrder, boolean isCtgViewOnly, GeneralDate sDate, Map<Integer, Map<String,  List<ComboBoxObject>>> combobox) {
		setData(itemForLayout, empId, ctgType, itemDef, perInfoCd, dispOrder, isCtgViewOnly, sDate, combobox);
		// get children by itemId list
		if(itemDef.getItemTypeState().getItemType() == ItemType.SET_ITEM) {
			List<PersonInfoItemDefinition> lstDomain = getListChildrenDef(itemDef.getItemTypeState());
			List<PerInfoItemDefForLayoutDto> lstChildren = getPerItemSet(lstDomain, empId, ctgType, itemDef.getItemCode().v(),
					perInfoCd, dispOrder, isCtgViewOnly, sDate, itemForLayout.getActionRole(), combobox);					
			for(int index = 0; index < lstDomain.size(); index++) {
				PersonInfoItemDefinition i = lstDomain.get(index);
				if(i.getItemTypeState().getItemType() == ItemType.SET_ITEM) {
					List<PersonInfoItemDefinition> lstGrandChild = getListChildrenDef(i.getItemTypeState());
					List<PerInfoItemDefForLayoutDto> lstGrandChildDto = getPerItemSet(lstGrandChild, empId, ctgType, i.getItemCode().v(),
							perInfoCd, dispOrder, isCtgViewOnly, sDate, lstChildren.get(index).getActionRole(), combobox);		
					lstChildren.addAll(lstGrandChildDto);
				}
			}
			itemForLayout.setLstChildItemDef(lstChildren);
		}
		
	}
	
	private void setData(PerInfoItemDefForLayoutDto itemForLayout, String empId, int ctgType,
			PersonInfoItemDefinition itemDef, String perInfoCd, int dispOrder, boolean isCtgViewOnly, GeneralDate sDate, Map<Integer, Map<String,  List<ComboBoxObject>>> combobox) {
		
		itemForLayout.setCtgType(ctgType);
		if (isCtgViewOnly)
			itemForLayout.setActionRole(ActionRole.VIEW_ONLY);
		itemForLayout.setId(itemDef.getPerInfoItemDefId());
		itemForLayout.setPerInfoCtgId(itemDef.getPerInfoCategoryId());
		itemForLayout.setPerInfoCtgCd(perInfoCd);
		itemForLayout.setItemCode(itemDef.getItemCode().v());
		itemForLayout.setItemName(itemDef.getItemName().v());
		itemForLayout.setItemDefType(itemDef.getItemTypeState().getItemType().value);

		itemForLayout.setIsRequired(itemDef.getIsRequired().value);
		itemForLayout.setDispOrder(dispOrder);

		itemForLayout.setSelectionItemRefType(itemDef.getSelectionItemRefType());
		itemForLayout.setItemTypeState(PerInfoItemDefFinder.createItemTypeStateDto(itemDef.getItemTypeState()));
		List<EnumConstant> selectionItemRefTypes = EnumAdaptor.convertToValueNameList(ReferenceTypes.class,
				ukResouce);
		itemForLayout.setSelectionItemRefTypes(selectionItemRefTypes);
		if (itemForLayout.getItemDefType() == 2) {
			SingleItem singleItemDom = (SingleItem) itemDef.getItemTypeState();
			DataTypeValue dataTypeValue = singleItemDom.getDataTypeState().getDataTypeValue();
			if (dataTypeValue == DataTypeValue.SELECTION || dataTypeValue == DataTypeValue.SELECTION_RADIO || dataTypeValue == DataTypeValue.SELECTION_BUTTON) {
				DataTypeStateDto dataTypeStateDto = PerInfoItemDefFinder.createDataTypeStateDto(singleItemDom.getDataTypeState());
				SelectionItemDto selectionItemDto = (SelectionItemDto) dataTypeStateDto;
				
				List<ComboBoxObject> lstCombo = getCombo(selectionItemDto, combobox, empId, sDate, itemDef.getIsRequired() == IsRequired.REQUIRED);
				itemForLayout.setLstComboxBoxValue(lstCombo);

			}
		}
	}
	
	private List<ComboBoxObject> getCombo(SelectionItemDto selectionItemDto, Map<Integer, Map<String, List<ComboBoxObject>>> combobox, String empId, GeneralDate sDate, boolean isRequired){
		List<Object> key = new ArrayList<Object>();
		ReferenceTypes dataType = selectionItemDto.getReferenceType();
		key.add(dataType.value);
		switch(dataType) {
			case DESIGNATED_MASTER :
				MasterRefConditionDto master = (MasterRefConditionDto)selectionItemDto;
				key.add(master.getMasterType());
				break;
			case CODE_NAME: 
				CodeNameRefTypeDto code = (CodeNameRefTypeDto)selectionItemDto;
				key.add(code.getTypeCode());
				break;
			case ENUM:
				EnumRefConditionDto enu = (EnumRefConditionDto)selectionItemDto;
				key.add(enu.getEnumName());
				break;
		}
		if(combobox.containsKey(key.get(0))) {
			Map<String, List<ComboBoxObject>> mapComboValue = combobox.get(key.get(0));
			if(mapComboValue.containsKey(key.get(1))) {
				return mapComboValue.get(key.get(1));
			}else {
				 List<ComboBoxObject> returnList =  getLstComboBoxValue(selectionItemDto, empId, sDate,
						isRequired);
				 mapComboValue.put((String) key.get(1), returnList);
				 combobox.put((Integer) key.get(0), mapComboValue);
				 return returnList;
			}
		}else {
			List<ComboBoxObject> returnList =  getLstComboBoxValue(selectionItemDto, empId, sDate,
					isRequired);
			Map<String, List<ComboBoxObject>> mapComboValue = new HashMap<>();
			mapComboValue.put((String) key.get(1), returnList);
			combobox.put((Integer) key.get(0), mapComboValue);
			return returnList;
		}
	}

	
	private List<PersonInfoItemDefinition> getListChildrenDef(ItemTypeState item){
		String contractCode = AppContexts.user().contractCode();
		// get itemId list of children
		SetItem setItem = (SetItem) item;
		List<String> items = setItem.getItems();
		
		// get children by itemId list
		List<PersonInfoItemDefinition> lstDomain = perInfoItemDefRepositoty.getPerInfoItemDefByListId(items,
				contractCode);
		return lstDomain;
	}

	/**
	 * get per item set from domain
	 * 
	 * @param item
	 * @return
	 */
	private List<PerInfoItemDefForLayoutDto> getPerItemSet(List<PersonInfoItemDefinition> lstDomain, String empId, int ctgType, String itemParentCode,
			String perInfoCd, int dispOrder, boolean ctgIsViewOnly, GeneralDate sDate, ActionRole actionRole, Map<Integer, Map<String,  List<ComboBoxObject>>> combobox) {
		// 1 set - 2 Single
		List<PerInfoItemDefForLayoutDto> lstResult = new ArrayList<>();		
		PerInfoItemDefForLayoutDto childItem;
		for (int i = 0; i < lstDomain.size(); i++) {
			childItem = new PerInfoItemDefForLayoutDto();
			childItem.setActionRole(actionRole);
			childItem.setItemParentCode(itemParentCode);
			setData(childItem, empId, ctgType, lstDomain.get(i), perInfoCd, dispOrder, ctgIsViewOnly,
					sDate,combobox) ;
			lstResult.add(childItem);
		}		
		return lstResult;
	}

	public List<ComboBoxObject> getLstComboBoxValue(SelectionItemDto selectionItemDto, String empId,
			GeneralDate baseDate, boolean isRequired) {

		GeneralDate standardDate = baseDate;
		if (baseDate == null) {
			AffCompanyHist affCompanyHist = achFinder.getAffCompanyHistoryOfEmployeeDesc(AppContexts.user().companyId(),
					empId);
			standardDate = affCompanyHist.getLstAffCompanyHistByEmployee().get(0).getLstAffCompanyHistoryItem().stream()
					.collect(Collectors.toList()).get(0).start();
		}
		return comboBoxRetrieveFactory.getComboBox(selectionItemDto, empId, standardDate, isRequired);
	}
}
