package nts.uk.ctx.at.request.ac.record;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.pub.workinformation.RecordWorkInfoPub;
import nts.uk.ctx.at.record.pub.workinformation.RecordWorkInfoPubExport;
import nts.uk.ctx.at.request.dom.application.common.adapter.frame.OvertimeInputCaculation;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoImport;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class RecordWorkInfoImpl implements RecordWorkInfoAdapter {
	
	@Inject
	private RecordWorkInfoPub recordWorkInfoPub;
	
	@Override
	public RecordWorkInfoImport getRecordWorkInfo(String employeeId, GeneralDate ymd) {
		List<OvertimeInputCaculation> overtimeCaculations = initOvertimeCaculation();
		RecordWorkInfoPubExport recordWorkInfoPubExport = recordWorkInfoPub.getRecordWorkInfo(employeeId, ymd);
		return new RecordWorkInfoImport(
				recordWorkInfoPubExport.getWorkTypeCode(), 
				recordWorkInfoPubExport.getWorkTimeCode(), 
				recordWorkInfoPubExport.getAttendanceStampTimeFirst(), 
				recordWorkInfoPubExport.getLeaveStampTimeFirst(), 
				recordWorkInfoPubExport.getAttendanceStampTimeSecond(), 
				recordWorkInfoPubExport.getLeaveStampTimeSecond(), 
				recordWorkInfoPubExport.getLateTime1(), 
				recordWorkInfoPubExport.getLeaveEarlyTime1(), 
				recordWorkInfoPubExport.getLateTime2(), 
				recordWorkInfoPubExport.getLeaveEarlyTime2(), 
				recordWorkInfoPubExport.getChildCareTime(),
				0,
				overtimeCaculations,
				overtimeCaculations,
				overtimeCaculations,
				overtimeCaculations,
				0,
				0);
	}
	private List<OvertimeInputCaculation> initOvertimeCaculation(){
		List<OvertimeInputCaculation> result = new ArrayList<>();
		for(int i = 0; i < 10; i++){
			OvertimeInputCaculation overtimeCaculation = new OvertimeInputCaculation(0, i, 0);
			result.add(overtimeCaculation);
		}
		return result;
	}
	
}
