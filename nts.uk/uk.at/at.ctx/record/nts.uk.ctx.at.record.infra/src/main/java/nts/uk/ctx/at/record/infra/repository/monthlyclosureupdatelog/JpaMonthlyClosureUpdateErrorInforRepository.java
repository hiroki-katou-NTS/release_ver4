package nts.uk.ctx.at.record.infra.repository.monthlyclosureupdatelog;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.monthlyclosureupdatelog.MonthlyClosureUpdateErrorInfor;
import nts.uk.ctx.at.record.dom.monthlyclosureupdatelog.MonthlyClosureUpdateErrorInforRepository;
import nts.uk.ctx.at.record.infra.entity.monthlyclosureupdatelog.KrcdtMcloseUpdErrInf;
import nts.uk.ctx.at.record.infra.entity.monthlyclosureupdatelog.KrcdtMcloseUpdErrInfPk;

/**
 * 
 * @author HungTT
 *
 */

@Stateless
public class JpaMonthlyClosureUpdateErrorInforRepository extends JpaRepository
		implements MonthlyClosureUpdateErrorInforRepository {

	@Override
	public void add(MonthlyClosureUpdateErrorInfor domain) {
		this.commandProxy().insert(KrcdtMcloseUpdErrInf.fromDomain(domain));
	}

	@Override
	public List<MonthlyClosureUpdateErrorInfor> getAll(String monthlyClosureUpdateLogId) {
		String sql = "SELECT e FROM KrcdtMcloseUpdErrInf e WHERE e.pk.monthlyClosureUpdateLogId = :logId";
		return this.queryProxy().query(sql, KrcdtMcloseUpdErrInf.class).setParameter("logId", monthlyClosureUpdateLogId)
				.getList(c -> c.toDomain());
	}

	@Override
	public List<MonthlyClosureUpdateErrorInfor> getByLogIdAndEmpId(String monthlyClosureUpdateLogId,
			String employeeId) {
		String sql = "SELECT e FROM KrcdtMcloseUpdErrInf e WHERE e.pk.monthlyClosureUpdateLogId = :logId AND e.pk.employeeId = :employeeId";
		return this.queryProxy().query(sql, KrcdtMcloseUpdErrInf.class).setParameter("logId", monthlyClosureUpdateLogId)
				.setParameter("employeeId", employeeId).getList(c -> c.toDomain());
	}

	@Override
	public Optional<MonthlyClosureUpdateErrorInfor> getById(String monthlyClosureUpdateLogId, String employeeId,
			GeneralDate actualClosureEndDate, String resourceId) {
		Optional<KrcdtMcloseUpdErrInf> optEntity = this.queryProxy().find(
				new KrcdtMcloseUpdErrInfPk(employeeId, monthlyClosureUpdateLogId, actualClosureEndDate, resourceId),
				KrcdtMcloseUpdErrInf.class);
		if (optEntity.isPresent())
			return Optional.of(optEntity.get().toDomain());
		else
			return Optional.empty();
	}

}
