package nts.uk.ctx.core.infra.entity.socialinsurance.healthinsurance;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.core.dom.socialinsurance.healthinsurance.HealthInsuranceMonthlyFee;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 健康保険月額保険料額
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QPBMT_HEALTH_INS_MON_FEE")
public class QpbmtHealthInsuranceMonthlyFee extends UkJpaEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 履歴ID
     */
    @Column(name = "HISTORY_ID")
    @Id
    public String historyId;

    /**
     * 自動計算実施区分
     */
    @Basic(optional = false)
    @Column(name = "AUTO_CALCULATION_CLS")
    public int autoCalculationCls;

    /**
     * 介護保険料率
     */
    @Basic(optional = false)
    @Column(name = "INDIVIDUAL_LONG_CARE_INSURANCE_RATE")
    public BigDecimal individualLongCareInsuranceRate;

    /**
     * 基本保険料率
     */
    @Basic(optional = false)
    @Column(name = "INDIVIDUAL_BASIC_INSURANCE_RATE")
    public BigDecimal individualBasicInsuranceRate;

    /**
     * 健康保険料率
     */
    @Basic(optional = false)
    @Column(name = "INDIVIDUAL_HEALTH_INSURANCE_RATE")
    public BigDecimal individualHealthInsuranceRate;

    /**
     * 端数区分
     */
    @Basic(optional = false)
    @Column(name = "INDIVIDUAL_FRACTION_CLS")
    public int individualFractionCls;

    /**
     * 特定保険料率
     */
    @Basic(optional = false)
    @Column(name = "INDIVIDUAL_SPECIAL_INSURANCE_RATE")
    public BigDecimal individualSpecialInsuranceRate;

    /**
     * 事業主負担分計算方法
     */
    @Basic(optional = false)
    @Column(name = "EMPLOYEE_SHARE_AMOUNT_METHOD")
    public int employeeShareAmountMethod;

    /**
     * 介護保険料率
     */
    @Basic(optional = false)
    @Column(name = "EMPLOYEE_LONG_CARE_INSURANCE_RATE")
    public BigDecimal employeeLongCareInsuranceRate;

    /**
     * 基本保険料率
     */
    @Basic(optional = false)
    @Column(name = "EMPLOYEE_BASIC_INSURANCE_RATE")
    public BigDecimal employeeBasicInsuranceRate;

    /**
     * 健康保険料率
     */
    @Basic(optional = false)
    @Column(name = "EMPLOYEE_HEALTH_INSURANCE_RATE")
    public BigDecimal employeeHealthInsuranceRate;

    /**
     * 端数区分
     */
    @Basic(optional = false)
    @Column(name = "EMPLOYEE_FRACTION_CLS")
    public int employeeFractionCls;

    /**
     * 特定保険料率
     */
    @Basic(optional = false)
    @Column(name = "EMPLOYEE_SPECIAL_INSURANCE_RATE")
    public BigDecimal employeeSpecialInsuranceRate;

    @Override
    protected Object getKey() {
        return historyId;
    }

    /**
     * Convert domain to QpbmtHealthInsuranceMonthlyFee entity
     *
     * @param domain HealthInsuranceMonthlyFee
     * @return QpbmtHealthInsuranceMonthlyFee
     */
    private QpbmtHealthInsuranceMonthlyFee toEntity(HealthInsuranceMonthlyFee domain) {
        return new QpbmtHealthInsuranceMonthlyFee(domain.getHistoryId(), domain.getAutoCalculationCls().value,
                domain.getHealthInsuranceRate().getIndividualBurdenRatio().getLongCareInsuranceRate().v(),
                domain.getHealthInsuranceRate().getIndividualBurdenRatio().getBasicInsuranceRate().v(),
                domain.getHealthInsuranceRate().getIndividualBurdenRatio().getHealthInsuranceRate().v(),
                domain.getHealthInsuranceRate().getIndividualBurdenRatio().getFractionCls().value,
                domain.getHealthInsuranceRate().getIndividualBurdenRatio().getSpecialInsuranceRate().v(),
                domain.getHealthInsuranceRate().getEmployeeShareAmountMethod().value,
                domain.getHealthInsuranceRate().getEmployeeBurdenRatio().getLongCareInsuranceRate().v(),
                domain.getHealthInsuranceRate().getEmployeeBurdenRatio().getBasicInsuranceRate().v(),
                domain.getHealthInsuranceRate().getEmployeeBurdenRatio().getHealthInsuranceRate().v(),
                domain.getHealthInsuranceRate().getEmployeeBurdenRatio().getFractionCls().value,
                domain.getHealthInsuranceRate().getEmployeeBurdenRatio().getSpecialInsuranceRate().v());
    }
}
