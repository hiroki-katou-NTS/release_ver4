package nts.uk.ctx.at.function.pub.attendancetype;

import java.util.List;


public interface AttendanceTypePub {
	public List<AttendanceTypePubDto> getItemByScreenUseAtr(String companyId, int screenUseAtr);
}
