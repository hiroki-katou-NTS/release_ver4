package nts.uk.ctx.pr.report.infra.entity.wageledger;

import java.util.Date;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(QlsptLedgerFormHead.class)
public class QlsptLedgerFormHead_ { 

    public static volatile SingularAttribute<QlsptLedgerFormHead, Integer> print1pageByPersonSet;
    public static volatile SingularAttribute<QlsptLedgerFormHead, Date> insDate;
    public static volatile SingularAttribute<QlsptLedgerFormHead, String> updCcd;
    public static volatile SingularAttribute<QlsptLedgerFormHead, String> insCcd;
    public static volatile SingularAttribute<QlsptLedgerFormHead, String> insScd;
    public static volatile SingularAttribute<QlsptLedgerFormHead, String> formName;
    public static volatile SingularAttribute<QlsptLedgerFormHead, QlsptLedgerFormHeadPK> qlsptLedgerFormHeadPK;
    public static volatile SingularAttribute<QlsptLedgerFormHead, String> updScd;
    public static volatile SingularAttribute<QlsptLedgerFormHead, Date> updDate;
    public static volatile SingularAttribute<QlsptLedgerFormHead, Integer> exclusVer;
    public static volatile SingularAttribute<QlsptLedgerFormHead, String> insPg;
    public static volatile ListAttribute<QlsptLedgerFormHead, QlsptLedgerFormDetail> qlsptLedgerFormDetailList;

}