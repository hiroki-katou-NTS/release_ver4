package nts.uk.ctx.pr.core.dom.itemmaster;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.gul.text.StringUtil;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.enums.CategoryAtr;
import nts.uk.ctx.pr.core.dom.enums.DisplayAtr;

@Getter
public class ItemMaster extends AggregateRoot {
	/**
	 * Company Code
	 */
	private CompanyCode companyCode;
	/**
	 * item Code
	 */
	private ItemCode itemCode;
	/**
	 * Item Name
	 */
	private ItemName itemName;
	/**
	 * Abbreviations Name
	 */
	private ItemAbName itemAbName;
	/**
	 * Abbreviations English Name
	 */
	private ItemName itemAbNameE;
	/**
	 * Abbreviations English Multi language
	 */
	private ItemName itemAbNameO;
	/**
	 * Category Attribute
	 */
	private CategoryAtr categoryAtr;
	/**
	 * Fix Attribute
	 */
	private FixAtr fixAtr;
	/**
	 * Display classification
	 */
	private DisplayAtr displaySet;
	/**
	 * Integration item code
	 */
	private UniteCode uniteCode;
	/**
	 * Zero display indicator
	 */
	private DisplayAtr zeroDisplaySet;
	/**
	 * Item name display classification
	 */
	private ItemDisplayAtr itemDisplayAtr;

	/**
	 * validate item Master
	 */
	@Override
	public void validate() {
		super.validate();
		if (StringUtil.isNullOrEmpty(this.itemCode.v(), true) || StringUtil.isNullOrEmpty(this.itemName.v(), true)
				|| StringUtil.isNullOrEmpty(this.itemAbName.v(), true) || this.displaySet == null) {
			throw new BusinessException("ER001");
		}

	}
	
	public void validateAddNew() {
		if (!StringUtils.isNumericSpace(this.itemCode.v())) {
			throw new BusinessException("ER001");
		}
	}

	public ItemMaster(CompanyCode companyCode, ItemCode itemCode, ItemName itemName, ItemAbName itemAbName,
			ItemName itemAbNameE, ItemName itemAbNameO, CategoryAtr categoryAtr, FixAtr fixAtr, DisplayAtr displaySet,
			UniteCode uniteCode, DisplayAtr zeroDisplaySet, ItemDisplayAtr itemDisplayAtr) {
		super();
		this.companyCode = companyCode;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.itemAbName = itemAbName;
		this.itemAbNameE = itemAbNameE;
		this.itemAbNameO = itemAbNameO;
		this.categoryAtr = categoryAtr;
		this.fixAtr = fixAtr;
		this.displaySet = displaySet;
		this.uniteCode = uniteCode;
		this.zeroDisplaySet = zeroDisplaySet;
		this.itemDisplayAtr = itemDisplayAtr;
	}

	/**
	 * Create instance using Java type parameters.
	 * 
	 * @return ItemMaster
	 */

	public static ItemMaster createFromJavaType(String companyCode, String itemCode, String itemName, String itemAbName,
			String itemAbNameE, String itemAbNameO, int categoryAtr, int fixAtr, int displaySet, String uniteCode,
			int zeroDisplaySet, int itemDisplayAtr) {
		return new ItemMaster(new CompanyCode(companyCode), new ItemCode(itemCode), new ItemName(itemName),
				new ItemAbName(itemAbName), new ItemName(itemAbNameE), new ItemName(itemAbNameO),
				EnumAdaptor.valueOf(categoryAtr, CategoryAtr.class), EnumAdaptor.valueOf(fixAtr, FixAtr.class),
				EnumAdaptor.valueOf(displaySet, DisplayAtr.class), new UniteCode(uniteCode),
				EnumAdaptor.valueOf(zeroDisplaySet, DisplayAtr.class),
				EnumAdaptor.valueOf(itemDisplayAtr, ItemDisplayAtr.class));
	}
}
