package nts.uk.ctx.at.request.dom.application.overtime.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.common.adapter.frame.OvertimeInputCaculation;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoImport;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.IErrorCheckBeforeRegister;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeInput;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeCheckResult;
@Stateless
public class OvertimeFourProcessImpl implements OvertimeFourProcess{
	@Inject
	private IErrorCheckBeforeRegister IErrorCheckBeforeRegister;
	@Inject
	private OvertimeSixProcess overtimeSixProcess;
	@Inject
	private RecordWorkInfoAdapter recordWorkInfoAdapter;
	
	@Override
	public List<CaculationTime> checkDisplayColor(List<CaculationTime> overTimeInputs,
			List<OvertimeInputCaculation> overtimeInputCaculations, int prePostAtr, GeneralDateTime inputDate,
			GeneralDate appDate, int appType, String employeeID, String companyID) {
		for(CaculationTime overtimeInput : overTimeInputs ){
			for(OvertimeInputCaculation overtimeInputCaculation : overtimeInputCaculations){
					if(overtimeInput.getFrameNo() == overtimeInputCaculation.getFrameNo()){
						if(overtimeInput.getApplicationTime() != null && overtimeInput.getApplicationTime() != overtimeInputCaculation.getResultCaculation()){
							overtimeInput.setErrorCode(3); // 色定義名：計算値
						}
						if(overtimeInputCaculation.getResultCaculation() != null && overtimeInputCaculation.getResultCaculation() == 0){
							continue;
						}else if(overtimeInputCaculation.getResultCaculation() != null && overtimeInputCaculation.getResultCaculation() > 0){
							// 03-01_事前申請超過チェック
							OvertimeCheckResult overtimeCheckResult = this.IErrorCheckBeforeRegister.preApplicationExceededCheck(overtimeInput.getCompanyID(),appDate, inputDate, EnumAdaptor.valueOf(prePostAtr, PrePostAtr.class), overtimeInputCaculation.getAttendanceID(), convert(overtimeInput));
							if(overtimeCheckResult.getErrorCode() != 0){
								overtimeInput.setErrorCode(overtimeCheckResult.getErrorCode());
							}
							//04-01_計算実績超過チェック(一覧)
							List<OvertimeInputCaculation> cal= new ArrayList<>();
							cal.add(overtimeInputCaculation);
							overtimeInput.setErrorCode(checkCalculationActualExcess(prePostAtr,appType,employeeID,companyID,appDate,overtimeInput,cal).getErrorCode());
						}
					}
			}
		}
		return overTimeInputs;
	}
	//04-01_計算実績超過チェック(一覧)
	@Override
	public CaculationTime checkCalculationActualExcess(int prePostAtr, int appType, String employeeID,
			String companyID, GeneralDate appDate,
			CaculationTime overTimeInputs,List<OvertimeInputCaculation> overtimeInputCaculations) {
		CaculationTime caculation = new CaculationTime();
		boolean condition = overtimeSixProcess.checkCondition(prePostAtr,appType,companyID);
		if(condition){
			//Imported(申請承認)「勤務実績」を取得する
			RecordWorkInfoImport recordWorkInfoImport = recordWorkInfoAdapter.getRecordWorkInfo(employeeID,appDate);
			// Imported(申請承認)「計算残業時間」を取得する 
			caculation = printColor(overTimeInputs,overtimeInputCaculations);
			
		}
		return caculation;
	}
	private CaculationTime printColor(CaculationTime overtimeHour,List<OvertimeInputCaculation> overtimeInputCaculations){
			for(OvertimeInputCaculation overtimeInput :overtimeInputCaculations){
				if(overtimeHour.getFrameNo() == overtimeInput.getFrameNo()){
					if(overtimeHour.getApplicationTime()!= null && overtimeInput.getResultCaculation() != null && overtimeHour.getApplicationTime() > overtimeInput.getResultCaculation()){
						overtimeHour.setFrameNo(overtimeHour.getFrameNo());
						overtimeHour.setErrorCode(2);
						overtimeHour.setCaculationTime(Integer.toString(overtimeInput.getResultCaculation()));
					}else{
						overtimeHour.setCaculationTime(overtimeInput.getResultCaculation() == null ? null : Integer.toString(overtimeInput.getResultCaculation()));
					}
				}
			}
		return overtimeHour;
	}
	
	private List<OverTimeInput> convert(CaculationTime caculationTime){
		List<OverTimeInput> overTimeInputs = new ArrayList<>();
			if(caculationTime .getApplicationTime() != null){
				OverTimeInput overTimeInput = OverTimeInput.createSimpleFromJavaType(caculationTime.getCompanyID(),
						caculationTime.getAppID(),
						caculationTime.getAttendanceID(), 
						caculationTime.getFrameNo(),
						-1,
						-1,
						caculationTime.getApplicationTime(),
						caculationTime.getTimeItemTypeAtr());
				overTimeInputs.add(overTimeInput);
			}
		return overTimeInputs;
	}
}
