module nts.uk.at.kal011.d {

    const API = {
        extractStart: "at/function/alarm-workplace/alarm-list/extract/start",
        extractExecute: "at/function/alarm-workplace/alarm-list/extract/execute",
        extractUpdateStatus: "at/function/alarm-workplace/alarm-list/extract/update-status",
    }

    @bean()
    export class Kal011DViewModel extends ko.ViewModel {

        /*実行開始日時*/
        start: Date;
        startTime: KnockoutObservable<any> = ko.observable(null);
        /*実行終了日時*/
        endTime: KnockoutObservable<any> = ko.observable(null);
        /*経過時間*/
        elapsedTime: KnockoutObservable<any> = ko.observable("00:00:00");;
        /*処理状態*/
        progress: KnockoutObservable<any> = ko.observable("0");

        alarmPatternCode: string;
        workplaceIds: Array<string>;
        categoryPeriods: Array<any>;
        processStatusId: string;

        processId: string;
        dialogMode: KnockoutObservable<number> = ko.observable(ExtractState.PROCESSING);
        // interval 1000ms request to server
        interval: any;
        taskId: string;

        formatDate: string = "YYYY/MM/DD HH:mm:ss";

        canInterrupt: KnockoutObservable<boolean> = ko.observable(false);
        canCheckResult: KnockoutObservable<boolean> = ko.observable(false);
        canClose: KnockoutObservable<boolean> = ko.observable(false);

        constructor() {
            super();
            const vm = this;
            let data = nts.uk.ui.windows.getShared("KAL011DModalData");
            vm.alarmPatternCode = data.alarmPatternCode;
            vm.workplaceIds = data.workplaceIds;
            vm.categoryPeriods = data.categoryPeriods;
        }

        created() {
            const vm = this;
            _.extend(window, { vm });
            vm.start = vm.$date.now();
            vm.startTime(moment(vm.start).format(vm.formatDate))
            vm.interval = setInterval(vm.countTime, 1000, vm);
            vm.extractAlarm().done((data: any) => {
                console.log(data);
                vm.extractUpdateStatus(ExtractState.SUCCESSFUL_COMPLE);
            }).fail((err: any) => {
                vm.$dialog.error(err);
            }).always(() => {
                vm.setFinished();
            });
        }

        extractAlarm(): JQueryPromise<any> {
            const vm = this;
            let dfd = $.Deferred();

            vm.$ajax(API.extractStart).done((processStatusId: string) => {
                vm.processStatusId = processStatusId;
                let param: any = {
                    workplaceIds: vm.workplaceIds,
                    alarmPatternCode: vm.alarmPatternCode,
                    categoryPeriods: vm.categoryPeriods,
                    processStatusId: vm.processStatusId
                }
                // 抽出処理が実行中である場合
                vm.setControlStatus(ScreenMode.IN_PROGRESS);
                vm.$ajax(API.extractExecute, param).done((task: any) => {
                    vm.taskId = task.id;
                    nts.uk.deferred.repeat(conf => conf.task(() => {
                        return nts.uk.request.asyncTask.getInfo(vm.taskId).done(function (res: any) {
                            vm.updateProgress(res.taskDatas);
                            if (res.succeeded) {
                                let data = {};
                                vm.setControlStatus(ScreenMode.SUCCESSFUL);

                                dfd.resolve(data);
                            } else if (res.failed) {
                                // 抽出処理が中断された場合
                                vm.setControlStatus(ScreenMode.INTERRUPT);
                                dfd.reject(res.error);
                            }
                        });
                    }).while(infor => {
                        return (infor.pending || infor.running) && infor.status != "REQUESTED_CANCEL";
                    }).pause(1000));
                }).fail((err: any) => {
                    // 抽出処理が開始できない場合
                    vm.setControlStatus(ScreenMode.NOT_START);

                    dfd.reject(err);
                });
            }).fail((err: any) => {
                // 抽出処理が開始できない場合
                vm.setControlStatus(ScreenMode.NOT_START);

                dfd.reject(err);
            });

            return dfd.promise();
        }

        updateProgress(taskDatas: Array<any>) {
            const vm = this;
            let progress = "";
            let count: any = _.find(taskDatas, (task: any) => { return task.key == "ctgCount" });
            progress += count == null ? "0" : count.valueAsString;
            vm.progress(progress);
        }

        extractUpdateStatus(status: ExtractState): JQueryPromise<any> {
            const vm = this;
            let dfd = $.Deferred();
            let param = {
                processStatusId: vm.processStatusId,
                status: status
            }
            vm.$ajax(API.extractUpdateStatus, param).done(() => dfd.resolve()).fail((err: any) => dfd.reject(err));
            return dfd.promise();
        }

        interruptProcess() {
            const vm = this;
            vm.$dialog.confirm({ messageId: "Msg_1412" }).then((result: string) => {
                if (result === 'yes') {
                    nts.uk.request.asyncTask.requestToCancel(vm.taskId);
                    vm.extractUpdateStatus(ExtractState.INTERRUPTION);
                    vm.setFinished();

                    // 抽出処理が中断された場合
                    vm.setControlStatus(ScreenMode.INTERRUPT);
                }
            });
        }

        checkResult() {
            const vm = this;
            let modalData = {
                selectedCode: vm.alarmPatternCode,
                processId: vm.processId,
                isClose: false
            }
            vm.$window.storage('KAL011DModalData', modalData).done(() => {
                nts.uk.ui.windows.close();
            })
        }

        closeDialog() {
            var vm = this;
            let modalData = {
                isClose: true
            }
            vm.$window.storage('KAL011DModalData', modalData).done(() => {
                nts.uk.ui.windows.close();
            });
        }

        setFinished() {
            const vm = this;
            vm.endTime(moment(vm.$date.now()).format(vm.formatDate));
            window.clearInterval(vm.interval);
        }

        countTime(vm: Kal011DViewModel) {
            let over = (new Date()).getTime() - vm.start.getTime()
            let time = new Date(null);
            time.setHours(0);
            time.setTime(time.getTime() + over);
            let result = moment(time).format("HH:mm:ss")
            vm.elapsedTime(result);
        }

        setControlStatus(mode: ScreenMode) {
            const vm = this;

            switch (mode) {
                case ScreenMode.IN_PROGRESS:
                    // 抽出処理が実行中である場合
                    vm.canInterrupt(true);
                    vm.canCheckResult(false);
                    vm.canClose(false);
                    break;
                case ScreenMode.INTERRUPT:
                    // 抽出処理が中断された場合
                    vm.canInterrupt(false);
                    vm.canCheckResult(true);
                    vm.canClose(true);
                    break;
                case ScreenMode.NOT_START:
                    // 抽出処理が開始できない場合
                    vm.canInterrupt(false);
                    vm.canCheckResult(false);
                    vm.canClose(true);
                    break;
                case ScreenMode.SUCCESSFUL:
                    vm.canInterrupt(false);
                    vm.canCheckResult(true);
                    vm.canClose(false);
                    break;
            }
        }
    }

    enum ExtractState {
        SUCCESSFUL_COMPLE = 0, /**正常終了*/
        ABNORMAL_TERMI = 1, /**異常終了*/
        PROCESSING = 2, /**処理中*/
        INTERRUPTION = 3,    /**中断*/
    }

    enum ScreenMode {
        IN_PROGRESS = 1,
        INTERRUPT = 2,
        NOT_START = 3,
        SUCCESSFUL = 4
    }
}