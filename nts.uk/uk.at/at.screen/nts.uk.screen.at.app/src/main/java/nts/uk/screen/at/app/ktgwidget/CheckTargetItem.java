package nts.uk.screen.at.app.ktgwidget;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.YearMonth;

/**
 * 
 * @author tutk
 *
 */

@Getter
@Setter
@NoArgsConstructor
public class CheckTargetItem {
	
	private int closureId;
	
	private YearMonth yearMonth;

	public CheckTargetItem(int closureId, YearMonth yearMonth) {
		super();
		this.closureId = closureId;
		this.yearMonth = yearMonth;
	}
}
