/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.vacation.setting.compensatoryleave;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The Class KocmtOccurrenceSetPK_.
 */
@StaticMetamodel(KocmtOccurrenceSetPK.class)
public class KocmtOccurrenceSetPK_ {
    
    /** The cid. */
    public static volatile SingularAttribute<KocmtOccurrenceSetPK, String> cid;
    
    /** The occurr type. */
    public static volatile SingularAttribute<KocmtOccurrenceSetPK, Integer> occurrType;
}
