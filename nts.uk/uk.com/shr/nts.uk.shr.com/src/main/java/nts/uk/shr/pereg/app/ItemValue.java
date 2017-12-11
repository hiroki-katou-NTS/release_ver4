package nts.uk.shr.pereg.app;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;

@AllArgsConstructor
@NoArgsConstructor
public class ItemValue {

	private String definitionId;
	private String itemCode;
	private String value;
	private int type;
	
	/**
	 * 個人情報項目定義ID
	 * @return 個人情報項目定義ID
	 */
	public String definitionId() {
		return this.definitionId;
	}
	
	/**
	 * 項目定義コード
	 * @return 項目定義コード
	 */
	public String itemCode() {
		return this.itemCode;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T value() {
		Object convertedValue;
		switch (this.itemValueType()) {
		case NUMERIC:
		case TIME:
		case TIMEPOINT:
			// In case of value is empty or null return default value
			if (StringUtils.isEmpty(this.value)){
				convertedValue = new BigDecimal(0);
			} else {
				convertedValue = new BigDecimal(this.value);
			}
			break;
		case STRING:
		case SELECTION:
			convertedValue = this.value;
			break;
		case DATE:
			// In case of value is empty or null return null 
			if (StringUtils.isEmpty(this.value)){
				convertedValue = null;
			} else {
				convertedValue = GeneralDate.fromString(this.value, "yyyy/MM/dd");
			}
			break;
		default:
			throw new RuntimeException("invalid type: " + this.type);
		}
		
		return (T)convertedValue;
	}
	
	public void setValue(Object obj) {
		switch (this.itemValueType()) {
		case NUMERIC:
		case TIME:
		case TIMEPOINT:
			this.value = obj.toString();
			break;
		case STRING:
		case SELECTION:
			this.value = obj.toString();
			break;
		case DATE:
			this.value = ((GeneralDate)obj).toString("yyyy/MM/dd");
			break;
		default:
			throw new RuntimeException("invalid type: " + this.type);
		}
	}
	
	public ItemValueType itemValueType() {
		return EnumAdaptor.valueOf(this.type, ItemValueType.class);
	}
}
