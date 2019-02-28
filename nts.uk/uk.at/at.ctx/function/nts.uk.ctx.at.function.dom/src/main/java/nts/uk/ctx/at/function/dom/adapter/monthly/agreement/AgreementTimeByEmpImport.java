package nts.uk.ctx.at.function.dom.adapter.monthly.agreement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.at.shared.dom.monthly.agreement.PeriodAtrOfAgreement;

/**
 * @author dat.lh
 */
@Getter
@AllArgsConstructor
public class AgreementTimeByEmpImport {
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
    private AgreementTimeByPeriodImport agreementTime;

}
