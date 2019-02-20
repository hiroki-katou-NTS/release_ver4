package nts.uk.ctx.at.shared.infra.repository.remainingnumber;

import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;

import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnLeaEmpBasicInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnualLeaveEmpBasicInfo;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.annlea.KrcmtAnnLeaBasicInfo;

@Stateless
public class JpaAnnLeaEmpBasicInfoRepo extends JpaRepository implements AnnLeaEmpBasicInfoRepository {
	
	private static final String SELECT_ALL = "SELECT si FROM KrcmtAnnLeaBasicInfo si WHERE si.sid IN : listEmployeeId ";
	@Override
	public Optional<AnnualLeaveEmpBasicInfo> get(String employeeId) {
		Optional<KrcmtAnnLeaBasicInfo> entityOpt = this.queryProxy().find(employeeId, KrcmtAnnLeaBasicInfo.class);
		if (entityOpt.isPresent()) {
			KrcmtAnnLeaBasicInfo ent = entityOpt.get();
			return Optional.of(AnnualLeaveEmpBasicInfo.createFromJavaType(ent.sid, ent.workDaysPerYear,
					ent.workDaysBeforeIntro, ent.grantTableCode, ent.grantStandardDate));
		}
		return Optional.empty();
	}
	
	public List<AnnualLeaveEmpBasicInfo> getAll(List<String> listEmployeeId) {
		return this.queryProxy().query(SELECT_ALL, KrcmtAnnLeaBasicInfo.class)
				.setParameter("listEmployeeId", listEmployeeId)
				.getList(c -> c.toDomain());
	}

	@Override
	public void add(AnnualLeaveEmpBasicInfo basicInfo) {
		KrcmtAnnLeaBasicInfo entity = new KrcmtAnnLeaBasicInfo();
		entity.sid = basicInfo.getEmployeeId();
		entity.cid = basicInfo.getCompanyId();
		entity.workDaysPerYear = basicInfo.getWorkingDaysPerYear().isPresent()
				? basicInfo.getWorkingDaysPerYear().get().v() : null;
		entity.workDaysBeforeIntro = basicInfo.getWorkingDayBeforeIntroduction().isPresent()
				? basicInfo.getWorkingDayBeforeIntroduction().get().v() : null; 
		entity.grantTableCode = basicInfo.getGrantRule().getGrantTableCode().v();
		entity.grantStandardDate = basicInfo.getGrantRule().getGrantStandardDate();
		this.commandProxy().insert(entity);
	}

	@Override
	public void update(AnnualLeaveEmpBasicInfo basicInfo) {
		Optional<KrcmtAnnLeaBasicInfo> entityOpt = this.queryProxy().find(basicInfo.getEmployeeId(),
				KrcmtAnnLeaBasicInfo.class);
		if (entityOpt.isPresent()) {
			KrcmtAnnLeaBasicInfo ent = entityOpt.get();
			ent.workDaysPerYear = basicInfo.getWorkingDaysPerYear().isPresent()
					? basicInfo.getWorkingDaysPerYear().get().v() : null;
			ent.workDaysBeforeIntro = basicInfo.getWorkingDayBeforeIntroduction().isPresent()
					? basicInfo.getWorkingDayBeforeIntroduction().get().v() : null;
			ent.grantTableCode = basicInfo.getGrantRule().getGrantTableCode().v();
			ent.grantStandardDate = basicInfo.getGrantRule().getGrantStandardDate();
			this.commandProxy().update(ent);
		} else {
			add(basicInfo);
		}
	}

	@Override
	public void delete(String employeeId) {
		Optional<KrcmtAnnLeaBasicInfo> entityOpt = this.queryProxy().find(employeeId, KrcmtAnnLeaBasicInfo.class);
		if (entityOpt.isPresent()) {
			KrcmtAnnLeaBasicInfo ent = entityOpt.get();
			this.commandProxy().remove(ent);
		}

	}
}
