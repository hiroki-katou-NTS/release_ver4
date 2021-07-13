/// <reference path='../../../../lib/nittsu/viewcontext.d.ts' />

module nts.uk.at.view.kbt002.f {

  const API = {
    getExecItemInfoList: "at/function/processexec/getExecItemInfoList",
    getExecItemInfo: "at/function/processexec/getExecItemInfo/{0}",
    execute: 'at/function/processexec/execute',
    terminate: 'at/function/processexec/terminate',
    changeSetting: 'at/function/processexec/changeSetting',
  };

  const DATETIME_FORMAT = 'YY/M/D HH:mm:ss';
  const DOM_DATA_VALUE = 'data-value';
  const SELECTED_CLASS = "selected";

  @bean()
  export class KBT002FViewModel extends ko.ViewModel {
    $grid!: JQuery;

    dataSource: KnockoutObservableArray<any> = ko.observableArray([]);
    dataSourceModel: KnockoutObservableArray<ExecutionItemInfomationModel> = ko.observableArray([]);
    selectedExecCd: KnockoutObservable<string> = ko.observable('');
    isOnceMessage101: KnockoutObservable<boolean> = ko.observable(true);
    isOnceExecType: KnockoutObservable<boolean> = ko.observable(true);
    isOnceCurrentStatus: KnockoutObservable<boolean> = ko.observable(true);
    isOnceInterupt: KnockoutObservable<boolean> = ko.observable(true);
    isCreateSchedule: KnockoutObservable<boolean> = ko.observable(true);
    taskTerminate: KnockoutObservable<any> = ko.observable("");

    mounted() {
      const vm = this;
      vm.$grid = $("#F2_1");
      vm.getExecItemInfoList();
      // data binding
      $(document).on("click", ".button-process-start", function () {
        const key = $(this).attr(DOM_DATA_VALUE);
        vm.executeUpdateProcess(key);
      });
      $(document).on("click", ".button-process-stop", function () {
        const key = $(this).attr(DOM_DATA_VALUE);
        vm.stopUpdateProcess(key);
      });
      $(document).on("click", ".button-open-g", function () {
        const key = $(this).attr(DOM_DATA_VALUE);
        vm.openDialogG(key);
      });
      $(document).on("click", ".button-open-h", function () {
        const key = $(this).attr(DOM_DATA_VALUE);
        vm.openDialogH(key);
      });
      $(document).on("click", "div[class^='nts-grid-control-isTaskExecution']", function () {
        const className = $(this).attr("class");
        const regex = /.*-([0-9]+)/;
        const key = regex.exec(className)[1];
        vm.changeSetting(key, $(this));
      });
    }

    /**
     * アルゴリズム「起動時処理」を実行する
     */
    private getExecItemInfoList(): JQueryPromise<any> {
      const vm = this;
      vm.$blockui('grayout');
      return vm.$ajax(API.getExecItemInfoList)
        .then((response) => {
          vm.$blockui('clear');
          vm.dataSource(response);
          const dataSourceModel = _.map(response, (item) => new ExecutionItemInfomationModel(vm, item));
          vm.dataSourceModel(dataSourceModel);
        })
        .fail(err => vm.$dialog.error({ messageId: err.messageId }))
        .always(() => {
          vm.$blockui("clear");
          // Render grid list
          vm.initGridList();
        });
    }

    private getExecItemInfo(execItemCd: string): JQueryPromise<any> {
      const vm = this;
      return vm.$ajax(nts.uk.text.format(API.getExecItemInfo, execItemCd))
        .then(res => {
          const data = _.find(vm.dataSourceModel(), { execItemCd: res.execItemCd });
          const index = vm.dataSourceModel().indexOf(data);
          vm.dataSourceModel().splice(index, 1, new ExecutionItemInfomationModel(vm, res));
          vm.dataSource().splice(_.findIndex(vm.dataSource(), { execItemCd: res.execItemCd }), 1, res);
          vm.initGridList(index);
        });
    }

