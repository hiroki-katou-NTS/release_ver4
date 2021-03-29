package nts.uk.ctx.at.aggregation.infra.entity.schedulecounter.wkptimezonepeoplenumber;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.timezonepeople.WorkplaceCounterStartTime;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.timezonepeople.WorkplaceCounterTimeZonePeopleNumber;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KAGMT_TALLY_BYWKP_EVERY_TIMEZONE")
public class KscmtTallyByWkpEveryTimeZone extends ContractUkJpaEntity implements Serializable {

	/** 会社ID */
	@EmbeddedId
	public KscmtTallyByWkpEveryTimeZonePk pk;

	@Override
	protected Object getKey() {
		return this.pk;
	}

	public static List<KscmtTallyByWkpEveryTimeZone> toEntity(String companyId, WorkplaceCounterTimeZonePeopleNumber domain) {
		return domain.getTimeZoneList().stream().map(x -> {
			KscmtTallyByWkpEveryTimeZonePk pk = new KscmtTallyByWkpEveryTimeZonePk(companyId,x.v());
			KscmtTallyByWkpEveryTimeZone result = new KscmtTallyByWkpEveryTimeZone(pk);
			result.contractCd = AppContexts.user().contractCode();
			return result;
		}).collect(Collectors.toList());
	}

	public static WorkplaceCounterTimeZonePeopleNumber toDomain(List<KscmtTallyByWkpEveryTimeZone> entities) {
		List<WorkplaceCounterStartTime> workplaceCounterStartTimes =  entities.stream().map(x ->{
			return new WorkplaceCounterStartTime(x.pk.startClock);
		}).collect(Collectors.toList());

		return new WorkplaceCounterTimeZonePeopleNumber(workplaceCounterStartTimes);
	}
}
