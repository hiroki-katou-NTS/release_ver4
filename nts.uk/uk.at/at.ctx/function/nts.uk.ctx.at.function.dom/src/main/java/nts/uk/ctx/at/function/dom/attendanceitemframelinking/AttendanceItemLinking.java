package nts.uk.ctx.at.function.dom.attendanceitemframelinking;

import java.util.Optional;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.function.dom.attendanceitemframelinking.enums.FrameCategory;
import nts.uk.ctx.at.function.dom.attendanceitemframelinking.enums.TypeOfItem;
import nts.uk.ctx.at.function.dom.attendanceitemframelinking.primitivevalue.FrameNo;

/**
 * 
 * @author nampt
 *勤怠項目と枠の紐付け
 */
@Getter
@EqualsAndHashCode
public class AttendanceItemLinking {

	/* 勤怠項目ID */
	private int attendanceItemId;

	/* 枠NO */
	private FrameNo frameNo;

	/* 勤怠項目の種類 */
	private TypeOfItem typeOfAttendanceItem;

	/* 枠カテゴリ */
	private FrameCategory frameCategory;
	
	/* 枠NO */
	private Optional<FrameNo> preliminaryFrameNO;

	public AttendanceItemLinking(int attendanceItemId, FrameNo frameNo, TypeOfItem typeOfAttendanceItem,
			FrameCategory frameCategory) {
		super();
		this.attendanceItemId = attendanceItemId;
		this.frameNo = frameNo;
		this.typeOfAttendanceItem = typeOfAttendanceItem;
		this.frameCategory = frameCategory;
	}

	public static AttendanceItemLinking createFromJavaType(int attendanceItemId, int frameNo, int typeOfAttendanceItem,
			int frameCategory) {
		return new AttendanceItemLinking(attendanceItemId, new FrameNo(frameNo),
				EnumAdaptor.valueOf(typeOfAttendanceItem, TypeOfItem.class),
				EnumAdaptor.valueOf(frameCategory, FrameCategory.class));
	}

	public AttendanceItemLinking(int attendanceItemId, FrameNo frameNo, TypeOfItem typeOfAttendanceItem,
			FrameCategory frameCategory, FrameNo preliminaryFrameNO) {
		super();
		this.attendanceItemId = attendanceItemId;
		this.frameNo = frameNo;
		this.typeOfAttendanceItem = typeOfAttendanceItem;
		this.frameCategory = frameCategory;
		this.preliminaryFrameNO = Optional.ofNullable(preliminaryFrameNO);
	}
}
