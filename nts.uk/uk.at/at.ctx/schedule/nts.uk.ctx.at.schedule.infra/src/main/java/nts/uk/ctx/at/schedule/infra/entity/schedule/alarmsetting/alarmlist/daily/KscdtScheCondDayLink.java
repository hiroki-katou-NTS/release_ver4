package nts.uk.ctx.at.schedule.infra.entity.schedule.alarmsetting.alarmlist.daily;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.schedule.dom.schedule.alarmsetting.alarmlist.daily.ExtractionCondScheduleDay;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

import javax.persistence.*;

/**
 * スケジュール日次のアラームチェック条件
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "KSCDT_SCHE_COND_DAY_LINK")
public class KscdtScheCondDayLink extends ContractUkJpaEntity {

    @Id
    @Column(name = "ERAL_CHECK_ID")
    public String eralCheckId;

    /* チェック条件コード */
    @Column(name = "AL_CHECK_COND_CATE_CD")
    public boolean ctgCd;

    /* カテゴリ */
    @Column(name = "CATEGORY")
    public int ctg;

    @Override
    protected Object getKey() {
        return this.eralCheckId;
    }

    public KscdtScheCondDayLink toDomain(){
        return new KscdtScheCondDayLink();
    }
}
