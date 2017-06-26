package nts.uk.ctx.pr.report.infra.entity.salarydetail;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(QlsptPaylstAggreDetail.class)
public class QlsptPaylstAggreDetail_ { 

    public static volatile SingularAttribute<QlsptPaylstAggreDetail, QlsptPaylstAggreDetailPK> qlsptPaylstAggreDetailPK;
    public static volatile SingularAttribute<QlsptPaylstAggreDetail, Date> insDate;
    public static volatile SingularAttribute<QlsptPaylstAggreDetail, String> updCcd;
    public static volatile SingularAttribute<QlsptPaylstAggreDetail, QlsptPaylstAggreHead> qlsptPaylstAggreHead;
    public static volatile SingularAttribute<QlsptPaylstAggreDetail, String> insCcd;
    public static volatile SingularAttribute<QlsptPaylstAggreDetail, String> insScd;
    public static volatile SingularAttribute<QlsptPaylstAggreDetail, String> updScd;
    public static volatile SingularAttribute<QlsptPaylstAggreDetail, String> insPg;
    public static volatile SingularAttribute<QlsptPaylstAggreDetail, Date> updDate;
    public static volatile SingularAttribute<QlsptPaylstAggreDetail, Integer> exclusVer;

}