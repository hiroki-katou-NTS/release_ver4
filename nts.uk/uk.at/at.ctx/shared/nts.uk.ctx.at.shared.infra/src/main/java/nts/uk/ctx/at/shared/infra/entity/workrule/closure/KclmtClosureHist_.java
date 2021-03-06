/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.workrule.closure;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The Class KclmtClosureHist_.
 */
@StaticMetamodel(KshmtClosureHist.class)
public class KclmtClosureHist_ {

	/** The kclmt closure hist PK. */
	public static volatile SingularAttribute<KshmtClosureHist, KclmtClosureHistPK> kclmtClosureHistPK;
	
	/** The name. */
	public static volatile SingularAttribute<KshmtClosureHist, String> name;
	
	/** The end D. */
	public static volatile SingularAttribute<KshmtClosureHist, Integer> endYM;
	
	/** The close day. */
	public static volatile SingularAttribute<KshmtClosureHist, Integer> closeDay;
	
	/** The is last day. */
	public static volatile SingularAttribute<KshmtClosureHist, Integer> isLastDay;
}
