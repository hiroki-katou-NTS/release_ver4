package nts.uk.ctx.at.request.dom.setting.workplace;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * 申請利用設定の備考
 * @author Doan Duy Hung
 *
 */
@StringMaxLength(400)
public class AppUseSetRemark extends StringPrimitiveValue<AppUseSetRemark> {

	public AppUseSetRemark(String rawValue) {
		super(rawValue);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
