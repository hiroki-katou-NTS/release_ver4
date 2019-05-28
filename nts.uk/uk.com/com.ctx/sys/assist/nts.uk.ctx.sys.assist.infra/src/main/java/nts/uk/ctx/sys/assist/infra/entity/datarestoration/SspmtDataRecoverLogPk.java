package nts.uk.ctx.sys.assist.infra.entity.datarestoration;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * データ復旧の結果ログ: 主キー情報
 */
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class SspmtDataRecoverLogPk implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * データ復旧処理ID
	 */
	@Basic(optional = false)
	@Column(name = "RECOVERY_PROCESS_ID")
	public String recoveryProcessId;

	/**
	 * 対象者
	 */
	@Basic(optional = false)
	@Column(name = "LOG_SEQUENCE_NUMBER")
	public int logSequenceNumber;
}
