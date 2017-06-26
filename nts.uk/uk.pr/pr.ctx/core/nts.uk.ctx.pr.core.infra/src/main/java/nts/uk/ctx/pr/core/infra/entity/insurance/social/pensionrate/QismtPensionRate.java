/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionrate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn.QismtPensionAmount;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn.QismtPensionAvgearnD;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QismtPensionRate.
 */
@Setter
@Getter
@Entity
@Table(name = "QISMT_PENSION_RATE")
public class QismtPensionRate extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Column(name = "CCD")
	private String ccd;

	/** The si office cd. */
	@Column(name = "SI_OFFICE_CD")
	private String siOfficeCd;

	/** The hist id. */
	@Id
	@Column(name = "HIST_ID")
	private String histId;

	/** The str ym. */
	@Column(name = "STR_YM")
	private int strYm;

	/** The end ym. */
	@Column(name = "END_YM")
	private int endYm;

	/** The pension fund join atr. */
	@Column(name = "PENSION_FUND_JOIN_ATR")
	private int pensionFundJoinAtr;

	/** The p pay pension male rate. */
	@Column(name = "P_PAY_PENSION_MALE_RATE")
	private BigDecimal pPayPensionMaleRate;

	/** The p pay pension fem rate. */
	@Column(name = "P_PAY_PENSION_FEM_RATE")
	private BigDecimal pPayPensionFemRate;

	/** The p pay pension miner rate. */
	@Column(name = "P_PAY_PENSION_MINER_RATE")
	private BigDecimal pPayPensionMinerRate;

	/** The p pay pension round atr. */
	@Column(name = "P_PAY_PENSION_ROUND_ATR")
	private int pPayPensionRoundAtr;

	/** The c pay pension male rate. */
	@Column(name = "C_PAY_PENSION_MALE_RATE")
	private BigDecimal cPayPensionMaleRate;

	/** The c pay pension fem rate. */
	@Column(name = "C_PAY_PENSION_FEM_RATE")
	private BigDecimal cPayPensionFemRate;

	/** The c pay pension miner rate. */
	@Column(name = "C_PAY_PENSION_MINER_RATE")
	private BigDecimal cPayPensionMinerRate;

	/** The c pay pension round atr. */
	@Column(name = "C_PAY_PENSION_ROUND_ATR")
	private int cPayPensionRoundAtr;

	/** The p bns pension male rate. */
	@Column(name = "P_BNS_PENSION_MALE_RATE")
	private BigDecimal pBnsPensionMaleRate;

	/** The p bns pension fem rate. */
	@Column(name = "P_BNS_PENSION_FEM_RATE")
	private BigDecimal pBnsPensionFemRate;

	/** The p bns pension miner rate. */
	@Column(name = "P_BNS_PENSION_MINER_RATE")
	private BigDecimal pBnsPensionMinerRate;

	/** The p bns pension round atr. */
	@Column(name = "P_BNS_PENSION_ROUND_ATR")
	private int pBnsPensionRoundAtr;

	/** The c bns pension male rate. */
	@Column(name = "C_BNS_PENSION_MALE_RATE")
	private BigDecimal cBnsPensionMaleRate;

	/** The c bns pension fem rate. */
	@Column(name = "C_BNS_PENSION_FEM_RATE")
	private BigDecimal cBnsPensionFemRate;

	/** The c bns pension miner rate. */
	@Column(name = "C_BNS_PENSION_MINER_RATE")
	private BigDecimal cBnsPensionMinerRate;

	/** The c bns pension round atr. */
	@Column(name = "C_BNS_PENSION_ROUND_ATR")
	private int cBnsPensionRoundAtr;

	/** The p pay fund male rate. */
	@Column(name = "P_PAY_FUND_MALE_RATE")
	private BigDecimal pPayFundMaleRate;

	/** The p pay fund ex male rate. */
	@Column(name = "P_PAY_FUND_EX_MALE_RATE")
	private BigDecimal pPayFundExMaleRate;

	/** The p pay fund fem rate. */
	@Column(name = "P_PAY_FUND_FEM_RATE")
	private BigDecimal pPayFundFemRate;

	/** The p pay fund ex fem rate. */
	@Column(name = "P_PAY_FUND_EX_FEM_RATE")
	private BigDecimal pPayFundExFemRate;

	/** The p pay fund miner rate. */
	@Column(name = "P_PAY_FUND_MINER_RATE")
	private BigDecimal pPayFundMinerRate;

	/** The p pay fund ex miner rate. */
	@Column(name = "P_PAY_FUND_EX_MINER_RATE")
	private BigDecimal pPayFundExMinerRate;

	/** The c pay fund male rate. */
	@Column(name = "C_PAY_FUND_MALE_RATE")
	private BigDecimal cPayFundMaleRate;

	/** The c pay fund ex male rate. */
	@Column(name = "C_PAY_FUND_EX_MALE_RATE")
	private BigDecimal cPayFundExMaleRate;

	/** The c pay fund fem rate. */
	@Column(name = "C_PAY_FUND_FEM_RATE")
	private BigDecimal cPayFundFemRate;

	/** The c pay fund ex fem rate. */
	@Column(name = "C_PAY_FUND_EX_FEM_RATE")
	private BigDecimal cPayFundExFemRate;

	/** The c pay fund miner rate. */
	@Column(name = "C_PAY_FUND_MINER_RATE")
	private BigDecimal cPayFundMinerRate;

	/** The c pay fund ex miner rate. */
	@Column(name = "C_PAY_FUND_EX_MINER_RATE")
	private BigDecimal cPayFundExMinerRate;

	/** The p bns fund man rate. */
	@Column(name = "P_BNS_FUND_MAN_RATE")
	private BigDecimal pBnsFundManRate;

	/** The p bns fund ex male rate. */
	@Column(name = "P_BNS_FUND_EX_MALE_RATE")
	private BigDecimal pBnsFundExMaleRate;

	/** The p bns fund fem rate. */
	@Column(name = "P_BNS_FUND_FEM_RATE")
	private BigDecimal pBnsFundFemRate;

	/** The p bns fund ex fem rate. */
	@Column(name = "P_BNS_FUND_EX_FEM_RATE")
	private BigDecimal pBnsFundExFemRate;

	/** The p bns fund miner rate. */
	@Column(name = "P_BNS_FUND_MINER_RATE")
	private BigDecimal pBnsFundMinerRate;

	/** The p bns fund ex miner rate. */
	@Column(name = "P_BNS_FUND_EX_MINER_RATE")
	private BigDecimal pBnsFundExMinerRate;

	/** The c bns fund man rate. */
	@Column(name = "C_BNS_FUND_MAN_RATE")
	private BigDecimal cBnsFundManRate;

	/** The c bns fund ex male rate. */
	@Column(name = "C_BNS_FUND_EX_MALE_RATE")
	private BigDecimal cBnsFundExMaleRate;

	/** The c bns fund fem rate. */
	@Column(name = "C_BNS_FUND_FEM_RATE")
	private BigDecimal cBnsFundFemRate;

	/** The c bns fund ex fem rate. */
	@Column(name = "C_BNS_FUND_EX_FEM_RATE")
	private BigDecimal cBnsFundExFemRate;

	/** The c bns fund miner rate. */
	@Column(name = "C_BNS_FUND_MINER_RATE")
	private BigDecimal cBnsFundMinerRate;

	/** The c bns fund ex miner rate. */
	@Column(name = "C_BNS_FUND_EX_MINER_RATE")
	private BigDecimal cBnsFundExMinerRate;

	/** The bonus pension max mny. */
	@Column(name = "BONUS_PENSION_MAX_MNY")
	private BigDecimal bonusPensionMaxMny;

	/** The child contribution rate. */
	@Column(name = "CHILD_CONTRIBUTION_RATE")
	private BigDecimal childContributionRate;

	/** The keep entry flg. */
	@Column(name = "KEEP_ENTRY_FLG")
	private int keepEntryFlg;

	/** The qismt pension avgearn D list. */
	@JoinColumns({
			@JoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID", insertable = false, updatable = false) })
	@OneToMany(cascade = CascadeType.ALL)
	private List<QismtPensionAvgearnD> qismtPensionAvgearnDList;

	/** The qismt pension amount list. */
	@JoinColumns({
			@JoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID", insertable = false, updatable = false) })
	@OneToMany(cascade = CascadeType.ALL)
	private List<QismtPensionAmount> qismtPensionAmountList;

	/**
	 * Instantiates a new qismt pension rate.
	 */
	public QismtPensionRate() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.histId;
	}

}
