/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.entity.wageledger;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class QlsptLedgerFormHeadPK.
 */
@Getter
@Setter
@Embeddable
public class QlsptLedgerFormHeadPK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Basic(optional = false)
	@Column(name = "CCD")
	private String ccd;

	/** The form cd. */
	@Basic(optional = false)
	@Column(name = "FORM_CD")
	private String formCd;

	/**
	 * Instantiates a new qlspt ledger form head PK.
	 */
	public QlsptLedgerFormHeadPK() {
		super();
	}

	/**
	 * Instantiates a new qlspt ledger form head PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param formCd
	 *            the form cd
	 */
	public QlsptLedgerFormHeadPK(String ccd, String formCd) {
		this.ccd = ccd;
		this.formCd = formCd;
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
		hash += (formCd != null ? formCd.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QlsptLedgerFormHeadPK)) {
			return false;
		}
		QlsptLedgerFormHeadPK other = (QlsptLedgerFormHeadPK) object;
		if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
			return false;
		}
		if ((this.formCd == null && other.formCd != null)
				|| (this.formCd != null && !this.formCd.equals(other.formCd))) {
			return false;
		}
		return true;
	}

}
