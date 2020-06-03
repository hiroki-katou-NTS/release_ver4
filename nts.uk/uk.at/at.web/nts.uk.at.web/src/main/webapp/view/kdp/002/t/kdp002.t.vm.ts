module nts.uk.at.view.kdp002.t {
    export module viewmodel {
        export class ScreenModel {
            dataShare: KnockoutObservableArray<any> = ko.observableArray([]);
            labelNames: KnockoutObservable<string> = ko.observable('');
            labelColor: KnockoutObservable<string> = ko.observable('');
            buttonInfo: KnockoutObservableArray<ItemModel> = ko.observableArray([]);
            share: KnockoutObservableArray<any> = ko.observableArray([]);
            messageContent: KnockoutObservable<string> = ko.observable('');
            messageColor: KnockoutObservable<string> = ko.observable('');
            errorDate: KnockoutObservable<string> = ko.observable('');
            constructor() {
            }
            /**
             * start page  
             */
            public startPage(): JQueryPromise<any> {
                let self = this,
                    dfd = $.Deferred();
                self.share = nts.uk.ui.windows.getShared('KDP010_2T');
                let lstButton: ItemModel[] = [];
                for (let i = 1; i < 6; i++) {
                    lstButton.push(new ItemModel('', '', ''));
                }
                self.buttonInfo(lstButton);
                if (!self.share) {
                    self.dataShare = {
                        messageContent: '個人の打刻入力で利用する設定です',
                        messageColor: '#0033cc',
                        listRequired: [{
                            buttonName: ko.observable('個人の打')
                        }, {
                            buttonName: ko.observable('個人の打')
                        }, {
                            buttonName: ko.observable('個人の打')
                        }, {
                            buttonName: ko.observable('個人の打')
                        }, {
                            buttonName: ko.observable('個人の打')
                        }, {
                            buttonName: ko.observable('個人の打')
                        }]
                    }
                } else {
                    self.messageContent(self.share.dailyAttdErrorInfos[0].messageContent);
                    self.messageColor(self.share.dailyAttdErrorInfos[0].messageColor);
                    self.errorDate(self.share.dailyAttdErrorInfos[0].lastDateError);
                    self.dataShare = {
                        listRequired: [{
                            buttonName: ko.observable(self.share.appDispNames[0].dispName)
                        }, {
                            buttonName: ko.observable(self.share.appDispNames[1].dispName)
                        }, {
                            buttonName: ko.observable(self.share.appDispNames[2].dispName)
                        }, {
                            buttonName: ko.observable(self.share.appDispNames[3].dispName)
                        }, {
                            buttonName: ko.observable(self.share.appDispNames[4].dispName)
                        }, {
                            buttonName: ko.observable(self.share.appDispNames[5].dispName)
                        }]
                    }

                }

                dfd.resolve();
                return dfd.promise();
            }

            /**
             * Close dialog
             */
            public closeDialog(): void {
                let self = this;
                let shareG = {
                    messageContent: self.labelNames(),
                    messageColor: self.labelColor(),
                    errorDate: self.errorDate(),
                    listRequired: self.dataShare.listRequired,
                    isClose: true
                };
                nts.uk.ui.windows.setShared('KDP010_T', shareG);
                nts.uk.ui.windows.close();
            }

            /**
             * Close dialog
             */
            public jumpScreen(data, appType): void {
                console.log(data);
                console.log(appType);
                let self = this;
                let shareG = {
                    messageContent: self.labelNames(),
                    messageColor: self.labelColor(),
                    errorDate: self.errorDate(),
                    listRequired: self.dataShare.listRequired,
                    appType: appType
                };
                nts.uk.ui.windows.setShared('KDP010_T', shareG);
                nts.uk.ui.windows.close();
            }

        }

    }
    export class ItemModel {
        buttonName: string;
        buttonColor: string;
        textColor: string;

        constructor(buttonName: string, buttonColor: string, textColor: string) {
            this.buttonName = ko.observable('') || '';
            this.buttonColor = ko.observable('') || '';
            this.textColor = ko.observable('') || '';
        }
    }
}