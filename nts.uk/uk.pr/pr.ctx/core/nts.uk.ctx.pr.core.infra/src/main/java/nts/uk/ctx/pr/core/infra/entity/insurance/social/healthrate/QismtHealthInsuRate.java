/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.social.healthrate;

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
import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn.QismtHealInsuAvgearnD;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn.QismtHealthInsuAmount;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QismtHealthInsuRate.
 */
@Setter
@Getter
@Entity
@Table(name = "QISMT_HEALTH_INSU_RATE")
public class QismtHealthInsuRate extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Column(name = "CCD")
	private String ccd;

	/** The si office cd. */
	@Column(name = "SI_OFFICE_CD")
	private String siOfficeCd;

	/** The qismt health insu rate PK. */
	@Id
	@Column(name = "HIST_ID")
	private String histId;

	/** The str ym. */
	@Column(name = "STR_YM")
	private int strYm;

	/** The end ym. */
	@Column(name = "END_YM")
	private int endYm;

	/** The p pay general rate. */
	@Column(name = "P_PAY_GENERAL_RATE")
	private BigDecimal pPayGeneralRate;

	/** The p pay nursing rate. */
	@Column(name = "P_PAY_NURSING_RATE")
	private BigDecimal pPayNursingRate;

	/** The p pay specific rate. */
	@Column(name = "P_PAY_SPECIFIC_RATE")
	private BigDecimal pPaySpecificRate;

	/** The p pay basic rate. */
	@Column(name = "P_PAY_BASIC_RATE")
	private BigDecimal pPayBasicRate;

	/** The p pay health round atr. */
	@Column(name = "P_PAY_HEALTH_ROUND_ATR")
	private int pPayHealthRoundAtr;

	/** The c pay general rate. */
	@Column(name = "C_PAY_GENERAL_RATE")
	private BigDecimal cPayGeneralRate;

	/** The c pay nursing rate. */
	@Column(name = "C_PAY_NURSING_RATE")
	private BigDecimal cPayNursingRate;

	/** The c pay specific rate. */
	@Column(name = "C_PAY_SPECIFIC_RATE")
	private BigDecimal cPaySpecificRate;

	/** The c pay basic rate. */
	@Column(name = "C_PAY_BASIC_RATE")
	private BigDecimal cPayBasicRate;

	/** The c pay health round atr. */
	@Column(name = "C_PAY_HEALTH_ROUND_ATR")
	private int cPayHealthRoundAtr;

	/** The p bns general rate. */
	@Column(name = "P_BNS_GENERAL_RATE")
	private BigDecimal pBnsGeneralRate;

	/** The p bns nursing rate. */
	@Column(name = "P_BNS_NURSING_RATE")
	private BigDecimal pBnsNursingRate;

	/** The p bns specific rate. */
	@Column(name = "P_BNS_SPECIFIC_RATE")
	private BigDecimal pBnsSpecificRate;

	/** The p bns basic rate. */
	@Column(name = "P_BNS_BASIC_RATE")
	private BigDecimal pBnsBasicRate;

	/** The p bns health round atr. */
	@Column(name = "P_BNS_HEALTH_ROUND_ATR")
	private int pBnsHealthRoundAtr;

	/** The c bns general rate. */
	@Column(name = "C_BNS_GENERAL_RATE")
	private BigDecimal cBnsGeneralRate;

	/** The c bns nursing rate. */
	@Column(name = "C_BNS_NURSING_RATE")
	private BigDecimal cBnsNursingRate;

	/** The c bns specific rate. */
	@Column(name = "C_BNS_SPECIFIC_RATE")
	private BigDecimal cBnsSpecificRate;

	/** The c bns basic rate. */
	@Column(name = "C_BNS_BASIC_RATE")
	private BigDecimal cBnsBasicRate;

	/** The c bns health round atr. */
	@Column(name = "C_BNS_HEALTH_ROUND_ATR")
	private int cBnsHealthRoundAtr;

	/** The bonus health max mny. */
	@Column(name = "BONUS_HEALTH_MAX_MNY")
	private BigDecimal bonusHealthMaxMny;

	/** The keep entry flg. */
	@Column(name = "KEEP_ENTRY_FLG")
	private int keepEntryFlg;

	/** The qismt health insu avgearn list. */
	@JoinColumns({
			@JoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID", insertable = false, updatable = false) })
	@OneToMany(cascade = CascadeType.ALL)
	private List<QismtHealInsuAvgearnD> qismtHealInsuAvgearnDList;

	/** The qismt health insu avgearn list. */
	@JoinColumns({
			@JoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID", insertable = false, updatable = false) })
	@OneToMany(cascade = CascadeType.ALL)
	private List<QismtHealthInsuAmount> qismtHealthInsuAmountList;

	/**
	 * Instantiates a new qismt health insu rate.
	 */
	public QismtHealthInsuRate() {
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
