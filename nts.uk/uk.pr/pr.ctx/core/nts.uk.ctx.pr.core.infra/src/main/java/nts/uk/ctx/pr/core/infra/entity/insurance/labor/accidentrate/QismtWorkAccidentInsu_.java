/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.labor.accidentrate;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import nts.uk.ctx.pr.core.infra.entity.insurance.labor.businesstype.QismtBusinessType;

/**
 * The Class QismtWorkAccidentInsu_.
 */
@StaticMetamodel(QismtWorkAccidentInsu.class)
public class QismtWorkAccidentInsu_ {

	/** The qismt work accident insu PK. */
	public static volatile SingularAttribute<QismtWorkAccidentInsu, QismtWorkAccidentInsuPK> qismtWorkAccidentInsuPK;

	/** The ins date. */
	public static volatile SingularAttribute<QismtWorkAccidentInsu, Date> insDate;

	/** The ins ccd. */
	public static volatile SingularAttribute<QismtWorkAccidentInsu, String> insCcd;

	/** The ins scd. */
	public static volatile SingularAttribute<QismtWorkAccidentInsu, String> insScd;

	/** The ins pg. */
	public static volatile SingularAttribute<QismtWorkAccidentInsu, String> insPg;

	/** The upd date. */
	public static volatile SingularAttribute<QismtWorkAccidentInsu, Date> updDate;

	/** The upd ccd. */
	public static volatile SingularAttribute<QismtWorkAccidentInsu, String> updCcd;

	/** The upd scd. */
	public static volatile SingularAttribute<QismtWorkAccidentInsu, String> updScd;

	/** The upd pg. */
	public static volatile SingularAttribute<QismtWorkAccidentInsu, String> updPg;

	/** The exclus ver. */
	public static volatile SingularAttribute<QismtWorkAccidentInsu, Long> exclusVer;

	/** The str ym. */
	public static volatile SingularAttribute<QismtWorkAccidentInsu, Integer> strYm;

	/** The end ym. */
	public static volatile SingularAttribute<QismtWorkAccidentInsu, Integer> endYm;

	/** The wa insu rate. */
	public static volatile SingularAttribute<QismtWorkAccidentInsu, BigDecimal> waInsuRate;

	/** The wa insu round. */
	public static volatile SingularAttribute<QismtWorkAccidentInsu, Integer> waInsuRound;

	/** The qismt business type. */
	public static volatile SingularAttribute<QismtWorkAccidentInsu, QismtBusinessType> qismtBusinessType;

}