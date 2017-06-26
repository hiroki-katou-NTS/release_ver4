package nts.uk.ctx.pr.formula.app.find.formula;

import lombok.Value;
import nts.uk.ctx.pr.formula.dom.formula.SimpleCalSetting;

@Value
public class SimpleCalSettingDto {

	private String itemCode;

	private String itemName;

	public static SimpleCalSettingDto fromDomain(SimpleCalSetting domain) {
		return new SimpleCalSettingDto(domain.getItemCode().v(), domain.getItemName());
	}
}
