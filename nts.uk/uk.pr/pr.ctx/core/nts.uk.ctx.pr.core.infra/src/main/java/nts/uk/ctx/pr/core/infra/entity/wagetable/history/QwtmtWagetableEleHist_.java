package nts.uk.ctx.pr.core.infra.entity.wagetable.history;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableCd;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableEleHistPK;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-07T13:41:13")
@StaticMetamodel(QwtmtWagetableEleHist.class)
public class QwtmtWagetableEleHist_ { 

    public static volatile SingularAttribute<QwtmtWagetableEleHist, BigDecimal> demensionUpperLimit;
    public static volatile SingularAttribute<QwtmtWagetableEleHist, QwtmtWagetableEleHistPK> qwtmtWagetableEleHistPK;
    public static volatile SingularAttribute<QwtmtWagetableEleHist, Date> insDate;
    public static volatile SingularAttribute<QwtmtWagetableEleHist, String> updCcd;
    public static volatile SingularAttribute<QwtmtWagetableEleHist, String> updPg;
    public static volatile SingularAttribute<QwtmtWagetableEleHist, String> insCcd;
    public static volatile SingularAttribute<QwtmtWagetableEleHist, String> updScd;
    public static volatile SingularAttribute<QwtmtWagetableEleHist, Date> updDate;
    public static volatile SingularAttribute<QwtmtWagetableEleHist, Integer> exclusVer;
    public static volatile SingularAttribute<QwtmtWagetableEleHist, String> insScd;
    public static volatile SingularAttribute<QwtmtWagetableEleHist, BigDecimal> demensionLowerLimit;
    public static volatile ListAttribute<QwtmtWagetableEleHist, QwtmtWagetableCd> qwtmtWagetableCdList;
    public static volatile SingularAttribute<QwtmtWagetableEleHist, BigDecimal> demensionInterval;
    public static volatile SingularAttribute<QwtmtWagetableEleHist, String> insPg;

}