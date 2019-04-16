package nts.uk.screen.at.app.dailyperformance.correction.datadialog;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.algorithm.masterinfo.CodeNameInfo;
import nts.uk.ctx.at.record.dom.algorithm.masterinfo.GetMaterData;

/**
 * @author thanhnx 
 * マスタの取得
 *
 */
@Stateless
public class GetMaterDataImpl implements GetMaterData {

	@Inject
	private DataDialogWithTypeProcessor dataDialogWithTypeProcessor;

	@Override
	public Map<Integer, Map<String, CodeNameInfo>> getAllDataMaster(String companyId, GeneralDate dateReference,
			List<Integer> lstDivNO) {
		return dataDialogWithTypeProcessor.getAllDataMaster(companyId, dateReference, lstDivNO);

	}

}
