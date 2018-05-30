/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.entity.optitem;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The Class KrcstOptionalItemPK_.
 */
@StaticMetamodel(KrcstOptionalItemPK.class)
public class KrcstOptionalItemPK_ {

	/** The cid. */
	public static volatile SingularAttribute<KrcstOptionalItemPK, String> cid;

	/** The optional item no. */
	public static volatile SingularAttribute<KrcstOptionalItemPK, Integer> optionalItemNo;

}
