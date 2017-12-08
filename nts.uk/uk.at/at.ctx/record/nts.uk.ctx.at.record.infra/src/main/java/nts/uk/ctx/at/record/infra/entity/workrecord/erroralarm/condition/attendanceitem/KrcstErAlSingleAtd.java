/**
 * 5:26:40 PM Dec 5, 2017
 */
package nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.attendanceitem;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * @author hungnm
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KRCST_ERAL_SINGLE_ATD")
public class KrcstErAlSingleAtd extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public KrcstErAlSingleAtdPK krcstEralSingleAtdPK;
	@Basic(optional = false)
	@NotNull
	@Column(name = "TARGET_ATR")
	public BigDecimal targetAtr;

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
	@JoinColumns({ @JoinColumn(name = "CONDITION_GROUP_ID", referencedColumnName = "CONDITION_GROUP_ID", insertable = false, updatable = false),
			@JoinColumn(name = "ATD_ITEM_CON_NO", referencedColumnName = "ATD_ITEM_CON_NO", insertable = false, updatable = false) })
	public KrcmtErAlAtdItemCon krcmtErAlAtdItemCon;
	
	@Override
	protected Object getKey() {
		return this.krcstEralSingleAtdPK;
	}

	public KrcstErAlSingleAtd(KrcstErAlSingleAtdPK krcstEralSingleAtdPK, BigDecimal targetAtr) {
		super();
		this.krcstEralSingleAtdPK = krcstEralSingleAtdPK;
		this.targetAtr = targetAtr;
	}
	
}