    private initGridList(index: number = null) {
      const vm = this;
      if (vm.$grid.data("igGrid")) {
        vm.$grid.ntsGrid("destroy");
      }
      vm.$nextTick(() => {
        vm.$grid.ntsGrid({
          name: '#[KBT002_126]',
          width: "1235px",
          height: "450px",
          dataSource: vm.dataSourceModel(),
          primaryKey: 'execItemCd',
          columns: [
            { headerText: '', key: 'execItemCd', hidden: true },
            {
              headerText: vm.$i18n("KBT002_201"),
              key: 'execItem',
              width: 150,
              formatter: (value: string, record: ExecutionItemInfomationModel) => {
                return `<span class="limited-label" style="white-space: nowrap;">${_.escape(value)}</span>`;
              }
            },
            {
              headerText: "",
              key: 'showWarningIcon',
              width: 30,
              formatter: (value: number, record: ExecutionItemInfomationModel) => {
                if (value === 0) {
                  return `<div class="cell-center"><i class="icon-warning" data-bind="ntsIcon: { no: 228, width: 20, height: 20 }" title="${vm.$i18n("KBT002_314")}" tabindex="4"></i></div>`;
                } else if (value === 1) {
                  return `<div class="cell-center"><i class="icon-warning" data-bind="ntsIcon: { no: 228, width: 20, height: 20 }" title="${vm.$i18n("KBT002_315")}" tabindex="4"></i></div>`;
                } else {
                  return `<div class="cell-center"><i class="icon-warning"></i></div>`;
                }
              }
            },
            {
              headerText: vm.$i18n("KBT002_121"),
              key: 'execStatus',
              width: 70,
              formatter: (value: number, record: ExecutionItemInfomationModel) => {
                if (value === 0) {
                  // 現在の実行状態 = 実行中
                  return `<div class="color-holiday">${vm.$i18n('KBT002_265')}</div>`;
                } else if (value === 1) {
                  // 現在の実行状態 = 待機中
                  return `<div class="color-daily-extra-holiday">${vm.$i18n('KBT002_266')}</div>`;
                } else if (value === 2) {
                  // 現在の実行状態 = 無効
                  return `<div class="color-weekdays">${vm.$i18n('KBT002_124')}</div>`;
                } else {
                  // 現在の実行状態 = empty
                  return "";
                }
              }
            },
            { headerText: vm.$i18n("KBT002_260"), key: 'lastStartDateTime', width: 150, formatter: _.escape },
            { headerText: vm.$i18n("KBT002_261"), key: 'lastEndDateTime', width: 150, formatter: _.escape },
            { headerText: vm.$i18n("KBT002_204"), key: 'processingTime', width: 80, formatter: _.escape },
            {
              headerText: vm.$i18n("KBT002_143"),
              key: 'overallStatus',
              width: 90,
              formatter: (value: number, record: ExecutionItemInfomationModel) => {
                if (value === 0) {
                  // 全体の終了状態 = 未実施
                  return `<div class="color-daily-extra-holiday">${vm.$i18n('KBT002_267')}</div>`;
                } else if (value === 1) {
                  // 全体の終了状態 = 完了
                  return `<div class="color-working-schedule">${vm.$i18n('KBT002_268')}</div>`;
                } else if (value === 2) {
                  // 全体の終了状態 =強制終了
                  return `<div class="color-holiday">${vm.$i18n('KBT002_258')}</div>`;
                } else if (value === 3) {
                  // 全体の終了状態 = 終了中
                  return `<div class="color-holiday">${vm.$i18n('KBT002_269')}</div>`;
                } else {
                  // 全体の終了状態 = empty
                  return "";
                }
              }
            },
            { headerText: vm.$i18n("KBT002_206"), key: 'systemError', width: 80, formatter: _.escape },
            { headerText: vm.$i18n("KBT002_207"), key: 'businessError', width: 80, formatter: _.escape },
            {
              headerText: vm.$i18n("KBT002_144"),
              key: 'execItemCd',
              width: 50,
              formatter: (value: any, record: ExecutionItemInfomationModel) => {
                const enabled = (record.execStatus === 1 || record.execStatus === 2) && (record.overallStatus !== 0 && record.overallStatus !== 3);
                const $button = $("<button>", { "class": "setting small button-open-g", "tabindex": 5, "disabled": !enabled, "text": vm.$i18n("KBT002_144") });
                $button.attr(DOM_DATA_VALUE, record["execItemCd"]);
                return $button[0].outerHTML;
              }
            },
            { headerText: vm.$i18n("KBT002_131"), key: 'nextExecDate', width: 150, formatter: _.escape },
            {
              headerText: vm.$i18n("KBT002_257"),
              key: 'execItemCd',
              width: 70,
              formatter: (value: number, record: ExecutionItemInfomationModel) => {
                const enabled: boolean = (record.execStatus === 1 || record.execStatus === 2);
                const $button = $("<button>", { "class": "btn-center setting small button-process-start", "tabindex": 6, "disabled": !enabled });
                $button.attr(DOM_DATA_VALUE, record["execItemCd"]);
                $button.append(`<i class="img-icon icon-start" data-bind="ntsIcon: { no: 226, width: 25, height: 25 }"></i>${vm.$i18n('KBT002_262')}`);
                return $button[0].outerHTML;
              }
            },
            {
              headerText: vm.$i18n("KBT002_258"),
              key: 'execItemCd',
              width: 70,
              formatter: (value: number, record: ExecutionItemInfomationModel) => {
                const enabled: boolean = (record.execStatus === 0);
                const $button = $("<button>", { "class": "btn-center setting small button-process-stop", "tabindex": 7, "disabled": !enabled });
                $button.attr(DOM_DATA_VALUE, record["execItemCd"]);
                $button.append(`<i class="img-icon icon-stop" data-bind="ntsIcon: { no: 227, width: 25, height: 25 }"></i>${vm.$i18n('KBT002_133')}`);
                return $button[0].outerHTML;
              }
            },
            { headerText: vm.$i18n("KBT002_259"), key: 'isTaskExecution', width: 100, ntsControl: 'Switch' },
            {
              headerText: vm.$i18n("KBT002_147"),
              key: 'execItemCd',
              width: 70,
              formatter: (value: any, record: ExecutionItemInfomationModel) => {
                const enabled = !!(record.lastStartDateTime);
                const $button = $("<button>", { "class": "setting small button-open-h", "tabindex": 9, "disabled": !enabled, "text": vm.$i18n("KBT002_147") });
                $button.attr(DOM_DATA_VALUE, record["execItemCd"]);
                return $button[0].outerHTML;
              }
            },
          ],
          features: [
            {
              name: "Selection",
              mode: "row",
              multipleSelection: false,
              activation: false,
              rowSelectionChanged: (event: any, ui: any) => vm.selectedExecCd(ui.row.id),
            },
          ],
          ntsControls: [
            {
              name: 'Switch',
              options: [
                { value: true, text: vm.$i18n('KBT002_123') },
                { value: false, text: vm.$i18n('KBT002_124') }
              ],
              optionsValue: 'value',
              optionsText: 'text',
              controlType: 'SwitchButtons'
            },
          ],
          virtualization: true,
          virtualizationMode: 'continuous',
        });

        vm.$grid.ready(function () {
          _.forEach(vm.dataSource(), item => {
            const $switch = $(`.nts-grid-control-isTaskExecution-${nts.uk.text.padLeft(item.execItemCd, '0', 2)}`);
            // Add tabindex for switch
            $switch.find("button:first-child()").attr("tabindex", "8");
            // Disable F3_7
            if (!item.executionTaskSetting) {
              $switch.find("button").attr("disabled", "disabled");
              $switch.attr("tabindex", "-1");
            }

            
          });
          // Force rebind
          for (var i = 0; i < vm.dataSource().length; i++) {
            ko.applyBindings(vm, $("#F2_1 .icon-warning")[i]);
            ko.applyBindings(vm, $("#F2_1 .icon-start")[i]);
            ko.applyBindings(vm, $("#F2_1 .icon-stop")[i]);
          }
          vm.$grid.igGrid("virtualScrollTo", index);
        });
      });
    }

