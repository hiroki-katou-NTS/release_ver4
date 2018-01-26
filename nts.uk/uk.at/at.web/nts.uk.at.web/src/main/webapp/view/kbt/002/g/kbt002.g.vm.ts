module nts.uk.at.view.kbt002.g {
    export module viewmodel {
        import modal = nts.uk.ui.windows.sub.modal;
        import setShared = nts.uk.ui.windows.setShared;
        import getShared = nts.uk.ui.windows.getShared;
        import windows = nts.uk.ui.windows;
        import block = nts.uk.ui.block;
        import dialog = nts.uk.ui.dialog;
        import getText = nts.uk.resource.getText;
        
        export class ScreenModel {
            execLog: any = {};
            sharedObj = {};
            modalLink = '';
            constructor() {
                let self = this;
            }
            
            // Start page
            start() {
                let self = this;
                var dfd = $.Deferred();
                var sharedData = nts.uk.ui.windows.getShared('inputDialogG');
                if (sharedData) {
                    self.execLog = sharedData.execLog;
                }
                
                dfd.resolve();
                return dfd.promise();
            }
            
            // 閉じる button
            closeDialog() {
                windows.close();
            }
            
            openDetailDialog(data, event){
                let self = this;
                block.grayout();
                service.getLogHistory(self.execLog.execItemCd, self.execLog.execId).done(function(logHistory) {
                    var taskId = $(event.target).data("code");
                    self.createLinkAndSharedObject(taskId, logHistory);
                    
                    setShared('inputDialogG', self.sharedObj);
                    modal(self.modalLink).onClosed(function(){
                        block.clear();
                    });
                });
                    
            }
            
            private createLinkAndSharedObject(taskId, logHistory) {
                let self = this;
                if (taskId == 0) { // スケジュールの作成
                    self.sharedObj = {execId : logHistory.execId,
                                     startDate : logHistory.schCreateStart,
                                     endDate : logHistory.schCreateEnd};
                    self.modalLink = "/view/ksc/001/k/index.xhtml";
                } else if (taskId == 1) { // 日別作成
                    self.sharedObj = { empCalAndSumExecLogID : logHistory.execId, //・就業計算と集計実行ログID
                                       executionContentName : '',
                                       executionContent : 0,  // 日別作成
                                       listTargetPerson : [], //・社員ID（list）  ・従業員の実行状況
                                       executionStartTime : logHistory.lastExecDateTime, //・実行開始日時
                                       objectPeriod : {start : logHistory.dailyCreateStart, end : logHistory.dailyCreateStart}, //・対象期間
                                       nameClosue : 0, //・選択した締め
                                       processingMonth : '' //・処理月
                                     };
                    self.modalLink = "/view/kdw/001/h/index.xhtml";
                } else if (taskId == 2) { // 日別計算
                    self.sharedObj = { empCalAndSumExecLogID : logHistory.execId, //・就業計算と集計実行ログID
                                       executionContentName : '',
                                       executionContent : 1,  // 日別計算
                                       listTargetPerson : [], //・社員ID（list）  ・従業員の実行状況
                                       executionStartTime : logHistory.lastExecDateTime, //・実行開始日時
                                       objectPeriod : {start : logHistory.dailyCreateStart, end : logHistory.dailyCreateStart}, //・対象期間
                                       nameClosue : 0, //・選択した締め
                                       processingMonth : '' //・処理月
                                     };
                    self.modalLink = "/view/kdw/001/h/index.xhtml";
                } else if (taskId == 3) { // 承認結果反映
                } else if (taskId == 4) { // 月別集計
                    self.sharedObj = { empCalAndSumExecLogID : logHistory.execId, //・就業計算と集計実行ログID
                                       executionContentName : '',
                                       executionContent : 3,  // 月別集計
                                       listTargetPerson : [], //・社員ID（list）  ・従業員の実行状況
                                       executionStartTime : logHistory.lastExecDateTime, //・実行開始日時
                                       objectPeriod : {}, //・対象期間
                                       nameClosue : 0, //・選択した締め
                                       processingMonth : '' //・処理月
                                     };
                    self.modalLink = "/view/kbt/002/g/index.xhtml";
                } else if (taskId == 5) { // アラーム抽出（個人別）
                } else if (taskId == 6) { // アラーム抽出（職場別）
                }
            }
        }
    }
}