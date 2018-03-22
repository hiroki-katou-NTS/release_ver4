/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.infra.entity.securitypolicy.loginlog;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The Class SgwmtLogoutDataPK_.
 */
@StaticMetamodel(SgwmtLoginLogPK.class)
public class SgwmtLoginLogPK_ {

	/** The sgwmt logout data PK. */
	public static volatile SingularAttribute<SgwmtLoginLogPK, String> userId;

	/** The contract cd. */
	public static volatile SingularAttribute<SgwmtLoginLogPK, String> contractCd;
	
	/** The login method. */
	public static volatile SingularAttribute<SgwmtLoginLogPK, String> programId;
	
}
