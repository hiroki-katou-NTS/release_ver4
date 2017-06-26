package nts.uk.ctx.pr.core.dom.retirement.payment;

public class RetirementPaymentDomainService {
	
	/**
	 * auto Caculator Money
	 * @param retirementPayment Retirement Payment
	 */
	public static void autoCalculate(RetirementPayment retirementPayment) {
		int serviceYear = 20; 
		int age = 30;
		int retirementPayOption = retirementPayment.getRetirementPayOption().value;
		int totalPaymentMoney = retirementPayment.getTotalPaymentMoney().v();
		int incomeTax = retirementPayment.getIncomeTaxMoney().v();
        int reduction = 0;
        if(serviceYear > 20 ) {
            reduction = 8000000 + 700000*(serviceYear - 20);
            if(reduction < 800000) reduction = 800000;
        } else reduction = 400000 * serviceYear;
        int tax = (totalPaymentMoney - reduction)/2;
        if(tax < 0) tax = 0;
        if(retirementPayOption==2){
        	incomeTax = ((int)(totalPaymentMoney*20.42/100));
        } else { 
        	if((1000 <= totalPaymentMoney)&&(totalPaymentMoney < 1950000)) incomeTax =  totalPaymentMoney*5/100-0;
        	else if((1950000 <= totalPaymentMoney)&&(totalPaymentMoney < 3300000)) incomeTax =  totalPaymentMoney*10/100-97500;
        	else if((3300000 <= totalPaymentMoney)&&(totalPaymentMoney < 6950000)) incomeTax =  totalPaymentMoney*20/100-427500;
        	else if((6950000 <= totalPaymentMoney)&&(totalPaymentMoney < 9000000)) incomeTax =  totalPaymentMoney*23/100-636000;
        	else if((9000000 <= totalPaymentMoney)&&(totalPaymentMoney < 18000000)) incomeTax =  totalPaymentMoney*33/100-1536000;
        	else if((18000000 <= totalPaymentMoney)&&(totalPaymentMoney < 40000000)) incomeTax =  totalPaymentMoney*40/100-2796000;
        	else if((40000000 <= totalPaymentMoney)) incomeTax =  totalPaymentMoney*45/100-4796000;
        	else incomeTax =  totalPaymentMoney*0-0;
            if((25 <= age) && (age <= 49)) incomeTax *= 102.1/100;
        }
        int cityTax = (tax*6/100)*90/100;
        int prefectureTax = (tax*4/100)*90/100;
        int deduction1 = retirementPayment.getDeduction1Money().v();
        int deduction2 = retirementPayment.getDeduction2Money().v();
        int deduction3 = retirementPayment.getDeduction3Money().v();
        int totalDeclarationMoney = 
            (deduction1>0?deduction1:0)+
            (deduction2>0?deduction2:0)+
            (deduction3>0?deduction3:0)+
            (incomeTax>0?incomeTax:0)+
            (cityTax>0?cityTax:0)+
            (prefectureTax>0?prefectureTax:0);
        retirementPayment.autoCalculate(
        		new PaymentMoney(incomeTax), 
        		new PaymentMoney(cityTax), 
        		new PaymentMoney(prefectureTax), 
        		new PaymentMoney(totalDeclarationMoney), 
        		new PaymentMoney(totalPaymentMoney - totalDeclarationMoney));
    }
	
	/**
	 * manual Caculator Money
	 * @param retirementPayment Retirement Payment
	 */
    public static void manualCalculate(RetirementPayment retirementPayment) {
        int totalPaymentMoney = retirementPayment.getTotalPaymentMoney().v();
        int deduction1 = retirementPayment.getDeduction1Money().v();
        int deduction2 = retirementPayment.getDeduction2Money().v();
        int deduction3 = retirementPayment.getDeduction3Money().v();
        int incomeTax = retirementPayment.getIncomeTaxMoney().v();
        int cityTax = retirementPayment.getCityTaxMoney().v();
        int prefectureTax = retirementPayment.getPrefectureTaxMoney().v();
        int totalDeclarationMoney = 
            (deduction1>0?deduction1:0)+
            (deduction2>0?deduction2:0)+
            (deduction3>0?deduction3:0)+
            (incomeTax>0?incomeTax:0)+
            (cityTax>0?cityTax:0)+
            (prefectureTax>0?prefectureTax:0);
        retirementPayment.manualCalculate(
        		new PaymentMoney(totalDeclarationMoney), 
        		new PaymentMoney(totalPaymentMoney - totalDeclarationMoney));
    }
}
