package nts.uk.ctx.at.shared.dom.common.anyitem;

import nts.arc.primitive.TimeDurationPrimitiveValue;
import nts.arc.primitive.constraint.TimeRange;

/**
 * 月次任意時間
 * @author shuichu_ishida
 */
@TimeRange(min = "-999999:59", max = "999999:59")
public class AnyTimeMonth extends TimeDurationPrimitiveValue<AnyTimeMonth> {

	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * @param minutes 分（0:00からの経過）
	 */
	public AnyTimeMonth(int minutes){
		super(minutes);
	}
}
