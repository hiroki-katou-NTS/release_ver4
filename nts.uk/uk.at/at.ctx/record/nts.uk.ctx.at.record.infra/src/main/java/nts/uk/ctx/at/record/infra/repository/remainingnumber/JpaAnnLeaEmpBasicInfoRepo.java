package nts.uk.ctx.at.record.infra.repository.remainingnumber;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnLeaEmpBasicInfoRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnualLeaveEmpBasicInfo;
import nts.uk.ctx.at.record.infra.entity.remainingnumber.annlea.KrcmtAnnLeaBasicInfo;

@Stateless
public class JpaAnnLeaEmpBasicInfoRepo extends JpaRepository implements AnnLeaEmpBasicInfoRepository {

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

	@Override
	public void add(AnnualLeaveEmpBasicInfo basicInfo) {
		KrcmtAnnLeaBasicInfo entity = new KrcmtAnnLeaBasicInfo();
		entity.sid = basicInfo.getEmployeeId();
		entity.workDaysPerYear = basicInfo.getWorkingDaysPerYear().v();
		entity.workDaysBeforeIntro = basicInfo.getWorkingDayBeforeIntroduction().v();
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
			ent.workDaysPerYear = basicInfo.getWorkingDaysPerYear().v();
			ent.workDaysBeforeIntro = basicInfo.getWorkingDayBeforeIntroduction().v();
			ent.grantTableCode = basicInfo.getGrantRule().getGrantTableCode().v();
			ent.grantStandardDate = basicInfo.getGrantRule().getGrantStandardDate();
			this.commandProxy().update(ent);
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
