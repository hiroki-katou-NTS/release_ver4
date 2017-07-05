/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.entity.workrecord.closure;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The Class KclmtClosureHistPK_.
 */
@StaticMetamodel(KclmtClosureHistPK.class)
public class KclmtClosureHistPK_ {

	/** The ccid. */
	public static volatile SingularAttribute<KclmtClosureHistPK, String> cid;

	/** The closure id. */
	public static volatile SingularAttribute<KclmtClosureHistPK, String> closureId;
	
	/** The str YM. */
	public static volatile SingularAttribute<KclmtClosureHistPK, Integer> strYM;

}