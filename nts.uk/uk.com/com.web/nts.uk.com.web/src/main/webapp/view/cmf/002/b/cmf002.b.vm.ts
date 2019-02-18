module nts.uk.com.view.cmf002.b.viewmodel {
    import close = nts.uk.ui.windows.close;
    import getText = nts.uk.resource.getText;
    import dialog  = nts.uk.ui.dialog;
    import model = cmf002.share.model;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import block = nts.uk.ui.block;
    import modal = nts.uk.ui.windows.sub.modal;
    export class ScreenModel {
        isNewMode:                      KnockoutObservable<boolean> = ko.observable(true);
        checkFocus:                     KnockoutObservable<boolean> = ko.observable(true);
        standType:                      KnockoutObservable<number> = ko.observable(1);
        index:                          KnockoutObservable<number> = ko.observable(0);  
        conditionSettingList:           KnockoutObservableArray<IConditionSet> = ko.observableArray([]);
        outputItemList:                 KnockoutObservableArray<IOutputItem>   = ko.observableArray([]);
        selectedConditionSetting:       KnockoutObservable<IConditionSet> = ko.observable();
        selectedConditionSettingCode:   KnockoutObservable<string> = ko.observable('');
        notUseAtrItems:                 KnockoutObservableArray<model.ItemModel> = ko.observableArray(getNotUseAtrItems());
        delimiterItems:                 KnockoutObservableArray<model.ItemModel> = ko.observableArray(getDelimiterItems());
        stringFormatItems:              KnockoutObservableArray<model.ItemModel> = ko.observableArray(getStringFormatItems());
        listCategory:                   KnockoutObservableArray<Category> = ko.observableArray([]);
        categoryName:                   KnockoutObservable<string>       = ko.observable('');
        outItemCd:                      KnockoutObservable<string>       = ko.observable('');
        roleAuthority: any;
        conditionSetData:               KnockoutObservable<ConditionSet> = ko.observable(new ConditionSet ({
            cId: '',
            conditionSetCode: '',
            conditionSetName: '',
            categoryId: '',
            conditionOutputName: 0,
            autoExecution: 1,
            delimiter: 1,
            stringFormat: 0,
            itemOutputName: 0
        }));
        checkFocusWhenCopy: boolean = false;

        constructor() {
            block.invisible();
            let self = this;
            self.roleAuthority = getShared("CMF002B_PARAMS");
            self.index(0);
            block.clear();
            self.selectedConditionSettingCode.subscribe((data) => {
                if (data == '') {
                    self.createNewCondition();
                } else {
                    nts.uk.ui.errors.clearAll();
                    block.invisible();
                    self.index(self.getIndex(data));
                    self.selectedConditionSetting(self.conditionSettingList()[self.index()]);
                    self.getOutItem(data);
                    self.settingCurrentCondition();
                    self.setNewMode(false);
                    block.clear();
                }
            });

            self.checkFocus.subscribe((data) => {
               let self = this;
               if(self.checkFocusWhenCopy){
                   $("#B5_2").focus();
                   self.checkFocusWhenCopy = false;
                   return;
               }
               if (data) {
                   $("#B5_1").focus();
               } else {
                   $("#B3_3_container").focus();
               }
            });

        }

        setNewMode(mode: boolean) {
            let self = this;
            self.isNewMode(mode);
            if (self.checkFocus() == mode) {
                self.checkFocus.valueHasMutated();
            } else {
                self.checkFocus(mode);
            }
        }
        
        setCondSetCode(code: string) {
            let self = this;
            self.selectedConditionSettingCode('');
            self.selectedConditionSettingCode(code);
        }

        /**
         * 起動する
         * アルゴリズム「外部出力条件設定一覧」を実行する
         */
        initScreen(conditionSetCode :string){
            block.invisible();
            let self = this;
            let itemList: Array<IConditionSet> = [];
            let conditionSetCodeParam: string = '';
            self.standType(1);
            //アルゴリズム「外部出力取得設定一覧」を実行する
            service.getCndSet(self.roleAuthority).done((itemList: Array<IConditionSet>) =>{
                self.conditionSettingList.removeAll();
                if (itemList && itemList.length > 0) {
                    self.conditionSettingList(itemList);
                    if (conditionSetCode) {
                        self.setCondSetCode(conditionSetCode);
                        self.index(self.getIndex(conditionSetCode));
                    }                    
                    let code = self.conditionSettingList()[self.index()].conditionSetCode;
                    self.setCondSetCode(code);
                    setTimeout(function(){ 
                        $("tr[data-id='" + code + "'] ").focus();
                        self.setNewMode(false);
                    }, 100);
                } else {
                    self.createNewCondition();
                }
            }).always(() => {
                block.clear();
            });
            
        }


        /**
         * Setting each item on screen
         */
        settingCurrentCondition() {
            let self = this;
            if (!self.conditionSettingList()) {
                return;
            }
            let condSet: IConditionSet = self.conditionSettingList()[self.index()],
                categoryId: string = condSet.categoryId,
                categoryName: string = self.getCategoryName(categoryId);
            self.conditionSetData().cId(condSet.cId);
            self.conditionSetData().conditionSetCode(condSet.conditionSetCode);
            self.conditionSetData().conditionSetName(condSet.conditionSetName);
            self.conditionSetData().categoryId(categoryId);
            
            if (self.listCategory() && self.listCategory().length > 0 && categoryName) {
                self.categoryName(categoryId + "　" + categoryName);
            } else {
                self.categoryName("");                
            }
            self.conditionSetData().conditionOutputName(condSet.conditionOutputName);
            self.conditionSetData().autoExecution(condSet.autoExecution);
            self.conditionSetData().delimiter(condSet.delimiter);
            self.conditionSetData().stringFormat(condSet.stringFormat);
            self.conditionSetData().itemOutputName(condSet.itemOutputName);
        }
        
        getOutItem(selectedConditionSettingCode: string){
            let self = this;
            let itemList: Array<IOutputItem> = [];
            if (selectedConditionSettingCode) {
                block.invisible();
                self.outputItemList.removeAll();
                service.outSetContent(selectedConditionSettingCode, self.standType()).done((itemList: Array<IOutputItem>) =>{
                    if (itemList && itemList.length > 0) {
                        self.outputItemList(itemList);
                    }
                }).always(() => {
                    block.clear();
                });
            }
        }
        
        
        getCategoryName(cateId){
            let self = this;
            let category :Category = _.find(self.listCategory(), function (x) { return x.categoryId == cateId; });
            if (category) {
                return category.categoryName;
            }
        }
        
        getIndex(conditionCode) {
            let self = this;
            let index = _.findIndex(self.conditionSettingList(), { 'conditionSetCode': conditionCode });
            return index;
        }
        
        
        
        deleteCnd() {
            let self = this;
            dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {
                let data :any= {
                    conditionSetCd: self.selectedConditionSettingCode()
                }
                service.deleteCnd(data).done(result => {
                    dialog.info({ messageId: "Msg_16" }).then(() => {
                        let index = 0;
                        if (self.index() != self.conditionSettingList().length - 1){
                            index = self.index() + 1;
                        } else {
                            index = self.index() - 1;
                        }
                        if (self.conditionSettingList().length != 1) {
                            self.initScreen(self.conditionSettingList()[index].conditionSetCode);
                        } else {
                            self.initScreen(null);
                        }
                    }); 
                });
             });
        }
        
        openCopyScreen() {
            let self = this;
            setShared('CMF002_T_PARAMS', {
                    standType:self.standType() , 
                    conditionSetCd: self.selectedConditionSetting().conditionSetCode , 
                    conditionName: self.selectedConditionSetting().conditionSetName});
            
            modal("/view/cmf/002/t/index.xhtml").onClosed(function() {
                let params = getShared('CMF002_T_Output');
                
                if (params) {
                    let override = params.overWrite;
                    let destinationCode = params.copyDestinationCode;
                    let destinationName = params.destinationName;
                    let result = params.result;
                    let copyParams: any = {
                                            newMode: self.isNewMode(),
                                            standType: self.standType(),
                                            destinationCode: destinationCode,
                                            destinationName: destinationName,
                                            categoryId: self.conditionSetData().categoryId(),
                                            conditionSetCode: self.conditionSetData().conditionSetCode(),
                                            conditionSetName: self.conditionSetData().conditionSetName(),
                                            conditionOutputName: self.conditionSetData().conditionOutputName(),
                                            autoExecution: self.conditionSetData().autoExecution(),
                                            delimiter: self.conditionSetData().delimiter(),
                                            itemOutputName: self.conditionSetData().itemOutputName(),
                                            stringFormat: self.conditionSetData().stringFormat()
                    };
                    service.copy(copyParams).done(()=> {
                        dialog.info({ messageId: "Msg_15" }).then(() => {
                            if (destinationCode == self.selectedConditionSettingCode()){
                                self.conditionSetData().conditionSetName(destinationName);
                            }
                            self.checkFocusWhenCopy = true;
                            self.initScreen(destinationCode);
                        });  
                    });
                }
            });
        }
        
        
        openVScreen(){
            let self = this;
            setShared('CMF002_V_PARAMS', {
                categoryId :self.conditionSetData().categoryId() || '',
                roleAuthority: self.roleAuthority
            });
 
            modal("/view/cmf/002/v1/index.xhtml").onClosed(function() {
                let params = getShared('CMF002_B_PARAMS');
                if (params) {
                    self.conditionSetData().categoryId(params.categoryId);
                    self.categoryName(params.categoryId + "　" + params.categoryName);
                    nts.uk.ui.errors.clearAll();
                }
            });
           
        }

        openDscreen(){
            let self = this;
            setShared('CMF002_D_PARAMS', {
                    categoryName: self.categoryName(),
                    categoryId: self.conditionSetData().categoryId(),
                    cndSetCd: self.conditionSetData().conditionSetCode(),
                    cndSetName: self.conditionSetData().conditionSetName()
                    });
            modal("/view/cmf/002/d/index.xhtml");
            
        }
        
        openCscreen(){
            let self = this;
            setShared('CMF002_C_PARAMS_FROM_B', {
                    conditionSetCode: self.conditionSetData().conditionSetCode(),
                    conditionSetName: self.conditionSetData().conditionSetName(),
                    categoryId: self.conditionSetData().categoryId(),
                    categoryName: self.categoryName(),
                    standType: self.standType()
            });
            
            modal("/view/cmf/002/c/index.xhtml").onClosed(function() {
                let params = getShared('CMF002_B_PARAMS_FROM_C');
                let data :any = {
                    conditionSetCd: self.conditionSetData().conditionSetCode(),
                    standType: self.standType()
                }
                if (params.isUpdateExecution) {
                    self.getOutItem(data.conditionSetCd);
                }
                
            });
        }
        
        createNewCondition() {
            let self = this;
            let outputItem: Array<IOutputItem> = [];
            nts.uk.ui.errors.clearAll();
            self.setCondSetCode('');
            self.selectedConditionSetting(null);
            self.outputItemList(outputItem);
            self.categoryName('');
            self.conditionSetData().cId('');
            self.conditionSetData().conditionSetCode('');
            self.conditionSetData().conditionSetName('');
            self.conditionSetData().categoryId('');
            self.conditionSetData().conditionOutputName(0);
            self.conditionSetData().autoExecution(1);
            self.conditionSetData().delimiter(1);
            self.conditionSetData().stringFormat(0);
            self.conditionSetData().itemOutputName(0);
            self.setNewMode(true);
        }
           
    
        register(){
            let self = this;
            nts.uk.ui.errors.clearAll();
            $("#B5_1").trigger("validate");
            $("#B5_2").trigger("validate");
            if (!self.categoryName() || self.categoryName().trim() == '') {
                var CMF002_43 = resource.getText('CMF002_43');
                $('#B6_2').ntsError('set', resource.getMessage("MsgB_2", [CMF002_43]), "MsgB_2");
            }
            if (nts.uk.ui.errors.hasError()) {
               return;
            }
            let data :any = {
                             conditionSetCd: self.conditionSetData().conditionSetCode(),
                             categoryId: self.conditionSetData().categoryId(),
                             delimiter: self.conditionSetData().delimiter(),
                             itemOutputName: self.conditionSetData().itemOutputName(),
                             autoExecution: self.conditionSetData().autoExecution(),
                             conditionSetName: self.conditionSetData().conditionSetName(),
                             stringFormat: self.conditionSetData().stringFormat(),
                             conditionOutputName: self.conditionSetData().conditionOutputName(),
                             standType: self.standType(),
                             newMode: self.isNewMode(),
                             listStandardOutputItem: self.outputItemList()
            };
            service.register(data).done(result => {
                dialog.info({ messageId: "Msg_15" }).then(() => {
                    self.initScreen(data.conditionSetCd);
                    if(self.outputItemList() && self.outputItemList().length > 0) {
                        self.getOutItem(data.conditionSetCd);
                    }
                });
            }).fail(function(res: any) {
                if(res)
                    dialog.alertError(res);
            });
      
        }
        
        startPage(): JQueryPromise<any> {
            let self = this;
            
            if (!self.roleAuthority) {
                self.listCategory(null);
                return;
            }
            let dfd = $.Deferred();
            block.invisible();
            $.when(
                service.getCategory(self.roleAuthority)
            ).done((
                data: Array<Category>)=> {
                if(data && data.length > 0) {
                    self.listCategory(data);
                }
                self.initScreen(null);
                dfd.resolve(self);
            }).fail((error) => {
                dialog.alertError(error);
                dfd.reject();
            }).always(() => {
                self.focusUpDown();
                block.clear();
            });

            return dfd.promise();
        }

        focusUpDown() {
            $("#B14_2-up").click(() => {
                $("#B13_2_container").focus();
            })
            $("#B14_2-down").click(() => {
                $("#B13_2_container").focus();
            })
        }

    }
    //条件名出力選択, 項目名出力選択
    export function getNotUseAtrItems(): Array<model.ItemModel> {
        return [
            new model.ItemModel(1, getText('CMF002_47')),
            new model.ItemModel(0, getText('CMF002_48'))
        ];
    }

    //区切り文字選択
    export function getDelimiterItems(): Array<model.ItemModel> {
        return [
            new model.ItemModel(0, getText('CMF002_358')),
            new model.ItemModel(1, getText('CMF002_359')),
            new model.ItemModel(2, getText('CMF002_360')),
            new model.ItemModel(3, getText('CMF002_361')),
            new model.ItemModel(4, getText('CMF002_362'))
        ];
    }

    //文字列形式選択
    export function getStringFormatItems(): Array<model.ItemModel> {
        return [
            new model.ItemModel(0, getText('CMF002_363')),
            new model.ItemModel(1, getText('CMF002_364')),
            new model.ItemModel(2, getText('CMF002_365'))
        ];
    }

    export interface IConditionSet {
        cId: string;
        conditionSetCode: string;
        conditionSetName: string;
        categoryId: string;
        conditionOutputName: number;
        autoExecution: number;
        delimiter: number;
        stringFormat: number;
        itemOutputName:string;
    }

    export class ConditionSet {
        cId:                  KnockoutObservable<string> = ko.observable('');
        conditionSetCode:     KnockoutObservable<string> = ko.observable('');
        conditionSetName:     KnockoutObservable<string> = ko.observable('');
        categoryId:           KnockoutObservable<string> = ko.observable('');
        conditionOutputName:  KnockoutObservable<number> = ko.observable(0);
        autoExecution:        KnockoutObservable<number> = ko.observable(1);
        delimiter:            KnockoutObservable<number> = ko.observable(0);
        stringFormat:         KnockoutObservable<number> = ko.observable(0);
        itemOutputName:       KnockoutObservable<string> = ko.observable('');
        constructor(param: IConditionSet) {
            let self = this;
            self.cId(param.cId);
            self.conditionSetCode(param.conditionSetCode || '');
            self.conditionSetName(param.conditionSetName || '');
            self.categoryId(param.categoryId || '');
            self.conditionOutputName(param.conditionOutputName || 0);
            self.autoExecution(param.autoExecution || 1);
            self.delimiter(param.delimiter || 0);
            self.stringFormat(param.stringFormat || 0);
            self.itemOutputName(param.itemOutputName || '');
        }
    }

    export interface IOutputItem {
        outItemCd: string;
        outItemName: string;
        order: number;
    }
    
    export class OutputItem {
        outItemCd: KnockoutObservable<string> = ko.observable('');
        outItemName: KnockoutObservable<string> = ko.observable('');
        order: KnockoutObservable<number> = ko.observable(0);
        constructor(param: IOutputItem) {
            let self = this;
            self.outItemCd(param.outItemCd || '');
            self.outItemName(param.outItemName || '');
            self.order(param.order || 0);
        }
    }

    export interface ICategory {
        categoryId: string;
        categoryName: string;
    }
    
    export class Category {
        categoryId:   KnockoutObservable<string> = ko.observable('');
        categoryName: KnockoutObservable<string> = ko.observable('');
        constructor(param: ICategory) {
            let self = this;
            self.categoryId(param.categoryId || '');
            self.categoryName(param.categoryName || '');
        }
    }
 }

