package nts.uk.ctx.exio.dom.qmm.specificationitem;

import java.util.Optional;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;

/**
 * 明細書項目名称
 */
@Getter
public class SpecificationItemName extends AggregateRoot {

	/**
	 * 会社ID
	 */
	private String cid;

	/**
	 * 給与項目ID
	 */
	private String salaryItemId;

	/**
	 * 名称
	 */
	private ItemName name;

	/**
	 * 略名
	 */
	private ShortName shortName;

	/**
	 * その他言語名称
	 */
	private Optional<OtherLanguageName> otherLanguageName;

	/**
	 * 英語名称
	 */
	private Optional<EnglishName> englishName;

	public SpecificationItemName(String cid, String salaryItemId, String name, String shortName,
			String otherLanguageName, String englishName) {
		this.cid = cid;
		this.salaryItemId = salaryItemId;
		this.name = new ItemName(name);
		this.shortName = new ShortName(shortName);
		this.otherLanguageName = otherLanguageName == null ? Optional.empty()
				: Optional.of(new OtherLanguageName(otherLanguageName));
		this.englishName = englishName == null ? Optional.empty() : Optional.of(new EnglishName(englishName));
	}
}
