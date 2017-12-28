package nts.uk.ctx.at.record.infra.repository.calculationsetting;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.calculationsetting.StampReflectionManagement;
import nts.uk.ctx.at.record.dom.calculationsetting.repository.StampReflectionManagementRepository;
import nts.uk.ctx.at.record.infra.entity.calculationsetting.KrcmtStampImprint;
import nts.uk.ctx.at.record.infra.entity.calculationsetting.KrcmtStampImprintPK;


@Stateless
public class JpaStampReflectionManagementRepository extends JpaRepository
		implements StampReflectionManagementRepository {

	private static final String FIND;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtStampReflect a ");
		builderString.append("WHERE a.krcdtStampReflectPK.companyId = :companyId ");
		FIND = builderString.toString();
	}

	@Override
	public Optional<StampReflectionManagement> findByCid(String companyId) {
		return this.queryProxy().query(FIND, KrcmtStampImprint.class).setParameter("companyId", companyId)
				.getSingle(f -> toDomain(f));
	}

	private static StampReflectionManagement toDomain(KrcmtStampImprint krcdtStampReflect) {
		StampReflectionManagement stampReflectionManagement = StampReflectionManagement.createJavaType(
				krcdtStampReflect.krcdtStampReflectPK.companyId, krcdtStampReflect.breakSwitchClass,
				krcdtStampReflect.autoStampReflectionClass,
				krcdtStampReflect.actualStampOfPriorityClass,
				krcdtStampReflect.reflectWorkingTimeClass,
				krcdtStampReflect.goBackOutCorrectionClass,
				krcdtStampReflect.managementOfEntrance,
				krcdtStampReflect.autoStampForFutureDayClass,
				krcdtStampReflect.outingAtr,
				krcdtStampReflect.maxUseCount);
		return stampReflectionManagement;
	}

	@Override
	public void update(StampReflectionManagement reflectionManagement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(StampReflectionManagement reflectionManagement) {
		this.commandProxy().insert(convertToDbType(reflectionManagement));
		
	}
		private KrcmtStampImprint convertToDbType(StampReflectionManagement atten) {
		KrcmtStampImprint newEntity = KrcmtStampImprint.toEntity(atten);
		KrcmtStampImprintPK attSetPK = new KrcmtStampImprintPK(atten.getCompanyId());
		Optional<KrcmtStampImprint> optUpdateEntity = this.queryProxy().find(attSetPK, KrcmtStampImprint.class);
		if (optUpdateEntity.isPresent()) {
			KrcmtStampImprint updateEntity = optUpdateEntity.get();
			updateEntity.breakSwitchClass = atten.getBreakSwitchClass().value;
			updateEntity.autoStampReflectionClass = atten.getAutoStampReflectionClass().value;
			updateEntity.actualStampOfPriorityClass = atten.getActualStampOfPriorityClass().value;
			updateEntity.reflectWorkingTimeClass = atten.getReflectWorkingTimeClass().value;
			updateEntity.goBackOutCorrectionClass = atten.getGoBackOutCorrectionClass().value;
			updateEntity.managementOfEntrance = atten.getManagementOfEntrance().value;
			updateEntity.autoStampForFutureDayClass = atten.getAutoStampForFutureDayClass().value;
			updateEntity.outingAtr = atten.getOutingAtr().value;
			updateEntity.maxUseCount = atten.getMaxUseCount();
			
			updateEntity.krcdtStampReflectPK = attSetPK;
			return updateEntity;
		}
		return  newEntity;
	}
	
	
}
