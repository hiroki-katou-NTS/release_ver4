package nts.uk.ctx.at.record.infra.entity.divergence.reason;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import nts.uk.ctx.at.record.infra.entity.divergence.reason.KrcstDvgcReasonPK;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-03-12T19:03:47")
@StaticMetamodel(KrcstDvgcReason.class)
public class KrcstDvgcReason_ { 

    public static volatile SingularAttribute<KrcstDvgcReason, String> reason;
    public static volatile SingularAttribute<KrcstDvgcReason, String> insDate;
    public static volatile SingularAttribute<KrcstDvgcReason, String> updCcd;
    public static volatile SingularAttribute<KrcstDvgcReason, String> updPg;
    public static volatile SingularAttribute<KrcstDvgcReason, String> insCcd;
    public static volatile SingularAttribute<KrcstDvgcReason, String> insScd;
    public static volatile SingularAttribute<KrcstDvgcReason, String> updScd;
    public static volatile SingularAttribute<KrcstDvgcReason, KrcstDvgcReasonPK> id;
    public static volatile SingularAttribute<KrcstDvgcReason, BigDecimal> exclusVer;
    public static volatile SingularAttribute<KrcstDvgcReason, String> updDate;
    public static volatile SingularAttribute<KrcstDvgcReason, String> insPg;
    public static volatile SingularAttribute<KrcstDvgcReason, BigDecimal> reasonRequired;

}