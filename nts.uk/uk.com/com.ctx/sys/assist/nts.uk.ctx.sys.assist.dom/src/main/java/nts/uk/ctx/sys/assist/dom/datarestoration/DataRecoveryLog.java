package nts.uk.ctx.sys.assist.dom.datarestoration;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.arc.time.GeneralDate;

/**
 * データ復旧の結果ログ
 */
@Getter
public class DataRecoveryLog extends DomainObject {

	/**
	 * データ復旧処理ID
	 */
	private String recoveryProcessId;

	/**
	 * LOG_SEQUENCE_NUMBER ログ連番
	 */
	private int logSequenceNumber;

	/**
	 * 処理内容
	 */
	private ProcessingRecoverContent processingContent;

	/**
	 * 対象者
	 */
	private String target;

	/**
	 * 対象日
	 */
	private GeneralDate targetDate;

	/**
	 * エラー内容
	 */
	private RecoveryErrDescript errorContent;

	/**
	 * 実行SQL
	 */
	private ContentSql contentSql;

	public DataRecoveryLog(String recoveryProcessId, String target, String errorContent, GeneralDate targetDate,
			int logSequenceNumber, String processingContent, String contentSql) {
		this.recoveryProcessId = recoveryProcessId;
		this.target = target;
		this.errorContent = new RecoveryErrDescript(errorContent.length() > 1995 ? errorContent.substring(0,1995) : errorContent);
		this.targetDate = targetDate;
		this.logSequenceNumber = logSequenceNumber;
		this.processingContent = new ProcessingRecoverContent(processingContent.length() > 95 ? processingContent.substring(0,95) : processingContent);
		this.contentSql = new ContentSql(contentSql.length() > 1995 ? contentSql.substring(0,1995) : contentSql);
	}

	public static DataRecoveryLog createFromJavatype(String recoveryProcessId, String target, String errorContent,
			GeneralDate targetDate, int logSequenceNumber, String processingContent, String contentSql) {
		
		return new DataRecoveryLog(recoveryProcessId, target, errorContent, targetDate, logSequenceNumber,
				processingContent, contentSql);
	}
}
