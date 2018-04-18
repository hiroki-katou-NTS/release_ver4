module nts.uk.at.view.kaf018.h.viewmodel {
    import text = nts.uk.resource.getText;
    import block = nts.uk.ui.block;
    import info = nts.uk.ui.dialog.info;
    import error = nts.uk.ui.dialog.alertError;
    import ntsError = nts.uk.ui.errors;
    import confirm = nts.uk.ui.dialog.confirm;
    import model = kaf018.share.model;

    export class ScreenModel {
        tabs: KnockoutObservableArray<nts.uk.ui.NtsTabPanelModel>;
        selectedTab: KnockoutObservable<string>;
        checkH3: KnockoutObservable<boolean> = ko.observable(false);
        checkH2: KnockoutObservable<boolean> = ko.observable(false);
        checkH1: KnockoutObservable<boolean> = ko.observable(false);

        appApprovalUnapproved: model.MailTemp = null;
        dailyUnconfirmByPrincipal: model.MailTemp = null;
        dailyUnconfirmByConfirmer: model.MailTemp = null;
        monthlyUnconfirmByConfirmer: model.MailTemp = null;
        workConfirmation: model.MailTemp = null;

        identityProcessUseSet: model.IdentityProcessUseSet = new model.IdentityProcessUseSet(false);
        approvalProcessingUseSet: model.ApprovalProcessingUseSetting = new model.ApprovalProcessingUseSetting(false, false);

        screenEditMode: KnockoutObservable<boolean> = ko.observable(false);

        constructor() {
            var self = this;
            self.tabs = ko.observableArray([
                { id: 'tab-1', title: text("KAF018_77"), content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true) },
                { id: 'tab-2', title: text("KAF018_78"), content: '.tab-content-2', enable: self.checkH3, visible: ko.observable(true) },
                { id: 'tab-3', title: text("KAF018_79"), content: '.tab-content-3', enable: self.checkH2, visible: ko.observable(true) },
                { id: 'tab-4', title: text("KAF018_80"), content: '.tab-content-4', enable: self.checkH1, visible: ko.observable(true) },
                { id: 'tab-5', title: text("KAF018_81"), content: '.tab-content-5', enable: ko.observable(true), visible: ko.observable(true) }
            ]);
            self.selectedTab = ko.observable('tab-1');

            self.selectedTab.subscribe((newValue) => {
                let mailType = 0;
                switch (newValue) {
                    case 'tab-1':
                        self.screenEditMode(self.appApprovalUnapproved.editMode());
                        break;
                    case 'tab-2':
                        self.screenEditMode(self.dailyUnconfirmByPrincipal.editMode());
                        break;
                    case 'tab-3':
                        self.screenEditMode(self.dailyUnconfirmByConfirmer.editMode());
                        break;
                    case 'tab-4':
                        self.screenEditMode(self.monthlyUnconfirmByConfirmer.editMode());
                        break;
                    case 'tab-5':
                        self.screenEditMode(self.workConfirmation.editMode());
                        break;
                }
            });
        }

        /**
         * 起動する
         */
        startPage(): JQueryPromise<any> {
            var self = this;
            let dfd = $.Deferred();
            block.invisible();

            service.getMailBySetting().done(function(data: any) {
                _.each(data, function(mail) {
                    let temp = new model.MailTemp(
                        mail.mailType,
                        mail.mailSubject,
                        mail.mailContent,
                        mail.urlApprovalEmbed,
                        mail.urlDayEmbed,
                        mail.urlMonthEmbed,
                        mail.editMode);
                    switch (mail.mailType) {
                        case 0:
                            self.appApprovalUnapproved = temp;
                            break
                        case 1:
                            self.dailyUnconfirmByPrincipal = temp;
                            break
                        case 2:
                            self.dailyUnconfirmByConfirmer = temp;
                            break
                        case 3:
                            self.monthlyUnconfirmByConfirmer = temp;
                            break
                        case 4:
                            self.workConfirmation = temp;
                            break;
                    }
                });

                self.screenEditMode(self.appApprovalUnapproved.editMode());
                self.checkH3(self.dailyUnconfirmByPrincipal == null ? false : true);
                self.checkH2(self.dailyUnconfirmByConfirmer == null ? false : true);
                self.checkH1(self.monthlyUnconfirmByConfirmer == null ? false : true);

                block.clear();
                dfd.resolve();
            });
            return dfd.promise();
        }

        /**
         * メール本文を登録する
         */
        private registerApprovalStatusMail(): void {
            var self = this;

            //validate
            /*if (ntsError.hasError()) {
                return;
            }*/

            block.invisible();
            let listMail = [
                self.getMailTempJS(self.appApprovalUnapproved),
                self.getMailTempJS(self.workConfirmation)
            ];
            if (self.checkH3()) {
                listMail.push(self.getMailTempJS(self.dailyUnconfirmByPrincipal));
            }
            if (self.checkH2()) {
                listMail.push(self.getMailTempJS(self.dailyUnconfirmByConfirmer));
            }
            if (self.checkH1()) {
                listMail.push(self.getMailTempJS(self.monthlyUnconfirmByConfirmer));
            }

            //アルゴリズム「承認状況メール本文登録」を実行する
            service.registerApprovalStatusMail(listMail).done(function() {
                //画面モード　＝　更新
                self.screenEditMode(true);
                self.appApprovalUnapproved.editMode(true);
                if (self.checkH3()) {
                    self.dailyUnconfirmByPrincipal.editMode(true);
                }
                if (self.checkH2()) {
                    self.dailyUnconfirmByConfirmer.editMode(true);
                }
                if (self.checkH1()) {
                    self.monthlyUnconfirmByConfirmer.editMode(true);
                }
                block.clear();
            });
        }

        private getMailTempJS(mail: model.MailTemp) {
            let obj = ko.toJS(mail)
            obj.urlApprovalEmbed = obj.urlApprovalEmbed ? 1 : 0;
            obj.urlDayEmbed = obj.urlDayEmbed ? 1 : 0;
            obj.urlMonthEmbed = obj.urlMonthEmbed ? 1 : 0;
            obj.editMode = obj.editMode ? 1 : 0;
            return obj;
        }

        /**
         * テストメールを送信する
         */
        sendTestMail() {
            var self = this;
            block.invisible();
            // アルゴリズム「承認状況メールテスト送信」を実行する
            service.confirmSenderMail().done(function(data: any) {
                //メッセージ（Msg_800）を表示する
                confirm({ messageId: "Msg_800", messageParams: [data] }).ifYes(() => {
                    //アルゴリズム「承認状況メールテスト送信実行」を実行する
                    let mailType = 0;
                    switch (self.selectedTab()) {
                        case 'tab-1':
                            mailType = 0;
                            break;
                        case 'tab-2':
                            mailType = 1;
                            break;
                        case 'tab-3':
                            mailType = 2;
                            break;
                        case 'tab-4':
                            mailType = 3;
                            break;
                        case 'tab-5':
                            mailType = 4;
                            break;
                    }
                    block.invisible();
                    service.sendTestMail(mailType).done(function(result: any) {
                        console.log(result);
                    }).fail(function(err) {
                        error({ messageId: err.messageId });
                    }).always(function() {
                        block.clear();
                    });
                })
            }).fail(function(err) {
                error({ messageId: err.messageId });
            }).always(function() {
                block.clear();
            });
        }

        /**
         * 終了する
        */
        close() {
            //画面を閉じる
            nts.uk.ui.windows.close()
        }
    }
}