package nts.uk.ctx.sys.portal.dom.toppage;

import lombok.Getter;
import nts.uk.ctx.sys.portal.dom.primitive.CompanyId;
import nts.uk.ctx.sys.portal.dom.primitive.TopPageCode;
import nts.uk.ctx.sys.portal.dom.primitive.TopPageName;

/**
 * The Class TopPage.
 */
@Getter
public class TopPage {
	
	/** The company id. */
	private CompanyId companyId;
	
	/** The top page code. */
	private TopPageCode topPageCode;
	
	/** The layout. */
	private Layout layout;
	
	/** The top page name. */
	private TopPageName topPageName;
	
	/** The language number. */
	private Language languageNumber;

	/**
	 * Instantiates a new top page.
	 */
	private TopPage() {
		super();
	}

	/**
	 * @param topPageCode
	 * @param layout
	 * @param topPageName
	 * @param languageNumber
	 */
	public TopPage(TopPageCode topPageCode, Layout layout, TopPageName topPageName,Language languageNumber) {
		super();
		this.topPageCode = topPageCode;
		this.layout = layout;
		this.topPageName = topPageName;
		this.languageNumber = languageNumber;
	}
	
}
