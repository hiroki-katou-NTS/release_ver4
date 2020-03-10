package nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScheAndRecordIsReflect {
	private boolean scheReflect;
	private boolean recordReflect;
	/*
	 * True:本人確認なし、False：　本人確認
	 */
	private boolean honninKakunin;
}
