package nts.uk.ctx.at.record.infra.entity.divergence.time;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDrtPK;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-03-12T19:03:47")
@StaticMetamodel(KrcstDrt.class)
public class KrcstDrt_ { 

    public static volatile SingularAttribute<KrcstDrt, String> insDate;
    public static volatile SingularAttribute<KrcstDrt, String> updCcd;
    public static volatile SingularAttribute<KrcstDrt, String> updPg;
    public static volatile SingularAttribute<KrcstDrt, String> insCcd;
    public static volatile SingularAttribute<KrcstDrt, BigDecimal> alarmTime;
    public static volatile SingularAttribute<KrcstDrt, String> updScd;
    public static volatile SingularAttribute<KrcstDrt, BigDecimal> exclusVer;
    public static volatile SingularAttribute<KrcstDrt, String> updDate;
    public static volatile SingularAttribute<KrcstDrt, BigDecimal> errorTime;
    public static volatile SingularAttribute<KrcstDrt, BigDecimal> dvgcTimeUseSet;
    public static volatile SingularAttribute<KrcstDrt, String> insScd;
    public static volatile SingularAttribute<KrcstDrt, KrcstDrtPK> id;
    public static volatile SingularAttribute<KrcstDrt, String> insPg;

}