    /**
     * 更新処理を即時実行する
     */
    private executeUpdateProcess(execItemCd: string) {
      const vm = this;
      const selectedItem: any = _.find(vm.dataSource(), (item) => item.execItemCd === execItemCd);
      // if (selectedItem && selectedItem.updateProcessAutoExecLog) {
        vm.$blockui("grayout");
        const command: ExecuteProcessExecutionCommand = new ExecuteProcessExecutionCommand({
          companyId: selectedItem.updateProcessAutoExec.companyId,
          execItemCd: selectedItem.updateProcessAutoExec.execItemCode,
          execId: selectedItem.updateProcessAutoExecLog ? selectedItem.updateProcessAutoExecLog.execId : null,
          execType: 1,
        });
        vm.$ajax(API.execute, command)
          .then(res => vm.repeatCheckAsyncResult(res.id, selectedItem))
          .then(() => vm.getExecItemInfo(execItemCd))
          .fail((err) => {
            vm.$dialog.error({ messageId: err.messageId });
            return vm.getExecItemInfoList();
          })
          .always(() => vm.$blockui("clear"));
      // }
    }

    private repeatCheckAsyncResult(taskId: any, selectedItem: any) {
      const vm = this;
      // Wait for maximum 3 sec
      var delay = 3;
      return nts.uk.deferred.repeat(conf => conf
        .task(() => {
          return (nts.uk.request as any).asyncTask.getInfo(taskId)
            .done((res: any) => {
              //ExecuteProcessExecCommandHandler
              const message101 = vm.getAsyncData(res.taskDatas, "message1101").valueAsString;
              if (message101 === "Msg_1101" && vm.isOnceMessage101()) {
                vm.isOnceMessage101(false);
                vm.$dialog.alert({ messageId: message101 });
              }
              //TerminateProcessExecutionCommandHandler
              const currentStatusIsOneOrTwo = vm.getAsyncData(res.taskDatas, "currentStatusIsOneOrTwo").valueAsString;
              if (currentStatusIsOneOrTwo === "Msg_1102" && vm.isOnceCurrentStatus()) {
                vm.isOnceCurrentStatus(false);
                vm.$dialog.alert({ messageId: currentStatusIsOneOrTwo });
              }
              const interupt = vm.getAsyncData(res.taskDatas, "interupt").valueAsString;
              if (interupt === "true" && vm.isOnceInterupt()) {
                vm.isOnceInterupt(false);
              }
              const createSchedule = vm.getAsyncData(res.taskDatas, "createSchedule").valueAsString;
              if (createSchedule === "done" && vm.isCreateSchedule()) {
                vm.isCreateSchedule(false);

              }
              const task = vm.getAsyncData(res.taskDatas, "taskId").valueAsString;
              if (!nts.uk.text.isNullOrEmpty(task)) {
                vm.taskTerminate(task);
              }
            })
            .fail((err: any) => vm.$dialog.error({ messageId: err.messageId }));
        })
        .while(() => {
          if (!vm.isOnceCurrentStatus() || !vm.isOnceMessage101() || !vm.isOnceInterupt()) {
            vm.isOnceCurrentStatus(true);
            vm.isOnceMessage101(true);
            vm.isOnceInterupt(true);
            return false;
          } else {
            return delay-- > 0;
          }
        })
        .pause(1000));
    }

