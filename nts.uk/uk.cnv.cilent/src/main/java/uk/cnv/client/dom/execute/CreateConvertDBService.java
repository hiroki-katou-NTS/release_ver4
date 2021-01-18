package uk.cnv.client.dom.execute;

import lombok.val;
import uk.cnv.client.LogManager;
import uk.cnv.client.UkConvertProperty;

public class CreateConvertDBService {
	private static final String CREATE_CONVERT_DB_SCRIPT = "createConvertDbScript";
	private static final String CREATE_UKDB_SCRIPT = "createUkDbScript";

	public CommandResult doWork() {
		String createConvertDbScript = UkConvertProperty.getProperty(CREATE_CONVERT_DB_SCRIPT);
		String createUkDbScript = UkConvertProperty.getProperty(CREATE_UKDB_SCRIPT);
		val executor = new CommandExecutor();

		LogManager.out("移行作業用DB作成　開始");
		val createConvertDbResult = executor.sqlFileExecuteNoDbName(createConvertDbScript);
		if(createConvertDbResult.isError()) {
			return createConvertDbResult;
		}
		LogManager.out("移行作業用DB作成　終了");

		LogManager.out("移行先DB作成　開始");
		val createUkDbResult = executor.sqlFileExecuteNoDbName(createUkDbScript);
		LogManager.out("移行先DB作成　終了");
		return createUkDbResult;
	}
}
