package command.person.setting.selection;

import lombok.Value;

@Value
public class FormatSelectionCommand {
	private int selectionCode;
	private int selectionCodeCharacter;
	private int selectionName;
	private int selectionExternalCode;
}
