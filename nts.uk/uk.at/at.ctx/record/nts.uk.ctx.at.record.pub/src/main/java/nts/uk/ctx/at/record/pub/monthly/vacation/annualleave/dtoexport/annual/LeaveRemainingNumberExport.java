package nts.uk.ctx.at.record.pub.monthly.vacation.annualleave.dtoexport.annual;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 休暇残数
 * 
 * @author masaaki_jinno
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class LeaveRemainingNumberExport {

	/**
	 * 日数
	 */
	protected double days;

	/**
	 * 時間
	 */
	protected Optional<Integer> minutes;

}
