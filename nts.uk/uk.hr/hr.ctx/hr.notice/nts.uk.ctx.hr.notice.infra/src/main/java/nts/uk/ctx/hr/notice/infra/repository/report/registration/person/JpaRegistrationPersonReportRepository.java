/**
 * 
 */
package nts.uk.ctx.hr.notice.infra.repository.report.registration.person;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.hr.notice.dom.report.registration.person.RegistrationPersonReport;
import nts.uk.ctx.hr.notice.dom.report.registration.person.RegistrationPersonReportRepository;
import nts.uk.ctx.hr.notice.infra.entity.report.registration.person.JhndtReportRegis;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author laitv
 *
 */
@Stateless
public class JpaRegistrationPersonReportRepository extends JpaRepository implements RegistrationPersonReportRepository {

	private static final String getListReportBySId = "select c FROM  JhndtReportRegis c "
			+ "Where c.pk.cid = :cid "
			+ "and c.inputSid = :sid "
			+ "ORDER BY c.reportName ASC";
	
	private static final String getListReportSaveDraft = "select c FROM  JhndtReportRegis c "
			+ " Where c.pk.cid = :cid "
			+ " and c.inputSid = :sid and c.appSid = :sid "
			+ " and c.regStatus = 1 and c.delFlg = 0 ORDER BY c.reportName ASC ";
	private static final String getDomainDetail = "select c FROM  JhndtReportRegis c Where c.pk.cid = :cid and c.reportLayoutID = :reportLayoutID ";
	private static final String getDomainByReportId = "select c FROM  JhndtReportRegis c Where c.pk.cid = :cid and c.pk.reportId = :reportId ";
	private static final String GET_MAX_REPORT_ID = "SELECT MAX(a.pk.reportId) FROM JhndtReportRegis a WHERE a.pk.cid = :cid and a.inputSid = :sid";
	

	
	private RegistrationPersonReport toDomain(JhndtReportRegis entity) {
		return entity.toDomain();
	}
	
	private JhndtReportRegis toEntity(RegistrationPersonReport domain) {
		return JhndtReportRegis.toEntity(domain);
	}
	
	@Override
	public int getMaxReportId(String sid , String cid) {
		 Object max = this.queryProxy().query(GET_MAX_REPORT_ID, Object.class)
					.setParameter("cid", cid)
					.setParameter("sid", sid).getSingleOrNull();
		 if(max.equals(null)) {
			 return 0;
		 }else {
			 return (int)max;
		 }
	 }
	
	@Override
	public List<RegistrationPersonReport> getListBySIds(String sid) {
		String cid =  AppContexts.user().companyId();
		return this.queryProxy().query(getListReportBySId, JhndtReportRegis.class)
				.setParameter("cid", cid)
				.setParameter("sid", sid).getList(c -> toDomain(c));
	}

	@Override
	public List<RegistrationPersonReport> getListReportSaveDraft(String sid) {
		String cid =  AppContexts.user().companyId();
		return this.queryProxy().query(getListReportSaveDraft, JhndtReportRegis.class)
				.setParameter("cid", cid)
				.setParameter("sid", sid).getList(c -> toDomain(c));
	}

	@Override
	public Optional<RegistrationPersonReport> getDomain(String cid, Integer reportLayoutID) {
		if (reportLayoutID == null) {
			return Optional.empty();
		}
		
		Optional<JhndtReportRegis> entityOpt = this.queryProxy().query(getDomainDetail, JhndtReportRegis.class)
				.setParameter("cid", cid)
				.setParameter("reportLayoutID", reportLayoutID).getSingle();
		
		if (!entityOpt.isPresent()) {
			return Optional.empty();
		} else {
			return Optional.of(toDomain(entityOpt.get()));
		}
	}
	
	@Override
	public Optional<RegistrationPersonReport> getDomainByReportId(String cid, Integer reportId) {
		if (reportId == null) {
			return Optional.empty();
		}
		
		Optional<JhndtReportRegis> entityOpt = this.queryProxy().query(getDomainByReportId, JhndtReportRegis.class)
				.setParameter("cid", cid)
				.setParameter("reportId", reportId).getSingle();
		
		if (!entityOpt.isPresent()) {
			return Optional.empty();
		} else {
			return Optional.of(toDomain(entityOpt.get()));
		}
	}
	
	@Override
	public void add(RegistrationPersonReport domain) {
		this.commandProxy().insert(toEntity(domain));
	}

	@Override
	public void update(RegistrationPersonReport domain) {
		Optional<JhndtReportRegis> entityOpt = this.queryProxy().query(getDomainByReportId, JhndtReportRegis.class)
				.setParameter("cid", domain.getCid())
				.setParameter("reportId", domain.getReportID()).getSingle();
		if (entityOpt.isPresent()) {
			JhndtReportRegis entity = entityOpt.get();
			updateEntity(entity, domain);
			this.commandProxy().update(entity);
		}
	}

	private void updateEntity(JhndtReportRegis entity, RegistrationPersonReport domain) {
		entity.regStatus = domain.getRegStatus().value;
		entity.draftSaveDate =domain.getDraftSaveDate();
		entity.missingDocName = domain.getMissingDocName();
		entity.inputPid = domain.getInputPid();
		entity.inputSid = domain.getInputSid();
		entity.inputScd = domain.getInputScd();
		entity.inputBussinessName = domain.getInputBussinessName();
		entity.inputDate = domain.getInputDate();
		entity.appPid = domain.getAppPid();
		entity.appSid = domain.getAppSid();
		entity.appScd = domain.getAppScd();
		entity.appBussinessName = domain.getAppBussinessName();
		entity.appDate = domain.getAppDate();
		entity.appDevId =domain.getAppDevId();
		entity.appDevCd = domain.getAppDevCd();
		entity.appDevName = domain.getAppDevName();
		entity.appPosId = domain.getAppPosId();
		entity.appPosCd = domain.getAppPosCd();
		entity.appPosName = domain.getAppPosName();
		entity.reportType  = domain.getReportType().value;
		entity.sendBackSID  = domain.getSendBackSID();
		entity.sendBackComment = domain.getSendBackComment();
	}

	@Override
	public void remove(String cid, Integer reportId) {
		if (reportId == null) {
			return;
		}
		
		Optional<JhndtReportRegis> entityOpt = this.queryProxy().query(getDomainByReportId, JhndtReportRegis.class)
				.setParameter("cid", cid)
				.setParameter("reportId", reportId).getSingle();
		JhndtReportRegis entity = entityOpt.get();
		entity.setDelFlg(1);
		//entity.setDraftSaveDate(GeneralDateTime.now());
		this.commandProxy().update(entity);
	}

}
