package nts.uk.ctx.at.record.infra.repository.daily.remark;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.daily.remarks.RemarksOfDailyPerform;
import nts.uk.ctx.at.record.dom.daily.remarks.RemarksOfDailyPerformRepo;
import nts.uk.ctx.at.record.infra.entity.daily.remarks.KrcdtDayRemarksColumn;
import nts.uk.ctx.at.record.infra.entity.daily.remarks.KrcdtDayRemarksColumnPK;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class RemarksOfDailyPerformRepoImpl extends JpaRepository implements RemarksOfDailyPerformRepo {

	@Override
	public List<RemarksOfDailyPerform> getRemarks(String employeeId, GeneralDate workingDate) {
		return findEntity(employeeId, workingDate)
				.getList(c -> c.toDomain());
	}

	@Override
	public List<RemarksOfDailyPerform> getRemarks(List<String> employeeId, DatePeriod baseDate) {
		List<RemarksOfDailyPerform> remarks = new ArrayList<>();
		String query = new StringBuilder("SELECT r FROM KrcdtDayRemarksColumn r")
								.append(" WHERE r.krcdtDayRemarksColumnPK.sid IN :sid")
								.append(" AND r.krcdtDayRemarksColumnPK.ymd >= :start")
								.append(" AND r.krcdtDayRemarksColumnPK.ymd <= :end")
								.toString();
		
		CollectionUtil.split(employeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, sub -> {
			remarks.addAll(queryProxy().query(query, KrcdtDayRemarksColumn.class)
										.setParameter("sid", sub)
										.setParameter("start", baseDate.start())
										.setParameter("end", baseDate.end())
										.getList(c -> c.toDomain()));
		});
		return remarks;
	}

	@Override
	public List<RemarksOfDailyPerform> getRemarks(String employeeId, List<GeneralDate> baseDate) {
		List<RemarksOfDailyPerform> remarks = new ArrayList<>();
		String query = new StringBuilder("SELECT r FROM KrcdtDayRemarksColumn r")
								.append(" WHERE r.krcdtDayRemarksColumnPK.sid = :sid")
								.append(" AND r.krcdtDayRemarksColumnPK.ymd IN ymd")
								.toString();
		
		CollectionUtil.split(baseDate, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, sub -> {
			remarks.addAll(queryProxy().query(query, KrcdtDayRemarksColumn.class)
										.setParameter("sid", sub)
										.setParameter("ymd", baseDate)
										.getList(c -> c.toDomain()));
		});
		return remarks;
	}

	@Override
	public void update(RemarksOfDailyPerform domain) {
		queryProxy().find(new KrcdtDayRemarksColumnPK(domain.getEmployeeId(), domain.getYmd(), domain.getRemarkNo()), 
				KrcdtDayRemarksColumn.class).ifPresent(c -> {
			c.remarks = domain.getRemarks() == null ? null : domain.getRemarks().v();
			commandProxy().update(c);
		});
	}

	@Override
	public void add(RemarksOfDailyPerform domain) {
		commandProxy().insert(KrcdtDayRemarksColumn.toEntity(domain));
	}

	@Override
	public void remove(String employeeId, GeneralDate workingDate) {
		List<KrcdtDayRemarksColumn> entities = findEntity(employeeId, workingDate).getList();
		if(!entities.isEmpty()){
			commandProxy().removeAll(entities);
		}
	}

	@Override
	public void update(List<RemarksOfDailyPerform> domain) {
		if(!domain.isEmpty()){
			domain.stream().forEach(c -> {
				update(c);
			});
		}
	}

	@Override
	public void add(List<RemarksOfDailyPerform> domain) {
		if(!domain.isEmpty()){
			commandProxy().insert(domain.stream().map(c -> KrcdtDayRemarksColumn.toEntity(c)).collect(Collectors.toList()));	
		}
		
	}

	private TypedQueryWrapper<KrcdtDayRemarksColumn> findEntity(String employeeId, GeneralDate workingDate) {
		String query = "SELECT r FROM KrcdtDayRemarksColumn r WHERE r.krcdtDayRemarksColumnPK.sid = :sid AND r.krcdtDayRemarksColumnPK.ymd = :ymd";
		return queryProxy().query(query, KrcdtDayRemarksColumn.class)
				.setParameter("sid", employeeId)
				.setParameter("ymd", workingDate);
	}
}
