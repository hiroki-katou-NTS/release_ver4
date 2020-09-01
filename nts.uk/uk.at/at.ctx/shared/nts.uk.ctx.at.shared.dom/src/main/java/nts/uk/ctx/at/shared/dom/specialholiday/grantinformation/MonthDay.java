package nts.uk.ctx.at.shared.dom.specialholiday.grantinformation;

import lombok.Getter;
import lombok.Setter;

/**
 * 月日
 * @author masaaki_jinno
 *
 */
@Getter
@Setter
public class MonthDay {
	
	private int month;
	private int day;
	
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof YearMonth))
            return false;

        MonthDay mdc = (MonthDay) obj;
        return mdc.month == this.month && mdc.day == this.day;
    }
}