package nts.uk.shr.pereg.app.command;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;
import nts.gul.reflection.AnnotationUtil;
import nts.gul.reflection.ReflectionUtil;
import nts.uk.shr.pereg.app.ItemValue;
import nts.uk.shr.pereg.app.ItemValueType;
import nts.uk.shr.pereg.app.PeregEmployeeId;
import nts.uk.shr.pereg.app.PeregItem;
import nts.uk.shr.pereg.app.PeregPersonId;
import nts.uk.shr.pereg.app.PeregRecordId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemsByCategory {

	/** category code */
	private String categoryCd;

	/** Record Id, but this is null when new record */
	private String recordId;

	/** input items */
	private List<ItemValue> items;

	public Object createCommandForSystemDomain(String personId, String employeeId, Class<?> commandClass) {

		val command = ReflectionUtil.newInstance(commandClass);

		// set person ID
		AnnotationUtil.getFieldAnnotated(commandClass, PeregPersonId.class).ifPresent(field -> {
			ReflectionUtil.setFieldValue(field, command, personId);
		});

		// set employee ID
		AnnotationUtil.getFieldAnnotated(commandClass, PeregEmployeeId.class).ifPresent(field -> {
			ReflectionUtil.setFieldValue(field, command, employeeId);
		});

		// set record ID
		AnnotationUtil.getFieldAnnotated(commandClass, PeregRecordId.class).ifPresent(field -> {
			ReflectionUtil.setFieldValue(field, command, this.recordId);
		});
		
		// set item values
		val inputsMap = this.createInputsMap();

		AnnotationUtil.getStreamOfFieldsAnnotated(commandClass, PeregItem.class).forEach(field -> {
			String itemCode = field.getAnnotation(PeregItem.class).value();
			val inputItem = inputsMap.get(itemCode);
			if (inputItem != null) {
				if (inputItem.value() != null && field.getType() == String.class) {
					ReflectionUtil.setFieldValue(field, command, inputItem.value().toString());
				} else if (inputItem.itemValueType() == ItemValueType.SELECTION && inputItem.value() != null && field.getType() == BigDecimal.class) {
					ReflectionUtil.setFieldValue(field, command, new BigDecimal(Integer.parseInt(inputItem.value())));
				} else {
					ReflectionUtil.setFieldValue(field, command, inputItem.value());
				}
			}
		});

		return command;
	}

	private Map<String, ItemValue> createInputsMap() {
		return this.items.stream()
				// exclude user defined
				.filter(item -> isDefinedBySystem(item.itemCode()))
				.collect(Collectors.toMap(item -> item.itemCode(), item -> item));
	}

	public List<ItemValue> collectItemsDefinedByUser() {
		return this.items.stream().filter(item -> isDefinedByUser(item.itemCode())).collect(Collectors.toList());
	}

	private static boolean isDefinedBySystem(String itemCode) {
		return !isDefinedByUser(itemCode);
	}

	private static boolean isDefinedByUser(String itemCode) {
		return itemCode.charAt(1) == 'O';
	}
}
