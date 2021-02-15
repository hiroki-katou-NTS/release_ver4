package nts.uk.ctx.at.record.infra.repository.workrecord.erroralarm.multimonth;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.multimonth.MulMonCheckCondAvgRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.multimonth.MulMonthCheckCondAverage;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.multimonth.KrcmtMulMonCondAvg;
@Stateless
public class JpaMulMonCheckCondAvgRepository extends JpaRepository implements MulMonCheckCondAvgRepository {

	private static final String SELECT_COND_BY_ID = " SELECT c FROM KrcmtMulMonCondAvg c"
			+ " WHERE c.errorAlarmCheckID = :errorAlarmCheckID ";

	@Override
	public Optional<MulMonthCheckCondAverage> getMulMonthCheckCondAvgById(String errorAlarmCheckID) {
		Optional<MulMonthCheckCondAverage> data = this.queryProxy().query(SELECT_COND_BY_ID,KrcmtMulMonCondAvg.class)
				.setParameter("errorAlarmCheckID", errorAlarmCheckID)
				.getSingle(c->c.toDomain());
		return data;
	}

	@Override
	public void addMulMonthCheckCondAvg(MulMonthCheckCondAverage mulMonthCheckCondAverage) {
		this.commandProxy().insert(KrcmtMulMonCondAvg.toEntity(mulMonthCheckCondAverage));
		this.getEntityManager().flush();
	}

	@Override
	public void updateMulMonthCheckCondAvg(MulMonthCheckCondAverage mulMonthCheckCondAverage) {
		KrcmtMulMonCondAvg newEntity = KrcmtMulMonCondAvg.toEntity(mulMonthCheckCondAverage);
		KrcmtMulMonCondAvg updateEntity = this.queryProxy().find(
				mulMonthCheckCondAverage.getErrorAlarmCheckID(), KrcmtMulMonCondAvg.class).get();
		updateEntity.isUseFlg = newEntity.isUseFlg;
		updateEntity.krcmtErAlAtdItemCon = newEntity.krcmtErAlAtdItemCon;
		this.commandProxy().update(updateEntity);
		this.getEntityManager().flush();
		
	}

	@Override
	public void deleteMulMonthCheckCondAvg(String errorAlarmCheckID) {
		KrcmtMulMonCondAvg newEntity = this.queryProxy().query(SELECT_COND_BY_ID,KrcmtMulMonCondAvg.class)
				.setParameter("errorAlarmCheckID", errorAlarmCheckID)
				.getSingle().get();
		this.commandProxy().remove(newEntity);
		this.getEntityManager().flush();
	}
}
