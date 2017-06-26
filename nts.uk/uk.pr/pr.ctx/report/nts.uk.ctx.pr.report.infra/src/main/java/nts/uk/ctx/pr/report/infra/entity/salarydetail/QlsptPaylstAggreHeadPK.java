/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.entity.salarydetail;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class QlsptPaylstAggreHeadPK.
 */
@Getter
@Setter
@Embeddable
public class QlsptPaylstAggreHeadPK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Basic(optional = false)
	@Column(name = "CCD")
	private String ccd;

	/** The aggregate cd. */
	@Basic(optional = false)
	@Column(name = "AGGREGATE_CD")
	private String aggregateCd;

	/** The ctg atr. */
	@Basic(optional = false)
	@Column(name = "CTG_ATR")
	private int ctgAtr;

	/**
	 * Instantiates a new qlspt paylst aggre head PK.
	 */
	public QlsptPaylstAggreHeadPK() {
		super();
	}

	/**
	 * Instantiates a new qlspt paylst aggre head PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param aggregateCd
	 *            the aggregate cd
	 * @param ctgAtr
	 *            the ctg atr
	 */
	public QlsptPaylstAggreHeadPK(String ccd, String aggregateCd, int ctgAtr) {
		this.ccd = ccd;
		this.aggregateCd = aggregateCd;
		this.ctgAtr = ctgAtr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ccd != null ? ccd.hashCode() : 0);
		hash += (aggregateCd != null ? aggregateCd.hashCode() : 0);
		hash += (int) ctgAtr;
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QlsptPaylstAggreHeadPK)) {
			return false;
		}
		QlsptPaylstAggreHeadPK other = (QlsptPaylstAggreHeadPK) object;
		if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
			return false;
		}
		if ((this.aggregateCd == null && other.aggregateCd != null)
				|| (this.aggregateCd != null && !this.aggregateCd.equals(other.aggregateCd))) {
			return false;
		}
		if (this.ctgAtr != other.ctgAtr) {
			return false;
		}
		return true;
	}

}
