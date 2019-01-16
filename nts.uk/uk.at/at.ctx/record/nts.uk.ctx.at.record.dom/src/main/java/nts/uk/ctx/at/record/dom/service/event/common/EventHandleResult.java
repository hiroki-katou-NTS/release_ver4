package nts.uk.ctx.at.record.dom.service.event.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EventHandleResult<T> {

	private EventHandleAction action;

	private T data;

	public static <T> EventHandleResult<T> withResult(EventHandleAction action, T data) {
		return new EventHandleResult<T>(action, data);
	}

	public static <T> EventHandleResult<T> onFail() {
		return new EventHandleResult<T>(EventHandleAction.ABORT, null);
	}

	@AllArgsConstructor
	public enum EventHandleAction {

		DELETE(1, "DELETE"), UPDATE(2, "UPDATE"), INSERT(3, "INSERT"), ABORT(4, "ABORT");

		final int value;

		final String name;
	}
}