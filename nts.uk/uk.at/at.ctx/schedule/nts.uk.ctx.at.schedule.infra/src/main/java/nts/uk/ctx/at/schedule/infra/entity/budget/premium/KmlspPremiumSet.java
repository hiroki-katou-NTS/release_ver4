package nts.uk.ctx.at.schedule.infra.entity.budget.premium;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.schedule.dom.budget.premium.PersonCostCalculation;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "KMLST_PREMIUM_SET")
@AllArgsConstructor
@NoArgsConstructor
public class KmlspPremiumSet extends UkJpaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    public KmlspPremiumSetPK pk;
    // 割増設定-割増率
    @Column(name = "PREMIUM_RATE")
    public int premiumRate;

    // 単価 ->人件費計算設定.単価
    @Column(name = "UNIT_PRICE")
    public Integer unitPrice;

    @Override
    protected Object getKey() {
        return pk;
    }

    public static List<KmlspPremiumSet> toEntity(PersonCostCalculation domain, String histId) {
        return domain.getPremiumSettings().stream().map(e ->
                new KmlspPremiumSet(
                        new KmlspPremiumSetPK(domain.getCompanyID(), histId, e.getID().value),
                        e.getRate().v(),
                        e.getUnitPrice().value
                )
        ).collect(Collectors.toList());

    }
}
