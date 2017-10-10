package nts.uk.ctx.bs.person.dom.person.setting.selectionitem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;

@AllArgsConstructor
@Getter
public class FormatSelection {
	private SelectionCodeLength selectionCode;
	private SelectionCodeCharacter selectionCodeCharacter;
	private SelectionNameLength selectionName;
	private SelectionExternalCDLength selectionExternalCode;

	public static FormatSelection createFromJavaType(int selectionCd, int characterTypeAtr, int selectionName,
			int selectionExtCd) {
		return new FormatSelection(new SelectionCodeLength(selectionCd),
				EnumAdaptor.valueOf(characterTypeAtr, SelectionCodeCharacter.class),
				new SelectionNameLength(selectionName),
				new SelectionExternalCDLength(selectionExtCd));

	}
}
