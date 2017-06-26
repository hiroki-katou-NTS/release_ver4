module nts.uk.pr.view.base.simplehistory.newhistory {
    export module viewmodel {
        /**
         * New history screen options.
         */
        export interface NewHistoryScreenOption {
            /**
             * Screen mode.
             * Default: MODE_MASTER_HISTORY
             * @see UpdateHistoryScreenMode
             */
            screenMode?: number;

            /**
             * Function name.
             */
            name: string;

            /**
             * Master model.
             * Optional in MODE_HISTORY_ONLY
             */
            master?: model.MasterModel<any>;

            /**
             * Latest model.
             */
            lastest: model.HistoryModel;

            /**
             * On copy call back.
             */
            onCopyCallBack: (data: NewHistoryCallBackData) => JQueryPromise<any>;

            /**
             * On create call back.
             */
            onCreateCallBack: (data: NewHistoryCallBackData) => JQueryPromise<any>;
        }

        /**
         * Callback data.
         */
        export interface NewHistoryCallBackData {
            masterCode: string;
            startYearMonth: number;
        }

        /**
         * Add simple history screen model.
         */
        export class ScreenModel {
            private static CREATE_TYPE_COPY_LATEST: string = 'COPY';
            private static CREATE_TYPE_INIT: string = 'INIT';

            /**
             * Dialog options.
             */
            dialogOptions: NewHistoryScreenOption;

            /**
             * Create type.
             */
            createType: KnockoutObservable<string>;

            /**
             * Start year month.
             */
            startYearMonth: KnockoutObservable<number>;

            /**
             * Last year month.
             */
            lastYearMonth: string;

            /**
             * Constructor.
             */
            constructor() {
                var self = this;
                self.dialogOptions = nts.uk.ui.windows.getShared('options');
                self.dialogOptions.screenMode = self.dialogOptions.screenMode || simplehistory.dialogbase.ScreenMode.MODE_MASTER_HISTORY;
                if (self.dialogOptions.lastest) {
                    self.createType = ko.observable(ScreenModel.CREATE_TYPE_COPY_LATEST);
                } else {
                    self.createType = ko.observable(ScreenModel.CREATE_TYPE_INIT);
                }
                if (self.dialogOptions.lastest) {
                    self.startYearMonth = ko.observable(self.dialogOptions.lastest.start);
                    self.lastYearMonth = nts.uk.time.formatYearMonth(self.dialogOptions.lastest.start);
                } else {
                    self.startYearMonth = ko.observable(parseInt(nts.uk.time.formatDate(new Date(), 'yyyyMM')));
                }
            }

            /**
             * Start page.
             */
            public startPage(): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();
                dfd.resolve();
                return dfd.promise();
            }

            /**
             * Create history and then dialog.
             */
            private btnApplyClicked(): void {
                var self = this;
                var callBackData: NewHistoryCallBackData = {
                    masterCode: self.dialogOptions.master ? self.dialogOptions.master.code : undefined,
                    startYearMonth: self.startYearMonth()
                };
                if (self.createType() == ScreenModel.CREATE_TYPE_COPY_LATEST) {
                    self.dialogOptions.onCopyCallBack(callBackData)
                        .done(() => nts.uk.ui.windows.close())
                        .fail((res) => {
                            nts.uk.ui.dialog.alert(res.message);
                            if (res.messageId == 'ER010') {
                                $('#startYearMonth').ntsError('set', '対象データがありません。');
                            }
                        });
                } else {
                    self.dialogOptions.onCreateCallBack(callBackData)
                        .done(() => nts.uk.ui.windows.close())
                        .fail((res) => {
                            nts.uk.ui.dialog.alert(res.message);
                            if (res.messageId == 'ER010') {
                                $('#startYearMonth').ntsError('set', '対象データがありません。');
                            }
                        });
                }
            }

            /**
             * Close dialog.
             */
            private btnCancelClicked(): void {
                nts.uk.ui.windows.close();
            }
        }
    }
}