/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.entity.shift.estimate.commonset;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The Class KscstEstAlarmColor_.
 */
@StaticMetamodel(KscstEstAlarmColor.class)
public class KscstEstAlarmColor_ {

	/** The kscst est alarm color PK. */
	public static volatile SingularAttribute<KscstEstAlarmColor, KscstEstAlarmColorPK> kscstEstAlarmColorPK;
	
	/** The color cd. */
	public static volatile SingularAttribute<KscstEstAlarmColor, String> colorCd;
	
	/** The kscst est guide setting. */
	public static volatile SingularAttribute<KscstEstAlarmColor, KscstEstComSet> kscstEstGuideSetting;
}
