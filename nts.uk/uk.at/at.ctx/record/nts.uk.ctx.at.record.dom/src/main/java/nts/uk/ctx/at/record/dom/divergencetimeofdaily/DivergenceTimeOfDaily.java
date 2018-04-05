package nts.uk.ctx.at.record.dom.divergencetimeofdaily;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
 * @author nampt
 * 日別実績の乖離時間
 *
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DivergenceTimeOfDaily {
	
	/** 乖離時間 */
	private List<DivergenceTime> divergenceTime;

}
