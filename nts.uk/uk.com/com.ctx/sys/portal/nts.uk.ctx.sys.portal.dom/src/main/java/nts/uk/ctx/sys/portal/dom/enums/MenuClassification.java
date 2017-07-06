package nts.uk.ctx.sys.portal.dom.enums;

public enum MenuClassification {
	
	/**0:標準 */
	Standard(0),
	/**1:任意項目申請 */
	OptionalItemApplication(1),
	/**2:携帯 */
	MobilePhone(2),
	/**3:タブレット */
	Tablet(3),
	/**4:コード名称 */
	CodeName(4),
	/**5:グループ会社メニュー */
	GroupCompanyMenu(5),
	/**6:カスタマイズ */
	Customize(6),
	/**7:オフィスヘルパー稟議書*/
	OfficeHelper(7),
	/**8:トップページ*/
	TopPage(8);

	public int value;

	private MenuClassification(int type) {
		this.value = type;
	}

}
