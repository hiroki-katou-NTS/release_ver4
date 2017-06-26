/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nts.uk.ctx.pr.core.infra.entity.itemmaster;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 *
 * @author chinhbv
 */
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "QCAMT_ITEM_DEDUCT")
@Entity
public class QcamtItemDeduct extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public QcamtItemDeductPK qcamtItemDeductPK;

	@Basic(optional = false)
	@Column(name = "DEDUCT_ATR")
	public int deductAtr;
	@Basic(optional = false)
	@Column(name = "ERR_RANGE_LOW_ATR")
	public int errRangeLowAtr;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Basic(optional = false)
	@Column(name = "ERR_RANGE_LOW")
	public BigDecimal errRangeLow;
	@Basic(optional = false)
	@Column(name = "ERR_RANGE_HIGH_ATR")
	public int errRangeHighAtr;
	@Basic(optional = false)
	@Column(name = "ERR_RANGE_HIGH")
	public BigDecimal errRangeHigh;
	@Basic(optional = false)
	@Column(name = "AL_RANGE_LOW_ATR")
	public int alRangeLowAtr;
	@Basic(optional = false)
	@Column(name = "AL_RANGE_LOW")
	public BigDecimal alRangeLow;
	@Basic(optional = false)
	@Column(name = "AL_RANGE_HIGH_ATR")
	public int alRangeHighAtr;
	@Basic(optional = false)
	@Column(name = "AL_RANGE_HIGH")
	public BigDecimal alRangeHigh;
	@Column(name = "MEMO")
	public String memo;

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qcamtItemDeductPK != null ? qcamtItemDeductPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof QcamtItemDeduct)) {
			return false;
		}
		QcamtItemDeduct other = (QcamtItemDeduct) object;
		if ((this.qcamtItemDeductPK == null && other.qcamtItemDeductPK != null)
				|| (this.qcamtItemDeductPK != null && !this.qcamtItemDeductPK.equals(other.qcamtItemDeductPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.itemmaster.QcamtItemDeduct[ qcamtItemDeductPK=" + qcamtItemDeductPK + " ]";
	}

	@Override
	protected QcamtItemDeductPK getKey() {
		return this.qcamtItemDeductPK;
	}
}
