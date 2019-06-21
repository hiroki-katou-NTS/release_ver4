package nts.uk.ctx.at.record.dom.adapter.initswitchsetting;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @author sonnh1
 *
 */
@Data
@AllArgsConstructor
public class InitSwitchSetDto {
	/**
	 * Enum 当月 = 1 翌月 = 2
	 **/
	private int currentOrNextMonth;
	private List<DateProcessedRecord> listDateProcessed;
}
