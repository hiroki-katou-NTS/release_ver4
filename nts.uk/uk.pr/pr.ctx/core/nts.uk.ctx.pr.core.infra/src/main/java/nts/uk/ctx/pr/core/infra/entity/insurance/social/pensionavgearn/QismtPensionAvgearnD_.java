/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionrate.QismtPensionRate;

/**
 * The Class QismtPensionAvgearnD_.
 */
@StaticMetamodel(QismtPensionAvgearnD.class)
public class QismtPensionAvgearnD_ {

	/** The qismt pension avgearn PK. */
	public static volatile SingularAttribute<QismtPensionAvgearnD, QismtPensionAvgearnDPK> qismtPensionAvgearnDPK;

	/** The ins date. */
	public static volatile SingularAttribute<QismtPensionAvgearnD, Date> insDate;

	/** The ins ccd. */
	public static volatile SingularAttribute<QismtPensionAvgearnD, String> insCcd;

	/** The ins scd. */
	public static volatile SingularAttribute<QismtPensionAvgearnD, String> insScd;

	/** The ins pg. */
	public static volatile SingularAttribute<QismtPensionAvgearnD, String> insPg;

	/** The upd date. */
	public static volatile SingularAttribute<QismtPensionAvgearnD, Date> updDate;

	/** The upd ccd. */
	public static volatile SingularAttribute<QismtPensionAvgearnD, String> updCcd;

	/** The upd scd. */
	public static volatile SingularAttribute<QismtPensionAvgearnD, String> updScd;

	/** The upd pg. */
	public static volatile SingularAttribute<QismtPensionAvgearnD, String> updPg;

	/** The exclus ver. */
	public static volatile SingularAttribute<QismtPensionAvgearnD, Long> exclusVer;

	/** The pension avg earn. */
	public static volatile SingularAttribute<QismtPensionAvgearnD, BigDecimal> pensionAvgEarn;

	/** The pension upper limit. */
	public static volatile SingularAttribute<QismtPensionAvgearnD, BigDecimal> pensionUpperLimit;

	/** The qismt pension rate. */
	public static volatile SingularAttribute<QismtPensionAvgearnD, QismtPensionRate> qismtPensionRate;
}