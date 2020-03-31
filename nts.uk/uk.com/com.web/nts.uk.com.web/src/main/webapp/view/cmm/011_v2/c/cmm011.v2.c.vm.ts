module nts.uk.com.view.cmm011.v2.c.viewmodel {
    // import getText = nts.uk.resource.getText;
    // import setShared = nts.uk.ui.windows.setShared;
    // import getShared = nts.uk.ui.windows.getShared;
    // import alertError = nts.uk.ui.dialog.alertError;

    export class ScreenModel {
        // itemList: KnockoutObservableArray<any>;
        // createMethod: KnockoutObservable<number>;
        // inputList: KnockoutObservableArray<InputItems>;
        // listColums: KnockoutObservableArray<any>;
        // targetHistId: KnockoutObservable<any>;
        // screenMode: number = 0;

        // constructor() {
        //     let self = this;
        //     self.inputList = ko.observableArray([]);
        //     let params = getShared("CMM011DParams");
        //     if (params) {
        //         self.screenMode = params.initMode;
        //         self.inputList(params.listDuplicate);
        //         self.inputList(_.orderBy(self.inputList(),['date'],['desc']));
        //     }
        //     self.itemList = ko.observableArray([
        //         new BoxModel(CREATE_METHOD.CREATE_NEW, self.screenMode == SCREEN_MODE.WORKPLACE ? getText('CMM011_208') : getText('CMM011_308')),
        //         new BoxModel(CREATE_METHOD.RECOVER_PAST, self.screenMode == SCREEN_MODE.WORKPLACE ? getText('CMM011_209') : getText('CMM011_309'))
        //     ]);
        //     self.createMethod = ko.observable(CREATE_METHOD.RECOVER_PAST);
         
        //     //Grid list columns
        //     self.listColums = ko.observableArray([
        //         {headerText: 'historyId' ,key: 'historyId', hidden: true},
        //         {headerText: getText('CMM011_130'), key: 'targetCode', width: 100, formatter: _.escape},
        //         {headerText: getText('CMM011_131'), key: 'targetName', width: 150, formatter: _.escape},
        //         {headerText: getText('CMM011_132'), key: 'deleteDate', width: 100, format: 'YYYY/MM/DD', formatter: _.escape}
        //     ]);
        //     this.targetHistId = ko.observable(null);
        // }

        // create() {
        //     let self = this;
        //     if (self.createMethod() == CREATE_METHOD.CREATE_NEW) {
        //         setShared("CMM011CParams", {targetId: null, historyId: null});
        //         nts.uk.ui.windows.close();
        //     } else if (self.targetHistId()) {
        //         let item = _.find(self.inputList(), x => x.historyId == self.targetHistId());
        //         setShared("CMM011CParams", {
        //             targetId: item.targetId,
        //             historyId: item.historyId
        //         });
        //         nts.uk.ui.windows.close();
        //     } else {
        //         alertError({messageId: "Msg_1504"});
        //     }
        // }

        // close() {
        //     nts.uk.ui.windows.close();
        // }
        
    }
    
    // enum CREATE_METHOD {
    //     CREATE_NEW = 1,
    //     RECOVER_PAST = 2
    // }
    
    // enum SCREEN_MODE {
    //     WORKPLACE = 0,
    //     DEPARTMENT = 1
    // }

    // class BoxModel {
    //     id: number;
    //     name: string;

    //     constructor(id, name) {
    //         let self = this;
    //         self.id = id;
    //         self.name = name;
    //     }
    // }

    // class InputItems {
    //     targetId: string;
    //     targetCode: string;
    //     targetName: string;
    //     deleteDate: string;
    //     historyId: string;

    //     constructor(targetId: string, code: string, name: string, date: string, historyId: string) {
    //         this.targetId = targetId;
    //         this.targetCode = code;
    //         this.targetName = name;
    //         this.deleteDate = date;
    //         this.historyId = historyId;
    //     }
    // }
}