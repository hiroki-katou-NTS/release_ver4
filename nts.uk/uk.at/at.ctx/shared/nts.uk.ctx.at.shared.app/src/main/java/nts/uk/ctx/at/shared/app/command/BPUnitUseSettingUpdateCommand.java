package nts.uk.ctx.at.shared.app.command;

import lombok.AllArgsConstructor;
import lombok.Value;
@AllArgsConstructor
@Value
public class BPUnitUseSettingUpdateCommand {
		public int workplaceUseAtr;
		public int personalUseAtr;
		public int workingTimesheetUseAtr;

}
