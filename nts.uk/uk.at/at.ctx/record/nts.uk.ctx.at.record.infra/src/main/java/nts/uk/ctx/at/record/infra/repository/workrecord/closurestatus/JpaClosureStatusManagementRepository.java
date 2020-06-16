package nts.uk.ctx.at.record.infra.repository.workrecord.closurestatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.workrecord.closurestatus.ClosureStatusManagement;
import nts.uk.ctx.at.record.dom.workrecord.closurestatus.ClosureStatusManagementRepository;
import nts.uk.ctx.at.record.infra.entity.workrecord.closurestatus.KrcdtClosureSttMng;
import nts.uk.ctx.at.record.infra.entity.workrecord.closurestatus.KrcdtClosureSttMngPk;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author HungTT
 *
 */

@Stateless
public class JpaClosureStatusManagementRepository extends JpaRepository implements ClosureStatusManagementRepository {

	@Override
	public void add(ClosureStatusManagement domain) {
		this.commandProxy().insert(KrcdtClosureSttMng.fromDomain(domain));
	}

	@Override
	public Optional<ClosureStatusManagement> getById(String employeeId, YearMonth ym, int closureId,
			ClosureDate closureDate) {
		Optional<KrcdtClosureSttMng> opt = this.queryProxy().find(new KrcdtClosureSttMngPk(ym.v(), employeeId,
				closureId, closureDate.getClosureDay().v(), closureDate.getLastDayOfMonth() ? 1 : 0),
				KrcdtClosureSttMng.class);
		if (opt.isPresent())
			return Optional.of(opt.get().toDomain());
		return Optional.empty();
	}

	@Override
	public Optional<ClosureStatusManagement> getLatestByEmpId(String employeeId) {
		String sql = "SELECT * FROM KRCDT_CLOSURE_STT_MNG  WHERE SID = @employeeId ORDER BY END_DATE DESC";

		List<KrcdtClosureSttMng> lstEntity = new NtsStatement(sql, this.jdbcProxy())
				.paramString("employeeId", employeeId).getList(rec -> {
					return new KrcdtClosureSttMng(
							new KrcdtClosureSttMngPk(rec.getInt("YM"), rec.getString("SID"), rec.getInt("CLOSURE_ID"),
									rec.getInt("CLOSURE_DAY"), rec.getInt("IS_LAST_DAY")),
							rec.getGeneralDate("START_DATE"), rec.getGeneralDate("END_DATE"));
				});
		if (lstEntity.isEmpty())
			return Optional.empty();
		return Optional.of(lstEntity.get(0).toDomain());
	}
	
	@Override
	public List<ClosureStatusManagement> getByIdListAndDatePeriod(List<String> employeeIds, DatePeriod span){
		
		List<KrcdtClosureSttMng> result = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT a FROM KrcdtClosureSttMng a ");
		query.append("WHERE a.pk.employeeId IN :employeeId ");
		query.append("AND a.start <= :endDate ");
		query.append("AND a.end >= :startDate ");
		TypedQueryWrapper<KrcdtClosureSttMng> tQuery=  this.queryProxy().query(query.toString(), KrcdtClosureSttMng.class);
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, empIds -> {
			result.addAll(tQuery
					.setParameter("employeeId", empIds)
					.setParameter("startDate", span.start())
					.setParameter("endDate", span.end())
					.getList());
		});
		return toDomainFromJoin(result);
	}

	private List<ClosureStatusManagement> toDomainFromJoin(List<KrcdtClosureSttMng> result) {
		return result.stream().map(tc -> tc.toDomain()).collect(Collectors.toList());		
	}

}
