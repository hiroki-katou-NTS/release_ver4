package nts.uk.ctx.at.record.app.find.log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutputStartScreenKdw001D {
	
	private int closureId;
	
	private boolean lockDay;
	
	private boolean lockMonthy;
	
	private GeneralDate startCal;
	
	private GeneralDate endCal;

}
