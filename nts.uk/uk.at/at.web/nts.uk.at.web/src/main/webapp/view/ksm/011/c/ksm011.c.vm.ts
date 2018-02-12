module nts.uk.at.view.ksm011.c.viewmodel {
    import blockUI = nts.uk.ui.block;

    export class ScreenModel {
        controlUseCls: KnockoutObservableArray<any>;
        selectedControlUse: KnockoutObservable<number>;
        workTypeList: KnockoutObservableArray<any>;
        openDialogEnable: KnockoutObservable<boolean>;
        workTypeListEnable: KnockoutObservable<boolean>;
        currentItem: KnockoutObservable<WorktypeDisplayDto>;
        items: KnockoutObservableArray<WorktypeDisplayDto>;
        workTypeNames: KnockoutObservable<string>;

        constructor() {
            var self = this;

            self.controlUseCls = ko.observableArray([
                { code: 0, name: nts.uk.resource.getText("KSM011_8") },
                { code: 1, name: nts.uk.resource.getText("KSM011_9") }
            ]);

            self.selectedControlUse = ko.observable(0);
            self.workTypeList = ko.observableArray([]);
            self.openDialogEnable = ko.observable(true);
            self.workTypeListEnable = ko.observable(true);
            self.currentItem = ko.observable(new WorktypeDisplayDto({}));
            self.workTypeNames = ko.observable("");
            self.items = ko.observableArray([]);

            self.selectedControlUse.subscribe(function(value) {
                if (value == 0) {
                    self.openDialogEnable(true);
                    self.workTypeListEnable(true);
                } else {
                    self.openDialogEnable(false);
                    self.workTypeListEnable(false);
                }
            });
        }

        /**
         * Start page.
         */
        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            self.getWorkTypeList();
            self.findAll();
            return dfd.promise();
        }

        openKDL002Dialog() {
            let self = this;
            nts.uk.ui.errors.clearAll();
            nts.uk.ui.block.invisible();
            var workTypeCodes = _.map(self.workTypeList(), function(item: IWorkTypeModal) { return item.workTypeCode });
            nts.uk.ui.windows.setShared('KDL002_Multiple', true);
            nts.uk.ui.windows.setShared('KDL002_AllItemObj', workTypeCodes);
            nts.uk.ui.windows.setShared('KDL002_SelectedItemId', self.currentItem().workTypeList());

            nts.uk.ui.windows.sub.modal('/view/kdl/002/a/index.xhtml').onClosed(function(): any {
                nts.uk.ui.block.clear();
                var data = nts.uk.ui.windows.getShared('KDL002_SelectedNewItem');
                var name = [];
                _.forEach(data, function(item: IWorkTypeModal) {
                    name.push(item.name);
                });
                self.workTypeNames(name.join(" + "));

                var workTypeCodes = _.map(data, function(item: any) { return item.code; });
                self.currentItem().workTypeList(workTypeCodes);
            });
            nts.uk.ui.block.clear();
        }

        getWorkTypeList() {
            var self = this;
            var dfd = $.Deferred();
            service.findWorkType().done(function(res) {
                _.forEach(res, function(item) {
                    self.workTypeList.push({
                        workTypeCode: item.workTypeCode,
                        name: item.name,
                        memo: item.memo
                    });
                });
                dfd.resolve();
            }).fail(function(error) {
                alert(error.message);
                dfd.reject(error);
            });
            return dfd.promise();
        }

        /**
         * Registration function.
         */
        registration() {
            var self = this;
            var dfd = $.Deferred();
            nts.uk.ui.block.invisible();
            var workType = ko.toJS(self.currentItem());
            service.add(workType).done(function() {
                nts.uk.ui.dialog.info({ messageId: "Msg_15" });
            }).fail(function(res) {
                nts.uk.ui.dialog.alertError(res.message);
            }).always(() => {
                nts.uk.ui.block.clear();
            });

            return dfd.promise();

        }

        findAll(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            self.items.removeAll();
            service.findAll().done(function(totalTimeArr: Array<any>) {
                    var totalTime: IWorktypeDisplayDto = {
                        useAtr: totalTimeArr.useAtr,
                        workTypeList: totalTimeArr.workTypeList
                    };
                    self.items.push(new WorktypeDisplayDto(totalTime));
                    var names = self.getNames(self.workTypeList(), totalTimeArr.workTypeList);
                    self.workTypeNames(names);
            });
            return dfd.promise();
        }

        getNames(data: Array<IWorkTypeModal>, workTypeCodesSelected: Array<string>) {
            var name = [];
            var self = this;
            if (workTypeCodesSelected && workTypeCodesSelected.length > 0) {
                _.forEach(data, function(item: IWorkTypeModal) {
                    _.forEach(workTypeCodesSelected, function(items: any) {
                        if (_.includes(items.workTypeCode, item.workTypeCode)) {
                            name.push(item.name);
                        }
                    });
                });
            }
            return name.join(" + ");
        }

    }
    export class WorkTypeModal {
        workTypeCode: string;
        name: string;
        memo: string;
        constructor(param: IWorkTypeModal) {
            this.workTypeCode = param.workTypeCode;
            this.name = param.name;
            this.memo = param.memo;
        }
    }

    export interface IWorkTypeModal {
        workTypeCode: string;
        name: string;
        memo: string;
    }



    export class WorktypeDisplayDto {
        useAtr: KnockoutObservable<number>;
        workTypeList: KnockoutObservableArray<WorktypeDisplaySetDto>;
        constructor(param: IWorktypeDisplayDto) {
            this.useAtr = ko.observable(param.useAtr || 0);
            this.workTypeList = ko.observableArray(param.workTypeList || null);
        }
    }

    export interface IWorktypeDisplayDto {
        useAtr?: number;
        workTypeList?: Array<WorktypeDisplaySetDto>;
    }


    export class WorktypeDisplaySetDto {
        workTypeCode: string;
        constructor(param: IWorktypeDisplaySetDto) {
            this.workTypeCode = param.workTypeCode;
        }
    }

    export interface IWorktypeDisplaySetDto {
        workTypeCode?: string;
    }
}
