module cmm008.a.viewmodel{
    import option = nts.uk.ui.option;
    
    export class ScreenModel {
        employmentCode: KnockoutObservable<string>;
        employmentName: KnockoutObservable<string>;
        textEditorOption: KnockoutObservable<any>;
        isCheckbox: KnockoutObservable<Boolean>;
        
        //search box
        dataSource: KnockoutObservableArray<service.model.employmentDto>;;
        columns: KnockoutObservableArray<any>;
        currentCode: KnockoutObservable<string>;
        singleSelectedCode: any;
//        headers: any;
        //締め日 DATA COMBOBOX
        closeDateList: KnockoutObservableArray<ItemCloseDate>;
        selectedCloseCode: KnockoutObservable<string>;
        //公休の管理
        managementHolidays: KnockoutObservableArray<any>;
        holidayCode: KnockoutObservable<number>;
        //処理日区分
        processingDateList: KnockoutObservableArray<ItemProcessingDate>;
        selectedProcessNo: KnockoutObservable<number>;
        //外部コード
        employmentOutCode: KnockoutObservable<string>;
        isEnable: KnockoutObservable<boolean>;
        //memo
        multilineeditor: any;  
        //delete
        isDelete: KnockoutObservable<boolean>;
        //message
        lstMessage: KnockoutObservableArray<ItemMessage>;
        //check display mess
        isMess: KnockoutObservable<boolean>;
        //get 就業権限
        isUseKtSet: KnockoutObservable<number>;
        constructor(){
            var self = this;
            self.employmentName = ko.observable("");
            self.isCheckbox = ko.observable(false);
            self.closeDateList = ko.observableArray([]);
            self.selectedCloseCode = ko.observable('システム未導入');
            self.managementHolidays = ko.observableArray([]);
            self.holidayCode = ko.observable(0);
            self.processingDateList = ko.observableArray([]);
            self.selectedProcessNo = ko.observable(0);
            self.employmentOutCode = ko.observable("");
            self.textEditorOption = ko.mapping.fromJS(new option.TextEditorOption());
            self.dataSource = ko.observableArray([]);
            self.currentCode = ko.observable("");
            self.employmentCode = ko.observable("");
            self.isEnable = ko.observable(false);
            self.isDelete = ko.observable(true);
            self.lstMessage = ko.observableArray([]);
            self.isMess = ko.observable(false);
            self.isUseKtSet = ko.observable(0);
            
            self.multilineeditor = {
                memoValue: ko.observable(""),
                constraint: '',
                option: ko.mapping.fromJS(new nts.uk.ui.option.MultilineEditorOption({
                    resizeable: true,
                    placeholder: "",
                    width: "",
                    textalign: "left"
                })),
                required: ko.observable(true),
            };
            
            
        }
        // start function
        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();
            var heightScreen = $(window).height();
            var widthScreen =  $(window).width();
            var heightHeader = $('#header').height() + $('#functions-area').height();
            var height = heightScreen-heightHeader - 80;
            $('#contents-left').css({height: height, width: widthScreen*40/100});
            $('#contents-right').css({height: height, width: widthScreen*60/100});
            
            self.listMessage();
            
            $.when(self.userKtSet()).done(function(){
                self.closeDateListItem();
                self.processingDateItem();
                self.managementHolidaylist();
                self.dataSourceItem();
                dfd.resolve(self.holidayCode()); 
            })  
            
            
            //list data click
            self.currentCode.subscribe(function(newValue){
                if(!self.checkChange(self.employmentCode())){
                    var AL001 = _.find(self.lstMessage(), function(mess){
                        return  mess.messCode === "AL001";
                    })
                    if(!self.isMess()){
                        nts.uk.ui.dialog.confirm(AL001.messName).ifCancel(function(){
                            self.isMess(true);
                            self.currentCode(self.employmentCode());
                            return;    
                        }).ifYes(function(){
                            self.isMess(false);
                            self.reloadScreenWhenListClick(newValue);
                        })   
                    }  
                }else{
                     self.reloadScreenWhenListClick(newValue);
                }
                if(self.isMess() && self.employmentCode() === newValue){
                    self.isMess(false);    
                }
                
            });
            return dfd.promise();
        }
        
