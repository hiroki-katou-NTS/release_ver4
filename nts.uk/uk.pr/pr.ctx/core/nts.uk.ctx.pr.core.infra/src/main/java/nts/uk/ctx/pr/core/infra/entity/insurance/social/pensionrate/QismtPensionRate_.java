/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionrate;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn.QismtPensionAmount;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn.QismtPensionAvgearnD;

/**
 * The Class QismtPensionRate_.
 */
@StaticMetamodel(QismtPensionRate.class)
public class QismtPensionRate_ {

	/** The ccd. */
	public static volatile SingularAttribute<QismtPensionRate, String> ccd;

	/** The si office cd. */
	public static volatile SingularAttribute<QismtPensionRate, String> siOfficeCd;

	/** The hist id. */
	public static volatile SingularAttribute<QismtPensionRate, String> histId;

	/** The ins date. */
	public static volatile SingularAttribute<QismtPensionRate, Date> insDate;

	/** The ins ccd. */
	public static volatile SingularAttribute<QismtPensionRate, String> insCcd;

	/** The ins scd. */
	public static volatile SingularAttribute<QismtPensionRate, String> insScd;

	/** The ins pg. */
	public static volatile SingularAttribute<QismtPensionRate, String> insPg;

	/** The upd date. */
	public static volatile SingularAttribute<QismtPensionRate, Date> updDate;

	/** The upd ccd. */
	public static volatile SingularAttribute<QismtPensionRate, String> updCcd;

	/** The upd scd. */
	public static volatile SingularAttribute<QismtPensionRate, String> updScd;

	/** The upd pg. */
	public static volatile SingularAttribute<QismtPensionRate, String> updPg;

	/** The exclus ver. */
	public static volatile SingularAttribute<QismtPensionRate, Long> exclusVer;

	/** The str ym. */
	public static volatile SingularAttribute<QismtPensionRate, Integer> strYm;

	/** The end ym. */
	public static volatile SingularAttribute<QismtPensionRate, Integer> endYm;

	/** The pension fund join atr. */
	public static volatile SingularAttribute<QismtPensionRate, Integer> pensionFundJoinAtr;

	/** The p pay pension male rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> pPayPensionMaleRate;

	/** The p pay pension fem rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> pPayPensionFemRate;

	/** The p pay pension miner rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> pPayPensionMinerRate;

	/** The p pay pension round atr. */
	public static volatile SingularAttribute<QismtPensionRate, Integer> pPayPensionRoundAtr;

	/** The c pay pension male rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> cPayPensionMaleRate;

	/** The c pay pension fem rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> cPayPensionFemRate;

	/** The c pay pension miner rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> cPayPensionMinerRate;

	/** The c pay pension round atr. */
	public static volatile SingularAttribute<QismtPensionRate, Integer> cPayPensionRoundAtr;

	/** The p bns pension male rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> pBnsPensionMaleRate;

	/** The p bns pension fem rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> pBnsPensionFemRate;

	/** The p bns pension miner rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> pBnsPensionMinerRate;

	/** The p bns pension round atr. */
	public static volatile SingularAttribute<QismtPensionRate, Integer> pBnsPensionRoundAtr;

	/** The c bns pension male rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> cBnsPensionMaleRate;

	/** The c bns pension fem rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> cBnsPensionFemRate;

	/** The c bns pension miner rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> cBnsPensionMinerRate;

	/** The c bns pension round atr. */
	public static volatile SingularAttribute<QismtPensionRate, Integer> cBnsPensionRoundAtr;

	/** The p pay fund male rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> pPayFundMaleRate;

	/** The p pay fund ex male rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> pPayFundExMaleRate;

	/** The p pay fund fem rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> pPayFundFemRate;

	/** The p pay fund ex fem rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> pPayFundExFemRate;

	/** The p pay fund miner rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> pPayFundMinerRate;

	/** The p pay fund ex miner rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> pPayFundExMinerRate;

	/** The c pay fund male rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> cPayFundMaleRate;

	/** The c pay fund ex male rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> cPayFundExMaleRate;

	/** The c pay fund fem rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> cPayFundFemRate;

	/** The c pay fund ex fem rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> cPayFundExFemRate;

	/** The c pay fund miner rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> cPayFundMinerRate;

	/** The c pay fund ex miner rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> cPayFundExMinerRate;

	/** The p bns fund man rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> pBnsFundManRate;

	/** The p bns fund ex male rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> pBnsFundExMaleRate;

	/** The p bns fund fem rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> pBnsFundFemRate;

	/** The p bns fund ex fem rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> pBnsFundExFemRate;

	/** The p bns fund miner rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> pBnsFundMinerRate;

	/** The p bns fund ex miner rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> pBnsFundExMinerRate;

	/** The c bns fund man rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> cBnsFundManRate;

	/** The c bns fund ex male rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> cBnsFundExMaleRate;

	/** The c bns fund fem rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> cBnsFundFemRate;

	/** The c bns fund ex fem rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> cBnsFundExFemRate;

	/** The c bns fund miner rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> cBnsFundMinerRate;

	/** The c bns fund ex miner rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> cBnsFundExMinerRate;

	/** The bonus pension max mny. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> bonusPensionMaxMny;

	/** The child contribution rate. */
	public static volatile SingularAttribute<QismtPensionRate, BigDecimal> childContributionRate;

	/** The keep entry flg. */
	public static volatile SingularAttribute<QismtPensionRate, Integer> keepEntryFlg;

	/** The qismt pension avgearn D list. */
	public static volatile ListAttribute<QismtPensionRate, QismtPensionAvgearnD> qismtPensionAvgearnDList;

	/** The qismt pension amount list. */
	public static volatile ListAttribute<QismtPensionRate, QismtPensionAmount> qismtPensionAmountList;
}