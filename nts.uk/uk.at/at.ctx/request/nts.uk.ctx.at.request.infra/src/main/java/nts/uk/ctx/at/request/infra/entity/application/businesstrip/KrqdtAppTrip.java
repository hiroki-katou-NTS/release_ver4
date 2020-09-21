package nts.uk.ctx.at.request.infra.entity.application.businesstrip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="KRQDT_APP_TRIP")
public class KrqdtAppTrip extends UkJpaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    public KrqdtAppTripPK krqdtAppTripPK;

    @Column(name="CONTRACT_CD")
    private String contractCD;

    @Column(name="WORK_TYPE_CD")
    public String workTypeCD;

    @Column(name="WORK_TIME_CD")
    public String workTimeCD;

    @Column(name="WORK_TIME_START")
    public Integer workTimeStart;

    @Column(name="WORK_TIME_END")
    public Integer workTimeEnd;

    @Column(name="START_TIME")
    public Integer startTime;

    @Column(name="ARRIVAL_TIME")
    public Integer arrivalTime;

    @Override
    protected Object getKey() {
        return this.krqdtAppTripPK;
    }

}
