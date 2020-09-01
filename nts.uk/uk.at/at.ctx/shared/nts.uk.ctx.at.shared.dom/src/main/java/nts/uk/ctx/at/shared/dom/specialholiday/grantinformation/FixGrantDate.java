package nts.uk.ctx.at.shared.dom.specialholiday.grantinformation;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 指定日付与
 * @author masaaki_jinno
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FixGrantDate {
	
	/** 付与日数 */
	private RegularGrantDays grantDays;

	/** 期限 */
	
	/** 付与月日 */
	private Optional<MonthDay> grantMonthDay;
	
}