    private getAsyncData(data: any[], key: string): any {
      const result = _.find(data, (item) => {
        return item.key === key;
      });
      return result || { valueAsString: "", valueAsNumber: 0, valueAsBoolean: false };
    }

    /**
     * 実行中の更新処理を終了する
     */
    private stopUpdateProcess(execItemCd: string) {
      const vm = this;
      const selectedItem: any = _.find(vm.dataSource(), (item) => item.execItemCd === execItemCd);
      if (selectedItem && selectedItem.updateProcessAutoExecLog) {
        vm.$blockui("grayout");
        const command: TerminateProcessExecutionCommand = new TerminateProcessExecutionCommand({
          companyId: selectedItem.updateProcessAutoExecLog.companyId,
          execItemCd: selectedItem.updateProcessAutoExecLog.execItemCd,
          execId: selectedItem.updateProcessAutoExecLog.execId,
          execType: 1,
          taskTerminate: vm.taskTerminate(),
        });
        vm.$ajax(API.terminate, command)
          .then(res => vm.repeatCheckAsyncResult(res.id, selectedItem))
          .then(() => vm.getExecItemInfo(execItemCd))
          .fail((err) => {
            vm.$dialog.error({ messageId: err.messageId });
            return vm.getExecItemInfoList();
          })
          .always(() => vm.$blockui("clear"));
      }
    }

