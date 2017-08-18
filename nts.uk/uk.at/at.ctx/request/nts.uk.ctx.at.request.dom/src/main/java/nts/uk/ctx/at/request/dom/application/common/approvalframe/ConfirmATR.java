package nts.uk.ctx.at.request.dom.application.common.approvalframe;
/**
 * 
 * @author hieult
 *
 */
public enum ConfirmATR {

	/** 0: 使用しない */
	USEATR_NOTUSE(0),
	/** 1: 使用する */
	USEATR_USE(1);

	public int value;

	ConfirmATR(int type) {
		this.value = type;
	}
}
