package nts.uk.ctx.exio.app.command.exi.csvimport;

import java.util.concurrent.TimeUnit;

import javax.ejb.Stateful;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.task.data.TaskDataSetter;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.exio.dom.exi.execlog.ExacErrorLog;
import nts.uk.ctx.exio.dom.exi.execlog.ExacErrorLogRepository;

@Stateful
public class SyncCsvCheckImportDataCommandHandler extends AsyncCommandHandler<CsvImportDataCommand> {
	@Inject 
	private ExacErrorLogRepository exacErrorLogRepository;
	
	private static final String NUMBER_OF_ERROR = "エラー件数";
	private static final String NUMBER_OF_SUCCESS = "処理カウント";
	private static final String NUMBER_OF_TOTAL = "処理トータルカウント";
	private static final String STOP_MODE = "中断するしない"; // 0 - しない　1-する
	private static final String STATUS = "動作状態"; // 0 - チェック中, 1 - チェック完了, 2 - 受入中, 3 - 完了
	
	@Override
	protected void handle(CommandHandlerContext<CsvImportDataCommand> context) {
		val asyncTask = context.asAsync();
		TaskDataSetter setter = asyncTask.getDataSetter();
		
		// アルゴリズム「非同期タスクデータを保存する」を実行する 

		// TODO	Start --- Dump data for testing. they will be deleted after process at phrase 2
		CsvImportDataCommand command = context.getCommand();
		setter.setData(NUMBER_OF_SUCCESS, command.getCurrentLine());
		setter.setData(NUMBER_OF_ERROR, command.getErrorCount());
		setter.setData(NUMBER_OF_TOTAL, command.getCsvLine());
		setter.setData(STOP_MODE, command.getStopMode());
		setter.setData(STATUS, command.getStateBehavior());
		
		for (int i = 1; i < command.getCsvLine(); i++) {
			//TODO アルゴリズム「外部受入テスト本体」を実行する

			if (asyncTask.hasBeenRequestedToCancel()) {
				// 外部受入動作管理の中断するしない区分を更新する
				setter.updateData(STOP_MODE, 1);
				asyncTask.finishedAsCancelled();
				break;
			}

			if(i %5 == 0){
				exacErrorLogRepository.add(new ExacErrorLog(i, "cid", command.getProcessId(), "csvErrorItemName", "csvAcceptedValue", "errorContents", 100, GeneralDateTime.ymdhms(2018,03,14,10,10,10), "ITEM_NAME", 1));
				setter.updateData(NUMBER_OF_ERROR, i/5);
			}
			setter.updateData(NUMBER_OF_SUCCESS, i);
			setter.updateData(STATUS, command.getStateBehavior());
			
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// TODO	END --- Dump data for testing. they will be deleted after process at phrase 2
	}

}
