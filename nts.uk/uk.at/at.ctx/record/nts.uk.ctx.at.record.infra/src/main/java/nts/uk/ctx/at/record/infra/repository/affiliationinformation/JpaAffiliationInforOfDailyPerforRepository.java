package nts.uk.ctx.at.record.infra.repository.affiliationinformation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.AffiliationInforOfDailyPerforRepository;
import nts.uk.ctx.at.record.infra.entity.affiliationinformation.KrcdtDaiAffiliationInf;
import nts.uk.ctx.at.record.infra.entity.affiliationinformation.KrcdtDaiAffiliationInfPK;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class JpaAffiliationInforOfDailyPerforRepository extends JpaRepository
		implements AffiliationInforOfDailyPerforRepository {

	private static final String REMOVE_BY_EMPLOYEE;
	
	private static final String FIND_BY_KEY;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtDaiAffiliationInf a ");
		builderString.append("WHERE a.krcdtDaiAffiliationInfPK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiAffiliationInfPK.ymd = :ymd ");
		REMOVE_BY_EMPLOYEE = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDaiAffiliationInf a ");
		builderString.append("WHERE a.krcdtDaiAffiliationInfPK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiAffiliationInfPK.ymd = :ymd ");
		FIND_BY_KEY = builderString.toString();
	}

	@Override
	public void delete(String employeeId, GeneralDate ymd) {
		this.getEntityManager().createQuery(REMOVE_BY_EMPLOYEE).setParameter("employeeId", employeeId)
				.setParameter("ymd", ymd).executeUpdate();
		this.getEntityManager().flush();
	}

	@Override
	public void add(AffiliationInforOfDailyPerfor affiliationInforOfDailyPerfor) {
		this.commandProxy().insert(toEntity(affiliationInforOfDailyPerfor));
	}

	private KrcdtDaiAffiliationInf toEntity(AffiliationInforOfDailyPerfor affiliationInforOfDailyPerfor) {
		val entity = new KrcdtDaiAffiliationInf();

		entity.krcdtDaiAffiliationInfPK = new KrcdtDaiAffiliationInfPK();
		entity.krcdtDaiAffiliationInfPK.employeeId = affiliationInforOfDailyPerfor.getEmployeeId();
		entity.krcdtDaiAffiliationInfPK.ymd = affiliationInforOfDailyPerfor.getYmd();
		entity.bonusPayCode = affiliationInforOfDailyPerfor.getBonusPaySettingCode() != null ? affiliationInforOfDailyPerfor.getBonusPaySettingCode().v() : null;
		entity.classificationCode = affiliationInforOfDailyPerfor.getClsCode() == null ? null : affiliationInforOfDailyPerfor.getClsCode().v();
		entity.employmentCode = affiliationInforOfDailyPerfor.getEmploymentCode() == null ? null : affiliationInforOfDailyPerfor.getEmploymentCode().v();
		entity.jobtitleID = affiliationInforOfDailyPerfor.getJobTitleID();
		entity.workplaceID = affiliationInforOfDailyPerfor.getWplID();

		return entity;
	}

	@Override
	public Optional<AffiliationInforOfDailyPerfor> findByKey(String employeeId, GeneralDate ymd) {
		return this.queryProxy().query(FIND_BY_KEY, KrcdtDaiAffiliationInf.class).setParameter("employeeId", employeeId)
				.setParameter("ymd", ymd).getSingle(f -> f.toDomain());
	}

	@Override
	public void updateByKey(AffiliationInforOfDailyPerfor domain) {
		Optional<KrcdtDaiAffiliationInf> dataOpt = this.queryProxy().query(FIND_BY_KEY, KrcdtDaiAffiliationInf.class)
				.setParameter("employeeId", domain.getEmployeeId())
				.setParameter("ymd", domain.getYmd()).getSingle();
		KrcdtDaiAffiliationInf data = dataOpt.isPresent() ? dataOpt.get() : new KrcdtDaiAffiliationInf();
		if(!dataOpt.isPresent()){
			data.krcdtDaiAffiliationInfPK = new KrcdtDaiAffiliationInfPK(domain.getEmployeeId(), domain.getYmd());
		}
		data.bonusPayCode = domain.getBonusPaySettingCode() == null ? null : domain.getBonusPaySettingCode().v();
		data.classificationCode = domain.getClsCode() == null ? null : domain.getClsCode().v();
		data.employmentCode = domain.getEmploymentCode() == null ? null : domain.getEmploymentCode().v();
		data.workplaceID = domain.getWplID();
		data.jobtitleID = domain.getJobTitleID();
		this.commandProxy().update(data);
	}

	@Override
	public List<AffiliationInforOfDailyPerfor> finds(List<String> employeeId, DatePeriod ymd) {
		List<AffiliationInforOfDailyPerfor> result = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT af FROM KrcdtDaiAffiliationInf af ");
		query.append("WHERE af.krcdtDaiAffiliationInfPK.employeeId IN :employeeId ");
		query.append("AND af.krcdtDaiAffiliationInfPK.ymd <= :end AND af.krcdtDaiAffiliationInfPK.ymd >= :start");
		TypedQueryWrapper<KrcdtDaiAffiliationInf> tQuery=  this.queryProxy().query(query.toString(), KrcdtDaiAffiliationInf.class);
		CollectionUtil.split(employeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, empIds -> {
			result.addAll(tQuery.setParameter("employeeId", empIds)
								.setParameter("start", ymd.start())
								.setParameter("end", ymd.end()).getList(af -> af.toDomain()));
		});
		return result;
	}

	@Override
	public List<AffiliationInforOfDailyPerfor> finds(Map<String, GeneralDate> param) {
		List<AffiliationInforOfDailyPerfor> result = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT af FROM KrcdtDaiAffiliationInf af ");
		query.append("WHERE af.krcdtDaiAffiliationInfPK.employeeId IN :employeeId ");
		query.append("AND af.krcdtDaiAffiliationInfPK.ymd IN :date");
		TypedQueryWrapper<KrcdtDaiAffiliationInf> tQuery=  this.queryProxy().query(query.toString(), KrcdtDaiAffiliationInf.class);
		CollectionUtil.split(param, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, p -> {
			result.addAll(tQuery.setParameter("employeeId", p.keySet())
								.setParameter("date", new HashSet<>(p.values()))
								.getList().stream()
								.filter(c -> c.krcdtDaiAffiliationInfPK.ymd.equals(p.get(c.krcdtDaiAffiliationInfPK.employeeId)))
								.map(af -> af.toDomain()).collect(Collectors.toList()));
		});
		return result;
	}
}
