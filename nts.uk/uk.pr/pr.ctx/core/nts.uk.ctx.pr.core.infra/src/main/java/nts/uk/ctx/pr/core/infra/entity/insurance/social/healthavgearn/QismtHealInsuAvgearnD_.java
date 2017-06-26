/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthrate.QismtHealthInsuRate;

/**
 * The Class QismtHealInsuAvgearnD_.
 */
@StaticMetamodel(QismtHealInsuAvgearnD.class)
public class QismtHealInsuAvgearnD_ {
	/** The ccd. */
	public static volatile SingularAttribute<QismtHealInsuAvgearnDPK, String> ccd;

	/** The qismt health insu avgearn PK. */
	public static volatile SingularAttribute<QismtHealInsuAvgearnD, QismtHealInsuAvgearnDPK> qismtHealInsuAvgearnDPK;

	/** The ins date. */
	public static volatile SingularAttribute<QismtHealInsuAvgearnD, Date> insDate;

	/** The ins ccd. */
	public static volatile SingularAttribute<QismtHealInsuAvgearnD, String> insCcd;

	/** The ins scd. */
	public static volatile SingularAttribute<QismtHealInsuAvgearnD, String> insScd;

	/** The ins pg. */
	public static volatile SingularAttribute<QismtHealInsuAvgearnD, String> insPg;

	/** The upd date. */
	public static volatile SingularAttribute<QismtHealInsuAvgearnD, Date> updDate;

	/** The upd ccd. */
	public static volatile SingularAttribute<QismtHealInsuAvgearnD, String> updCcd;

	/** The upd scd. */
	public static volatile SingularAttribute<QismtHealInsuAvgearnD, String> updScd;

	/** The upd pg. */
	public static volatile SingularAttribute<QismtHealInsuAvgearnD, String> updPg;

	/** The exclus ver. */
	public static volatile SingularAttribute<QismtHealInsuAvgearnD, Long> exclusVer;

	/** The health insu avg earn. */
	public static volatile SingularAttribute<QismtHealInsuAvgearnD, BigDecimal> healthInsuAvgEarn;

	/** The health insu upper limit. */
	public static volatile SingularAttribute<QismtHealInsuAvgearnD, BigDecimal> healthInsuUpperLimit;

	/** The qismt health insu rate. */
	public static volatile SingularAttribute<QismtHealInsuAvgearnD, QismtHealthInsuRate> qismtHealthInsuRate;

}