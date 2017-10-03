/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.entity.workrecord.actuallock;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The Class KrcmtActualLock_.
 */
@StaticMetamodel(KrcmtActualLock.class)
public class KrcmtActualLock_ {

	/** The krcmt actual lock PK. */
	public static volatile SingularAttribute<KrcmtActualLock, KrcmtActualLockPK> krcmtActualLockPK;
	
	/** The d lock state. */
	public static volatile SingularAttribute<KrcmtActualLock, Short> dLockState;
	
	/** The m lock state. */
	public static volatile SingularAttribute<KrcmtActualLock, Short> mLockState;
}
