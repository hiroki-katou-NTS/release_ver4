/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.wageledger.data.share;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nts.gul.collection.CollectionUtil;

/**
 * The Class ReportItemDto.
 */
@Builder
@AllArgsConstructor
public class ReportItemDto {
	
	/** The code. */
	public String code;

	/** The name. */
	public String name;
	
	/** The is show name. */
	public boolean isShowName;
	
	/** The is show value. */
	public boolean isShowValue;

	/** The monthly datas. */
	public List<MonthlyData> monthlyDatas;
	
	/** The total. */
	
	/**
	 * Gets the total.
	 *
	 * @return the total
	 */
	@Getter
	private Long total;
	
	/**
	 * Calculate total.
	 */
	public void calculateTotal() {
		this.total = this.monthlyDatas.stream().mapToLong(data -> data.amount).sum();
	}
	
	/**
	 * Checks if is zero value.
	 *
	 * @return true, if is zero value
	 */
	public boolean isZeroValue() {
		if (CollectionUtil.isEmpty(monthlyDatas)) {
			return true;
		}
		return this.monthlyDatas.stream().allMatch(data -> data.amount == 0);
	}
	
	/**
	 * Checks if is show.
	 *
	 * @return true, if is show
	 */
	public boolean isShow() {
		return !(this.isZeroValue() && !this.isShowName && !this.isShowValue);
	}
	/**
	 * Instantiates a new report item dto.
	 *
	 * @param name the name
	 * @param code the code
	 */
	public ReportItemDto(String name, String code) {
		super();
		this.monthlyDatas = new ArrayList<>();
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((monthlyDatas == null) ? 0 : monthlyDatas.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportItemDto other = (ReportItemDto) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (monthlyDatas == null) {
			if (other.monthlyDatas != null)
				return false;
		} else if (!monthlyDatas.equals(other.monthlyDatas))
			return false;
		return true;
	}
}
