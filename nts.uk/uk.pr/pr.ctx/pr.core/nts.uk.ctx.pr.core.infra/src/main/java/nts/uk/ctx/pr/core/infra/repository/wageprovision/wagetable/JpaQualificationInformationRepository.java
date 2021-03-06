package nts.uk.ctx.pr.core.infra.repository.wageprovision.wagetable;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.wageprovision.wagetable.QualificationInformation;
import nts.uk.ctx.pr.core.dom.wageprovision.wagetable.QualificationInformationRepository;
import nts.uk.ctx.pr.core.infra.entity.wageprovision.wagetable.QpbmtQualificationInformation;
import nts.uk.ctx.pr.core.infra.entity.wageprovision.wagetable.QpbmtQualificationInformationPk;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class JpaQualificationInformationRepository extends JpaRepository implements QualificationInformationRepository {

	private static final String FIND_BY_COMPANY = "SELECT a FROM QpbmtQualificationInformation a WHERE a.pk.cid =:cid";

	@Override
	public List<QualificationInformation> getQualificationGroupSettingByCompanyID() {
		return this.queryProxy().query(FIND_BY_COMPANY, QpbmtQualificationInformation.class)
				.setParameter("cid", AppContexts.user().companyId()).getList(item -> item.toDomain());
	}

	@Override
	public Optional<QualificationInformation> getQualificationGroupSettingById(String qualificationCode) {
		return this.queryProxy()
				.find(new QpbmtQualificationInformationPk(AppContexts.user().companyId(), qualificationCode),
						QpbmtQualificationInformation.class)
				.map(item -> item.toDomain());
	}

	@Override
	public void add(QualificationInformation domain) {
		this.commandProxy().insert(QpbmtQualificationInformation.toEntity(domain));
	}

	@Override
	public void update(QualificationInformation domain) {
		this.commandProxy().update(QpbmtQualificationInformation.toEntity(domain));
	}

	@Override
	public void remove(QualificationInformation domain) {
		this.commandProxy().remove(QpbmtQualificationInformation.toEntity(domain));
	}
}
