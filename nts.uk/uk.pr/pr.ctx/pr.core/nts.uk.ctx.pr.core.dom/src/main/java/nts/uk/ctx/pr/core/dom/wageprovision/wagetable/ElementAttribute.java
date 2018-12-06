package nts.uk.ctx.pr.core.dom.wageprovision.wagetable;

import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.DomainObject;

/**
 * 要素の属性
 */
@Getter
@AllArgsConstructor
public class ElementAttribute extends DomainObject {

	/**
	 * マスタ数値区分
	 */
	private Optional<MasterNumericAtr> masterNumericAtr;

	/**
	 * 固定の要素
	 */
	private Optional<ElementType> fixedElement;

	/**
	 * 任意追加の要素
	 */
	private Optional<ItemNameCode> optionalAdditionalElement;

	public ElementAttribute(Integer masterNumericInformation, Integer fixedElement, String optionalAdditionalElement) {
		this.masterNumericAtr = masterNumericInformation == null ? Optional.empty()
				: Optional.of(EnumAdaptor.valueOf(masterNumericInformation, MasterNumericAtr.class));
		this.fixedElement = fixedElement == null ? Optional.empty()
				: Arrays.stream(ElementType.values()).filter(item -> item.value == fixedElement).findFirst();
		this.optionalAdditionalElement = optionalAdditionalElement == null ? Optional.empty()
				: Optional.of(new ItemNameCode(optionalAdditionalElement));

	}

}
