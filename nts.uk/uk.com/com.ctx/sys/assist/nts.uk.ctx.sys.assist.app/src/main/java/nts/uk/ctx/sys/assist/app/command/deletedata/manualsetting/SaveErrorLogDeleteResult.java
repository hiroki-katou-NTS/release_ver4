/**
 * 
 */
package nts.uk.ctx.sys.assist.app.command.deletedata.manualsetting;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.sys.assist.dom.deletedata.FileName;
import nts.uk.ctx.sys.assist.dom.deletedata.ManualSetDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.ResultDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.ResultDeletionRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.ResultLogDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.ResultLogDeletionRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.SaveStatus;
import nts.uk.shr.com.i18n.TextResource;

/**
 * @author laitv
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SaveErrorLogDeleteResult {
	
	private static final String MSG_DEL_ERROR_LOG = "CMF005_223";
	private static final String MSG_INTERRUPT_PROESS = "CMF005_216";
	@Inject
	private ResultLogDeletionRepository repoResultLogDel;
	@Inject
	private ResultDeletionRepository repoResultDel;
	
	/**
	 * ドメインモデル「データ削除の結果ログ」を追加する
	 */
	public void saveErrorWhenDelData(ManualSetDeletion domain, String msgError) {
		String msgId = MSG_DEL_ERROR_LOG;
		String errorContent = "";
		if(msgError.length() > 1995){
			errorContent = subStringContent(msgError, 1995);
		}else{
			errorContent = msgError;
		}
		GeneralDateTime logTime = GeneralDateTime.now();
		int seqId = repoResultLogDel.getMaxSeqId(domain.getDelId()) + 1;
		ResultLogDeletion resultLogDomain = ResultLogDeletion.createFromJavatype(seqId, domain.getDelId(),
				domain.getCompanyId(), logTime, TextResource.localize(msgId), errorContent, null, null);
		repoResultLogDel.add(resultLogDomain);
	}
	
	public void saveErrorWhenCreFileCsv(ManualSetDeletion domain, String msgError) {
		String errorContent = "";
		if(msgError.length() > 1995){
			errorContent = subStringContent(msgError, 1995);
		}else{
			errorContent = msgError;
		}
		GeneralDateTime logTime = GeneralDateTime.now();
		int seqId = repoResultLogDel.getMaxSeqId(domain.getDelId()) + 1;
		ResultLogDeletion resultLogDomain = ResultLogDeletion.createFromJavatype(seqId, domain.getDelId(),
				domain.getCompanyId(), logTime, TextResource.localize("CMF005_34") + " " + TextResource.localize("CMF005_205") ,
				errorContent, null, null);
		repoResultLogDel.add(resultLogDomain);
	}
	
	/**
	 * ドメインモデル「データ削除の結果ログ」を追加する
	 */
	public void saveErrorWhenInterruptProcessing(String delId, String cid) {
		String msgId = MSG_INTERRUPT_PROESS;
		String errorContent = "";
		GeneralDateTime logTime = GeneralDateTime.now();
		int seqId = repoResultLogDel.getMaxSeqId(delId) + 1;
		ResultLogDeletion resultLogDomain = ResultLogDeletion.createFromJavatype(seqId, delId,
				cid, logTime, TextResource.localize(msgId), errorContent, null, null);
		repoResultLogDel.add(resultLogDomain);
	}
	
	/**
	 * ドメインモデル「データ削除の保存結果」を更新する
	 * Update domain model 「データ削除の保存結果」
	 */
	public void saveEndResultDelInterrupt(String delId) {
		Optional<ResultDeletion> optResultDel = repoResultDel.getResultDeletionById(delId);
		if (optResultDel.isPresent()) {
			GeneralDateTime endDateTimeDel = GeneralDateTime.now();
			ResultDeletion resultDel = optResultDel.get();
			resultDel.setStatus(SaveStatus.INTERRUPTION);
			resultDel.setEndDateTimeDel(endDateTimeDel);
			resultDel.setFileName(new FileName(""));
			resultDel.setFileSize(0);
			resultDel.setFileId("");
			repoResultDel.update(resultDel);
		}
	
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
