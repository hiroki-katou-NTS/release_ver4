package nts.uk.ctx.pr.core.infra.entity.wageprovision.unitpricename;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.pr.core.dom.wageprovision.unitpricename.SalaryPerUnitPrice;
import nts.uk.ctx.pr.core.dom.wageprovision.unitpricename.SalaryPerUnitPriceName;
import nts.uk.ctx.pr.core.dom.wageprovision.unitpricename.SalaryPerUnitPriceSetting;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
* 給与個人単価
*/
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QPBMT_PER_UNIT_PRICE")
public class QpbmtPerUnitPrice extends UkJpaEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**
    * ID
    */
    @EmbeddedId
    public QpbmtPerUnitPricePk perUnitPricePk;
    
    /**
    * 名称
    */
    @Basic(optional = false)
    @Column(name = "INDIVIDUAL_UNIT_PRICE_NAME")
    public String name;
    
    /**
    * 廃止区分
    */
    @Basic(optional = false)
    @Column(name = "DEPRECATED_ATR")
    public int abolition;
    
    /**
    * 略名
    */
    @Basic(optional = false)
    @Column(name = "INDIVIDUAL_UNIT_PRICE_ABNAME")
    public String shortName;
    
    /**
    * 統合コード
    */
    @Basic(optional = true)
    @Column(name = "INTERGRATE_CD")
    public String integrationCode;
    
    /**
    * メモ
    */
    @Basic(optional = true)
    @Column(name = "NOTE")
    public String note;
    
    /**
    * 単価種類
    */
    @Basic(optional = false)
    @Column(name = "UNIT_PRICE_TYPE")
    public int unitPriceType;
    
    /**
    * 設定区分
    */
    @Basic(optional = false)
    @Column(name = "SETTING_ATR")
    public int settingAtr;
    
    /**
    * 対象区分
    */
    @Basic(optional = false)
    @Column(name = "TARGET_ATR")
    public int targetClassification;
    
    /**
    * 月給者
    */
    @Basic(optional = false)
    @Column(name = "TARGET_ATR_MONTHLYPAY")
    public int monthlySalary;
    
    /**
    * 日給月給者
    */
    @Basic(optional = false)
    @Column(name = "TARGET_ATR_MONTHVARIABLEPAY")
    public int monthlySalaryPerday;
    
    /**
    * 日給者
    */
    @Basic(optional = false)
    @Column(name = "TARGET_ATR_DAYPAY")
    public int dayPayee;
    
    /**
    * 時給者
    */
    @Basic(optional = false)
    @Column(name = "TARGET_ATR_HOURLYPAY")
    public int hourlyPay;
    
    @Override
    protected Object getKey()
    {
        return perUnitPricePk;
    }

    public SalaryPerUnitPrice toDomain() {
        SalaryPerUnitPriceName name = new SalaryPerUnitPriceName(this.perUnitPricePk.cid, this.perUnitPricePk.code, this.name, this.abolition, this.shortName, this.integrationCode, this.note);
        SalaryPerUnitPriceSetting setting = new SalaryPerUnitPriceSetting(this.perUnitPricePk.cid, this.perUnitPricePk.code, this.unitPriceType, this.settingAtr, this.targetClassification, this.monthlySalary, this.monthlySalaryPerday, this.dayPayee, this.hourlyPay);
        return new SalaryPerUnitPrice(name, setting);
    }
    public QpbmtPerUnitPrice (SalaryPerUnitPrice domain) {
        SalaryPerUnitPriceName name = domain.getSalaryPerUnitPriceName();
        SalaryPerUnitPriceSetting setting = domain.getSalaryPerUnitPriceSetting();

        this.perUnitPricePk = new QpbmtPerUnitPricePk(name.getCid(), name.getCode().v());
        this.name = name.getName().v();
        this.abolition = name.getAbolition().value;
        this.shortName = name.getShortName().v();
        this.integrationCode = name.getIntegrationCode().map(i->i.v()).orElse(null);
        this.note = name.getNote().map(i->i.v()).orElse(null);

        this.unitPriceType = setting.getUnitPriceType().value;
        this.settingAtr = setting.getFixedWage().getSettingAtr().value;
        this.targetClassification = setting.getFixedWage().getEveryoneEqualSet().map(x -> Integer.valueOf(x.getTargetClassification().value)).orElse(0);
        this.monthlySalary = setting.getFixedWage().getPerSalaryContractType().map(x -> Integer.valueOf(x.getMonthlySalary().value)).orElse(0);
        this.monthlySalaryPerday = setting.getFixedWage().getPerSalaryContractType().map(x -> Integer.valueOf(x.getMonthlySalaryPerday().value)).orElse(0);
        this.dayPayee = setting.getFixedWage().getPerSalaryContractType().map(x -> Integer.valueOf(x.getDayPayee().value)).orElse(0);
        this.hourlyPay = setting.getFixedWage().getPerSalaryContractType().map(x -> Integer.valueOf(x.getHourlyPay().value)).orElse(0);
    }

}
