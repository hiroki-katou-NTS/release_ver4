package nts.uk.ctx.pr.core.dom.socialinsurance.welfarepensioninsurance;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.DomainObject;

/**
 * 給与厚生年金保険料率
 */
@Getter
public class SalaryEmployeesPensionInsuranceRate extends DomainObject {

    /**
     * 事業主負担分計算方法
     */
    private EmployeeShareAmountMethod employeeShareAmountMethod;

    /**
     * 男子負担率
     */
    private EmployeesPensionContributionRate maleContributionRate;

    /**
     * 女子負担率
     */
    private EmployeesPensionContributionRate femaleContributionRate;

    /**
     * 端数区分
     */
    private EmployeesPensionClassification fractionClassification;

    /**
     * 給与厚生年金保険料率
     *
     * @param employeeShareAmountMethod 事業主負担分計算方法
     * @param maleContributionRate      男子負担率
     * @param femaleContributionRate    女子負担率
     * @param fractionClassification    端数区分
     */
    public SalaryEmployeesPensionInsuranceRate(int employeeShareAmountMethod, EmployeesPensionContributionRate maleContributionRate, EmployeesPensionContributionRate femaleContributionRate, EmployeesPensionClassification fractionClassification) {
        this.employeeShareAmountMethod = EnumAdaptor.valueOf(employeeShareAmountMethod, EmployeeShareAmountMethod.class);
        this.maleContributionRate = maleContributionRate;
        this.femaleContributionRate = femaleContributionRate;
        this.fractionClassification = fractionClassification;
    }
    
    public void changeDataWhenNotJoinFund (){
    	this.maleContributionRate.changeDataWhenNotJoinFund();
    	this.femaleContributionRate.changeDataWhenNotJoinFund();
    }
}
