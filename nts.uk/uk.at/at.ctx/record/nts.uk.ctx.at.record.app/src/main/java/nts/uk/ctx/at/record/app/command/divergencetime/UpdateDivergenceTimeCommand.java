package nts.uk.ctx.at.record.app.command.divergencetime;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class UpdateDivergenceTimeCommand {

	private DivergenceTime divTime;
	private List<TimeItemSet> timeItem;
	
}
