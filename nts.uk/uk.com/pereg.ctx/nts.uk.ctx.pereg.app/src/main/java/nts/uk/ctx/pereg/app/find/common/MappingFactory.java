/**
 * 
 */
package nts.uk.ctx.pereg.app.find.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nts.gul.reflection.AnnotationUtil;
import nts.gul.reflection.FieldsWorkerStream;
import nts.gul.reflection.ReflectionUtil;
import nts.uk.ctx.pereg.app.find.layoutdef.classification.LayoutPersonInfoClsDto;
import nts.uk.ctx.pereg.app.find.layoutdef.classification.LayoutPersonInfoValueDto;
import nts.uk.ctx.pereg.dom.person.info.singleitem.DataTypeValue;
import nts.uk.shr.pereg.app.PeregItem;
import nts.uk.shr.pereg.app.find.dto.DataClassification;
import nts.uk.shr.pereg.app.find.dto.EmpOptionalDto;
import nts.uk.shr.pereg.app.find.dto.PeregDomainDto;
import nts.uk.shr.pereg.app.find.dto.PeregDto;
import nts.uk.shr.pereg.app.find.dto.PersonOptionalDto;

/**
 * @author danpv
 *
 */
public class MappingFactory {
	
	public static void mapItemClass(PeregDto peregDto, LayoutPersonInfoClsDto classItem) {

		// map data
		Map<String, Object> itemCodeValueMap = getFullDtoValue(peregDto);

		for (Object item : classItem.getItems()) {
			LayoutPersonInfoValueDto valueItem = (LayoutPersonInfoValueDto) item;
			valueItem.setValue(itemCodeValueMap.get(valueItem.getItemCode()));
		}

		// map record ID
		String recordId = peregDto.getDomainDto().getRecordId();
		for (Object item : classItem.getItems()) {
			LayoutPersonInfoValueDto valueItem = (LayoutPersonInfoValueDto) item;
			boolean optionItemNoValue = !itemCodeValueMap.containsKey(valueItem.getItemCode())
					&& valueItem.getItemCode().charAt(1) == 'O';
			if (!optionItemNoValue) {
				valueItem.setRecordId(recordId);
			}

		}

	}

	/**
	 * map peregDto to classItemList which is same category
	 * 
	 * @param peregDto
	 * @param classItemList
	 */
	public static void mapListItemClass(PeregDto peregDto, List<LayoutPersonInfoClsDto> classItemList) {

		// map data
		Map<String, Object> itemCodeValueMap = getFullDtoValue(peregDto);
		String recordId = peregDto.getDomainDto().getRecordId();
		for (LayoutPersonInfoClsDto classItem : classItemList) {
			for (Object item : classItem.getItems()) {
				LayoutPersonInfoValueDto valueItem = (LayoutPersonInfoValueDto) item;
				Object value = itemCodeValueMap.get(valueItem.getItemCode());
				if (valueItem.getItem() != null) {
					int itemType = valueItem.getItem().getDataTypeValue() ;
					if(itemType == DataTypeValue.SELECTION.value || 
							itemType == DataTypeValue.SELECTION_BUTTON.value || 
							itemType == DataTypeValue.SELECTION_RADIO.value) {
						value = value == null ? null : value.toString();
					}
				}
				valueItem.setValue(value);
				boolean optionItemNoValue = itemCodeValueMap.containsKey(valueItem.getItemCode());
				if (optionItemNoValue) {
					valueItem.setRecordId(recordId);
				}
			}
		}

		// map record ID
		// String recordId = peregDto.getDomainDto().getRecordId();
		// for (LayoutPersonInfoClsDto classItem : classItemList) {
		// for (Object item : classItem.getItems()) {
		// LayoutPersonInfoValueDto valueItem = (LayoutPersonInfoValueDto) item;
		// boolean optionItemNoValue =
		// itemCodeValueMap.containsKey(valueItem.getItemCode());
		// if (optionItemNoValue ) {
		// valueItem.setRecordId(recordId);
		// }
		// }
		// }

	}

	public static Map<String, Object> getFullDtoValue(PeregDto peregDto) {
		// Map<itemcode, Object: value of field>
		Map<String, Object> itemCodeValueMap = new HashMap<String, Object>();

		// map from domain data
		PeregDomainDto domainDto = peregDto.getDomainDto();
		if (domainDto == null) {
			return itemCodeValueMap;
		}

		FieldsWorkerStream lstField = AnnotationUtil.getStreamOfFieldsAnnotated(peregDto.getDtoClass(),
				PeregItem.class);
		lstField.forEach(field -> {
			String itemCode = field.getAnnotation(PeregItem.class).value();
			Object obj = ReflectionUtil.getFieldValue(field, domainDto);
			itemCodeValueMap.put(itemCode, obj);
		});

		// map from option data
		if (peregDto.getDataType() == DataClassification.EMPLOYEE) {
			peregDto.getEmpOptionalData()
					.forEach(empData -> itemCodeValueMap.put(empData.getItemCode(), empData.getValue()));
		} else {
			peregDto.getPerOptionalData()
					.forEach(perData -> itemCodeValueMap.put(perData.getItemCode(), perData.getValue()));
		}

		return itemCodeValueMap;
	}

	public static void matchPerOptionData(String recordId, List<LayoutPersonInfoClsDto> classItemList,
			List<PersonOptionalDto> dataItems) {
		for (LayoutPersonInfoClsDto classItem : classItemList) {
			for (Object item : classItem.getItems()) {
				LayoutPersonInfoValueDto valueItem = (LayoutPersonInfoValueDto) item;

				// recordId
				valueItem.setRecordId(recordId);

				// data
				for (PersonOptionalDto dataItem : dataItems) {
					if (valueItem.getItemCode().equals(dataItem.getItemCode())) {
						valueItem.setValue(dataItem.getValue());
					}
				}
			}
		}

	}

	public static void matchEmpOptionData(String recordId, List<LayoutPersonInfoClsDto> classItemList,
			List<EmpOptionalDto> dataItems) {
		for (LayoutPersonInfoClsDto classItem : classItemList) {
			for (Object item : classItem.getItems()) {
				LayoutPersonInfoValueDto valueItem = (LayoutPersonInfoValueDto) item;

				// recordId
				valueItem.setRecordId(recordId);

				// data
				for (EmpOptionalDto dataItem : dataItems) {
					if (valueItem.getItemCode().equals(dataItem.getItemCode())) {
						valueItem.setValue(dataItem.getValue());
					}
				}
			}
		}
	}

}
