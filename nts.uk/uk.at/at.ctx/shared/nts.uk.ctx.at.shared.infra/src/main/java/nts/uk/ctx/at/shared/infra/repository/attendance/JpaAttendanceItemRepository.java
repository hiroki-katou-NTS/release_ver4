package nts.uk.ctx.at.shared.infra.repository.attendance;

import java.util.List;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.attendance.AttendanceItem;
import nts.uk.ctx.at.shared.dom.attendance.AttendanceItemRepository;
import nts.uk.ctx.at.shared.infra.entity.attendance.KmnmtAttendanceItem;

@Stateless
public class JpaAttendanceItemRepository extends JpaRepository implements AttendanceItemRepository {

	private final String SELECT_NO_WHERE = "SELECT c FROM KmnmtAttendanceItem c";
	private final String SELECT_POSSIBLE_ITEM = SELECT_NO_WHERE
			+ " WHERE c.kmnmtAttendanceItemPK.companyId = :companyId"
			+ " AND c.kmnmtAttendanceItemPK.attendanceItemId IN :listPossibleItem";

	private static AttendanceItem toDomain(KmnmtAttendanceItem entity) {
		val domain = AttendanceItem.createSimpleFromJavaType(
				entity.kmnmtAttendanceItemPK.companyId,
				entity.kmnmtAttendanceItemPK.attendanceItemId, 
				entity.attendanceItemName, 
				entity.displayNumber,
				entity.useAtr, 
				entity.attendanceAtr);
		return domain;
	}

	@Override
	public List<AttendanceItem> getPossibleAttendanceItems(String companyId, List<Integer> lstPossible) {
		return this.queryProxy().query(SELECT_POSSIBLE_ITEM, KmnmtAttendanceItem.class)
				.setParameter("companyId", companyId)
				.setParameter("listPossibleItem", lstPossible)
				.getList(c -> toDomain(c));
	}

}
