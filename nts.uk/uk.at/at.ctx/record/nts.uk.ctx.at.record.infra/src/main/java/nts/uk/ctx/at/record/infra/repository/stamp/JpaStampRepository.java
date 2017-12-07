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
	private final String SELECT_STAMP = "SELECT c FROM KwkdtStamp c";
	private final String SELECT_NO_WHERE = "SELECT e.employeeID, d.workLocationName, c FROM KwkdtStamp c";

	private final String SELECT_BY_LIST_CARD_NO = SELECT_STAMP + " WHERE c.kwkdtStampPK.cardNumber IN :lstCardNumber";
	private final String SELECT_BY_EMPPLOYEE_ID = SELECT_NO_WHERE
			+ " LEFT JOIN KwlmtWorkLocation d ON c.workLocationCd = d.kwlmtWorkLocationPK.workLocationCD"
			+ " AND d.kwlmtWorkLocationPK.companyID = :companyId"
			+ " INNER JOIN KwkdtStampCard e ON e.kwkdtStampCardPK.cardNumber = c.kwkdtStampPK.cardNumber"
			+ " WHERE c.kwkdtStampPK.stampDate >= :startDate" + " AND c.kwkdtStampPK.stampDate <= :endDate"
			+ " AND c.kwkdtStampPK.cardNumber IN :lstCardNumber";

	/**
	 * Convert to domain contain Stamp Entity only.
	 * 
	 * @param object
	 * @return
	 */
	private static StampItem toDomainStampOnly(Object[] object) {
		KwkdtStamp entity = (KwkdtStamp) object[0];
		// Set empty value for 2 record not exist in Stamp Entity.
		StampItem domain = StampItem.createFromJavaType(entity.kwkdtStampPK.cardNumber,
				entity.kwkdtStampPK.attendanceTime, entity.stampCombinationAtr, entity.siftCd, entity.stampMethod,
				entity.kwkdtStampPK.stampAtr, entity.workLocationCd, "", entity.stampReason,
				entity.kwkdtStampPK.stampDate, "");
		return domain;
	}

	private static StampItem toDomain(Object[] object) {
		String personId = (String) object[0];
		String workLocationName = (String) object[1];
		KwkdtStamp entity = (KwkdtStamp) object[2];
		StampItem domain = StampItem.createFromJavaType(entity.kwkdtStampPK.cardNumber,
				entity.kwkdtStampPK.attendanceTime, entity.stampCombinationAtr, entity.siftCd, entity.stampMethod,
				entity.kwkdtStampPK.stampAtr, entity.workLocationCd, workLocationName, entity.stampReason,
				entity.kwkdtStampPK.stampDate, personId);
		return domain;
	}

	/**
	 * Get list StampItem by List Card Number.
	 */
	@Override
	public List<StampItem> findByListCardNo(List<String> lstCardNumber) {
		List<StampItem> list = this.queryProxy().query(SELECT_BY_LIST_CARD_NO, Object[].class)
				.setParameter("lstCardNumber", lstCardNumber).getList(c -> toDomainStampOnly(c));
		return list;
	}

	@Override
	public List<StampItem> findByEmployeeID(String companyId, List<String> stampCards, String startDate,
			String endDate) {
		List<StampItem> list = this.queryProxy().query(SELECT_BY_EMPPLOYEE_ID, Object[].class)
				.setParameter("companyId", companyId)
				.setParameter("startDate", GeneralDate.fromString(startDate, "yyyyMMdd"))
				.setParameter("endDate", GeneralDate.fromString(endDate, "yyyyMMdd"))
				.setParameter("lstCardNumber", stampCards)
				.getList(c -> toDomain(c));
		return list;
	}

	@Override
	public List<StampItem> findByDate(String companyId, String cardNumber, String startDate, String endDate) {
		// TODO Auto-generated method stubs
		return null;
	}

}
