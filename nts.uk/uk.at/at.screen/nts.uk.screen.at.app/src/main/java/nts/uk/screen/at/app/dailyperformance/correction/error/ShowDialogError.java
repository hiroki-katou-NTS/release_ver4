package nts.uk.screen.at.app.dailyperformance.correction.error;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPErrorDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.OperationOfDailyPerformanceDto;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ShowDialogError {
	
	@Inject
	private DailyPerformanceScreenRepo dailyPerformanceScreenRepo;

	// エラーアラーム参照を起動するかチェックする
	public boolean showDialogError(List<DPErrorDto> lstError, Boolean showError, OperationOfDailyPerformanceDto settingMaster) {
		if (lstError.isEmpty())
			return false;
		//fix bug 102116
		//them thuat toan ドメインモデル「勤務実績のエラーアラーム」を取得する
		//check table 'era set'
		boolean isErAl = dailyPerformanceScreenRepo.isErAl(AppContexts.user().companyId(), 
				lstError.stream().map(e -> e.getErrorCode()).collect(Collectors.toList()));
		if(isErAl == false)
			return false;
		if (showError != null)
			return showError;

		return settingMaster == null ? false : settingMaster.isShowError();
	}

}
