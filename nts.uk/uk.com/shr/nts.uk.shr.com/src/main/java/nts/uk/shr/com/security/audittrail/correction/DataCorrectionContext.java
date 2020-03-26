package nts.uk.shr.com.security.audittrail.correction;

import java.io.Serializable;
import java.util.HashMap;
import java.util.function.Supplier;

import lombok.val;
import nts.arc.bean.SingletonBeansSoftCache;
import nts.gul.text.IdentifierUtil;
import nts.uk.shr.com.security.audittrail.correction.processor.CorrectionProcessorId;

/**
 * データ修正記録のコンテキスト情報
 */
public class DataCorrectionContext {
	
	public static final String DEFAULT_PARAMETER_KEY = "DEFAULT_PARAMETER_KEY";
	
	private static final int DEFAULT_TRANSACTION_CONTROL_LEVEL = -1;

	private static ThreadLocal<DataCorrectionContext> contextThreadLocal = new ThreadLocal<>();
	
	/** Processor ID */
	private final CorrectionProcessorId processorId;

	/** 操作ID */
	private final String operationId;
	
	/**
	 * トランザクション制御レベル
	 * transactionFinishingを呼ぶ際に、transactionBegunで指定した値と同じ値を渡さないと、呼び出しが無視される。
	 * 個人情報の登録のようにCommandFacadeを使っている場合は、transactionBegun/Finishingが二重に呼ばれる可能性があるが、
	 * そのケースでは、一番外側のBegun/Finishingで処理しなければならないため、その判別のためにこの値を指定させる。
	 */
	private final int transactionControlLevel;
	
	/** パラメータ */
	private HashMap<String, Serializable> parameters;
	
	private DataCorrectionContext(CorrectionProcessorId processorId, String operationId, int transactionControlLevel) {
		this.processorId = processorId;
		this.operationId = operationId;
		this.transactionControlLevel = transactionControlLevel;
		this.parameters = new HashMap<>();
	}
	
	public static void transactional(CorrectionProcessorId processorId, Runnable transaction) {
		transactional(processorId, DEFAULT_TRANSACTION_CONTROL_LEVEL, transaction);
	}
	
	public static <T> T transactional(CorrectionProcessorId processorId, Supplier<T> transaction) {
		return transactional(processorId, DEFAULT_TRANSACTION_CONTROL_LEVEL, transaction);
	}
	
	public static void transactional(CorrectionProcessorId processorId, int transactionControlLevel, Runnable transaction) {
		transactional(processorId, transactionControlLevel, () -> {
			transaction.run();
			return null;
		});
	}
	
	public static <T> T transactional(CorrectionProcessorId processorId, int transactionControlLevel, Supplier<T> transaction) {
		transactionBegun(processorId, transactionControlLevel);
		
		T result;
		try {
			result = transaction.get();
		} catch (Exception e) {
			transactionAborted(transactionControlLevel);
			throw e;
		}
		
		transactionFinishing(transactionControlLevel);
		return result;
	}
	
	/**
	 * Set a parameter to be passed to Correction Log Processor.
	 * @param parameter
	 */
	public static void setParameter(Serializable parameter) {
		setParameter(DEFAULT_PARAMETER_KEY, parameter);
	}
	
	/**
	 * Set a parameter to be passed to Correction Log Processor.
	 * @param key key
	 * @param parameter parameter
	 */
	public static void setParameter(String key, Serializable parameter) {
		val context = getCurrentContext();
		context.parameters.put(key, parameter);
	}
	
	/**
	 * This method must be called when transaction begin.
	 * @param processorId Processor ID
	 */
//	private static void transactionBegun(CorrectionProcessorId processorId) {
//		transactionBegun(processorId, DEFAULT_TRANSACTION_CONTROL_LEVEL);
//	}
	
	/**
	 * This method must be called when transaction begin.
	 * @param processorId Processor ID
	 */
	private static void transactionBegun(CorrectionProcessorId processorId, int transactionControlLevel) {
		// 個人情報の登録のようにCommandFacadeを使っている場合は、transactionBegunが二重に呼ばれる可能性がある
		if (contextThreadLocal.get() != null) {
			return;
		}
		
		contextThreadLocal.set(new DataCorrectionContext(
				processorId,
				IdentifierUtil.randomUniqueId(),
				transactionControlLevel));
	}
	
	/**
	 * This method must be called when transaction finish.
	 */
//	private static void transactionFinishing() {
//		transactionFinishing(DEFAULT_TRANSACTION_CONTROL_LEVEL);
//	}
	
	/**
	 * This method must be called when transaction finish.
	 */
	private static void transactionFinishing(int transactionControlLevel) {
		val context = getCurrentContext();
		if (context.transactionControlLevel != transactionControlLevel) {
			return;
		}
		
		val agent = SingletonBeansSoftCache.get(CorrectionLoggingAgent.class);
		agent.requestProcess(context.operationId, context.processorId, context.parameters);
		
		cleanTransaction();
	}
	
	private static void transactionAborted(int transactionControlLevel) {
		val context = getCurrentContext();
		if (context.transactionControlLevel != transactionControlLevel) {
			return;
		}

		cleanTransaction();
	}
	
	private static void cleanTransaction() {
		contextThreadLocal.set(null);
	}
	
	private static DataCorrectionContext getCurrentContext() {
		val context = contextThreadLocal.get();
		
		if (context == null) {
			throw new RuntimeException("the current context has not been started.");
		}
		
		return context;
	}
	
}
