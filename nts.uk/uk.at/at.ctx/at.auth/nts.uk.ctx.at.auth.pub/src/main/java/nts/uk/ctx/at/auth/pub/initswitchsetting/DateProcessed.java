/**
 * 
 */
package nts.uk.ctx.at.auth.pub.initswitchsetting;

import lombok.Setter;
import lombok.Value;
import nts.arc.time.YearMonth;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author hieult
 *
 */
@Value
@Setter
public class DateProcessed {
	private int closureID;
	private YearMonth targetDate;
	private DatePeriod datePeriod;
	
}
