module nts.uk.pr.view.qpp007.a {

    export module viewmodel {

        export class ScreenModel {
            startYearMonth: KnockoutObservable<string>;
            endYearMonth: KnockoutObservable<string>;
            isUsuallyAMonth: KnockoutObservable<boolean>;
            isPreliminaryMonth: KnockoutObservable<boolean>;
            outputFormatType: KnockoutObservableArray<SelectionModel>;
            selectedOutputFormat: KnockoutObservable<string>;
            outputSettings: KnockoutObservableArray<SelectionModel>;
            selectedOutputSetting: KnockoutObservable<string>;
            isVerticalLine: KnockoutObservable<boolean>;
            isHorizontalRuledLine: KnockoutObservable<boolean>;
            pageBreakSetting: KnockoutObservableArray<SelectionModel>;
            selectedpageBreakSetting: KnockoutObservable<string>;
            departmentHierarchyList: KnockoutObservableArray<SelectionModel>;
            selectedHierachy: KnockoutObservable<string>;
            outputLanguage: KnockoutObservableArray<SelectionModel>;
            selectedOutputLanguage: KnockoutObservable<string>;
            isDepartmentHierarchy: KnockoutObservable<boolean>;
            hourMinuteOutputClassification: KnockoutObservableArray<SelectionModel>;
            selectedHourMinuteOutputClassification: KnockoutObservable<string>;

            constructor() {
                this.isUsuallyAMonth = ko.observable(true);
                this.isPreliminaryMonth = ko.observable(false);
                this.startYearMonth = ko.observable('2016/12');
                this.endYearMonth = ko.observable('2017/03');
                this.selectedOutputFormat = ko.observable('1');
                this.outputFormatType = ko.observableArray<SelectionModel>([
                    new SelectionModel('1', '明細一覧表'),
                    new SelectionModel('2', '明細累計表')
                ]);
                this.outputSettings = ko.observableArray<SelectionModel>([]);
                this.selectedOutputSetting = ko.observable('1');
                this.isVerticalLine = ko.observable(false);
                this.isHorizontalRuledLine = ko.observable(false);

                this.pageBreakSetting = ko.observableArray<SelectionModel>([
                    new SelectionModel('1', 'なし'),
                    new SelectionModel('2', '社員毎'),
                    new SelectionModel('3', '部門毎'),
                    new SelectionModel('4', '部門階層')
                ]);
                this.selectedpageBreakSetting = ko.observable('1');

                this.departmentHierarchyList = ko.observableArray<SelectionModel>([
                    new SelectionModel('1', '1'), new SelectionModel('2', '2'), new SelectionModel('3', '3'),
                    new SelectionModel('4', '4'), new SelectionModel('5', '5'), new SelectionModel('6', '6'),
                    new SelectionModel('7', '7'), new SelectionModel('8', '8'), new SelectionModel('9', '9')
                ]);
                this.selectedHierachy = ko.observable('1');

                this.outputLanguage = ko.observableArray<SelectionModel>([
                    new SelectionModel('1', '日本語'),
                    new SelectionModel('2', '英語')
                ]);
                this.selectedOutputLanguage = ko.observable('1');
                var self = this;
                this.isDepartmentHierarchy = ko.computed(function() {
                    return self.selectedpageBreakSetting() == '4';
                });
                self.hourMinuteOutputClassification = ko.observableArray<SelectionModel>([
                    new SelectionModel('0', '時間'),
                    new SelectionModel('1', '分')
                ]);
                self.selectedHourMinuteOutputClassification = ko.observable('0');
            }

            /**
             * Start srceen.
             */
            public start(): JQueryPromise<any> {
                var dfd = $.Deferred<any>();
                var self = this;
                $.when(self.loadAllOutputSetting()).done(function() {
                    dfd.resolve();
                })
                return dfd.promise();
            }

            /**
             * Load all OutputSetting.
             */
            public loadAllOutputSetting(): JQueryPromise<any> {
                var dfd = $.Deferred<any>();
                var self = this;
                service.findAllSalaryOutputSetting().done(function(data) {
                    self.outputSettings(data);
                    dfd.resolve();
                }).fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);
                    dfd.reject();
                })
                return dfd.promise();
            }

            /**
             * Open PrintSetting dialog
             */
            public openPrintSettingDialog() {
                nts.uk.ui.windows.sub.modal("/view/qpp/007/b/index.xhtml", { title: "印刷設定", dialogClass: 'no-close' });
            }

            /**
             * Open SalaryOuputSetting dialog
             */
            public openSalaryOuputSettingDialog() {
                var self = this;
                // Set parent value
                nts.uk.ui.windows.setShared("selectedCode", self.selectedOutputSetting());
                nts.uk.ui.windows.setShared("outputSettings", self.outputSettings());
                nts.uk.ui.windows.sub.modal("/view/qpp/007/c/index.xhtml", { title: "出力項目の設定", dialogClass: 'no-close' }).onClosed(() => {
                    if (nts.uk.ui.windows.getShared('isSomethingChanged')) {
                        // Reload output setting list.
                        self.loadAllOutputSetting();
                    }
                })
            }

            /**
             * Print selected Employee.
             */
            public printSelectedEmployee(): void {
                let self = this;
                // Validate
                self.validate();
                if (!nts.uk.ui._viewModel.errors.isEmpty()) {
                    return;
                }
                let command = self.toJsObject();
                service.saveAsPdf(command)
                    .fail(function(res) {
                        //TODO ...
                    });
            }

            /**
             * Output text.
             */
            public outputText(): void {
                let self = this;
                // Validate
                self.validate();
                if (!nts.uk.ui._viewModel.errors.isEmpty()) {
                    return;
                }
            }

            /**
             * Collect data from model then convert to JsObject.
             */
            private toJsObject(): any {
                let self = this;
                let command: any = {};
                command.outputFormatType = self.selectedOutputFormat();
                command.outputSettingCode = self.selectedOutputSetting();
                command.isVerticalLine = self.isVerticalLine();
                command.isHorizontalLine = self.isHorizontalRuledLine();
                command.outputLanguage =  self.selectedOutputLanguage();
                command.pageBreakSetting = self.selectedpageBreakSetting();
                if (self.isDepartmentHierarchy()) {
                    command.hierarchy  = self.selectedHierachy();
                }
                return command;
            }

            /**
            * Clear all input errors.
            */
            private clearErrors(): void {
                if (nts.uk.ui._viewModel) {
                    $('#start-ym').ntsError('clear');
                    $('#end-ym').ntsError('clear');
                }
            }

            /**
            * Validate all inputs.
            */
            private validate(): void {
                $('#start-ym').ntsEditor('validate');
                $('#end-ym').ntsEditor('validate');
            }

        }

        /**
         *  Class SelectionModel.
         */
        export class SelectionModel {
            code: string;
            name: string;

            constructor(code: string, name: string) {
                this.code = code;
                this.name = name;
            }
        }
    }
}