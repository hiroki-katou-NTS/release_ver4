package nts.uk.ctx.at.shared.infra.repository.remainingnumber.nursingcareleavemanagement.childcare.interimdata;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.childcare.interimdata.ChildTempCareData;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.childcare.interimdata.ChildTempCareDataRepository;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.nursingcareleave.childcare.interimdata.KrcdtChildTempCareData;

@Stateless
public class JpaChildTempCareDataRepository extends JpaRepository implements ChildTempCareDataRepository {

	private static final String QUERY_WITH_PERIOD = "SELECT c FROM KrcdtChildTempCareData c "
			+ " WHERE c.id.sid =:employeeId AND c.id.ymd >= :startDate AND c.id.ymd <= :endDate";

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ChildTempCareData> findByEmpIdInPeriod(String employeeId, GeneralDate startDate, GeneralDate endDate) {
		List<KrcdtChildTempCareData> entities = this.queryProxy().query(QUERY_WITH_PERIOD, KrcdtChildTempCareData.class)
				.setParameter("employeeId", employeeId).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).getList();
		return entities.stream().map(ent -> ent.toDomain()).collect(Collectors.toList());
	}

}
