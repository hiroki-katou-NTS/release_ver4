package nts.uk.ctx.at.record.infra.repository.remainingnumber;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.paymana.PayoutSubofHDManaRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.paymana.PayoutSubofHDManagement;
import nts.uk.ctx.at.record.infra.entity.remainingnumber.paymana.KrcmtPayoutSubOfHDMana;
import nts.uk.ctx.at.record.infra.entity.remainingnumber.paymana.KrcmtPayoutSubOfHDManaPK;

@Stateless
public class JpaPayoutSubofHDManaRepository extends JpaRepository implements PayoutSubofHDManaRepository {

	private final String QUERY = "SELECT ps FROM KrcmtPayoutSubOfHDMana ps ";
	
	private final String QUERY_BY_PAYOUTID = String.join(" ",QUERY, " WHERE ps.krcmtPayoutSubOfHDManaPK.payoutId =:payoutId");
	private final String QUERY_BY_SUBID = String.join(" ",QUERY, " WHERE ps.krcmtPayoutSubOfHDManaPK.subOfHDID =:subOfHDID");
	private final String DELETE_BY_PAYOUTID = "DELETE FORM KrcmtPayoutSubOfHDMana WHERE ps.krcmtPayoutSubOfHDManaPK.payoutId =:payoutId";
	
	@Override
	public void add(PayoutSubofHDManagement domain) {
		this.commandProxy().insert(toEntity(domain));
	}

	@Override
	public void update(PayoutSubofHDManagement domain) {
		KrcmtPayoutSubOfHDManaPK key = new KrcmtPayoutSubOfHDManaPK(domain.getPayoutId(), domain.getSubOfHDID());
		Optional<KrcmtPayoutSubOfHDMana> existed = this.queryProxy().find(key, KrcmtPayoutSubOfHDMana.class);
		if (existed.isPresent()){
			this.commandProxy().update(toEntity(domain));	
		}
	}

	@Override
	public void delete(String payoutId, String subOfHDID) {
		KrcmtPayoutSubOfHDManaPK key = new KrcmtPayoutSubOfHDManaPK(payoutId, subOfHDID);
		Optional<KrcmtPayoutSubOfHDMana> existed = this.queryProxy().find(key, KrcmtPayoutSubOfHDMana.class);
		if (existed.isPresent()){
			this.commandProxy().remove(KrcmtPayoutSubOfHDMana.class, key);	
		}
		
	}
	
	/** 
	 * Convert from enity to domain
	 * @param entity
	 * @return
	 */
	private PayoutSubofHDManagement toDomain(KrcmtPayoutSubOfHDMana entity){
		return new PayoutSubofHDManagement(entity.krcmtPayoutSubOfHDManaPK.payoutId, entity.krcmtPayoutSubOfHDManaPK.subOfHDID, entity.usedDays, entity.targetSelectionAtr);
	}
	
	/**
	 * Convert from domain to entity
	 * @param domain
	 * @return
	 */
	private KrcmtPayoutSubOfHDMana toEntity(PayoutSubofHDManagement domain){
		KrcmtPayoutSubOfHDManaPK key = new KrcmtPayoutSubOfHDManaPK(domain.getPayoutId(), domain.getSubOfHDID());
		return new KrcmtPayoutSubOfHDMana(key, domain.getUsedDays().v(), domain.getTargetSelectionAtr().value);
	}

	@Override
	public List<PayoutSubofHDManagement> getByPayoutId(String payoutId) {
		List<KrcmtPayoutSubOfHDMana> listpayoutSub = this.queryProxy().query(QUERY_BY_PAYOUTID,KrcmtPayoutSubOfHDMana.class)
				.setParameter("payoutId", payoutId).getList();
		return listpayoutSub.stream().map(item->toDomain(item)).collect(Collectors.toList());
	}

	@Override
	public List<PayoutSubofHDManagement> getBySubId(String subID) {
		List<KrcmtPayoutSubOfHDMana> listpayoutSub = this.queryProxy().query(QUERY_BY_PAYOUTID,KrcmtPayoutSubOfHDMana.class)
				.setParameter("subOfHDID", subID).getList();
		return listpayoutSub.stream().map(item->toDomain(item)).collect(Collectors.toList());
	}

	@Override
	public void delete(String payoutId) {
		this.getEntityManager().createQuery(DELETE_BY_PAYOUTID).setParameter("payoutId", payoutId);
	}

}
