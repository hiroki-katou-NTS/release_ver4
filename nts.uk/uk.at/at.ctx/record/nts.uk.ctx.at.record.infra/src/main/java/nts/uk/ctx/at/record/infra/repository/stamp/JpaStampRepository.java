package nts.uk.ctx.at.record.infra.repository.stamp;

import java.util.List;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.stamp.StampItem;
import nts.uk.ctx.at.record.dom.stamp.StampRepository;
import nts.uk.ctx.at.record.infra.entity.stamp.KwkdtStamp;

@Stateless
public class JpaStampRepository extends JpaRepository implements StampRepository {
	private final String SELECT_NO_WHERE = "SELECT e.personId, d.workLocationName, c FROM KwkdtStamp c";
	private final String SELECT_BY_EMPPLOYEE_CODE = SELECT_NO_WHERE
			+ " LEFT JOIN KwlmtWorkLocation d ON c.workLocationCd = d.kwlmtWorkLocationPK.workLocationCD"
			+ " AND d.kwlmtWorkLocationPK.companyID = :companyId"
			+ " INNER JOIN KwkdtStampCard e ON e.kwkdtStampCardPK.cardNumber = c.kwkdtStampPK.cardNumber"
			+ " WHERE c.kwkdtStampPK.stampDate >= :startDate"
			+ " AND c.kwkdtStampPK.stampDate <= :endDate" 
			+ " AND c.kwkdtStampPK.cardNumber IN :lstCardNumber";

	private static StampItem toDomain(Object[] object) {
		String personId = (String) object[0];
		String workLocationName = (String) object[1];
		KwkdtStamp entity = (KwkdtStamp) object[2];
		StampItem domain = StampItem.createFromJavaType(
				entity.kwkdtStampPK.cardNumber,
				entity.kwkdtStampPK.attendanceTime, 
				entity.stampCombinationAtr, 
				entity.siftCd, 
				entity.stampMethod,
				entity.kwkdtStampPK.stampAtr, 
				entity.workLocationCd, 
				workLocationName, 
				entity.stampReason,
				entity.kwkdtStampPK.stampDate,
				personId);
		return domain;
	}

	@Override
	public List<StampItem> findByEmployeeCode(String companyId, List<String> lstCardNumber, String startDate, String endDate) {
		List<StampItem> list = this.queryProxy().query(SELECT_BY_EMPPLOYEE_CODE, Object[].class)
				.setParameter("companyId", companyId)
				.setParameter("startDate", GeneralDate.fromString(startDate, "yyyyMMdd"))
				.setParameter("endDate", GeneralDate.fromString(endDate, "yyyyMMdd"))
				.setParameter("lstCardNumber", lstCardNumber)
				.getList(c -> toDomain(c));
		return list;
	}

	@Override
	public List<StampItem> findByDate(String companyId, String cardNumber, String startDate, String endDate) {
		// TODO Auto-generated method stubs
		return null;
	}

}
