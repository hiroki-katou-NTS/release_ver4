package nts.uk.ctx.pr.report.infra.entity.salarydetail;

import java.util.Date;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(QlsptPaylstAggreHead.class)
public class QlsptPaylstAggreHead_ { 

    public static volatile SingularAttribute<QlsptPaylstAggreHead, QlsptPaylstAggreHeadPK> qlsptPaylstAggreHeadPK;
    public static volatile ListAttribute<QlsptPaylstAggreHead, QlsptPaylstAggreDetail> qlsptPaylstAggreDetailList;
    public static volatile SingularAttribute<QlsptPaylstAggreHead, Date> insDate;
    public static volatile SingularAttribute<QlsptPaylstAggreHead, String> updCcd;
    public static volatile SingularAttribute<QlsptPaylstAggreHead, String> insCcd;
    public static volatile SingularAttribute<QlsptPaylstAggreHead, String> insScd;
    public static volatile SingularAttribute<QlsptPaylstAggreHead, String> aggregateName;
    public static volatile SingularAttribute<QlsptPaylstAggreHead, String> updScd;
    public static volatile SingularAttribute<QlsptPaylstAggreHead, String> insPg;
    public static volatile SingularAttribute<QlsptPaylstAggreHead, Date> updDate;
    public static volatile SingularAttribute<QlsptPaylstAggreHead, Integer> exclusVer;

}