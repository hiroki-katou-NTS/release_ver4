package nts.uk.ctx.at.record.dom.divergence.time.service.attendance;

import java.util.List;

public interface DivergenceAttendanceTypeAdapter {

	public List<DivergenceAttendanceTypeDto> getItemByScreenUseAtr(String companyId, int screenUseAtr);
}
