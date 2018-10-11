package nts.uk.ctx.core.infra.entity.socialinsurance.welfarepensioninsurance;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.core.dom.socialinsurance.welfarepensioninsurance.WelfarePensionStandardMonthlyFee;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 等級毎標準月額
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QPBMT_PEN_STD_GRA_MON")
public class QpbmtWelfarePensionStandardGradePerMonth extends UkJpaEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @EmbeddedId
    public QpbmtWelfarePensionStandardGradePerMonthPk penStdGraMonPk;

    /**
     * 標準月額
     */
    @Basic(optional = false)
    @Column(name = "STANDARD_MONTHLY_FEE")
    public long standardMonthlyFee;

    @Override
    protected Object getKey() {
        return penStdGraMonPk;
    }

    public static List<QpbmtWelfarePensionStandardGradePerMonth> toEntity(WelfarePensionStandardMonthlyFee domain) {
        return domain.getStandardMonthlyPrice().stream().map(x -> new QpbmtWelfarePensionStandardGradePerMonth(
                new QpbmtWelfarePensionStandardGradePerMonthPk(domain.getTargetPeriod().start().v(), domain.getTargetPeriod().end().v(), x.getWelfarePensionGrade()),
                x.getStandardMonthlyFee())).collect(Collectors.toList());
    }
}
