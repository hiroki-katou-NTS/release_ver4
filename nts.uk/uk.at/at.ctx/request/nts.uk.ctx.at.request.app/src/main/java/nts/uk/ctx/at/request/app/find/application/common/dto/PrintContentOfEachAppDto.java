package nts.uk.ctx.at.request.app.find.application.common.dto;

import java.util.Optional;

import nts.uk.ctx.at.request.app.find.application.gobackdirectly.InforGoBackCommonDirectDto;
import nts.uk.ctx.at.request.app.find.application.lateorleaveearly.ArrivedLateLeaveEarlyInfoDto;
import nts.uk.ctx.at.request.app.find.application.stamp.dto.AppStampOutputDto;
import nts.uk.ctx.at.request.app.find.application.workchange.AppWorkChangeOutputDto;
import nts.uk.ctx.at.request.dom.application.common.service.print.PrintContentOfEachApp;
import nts.uk.ctx.at.request.dom.application.common.service.print.PrintContentOfWorkChange;
import nts.uk.ctx.at.request.dom.application.workchange.output.AppWorkChangeOutput;

/**
 * refactor 4
 * @author Doan Duy Hung
 *
 */
public class PrintContentOfEachAppDto {
	
	/**
	 * 休暇申請の印刷内容
	 */
	
	/**
	 * 勤務変更申請の印刷内容
	 */
	public AppWorkChangeOutputDto opPrintContentOfWorkChange;
	
	/**
	 * 時間休暇申請の印刷内容
	 */
	
	/**
	 * 打刻申請の印刷内容
	 */
	public AppStampOutputDto opAppStampOutput;
	
	/**
	 * 遅刻早退取消申請の印刷内容
	 */
	public ArrivedLateLeaveEarlyInfoDto opArrivedLateLeaveEarlyInfo;

	/**
	 * 直行直帰申請の印刷内容
	 */
	public InforGoBackCommonDirectDto opInforGoBackCommonDirectOutput;
	
	public PrintContentOfEachApp toDomain() {
		PrintContentOfEachApp printContentOfEachApp = new PrintContentOfEachApp();
		if(opPrintContentOfWorkChange != null) {
			AppWorkChangeOutput appWorkChangeOutput = opPrintContentOfWorkChange.toDomain();
			PrintContentOfWorkChange printContentOfWorkChange = new PrintContentOfWorkChange(
					appWorkChangeOutput.getAppWorkChange().orElse(null),
					appWorkChangeOutput.getAppWorkChangeDispInfo());
			printContentOfEachApp.setOpPrintContentOfWorkChange(Optional.of(printContentOfWorkChange));
		}
		if(opAppStampOutput != null) {
			printContentOfEachApp.setOpAppStampOutput(Optional.of(opAppStampOutput.toDomain()));
		}
		if(opArrivedLateLeaveEarlyInfo != null) {
			printContentOfEachApp.setOpArrivedLateLeaveEarlyInfo(Optional.of(opArrivedLateLeaveEarlyInfo.toDomain()));
		}
		if(opInforGoBackCommonDirectOutput != null) {
			printContentOfEachApp.setOpInforGoBackCommonDirectOutput(Optional.of(opInforGoBackCommonDirectOutput.toDomain()));
		}
		return printContentOfEachApp;
	}
	
}