    /**
     * 各処理の前回終了状態を見る
     */
    private openDialogG(execItemCd: string) {
      const vm = this;
      const selectedDto = _.find(vm.dataSource(), (o) => o.execItemCd === execItemCd);
      if (selectedDto) {
        const params = {
          taskLogList: selectedDto.updateProcessAutoExecLog         // 各処理の終了状態
            ? _.filter(selectedDto.updateProcessAutoExecLog.taskLogList, (item: any) => item.execId === selectedDto.updateProcessAutoExecLog.execId)
            : [], 
          overallStatus: selectedDto.updateProcessAutoExecManage ? selectedDto.updateProcessAutoExecManage.overallStatus : null, // 全体の終了状態
          overallError: selectedDto.updateProcessAutoExecManage ? selectedDto.updateProcessAutoExecManage.overallError : null, // 全体のエラー詳細
          execId: selectedDto.updateProcessAutoExecLog ? selectedDto.updateProcessAutoExecLog.execId : null, // 実行ID
          execItemCd: selectedDto.execItemCd, // 更新処理自動実行項目コード
        };
        // 画面G「前回終了状態詳細」を起動する
        vm.$blockui('grayout');
        vm.$window.modal('/view/kbt/002/g/index.xhtml', params)
          .then((result: any) => {
            // bussiness logic after modal closed
            vm.$blockui('clear');
          });
      }
    }

    /**
     * 実行履歴を見る
     */
    private openDialogH(execItemCd: string) {
      const vm = this;
      const params = { execItemCd: execItemCd };
      // H画面「実行履歴」を起動する
      vm.$blockui('grayout');
      nts.uk.ui.windows.setShared('inputDialogH', params);
      vm.$window.modal('/view/kbt/002/h/index.xhtml', params)
        .then((result: any) => {
          // bussiness logic after modal closed
          vm.$blockui('clear');
        });
    }

    /**
     * 実行タスクの設定変更する
     */
    private changeSetting(execItemCd: string, $item: JQuery) {
      const vm = this;
      const selectedItem = _.find(vm.dataSource(), { execItemCd: execItemCd });
      if ($item.find("button").filter(`.${SELECTED_CLASS}`)[0].getAttribute("data-swbtn") !== String(selectedItem.executionTaskSetting.enabledSetting)) {
        vm.$dialog.confirm({ messageId: "Msg_1846" })
        .then((result: 'no' | 'yes' | 'cancel') => {
          if (result === 'yes') {
            // logic for yes case
            if (selectedItem) {
              const command: ChangeExecutionTaskSettingCommand = new ChangeExecutionTaskSettingCommand(selectedItem.executionTaskSetting);
              command.enabledSetting = !command.enabledSetting;
              // Update date format
              if (command.startDate) {
                command.startDate = moment.utc(command.startDate, "YYYY/MM/DD");
              }
              if (command.endDate) {
                command.endDate = moment.utc(command.endDate, "YYYY/MM/DD");
              }
              vm.$ajax(API.changeSetting, command)
                .then((res) => {
                  selectedItem.executionTaskSetting = res;
                  vm.getExecItemInfo(res.execItemCd);
                })
                .fail((err) => {
                  vm.revertSwitch($item, selectedItem);
                  vm.$dialog.alert({ messageId: err.messageId });
                });
            }
          } else {
            vm.revertSwitch($item, selectedItem);
          }
        });
      }
    }

    private revertSwitch($item: JQuery, selectedItem: any) {
      const onButton = $item.find("button").filter(`.${SELECTED_CLASS}`);
      const offButton = $item.find("button").filter(`:not(.${SELECTED_CLASS})`);
      if (onButton[0].getAttribute("data-swbtn") !== String(selectedItem.executionTaskSetting.enabledSetting)) {
        onButton.removeClass(SELECTED_CLASS);
        offButton.addClass(SELECTED_CLASS);
      }
    }

    /**
     * 最新の情報に更新する
     */
    public updateLastestInfo() {
      const vm = this;
      vm.getExecItemInfoList();
    }

    /**
     * 実行履歴ログを出力する
     */
    public exportHistoryLog() {
      const vm = this;
      // 「K：実行履歴の出力」ダイアログを起動する
      vm.$blockui('grayout');
      vm.$window.modal('/view/kbt/002/k/index.xhtml')
        .then((result: any) => {
          // bussiness logic after modal closed
          vm.$blockui('clear');
        });
    }
  }

  export class ExecutionItemInfomationModel {
    execItemCd: string;
    execItem: string;
    showWarningIcon: number;
    execStatus: number;
    nextExecDate: string;
    isTaskExecution: string;
    lastStartDateTime: string;
    lastEndDateTime: string;
    processingTime: string;
    overallStatus: number;
    systemError: string;
    businessError: string;

