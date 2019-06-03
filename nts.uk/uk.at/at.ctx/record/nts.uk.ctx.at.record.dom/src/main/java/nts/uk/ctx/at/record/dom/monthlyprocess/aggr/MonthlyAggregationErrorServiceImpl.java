package nts.uk.ctx.at.record.dom.monthlyprocess.aggr;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.task.data.TaskDataSetter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageResource;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ErrorPresent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.shr.com.i18n.TextResource;

/**
 * 実装：ドメインサービス：月別集計エラー処理
 * @author shuichi_ishida
 */
@Stateless
public class MonthlyAggregationErrorServiceImpl implements MonthlyAggregationErrorService {

	/** エラーメッセージ情報 */
	@Inject
	private ErrMessageInfoRepository errMessageInfoRepository;
	
	/**
	 * エラー処理（排他エラー用）
	 * @param dataSetter データセッター
	 * @param employeeId 社員ID
	 * @param empCalAndSumExecLogID 就業計算と集計実行ログID
	 * @param outYmd 出力年月日
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void errorProcForOptimisticLock(
			TaskDataSetter dataSetter,
			String employeeId,
			String empCalAndSumExecLogID,
			GeneralDate outYmd){
		
		// 「エラーあり」に更新
		dataSetter.updateData("monthlyAggregateHasError", ErrorPresent.HAS_ERROR.nameId);
		
		// エラー出力
		this.errMessageInfoRepository.add(new ErrMessageInfo(
				employeeId,
				empCalAndSumExecLogID,
				new ErrMessageResource("024"),
				ExecutionContent.MONTHLY_AGGREGATION,
				outYmd,
				new ErrMessageContent(TextResource.localize("Msg_1541"))));
	}
}
