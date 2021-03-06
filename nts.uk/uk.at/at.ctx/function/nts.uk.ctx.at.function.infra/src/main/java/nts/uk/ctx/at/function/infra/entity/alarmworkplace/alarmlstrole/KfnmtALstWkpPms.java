package nts.uk.ctx.at.function.infra.entity.alarmworkplace.alarmlstrole;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.function.infra.entity.alarmworkplace.alarmpatternworkplace.KfnmtALstWkpPtn;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * entity : アラーム権限設定
 */
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "KFNMT_ALSTWKP_PMS")
public class KfnmtALstWkpPms extends UkJpaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    public KfnmtALstWkpPmsPk pk;

    @Column(name = "CONTRACT_CD")
    public String contractCode;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "CID", referencedColumnName = "CID", insertable = false, updatable = false),
        @JoinColumn(name = "ALARM_PATTERN_CD", referencedColumnName = "ALARM_PATTERN_CD", insertable = false, updatable = false)})
    public KfnmtALstWkpPtn kfnmtALstWkpPtn;

    public KfnmtALstWkpPms(KfnmtALstWkpPmsPk pk, String contractCode) {
        this.pk = pk;
        this.contractCode = contractCode;
    }

    @Override
    protected Object getKey() {
        return this.pk;
    }

    public static KfnmtALstWkpPms toEntity(String roleId, String alarmPatternCode) {

        return new KfnmtALstWkpPms(new KfnmtALstWkpPmsPk(AppContexts.user().companyId(), alarmPatternCode,roleId), AppContexts.user().contractCode());
    }

}
