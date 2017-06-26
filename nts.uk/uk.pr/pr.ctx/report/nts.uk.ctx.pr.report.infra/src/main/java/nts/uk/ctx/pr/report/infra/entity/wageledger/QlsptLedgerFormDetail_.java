package nts.uk.ctx.pr.report.infra.entity.wageledger;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(QlsptLedgerFormDetail.class)
public class QlsptLedgerFormDetail_ { 

    public static volatile SingularAttribute<QlsptLedgerFormDetail, Date> insDate;
    public static volatile SingularAttribute<QlsptLedgerFormDetail, String> updCcd;
    public static volatile SingularAttribute<QlsptLedgerFormDetail, String> insCcd;
    public static volatile SingularAttribute<QlsptLedgerFormDetail, String> insScd;
    public static volatile SingularAttribute<QlsptLedgerFormDetail, String> updScd;
    public static volatile SingularAttribute<QlsptLedgerFormDetail, QlsptLedgerFormHead> qlsptLedgerFormHead;
    public static volatile SingularAttribute<QlsptLedgerFormDetail, Integer> dispOrder;
    public static volatile SingularAttribute<QlsptLedgerFormDetail, QlsptLedgerFormDetailPK> qlsptLedgerFormDetailPK;
    public static volatile SingularAttribute<QlsptLedgerFormDetail, String> insPg;
    public static volatile SingularAttribute<QlsptLedgerFormDetail, Date> updDate;
    public static volatile SingularAttribute<QlsptLedgerFormDetail, Integer> exclusVer;

}