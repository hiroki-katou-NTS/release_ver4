module nts.uk.com.view.ccg015.a {
    export module viewmodel {
        import commonModel = ccg.model;
        import TopPageItemDto = ccg015.a.service.model.TopPageItemDto;
        import TopPageDto = ccg015.a.service.model.TopPageDto;
        export class ScreenModel {
            listTopPage: KnockoutObservableArray<Node>;
            toppageSelectedCode: KnockoutObservable<string>;
            topPageModel: KnockoutObservable<TopPageModel>;
            columns: KnockoutObservable<any>;
            isNewMode: KnockoutObservable<boolean>;
            languageListOption: KnockoutObservableArray<ItemCbbModel>;
            languageSelectedCode: KnockoutObservable<string>;
            constructor() {
                var self = this;
                self.listTopPage = ko.observableArray<Node>([]);
                self.toppageSelectedCode = ko.observable(null);
                self.topPageModel = ko.observable(new TopPageModel());
                self.columns = ko.observableArray([
                    { headerText: "コード", width: "50px", key: 'code', dataType: "string", hidden: false },
                    { headerText: "名称", width: "300px", key: 'nodeText', dataType: "string" }
                ]);
                self.isNewMode = ko.observable(true);
                self.toppageSelectedCode.subscribe(function(selectedTopPageCode: string) {
                    if (selectedTopPageCode) {
                        service.loadDetailTopPage(selectedTopPageCode).done(function(data: TopPageDto) {
                            self.loadTopPageItemDetail(data);
                            $('.save-error').ntsError('clear');
                        });
                        self.isNewMode(false);
                        $("#inp_name").focus();
                    }
                    else {
                        self.isNewMode(false);
                        self.newTopPage();
                    }
                });
                self.languageListOption = ko.observableArray([
                    new ItemCbbModel("0", "日本語"),
                    new ItemCbbModel("1", "英語"),
                    new ItemCbbModel("2", "ベトナム語")
                ]);
                self.languageSelectedCode = ko.observable("0");
                //end constructor
            }

            start(): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();
                self.loadTopPageList().done(function() {
                    dfd.resolve();
                });
                return dfd.promise();
            }

            private loadTopPageList(): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();
                self.listTopPage([]);
                service.loadTopPage().done(function(data: Array<TopPageItemDto>) {
                    if (data.length > 0) {
                        data.forEach(function(item, index) {
                            self.listTopPage.push(new Node(item.topPageCode, item.topPageName, null));
                        });
                        if (self.listTopPage().length > 0) {
                            //focus first item
                            self.toppageSelectedCode(self.listTopPage()[0].code);
                        }
                    }
                    else {
                        self.newTopPage();
                    }
                    dfd.resolve();
                });
                return dfd.promise();
            }

            //load top page Item 
            private loadTopPageItemDetail(data: TopPageDto) {
                var self = this;
                self.topPageModel().topPageCode(data.topPageCode);
                self.topPageModel().topPageName(data.topPageName);
                if (data.layoutId != "" && data.layoutId != null) {
                    self.topPageModel().layoutId(data.layoutId);
                    self.changePreviewIframe(data.layoutId);
                }
                else {
                    self.changePreviewIframe(null);
                }
            }

            private collectData(): TopPageDto {
                var self = this;
                //mock data
                var data: TopPageDto = { topPageCode: self.topPageModel().topPageCode(), topPageName: self.topPageModel().topPageName(), languageNumber: 0, layoutId: self.topPageModel().layoutId() };
                return data;
            }

            private saveTopPage() {
                var self = this;
                $('.nts-input').ntsEditor('validate');
                if ($('.nts-input').ntsError('hasError')) {

                }
                else {
                    //check update or create
                    if (self.isNewMode()) {
                        service.registerTopPage(self.collectData()).done(function() {
                            nts.uk.ui.dialog.alert("登録しました。");
                        }).fail(function(res) {
                            alert(res.messageId);
                        });
                    }
                    else {
                        service.updateTopPage(self.collectData()).done(function() {
                            nts.uk.ui.dialog.alert("登録しました。");
                        });
                    }
                    self.loadTopPageList().done(function() {
                        self.toppageSelectedCode(self.collectData().topPageCode);
                    });
                }
            }
            private openMyPageSettingDialog() {
                nts.uk.ui.windows.sub.modal("/view/ccg/015/b/index.xhtml", {
                    height: 700, width: 850,
                    title: "マイページの設定",
                    dialogClass: 'no-close'
                }).onClosed(() => {
                    //TODO
                });

            }

            private copyTopPage() {
                var self = this;
                nts.uk.ui.windows.setShared('topPageCode', self.topPageModel().topPageCode());
                nts.uk.ui.windows.setShared('topPageName', self.topPageModel().topPageName());
                nts.uk.ui.windows.setShared('layoutId', self.topPageModel().layoutId());
                nts.uk.ui.windows.sub.modal("/view/ccg/015/c/index.xhtml", {
                    height: 400, width: 700,
                    title: "他のトップページコピー",
                    dialogClass: 'no-close'
                }).onClosed(() => {
                    var codeOfNewTopPage = nts.uk.ui.windows.getShared("codeOfNewTopPage");
                    self.loadTopPageList().done(function() {
                        self.toppageSelectedCode(codeOfNewTopPage);
                    });
                    //TODO 
                });

            }
            private openFlowMenuSettingDialog() {
                var self = this;
                nts.uk.ui.windows.setShared('topPageCode', self.topPageModel().topPageCode());
                nts.uk.ui.windows.setShared('topPageName', self.topPageModel().topPageName());
                nts.uk.ui.windows.sub.modal("/view/ccg/030/a/index.xhtml", {
                    height: 650, width: 1300,
                    title: "フローメニューの設定",//TODO change name
                    dialogClass: 'no-close'
                }).onClosed(() => {
                    //TODO on Close dialog
                });
            }

            private openLayoutSettingDialog() {
                var self = this;
                var layoutId = self.topPageModel().layoutId();
                var topPageCode = self.topPageModel().topPageCode();
                var transferData: commonModel.TransferLayoutInfo = {parentCode: topPageCode, layoutID: layoutId, pgType: 0};
                nts.uk.ui.windows.setShared('layout', transferData);
                nts.uk.ui.windows.sub.modal("/view/ccg/031/a/index.xhtml").onClosed(() => {
                    let returnInfo: commonModel.TransferLayoutInfo = nts.uk.ui.windows.getShared("layout");
                    self.topPageModel().layoutId(returnInfo.layoutID);
                    self.changePreviewIframe(returnInfo.layoutID);
                });
            }

            private newTopPage() {
                var self = this;
                self.topPageModel(new TopPageModel());
                self.isNewMode(true);
                self.toppageSelectedCode("");
                $("#inp_code").focus();
                nts.uk.ui.errors.clearAll()
            }

            private removeTopPage() {
                var self = this;
                nts.uk.ui.dialog.confirm("選択中のデータを削除しますか？").ifYes(function() {
                    var removeCode = self.toppageSelectedCode();
                    var removeIndex = self.getIndexOfRemoveItem(removeCode);
                    var listLength = self.listTopPage().length;
                    service.deleteTopPage(self.toppageSelectedCode()).done(function() {
                        //delete success
                    }).fail();
                    self.loadTopPageList().done(function() {
                        var lst = self.listTopPage();
                        if (lst.length > 0) {
                            if (removeIndex < listLength - 1) {
                                self.toppageSelectedCode(lst[removeIndex].code);
                            }
                            else {
                                self.toppageSelectedCode(lst[removeIndex - 1].code);
                            }
                        }
                    });
                }).ifNo(function() {

                });
            }
            //for frame review layout
            private changePreviewIframe(layoutID: string): void {
                if (!nts.uk.util.isNullOrEmpty(layoutID))
                    $("#preview-iframe").attr("src", "/nts.uk.com.web/view/ccg/common/previewWidget/index.xhtml?layoutid=" + layoutID);
                else
                    $("#preview-iframe").attr("src", "");
            }
            private getIndexOfRemoveItem(code: string): number {
                var self = this;
                var ind = 0;
                self.listTopPage().forEach(function(item, index) {
                    if (item.code == code) {
                        ind = index;
                    }
                });
                return ind;
            }
        }

        export class Node {
            code: string;
            name: string;
            nodeText: string;
            custom: string;
            childs: Array<Node>;
            constructor(code: string, name: string, childs: Array<Node>) {
                var self = this;
                self.code = code;
                self.name = name;
                self.nodeText = name;
                self.childs = childs;
                self.custom = 'Random' + new Date().getTime();
            }
        }

        export class TopPageModel {
            topPageCode: KnockoutObservable<string>;
            topPageName: KnockoutObservable<string>;
            placement: KnockoutObservableArray<PlacementModel>;
            layoutId: KnockoutObservable<string>;
            constructor() {
                this.topPageCode = ko.observable('');
                this.topPageName = ko.observable('');
                this.placement = ko.observableArray([]);
                this.layoutId = ko.observable('');
            }
        }

        export class PlacementModel {
            row: KnockoutObservable<number>;
            column: KnockoutObservable<number>;
            topPagePart: KnockoutObservable<TopPagePartModel>;
            constructor() {
                this.row = ko.observable(0);
                this.column = ko.observable(0);
                this.topPagePart = ko.observable(new TopPagePartModel());
            }
        }

        export class TopPagePartModel {
            topPagePartType: KnockoutObservable<number>;
            topPagePartCode: KnockoutObservable<string>;
            topPagePartName: KnockoutObservable<string>;
            width: KnockoutObservable<number>;
            height: KnockoutObservable<number>;
            constructor() {
                this.topPagePartType = ko.observable(0);
                this.topPagePartCode = ko.observable("");
                this.topPagePartName = ko.observable("");
                this.width = ko.observable(0);
                this.height = ko.observable(0);
            }
        }

        export class ItemCbbModel {
            code: string;
            name: string;
            label: string;
            constructor(code: string, name: string) {
                this.code = code;
                this.name = name;
            }
        }

    }
}