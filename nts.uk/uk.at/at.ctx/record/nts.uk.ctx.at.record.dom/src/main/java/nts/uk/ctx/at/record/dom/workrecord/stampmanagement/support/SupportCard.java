/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.stampmanagement.support;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;

/**
 * AR: 応援カード 
 * path: UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.勤務実績.打刻管理.応援.応援カード
 * @author laitv
 */
@Getter
public class SupportCard extends AggregateRoot {

	// 会社ID
	private final String cid;

	// カード番号
	private final SupportCardNumber supportCardNumber;

	// 	職場ID
	private String workplaceId;

	public SupportCard(String cid, SupportCardNumber supportCardNumber, String workplaceId) {
		super();
		this.cid = cid;
		this.supportCardNumber = supportCardNumber;
		this.workplaceId = workplaceId;
	}
	
	public SupportCard(String cid, int supportCardNumber, String workplaceId) {
		super();
		this.cid = cid;
		this.supportCardNumber = new SupportCardNumber(supportCardNumber);
		this.workplaceId = workplaceId;
	}
	
	public static SupportCard create(String cid, int supportCardNumber, String workplaceId) {
		return new SupportCard(cid, supportCardNumber, workplaceId);
	}

}
