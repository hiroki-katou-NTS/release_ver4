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
 * The Class QismtHealthInsuAmount_.
 */
@StaticMetamodel(QismtHealthInsuAmount.class)
public class QismtHealthInsuAmount_ {

	/** The ccd. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, String> ccd;

	/** The si office cd. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, String> siOfficeCd;

	/** The qismt health insu avgearn PK. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, QismtHealthInsuAmountPK> qismtHealthInsuAmountPK;

	/** The ins date. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, Date> insDate;

	/** The ins ccd. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, String> insCcd;

	/** The ins scd. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, String> insScd;

	/** The ins pg. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, String> insPg;

	/** The upd date. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, Date> updDate;

	/** The upd ccd. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, String> updCcd;

	/** The upd scd. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, String> updScd;

	/** The upd pg. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, String> updPg;

	/** The exclus ver. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, Long> exclusVer;

	/** The p health general mny. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, BigDecimal> pHealthGeneralMny;

	/** The p health nursing mny. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, BigDecimal> pHealthNursingMny;

	/** The p health specific mny. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, BigDecimal> pHealthSpecificMny;

	/** The p health basic mny. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, BigDecimal> pHealthBasicMny;

	/** The c health general mny. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, BigDecimal> cHealthGeneralMny;

	/** The c health nursing mny. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, BigDecimal> cHealthNursingMny;

	/** The c health specific mny. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, BigDecimal> cHealthSpecificMny;

	/** The c health basic mny. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, BigDecimal> cHealthBasicMny;

	/** The qismt health insu rate. */
	public static volatile SingularAttribute<QismtHealthInsuAmount, QismtHealthInsuRate> qismtHealthInsuRate;
}