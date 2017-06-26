package nts.uk.file.pr.app.export.residentialtax.data;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.arc.time.GeneralDate;

@Data
@AllArgsConstructor
public class RetirementPaymentDto {
	private String personId;
    private GeneralDate payDate;
    private int trialPeriodSet;
    private int exclusionYears;
    private int additionalBoardYears;
    private int boardYears;
    private int totalPaymentMny;
    private int deduction1Mny;
    private int deduction2Mny;
    private int deduction3Mny;
    private int otherRetirementPayOp;
    private int taxCalMethodSet;
    private int incomeTaxMny;
    private int cityTaxMny;
    private int prefectureTaxMny;
    private int totalDeductionMny;
    private int actualRecieveMny;
    private String  withholdingMeno;
    
}
