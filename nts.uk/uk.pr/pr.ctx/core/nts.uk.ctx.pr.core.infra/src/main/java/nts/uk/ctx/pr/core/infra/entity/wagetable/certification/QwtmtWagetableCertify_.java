package nts.uk.ctx.pr.core.infra.entity.wagetable.certification;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QcemtCertification;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QwtmtWagetableCertifyG;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QwtmtWagetableCertifyPK;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-07T13:41:14")
@StaticMetamodel(QwtmtWagetableCertify.class)
public class QwtmtWagetableCertify_ { 

    public static volatile SingularAttribute<QwtmtWagetableCertify, QcemtCertification> qcemtCertification;
    public static volatile SingularAttribute<QwtmtWagetableCertify, Date> insDate;
    public static volatile SingularAttribute<QwtmtWagetableCertify, String> updCcd;
    public static volatile SingularAttribute<QwtmtWagetableCertify, String> updPg;
    public static volatile SingularAttribute<QwtmtWagetableCertify, QwtmtWagetableCertifyPK> qwtmtWagetableCertifyPK;
    public static volatile SingularAttribute<QwtmtWagetableCertify, String> insCcd;
    public static volatile SingularAttribute<QwtmtWagetableCertify, String> insScd;
    public static volatile SingularAttribute<QwtmtWagetableCertify, String> updScd;
    public static volatile SingularAttribute<QwtmtWagetableCertify, QwtmtWagetableCertifyG> qwtmtWagetableCertifyG;
    public static volatile SingularAttribute<QwtmtWagetableCertify, Date> updDate;
    public static volatile SingularAttribute<QwtmtWagetableCertify, Long> exclusVer;
    public static volatile SingularAttribute<QwtmtWagetableCertify, String> insPg;

}