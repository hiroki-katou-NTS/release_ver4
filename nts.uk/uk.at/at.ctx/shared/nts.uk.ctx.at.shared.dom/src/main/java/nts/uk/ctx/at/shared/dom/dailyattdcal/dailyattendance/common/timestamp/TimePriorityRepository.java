package nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.common.timestamp;
/**
 * 
 * @author tutk
 *
 */

import java.util.Optional;

public interface TimePriorityRepository {
	public Optional<TimePriority> getByCid(String cid);
}
