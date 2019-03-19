package nts.uk.ctx.at.record.dom.monthly.agreement.export;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.at.shared.dom.monthly.agreement.PeriodAtrOfAgreement;

@Getter
@AllArgsConstructor
public class AgreementTimeByEmp {
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
}
