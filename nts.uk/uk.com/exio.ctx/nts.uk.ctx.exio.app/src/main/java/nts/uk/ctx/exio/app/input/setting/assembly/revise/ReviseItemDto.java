package nts.uk.ctx.exio.app.input.setting.assembly.revise;

import java.util.Optional;

import nts.uk.ctx.exio.dom.input.setting.assembly.revise.type.time.*;
import org.apache.commons.lang3.BooleanUtils;

import lombok.Data;
import lombok.Value;
import lombok.val;
import nts.uk.ctx.exio.dom.input.domain.ImportingDomainId;
import nts.uk.ctx.exio.dom.input.importableitem.ItemType;
import nts.uk.ctx.exio.dom.input.setting.ExternalImportCode;
import nts.uk.ctx.exio.dom.input.setting.assembly.revise.ReviseItem;
import nts.uk.ctx.exio.dom.input.setting.assembly.revise.ReviseValue;
import nts.uk.ctx.exio.dom.input.setting.assembly.revise.codeconvert.ExternalImportCodeConvert;
import nts.uk.ctx.exio.dom.input.setting.assembly.revise.type.date.DateRevise;
import nts.uk.ctx.exio.dom.input.setting.assembly.revise.type.date.ExternalImportDateFormat;
import nts.uk.ctx.exio.dom.input.setting.assembly.revise.type.integer.IntegerRevise;
import nts.uk.ctx.exio.dom.input.setting.assembly.revise.type.real.DecimalDigitNumber;
import nts.uk.ctx.exio.dom.input.setting.assembly.revise.type.real.RealRevise;
import nts.uk.ctx.exio.dom.input.setting.assembly.revise.type.string.Padding;
import nts.uk.ctx.exio.dom.input.setting.assembly.revise.type.string.PaddingLength;
import nts.uk.ctx.exio.dom.input.setting.assembly.revise.type.string.PaddingMethod;
import nts.uk.ctx.exio.dom.input.setting.assembly.revise.type.string.StringRevise;

@Value
public class ReviseItemDto {

	private String settingCode;
	private int itemNo;
	private RevisingValue revisingValue;

	public static ReviseItemDto of(ReviseItem domain) {

		return new ReviseItemDto(
				domain.getSettingCode().v(),
				domain.getItemNo(),
				RevisingValue.of(domain.getRevisingValue()));
	}
	
	public ReviseItem toDomain(String companyId, int domainId,  ItemType itemType) {
		
		return new ReviseItem(
				companyId,
				new ExternalImportCode(settingCode),
				ImportingDomainId.valueOf(domainId),
				itemNo,
				revisingValue.toDomain(itemType));
	}
	
	@Data
	public static class RevisingValue {
		
		// ?????????
		private Boolean usePadding;
		private Integer paddingLength;
		private int paddingMethod = -1;
		
		// ?????????????????????
		private Boolean useCodeConvert;
		private CodeConvertDto codeConvert = CodeConvertDto.empty();
		
		// ?????????
		private Boolean isDecimalization;
		private Integer decimalizationLength;
		
		// ?????????????????????
		private int timeHourlySegment = -1;
		private int timeBaseNumber = -1;
		private int timeDelimiter = -1;
		private int timeRounding = -1;
		
		// ?????????
		private int dateFormat = -1;

		
		public RevisingValue() {
		}
		public RevisingValue(
				Boolean usePadding,
				Integer paddingLength,
				int paddingMethod,
				Boolean useCodeConvert,
				CodeConvertDto codeConvert,
				Boolean isDecimalization,
				Integer decimalizationLength,
				int timeHourlySegment,
				int timeBaseNumber,
				int timeDelimiter,
				int timeRounding,
				int dateFormat) {

			this.usePadding = usePadding;
			this.paddingLength = paddingLength;
			this.paddingMethod = paddingMethod;
			this.useCodeConvert = useCodeConvert;
			this.codeConvert = codeConvert;
			this.isDecimalization = isDecimalization;
			this.decimalizationLength = decimalizationLength;
			this.timeHourlySegment = timeHourlySegment;
			this.timeBaseNumber = timeBaseNumber;
			this.timeDelimiter = timeDelimiter;
			this.timeRounding = (timeRounding == 0) ? -1 : timeRounding;
			this.dateFormat = dateFormat;
		}

		public void setTimeRounding(int timeRounding) {
			this.timeRounding = (timeRounding == 0) ? -1 : timeRounding;
		}
		
