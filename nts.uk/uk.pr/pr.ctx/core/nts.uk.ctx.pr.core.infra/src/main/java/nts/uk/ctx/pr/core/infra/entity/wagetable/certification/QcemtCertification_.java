package nts.uk.ctx.pr.core.infra.entity.wagetable.certification;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QcemtCertificationPK;
import nts.uk.ctx.pr.core.infra.entity.wagetable.certification.QwtmtWagetableCertify;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-07T13:41:14")
@StaticMetamodel(QcemtCertification.class)
public class QcemtCertification_ { 

    public static volatile SingularAttribute<QcemtCertification, Date> insDate;
    public static volatile SingularAttribute<QcemtCertification, String> updCcd;
    public static volatile SingularAttribute<QcemtCertification, String> updPg;
    public static volatile SingularAttribute<QcemtCertification, String> insCcd;
    public static volatile SingularAttribute<QcemtCertification, String> insScd;
    public static volatile SingularAttribute<QcemtCertification, QcemtCertificationPK> qcemtCertificationPK;
    public static volatile SingularAttribute<QcemtCertification, String> name;
    public static volatile SingularAttribute<QcemtCertification, String> updScd;
    public static volatile SingularAttribute<QcemtCertification, Date> updDate;
    public static volatile SingularAttribute<QcemtCertification, Long> exclusVer;
    public static volatile SingularAttribute<QcemtCertification, String> insPg;
    public static volatile ListAttribute<QcemtCertification, QwtmtWagetableCertify> qwtmtWagetableCertifyList;

}