        //就業権限
        userKtSet(): any {
            var def = $.Deferred();
            var self = this;
            service.getCompanyInfor().done(function(companyInfor: any){
                if(companyInfor !== undefined){
                    self.isUseKtSet(companyInfor.use_Kt_Set);
                    if(self.isUseKtSet() === 0){
                        $('.UseKtSet').css('display', 'none');
                    }
                }
                def.resolve(self.isUseKtSet());
            }).fail(function(res: any){
                nts.uk.ui.dialog.alert(res.messageId);
                def.reject();
            });
            return def.promise();
        }
        
        reloadScreenWhenListClick(newValue: string){
            var self = this;
            let newEmployment = _.find(self.dataSource(), function(employ){
                if(employ.employmentCode === newValue){
                    self.employmentCode(employ.employmentCode);                        
                    self.employmentName(employ.employmentName);
                    if(employ.closeDateNo === 0){
                        self.selectedCloseCode('システム未導入');    
                    }
                    self.selectedProcessNo(employ.processingNo);
                    self.multilineeditor.memoValue(employ.memo);
                    self.employmentOutCode(employ.employementOutCd); 
                    self.holidayCode(employ.statutoryHolidayAtr);
                    if(employ.displayFlg == 1){
                        self.isCheckbox(true);    
                    }else{
                        self.isCheckbox(false);    
                    }
                    self.isDelete(true);
                    self.isEnable(false); 
                    return;
                }
            })   
        }
        //締め日区分
        //今回は就業システム未導入の場合としてください。
        //（上記にあるように　締め日区分 = 0 ）
        closeDateListItem(): any{
            var self = this;
            self.closeDateList.removeAll();
            self.closeDateList.push(new ItemCloseDate(0,'システム未導入'));
        }
        //公休の管理
        managementHolidaylist(): any{
           
            var self = this;
            self.managementHolidays = ko.observableArray([
                {code: 0, name: 'する'},
                {code: 1, name: 'しない'}
            ]);
            self.holidayCode = ko.observable(0);
        }
        //list  message
        listMessage(): any{
            var self = this;
            self.lstMessage.push(new ItemMessage("ER001", "*が入力されていません。"));
            self.lstMessage.push(new ItemMessage("ER005","入力した*は既に存在しています。\r\n*を確認してください。"));
            self.lstMessage.push(new ItemMessage("ER010","対象データがありません。"));
            self.lstMessage.push(new ItemMessage("AL001","変更された内容が登録されていません。\r\nよろしいですか。"));
            self.lstMessage.push(new ItemMessage("AL002", "データを削除します。\r\nよろしいですか？"));
            self.lstMessage.push(new ItemMessage("ER026", "更新対象のデータが存在しません。"));
        }
        
        //処理日区分 を取得する
        processingDateItem(): any{
            var self = this;
            service.getProcessingNo().done(function(lstProcessingNo: any){
                 if(lstProcessingNo.length !== 0){
                     _.forEach(lstProcessingNo, function(processingNo){
                        self.processingDateList.push(new ItemProcessingDate(processingNo.processingNo, processingNo.processingName));
                     })
                          
                 }
            }).fail(function(res: any){
                var ER010 = _.find(self.lstMessage(), function(mess){
                    return  mess.messCode === "ER010";
                })
                nts.uk.ui.dialog.alert(ER010.messName);
            })
        }
        
