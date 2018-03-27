package nts.uk.ctx.at.function.infra.repository.alarm.mailsettings;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.function.dom.alarm.mailsettings.MailSettingAutomatic;
import nts.uk.ctx.at.function.dom.alarm.mailsettings.MailSettingAutomaticRepository;
import nts.uk.ctx.at.function.infra.entity.alarm.mailsettings.KfnmtMailSettingAutomatic;

@Stateless
public class JpaMailSettingAutomaticRepository extends JpaRepository implements MailSettingAutomaticRepository{
	
	private final String FIND_BY_COMPANY = "SELECT a FROM KfnmtMailSettingAutomatic a WHERE a.companyID = :companyId";
	private final String FIND_MAIL_LIST = "SELECT a.mailAddress FROM KfnmtMailSettingList a WHERE a.listMailId = :listMailId";
	private final String DEL_MAIL_LIST = "DELETE FROM KfnmtMailSettingList m "
			+ "WHERE m.listMailId = :listMailId1 "
			+ "OR m.listMailId = :listMailId2 "
			+ "OR m.listMailId = :listMailId3 "
			+ "OR m.listMailId = :listMailId4 ";
	
	@Override
	public Optional<MailSettingAutomatic> findByCompanyId(String companyId) {
		Optional<KfnmtMailSettingAutomatic> m = this.queryProxy().query(FIND_BY_COMPANY, KfnmtMailSettingAutomatic.class)
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
	public void create(MailSettingAutomatic mailSettingAutomatic) {
		this.commandProxy().insert(KfnmtMailSettingAutomatic.toEntity(
				IdentifierUtil.randomUniqueId(), 
				IdentifierUtil.randomUniqueId(), 
				IdentifierUtil.randomUniqueId(), 
				IdentifierUtil.randomUniqueId(), 
				mailSettingAutomatic));
	}

	@Override
	public void update(MailSettingAutomatic mailSettingAutomatic) {
		KfnmtMailSettingAutomatic updateEntity = this.queryProxy().query(FIND_BY_COMPANY, KfnmtMailSettingAutomatic.class)
				.setParameter("companyId", mailSettingAutomatic.getCompanyID())
				.getSingle().get();
		this.getEntityManager().createQuery(DEL_MAIL_LIST)
			   .setParameter("listMailId1", updateEntity.mailAddressCC)
			   .setParameter("listMailId2", updateEntity.mailAddressBCC)
			   .setParameter("listMailId3", updateEntity.adminMailAddressCC)
			   .setParameter("listMailId4", updateEntity.adminMailAddressBCC)
			   .executeUpdate();
		
		KfnmtMailSettingAutomatic newEntity = KfnmtMailSettingAutomatic.toEntity(
				IdentifierUtil.randomUniqueId(),
				IdentifierUtil.randomUniqueId(),
				IdentifierUtil.randomUniqueId(),
				IdentifierUtil.randomUniqueId(),
				mailSettingAutomatic);
		updateEntity.updateEntity(newEntity);
		
		this.commandProxy().update(updateEntity);
	}

}
