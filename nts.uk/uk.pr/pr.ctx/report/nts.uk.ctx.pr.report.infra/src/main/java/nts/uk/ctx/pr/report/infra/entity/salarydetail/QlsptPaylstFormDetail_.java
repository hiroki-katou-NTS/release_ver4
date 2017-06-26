package nts.uk.ctx.pr.report.infra.entity.salarydetail;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(QlsptPaylstFormDetail.class)
public class QlsptPaylstFormDetail_ { 

    public static volatile SingularAttribute<QlsptPaylstFormDetail, QlsptPaylstFormHead> qlsptPaylstFormHead;
    public static volatile SingularAttribute<QlsptPaylstFormDetail, QlsptPaylstFormDetailPK> qlsptPaylstFormDetailPK;
    public static volatile SingularAttribute<QlsptPaylstFormDetail, Date> insDate;
    public static volatile SingularAttribute<QlsptPaylstFormDetail, String> updCcd;
    public static volatile SingularAttribute<QlsptPaylstFormDetail, String> insCcd;
    public static volatile SingularAttribute<QlsptPaylstFormDetail, String> insScd;
    public static volatile SingularAttribute<QlsptPaylstFormDetail, String> updScd;
    public static volatile SingularAttribute<QlsptPaylstFormDetail, Integer> dispOrder;
    public static volatile SingularAttribute<QlsptPaylstFormDetail, String> insPg;
    public static volatile SingularAttribute<QlsptPaylstFormDetail, Date> updDate;
    public static volatile SingularAttribute<QlsptPaylstFormDetail, Integer> exclusVer;

}