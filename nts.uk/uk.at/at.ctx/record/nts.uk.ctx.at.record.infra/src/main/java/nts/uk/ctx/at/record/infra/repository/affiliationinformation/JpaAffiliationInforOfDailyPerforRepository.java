package nts.uk.ctx.at.record.infra.repository.affiliationinformation;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.AffiliationInforOfDailyPerforRepository;
import nts.uk.ctx.at.record.infra.entity.affiliationinformation.KrcdtDaiAffiliationInf;
import nts.uk.ctx.at.record.infra.entity.affiliationinformation.KrcdtDaiAffiliationInfPK;

@Stateless
public class JpaAffiliationInforOfDailyPerforRepository extends JpaRepository
		implements AffiliationInforOfDailyPerforRepository {

	private static final String REMOVE_BY_EMPLOYEE;
	
	private static final String DEL_BY_LIST_KEY;
	
	private static final String FIND_BY_KEY;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtDaiAffiliationInf a ");
		builderString.append("WHERE a.krcdtDaiAffiliationInfPK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiAffiliationInfPK.ymd = :ymd ");
		REMOVE_BY_EMPLOYEE = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtDaiAffiliationInf a ");
		builderString.append("WHERE a.krcdtDaiAffiliationInfPK.employeeId IN :employeeIds ");
		builderString.append("AND a.krcdtDaiAffiliationInfPK.ymd IN :ymds ");
		DEL_BY_LIST_KEY = builderString.toString();
		
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
	public void deleteByListEmployeeId(List<String> employeeIds, List<GeneralDate> ymds) {
		this.getEntityManager().createQuery(DEL_BY_LIST_KEY).setParameter("employeeIds", employeeIds)
		.setParameter("ymds", ymds).executeUpdate();
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
}
