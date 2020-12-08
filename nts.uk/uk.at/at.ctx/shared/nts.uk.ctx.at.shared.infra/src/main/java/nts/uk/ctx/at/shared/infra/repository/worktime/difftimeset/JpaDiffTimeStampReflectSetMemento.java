package nts.uk.ctx.at.shared.infra.repository.worktime.difftimeset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import nts.uk.ctx.at.shared.dom.worktime.common.BooleanGetAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.StampReflectTimezone;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeStampReflectSetMemento;
import nts.uk.ctx.at.shared.infra.entity.worktime.difftimeset.KshmtWtDif;
import nts.uk.ctx.at.shared.infra.entity.worktime.difftimeset.KshmtWtDifStmpRefTs;
import nts.uk.ctx.at.shared.infra.entity.worktime.difftimeset.KshmtDtStampReflectPK;

public class JpaDiffTimeStampReflectSetMemento implements DiffTimeStampReflectSetMemento {

	private KshmtWtDif entity;
	
	public JpaDiffTimeStampReflectSetMemento(KshmtWtDif entity) {
		this.entity = entity;
	}

	@Override
	public void setStampReflectTimezone(List<StampReflectTimezone> stampReflectTimezone) {
		// KSHMT_WT_DIF_STMP_REF_TS

		if (this.entity.getLstKshmtDtStampReflect() == null) {
			this.entity.setLstKshmtDtStampReflect(new ArrayList<>());
		}
		// get list old entity
		Map<KshmtDtStampReflectPK, KshmtWtDifStmpRefTs> lstOldEntity = this.entity.getLstKshmtDtStampReflect().stream()
				.collect(Collectors.toMap(KshmtWtDifStmpRefTs::getKshmtDtStampReflectPK, Function.identity()));

		List<KshmtWtDifStmpRefTs> newListEntity = new ArrayList<>();

		stampReflectTimezone.forEach(domain -> {
			KshmtDtStampReflectPK pk = new KshmtDtStampReflectPK(this.entity.getKshmtDiffTimeWorkSetPK().getCid(),
					this.entity.getKshmtDiffTimeWorkSetPK().getWorktimeCd(), domain.getWorkNo().v(), domain.getClassification().value);
			KshmtWtDifStmpRefTs entity = lstOldEntity.get(pk);
			if (entity == null) {
				entity = new KshmtWtDifStmpRefTs();
				entity.setKshmtDtStampReflectPK(pk);
			}
			domain.saveToMemento(new JpaDTStampReflectTimezoneSetMemento(entity));

			// add list
			newListEntity.add(entity);
		});

		this.entity.setLstKshmtDtStampReflect(newListEntity);
	}

	@Override
	public void setIsUpdateStartTime(boolean isUpdateStartTime) {
		this.entity.setUpdStartTime(BooleanGetAtr.getAtrByBoolean(isUpdateStartTime));
	}

}
