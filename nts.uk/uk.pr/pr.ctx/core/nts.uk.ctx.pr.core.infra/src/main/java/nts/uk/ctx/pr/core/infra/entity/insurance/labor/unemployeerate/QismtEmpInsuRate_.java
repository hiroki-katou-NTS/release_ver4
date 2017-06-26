/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.labor.unemployeerate;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The Class QismtBusinessType_.
 */
@StaticMetamodel(QismtEmpInsuRate.class)
public class QismtEmpInsuRate_ {

	/** The qismt emp insu rate PK. */
	public static volatile SingularAttribute<QismtEmpInsuRate, QismtEmpInsuRatePK> qismtEmpInsuRatePK;

	/** The ins date. */
	public static volatile SingularAttribute<QismtEmpInsuRate, Date> insDate;

	/** The ins ccd. */
	public static volatile SingularAttribute<QismtEmpInsuRate, String> insCcd;

	/** The ins scd. */
	public static volatile SingularAttribute<QismtEmpInsuRate, String> insScd;

	/** The ins pg. */
	public static volatile SingularAttribute<QismtEmpInsuRate, String> insPg;

	/** The upd date. */
	public static volatile SingularAttribute<QismtEmpInsuRate, Date> updDate;

	/** The upd ccd. */
	public static volatile SingularAttribute<QismtEmpInsuRate, String> updCcd;

	/** The upd scd. */
	public static volatile SingularAttribute<QismtEmpInsuRate, String> updScd;

	/** The upd pg. */
	public static volatile SingularAttribute<QismtEmpInsuRate, String> updPg;

	/** The exclus ver. */
	public static volatile SingularAttribute<QismtEmpInsuRate, Long> exclusVer;

	/** The str ym. */
	public static volatile SingularAttribute<QismtEmpInsuRate, Integer> strYm;

	/** The end ym. */
	public static volatile SingularAttribute<QismtEmpInsuRate, Integer> endYm;

	public static volatile SingularAttribute<QismtEmpInsuRate, BigDecimal> pEmpRateGeneral;

	/** The p emp round general. */
	public static volatile SingularAttribute<QismtEmpInsuRate, Integer> pEmpRoundGeneral;

	/** The c emp rate general. */
	public static volatile SingularAttribute<QismtEmpInsuRate, BigDecimal> cEmpRateGeneral;

	/** The c emp round general. */
	public static volatile SingularAttribute<QismtEmpInsuRate, Integer> cEmpRoundGeneral;

	/** The p emp rate other. */
	public static volatile SingularAttribute<QismtEmpInsuRate, BigDecimal> pEmpRateOther;

	/** The p emp round other. */
	public static volatile SingularAttribute<QismtEmpInsuRate, Integer> pEmpRoundOther;

	/** The c emp rate other. */
	public static volatile SingularAttribute<QismtEmpInsuRate, BigDecimal> cEmpRateOther;

	/** The c emp round other. */
	public static volatile SingularAttribute<QismtEmpInsuRate, Integer> cEmpRoundOther;

	/** The p emp rate const. */
	public static volatile SingularAttribute<QismtEmpInsuRate, BigDecimal> pEmpRateConst;

	/** The p emp round const. */
	public static volatile SingularAttribute<QismtEmpInsuRate, Integer> pEmpRoundConst;

	/** The c emp rate const. */
	public static volatile SingularAttribute<QismtEmpInsuRate, BigDecimal> cEmpRateConst;

	/** The c emp round const. */
	public static volatile SingularAttribute<QismtEmpInsuRate, Integer> cEmpRoundConst;

}