        dataSourceItem(): any {
            var self = this;
            var dfd = $.Deferred<any>();
            self.dataSource([]);
            $.when(service.getAllEmployments()).done(function(listResult : Array<service.model.employmentDto>){
                if(listResult.length === 0 || listResult === undefined){
                    self.isEnable(true); 
                    self.isDelete(false);   
                }else{
                    self.isEnable(false);
                    self.isDelete(true);
                    _.forEach(listResult, function(employ){
                        if (employ.displayFlg == 1) {
                            employ.displayStr = "<span style='color: #00B050; font-size: 18px'>●</span>";    
                        } else {
                            employ.displayStr = "";
                        }
                        if(employ.closeDateNo === 0){
                            employ.closeDateNoStr = "システム未導入";
                        }
                        //get processing name
                        var process = _.find(self.processingDateList(), function(processNo){
                            return employ.processingNo == processNo.processingNo;
                        })
                        if(process !== undefined)
                            employ.processingStr = process.processingName;
                        else
                            employ.processingStr = "";
                        self.dataSource.push(employ); 
                    })
                    if(self.currentCode() === ""){
                        var obEmployment =_.first(self.dataSource()); 
                        self.currentCode(obEmployment.employmentCode);    
                    }
                }
                dfd.resolve(listResult);
            }) 
            if(self.isUseKtSet() === 0){
                this.columns = ko.observableArray([
                    { headerText: 'コード', prop: 'employmentCode', width: '30%' },
                    { headerText: '名称', prop: 'employmentName', width: '50%' },
                    { headerText: '初期表示', prop: 'displayStr', width: '20%' }
                ]);
            }else{
                this.columns = ko.observableArray([
                    { headerText: 'コード', prop: 'employmentCode', width: '18%' },
                    { headerText: '名称', prop: 'employmentName', width: '28%' },
                    { headerText: '締め日', prop: 'closeDateNoStr', width: '23%' },
                    { headerText: '処理日区分', prop: 'processingStr', width: '17%' },
                    { headerText: '初期表示', prop: 'displayStr', width: '14%' }
                ]);
            }
            self.singleSelectedCode = ko.observable(null);
            return dfd.promise();
        }
        
         //登録ボタンを押す
        createEmployment() : any{
            var self = this;
            //必須項目の未入力チェック
            var ER001 = _.find(self.lstMessage(), function(mess){
                 return  mess.messCode === "ER001";
            })
            if(self.employmentCode() === ""){
                nts.uk.ui.dialog.alert(ER001.messName.replace('*','コード'));   
                $("#inpCode").focus(); 
                return;
            }
            if(self.employmentName() === ""){
                nts.uk.ui.dialog.alert(ER001.messName.replace('*','名称'));  
                $("#inpName").focus();  
                return;
            }
            var employment = new service.model.employmentDto();
            employment.employmentCode = self.employmentCode();
            employment.employmentName = self.employmentName();
            //今回は就業システム未導入の場合としてください。
            //（上記にあるように　締め日区分 = 0 ）
            employment.closeDateNo = 0;
            employment.processingNo = self.selectedProcessNo();
            employment.statutoryHolidayAtr = self.holidayCode();
            employment.employementOutCd = self.employmentOutCode();
            employment.memo = self.multilineeditor.memoValue();
            if(self.dataSource().length === 0){
                self.isCheckbox(true);
            }
            if(self.isCheckbox())
                employment.displayFlg = 1;
            else
                employment.displayFlg = 0;
            //新規の時
            if(self.isEnable()){
                service.createEmployment(employment).done(function(){
                    $.when(self.dataSource()).done(function(){
                        $.when(self.dataSourceItem()).done(function(){
                            self.currentCode(employment.employmentCode);
                        })    
                    })
                }).fail(function(error: any){
                    var newMess = _.find(self.lstMessage(), function(mess){
                        return  mess.messCode === error.messageId;
                    })
                    nts.uk.ui.dialog.alert(newMess.messName.split('*').join('コード'));    
                    self.isEnable(true);
                    $("#inpCode").focus();
                })   
            //更新の時 
            }else{
                $.when(service.updateEmployment(employment)).done(function(){                    
                    $.when(self.dataSourceItem()).done(function(){
                        self.currentCode(employment.employmentCode);
                    })
                }).fail(function(res){
                    var newMess = _.find(self.lstMessage(), function(mess){
                        return  mess.messCode === res.messageId;
                    })
                    nts.uk.ui.dialog.alert(newMess.messName);    
                })                
            }
            
        }
        //新規ボタンを押す
        newCreateEmployment(): any{
            var self = this;
            //変更確認
            if(self.dataSource().length !== 0 && !self.checkChange(self.employmentCode())){
                var AL001 = _.find(self.lstMessage(), function(mess){
                    return  mess.messCode === "AL001";
                })
                nts.uk.ui.dialog.confirm(AL001.messName).ifCancel(function(){
                    return;    
                }).ifYes(function(){
                    self.clearItem();
                })            
            }else{
                self.clearItem();    
            }
        }
        //tu lam dirty check
        checkChange(employmentCodeChk: string): any{
            var self = this;
            let chkEmployment = _.find(self.dataSource(), function(employ){
                return employ.employmentCode == employmentCodeChk;    
            })
            if(chkEmployment !== undefined && chkEmployment !== null){
                if(chkEmployment.employmentName !== self.employmentName()
                    || chkEmployment.memo !== self.multilineeditor.memoValue()
                    || chkEmployment.processingNo !== self.selectedProcessNo()
                    || chkEmployment.statutoryHolidayAtr !== self.holidayCode()
                    || chkEmployment.employementOutCd !== self.employmentOutCode()
                    || chkEmployment.displayFlg !== (self.isCheckbox() ? 1 : 0)){
                    return false;    
                }else{
                    return true;    
                }
            }
            //sau khi nhan nut xoa thi khong check
            else if(self.employmentCode() !== "" && self.isEnable()){
                return false;    
            }else{
                return true;    
            }
        }
        
