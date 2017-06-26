/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.labor.accidentrate;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The Class QismtWorkAccidentInsuPK_.
 */
@StaticMetamodel(QismtWorkAccidentInsuPK.class)
public class QismtWorkAccidentInsuPK_ {

	/** The ccd. */
	public static volatile SingularAttribute<QismtWorkAccidentInsuPK, String> ccd;

	/** The hist id. */
	public static volatile SingularAttribute<QismtWorkAccidentInsuPK, String> histId;

	/** The wa insu cd. */
	public static volatile SingularAttribute<QismtWorkAccidentInsuPK, Integer> waInsuCd;

}