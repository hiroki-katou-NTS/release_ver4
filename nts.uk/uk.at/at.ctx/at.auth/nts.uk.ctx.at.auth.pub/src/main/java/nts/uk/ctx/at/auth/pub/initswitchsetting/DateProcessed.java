/**
 * 
 */
package nts.uk.ctx.at.auth.pub.initswitchsetting;

import lombok.Value;
import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author hieult
 *
 */
@Value
public class DateProcessed {
	private int closureID;
	private GeneralDate targetDate;
	private DatePeriod datePeriod;
	
}
