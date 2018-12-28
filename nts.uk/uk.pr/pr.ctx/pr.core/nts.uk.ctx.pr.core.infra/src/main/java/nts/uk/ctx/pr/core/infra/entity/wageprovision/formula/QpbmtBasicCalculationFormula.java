package nts.uk.ctx.pr.core.infra.entity.wageprovision.formula;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import nts.arc.primitive.PrimitiveValueBase;
import nts.uk.ctx.pr.core.dom.wageprovision.formula.BasicCalculationForm;
import nts.uk.ctx.pr.core.dom.wageprovision.formula.BasicCalculationFormula;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.pr.core.dom.wageprovision.formula.BasicCalculationItemCategory;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
* かんたん計算式
*/
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QPBMT_BASIC_CAL_FORM")
public class QpbmtBasicCalculationFormula extends UkJpaEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**
    * ID
    */
    @EmbeddedId
    public QpbmtBasicCalculationFormulaPk basicCalFormPk;
    
    /**
    * 固定/計算式/既定区分
    */
    @Basic(optional = false)
    @Column(name = "CALCULATION_FORMULA_CLS")
    public int calculationFormulaCls;
    

    
    /**
    * かんたん計算式固定金額
    */
    @Basic(optional = true)
    @Column(name = "BASIC_CALCULATION_FORMULA")
    public BigDecimal basicCalculationFormula;

    /**
     * 基準金額区分
     */
    @Basic(optional = false)
    @Column(name = "STANDARD_AMOUNT_CLS")
    public int standardAmountCls;

    /**
     * 基準金額固定値
     */
    @Basic(optional = true)
    @Column(name = "STANDARD_FIXED_VALUE")
    public BigDecimal standardFixedValue;

    /**
    * 勤怠項目
    */
    @Basic(optional = true)
    @Column(name = "ATTENDANCE_ITEM")
    public String attendanceItem;
    
    /**
    * 係数区分（固定項目）
    */
    @Basic(optional = true)
    @Column(name = "COEFFICIENT_CLS")
    public Integer coefficientCls;
    
    /**
    * 係数固定値
    */
    @Basic(optional = true)
    @Column(name = "COEFFICIENT_FIXED_VALUE")
    public BigDecimal coefficientFixedValue;
    
    /**
    * 計算式タイプ
    */
    @Basic(optional = true)
    @Column(name = "FORMULA_TYPE")
    public Integer formulaType;
    
    /**
    * 結果端数処理
    */
    @Basic(optional = true)
    @Column(name = "ROUNDING_RESULT")
    public Integer roundingResult;
    
    /**
    * 調整区分
    */
    @Basic(optional = true)
    @Column(name = "ADJUSTMENT_CLS")
    public Integer adjustmentCls;
    
    /**
    * 基底項目区分
    */
    @Basic(optional = true)
    @Column(name = "BASE_ITEM_CLS")
    public Integer baseItemCls;
    
    /**
    * 基底項目固定値
    */
    @Basic(optional = true)
    @Column(name = "BASE_ITEM_FIXED_VALUE")
    public BigDecimal baseItemFixedValue;
    
    /**
    * 割増率
    */
    @Basic(optional = true)
    @Column(name = "PREMIUM_RATE")
    public Integer premiumRate;
    
    /**
    * 式中端数処理
    */
    @Basic(optional = true)
    @Column(name = "ROUNDING_METHOD")
    public Integer roundingMethod;
    
    @Override
    protected Object getKey()
    {
        return basicCalFormPk;
    }

    public static QpbmtBasicCalculationFormula toEntity(BasicCalculationFormula domain) {
        QpbmtBasicCalculationFormula entity = new QpbmtBasicCalculationFormula();
        entity.basicCalFormPk =new QpbmtBasicCalculationFormulaPk(domain.getHistoryID(), domain.getMasterUseCode().v());
        entity.calculationFormulaCls = domain.getCalculationFormulaClassification().value;
        entity.basicCalculationFormula = domain.getBasicCalculationFormula().map(PrimitiveValueBase::v).orElse(null);
        if (!domain.getBasicCalculationForm().isPresent()) return entity;
        BasicCalculationForm basicCalculationForm = domain.getBasicCalculationForm().get();
        entity.standardAmountCls = basicCalculationForm.getBasicCalculationStandardAmount().getStandardAmountClassification().value;
        entity.standardFixedValue = basicCalculationForm.getBasicCalculationStandardAmount().getStandardFixedValue().map(PrimitiveValueBase::v).orElse(null);
        entity.attendanceItem = basicCalculationForm.getBasicCalculationFactorClassification().getCoefficientItem().getAttendanceItem().map(PrimitiveValueBase::v).orElse(null);
        entity.coefficientCls = basicCalculationForm.getBasicCalculationFactorClassification().getCoefficientItem().getCoefficientClassification().map(i -> i.value).orElse(null);
        entity.coefficientFixedValue = basicCalculationForm.getBasicCalculationFactorClassification().getCoefficientFixedValue().map(PrimitiveValueBase::v).orElse(null);
        entity.formulaType = basicCalculationForm.getFormulaType().value;
        entity.roundingResult = basicCalculationForm.getRoundingResult().value;
        entity.adjustmentCls = basicCalculationForm.getAdjustmentClassification().value;
        entity.premiumRate = basicCalculationForm.getPremiumRate().map(PrimitiveValueBase::v).orElse(null);
        entity.roundingMethod = basicCalculationForm.getRoundingMethod().map(i -> i.value).orElse(null);
        if (!basicCalculationForm.getBasicCalculationItemCategory().isPresent()) return entity;
        BasicCalculationItemCategory basicCalculationItemCategory = basicCalculationForm.getBasicCalculationItemCategory().get();
        entity.baseItemCls = basicCalculationItemCategory.getBaseItemClassification().value;
        entity.baseItemFixedValue = basicCalculationItemCategory.getBaseItemFixedValue().map(PrimitiveValueBase::v).orElse(null);
        return entity;
    }

}
