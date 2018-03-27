package nts.uk.ctx.at.record.dom.divergence.time.history;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class DivergenceReferenceTimeValue.
 */
// 乖離時間の基準値
@Getter
@Setter
public class DivergenceReferenceTimeValue extends DomainObject {
	
	/** The alarm time. */
	// アラーム時間
	private Optional<DivergenceReferenceTime> alarmTime; 
	
	/** The error time. */
	// エラー時間
	private Optional<DivergenceReferenceTime> errorTime;
	
	/**
	 * Instantiates a new divergence reference time value.
	 *
	 * @param alarmTime the alarm time
	 * @param errorTime the error time
	 */
	public DivergenceReferenceTimeValue(DivergenceReferenceTime alarmTime, DivergenceReferenceTime errorTime) {
		this.alarmTime = Optional.of(alarmTime);
		this.errorTime = Optional.of(errorTime);
	}
}
