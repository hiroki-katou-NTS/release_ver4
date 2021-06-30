package nts.uk.ctx.exio.dom.input.canonicalize.groups;

import java.util.List;

import nts.arc.task.tran.AtomTask;
import nts.uk.ctx.exio.dom.input.ExecutionContext;
import nts.uk.ctx.exio.dom.input.canonicalize.CanonicalizedDataRecord;
import nts.uk.ctx.exio.dom.input.canonicalize.existing.AnyRecordToChange;
import nts.uk.ctx.exio.dom.input.canonicalize.existing.AnyRecordToDelete;
import nts.uk.ctx.exio.dom.input.canonicalize.methods.CanonicalizationMethod;
import nts.uk.ctx.exio.dom.input.canonicalize.methods.employee.history.EmployeeContinuousHistoryCanonicalization;

/**
 * 受入グループ別の正準化
 */
public interface GroupCanonicalization {

	/**
	 * 正準化する
	 * @param require
	 * @param context
	 */
	void canonicalize(RequireCanonicalize require, ExecutionContext context);
	
	/**
	 * 受入に影響される既存データを補正する
	 * @param require
	 * @param recordsToChange
	 * @param recordsToDelete
	 */
	AtomTask adjust(
			RequireAdjsut require,
			List<AnyRecordToChange> recordsToChange,
			List<AnyRecordToDelete> recordsToDelete);
	
	/**
	 * この受入グループにおける社員IDの項目Noを返す
	 * @return
	 * @throws UnSupportedOperationException そもそも社員IDの項目が存在しないグループに対して実行した場合
	 */
	int getItemNoOfEmployeeId();
	
	public static interface RequireCanonicalize extends
		CanonicalizationMethod.Require,
		TaskCanonicalization.RequireCanonicalize,
		DailyPerformanceCanonicalization.RequireCanonicalize {
		
		void save(ExecutionContext context, AnyRecordToDelete toDelete);
		
		void save(ExecutionContext context, CanonicalizedDataRecord canonicalizedDataRecord);
	}
	
	public static interface RequireAdjsut extends
		TaskCanonicalization.RequireAdjust,
		EmployeeContinuousHistoryCanonicalization.RequireAdjust,
		DailyPerformanceCanonicalization.RequireAdjust {
		
	}
}
