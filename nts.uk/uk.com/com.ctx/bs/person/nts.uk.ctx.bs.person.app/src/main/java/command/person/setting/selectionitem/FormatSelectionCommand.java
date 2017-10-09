package command.person.setting.selectionitem;

import lombok.Value;

@Value
public class FormatSelectionCommand {
	private int selectionCode;
	private boolean selectionCodeCharacter;
	private int selectionName;
	private int selectionExternalCode;
}
