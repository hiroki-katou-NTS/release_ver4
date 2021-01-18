package nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.weekly;

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
public class KrcdtWeekCondAlarmPk implements Serializable {
    /* 会社ID */
    @Column(name = "CID")
    public String cid;

    /* ID */
    @Column(name = "ERAL_CHECK_ID")
    public String checkId;

    /* No */
    @Column(name = "COND_NO")
    public int condNo;

}
