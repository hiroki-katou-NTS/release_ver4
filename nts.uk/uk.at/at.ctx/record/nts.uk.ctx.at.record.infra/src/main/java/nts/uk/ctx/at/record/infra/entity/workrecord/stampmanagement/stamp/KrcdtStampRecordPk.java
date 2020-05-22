package nts.uk.ctx.at.record.infra.entity.workrecord.stampmanagement.stamp;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDateTime;

/**
 * @author ThanhNX
 *
 */
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class KrcdtStampRecordPk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 打刻カード番号
	 */
	@Basic(optional = false)
	@Column(name = "CARD_NUMBER")
	public String cardNumber;

	/**
	 * 打刻日時
	 */
	@Basic(optional = false)
	@Column(name = "STAMP_DATE_TIME")
	public GeneralDateTime stampDateTime;
}
