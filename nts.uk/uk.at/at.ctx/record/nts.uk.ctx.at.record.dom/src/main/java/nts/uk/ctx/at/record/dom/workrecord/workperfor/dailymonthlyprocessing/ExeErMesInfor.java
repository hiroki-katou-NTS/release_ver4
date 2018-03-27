/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.SysErClass;

/**
 * 実行エラーメッセージ情報
 * 
 * @author tutk
 *
 */
@Getter
public class ExeErMesInfor {
	/**
	 * エラーメッセージ
	 */
	private String errorMessage;
	/**
	 * システムエラー区分
	 */
	private SysErClass sysErrorType;
	/**
	 * 就業計算と集計実行ログID
	 */
	private long empCalAndSumExecLogId;

	public ExeErMesInfor(String errorMessage, SysErClass sysErrorType, long empCalAndSumExecLogId) {
		super();
		this.errorMessage = errorMessage;
		this.sysErrorType = sysErrorType;
		this.empCalAndSumExecLogId = empCalAndSumExecLogId;
	}

}
