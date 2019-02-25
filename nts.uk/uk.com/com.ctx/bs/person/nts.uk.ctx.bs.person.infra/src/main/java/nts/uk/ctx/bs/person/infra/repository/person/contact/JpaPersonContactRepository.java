package nts.uk.ctx.bs.person.infra.repository.person.contact;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.person.dom.person.contact.EmergencyContact;
import nts.uk.ctx.bs.person.dom.person.contact.PersonContact;
import nts.uk.ctx.bs.person.dom.person.contact.PersonContactRepository;
import nts.uk.ctx.bs.person.infra.entity.person.contact.BpsmtPersonContact;
import nts.uk.ctx.bs.person.infra.entity.person.contact.BpsmtPersonContactPK;

@Stateless
public class JpaPersonContactRepository extends JpaRepository implements PersonContactRepository {

	private static final String GET_BY_LIST = "SELECT pc FROM BpsmtPersonContact pc WHERE pc.bpsmtPersonContactPK.pid IN :personIdList";

	@Override
	public void add(PersonContact domain) {
		this.commandProxy().insert(toEntity(domain));
	}

	@Override
	public void update(PersonContact domain) {
		BpsmtPersonContactPK key = new BpsmtPersonContactPK(domain.getPersonId());
		Optional<BpsmtPersonContact> entity = this.queryProxy().find(key, BpsmtPersonContact.class);

		if (entity.isPresent()) {
			updateEntity(domain, entity.get());
			this.commandProxy().update(entity.get());
		}
	}

	@Override
	public void delete(String pID) {
		BpsmtPersonContactPK key = new BpsmtPersonContactPK(pID);
		this.commandProxy().remove(BpsmtPersonContact.class, key);
	}

	private PersonContact toDomain(BpsmtPersonContact entity) {
		return new PersonContact(entity.bpsmtPersonContactPK.pid, entity.cellPhoneNumber, entity.mailAdress,
				entity.mobileMailAdress, entity.memo1, entity.contactName1, entity.phoneNo1, entity.memo2,
				entity.contactName2, entity.phoneNo2);
	}

	/**
	 * Convert domain to entity
	 * 
	 * @param domain
	 * @return
	 */
	private BpsmtPersonContact toEntity(PersonContact domain) {
		BpsmtPersonContactPK key = new BpsmtPersonContactPK(domain.getPersonId());
		BpsmtPersonContact entity = new BpsmtPersonContact();
		entity.bpsmtPersonContactPK = key;
		entity.cellPhoneNumber = domain.getCellPhoneNumber().isPresent() ? domain.getCellPhoneNumber().get().v() : null;
		entity.mailAdress = domain.getMailAdress().isPresent() ? domain.getMailAdress().get().v() : null;
		entity.mobileMailAdress = domain.getMobileMailAdress().isPresent() ? domain.getMobileMailAdress().get().v()
				: null;
		if (domain.getEmergencyContact1().isPresent()) {
			EmergencyContact emergencyContact1 = domain.getEmergencyContact1().get();
			entity.memo1 = emergencyContact1.getMemo().map(i->i.v()).orElse(null);
			entity.contactName1 = emergencyContact1.getContactName().map(i->i.v()).orElse(null);
			entity.phoneNo1 = emergencyContact1.getPhoneNumber().map(i->i.v()).orElse(null);
		}

		if (domain.getEmergencyContact2().isPresent()) {
			EmergencyContact emergencyContact2 = domain.getEmergencyContact2().get();
			entity.memo2 = emergencyContact2.getMemo().map(i->i.v()).orElse(null);
			entity.contactName2 = emergencyContact2.getContactName().map(i->i.v()).orElse(null);
			entity.phoneNo2 = emergencyContact2.getPhoneNumber().map(i->i.v()).orElse(null);
		}
		return entity;
	}

	/**
	 * Update entity
	 * 
	 * @param domain
	 * @param entity
	 */
	private void updateEntity(PersonContact domain, BpsmtPersonContact entity) {
		entity.cellPhoneNumber = domain.getCellPhoneNumber().isPresent() ? domain.getCellPhoneNumber().get().v() : null;
		entity.mailAdress = domain.getMailAdress().isPresent() ? domain.getMailAdress().get().v() : null;
		entity.mobileMailAdress = domain.getMobileMailAdress().isPresent() ? domain.getMobileMailAdress().get().v()
				: null;
		
		entity.memo1 = null;
		entity.contactName1 = null;
		entity.phoneNo1 = null;

		entity.memo2 = null;
		entity.contactName2 = null;
		entity.phoneNo2 = null;
		
		if (domain.getEmergencyContact1().isPresent()) {
			EmergencyContact emergencyContact1 = domain.getEmergencyContact1().get();
			entity.memo1 = emergencyContact1.getMemo().map(i->i.v()).orElse(null);
			entity.contactName1 = emergencyContact1.getContactName().map(i->i.v()).orElse(null);
			entity.phoneNo1 = emergencyContact1.getPhoneNumber().map(i->i.v()).orElse(null);
		}

		if (domain.getEmergencyContact2().isPresent()) {
			EmergencyContact emergencyContact2 = domain.getEmergencyContact2().get();
			entity.memo2 = emergencyContact2.getMemo().map(i->i.v()).orElse(null);
			entity.contactName2 = emergencyContact2.getContactName().map(i->i.v()).orElse(null);
			entity.phoneNo2 = emergencyContact2.getPhoneNumber().map(i->i.v()).orElse(null);
		}

	}

	/**
	 * get domain PersonContact by person id
	 * 
	 * @param perId
	 *            -- person id
	 */
	@Override
	public Optional<PersonContact> getByPId(String perId) {
		BpsmtPersonContactPK key = new BpsmtPersonContactPK(perId);
		Optional<BpsmtPersonContact> entity = this.queryProxy().find(key, BpsmtPersonContact.class);
		if (entity.isPresent())
			return Optional.of(toDomain(entity.get()));
		else
			return Optional.empty();
	}

	@Override
	public List<PersonContact> getByPersonIdList(List<String> personIds) {
		List<BpsmtPersonContact> entities = new ArrayList<>();
		CollectionUtil.split(personIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			
			String sql = "select * from BPSMT_PERSON_CONTACT"
					+ " where PID in (" + NtsStatement.In.createParamsString(subList) + ")";
			try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
				
				for (int i = 0; i < subList.size(); i++) {
					stmt.setString(i + 1, subList.get(i));
				}
				List<BpsmtPersonContact> ents = new NtsResultSet(stmt.executeQuery()).getList(rec -> {
					BpsmtPersonContact ent = new BpsmtPersonContact();
					ent.bpsmtPersonContactPK = new BpsmtPersonContactPK(rec.getString("PID"));
					ent.cellPhoneNumber = rec.getString("CELL_PHONE_NO");
					ent.mailAdress = rec.getString("MAIL_ADDRESS");
					ent.mobileMailAdress = rec.getString("MOBILE_MAIL_ADDRESS");
					ent.memo1 = rec.getString("MEMO1");
					ent.contactName1 = rec.getString("CONTACT_NAME_1");
					ent.phoneNo1 = rec.getString("PHONE_NO_1");
					ent.memo2 = rec.getString("MEMO2");
					ent.contactName2 = rec.getString("CONTACT_NAME_2");
					ent.phoneNo2 = rec.getString("PHONE_NO_2");
					return ent;
				});
				entities.addAll(ents);
				
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
		return entities.stream().map(ent -> toDomain(ent)).collect(Collectors.toList());
	}

}
