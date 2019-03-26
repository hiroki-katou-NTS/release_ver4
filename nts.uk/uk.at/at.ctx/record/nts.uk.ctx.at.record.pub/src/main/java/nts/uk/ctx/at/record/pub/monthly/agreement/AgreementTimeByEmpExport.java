package nts.uk.ctx.at.record.pub.monthly.agreement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.monthly.agreement.export.AgreementTimeByEmp;
import nts.uk.ctx.at.shared.dom.monthly.agreement.PeriodAtrOfAgreement;

@Getter
@Setter
@NoArgsConstructor
public class AgreementTimeByEmpExport {
    /**
     * 社員ID
     */
    private String employeeId;
    /**
     * 期間区分
     */
    private PeriodAtrOfAgreement periodAtr;
    /**
     * 指定期間36協定時間
     */
    private AgreementTimeByPeriod agreementTime;

    public static AgreementTimeByEmpExport fromDomain(AgreementTimeByEmp domain) {
        AgreementTimeByEmpExport time = new AgreementTimeByEmpExport();
        time.employeeId = domain.getEmployeeId();
        time.periodAtr = domain.getPeriodAtr();
        time.agreementTime = AgreementTimeByPeriod.of(domain.getAgreementTime().getStartMonth(),
                domain.getAgreementTime().getEndMonth(), domain.getAgreementTime().getAgreementTime(),
                domain.getAgreementTime().getLimitErrorTime(), domain.getAgreementTime().getLimitAlarmTime(),
                domain.getAgreementTime().getExceptionLimitErrorTime(), domain.getAgreementTime().getExceptionLimitAlarmTime(),
                domain.getAgreementTime().getStatus());
        return time;
    }
}
