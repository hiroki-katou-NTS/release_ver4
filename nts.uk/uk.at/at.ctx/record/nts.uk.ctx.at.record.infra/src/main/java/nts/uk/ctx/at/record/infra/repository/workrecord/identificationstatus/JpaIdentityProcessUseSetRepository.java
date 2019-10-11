package nts.uk.ctx.at.record.infra.repository.workrecord.identificationstatus;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.enums.SelfConfirmError;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentityProcessUseSetRepository;
import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtIdentityProcess;
import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtIdentityProcessPk;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

@Stateless
public class JpaIdentityProcessUseSetRepository extends JpaRepository implements IdentityProcessUseSetRepository {

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public Optional<IdentityProcessUseSet> findByKey(String companyId) {
		return this.queryProxy().find(new KrcmtIdentityProcessPk(companyId), KrcmtIdentityProcess.class)
				.map(x -> toDomain(x));
	}

	@Override
	public void insertIdentity(IdentityProcessUseSet identity) {
		this.commandProxy().insert(toEntity(identity));
	}

	@Override
	public void updateIdentity(IdentityProcessUseSet identity) {
		this.commandProxy().update(toEntity(identity));
	}

	private IdentityProcessUseSet toDomain(KrcmtIdentityProcess entity) {
		return new IdentityProcessUseSet(new CompanyId(entity.identityProcessPk.cid),
				entity.useDailySelfCk == 1 ? true : false, entity.useMonthSelfCK == 1 ? true : false,
				entity.yourselfConfirmError == null ? Optional.empty()
						: Optional.of(EnumAdaptor.valueOf(entity.yourselfConfirmError, SelfConfirmError.class)));
	}

	private KrcmtIdentityProcess toEntity(IdentityProcessUseSet domain) {
		return new KrcmtIdentityProcess(new KrcmtIdentityProcessPk(domain.getCompanyId().v()),
				domain.isUseConfirmByYourself() ? 1 : 0, domain.isUseIdentityOfMonth() ? 1 : 0,
				domain.getYourSelfConfirmError().isPresent() ? domain.getYourSelfConfirmError().get().value : null);
	}
}
