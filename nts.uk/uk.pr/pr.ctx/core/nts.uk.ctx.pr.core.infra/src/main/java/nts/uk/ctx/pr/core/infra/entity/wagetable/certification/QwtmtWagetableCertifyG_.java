package nts.uk.ctx.pr.core.infra.entity.wagetable.certification;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QwtmtWagetableCertify;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QwtmtWagetableCertifyGPK;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-07T13:41:14")
@StaticMetamodel(QwtmtWagetableCertifyG.class)
public class QwtmtWagetableCertifyG_ { 

    public static volatile SingularAttribute<QwtmtWagetableCertifyG, Date> insDate;
    public static volatile SingularAttribute<QwtmtWagetableCertifyG, String> updCcd;
    public static volatile SingularAttribute<QwtmtWagetableCertifyG, String> updPg;
    public static volatile SingularAttribute<QwtmtWagetableCertifyG, QwtmtWagetableCertifyGPK> qwtmtWagetableCertifyGPK;
    public static volatile SingularAttribute<QwtmtWagetableCertifyG, String> insCcd;
    public static volatile SingularAttribute<QwtmtWagetableCertifyG, String> updScd;
    public static volatile SingularAttribute<QwtmtWagetableCertifyG, String> certifyGroupName;
    public static volatile SingularAttribute<QwtmtWagetableCertifyG, Date> updDate;
    public static volatile SingularAttribute<QwtmtWagetableCertifyG, Long> exclusVer;
    public static volatile ListAttribute<QwtmtWagetableCertifyG, QwtmtWagetableCertify> qwtmtWagetableCertifyList;
    public static volatile SingularAttribute<QwtmtWagetableCertifyG, Integer> multiApplySet;
    public static volatile SingularAttribute<QwtmtWagetableCertifyG, String> insScd;
    public static volatile SingularAttribute<QwtmtWagetableCertifyG, String> insPg;

}