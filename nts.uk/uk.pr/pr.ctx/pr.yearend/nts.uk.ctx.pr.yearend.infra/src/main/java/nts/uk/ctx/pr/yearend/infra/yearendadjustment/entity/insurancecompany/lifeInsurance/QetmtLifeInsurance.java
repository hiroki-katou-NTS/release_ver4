package nts.uk.ctx.pr.yearend.infra.yearendadjustment.entity.insurancecompany.lifeInsurance;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.pr.yearend.dom.yearendadjustment.insurancecompany.earthquakeInsurance.EarthquakeInsurance;
import nts.uk.ctx.pr.yearend.dom.yearendadjustment.insurancecompany.lifeInsurance.LifeInsurance;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
* 生命保険情報
*/
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QETMT_INSURANCE_LIFE")
public class QetmtLifeInsurance extends UkJpaEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**
    * ID
    */
    @EmbeddedId
    public QetmtLifeInsurancePk lifeInsurancePk;
    
    /**
    * 名称
    */
    @Basic(optional = false)
    @Column(name = "INSURANCE_LIFE_NAME")
    public String lifeInsuranceName;
    
    @Override
    protected Object getKey()
    {
        return lifeInsurancePk;
    }

    public LifeInsurance toDomain() {
        return new LifeInsurance(this.lifeInsurancePk.cid, this.lifeInsurancePk.lifeInsuranceCode, this.lifeInsuranceName);
    }
    public static QetmtLifeInsurance toEntity(LifeInsurance domain) {
        return new QetmtLifeInsurance(new QetmtLifeInsurancePk(domain.getCId(), domain.getLifeInsuranceCode().v()),domain.getLifeInsuranceName().v());
    }

    public static QetmtLifeInsurance toEarthquakeEntity(EarthquakeInsurance domain) {
        return new QetmtLifeInsurance(new QetmtLifeInsurancePk(domain.getCId(), domain.getEarthquakeInsuranceCode().v()),domain.getEarthquakeInsuranceName().v());
    }

}
