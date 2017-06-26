package nts.uk.ctx.pr.report.infra.entity.salarydetail;

import java.util.Date;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(QlsptPaylstFormHead.class)
public class QlsptPaylstFormHead_ { 

    public static volatile SingularAttribute<QlsptPaylstFormHead, Date> insDate;
    public static volatile SingularAttribute<QlsptPaylstFormHead, String> updCcd;
    public static volatile SingularAttribute<QlsptPaylstFormHead, String> insCcd;
    public static volatile SingularAttribute<QlsptPaylstFormHead, String> insScd;
    public static volatile SingularAttribute<QlsptPaylstFormHead, String> formName;
    public static volatile SingularAttribute<QlsptPaylstFormHead, QlsptPaylstFormHeadPK> qlsptPaylstFormHeadPK;
    public static volatile SingularAttribute<QlsptPaylstFormHead, String> updScd;
    public static volatile ListAttribute<QlsptPaylstFormHead, QlsptPaylstFormDetail> qlsptPaylstFormDetailList;
    public static volatile SingularAttribute<QlsptPaylstFormHead, String> insPg;
    public static volatile SingularAttribute<QlsptPaylstFormHead, Date> updDate;
    public static volatile SingularAttribute<QlsptPaylstFormHead, Integer> exclusVer;

}