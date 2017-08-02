package nts.uk.ctx.bs.person.dom.person.info.selectionitem;

import lombok.Getter;

@Getter
public class CodeNameReferenceType extends ReferenceTypeState {
	
	private TypeCode typeCode;

	private CodeNameReferenceType(String typeCode) {
		super();
		this.referenceType = ReferenceType.CODE_NAME;
		this.typeCode = new TypeCode(typeCode);
	}

	public static CodeNameReferenceType createFromJavaType(String typeCode) {
		return new CodeNameReferenceType(typeCode);
	}

}
