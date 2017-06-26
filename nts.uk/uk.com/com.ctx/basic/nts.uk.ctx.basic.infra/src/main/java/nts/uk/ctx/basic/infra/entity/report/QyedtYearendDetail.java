/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.basic.infra.entity.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class QyedtYearendDetail.
 */
@Setter
@Getter
@Entity
@Table(name = "QYEDT_YEAREND_DETAIL")
public class QyedtYearendDetail implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qyedt yearend detail PK. */
	@EmbeddedId
	protected QyedtYearendDetailPK qyedtYearendDetailPK;

	/** The ins date. */
	@Column(name = "INS_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insDate;

	/** The ins ccd. */
	@Column(name = "INS_CCD")
	private String insCcd;

	/** The ins scd. */
	@Column(name = "INS_SCD")
	private String insScd;

	/** The ins pg. */
	@Column(name = "INS_PG")
	private String insPg;

	/** The upd date. */
	@Column(name = "UPD_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updDate;

	/** The upd ccd. */
	@Column(name = "UPD_CCD")
	private String updCcd;

	/** The upd scd. */
	@Column(name = "UPD_SCD")
	private String updScd;

	/** The upd pg. */
	@Column(name = "UPD_PG")
	private String updPg;

	/** The exclus ver. */
	@Basic(optional = false)
	@Column(name = "EXCLUS_VER")
	private int exclusVer;

	/** The inv scd. */
	@Column(name = "INV_SCD")
	private String invScd;

	/** The adj item name. */
	@Column(name = "ADJ_ITEM_NAME")
	private String adjItemName;

	/** The adj item japanese name. */
	@Column(name = "ADJ_ITEM_JAPANESE_NAME")
	private String adjItemJapaneseName;

	/** The adj atr. */
	@Basic(optional = false)
	@Column(name = "ADJ_ATR")
	private Character adjAtr;

	/** The val number. */
	@Basic(optional = false)
	@Column(name = "VAL_NUMBER")
	private BigDecimal valNumber;

	/** The val char. */
	@Column(name = "VAL_CHAR")
	private String valChar;

	/** The val date. */
	@Column(name = "VAL_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date valDate;

	/** The correct flg. */
	@Basic(optional = false)
	@Column(name = "CORRECT_FLG")
	private short correctFlg;

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qyedtYearendDetailPK != null ? qyedtYearendDetailPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof QyedtYearendDetail)) {
			return false;
		}
		QyedtYearendDetail other = (QyedtYearendDetail) object;
		if ((this.qyedtYearendDetailPK == null && other.qyedtYearendDetailPK != null)
				|| (this.qyedtYearendDetailPK != null
						&& !this.qyedtYearendDetailPK.equals(other.qyedtYearendDetailPK))) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "entity.QyedtYearendDetail[ qyedtYearendDetailPK=" + qyedtYearendDetailPK + " ]";
	}

}
