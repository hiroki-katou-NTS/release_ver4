package nts.uk.ctx.pr.core.infra.entity.wagetable.history;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableNumPK;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-07T13:41:14")
@StaticMetamodel(QwtmtWagetableNum.class)
public class QwtmtWagetableNum_ { 

    public static volatile SingularAttribute<QwtmtWagetableNum, String> elementId;
    public static volatile SingularAttribute<QwtmtWagetableNum, Date> insDate;
    public static volatile SingularAttribute<QwtmtWagetableNum, String> updCcd;
    public static volatile SingularAttribute<QwtmtWagetableNum, String> updPg;
    public static volatile SingularAttribute<QwtmtWagetableNum, String> insCcd;
    public static volatile SingularAttribute<QwtmtWagetableNum, BigDecimal> elementStr;
    public static volatile SingularAttribute<QwtmtWagetableNum, String> updScd;
    public static volatile SingularAttribute<QwtmtWagetableNum, BigDecimal> elementEnd;
    public static volatile SingularAttribute<QwtmtWagetableNum, Date> updDate;
    public static volatile SingularAttribute<QwtmtWagetableNum, Integer> exclusVer;
    public static volatile SingularAttribute<QwtmtWagetableNum, QwtmtWagetableNumPK> qwtmtWagetableNumPK;
    public static volatile SingularAttribute<QwtmtWagetableNum, String> insScd;
    public static volatile SingularAttribute<QwtmtWagetableNum, String> insPg;

}