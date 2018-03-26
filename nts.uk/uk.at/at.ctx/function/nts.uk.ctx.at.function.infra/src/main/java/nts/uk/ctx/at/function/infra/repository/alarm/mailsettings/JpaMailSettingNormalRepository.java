package nts.uk.ctx.at.function.infra.repository.alarm.mailsettings;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.function.dom.alarm.mailsettings.MailSettingNormal;
import nts.uk.ctx.at.function.dom.alarm.mailsettings.MailSettingNormalRepository;
import nts.uk.ctx.at.function.infra.entity.alarm.mailsettings.KfnmtMailSettingNormal;

@Stateless
public class JpaMailSettingNormalRepository extends JpaRepository implements MailSettingNormalRepository {

	private final String FIND_BY_COMPANY = "SELECT a FROM KfnmtMailSettingNormal a WHERE a.companyID = :companyId";
	private final String FIND_MAIL_LIST = "SELECT a.mailAddress FROM KfnmtMailSettingList a WHERE a.listMailId = :listMailId";
	
	@Override
	public Optional<MailSettingNormal> findByCompanyId(String companyId) {
		Optional<KfnmtMailSettingNormal> m = this.queryProxy().query(FIND_BY_COMPANY, KfnmtMailSettingNormal.class)
				.setParameter("companyId", companyId)
				.getSingle();
		if(!m.isPresent())
			return Optional.empty();
		List<String> mailSettingListCC = this.queryProxy().query(FIND_MAIL_LIST, String.class)
				.setParameter("listMailId", m.get().mailAddressCC).getList();
		List<String> mailSettingListBCC = this.queryProxy().query(FIND_MAIL_LIST, String.class)
				.setParameter("listMailId", m.get().mailAddressBCC).getList();
		List<String> mailSettingListAdminCC = this.queryProxy().query(FIND_MAIL_LIST, String.class)
				.setParameter("listMailId", m.get().adminMailAddressCC).getList();
		List<String> mailSettingListAdminBCC = this.queryProxy().query(FIND_MAIL_LIST, String.class)
				.setParameter("listMailId", m.get().adminMailAddressBCC).getList();
		
		
		
		return m.map(c ->c.toDomain(mailSettingListCC, mailSettingListBCC, mailSettingListAdminCC, mailSettingListAdminBCC));
	}

	@Override
	public void create(MailSettingNormal mailSettingNormal) {
		this.commandProxy().insert(KfnmtMailSettingNormal.toEntity(
				IdentifierUtil.randomUniqueId(), 
				IdentifierUtil.randomUniqueId(), 
				IdentifierUtil.randomUniqueId(), 
				IdentifierUtil.randomUniqueId(), 
				mailSettingNormal));
	}

	@Override
	public void update(MailSettingNormal mailSettingNormal) {
		KfnmtMailSettingNormal updateEntity = this.queryProxy().query(FIND_BY_COMPANY, KfnmtMailSettingNormal.class)
				.setParameter("companyId", mailSettingNormal.getCompanyID())
				.getSingle().get();
		
		KfnmtMailSettingNormal newEntity = KfnmtMailSettingNormal.toEntity(
				IdentifierUtil.randomUniqueId(),
				IdentifierUtil.randomUniqueId(),
				IdentifierUtil.randomUniqueId(),
				IdentifierUtil.randomUniqueId(),
				mailSettingNormal);
		updateEntity.updateEntity(newEntity);
		
		this.commandProxy().update(updateEntity);
		
	}

}
