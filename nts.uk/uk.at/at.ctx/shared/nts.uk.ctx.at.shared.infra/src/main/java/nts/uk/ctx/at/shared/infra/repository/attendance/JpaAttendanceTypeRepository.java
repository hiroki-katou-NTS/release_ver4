package nts.uk.ctx.at.shared.infra.repository.attendance;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.attendance.AttendanceType;
import nts.uk.ctx.at.shared.dom.attendance.AttendanceTypeRepository;
import nts.uk.ctx.at.shared.infra.entity.attendance.KmnmtAttendanceType;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class JpaAttendanceTypeRepository extends JpaRepository implements AttendanceTypeRepository{
	
	private final String SEL_ITEM_BY_TYPE = "SELECT a "
			+ "FROM KmnmtAttendanceType a "
			+ "WHERE a.kmnmtAttendanceTypePK.companyId = :companyId "
			+ "AND a.kmnmtAttendanceTypePK.screenUseAtr = :screenUseAtr";
	
	@Override
	public List<AttendanceType> getItemByScreenUseAtr(String companyID, int screenUseAtr) {
		return this.queryProxy()
				.query(SEL_ITEM_BY_TYPE, KmnmtAttendanceType.class)
				.setParameter("companyId", companyID)
				.setParameter("screenUseAtr", screenUseAtr)
			.getList().stream().map(x -> 
				AttendanceType.createSimpleFromJavaType(
						x.kmnmtAttendanceTypePK.companyId, 
						x.kmnmtAttendanceTypePK.attendanceItemId,
						x.kmnmtAttendanceTypePK.screenUseAtr,
						x.attendanctType))
			.collect(Collectors.toList());
	}

}
