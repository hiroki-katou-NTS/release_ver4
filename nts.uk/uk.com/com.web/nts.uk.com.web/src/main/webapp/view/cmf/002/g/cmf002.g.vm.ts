module nts.uk.com.view.cmf002.g.viewmodel {
    import close = nts.uk.ui.windows.close;
    import getText = nts.uk.resource.getText;
    import dialog = nts.uk.ui.dialog;
    import model = cmf002.share.model;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import block = nts.uk.ui.block;
    import modal = nts.uk.ui.windows.sub.modal;

    export class ScreenModel {
<<<<<<< HEAD
        items: KnockoutObservableArray<OutputCodeConvert>;
        columns: KnockoutObservableArray<NtsGridListColumn>;
        
        currentCode: KnockoutObservable<string>;
        currentItem: KnockoutObservable<CurrentOutputCodeConvertDetail>;
        
        cdConvertDetailList: KnockoutObservableArray<CdConvertDetail>;

        constructor() {
            let self = this;

            self.cdConvertDetailList = ko.observableArray();
=======
        codeConvertList: KnockoutObservableArray<OutputCodeConvert> = ko.observableArray([]);
        selectedConvertCode: KnockoutObservable<string> = ko.observable('');
        selectedConvertDetail: KnockoutObservable<number> = ko.observable(0);

        screenMode: KnockoutObservable<number>;

        codeConvertData: KnockoutObservable<OutputCodeConvert> = ko.observable(new OutputCodeConvert('', '', []));

        constructor() {
            let self = this;
            self.screenMode = ko.observable(model.SCREEN_MODE.UPDATE);
>>>>>>> 44d20521659ca83f7757779f1910e27eba779fa6
            $("#fixed-table").ntsFixedTable({ height: 300, width: 600 });
            
            
            self.selectedConvertCode.subscribe(function(convertCode: any) {
                if (convertCode) {
                    block.invisible();
                    service.getOutputCodeConvertByConvertCode(convertCode).done(function(data) {
                        if (data) {
                            self.codeConvertData().cdConvertDetails.removeAll();
                            
                            self.selectedConvertCode(data.convertCode);
                            
                            self.codeConvertData().convertCode(data.convertCode);
                            self.codeConvertData().convertName(data.convertName);
                            
                            var detail: Array<any> = _.sortBy(data.listCdConvertDetail, ['lineNumber']);
                            for (let i = 0; i < detail.length; i++) {
                                self.codeConvertData().cdConvertDetails.push(new CdConvertDetail(detail[i].lineNumber, detail[i].outputItem, detail[i].systemCode));
                            }

                            self.screenMode(model.SCREEN_MODE.UPDATE);

                            self.setFocusItem(FOCUS_TYPE.ROW_PRESS, model.SCREEN_MODE.UPDATE);
                        }
                    }).fail(function(error) {
                        dialog.alertError(error);
                    }).always(function() {
                        block.clear();
                    });
                }
            });
        }

<<<<<<< HEAD
            for (let i = 0; i < 5; i++) {
                self.items.push(new OutputCodeConvert('00' + i, '基本給'));
            }
=======
>>>>>>> 44d20521659ca83f7757779f1910e27eba779fa6

        initialScreen(convertCodeParam?: string) {
            let self = this;
            block.invisible();
            nts.uk.ui.errors.clearAll();

            service.getOutputCodeConvertByCompanyId().done(function(result: Array<any>) {
             
                if (result && result.length) {
                    let _codeConvertResult: Array<any> = _.sortBy(result, ['convertCode']);
                    let _codeConvertList: Array<OutputCodeConvert> = _.map(_codeConvertResult, x => {
                        return new OutputCodeConvert(x.convertCode, x.convertName, x.cdConvertDetails);
                    });
                    
                    self.screenMode(model.SCREEN_MODE.UPDATE);

                    let _codeConvert: string;
                    if (convertCodeParam) {
                        _codeConvert = convertCodeParam;
                    } else {
                        _codeConvert = _codeConvertList[0].convertCode();
                    }
                    self.selectedConvertCode(_codeConvert);

<<<<<<< HEAD
            self.currentCode = ko.observable();
            self.currentItem = ko.observable(new CurrentOutputCodeConvertDetail('', '', []));
            self.currentCode.subscribe(function(currentCode) {
                let result = _.find(self.items(), function(o) { return o.code === currentCode; });
                self.currentItem(new CurrentOutputCodeConvertDetail(result.code, result.name, [])); 
=======
                    self.codeConvertList(_codeConvertList);
                   
                } else {
                    self.screenMode(model.SCREEN_MODE.NEW);
                
                }
            }).fail(function(error) {
                dialog.alertError(error);
            }).always(function() {
                block.clear();
>>>>>>> 44d20521659ca83f7757779f1910e27eba779fa6
            });
        }


        addItem() {
            let self = this;
<<<<<<< HEAD
            self.cdConvertDetailList.push(new CdConvertDetail(0, '', ''));
=======
>>>>>>> 44d20521659ca83f7757779f1910e27eba779fa6
        }

        removeItem() {
            let self = this;
<<<<<<< HEAD
            self.cdConvertDetailList.pop();
=======
        }
        
        
         setFocusItem(focus: number, screenMode: number, index?: number) {
            let self = this;
            if (focus == FOCUS_TYPE.ADD_ROW_PRESS || focus == FOCUS_TYPE.DEL_ROW_PRESS) {
                $('tr[data-id=' + index + ']').find("input").first().focus();
            }
            _.defer(() => {nts.uk.ui.errors.clearAll()});
>>>>>>> 44d20521659ca83f7757779f1910e27eba779fa6
        }

        start(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();

            self.initialScreen();

            dfd.resolve();
            return dfd.promise();
        }
<<<<<<< HEAD
    }
    
    
    export class CurrentOutputCodeConvertDetail {
        code: KnockoutObservable<string>;
        name: KnockoutObservable<string>;
        cdConvertDetailList: KnockoutObservableArray<CdConvertDetail>;
        
         constructor(code: string, name: string, cdConvertDetail: Array<CdConvertDetail>){
             this.code = ko.observable(code);
             this.name = ko.observable(name);
             this.cdConvertDetailList = ko.observableArray(cdConvertDetail);
         }
    }
    

    export class OutputCodeConvert {
        code: string;
        name: string;
        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }
    }

    export class CdConvertDetail {
        lineNumber: KnockoutObservable<number>;
        code: KnockoutObservable<string>;
        name: KnockoutObservable<string>;

        constructor(lineNumber: number, code: string, name: string) {
            this.lineNumber = ko.observable(lineNumber);
            this.code = ko.observable(code);
            this.name = ko.observable(name);
=======
    } //end screenModel
 

    export enum FOCUS_TYPE {
        INIT = 0,
        ADD_PRESS = 1,
        REG_PRESS = 2,
        DEL_PRESS = 3,
        ROW_PRESS = 4,
        ADD_ROW_PRESS = 5,
        DEL_ROW_PRESS = 6
    }

 

    export class OutputCodeConvert {
        convertCode: KnockoutObservable<string>;
        dispConvertCode: string;
    
        convertName: KnockoutObservable<string>;
        dispConvertName: string;
    
        cdConvertDetails: KnockoutObservableArray<CdConvertDetail>;
    
        constructor(code: string, name: string, cdConvertDetails: Array<any>) {
            this.convertCode = ko.observable(code);
            this.dispConvertCode = code;
            this.convertName = ko.observable(name);
            this.dispConvertName = name;
            this.cdConvertDetails = ko.observableArray(cdConvertDetails);
        }
    }
    
    
    export class CdConvertDetail {
        lineNumber: KnockoutObservable<number>;
        outputItem: KnockoutObservable<string>;
        systemCode: KnockoutObservable<string>;
       
        
        
    
        constructor(lineNumber: number, outputItem: string, systemCode: string) {
            this.lineNumber = ko.observable(lineNumber);
            this.outputItem = ko.observable(outputItem);
            this.systemCode = ko.observable(systemCode);
>>>>>>> 44d20521659ca83f7757779f1910e27eba779fa6
        }
    }
}
