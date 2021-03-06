package nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto;

/**
 * エラーフラグ
 * 
 * @author vunv
 *
 */
public enum ErrorFlagImport {
	/** エラーなし */
	NO_ERROR(0),
	/** 承認者なし */
	NO_APPROVER(1),
	/** 確定者なし */
	NO_CONFIRM_PERSON(2),
	/** 承認者10人以上（フェーズ毎に） */
	APPROVER_UP_10(3);

	public int value;

	ErrorFlagImport(int type) {
		this.value = type;
	}
}
