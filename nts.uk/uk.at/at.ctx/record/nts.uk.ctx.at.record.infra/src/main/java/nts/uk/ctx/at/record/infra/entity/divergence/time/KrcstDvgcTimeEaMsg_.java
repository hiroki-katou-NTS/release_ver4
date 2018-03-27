package nts.uk.ctx.at.record.infra.entity.divergence.time;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcTimeEaMsgPK;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-03-12T19:03:47")
@StaticMetamodel(KrcstDvgcTimeEaMsg.class)
public class KrcstDvgcTimeEaMsg_ { 

    public static volatile SingularAttribute<KrcstDvgcTimeEaMsg, String> insDate;
    public static volatile SingularAttribute<KrcstDvgcTimeEaMsg, String> updCcd;
    public static volatile SingularAttribute<KrcstDvgcTimeEaMsg, String> updPg;
    public static volatile SingularAttribute<KrcstDvgcTimeEaMsg, String> insCcd;
    public static volatile SingularAttribute<KrcstDvgcTimeEaMsg, String> insScd;
    public static volatile SingularAttribute<KrcstDvgcTimeEaMsg, String> errorMessage;
    public static volatile SingularAttribute<KrcstDvgcTimeEaMsg, String> alarmMessage;
    public static volatile SingularAttribute<KrcstDvgcTimeEaMsg, String> updScd;
    public static volatile SingularAttribute<KrcstDvgcTimeEaMsg, KrcstDvgcTimeEaMsgPK> id;
    public static volatile SingularAttribute<KrcstDvgcTimeEaMsg, BigDecimal> exclusVer;
    public static volatile SingularAttribute<KrcstDvgcTimeEaMsg, String> updDate;
    public static volatile SingularAttribute<KrcstDvgcTimeEaMsg, String> insPg;

}