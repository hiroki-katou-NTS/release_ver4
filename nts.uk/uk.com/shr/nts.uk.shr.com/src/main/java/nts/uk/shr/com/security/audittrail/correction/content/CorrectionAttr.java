package nts.uk.shr.com.security.audittrail.correction.content;

import lombok.RequiredArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.uk.shr.com.i18n.TextResource;

/**
 * 修正区分
 */
@RequiredArgsConstructor
public enum CorrectionAttr {

	/** 手修正 */
	EDIT(0),
	
	/** 計算 */
	CALCULATE(1),

	/** 反映 */
	REFLECT(2),
	
	;
	public final int value;

	private static final String TEXT_EDIT = TextResource.localize("Enum_CorrectionAttr_EDIT");
	private static final String TEXT_CALCULATE = TextResource.localize("Enum_CorrectionAttr_CALCULATE");
	private static final String TEXT_REFLECT = TextResource.localize("Enum_CorrectionAttr_REFLECT");
	
	public static CorrectionAttr of(int value) {
		return EnumAdaptor.valueOf(value, CorrectionAttr.class);
	}
	
	public String localize() {
		switch (this) {
		case EDIT:
			return TEXT_EDIT;
		case CALCULATE:
			return TEXT_CALCULATE;
		case REFLECT:
			return TEXT_REFLECT;
		default:
			return "";
		}
	}
}