    constructor(vm: any, dto: any) {
      // PK
      this.execItemCd = dto.execItemCd;
      // 警告アイコン
      if (dto.isOverAverageExecTime) {
        this.showWarningIcon = 0;
      } else {
        if (dto.isPastNextExecDate) {
          this.showWarningIcon = 1;
        }
      }

      if (dto.updateProcessAutoExec) {
        // 実行項目
        this.execItem = `${dto.execItemCd} ${dto.updateProcessAutoExec.execItemName}`;
      }
      if (dto.updateProcessAutoExecManage) {
        // 実行状態
        this.execStatus = dto.updateProcessAutoExecManage.currentStatus;
        // 終了状態
        this.overallStatus = dto.updateProcessAutoExecManage.overallStatus;
        // 前回開始日時
        this.lastStartDateTime = dto.updateProcessAutoExecManage.lastExecDateTime
          ? moment.utc(dto.updateProcessAutoExecManage.lastExecDateTime).format(DATETIME_FORMAT)
          : "";
        // 前回終了日時
        this.lastEndDateTime = dto.updateProcessAutoExecManage.lastEndExecDateTime
          ? moment.utc(dto.updateProcessAutoExecManage.lastEndExecDateTime).format(DATETIME_FORMAT)
          : "";
        // 処理時間
        if (dto.updateProcessAutoExecManage.lastExecDateTime && dto.updateProcessAutoExecManage.lastEndExecDateTime) {
          const end = moment.utc(dto.updateProcessAutoExecManage.lastEndExecDateTime);
          const start = moment.utc(dto.updateProcessAutoExecManage.lastExecDateTime);
          this.processingTime = moment.utc(moment.duration(end.diff(start)).as('milliseconds')).format("HH:mm:ss");
        }
      }
      // タスク実行
      this.isTaskExecution = (dto.executionTaskSetting) ? dto.executionTaskSetting.enabledSetting : false;
      // 次回実行日時
      this.nextExecDate = dto.nextExecDate
        ? moment.utc(dto.nextExecDate).format(DATETIME_FORMAT)
        : vm.$i18n('KBT002_165');
      // ｼｽﾃﾑｴﾗｰ
      this.systemError = (dto.updateProcessAutoExecManage && dto.updateProcessAutoExecManage.errorSystem)
        ? vm.$i18n('KBT002_61')
        : vm.$i18n('KBT002_62');
      // 業務エラー
      this.businessError = (dto.updateProcessAutoExecManage && dto.updateProcessAutoExecManage.errorBusiness)
        ? vm.$i18n('KBT002_61')
        : vm.$i18n('KBT002_62');
    }
  }

  export class ChangeExecutionTaskSettingCommand {
    companyId: string;
    execItemCd: string;
    enabledSetting: boolean;
    oneDayRepInterval: number;
    oneDayRepClassification: number;
    nextExecDateTime: string;
    endDate: any;
    endDateCls: number;
    endTime: number;
    endTimeCls: number;
    repeatContent: number;
    startDate: any;
    startTime: number;
    scheduleId: string;
    endScheduleId: string;
    monday: boolean;
    tuesday: boolean;
    wednesday: boolean;
    thursday: boolean;
    friday: boolean;
    saturday: boolean;
    sunday: boolean;
    january: boolean;
    february: boolean;
    march: boolean;
    april: boolean;
    may: boolean;
    june: boolean;
    july: boolean;
    august: boolean;
    september: boolean;
    october: boolean;
    november: boolean;
    december: boolean;
    repeatMonthDateList: number[];
    newMode = false;

    constructor(init?: Partial<ChangeExecutionTaskSettingCommand>) {
      $.extend(this, init);
    }
  }

  export class ExecuteProcessExecutionCommand {
    companyId: string;
    execItemCd: string;
    execId: string;
    execType: number;

    constructor(init?: Partial<ExecuteProcessExecutionCommand>) {
      $.extend(this, init);
    }
  }

  export class TerminateProcessExecutionCommand {
    companyId: string;
    execItemCd: string;
    execId: string;
    execType: number;
    taskTerminate: string;

    constructor(init?: Partial<TerminateProcessExecutionCommand>) {
      $.extend(this, init);
    }
  }
}
