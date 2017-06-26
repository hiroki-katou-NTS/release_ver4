/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn;

import java.math.BigDecimal;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The Class QismtHealthInsuAvgearnPK_.
 */
@StaticMetamodel(QismtHealInsuAvgearnDPK.class)
public class QismtHealInsuAvgearnDPK_ {

	/** The hist id. */
	public static volatile SingularAttribute<QismtHealInsuAvgearnDPK, String> histId;

	/** The health insu grade. */
	public static volatile SingularAttribute<QismtHealInsuAvgearnDPK, BigDecimal> healthInsuGrade;
	
}