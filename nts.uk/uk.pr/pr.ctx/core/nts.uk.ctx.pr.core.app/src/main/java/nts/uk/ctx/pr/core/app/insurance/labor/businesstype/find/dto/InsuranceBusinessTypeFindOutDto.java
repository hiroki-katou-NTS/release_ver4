/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.businesstype.find.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.BusinessTypeEnum;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.InsuranceBusinessType;

/**
 * Instantiates a new insurance business type find out dto.
 */
@Getter
@Setter
public class InsuranceBusinessTypeFindOutDto {

	/** The biz name biz 1 st. */
	private String bizNameBiz1St;

	/** The biz name biz 2 nd. */
	private String bizNameBiz2Nd;

	/** The biz name biz 3 rd. */
	private String bizNameBiz3Rd;

	/** The biz name biz 4 th. */
	private String bizNameBiz4Th;

	/** The biz name biz 5 th. */
	private String bizNameBiz5Th;

	/** The biz name biz 6 th. */
	private String bizNameBiz6Th;

	/** The biz name biz 7 th. */
	private String bizNameBiz7Th;

	/** The biz name biz 8 th. */
	private String bizNameBiz8Th;

	/** The biz name biz 9 th. */
	private String bizNameBiz9Th;

	/** The biz name biz 10 th. */
	private String bizNameBiz10Th;

	/**
	 * From domain.
	 *
	 * @param domain
	 *            the domain
	 * @return the insurance business type find out dto
	 */
	public static InsuranceBusinessTypeFindOutDto fromDomain(List<InsuranceBusinessType> domains) {

		InsuranceBusinessTypeFindOutDto insuranceBusinessTypeFindOutDto = new InsuranceBusinessTypeFindOutDto();

		Map<BusinessTypeEnum, Consumer<String>> ENUM_SET_FUNCTION_MAP = new HashMap<>();
		ENUM_SET_FUNCTION_MAP.put(BusinessTypeEnum.Biz1St, insuranceBusinessTypeFindOutDto::setBizNameBiz1St);
		ENUM_SET_FUNCTION_MAP.put(BusinessTypeEnum.Biz2Nd, insuranceBusinessTypeFindOutDto::setBizNameBiz2Nd);
		ENUM_SET_FUNCTION_MAP.put(BusinessTypeEnum.Biz3Rd, insuranceBusinessTypeFindOutDto::setBizNameBiz3Rd);
		ENUM_SET_FUNCTION_MAP.put(BusinessTypeEnum.Biz4Th, insuranceBusinessTypeFindOutDto::setBizNameBiz4Th);
		ENUM_SET_FUNCTION_MAP.put(BusinessTypeEnum.Biz5Th, insuranceBusinessTypeFindOutDto::setBizNameBiz5Th);
		ENUM_SET_FUNCTION_MAP.put(BusinessTypeEnum.Biz6Th, insuranceBusinessTypeFindOutDto::setBizNameBiz6Th);
		ENUM_SET_FUNCTION_MAP.put(BusinessTypeEnum.Biz7Th, insuranceBusinessTypeFindOutDto::setBizNameBiz7Th);
		ENUM_SET_FUNCTION_MAP.put(BusinessTypeEnum.Biz8Th, insuranceBusinessTypeFindOutDto::setBizNameBiz8Th);
		ENUM_SET_FUNCTION_MAP.put(BusinessTypeEnum.Biz9Th, insuranceBusinessTypeFindOutDto::setBizNameBiz9Th);
		
		ENUM_SET_FUNCTION_MAP.put(BusinessTypeEnum.Biz10Th,
			insuranceBusinessTypeFindOutDto::setBizNameBiz10Th);

		domains.stream().forEach(item -> {
			ENUM_SET_FUNCTION_MAP.get(item.getBizOrder()).accept(item.getBizName().v());
		});

		return insuranceBusinessTypeFindOutDto;

	}
}
