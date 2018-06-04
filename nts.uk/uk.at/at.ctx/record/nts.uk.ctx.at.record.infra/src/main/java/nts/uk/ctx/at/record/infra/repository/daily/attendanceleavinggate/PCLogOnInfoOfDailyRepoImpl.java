package nts.uk.ctx.at.record.infra.repository.daily.attendanceleavinggate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.LogOnInfo;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo.PCLogOnInfoOfDailyRepo;
import nts.uk.ctx.at.record.infra.entity.daily.attendanceleavinggate.KrcdtDayPcLogonInfo;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class PCLogOnInfoOfDailyRepoImpl extends JpaRepository implements PCLogOnInfoOfDailyRepo {

	private static final String REMOVE_BY_KEY;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtDayPcLogonInfo a ");
		builderString.append("WHERE a.id.sid = :employeeId ");
		builderString.append("AND a.id.ymd = :ymd ");
		REMOVE_BY_KEY = builderString.toString();
	}

	@Override
	public Optional<PCLogOnInfoOfDaily> find(String employeeId, GeneralDate baseDate) {
		List<LogOnInfo> logOnInfo = findQuery(employeeId, baseDate).getList(c -> c.toDomain());
		if (!logOnInfo.isEmpty()) {
			return Optional.of(new PCLogOnInfoOfDaily(employeeId, baseDate, logOnInfo));
		}
		return Optional.empty();
	}

	@Override
	public List<PCLogOnInfoOfDaily> find(String employeeId, List<GeneralDate> baseDate) {
		if (baseDate.isEmpty()) {
			return Collections.emptyList();
		}
		return toList(this.queryProxy()
				.query("SELECT al FROM KrcdtDayPcLogonInfo al WHERE al.id.sid = :sid AND al.id.ymd IN :ymd",
						KrcdtDayPcLogonInfo.class)
				.setParameter("ymd", baseDate).setParameter("sid", employeeId).getList().stream());
	}

	@Override
	public List<PCLogOnInfoOfDaily> find(String employeeId) {
		return toList(this.queryProxy()
				.query("SELECT al FROM KrcdtDayPcLogonInfo al WHERE al.id.sid = :sid", KrcdtDayPcLogonInfo.class)
				.setParameter("sid", employeeId).getList().stream());
	}

	@Override
	public List<PCLogOnInfoOfDaily> finds(List<String> employeeId, DatePeriod baseDate) {
		if (employeeId.isEmpty()) {
			return Collections.emptyList();
		}
		List<PCLogOnInfoOfDaily> result = new ArrayList<>();
		String query = "SELECT al FROM KrcdtDayPcLogonInfo al WHERE al.id.sid IN :sid AND al.id.ymd <= :end AND al.id.ymd >= :start";
		TypedQueryWrapper<KrcdtDayPcLogonInfo> tQuery=  this.queryProxy().query(query, KrcdtDayPcLogonInfo.class);
		CollectionUtil.split(employeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, empIds -> {
			result.addAll(toList(tQuery.setParameter("sid", empIds)
										.setParameter("end", baseDate.end())
										.setParameter("start", baseDate.start()).getList().stream()));
		});
		return result;
	}

	@Override
	public List<PCLogOnInfoOfDaily> finds(Map<String, List<GeneralDate>> param) {
		if (param.isEmpty()) {
			return Collections.emptyList();
		}
		List<PCLogOnInfoOfDaily> result = new ArrayList<>();
		String query = "SELECT al FROM KrcdtDayPcLogonInfo al WHERE al.id.sid IN :sid AND al.id.ymd IN :date";
		TypedQueryWrapper<KrcdtDayPcLogonInfo> tQuery=  this.queryProxy().query(query, KrcdtDayPcLogonInfo.class);
		CollectionUtil.split(param, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, p -> {
			result.addAll(toList(tQuery.setParameter("sid", p.keySet())
										.setParameter("date", p.values().stream().flatMap(List::stream).collect(Collectors.toSet()))
										.getList().stream()
										.filter(c -> p.get(c.id.sid).contains(c.id.ymd))
										));
		});
		return result;
	}

	@Override
	public void update(PCLogOnInfoOfDaily domain) {
		List<KrcdtDayPcLogonInfo> entities = findQuery(domain.getEmployeeId(), domain.getYmd()).getList();
		if(entities.isEmpty()) {
			add(domain);
		} else {
			List<Integer> nos = domain.getLogOnInfo().stream().map(c -> c.getWorkNo().v()).collect(Collectors.toList());
			List<KrcdtDayPcLogonInfo> toDelete = entities.stream()
					.filter(c -> !nos.contains(c.id.pcLogNo)).collect(Collectors.toList());
			this.commandProxy().removeAll(toDelete);
			domain.getLogOnInfo().stream().forEach(c -> {
				Optional<KrcdtDayPcLogonInfo> entityOp = entities.stream().filter(e -> e.id.pcLogNo == c.getWorkNo().v())
																		.findFirst();
				if(entityOp.isPresent()) {
					KrcdtDayPcLogonInfo entity = entityOp.get();
					entity.setData(c);
					commandProxy().update(entity);
				} else {
					commandProxy().insert(KrcdtDayPcLogonInfo.from(domain.getEmployeeId(), domain.getYmd(), c));
				}
			});
		}
	}

	@Override
	public void add(PCLogOnInfoOfDaily domain) {
		this.commandProxy().insertAll(KrcdtDayPcLogonInfo.from(domain));
	}

	@Override
	public void remove(PCLogOnInfoOfDaily domain) {
		removeByKey(domain.getEmployeeId(), domain.getYmd());
	}

	@Override
	public void removeByKey(String employeeId, GeneralDate baseDate) {
		this.getEntityManager().createQuery(REMOVE_BY_KEY).setParameter("employeeId", employeeId)
				.setParameter("ymd", baseDate).executeUpdate();
		this.getEntityManager().flush();
	}

	private TypedQueryWrapper<KrcdtDayPcLogonInfo> findQuery(String employeeId, GeneralDate baseDate){
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDayPcLogonInfo a ");
		builderString.append("WHERE a.id.sid = :employeeId ");
		builderString.append("AND a.id.ymd = :ymd ");
		return this.queryProxy().query(builderString.toString(), KrcdtDayPcLogonInfo.class)
				.setParameter("employeeId", employeeId)
				.setParameter("ymd", baseDate);
	}

	private List<PCLogOnInfoOfDaily> toList(Stream<KrcdtDayPcLogonInfo> query) {
		return query.collect(Collectors.groupingBy(c -> c.id.sid + c.id.ymd.toString(), Collectors.toList()))
				.entrySet().stream()
				.map(c -> new PCLogOnInfoOfDaily(c.getValue().get(0).id.sid, c.getValue().get(0).id.ymd,
						c.getValue().stream().map(pc -> pc.toDomain()).collect(Collectors.toList())))
				.collect(Collectors.toList());
	}
	
}
