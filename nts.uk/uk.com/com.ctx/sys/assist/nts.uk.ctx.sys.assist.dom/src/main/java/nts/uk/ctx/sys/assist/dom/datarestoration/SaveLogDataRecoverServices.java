/**
 * 
 */
package nts.uk.ctx.sys.assist.dom.datarestoration;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SaveLogDataRecoverServices {

	@Inject
	private DataRecoveryLogRepository repoDataRecoveryLog;

	public void saveErrorLogDataRecover(String recoveryProcessId, String target, String errorContent,
			GeneralDate targetDate, String processingContent, String contentSql) {
		GeneralDate logTime = GeneralDate.today();
		int logSequenceNumber = repoDataRecoveryLog.getMaxSeqId(recoveryProcessId) + 1;
		String errorContent_ = "";
		if (errorContent.length() > 1995) {
			errorContent_ =  subStringContent(errorContent, 1995);
		} else {
			errorContent_ = errorContent;
		}

		String processingContent_ = "";
		if (processingContent.length() > 95) {
			processingContent_ =  subStringContent(processingContent, 95);
		} else {
			processingContent_ = processingContent;
		}

		String contentSql_ = "";
		if (contentSql.length() > 1995) {
			contentSql_ = subStringContent(contentSql, 1995);
		} else {
			contentSql_ = contentSql;
		}

		try {
			DataRecoveryLog dataRecoveryLog = DataRecoveryLog.createFromJavatype(recoveryProcessId,
					target == null ? "" : target, errorContent_, logTime, logSequenceNumber, processingContent_,
					contentSql_);
			repoDataRecoveryLog.add(dataRecoveryLog);
		} catch (Exception e) {
			System.out.println("validate domain exception" + e.getMessage() +" " + e.getCause());
		}

		System.out.println("testtt");
	}

	private String subStringContent (String text , int length){
		String content = text.substring(0, length);
		int lengthByKiban = lengthHalf(content);
		if (content.length() != lengthByKiban) {
			int chenhlech = Math.abs(lengthByKiban - content.length());
			return content.substring(0, length-chenhlech);
		}
		return content;
	}
	
	public static int lengthHalf(String text) {
		int count = 0;
		for (int i = 0, len = text.length(); i < len; i++) {
			int code = text.codePointAt(i);
			count += lengthHalf(code);
		}
		return count;
	}

	/**
	 * Returns character length as half width characters.
	 * 
	 * @param codeOfCharacter
	 *            code of character
	 * @return length
	 */
	public static int lengthHalf(int codeOfCharacter) {
		// 0x20 ～ 0x80: 半角記号と半角英数字
		// 0xff61 ～ 0xff9f: 半角カタカナ
		// 0x0a,0x0d: CR,LF
		if ((0x20 <= codeOfCharacter && codeOfCharacter <= 0x7e)
				|| (0xff61 <= codeOfCharacter && codeOfCharacter <= 0xff9f) || codeOfCharacter == 0x0a
				|| codeOfCharacter == 0x0d) {
			return 1;
		} else {
			return 2;
		}
	}
}
