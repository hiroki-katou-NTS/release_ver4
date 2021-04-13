package nts.uk.ctx.at.request.dom.application.common.adapter.record.reflect;

import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import nts.arc.task.tran.AtomTask;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.scherec.application.reflect.ReflectStatusResultShare;

public interface ReflectApplicationWorkRecordAdapter {
	public Pair<ReflectStatusResultShare, Optional<AtomTask>> process(Object application, GeneralDate date,
			ReflectStatusResultShare reflectStatus);
}
