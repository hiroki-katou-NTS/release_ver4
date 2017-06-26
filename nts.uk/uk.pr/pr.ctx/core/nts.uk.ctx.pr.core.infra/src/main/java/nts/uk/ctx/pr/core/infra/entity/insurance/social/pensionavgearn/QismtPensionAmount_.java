/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionrate.QismtPensionRate;

/**
 * The Class QismtPensionAmount_.
 */
@StaticMetamodel(QismtPensionAmount.class)
public class QismtPensionAmount_ {

	/** The ccd. */
	public static volatile SingularAttribute<QismtPensionAmount, String> ccd;

	/** The si office cd. */
	public static volatile SingularAttribute<QismtPensionAmount, String> siOfficeCd;

	/** The qismt pension avgearn PK. */
	public static volatile SingularAttribute<QismtPensionAmount, QismtPensionAmountPK> qismtPensionAmountPK;

	/** The ins date. */
	public static volatile SingularAttribute<QismtPensionAmount, Date> insDate;

	/** The ins ccd. */
	public static volatile SingularAttribute<QismtPensionAmount, String> insCcd;

	/** The ins scd. */
	public static volatile SingularAttribute<QismtPensionAmount, String> insScd;

	/** The ins pg. */
	public static volatile SingularAttribute<QismtPensionAmount, String> insPg;

	/** The upd date. */
	public static volatile SingularAttribute<QismtPensionAmount, Date> updDate;

	/** The upd ccd. */
	public static volatile SingularAttribute<QismtPensionAmount, String> updCcd;

	/** The upd scd. */
	public static volatile SingularAttribute<QismtPensionAmount, String> updScd;

	/** The upd pg. */
	public static volatile SingularAttribute<QismtPensionAmount, String> updPg;

	/** The exclus ver. */
	public static volatile SingularAttribute<QismtPensionAmount, Long> exclusVer;

	/** The p pension male mny. */
	public static volatile SingularAttribute<QismtPensionAmount, BigDecimal> pPensionMaleMny;

	/** The p pension fem mny. */
	public static volatile SingularAttribute<QismtPensionAmount, BigDecimal> pPensionFemMny;

	/** The p pension miner mny. */
	public static volatile SingularAttribute<QismtPensionAmount, BigDecimal> pPensionMinerMny;

	/** The c pension male mny. */
	public static volatile SingularAttribute<QismtPensionAmount, Long> cPensionMaleMny;

	/** The c pension fem mny. */
	public static volatile SingularAttribute<QismtPensionAmount, BigDecimal> cPensionFemMny;

	/** The c pension miner mny. */
	public static volatile SingularAttribute<QismtPensionAmount, BigDecimal> cPensionMinerMny;

	/** The p fund male mny. */
	public static volatile SingularAttribute<QismtPensionAmount, BigDecimal> pFundMaleMny;

	/** The p fund fem mny. */
	public static volatile SingularAttribute<QismtPensionAmount, BigDecimal> pFundFemMny;

	/** The p fund miner mny. */
	public static volatile SingularAttribute<QismtPensionAmount, BigDecimal> pFundMinerMny;

	/** The c fund male mny. */
	public static volatile SingularAttribute<QismtPensionAmount, BigDecimal> cFundMaleMny;

	/** The c fund fem mny. */
	public static volatile SingularAttribute<QismtPensionAmount, BigDecimal> cFundFemMny;

	/** The c fund miner mny. */
	public static volatile SingularAttribute<QismtPensionAmount, BigDecimal> cFundMinerMny;

	/** The p fund exempt male mny. */
	public static volatile SingularAttribute<QismtPensionAmount, BigDecimal> pFundExemptMaleMny;

	/** The p fund exempt fem mny. */
	public static volatile SingularAttribute<QismtPensionAmount, BigDecimal> pFundExemptFemMny;

	/** The p fund exempt miner mny. */
	public static volatile SingularAttribute<QismtPensionAmount, BigDecimal> pFundExemptMinerMny;

	/** The c fund exempt male mny. */
	public static volatile SingularAttribute<QismtPensionAmount, BigDecimal> cFundExemptMaleMny;

	/** The c fund exempt fem mny. */
	public static volatile SingularAttribute<QismtPensionAmount, BigDecimal> cFundExemptFemMny;

	/** The c fund exempt miner mny. */
	public static volatile SingularAttribute<QismtPensionAmount, BigDecimal> cFundExemptMinerMny;

	/** The child contribution mny. */
	public static volatile SingularAttribute<QismtPensionAmount, BigDecimal> childContributionMny;

	/** The qismt pension rate. */
	public static volatile SingularAttribute<QismtPensionAmount, QismtPensionRate> qismtPensionRate;
}