/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.entity.shift.basicworkregister;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The Class KscmtCompanyWorkSet_.
 */
@StaticMetamodel(KscmtCompanyWorkSet.class)
public class KscmtCompanyWorkSet_ {
	
	/** The kcbmt company work set PK. */
	public static volatile SingularAttribute<KscmtCompanyWorkSet, KscmtCompanyWorkSetPK> kscmtCompanyWorkSetPK;
	
	/** The work type code. */
	public static volatile SingularAttribute<KscmtCompanyWorkSet, String> worktypeCode;

	/** The working code. */
	public static volatile SingularAttribute<KscmtCompanyWorkSet, String> workingCode;

}
