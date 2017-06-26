/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice;

import java.util.Date;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The Class QupmtCUnitpriceHead_.
 */
@StaticMetamodel(QupmtCUnitpriceHeader.class)
public class QupmtCUnitpriceHeader_ {

	/** The qupmt C unitprice head PK. */
	public static volatile SingularAttribute<QupmtCUnitpriceHeader, QupmtCUnitpriceHeaderPK> qupmtCUnitpriceHeaderPK;

	/** The ins date. */
	public static volatile SingularAttribute<QupmtCUnitpriceHeader, Date> insDate;

	/** The ins ccd. */
	public static volatile SingularAttribute<QupmtCUnitpriceHeader, String> insCcd;

	/** The ins scd. */
	public static volatile SingularAttribute<QupmtCUnitpriceHeader, String> insScd;

	/** The ins pg. */
	public static volatile SingularAttribute<QupmtCUnitpriceHeader, String> insPg;

	/** The upd date. */
	public static volatile SingularAttribute<QupmtCUnitpriceHeader, Date> updDate;

	/** The upd ccd. */
	public static volatile SingularAttribute<QupmtCUnitpriceHeader, String> updCcd;

	/** The upd scd. */
	public static volatile SingularAttribute<QupmtCUnitpriceHeader, String> updScd;

	/** The upd pg. */
	public static volatile SingularAttribute<QupmtCUnitpriceHeader, String> updPg;

	/** The exclus ver. */
	public static volatile SingularAttribute<QupmtCUnitpriceHeader, Long> exclusVer;

	/** The c unitprice name. */
	public static volatile SingularAttribute<QupmtCUnitpriceHeader, String> cUnitpriceName;

	/** The qupmt C unitprice hist list. */
	public static volatile ListAttribute<QupmtCUnitpriceHeader, QupmtCUnitpriceDetail> qupmtCUnitpriceHistList;

}