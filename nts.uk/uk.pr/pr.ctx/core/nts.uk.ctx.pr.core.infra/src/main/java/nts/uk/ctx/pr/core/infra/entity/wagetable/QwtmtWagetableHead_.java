package nts.uk.ctx.pr.core.infra.entity.wagetable;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import nts.uk.ctx.pr.core.infra.entity.wagetable.element.QwtmtWagetableElement;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableHist;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-07T13:41:14")
@StaticMetamodel(QwtmtWagetableHead.class)
public class QwtmtWagetableHead_ { 

    public static volatile SingularAttribute<QwtmtWagetableHead, Date> insDate;
    public static volatile SingularAttribute<QwtmtWagetableHead, String> updCcd;
    public static volatile SingularAttribute<QwtmtWagetableHead, String> updPg;
    public static volatile ListAttribute<QwtmtWagetableHead, QwtmtWagetableElement> wagetableElementList;
    public static volatile SingularAttribute<QwtmtWagetableHead, String> insCcd;
    public static volatile SingularAttribute<QwtmtWagetableHead, String> memo;
    public static volatile SingularAttribute<QwtmtWagetableHead, QwtmtWagetableHeadPK> qwtmtWagetableHeadPK;
    public static volatile SingularAttribute<QwtmtWagetableHead, String> updScd;
    public static volatile SingularAttribute<QwtmtWagetableHead, String> wageTableName;
    public static volatile SingularAttribute<QwtmtWagetableHead, Date> updDate;
    public static volatile SingularAttribute<QwtmtWagetableHead, Integer> exclusVer;
    public static volatile SingularAttribute<QwtmtWagetableHead, String> insScd;
    public static volatile SingularAttribute<QwtmtWagetableHead, Integer> demensionSet;
    public static volatile SingularAttribute<QwtmtWagetableHead, String> insPg;
    public static volatile ListAttribute<QwtmtWagetableHead, QwtmtWagetableHist> wagetableHistList;

}