/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn;

import java.math.BigDecimal;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The Class QismtHealthInsuAvgearnPK_.
 */
@StaticMetamodel(QismtPensionAvgearnDPK.class)
public class QismtPensionAvgearnDPK_ {

	/** The ccd. */
	public static volatile SingularAttribute<QismtPensionAmountPK, String> ccd;

	/** The hist id. */
	public static volatile SingularAttribute<QismtPensionAmountPK, String> histId;

	/** The health insu grade. */
	public static volatile SingularAttribute<QismtPensionAmountPK, BigDecimal> pensionGrade;
}