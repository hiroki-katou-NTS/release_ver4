package nts.uk.ctx.at.shared.dom.specialholidaynew;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 対象項目
 * 
 * @author tanlv
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TargetItem {
	/** 対象の欠勤枠 */
	private List<Integer> absenceFrameNo;
	
	/** 対象の特別休暇枠 */
	private List<Integer> frameNo;

	public static TargetItem createFromJavaType(List<Integer> absenceFrameNo, List<Integer> frameNo) {
		return new TargetItem(absenceFrameNo, frameNo);
	}
}
