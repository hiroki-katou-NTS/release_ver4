package nts.uk.ctx.exio.dom.input.execute;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import lombok.val;
import nts.arc.task.tran.AtomTask;
import nts.uk.ctx.exio.dom.input.ExecutionContext;
import nts.uk.ctx.exio.dom.input.canonicalize.existing.AdjustExistingData;
import nts.uk.ctx.exio.dom.input.setting.ExternalImportCode;
import nts.uk.ctx.exio.dom.input.setting.ExternalImportSetting;

/**
 * 受入処理を実行する
 */
public class ExecuteImporting {

	public static Iterable<AtomTask> execute(Require require, String companyId, ExternalImportCode settingCode) {
		
		val setting = require.getExternalImportSetting(companyId, settingCode)
				.orElseThrow(() -> new RuntimeException("not found: " + companyId + ", " + settingCode));
		val context = ExecutionContext.create(setting);
		
		val transactionUnit = require.getGroupTransactionStrategy(context.getGroupId())
				.getTransactionUnit();
		
		switch (transactionUnit) {
		case ALL:
			return executeAll(require, context);
		case EMPLOYEE:
			return executeEmployees(require, context);
		default:
			throw new RuntimeException("unknown: " + transactionUnit);
		}
	}
	
	/**
	 * 全データを一括で受け入れる
	 * @param require
	 * @param context
	 * @return
	 */
	private static Iterable<AtomTask> executeAll(Require require, ExecutionContext context) {
		
		AtomTask atomTaskAdjust = AdjustExistingData.adjust(require, context);
		
		return Arrays.asList(atomTaskAdjust);
	}
	
	/**
	 * １社員ずつ受け入れる
	 * @param require
	 * @param context
	 * @return
	 */
	private static Iterable<AtomTask> executeEmployees(Require require, ExecutionContext context) {
		
		val employeeIds = require.getAllEmployeeIdsOfCanonicalizedData(context);
		
		return AtomTask.iterate(employeeIds, employeeId -> {
			
			AtomTask atomTaskAdjust = AdjustExistingData.adjust(require, context, employeeId);
			
			return atomTaskAdjust;
		});
	}
	
	public static interface Require extends
			AdjustExistingData.RequireAll,
			AdjustExistingData.RequireEmployee {
		
		Optional<ExternalImportSetting> getExternalImportSetting(String companyId, ExternalImportCode settingCode);
		
		GroupTransactionStrategy getGroupTransactionStrategy(int groupId);
		
		List<String> getAllEmployeeIdsOfCanonicalizedData(ExecutionContext context);
	}
}
