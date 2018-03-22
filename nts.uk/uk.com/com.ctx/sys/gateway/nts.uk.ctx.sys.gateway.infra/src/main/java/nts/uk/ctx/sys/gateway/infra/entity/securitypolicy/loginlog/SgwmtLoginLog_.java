/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.infra.entity.securitypolicy.loginlog;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import nts.arc.time.GeneralDateTime;


/**
 * The Class SgwdtContract_.
 */
@StaticMetamodel(SgwmtLoginLog.class)
public class SgwmtLoginLog_ {
    
    /** The sgwmt logout data PK. */
    public static volatile SingularAttribute<SgwmtLoginLog, SgwmtLoginLogPK> sgwmtLogoutDataPK;
    
    /** The operation section. */
    public static volatile SingularAttribute<SgwmtLoginLog, Integer> operationSection;
    
    /** The process date time. */
    public static volatile SingularAttribute<SgwmtLoginLog, GeneralDateTime> processDateTime;
    
    /** The success or failure. */
    public static volatile SingularAttribute<SgwmtLoginLog, Integer> successOrFailure;
}