        clearItem(): any{
            var self = this;
            self.employmentCode("");
            self.employmentName("");
            self.isEnable(true);
            self.multilineeditor.memoValue("");
            self.employmentOutCode("");
            self.currentCode("");
            self.isCheckbox(false);
            self.isDelete(false);
            self.holidayCode(0);
            self.selectedProcessNo(0);
            $("#inpCode").focus();
        }
        
        //削除
        deleteEmployment(): any{
            var self = this;
            var AL002 = _.find(self.lstMessage(), function(mess){
                return  mess.messCode === "AL002";
            })
            nts.uk.ui.dialog.confirm(AL002.messName).ifCancel(function(){
               return; 
            }).ifYes(function(){
                var employment = new service.model.employmentDto();
                employment.employmentCode = self.employmentCode();
                if(self.isCheckbox())
                    employment.displayFlg = 1;
                else
                    employment.displayFlg = 0;
                let indexItemDelete = _.findIndex(self.dataSource(), function(item) { return item.employmentCode == self.employmentCode(); });
                service.deleteEmployment(employment).done(function(){
                    $.when(self.dataSourceItem()).done(function(){
                        if (self.dataSource().length === 0) {
                            self.isEnable(true);
                            self.isDelete(false);
                            self.newCreateEmployment();
                        } else if (self.dataSource().length === indexItemDelete) {
                            self.isEnable(false);
                            self.isDelete(true);
                            self.currentCode(self.dataSource()[indexItemDelete - 1].employmentCode);
                        } else {
                            self.isEnable(false);
                            self.isDelete(true);
                            if(indexItemDelete > self.dataSource().length) {
                                self.currentCode(self.dataSource()[0].employmentCode);
                            } else {
                                self.currentCode(self.dataSource()[indexItemDelete].employmentCode);
                            }
                        }
                    })
                }).fail(function(res: any){
                    var delMess = _.find(self.lstMessage(), function(mess){
                        return  mess.messCode === res.messageId;
                    })
                    nts.uk.ui.dialog.alert(delMess.messName);
                })    
            })
        }
    }   
    
    export class ItemCloseDate{
        closeDateCode: number;
        closeDatename: string;
        constructor(closeDateCode: number, closeDatename: string){
            this.closeDateCode = closeDateCode;
            this.closeDatename = closeDatename;    
        }
    }
    
    export class ItemProcessingDate{
        processingNo: number;
        processingName: string;
        constructor(processingNo: number, processingName: string){
            this.processingNo = processingNo;
            this.processingName = processingName;    
        }   
    }
   
    export class ItemMessage{
        messCode: string;
        messName: string;
        constructor(messCode: string, messName: string){
            this.messCode = messCode;
            this.messName = messName;    
        }    
    }
}