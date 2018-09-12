package nts.uk.ctx.core.dom.socialinsurance.welfarepensioninsurance;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/**
 * 等級毎標準月額
 */
@Getter
public class StandardGradePerMonth extends DomainObject {

    /**
     * 厚生年金等級
     */
    private int welfarePensionGrade;

    /**
     * 標準月額
     */
    private long standardMonthlyFee;

    /**
     * 等級毎標準月額
     *
     * @param welfarePensionGrade 厚生年金等級
     * @param standardMonthlyFee  標準月額
     */
    public StandardGradePerMonth(int welfarePensionGrade, long standardMonthlyFee) {
        this.welfarePensionGrade = welfarePensionGrade;
        this.standardMonthlyFee = standardMonthlyFee;
    }

}
