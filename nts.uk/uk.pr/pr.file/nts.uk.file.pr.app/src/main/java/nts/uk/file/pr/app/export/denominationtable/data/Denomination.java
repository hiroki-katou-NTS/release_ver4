/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.denominationtable.data;

/**
 * The Enum Denomination.
 */
public enum Denomination {
	
	/** The ten thousand yen. */
	TEN_THOUSAND_YEN(0, 10000, "一万円札"),
	
	/** The five thousand yen. */
	FIVE_THOUSAND_YEN(1, 5000, "五千円札"),
	
	/** The two thousand yen. */
	TWO_THOUSAND_YEN(2, 2000, "二千円札"),
	
	/** The one thousand yen. */
	ONE_THOUSAND_YEN(3, 1000, "千円札"),
	
	/** The five hundred yen. */
	FIVE_HUNDRED_YEN(4, 500, "五百円玉"),
	
	/** The one hundred yen. */
	ONE_HUNDRED_YEN(5, 100, "百円玉"),
	
	/** The fifty yen. */
	FIFTY_YEN(6, 50, "五十円玉"),
	
	/** The ten yen. */
	TEN_YEN(7, 10, "十円玉"),
	
	/** The five yen. */
	FIVE_YEN(8, 5, "五円玉"),
	
	/** The one yen. */
	ONE_YEN(9, 1, "一円玉");
	
	/** The value. */
	public final Integer value;
	
	/** The deno. */
	public final Integer deno;
	
	/** The description. */
	public final String description;
	
	/**
	 * Instantiates a new denomination.
	 *
	 * @param value the value
	 * @param deno the deno
	 * @param description the description
	 */
	private Denomination(Integer value, Integer deno, String description){
		this.value = value;
		this.deno = deno;
		this.description = description;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	int getvalue(){
		return value;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	String getDescription(){
		return description;
	}

}
