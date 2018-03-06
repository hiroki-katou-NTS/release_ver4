package nts.uk.ctx.at.record.infra.repository.workrecord.erroralarm;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErAlApplication;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErAlApplicationRepository;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.KrcstErAlApplication;
@Stateless
public class JpaErAlApplicationRepository extends JpaRepository implements ErAlApplicationRepository {

	private final String GET_ERAL_BY_CODE = " SELECT c FROM KrcstErAlApplication c "
			+ " WHERE c.krcstErAlApplicationPK.cid = :cid"
			+ " AND c.krcstErAlApplicationPK.errorCd = :errorCd";
	
	@Override
	public Optional<ErAlApplication> getAllErAlAppByEralCode(String companyID, String errorAlarmCode) {
		
		
		List<KrcstErAlApplication> listKrcstErAlApplication = this.queryProxy().query(GET_ERAL_BY_CODE,KrcstErAlApplication.class)
		.setParameter("cid", companyID)
		.setParameter("errorCd", errorAlarmCode)
		.getList();
		if(listKrcstErAlApplication.isEmpty()) {
			return null;
		}
		List<Integer> listAppType = listKrcstErAlApplication.stream().map(c->c.krcstErAlApplicationPK.appTypeCd.intValueExact()).collect(Collectors.toList());
		
		return Optional.of( new ErAlApplication(companyID,errorAlarmCode,listAppType));
	}

}
