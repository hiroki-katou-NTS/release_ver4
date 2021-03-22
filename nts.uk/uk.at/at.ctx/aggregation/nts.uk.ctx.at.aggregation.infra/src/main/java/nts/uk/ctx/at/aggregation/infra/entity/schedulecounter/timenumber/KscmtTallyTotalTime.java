package nts.uk.ctx.at.aggregation.infra.entity.schedulecounter.timenumber;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.val;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.timescounting.TimesNumberCounterSelection;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.timescounting.TimesNumberCounterType;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KAGMT_TALLY_TOTAL_TIMES")
public class KscmtTallyTotalTime extends ContractUkJpaEntity implements Serializable {

	@EmbeddedId
	public KscmtTallyTotalTimePk pk;

	@Override
	protected Object getKey() {
		return this.pk;
	}

	public static List<KscmtTallyTotalTime> toEntity(String companyId, TimesNumberCounterSelection domain) {
		return domain.getSelectedNoList().stream().map(x -> {
			KscmtTallyTotalTimePk pk = new KscmtTallyTotalTimePk(companyId, domain.getType().value, x);
			KscmtTallyTotalTime result = new KscmtTallyTotalTime(pk);

			result.contractCd = AppContexts.user().contractCode();
			return result;
		}).collect(Collectors.toList());
	}

	public static TimesNumberCounterSelection toDomain(List<KscmtTallyTotalTime> entities) {
		if (entities.size() > 0){
			List<Integer> selectedNoList =  entities.stream().map(x ->{
				return x.pk.timeNo;
			}).collect(Collectors.toList());
			val type = entities.stream().findFirst();
			return TimesNumberCounterSelection.create(TimesNumberCounterType.of(type.get().pk.countType),selectedNoList);
		}
		return null;
	}
}