		static RevisingValue of(ReviseValue revisingValue) {
			
			val dto = new RevisingValue();
			
			if (revisingValue instanceof StringRevise) {
				val rev = (StringRevise) revisingValue;
				dto.usePadding = rev.isUsePadding();
				dto.paddingLength = rev.getPadding().map(v -> v.getLength().v()).orElse(null);
				dto.paddingMethod = rev.getPadding().map(v -> v.getMethod().value).orElse(-1);
				rev.getCodeConvert().ifPresent(c -> {
					dto.useCodeConvert = true;
					dto.codeConvert = CodeConvertDto.of(c);
				});
				return dto;
			}
			
			if (revisingValue instanceof IntegerRevise) {
				val rev = (IntegerRevise) revisingValue;
				dto.useCodeConvert = rev.getCodeConvert().isPresent();
				rev.getCodeConvert().ifPresent(c -> {
					dto.useCodeConvert = true;
					dto.codeConvert = CodeConvertDto.of(c);
				});
				return dto;
			}
			
			if (revisingValue instanceof RealRevise) {
				val rev = (RealRevise) revisingValue;
				dto.isDecimalization = rev.isDecimalization();
				dto.decimalizationLength = rev.getLength().map(l -> l.v()).orElse(null);
				return dto;
			}
			
			if (revisingValue instanceof TimeRevise) {
				val rev = (TimeRevise) revisingValue;
				dto.timeHourlySegment = rev.getHourly().value;
				dto.timeBaseNumber = rev.getBaseNumber().map(d -> d.value).orElse(-1);
				dto.timeDelimiter = rev.getDelimiter().map(d -> d.value).orElse(-1);
				dto.timeRounding = rev.getRounding().map(d -> d.value).orElse(-1);
				return dto;
			}
			
			if (revisingValue instanceof DateRevise) {
				val rev = (DateRevise) revisingValue;
				dto.dateFormat = rev.getDateFormat().value;
				return dto;
			}
			
			throw new RuntimeException("unknown: " + revisingValue);
		}
		
		public ReviseValue toDomain(ItemType itemType) {
			
			switch (itemType) {
			case STRING:
				return toDomainStringRevise();
			case INT:
				return toDomainIntegerRevise();
			case REAL:
				return toDomainRealRevise();
			case DATE:
				return toDomainDateRevise();
			case TIME_DURATION:
				return toDomainTimeDurationRevise();
			case TIME_POINT:
				return toDomainTimePointRevise();
			default:
				throw new RuntimeException("unknown: " + itemType);
			}
		}
		
		private StringRevise toDomainStringRevise() {
			
			if ((usePadding == null || paddingLength == null || paddingMethod == -1)
					&& BooleanUtils.isNotTrue(useCodeConvert)) {
				return null;
			}
			
			return new StringRevise((usePadding == null ? false : usePadding), toDomainPadding(), toDomainCodeConvert());
		}
		
		private IntegerRevise toDomainIntegerRevise() {
			
			return new IntegerRevise(toDomainCodeConvert());
		}

		private RealRevise toDomainRealRevise() {
			
			if (isDecimalization == null || decimalizationLength == null) {
				return null;
			}
			
			return new RealRevise(isDecimalization, toDomainDecimalDigitNumber());
		}

		private DateRevise toDomainDateRevise() {
			
			if (dateFormat == -1) {
				return null;
			}
			
			return new DateRevise(ExternalImportDateFormat.valueOf(dateFormat));
		}

		private TimeRevise toDomainTimeDurationRevise() {
			
			if (timeHourlySegment == -1) {
				return null;
			}
			
			return new TimeDurationRevise(
					HourlySegment.valueOf(timeHourlySegment),
					timeBaseNumber != -1 ? Optional.of(TimeBaseNumber.valueOf(timeBaseNumber)) : Optional.empty(),
					timeDelimiter != -1 ? Optional.of(TimeBase60Delimiter.valueOf(timeDelimiter)) : Optional.empty(),
					timeRounding != -1 ? Optional.of(TimeBase10Rounding.valueOf(timeRounding)) : Optional.empty());
		}

		private TimeRevise toDomainTimePointRevise() {

			if (timeHourlySegment == -1) {
				return null;
			}

			return new TimePointRevise(
					HourlySegment.valueOf(timeHourlySegment),
					timeBaseNumber != -1 ? Optional.of(TimeBaseNumber.valueOf(timeBaseNumber)) : Optional.empty(),
					timeDelimiter != -1 ? Optional.of(TimeBase60Delimiter.valueOf(timeDelimiter)) : Optional.empty(),
					timeRounding != -1 ? Optional.of(TimeBase10Rounding.valueOf(timeRounding)) : Optional.empty());
		}
		
		private Optional<Padding> toDomainPadding() {

			if (BooleanUtils.isNotTrue(usePadding)) {
				return Optional.empty();
			}
			
			return Optional.of(new Padding(
					new PaddingLength(paddingLength),
					PaddingMethod.valueOf(paddingMethod)));
		}
		
		private Optional<ExternalImportCodeConvert> toDomainCodeConvert() {
			
			if (BooleanUtils.isNotTrue(useCodeConvert) || codeConvert.getDetails().size() == 0) {
				return Optional.empty();
			}
			
			return Optional.of(codeConvert.toDomain());
		}
		
		private Optional<DecimalDigitNumber> toDomainDecimalDigitNumber() {
			
			if (BooleanUtils.isNotTrue(isDecimalization)) {
				return Optional.empty();
			}
			
			return Optional.of(new DecimalDigitNumber(decimalizationLength));
		}
	}
}
