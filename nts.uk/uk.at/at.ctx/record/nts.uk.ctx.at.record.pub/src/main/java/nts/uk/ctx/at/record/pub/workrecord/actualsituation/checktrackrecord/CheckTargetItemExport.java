package nts.uk.ctx.at.record.pub.workrecord.actualsituation.checktrackrecord;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.YearMonth;

@Getter
@Setter
@NoArgsConstructor
public class CheckTargetItemExport {
	
	private int closureId;
	
	private YearMonth yearMonth;

	public CheckTargetItemExport(int closureId, YearMonth yearMonth) {
		super();
		this.closureId = closureId;
		this.yearMonth = yearMonth;
	}
	
}
