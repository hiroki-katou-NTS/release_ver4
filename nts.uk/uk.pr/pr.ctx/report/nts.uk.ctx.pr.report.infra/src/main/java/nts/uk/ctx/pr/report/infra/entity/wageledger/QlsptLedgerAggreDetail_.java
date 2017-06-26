package nts.uk.ctx.pr.report.infra.entity.wageledger;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(QlsptLedgerAggreDetail.class)
public class QlsptLedgerAggreDetail_ { 

    public static volatile SingularAttribute<QlsptLedgerAggreDetail, QlsptLedgerAggreDetailPK> qlsptLedgerAggreDetailPK;
    public static volatile SingularAttribute<QlsptLedgerAggreDetail, Date> insDate;
    public static volatile SingularAttribute<QlsptLedgerAggreDetail, String> updCcd;
    public static volatile SingularAttribute<QlsptLedgerAggreDetail, String> insCcd;
    public static volatile SingularAttribute<QlsptLedgerAggreDetail, String> insScd;
    public static volatile SingularAttribute<QlsptLedgerAggreDetail, String> updScd;
    public static volatile SingularAttribute<QlsptLedgerAggreDetail, QlsptLedgerAggreHead> qlsptLedgerAggreHead;
    public static volatile SingularAttribute<QlsptLedgerAggreDetail, String> insPg;
    public static volatile SingularAttribute<QlsptLedgerAggreDetail, Date> updDate;
    public static volatile SingularAttribute<QlsptLedgerAggreDetail, Integer> exclusVer;

}