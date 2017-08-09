module nts.uk.at.view.kdw009.a.viewmodel {
    
    export class ScreenModel {
        // list business type A2_2
        lstBusinessType: KnockoutObservableArray<BusinessType>;
        // column in list
        gridListColumns: KnockoutObservableArray<any>;
        // selected code 
        selectedCode: KnockoutObservable<string>;
        // selected item
        selectedOption: KnockoutObservable<BusinessType>;
        // binding to text box name A3_3
        selectedName: KnockoutObservable<string>;
        // binding to text box code A3_2
        codeObject: KnockoutObservable<string>;
        // check new mode or not
        check: KnockoutObservable<boolean>;
        // check update or insert
        checkUpdate: KnockoutObservable<boolean>;
        constructor() {
            let self = this;
            self.gridListColumns = ko.observableArray([
                { headerText: nts.uk.resource.getText("KDW009_6"), key: 'businessTypeCode', width: 100 },
                { headerText: nts.uk.resource.getText("KDW009_7"), key: 'businessTypeName', width: 200, formatter: _.escape}
            ]);
            self.lstBusinessType = ko.observableArray([]);
            self.selectedCode = ko.observable("");
            self.selectedName = ko.observable("");
            self.selectedOption = ko.observable(null);
            self.check = ko.observable(false);
            self.codeObject = ko.observable("");
            self.checkUpdate = ko.observable(true);
            self.selectedCode.subscribe((businessTypeCode) => {
                if (businessTypeCode) {
                    let foundItem = _.find(self.lstBusinessType(), (item: BusinessType) => {
                        return item.businessTypeCode == businessTypeCode;
                    });
                    self.checkUpdate(true);
                    self.selectedOption(foundItem);
                    self.selectedName(self.selectedOption().businessTypeName);
                    self.codeObject(self.selectedOption().businessTypeCode)
                    self.check(false);
                }
            });
            self.startPage();
        }

        /** get data to list **/
        getData(): JQueryPromise<any>{
            let self = this;
            let dfd = $.Deferred();
            service.getAll().done((lstData: Array<viewmodel.BusinessType>) => {
                let sortedData = _.orderBy(lstData, ['businessTypeCode'], ['asc']);
                self.lstBusinessType(sortedData);
                dfd.resolve();
            }).fail(function(error){
                    dfd.reject();
                    alert(error.message);
                }) 
              return dfd.promise();      
        }

        /** get data when start dialog **/
        startPage(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            let array=[];
            let list=[];
            self.getData().done(function(){
                if(self.lstBusinessType().length == 0){
                self.newMode();
                return;
                }
                else{
                    self.selectedCode(self.lstBusinessType()[0].businessTypeCode.toString());
                }
            });
            return dfd.promise();
        }  
        
        /** update or insert data when click button register **/
        register() {
            let self = this;
            let code = "";  
            $("#inpPattern").trigger("validate");
            let updateOption = new BusinessType(self.selectedCode(), self.selectedName()); 
            code = self.codeObject();
            _.defer(() => {
                if (nts.uk.ui.errors.hasError() === false) {
                    // update item to list  
                    if(self.checkUpdate() == true){
                        service.update(updateOption).done(function(){
                            self.getData().done(function(){
                                self.selectedCode(code);
                                nts.uk.ui.dialog.info({ messageId: "Msg_15" }); 
                            });
                        });
                    }
                    else{
                        code = self.codeObject();
                        self.selectedOption(null);
                        let obj = new BusinessType(self.codeObject(), self.selectedName());
                        // insert item to list
                        service.insert(obj).done(function(){
                            self.getData().done(function(){
                                nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                                self.selectedCode(code);  
                            });
                        }).fail(function(res){
                            $('#inpCode').ntsError('set', res);
                        });
                    }
                }
            });    
            $("#inpPattern").focus();        
        } 
        //  new mode 
        newMode(){
            var t0 = performance.now(); 
            let self = this;
            self.check(true);
            self.checkUpdate(false);
            self.selectedCode("");
            self.codeObject("");
            self.selectedName("");
            $("#inpCode").focus(); 
            $("#inpCode").ntsError('clear');
            nts.uk.ui.errors.clearAll();                 
            var t1 = performance.now();
            console.log("Selection process " + (t1 - t0) + " milliseconds.");
        }
        /** remove item from list **/
        remove(){
            let self = this;
            let count = 0;
            for (let i = 0; i <= self.lstBusinessType().length; i++){
                if(self.lstBusinessType()[i].businessTypeCode == self.selectedCode()){
                    count = i;
                    break;
                }
            }
            nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(() => { 
                service.remove(self.selectedOption()).done(function(){
                    self.getData().done(function(){
                        // if number of item from list after delete == 0 
                        if(self.lstBusinessType().length==0){
                            self.newMode();
                            return;
                        }
                        // delete the last item
                        if(count == ((self.lstBusinessType().length))){
                            self.selectedCode(self.lstBusinessType()[count-1].businessTypeCode);
                            return;
                        }
                        // delete the first item
                        if(count == 0 ){
                            self.selectedCode(self.lstBusinessType()[0].businessTypeCode);
                            return;
                        }
                        // delete item at mediate list 
                        else if(count > 0 && count < self.lstBusinessType().length){
                            self.selectedCode(self.lstBusinessType()[count].businessTypeCode);    
                            return;
                        }
                        
                    });
                    nts.uk.ui.dialog.info({ messageId: "Msg_16" });
                });
            }).ifCancel(() => { 
            }); 
            $("#inpPattern").focus();
        }
    }
    export class BusinessType{
        businessTypeCode: string;
        businessTypeName: string;  
        constructor(businessTypeCode: string, businessTypeName: string){
            this.businessTypeCode = businessTypeCode;
            this.businessTypeName =businessTypeName;
        }
    }
}




