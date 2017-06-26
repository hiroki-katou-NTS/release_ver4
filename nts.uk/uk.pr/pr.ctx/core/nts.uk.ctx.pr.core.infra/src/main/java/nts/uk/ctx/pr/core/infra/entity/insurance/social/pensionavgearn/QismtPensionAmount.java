/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QismtPensionAmount.
 */
@Getter
@Setter
@Entity
@Table(name = "QISMT_PENSION_AMOUNT")
public class QismtPensionAmount extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Column(name = "CCD")
	private String ccd;

	/** The si office cd. */
	@Column(name = "SI_OFFICE_CD")
	private String siOfficeCd;

	/** The qismt pension amount PK. */
	@EmbeddedId
	protected QismtPensionAmountPK qismtPensionAmountPK;

	/** The p pension male mny. */
	@Column(name = "P_PENSION_MALE_MNY")
	private BigDecimal pPensionMaleMny;

	/** The p pension fem mny. */
	@Column(name = "P_PENSION_FEM_MNY")
	private BigDecimal pPensionFemMny;

	/** The p pension miner mny. */
	@Column(name = "P_PENSION_MINER_MNY")
	private BigDecimal pPensionMinerMny;

	/** The c pension male mny. */
	@Column(name = "C_PENSION_MALE_MNY")
	private BigDecimal cPensionMaleMny;

	/** The c pension fem mny. */
	@Column(name = "C_PENSION_FEM_MNY")
	private BigDecimal cPensionFemMny;

	/** The c pension miner mny. */
	@Column(name = "C_PENSION_MINER_MNY")
	private BigDecimal cPensionMinerMny;

	/** The p fund male mny. */
	@Column(name = "P_FUND_MALE_MNY")
	private BigDecimal pFundMaleMny;

	/** The p fund fem mny. */
	@Column(name = "P_FUND_FEM_MNY")
	private BigDecimal pFundFemMny;

	/** The p fund miner mny. */
	@Column(name = "P_FUND_MINER_MNY")
	private BigDecimal pFundMinerMny;

	/** The c fund male mny. */
	@Column(name = "C_FUND_MALE_MNY")
	private BigDecimal cFundMaleMny;

	/** The c fund fem mny. */
	@Column(name = "C_FUND_FEM_MNY")
	private BigDecimal cFundFemMny;

	/** The c fund miner mny. */
	@Column(name = "C_FUND_MINER_MNY")
	private BigDecimal cFundMinerMny;

	/** The p fund exempt male mny. */
	@Column(name = "P_FUND_EXEMPT_MALE_MNY")
	private BigDecimal pFundExemptMaleMny;

	/** The p fund exempt fem mny. */
	@Column(name = "P_FUND_EXEMPT_FEM_MNY")
	private BigDecimal pFundExemptFemMny;

	/** The p fund exempt miner mny. */
	@Column(name = "P_FUND_EXEMPT_MINER_MNY")
	private BigDecimal pFundExemptMinerMny;

	/** The c fund exempt male mny. */
	@Column(name = "C_FUND_EXEMPT_MALE_MNY")
	private BigDecimal cFundExemptMaleMny;

	/** The c fund exempt fem mny. */
	@Column(name = "C_FUND_EXEMPT_FEM_MNY")
	private BigDecimal cFundExemptFemMny;

	/** The c fund exempt miner mny. */
	@Column(name = "C_FUND_EXEMPT_MINER_MNY")
	private BigDecimal cFundExemptMinerMny;

	/** The child contribution mny. */
	@Column(name = "CHILD_CONTRIBUTION_MNY")
	private BigDecimal childContributionMny;

	/** The qismt pension avgearn D. */
	@JoinColumns({
			@JoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID", insertable = false, updatable = false),
			@JoinColumn(name = "PENSION_GRADE", referencedColumnName = "PENSION_GRADE", insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private QismtPensionAvgearnD qismtPensionAvgearnD;

	/**
	 * Instantiates a new qismt pension amount.
	 */
	public QismtPensionAmount() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qismtPensionAmountPK != null ? qismtPensionAmountPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QismtPensionAmount)) {
			return false;
		}
		QismtPensionAmount other = (QismtPensionAmount) object;
		if ((this.qismtPensionAmountPK == null && other.qismtPensionAmountPK != null)
				|| (this.qismtPensionAmountPK != null
						&& !this.qismtPensionAmountPK.equals(other.qismtPensionAmountPK))) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.qismtPensionAmountPK;
	}

}
