package nts.uk.shr.com.security.audittrail.correction.content;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nts.arc.time.GeneralDate;

/**
 * 項目情報
 */
@RequiredArgsConstructor
public class ItemInfo {

	/** 項目名 */
	@Getter
	private final String name;
	
	/** 修正前 */
	@Getter
	private final Value valueBefore;
	
	/** 修正後 */
	@Getter
	private final Value valueAfter;
	
	public static ItemInfo create(String name, DataValueAttribute attr, Object valueBefore, Object valueAfter) {
		return new ItemInfo(name, Value.create(valueBefore, attr), Value.create(valueBefore, attr));
	}
	
	@RequiredArgsConstructor
	@Getter
	public static class Value {
		
		/**
		 * 整形前の値。記録として残すためだけのもので、表示には使わない。
		 * 将来、データの復元などに利用する可能性がある。
		 */
		private final RawValue rawValue;
		
		/** 表示用に整形された値。画面に表示するときにはこちらを見れば良い。 */
		private final String viewValue;
	
		static Value create(Object value, DataValueAttribute attr) {
			
			switch (attr) {
			case COUNT:
				return new Value(
						RawValue.asInteger((Integer) value),
						value.toString());
				
			default:
				throw new RuntimeException("invalid attribute: " + attr);
			}
		}
	}
	
	@RequiredArgsConstructor
	@Getter
	public static class RawValue {
		
		private final Type type;
		private final Object value;
		
		public static RawValue asString(String value) {
			return new RawValue(Type.STRING, value);
		}
		
		public static RawValue asInteger(Integer value) {
			return new RawValue(Type.INTEGER, value);
		}
		
		public static RawValue asDouble(Double value) {
			return new RawValue(Type.DOUBLE, value);
		}
		
		public static RawValue asDecimal(BigDecimal value) {
			return new RawValue(Type.DECIMAL, value);
		}
		
		public static RawValue asDate(GeneralDate value) {
			return new RawValue(Type.DATE, value);
		}
		
		@RequiredArgsConstructor
		public enum Type {
			STRING(1),
			INTEGER(2),
			DOUBLE(3),
			DECIMAL(4),
			DATE(5),
			;
			public final int value;
		}
	}
}
