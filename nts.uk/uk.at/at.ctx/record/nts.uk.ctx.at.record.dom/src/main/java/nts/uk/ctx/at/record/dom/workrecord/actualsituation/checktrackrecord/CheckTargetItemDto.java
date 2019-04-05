package nts.uk.ctx.at.record.dom.workrecord.actualsituation.checktrackrecord;

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
public class CheckTargetItemDto {
	
	private int closureId;
	
	private YearMonth yearMonth;

	public CheckTargetItemDto(int closureId, YearMonth yearMonth) {
		super();
		this.closureId = closureId;
		this.yearMonth = yearMonth;
	}
	
}
