/**
 * 
 */
package nts.uk.ctx.sys.assist.app.command.deletedata.manualsetting;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.sys.assist.dom.deletedata.ManualSetDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.ResultLogDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.ResultLogDeletionRepository;
import nts.uk.shr.com.i18n.TextResource;

/**
 * @author laitv
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SaveErrorLogDeleteResult {
	
	private static final String MSG_DEL_ERROR_LOG = "CMF005_223";
	
	@Inject
	private ResultLogDeletionRepository repoResultLogDel;
	
	/**
	 * ドメインモデル「データ削除の結果ログ」を追加する
	 */
	public void saveErrorWhenDelData(ManualSetDeletion domain, String msgError) {
		String msgId = MSG_DEL_ERROR_LOG;
		String errorContent = "";
		if(msgError.length() > 2000){
			errorContent = msgError.substring(0, 1995) + "...";
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
		if(msgError.length() > 2000){
			errorContent = msgError.substring(0, 1995) + "...";
		}else{
			errorContent = msgError;
		}
		GeneralDateTime logTime = GeneralDateTime.now();
		int seqId = repoResultLogDel.getMaxSeqId(domain.getDelId()) + 1;
		ResultLogDeletion resultLogDomain = ResultLogDeletion.createFromJavatype(seqId, domain.getDelId(),
				domain.getCompanyId(), logTime, TextResource.localize("CMF005_34") + " 失敗  " + TextResource.localize("CMF005_205") ,
				errorContent, null, null);
		repoResultLogDel.add(resultLogDomain);
	}

}
