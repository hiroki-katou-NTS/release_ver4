package nts.uk.ctx.at.schedule.infra.entity.schedule.alarmsetting.alarmlist.daily;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class KscdtScheCondDayWtimePk implements Serializable {
    /* 会社ID */
    @Column(name = "CID")
    public String cid;

    /* ID */
    @Column(name = "ERAL_CHECK_ID")
    public String checkId;

    /* NO */
    @Column(name = "COND_NO")
    public int No;

    /* NO */
    @Column(name = "WORKTIME_CODE")
    public String wrkTimeCd;

}
