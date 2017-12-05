module cas009.a.viewmodel {
    import EnumConstantDto = cas009.a.service.model.EnumConstantDto;
    import service = cas009.a.service;
    import ccg = nts.uk.com.view.ccg025.a;
    export class ScreenModel {
        listRole : KnockoutObservableArray<viewmodel.model.Role>; 
        roleCode: KnockoutObservable<string>;
        roleId: KnockoutObservable<string>;
        name : KnockoutObservable<string>;
        assignAtr : KnockoutObservable<number>;
        employeeReferenceRange: KnockoutObservable<number>;
        referFutureDate : KnockoutObservable<boolean>;
        createMode : KnockoutObservable<boolean>;
        enumAuthen: KnockoutObservableArray<any>; 
        enumAllow: KnockoutObservableArray<any>;
        enumRange:  KnockoutObservableArray<EnumConstantDto>;
        enumRangeChange:  KnockoutObservableArray<EnumConstantDto>;
        component: ccg.component.viewmodel.ComponentModel;
        isSelected: KnockoutObservable<boolean>;
        constructor() {
            var self = this;
                self.listRole = ko.observableArray([]);
                self.roleCode = ko.observable("");
                self.roleId = ko.observable("");
                self.employeeReferenceRange = ko.observable(0);
                self.name = ko.observable("");
                self.assignAtr = ko.observable(1);
                self.referFutureDate = ko.observable(true);
                self.createMode = ko.observable(true);
                self.isSelected =  ko.observable(true);
                self.enumAuthen = ko.observableArray([
                         { code: '0', name: nts.uk.resource.getText("CAS009_14") }, 
                         { code: '1', name: nts.uk.resource.getText("CAS009_15") },
                    ]);
                self.enumAllow = ko.observableArray([
                         { code: true, name: nts.uk.resource.getText("CAS009_18") }, 
                         { code: false, name: nts.uk.resource.getText("CAS009_19") },
                    ]);
                self.enumRange = ko.observableArray([]);
                self.enumRangeChange = ko.observableArray([]);
                self.assignAtr.subscribe(function(newValue){
                   if(self.enumRange().length>0){
                        if(newValue ==0)     self.enumRangeChange(self.enumRange().slice(0,1));
                        else  self.enumRangeChange(self.enumRange().slice(1,4));
                   } 
                });        
                self.component = new ccg.component.viewmodel.ComponentModel({ 
                    roleType: 8,
                    multiple: false  
                });
            
                self.component.currentCode.subscribe((newValue) => {
                        if(newValue !=""){
                            self.roleId(newValue);
                            let current = _.find(self.listRole(), function(o){return o.roleId == newValue});
                            self.roleCode(current.roleCode)
                            self.createMode(false);
                            self.name(current.name);
                            self.assignAtr(current.assignAtr);
                            self.employeeReferenceRange(current.employeeReferenceRange);
                            self.referFutureDate(current.referFutureDate );       
                             $(".nts-input").ntsError("clear");                    
                        }
    
                });            
        }

        /** Start Page */
       public  startPage(): any {           
            let self = this;
            service.getOptItemEnum().done((res) => {
                self.enumRange(res);    
                if(self.assignAtr() ==0)     self.enumRangeChange(self.enumRange().slice(0,1));
                else  self.enumRangeChange(self.enumRange().slice(1,4));
            });
            self.getListRole();
            self.isSelected(true);
            self.createNew();
        }
        
        
        public getListRole():JQueryPromise<any>{
            let  self = this;
            let dfd = $.Deferred();
            self.component.startPage().done(function(){
                let roleIds : Array<string> = _.map(self.component.listRole(), function(x){
                    return x.roleId;
                });
                if(roleIds.length>0){
                    service.getPersonInfoRole(roleIds).done((res) => {
                        
                        self.listRole(_.map(self.component.listRole(), function(x){
                            let personInfo : any = _.find(res, function(o){ return o.roleId == x.roleId  });
                            
                            return new model.Role(true, x.roleId, x.roleCode, x.employeeReferenceRange, x.name, x.assignAtr, personInfo.referFutureDate );                            
                        }));
                        self.listRole(_.sortBy(self.listRole(), o=>o.roleCode));
                        
                        dfd.resolve();                                                                                                
                    });
                }else{
                    dfd.resolve();                        
                }
            });
            return dfd.promise();
                
        } 
        
        

        public createNew(): any{
            let self = this;
                self.roleCode("");
                self.roleId("");
                self.employeeReferenceRange(0);
                self.name("");
                self.assignAtr(0);
                self.referFutureDate(false);
                self.createMode(true);
                self.component.currentCode("");
                $('#roleCode').focus();
                nts.uk.ui.errors.clearAll();
        }
        
        public save(): any{
            let self = this;
            $(".nts-input").trigger("validate");
            if($(".nts-input").ntsError("hasError")) return ;
            let role = new model.Role(self.createMode(), self.roleId(), self.roleCode(), self.employeeReferenceRange(), self.name(), self.assignAtr(), self.referFutureDate());
            service.saveRole(role).done(function(){
                 nts.uk.ui.dialog.alert({ messageId: "Msg_15" });  
                 
                    self.getListRole().done(()=>{
                           self.component.currentCode(_.find(self.listRole(), function(o){ return o.roleCode == role.roleCode  }).roleId);
                 });
          
            }).fail((error) => {
                 nts.uk.ui.dialog.alert({ messageId: error.messageId });
            });    
        }
        public remove(): any{
            let self = this;
               if(self.component.currentCode() !="" && self.component.currentCode() !=null){
                    nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(function() {
                        service.deleteRole({roleId: self.roleId(), assignAtr: self.assignAtr()}).done(function(){
                            nts.uk.ui.dialog.info({ messageId: "Msg_16" });
                            self.getListRole();
                            self.component.currentCode(self.listRole()[0].roleId);   
                        }).fail((error) => {
                                nts.uk.ui.dialog.alertError({ messageId: error.messageId });
                        });   
                   }); 
               }   
        }
        
    }

    export module model {
        export class Role {
            createMode: boolean;
            roleId: string;
            roleCode: string;
            employeeReferenceRange: number;
            name : string;
            assignAtr : number;
            referFutureDate : boolean;
            
            constructor(createMode: boolean, roleId: string, roleCode: string, employeeReferenceRange: number, 
                            name: string, assignAtr: number, referFutureDate: boolean) {
                this.createMode = createMode;
                this.roleId = roleId;
                this.roleCode = roleCode;
                this.employeeReferenceRange = employeeReferenceRange;
                this.name = name;
                this.assignAtr = assignAtr;
                this.referFutureDate = referFutureDate;
            }
        }
    }
}