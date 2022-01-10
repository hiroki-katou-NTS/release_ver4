/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.stampmanagement.support;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.objecttype.DomainAggregate;
import nts.uk.ctx.at.record.dom.stamp.card.stamcardedit.StampCardEditMethod;

/**
 * AR: 応援カード 
 * path: UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.勤務実績.打刻管理.応援.応援カード
 * @author laitv
 */
@Getter
@AllArgsConstructor
public class SupportCard implements DomainAggregate {

	// 会社ID
	private final String cid;

	// カード番号
	private final SupportCardNumber supportCardNumber;

	// 	職場ID
	@Setter
	private String workplaceId;
	
	// [C-1] 応援カード作成する
	public static SupportCard create(Require require, String cid, SupportCardNumber supportCardNumber, String workplaceId) {
		Optional<SupportCardEdit> optEdit = require.getSupportCardEditSetting(cid);
		if (!optEdit.isPresent()) {
			optEdit = Optional.of(new SupportCardEdit(StampCardEditMethod.PreviousZero));
		}
		SupportCardNumber cardNumber = optEdit.map(data -> data.editTheCard(supportCardNumber)).orElse(supportCardNumber);
		return new SupportCard(cid, cardNumber, workplaceId);
	}
	
	public interface Require {
		
		// [R-1] 編集設定を取得する	
		Optional<SupportCardEdit> getSupportCardEditSetting(String cid);
	}
}
