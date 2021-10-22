package nts.uk.ctx.exio.infra.entity.exi.dataformat;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 時間型データ形式設定: 主キー情報
 */
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class OiomtTimeDataFmSetPk implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 会社ID
	 */
	@Basic(optional = false)
	@Column(name = "CID")
	public String cid;

	/**
	 * 条件設定コード
	 */
	@Basic(optional = false)
	@Column(name = "CONDITION_SET_CD")
	public String conditionSetCd;

	/**
	 * 受入項目番号
	 */
	@Basic(optional = false)
	@Column(name = "ACCEPT_ITEM_NUM")
	public int acceptItemNumber;

}
