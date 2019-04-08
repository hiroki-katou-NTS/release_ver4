package nts.uk.ctx.at.record.infra.repository.daily.remark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
		List<KrcdtDayRemarksColumn> remarks = new ArrayList<>();
		String query = new StringBuilder("SELECT r FROM KrcdtDayRemarksColumn r")
								.append(" WHERE r.krcdtDayRemarksColumnPK.sid IN :sid")
								.append(" AND r.krcdtDayRemarksColumnPK.ymd >= :start")
								.append(" AND r.krcdtDayRemarksColumnPK.ymd <= :end")
								.toString();
		TypedQueryWrapper<KrcdtDayRemarksColumn> tpQuery = queryProxy().query(query, KrcdtDayRemarksColumn.class)
																		.setParameter("start", baseDate.start())
																		.setParameter("end", baseDate.end());
		CollectionUtil.split(employeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, sub -> {
			remarks.addAll(tpQuery.setParameter("sid", sub).getList());
		});
		return remarks.stream().map(c -> c.toDomain()).collect(Collectors.toList());
	}

	@Override
	public List<RemarksOfDailyPerform> getRemarks(Map<String, List<GeneralDate>> param) {
		List<KrcdtDayRemarksColumn> remarks = new ArrayList<>();
		String query = new StringBuilder("SELECT r FROM KrcdtDayRemarksColumn r")
								.append(" WHERE r.krcdtDayRemarksColumnPK.sid IN :sid")
								.append(" AND r.krcdtDayRemarksColumnPK.ymd IN :date")
								.toString();
		TypedQueryWrapper<KrcdtDayRemarksColumn> tpQuery = queryProxy().query(query, KrcdtDayRemarksColumn.class);
		CollectionUtil.split(param, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, p -> {
			remarks.addAll(tpQuery.setParameter("sid", p.keySet())
					.setParameter("date", p.values().stream().flatMap(List::stream).collect(Collectors.toSet()))
					.getList().stream()
					.filter(c -> p.get(c.krcdtDayRemarksColumnPK.sid).contains(c.krcdtDayRemarksColumnPK.ymd))
					.collect(Collectors.toList()));
		});
		return remarks.stream().map(c -> c.toDomain()).collect(Collectors.toList());
	}

	@Override
	public List<RemarksOfDailyPerform> getRemarks(String employeeId, List<GeneralDate> baseDate) {
		List<KrcdtDayRemarksColumn> remarks = new ArrayList<>();
		String query = new StringBuilder("SELECT r FROM KrcdtDayRemarksColumn r")
								.append(" WHERE r.krcdtDayRemarksColumnPK.sid = :sid")
								.append(" AND r.krcdtDayRemarksColumnPK.ymd IN ymd")
								.toString();
		TypedQueryWrapper<KrcdtDayRemarksColumn> tpQuery = queryProxy().query(query, KrcdtDayRemarksColumn.class)
				.setParameter("sid", employeeId);
		CollectionUtil.split(baseDate, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, sub -> {
			remarks.addAll(tpQuery.setParameter("ymd", sub).getList());
		});
		return remarks.stream().map(c -> c.toDomain()).collect(Collectors.toList());
	}

	@Override
	public void update(RemarksOfDailyPerform domain) {
		Optional<KrcdtDayRemarksColumn> remarks = queryProxy().find(new KrcdtDayRemarksColumnPK(domain.getEmployeeId(), domain.getYmd(), domain.getRemarkNo()), 
				KrcdtDayRemarksColumn.class);
		if(remarks.isPresent()){
			KrcdtDayRemarksColumn c = remarks.get();
			c.remarks = domain.getRemarks() == null ? null : domain.getRemarks().v();
			commandProxy().update(c);
		} else {
			add(domain);
		}
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

	@Override
	public Optional<RemarksOfDailyPerform> getByKeys(String sid, GeneralDate ymd, int columnNo) {
		KrcdtDayRemarksColumnPK key = new KrcdtDayRemarksColumnPK(sid, ymd, columnNo);
		Optional<KrcdtDayRemarksColumn> optData = queryProxy().find(key, KrcdtDayRemarksColumn.class);
		if(optData.isPresent()) {
			return Optional.of(optData.get().toDomain());
		} 
		return Optional.empty();
	}
}
