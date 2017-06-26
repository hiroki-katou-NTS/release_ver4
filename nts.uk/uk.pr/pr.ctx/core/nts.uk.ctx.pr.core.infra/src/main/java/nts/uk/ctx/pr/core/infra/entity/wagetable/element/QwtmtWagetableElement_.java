package nts.uk.ctx.pr.core.infra.entity.wagetable.element;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import nts.uk.ctx.pr.core.infra.entity.wagetable.QwtmtWagetableHead;
import nts.uk.ctx.pr.core.infra.entity.wagetable.element.QwtmtWagetableElementPK;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableEleHist;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-07T13:41:13")
@StaticMetamodel(QwtmtWagetableElement.class)
public class QwtmtWagetableElement_ { 

    public static volatile SingularAttribute<QwtmtWagetableElement, Date> insDate;
    public static volatile SingularAttribute<QwtmtWagetableElement, String> updCcd;
    public static volatile SingularAttribute<QwtmtWagetableElement, String> updPg;
    public static volatile SingularAttribute<QwtmtWagetableElement, String> insCcd;
    public static volatile SingularAttribute<QwtmtWagetableElement, String> updScd;
    public static volatile SingularAttribute<QwtmtWagetableElement, Date> updDate;
    public static volatile SingularAttribute<QwtmtWagetableElement, Integer> exclusVer;
    public static volatile SingularAttribute<QwtmtWagetableElement, String> insScd;
    public static volatile SingularAttribute<QwtmtWagetableElement, QwtmtWagetableHead> qwtmtWagetableHead;
    public static volatile SingularAttribute<QwtmtWagetableElement, QwtmtWagetableElementPK> qwtmtWagetableElementPK;
    public static volatile ListAttribute<QwtmtWagetableElement, QwtmtWagetableEleHist> qwtmtWagetableEleHistList;
    public static volatile SingularAttribute<QwtmtWagetableElement, String> insPg;
    public static volatile SingularAttribute<QwtmtWagetableElement, Integer> demensionType;
    public static volatile SingularAttribute<QwtmtWagetableElement, String> demensionRefNo;

}