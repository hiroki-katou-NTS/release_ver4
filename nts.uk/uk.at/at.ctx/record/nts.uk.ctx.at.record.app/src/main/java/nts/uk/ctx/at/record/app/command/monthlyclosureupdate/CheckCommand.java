package nts.uk.ctx.at.record.app.command.monthlyclosureupdate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CheckCommand {

	private Integer closureId;
	
	private MonthlyClosureResponse screenParams;
	
	private Boolean overTime;
	
}
