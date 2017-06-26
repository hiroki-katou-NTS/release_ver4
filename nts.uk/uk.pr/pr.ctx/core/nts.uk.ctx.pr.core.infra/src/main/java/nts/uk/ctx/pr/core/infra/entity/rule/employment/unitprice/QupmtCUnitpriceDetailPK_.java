/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The Class QupmtCUnitpriceHeadPK_.
 */
@StaticMetamodel(QupmtCUnitpriceDetailPK.class)
public class QupmtCUnitpriceDetailPK_ {

	/** The ccd. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetailPK, String> ccd;

	/** The c unitprice cd. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetailPK, String> cUnitpriceCd;

	/** The hist id. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetailPK, String> histId;
}