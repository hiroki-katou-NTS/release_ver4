package nts.uk.ctx.at.function.app.command.alarmworkplace.alarmlist;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.task.data.TaskDataSetter;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.adapter.workplace.WorkPlaceInforExport;
import nts.uk.ctx.at.function.dom.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.at.function.dom.adapter.workplace.WorkplaceIdName;
import nts.uk.ctx.at.function.dom.alarm.AlarmPatternCode;
import nts.uk.ctx.at.function.dom.alarmworkplace.AlarmPatternSettingWorkPlace;
import nts.uk.ctx.at.function.dom.alarmworkplace.AlarmPatternSettingWorkPlaceRepository;
import nts.uk.ctx.at.function.dom.alarmworkplace.AlarmPermissionSetting;
import nts.uk.ctx.at.function.dom.alarmworkplace.extractprocessstatus.AlarmListExtractProcessStatusWorkplace;
import nts.uk.ctx.at.function.dom.alarmworkplace.extractprocessstatus.AlarmListExtractProcessStatusWorkplaceRepository;
import nts.uk.ctx.at.function.dom.alarmworkplace.extractprocessstatus.ExtractState;
import nts.uk.ctx.at.function.dom.alarmworkplace.extractresult.AlarmListExtractInfoWorkplace;
import nts.uk.ctx.at.function.dom.alarmworkplace.extractresult.dto.AlarmListExtractResultWorkplaceDto;
import nts.uk.ctx.at.function.dom.alarmworkplace.service.aggregateprocess.AggregateProcessService;
import nts.uk.ctx.at.function.dom.alarmworkplace.service.aggregateprocess.PeriodByAlarmCategory;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * UKDesign.UniversalK.就業.KAL_アラームリスト.KAL011_アラームリスト(職場別).D：進捗ダイアログ.アルゴリズム.アラームリストを出力する
 * アラームリストを出力する
 *
 * @author Le Huu Dat
 */
@Stateless
public class ExtractAlarmListWorkPlaceCommandHandler extends AsyncCommandHandler<ExtractAlarmListWorkPlaceCommand> {

    @Inject
    private AlarmPatternSettingWorkPlaceRepository alarmPatternSettingWorkPlaceRepo;
    @Inject
    private AggregateProcessService aggregateProcessService;
    @Inject
    private AlarmListExtractProcessStatusWorkplaceRepository alarmListExtractProcessStatusWorkplaceRepo;
    @Inject
    private WorkplaceAdapter workplaceAdapter;

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    protected void handle(CommandHandlerContext<ExtractAlarmListWorkPlaceCommand> context) {
        val asyncContext = context.asAsync();
        String cid = AppContexts.user().companyId();
        ExtractAlarmListWorkPlaceCommand command = context.getCommand();
        TaskDataSetter dataSetter = asyncContext.getDataSetter();
        AtomicInteger counter = new AtomicInteger(0);

        // ログイン者IDの権限をチェック
        if (!hasAuthorityToExecute(cid, command.getAlarmPatternCode())) {
            throw new BusinessException("Msg_504");
        }

        dataSetter.setData("ctgCount", counter.get());
        dataSetter.setData("ctgTotal", counter.get());
        // 集計処理
        List<AlarmListExtractInfoWorkplace> alExtractInfos = aggregateProcessService.process(cid,
                command.getAlarmPatternCode(), command.getWorkplaceIds(), convertPeriods(command.getCategoryPeriods()),
                total -> dataSetter.updateData("ctgTotal", total),
                finished -> {
                    counter.set(counter.get() + finished);
                    dataSetter.updateData("ctgCount", counter.get());
                },
                () -> shouldStop(cid, context, asyncContext));
        // [No.560]職場IDから職場の情報をすべて取得する
        List<WorkPlaceInforExport> wpInfos = workplaceAdapter.getWorkplaceInforByWkpIds(cid, command.getWorkplaceIds(), GeneralDate.today());

        // 取得した職場情報一覧から「アラーム抽出結果（職場別）」にデータをマッピングする
    }

    /**
     * UKDesign.UniversalK.就業.KAL_アラームリスト.KAL011_アラームリスト(職場別).D：進捗ダイアログ.アルゴリズム.アラームリストを出力する.ログイン者IDの権限をチェック
     * ログイン者IDの権限をチェック
     *
     * @param cid              会社ID
     * @param alarmPatternCode アラームパターンコード
     * @return 実行できるか権限
     */
    private boolean hasAuthorityToExecute(String cid, String alarmPatternCode) {
        boolean hasAuthority = false;

        //ドメインモデル「アラームリストパターン設定(職場別)」を取得
        Optional<AlarmPatternSettingWorkPlace> patternOpt = alarmPatternSettingWorkPlaceRepo.getBy(cid, new AlarmPatternCode(alarmPatternCode));
        if (!patternOpt.isPresent()) {
            throw new RuntimeException("「アラームリストパターン設定(職場別) 」が見つかりません！");
        }
        AlarmPermissionSetting alarmPerSet = patternOpt.get().getAlarmPerSet();

        // 取得したアラームリストパターン設定．実行権限．権限設定を使用するをチェック
        if (!alarmPerSet.isAuthSetting()) {
            hasAuthority = true;
        }

        // ロールをチェック
        String roleId = AppContexts.user().roles().forAttendance();
        if (alarmPerSet.getRoleIds().stream().anyMatch(x -> x.equals(roleId))) {
            hasAuthority = true;
        }

        return hasAuthority;
    }

    private List<PeriodByAlarmCategory> convertPeriods(List<CategoryPeriodCommand> categoryPeriods) {
        List<PeriodByAlarmCategory> periods = categoryPeriods.stream()
                .map(x -> new PeriodByAlarmCategory(x.getCategory(), x.getStart(), x.getEnd()))
                .collect(Collectors.toList());
        return periods;
    }

    private Boolean shouldStop(String cid, CommandHandlerContext<ExtractAlarmListWorkPlaceCommand> context,
                               AsyncCommandHandlerContext<ExtractAlarmListWorkPlaceCommand> asyncContext) {
        Optional<AlarmListExtractProcessStatusWorkplace> processStatus = this.alarmListExtractProcessStatusWorkplaceRepo
                .getBy(cid, context.getCommand().getProcessStatusId());
        if (processStatus.isPresent()
                && processStatus.get().getStatus() == ExtractState.INTERRUPTION) {
            asyncContext.finishedAsCancelled();
            return true;
        }
        return false;
    }

    private List<AlarmListExtractResultWorkplaceDto> mapToDto(List<AlarmListExtractInfoWorkplace> alExtractInfos,
                                                              List<WorkPlaceInforExport> wpInfos){
        
        return null;
    }
}