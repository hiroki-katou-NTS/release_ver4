package nts.uk.ctx.pr.shared.infra.entity.socialinsurance.employeesociainsur.empcomofficehis;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.pr.shared.dom.socialinsurance.employeesociainsur.empcomofficehis.AffOfficeInformation;
import nts.uk.ctx.pr.shared.dom.socialinsurance.employeesociainsur.empcomofficehis.EmpCorpHealthOffHis;
import nts.uk.ctx.pr.shared.dom.socialinsurance.employeesociainsur.empcomofficehis.SocialInsuranceOfficeCode;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
* 社員社保事業所所属履歴
*/
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QQSMT_EMP_CORP_OFF_HIS")
public class QqsmtEmpCorpOffHis extends UkJpaEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**
    * ID
    */
    @EmbeddedId
    public QqsmtEmpCorpOffHisPk empCorpOffHisPk;
    
    /**
    * 開始日
    */
    @Basic(optional = false)
    @Column(name = "START_DATE")
    public GeneralDate startDate;
    
    /**
    * 終了日
    */
    @Basic(optional = false)
    @Column(name = "END_DATE")
    public GeneralDate endDate;
    
    @Override
    protected Object getKey()
    {
        return empCorpOffHisPk;
    }

    public static EmpCorpHealthOffHis toDomain(List<QqsmtEmpCorpOffHis> entity) {
        String empID = entity.get(0).empCorpOffHisPk.employeeId;
        String historyID = entity.get(0).empCorpOffHisPk.historyId;
        List<DateHistoryItem> period = new ArrayList<>();
        entity.forEach(x -> {
            DatePeriod datePeriod = new DatePeriod(x.startDate,x.endDate);
            DateHistoryItem historyItem = new DateHistoryItem(historyID,datePeriod);
            period.add(historyItem);
        });

        return new EmpCorpHealthOffHis(empID,period);
    }

    public  AffOfficeInformation toDomain(){
        return new AffOfficeInformation(this.empCorpOffHisPk.historyId,
                new SocialInsuranceOfficeCode(this.empCorpOffHisPk.socialInsuranceOfficeCd)
                );
    }
    public static QqsmtEmpCorpOffHis toEntity(EmpCorpHealthOffHis domain) {
        return null;
    }

}
