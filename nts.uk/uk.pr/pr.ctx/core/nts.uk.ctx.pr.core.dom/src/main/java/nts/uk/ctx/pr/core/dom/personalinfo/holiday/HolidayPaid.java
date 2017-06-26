package nts.uk.ctx.pr.core.dom.personalinfo.holiday;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.shr.com.primitive.PersonId;

public class HolidayPaid extends AggregateRoot {
	/**
	 * Remain day.
	 */
	@Getter
	private BigDecimal remainDays;
	
	/**
	 * Remain time.
	 */
	@Getter
	private BigDecimal remainTime;
	
	/**
	 * Personal Id
	 */
	@Getter
	private PersonId personId;
	
	public HolidayPaid(BigDecimal remainDays, BigDecimal remainTime, PersonId personId) {
		this.remainDays = remainDays;
		this.remainTime = remainTime;
		this.personId = personId;
	}
	
	/**
	 * Create instance using Java type parameters.
	 * 
	 * @param remainDays remain Days
	 * @param remainTime remain Time
	 * @param personId personId
	 * @return HolidayPaid
	 */
	public static HolidayPaid createFromJavaType(BigDecimal remainDays, BigDecimal remainTime, String personId) {
		return new HolidayPaid(remainDays, remainTime, new PersonId(personId));
	}
}
