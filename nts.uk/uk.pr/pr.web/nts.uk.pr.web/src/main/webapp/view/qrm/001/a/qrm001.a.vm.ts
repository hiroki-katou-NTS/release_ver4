module qrm001.a.viewmodel {
    export class ScreenModel {
        //Employee Process Value
        employeeInfo: KnockoutObservable<EmployeeInfo>;
        employeeList: KnockoutObservableArray<EmployeeInfo>;
        currentEmployee: KnockoutObservable<EmployeeInfo>;
        currentEmployeeCode: KnockoutObservable<any>;
        currentEmployeeIndex: KnockoutObservable<any>;
        currentEmployeeIndexOld: KnockoutObservable<any>;
        previous: KnockoutObservable<any>;
        next: KnockoutObservable<any>;
        dirty: nts.uk.ui.DirtyChecker;
        
        //Retirement Payment Value
        date: KnockoutObservable<any>;
        personCom: KnockoutObservable<PersonCom>;
        retirementPaymentKeyList: KnockoutObservableArray<RetirementPaymentKey>;
        retirementPaymentList: KnockoutObservableArray<RetirementPayment>;
        retirementPaymentCurrent: KnockoutObservable<RetirementPayment>;
        bankTransferList: KnockoutObservableArray<PersonBankAccount>;
        fullSetSelect: KnockoutObservableArray<any>;
        select6List: KnockoutObservableArray<any>;
        select6Code: KnockoutObservableArray<KnockoutObservable<any>>;
        isUpdate: KnockoutObservable<boolean>;
        
        constructor() {
            var self = this;
            
            //Retirement Payment Process
            self.bankTransferList = ko.observableArray([
                new PersonBankAccount('bank1', 'branch1', '0001'),
                new PersonBankAccount('bank2', 'branch2', '0002'),
                new PersonBankAccount('bank3', 'branch3', '0003'),
                new PersonBankAccount('bank4', 'branch4', '0004'),
                new PersonBankAccount('bank5', 'branch5', '0005')
            ]);
            
            self.fullSetSelect = ko.observableArray([
                    { code: 0, name: '使用しない' },
                    { code: 1, name: '支給1' },
                    { code: 2, name: '支給2' },
                    { code: 3, name: '支給3' },
                    { code: 4, name: '支給4' },
                    { code: 5, name: '支給5' }]);
            
            self.isUpdate = ko.observable(false);
            self.retirementPaymentKeyList = ko.observableArray([new RetirementPaymentKey("A0001","2016-12-28")]);
            self.retirementPaymentList = ko.observableArray([]);
            self.retirementPaymentCurrent = ko.observable(new RetirementPayment('0','0',0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'0','0') );
            
            self.select6List = ko.observableArray(self.refreshSelect(self.fullSetSelect, self.retirementPaymentCurrent, 5));
            self.select6Code = ko.observableArray([ko.observable(1),ko.observable(2),ko.observable(3),ko.observable(4),ko.observable(5)]);
            self.select6Code().forEach(function(item,index){
                self.select6Code()[index].subscribe(function(value){
                    self.retirementPaymentCurrent()["bankTransferOption"+(index+1)](value);  
                    self.retirementPaymentCurrent()["option"+(index+1)+"Money"](value?value:null); 
                    self.select6List(self.refreshSelect(self.fullSetSelect, self.retirementPaymentCurrent, 5));
                });        
            });
            
            //Employee Process 
            self.employeeList = ko.observableArray([
                new EmployeeInfo('99900000-0000-0000-0000-000000000001', "A000000000001",'日通　一郎'),
                new EmployeeInfo('99900000-0000-0000-0000-000000000002', "A000000000002",'日通　二郎'),
                new EmployeeInfo('99900000-0000-0000-0000-000000000003', "A000000000003",'日通　三郎'),
                new EmployeeInfo('99900000-0000-0000-0000-000000000004', "A000000000004",'日通　三郎'),
                new EmployeeInfo('99900000-0000-0000-0000-000000000005', "A000000000005",'日通　三郎'),
                new EmployeeInfo('99900000-0000-0000-0000-000000000006', "A000000000006",'日通　三郎'),
                new EmployeeInfo('99900000-0000-0000-0000-000000000007', "A000000000007",'日通　三郎'),
                new EmployeeInfo('99900000-0000-0000-0000-000000000008', "A000000000008",'日通　三郎'),
                new EmployeeInfo('99900000-0000-0000-0000-000000000009', "A000000000009",'日通　三郎'),
                new EmployeeInfo('99900000-0000-0000-0000-000000000010', "A000000000010",'日通　三郎'),
                new EmployeeInfo('0', '','日通　三郎')
            ]);
            
            self.personCom = ko.observable(new PersonCom('000000000001',"4/25/2000","4/25/2017",0,1,1));
            self.retirementPaymentCurrent().serviceYear(new Date(self.personCom().endDate()).getFullYear() - new Date(self.personCom().startDate()).getFullYear());
            self.date = ko.observable("2016-12-28");
            self.currentEmployeeIndex = ko.observable(0);
            self.currentEmployeeIndexOld = ko.observable(0);
            self.currentEmployeeCode = ko.observable('99900000-0000-0000-0000-000000000001');
            self.currentEmployee = ko.observable(self.employeeList()[self.currentEmployeeIndex()]);
            self.previous = ko.observable(false);
            self.next = ko.observable(true);
            
            self.currentEmployeeIndex.subscribe(function(value){
                self.previous((value === 0)?false:true);
                self.next((value === (self.employeeList().length-1))?false:true);
                self.currentEmployee(self.employeeList()[value]);
                self.currentEmployeeCode(self.currentEmployee().personId());
                /*
                if(self.dirty.isDirty()){
                    nts.uk.ui.dialog.confirm("Do you want to say \"Hello World!\"?").ifYes(function(){
                        self.getRetirementPaymentByPersonId(self.currentEmployeeCode());
                        self.retirementPaymentCurrent().serviceYear(self.personCom().endDate().getFullYear() - self.personCom().startDate().getFullYear());
                    }).ifNo(function(){}); 
                } else {
                    */
                    self.getRetirementPaymentByPersonId(self.currentEmployeeCode());
                    self.retirementPaymentCurrent().serviceYear(new Date(self.personCom().endDate()).getFullYear() - new Date(self.personCom().startDate()).getFullYear());
                //}
            });
        }
        
        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            self.getRetirementPaymentByPersonId(self.currentEmployeeCode()).done(function(){
                $(document).delegate("#combo-box7", "igcomboselectionchanging", function (evt, ui) {
                    /*
                    if(self.dirty.isDirty()){
                    nts.uk.ui.dialog.confirm("Do you want to say \"Hello World!\"?").ifYes(function(){
                        self.date(ui.items[0].data.payDate);
                        self.retirementPaymentCurrent(_.find(self.retirementPaymentList(),function(o){return o.payDate() == self.date();}));
                        self.retirementPaymentCurrent().serviceYear(self.personCom().endDate().getFullYear() - self.personCom().startDate().getFullYear());
                    }).ifNo(function(){
                        self.date(ui.currentItems[0].data.payDate);
                        self.retirementPaymentCurrent(_.find(self.retirementPaymentList(),function(o){return o.payDate() == self.date();}));
                        self.retirementPaymentCurrent().serviceYear(self.personCom().endDate().getFullYear() - self.personCom().startDate().getFullYear());
                    });
                    } else {
                        */
                        self.date(ui.items[0].data.payDate);
                        self.rebind(self.retirementPaymentCurrent(),_.find(self.retirementPaymentList(),function(o){return o.payDate() == self.date();}));
                        self.retirementPaymentCurrent().serviceYear(new Date(self.personCom().endDate()).getFullYear() - new Date(self.personCom().startDate()).getFullYear());
                    //}
                });
                dfd.resolve();    
            }).fail(function(){
                dfd.reject();    
            });
            return dfd.promise(); 
        }
        
        // get list Retirement Payment
        getRetirementPaymentByPersonId(personId): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            qrm001.a.service.getRetirementPaymentList(personId).done(function(data) {
                if(!data.length){ 
                    self.isUpdate(false); 
                    self.rebind(self.retirementPaymentCurrent(), new RetirementPayment('0','0',0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'0','0'));
                }
                else { 
                    self.isUpdate(true);
                    self.retirementPaymentKeyList.removeAll();
                    self.retirementPaymentList.removeAll();
                    data.forEach(function(item) {
                        self.retirementPaymentList.push(new RetirementPayment(
                            item.personId,
                            item.payDate,
                            item.trialPeriodSet,
                            item.exclusionYears,
                            item.additionalBoardYears,
                            item.boardYears,
                            item.totalPaymentMoney,
                            item.deduction1Money,
                            item.deduction2Money,
                            item.deduction3Money,
                            item.retirementPayOption,
                            item.taxCalculationMethod,
                            item.incomeTaxMoney,
                            item.cityTaxMoney,
                            item.prefectureTaxMoney,
                            item.totalDeclarationMoney,
                            item.actualRecieveMoney,
                            item.bankTransferOption1,
                            item.option1Money,
                            item.bankTransferOption2,
                            item.option2Money,
                            item.bankTransferOption3,
                            item.option3Money,
                            item.bankTransferOption4,
                            item.option4Money,
                            item.bankTransferOption5,
                            item.option5Money,
                            item.withholdingMeno,
                            item.statementMemo));
                        self.retirementPaymentKeyList.push(new RetirementPaymentKey(
                            item.personId,
                            item.payDate));
                    });
                    self.rebind(self.retirementPaymentCurrent(),_.first(self.retirementPaymentList()));
                    self.select6List(self.refreshSelect(self.fullSetSelect, self.retirementPaymentCurrent, 5));
                    self.select6Code().forEach(function(item,index){
                        self.select6Code()[index](self.retirementPaymentCurrent()["bankTransferOption"+(index+1)]());        
                    });
                    self.date(_.first(self.retirementPaymentList()).payDate);
                }
                dfd.resolve();
            }).fail(function(res) {
                dfd.reject(res);
            });
            return dfd.promise();        
        }
        
        // save submit Data ( register or update )
        saveData(isUpdate): void{
            var self = this;
            self.retirementPaymentCurrent().payDate(self.date());
            let command = ko.mapping.toJS(self.retirementPaymentCurrent());
            if(isUpdate) {
                    qrm001.a.service.updateRetirementPaymentInfo(command);
            } else {
                    qrm001.a.service.registerRetirementPaymentInfo(command);
            }
        }
        
        // delete submit Data
        deleteData(): void{
            var self = this;
            self.retirementPaymentCurrent().payDate(self.date());
            let command = ko.mapping.toJS(self.retirementPaymentCurrent());
            /*
            if(self.dirty.isDirty()){
                nts.uk.ui.dialog.confirm("Do you want to Remove ?").ifYes(function(){
                    qrm001.a.service.removeRetirementPaymentInfo(command);
                }).ifNo(function(){});   
            } else {
                */
                qrm001.a.service.removeRetirementPaymentInfo(command);
            //}     
        }
        
        // open Screen B dialog
        openDialog(): void{
            var self = this;
            nts.uk.ui.windows.sub.modal('/view/qrm/001/b/index.xhtml', {title: '入力欄の背景色について', dialogClass: "no-close"});
        }
        
        
        // Process Calculator Money: auto or manual, change color each calculation
        static autoCalculate(retirementPayment: RetirementPayment, serviceYear: number, age: number ): void{
            var self = retirementPayment;
            let year = serviceYear;
            let reduction = 0;
            if(year > 20 ) {
                reduction = 8000000 + 700000*(year - 20);
                if(reduction < 800000) reduction = 800000;
            } else reduction = 400000 * year;
            let totalPaymentMoney = this.toNumber(self.totalPaymentMoney());
            let tax = (totalPaymentMoney-reduction)/2;
            if(tax < 0) tax = 0;
            let payOption = self.retirementPayOption();
            let deduction1 = this.toNumber(self.deduction1Money());
            let deduction2 = this.toNumber(self.deduction2Money());
            let deduction3 = this.toNumber(self.deduction3Money());
            let incomeTax = 0;
            if(payOption==2){
                incomeTax = totalPaymentMoney*20.42/100;
            } else { 
                switch(true) {
                    case (1000 <= totalPaymentMoney)&&(totalPaymentMoney < 1950000):
                        incomeTax =  totalPaymentMoney*5/100-0;
                        break;
                    case (1950000 <= totalPaymentMoney)&&(totalPaymentMoney < 3300000):
                        incomeTax =  totalPaymentMoney*10/100-97500;
                        break;
                    case (3300000 <= totalPaymentMoney)&&(totalPaymentMoney < 6950000):
                        incomeTax =  totalPaymentMoney*20/100-427500;
                        break;
                    case (6950000 <= totalPaymentMoney)&&(totalPaymentMoney < 9000000):
                        incomeTax =  totalPaymentMoney*23/100-636000;
                        break;
                    case (9000000 <= totalPaymentMoney)&&(totalPaymentMoney < 18000000):
                        incomeTax =  totalPaymentMoney*33/100-1536000;
                        break;
                    case (18000000 <= totalPaymentMoney)&&(totalPaymentMoney < 40000000):
                        incomeTax =  totalPaymentMoney*40/100-2796000;
                        break;
                    case (40000000 <= totalPaymentMoney):
                        incomeTax =  totalPaymentMoney*45/100-4796000;
                        break;
                    default:
                        incomeTax =  totalPaymentMoney*0-0;
                        break;
                }
                if((25 <= age) && (age <= 49)) incomeTax *= 102.1/100;
            }
            let cityTax = (tax*6/100)*90/100;
            let prefectureTax = (tax*4/100)*90/100;
            let totalDeclarationMoney = 
                (deduction1?deduction1:0)+
                (deduction2?deduction2:0)+
                (deduction3?deduction3:0)+
                this.toNumber(incomeTax?incomeTax:0)+
                this.toNumber(cityTax?cityTax:0)+
                this.toNumber(prefectureTax?prefectureTax:0);
            self.incomeTaxMoney(incomeTax);
            self.cityTaxMoney(cityTax);
            self.prefectureTaxMoney(prefectureTax);
            self.totalDeclarationMoney(totalDeclarationMoney);
            self.actualRecieveMoney(totalPaymentMoney - totalDeclarationMoney);     
        }
        static manualCalculate(retirementPayment: RetirementPayment): void{
            var self = retirementPayment;
            let totalPaymentMoney = this.toNumber(self.totalPaymentMoney());
            let deduction1 = this.toNumber(self.deduction1Money());
            let deduction2 = this.toNumber(self.deduction2Money());
            let deduction3 = this.toNumber(self.deduction3Money());
            let incomeTax = this.toNumber(self.incomeTaxMoney());
            let cityTax = this.toNumber(self.cityTaxMoney());
            let prefectureTax = this.toNumber(self.prefectureTaxMoney());
            let totalDeclarationMoney = 
                (deduction1?deduction1:0)+
                (deduction2?deduction2:0)+
                (deduction3?deduction3:0)+
                (incomeTax?incomeTax:0)+
                (cityTax?cityTax:0)+
                (prefectureTax?prefectureTax:0);
            self.totalDeclarationMoney(totalDeclarationMoney);
            self.actualRecieveMoney(totalPaymentMoney - totalDeclarationMoney);         
        }
        static changeColor(value: any, retirementPayment: RetirementPayment, serviceYear: number, age: number): void{
            var self = this;
            if(!value){
                self.autoCalculate(retirementPayment, serviceYear, age);
                $(".caculator").css('background-color', '#ffc000');
            } else $(".caculator").css('background-color', '#cee6ff');         
        }
        
        
        // Process Employee List: select next and previous Employee
        next_emp(): void {
            var self = this;
            self.currentEmployeeIndex(self.currentEmployeeIndex()+1);
        }
        previous_emp(): void {
            var self = this;
            self.currentEmployeeIndex(self.currentEmployeeIndex()-1);
        }
        
        
        // Process Bank Account Select: refresh Bank Account list each selected event
        filter(dataset: KnockoutObservableArray<any>,currentItem: KnockoutObservable<RetirementPayment>,index: number): Array<any>{
            var self = this;
            return _.filter(dataset(), function(o) { 
                    return  o.code == 0 ||
                            o.code == currentItem()["bankTransferOption"+(index+1)]() ||
                            o.code !== currentItem().bankTransferOption1() && 
                            o.code !== currentItem().bankTransferOption2() &&
                            o.code !== currentItem().bankTransferOption3() &&  
                            o.code !== currentItem().bankTransferOption4() && 
                            o.code !== currentItem().bankTransferOption5();
            });
        }
        refreshSelect(source1: KnockoutObservableArray<any>, source2: KnockoutObservable<RetirementPayment>, length: number): Array<any>{
            var self = this;
            let array = [];
            for(let i=0;i<length;i++){
                array.push(self.filter(source1, source2, i));    
            }
            return array;        
        }
        
        
        // Process Value 
        // format object value to number
        static toNumber(input: any): number {
            return parseInt(input.toString());
        }
        // Set new value for Knockout Object
        rebind(oldItem: RetirementPayment, newItem: RetirementPayment){
            oldItem.personId(newItem.personId());   
            oldItem.payDate(newItem.payDate());
            oldItem.trialPeriodSet(newItem.trialPeriodSet());
            oldItem.exclusionYears(newItem.exclusionYears());
            oldItem.additionalBoardYears(newItem.additionalBoardYears());
            oldItem.boardYears(newItem.boardYears());
            oldItem.totalPaymentMoney(newItem.totalPaymentMoney());
            oldItem.deduction1Money(newItem.deduction1Money());
            oldItem.deduction2Money(newItem.deduction2Money());
            oldItem.deduction3Money(newItem.deduction3Money());
            oldItem.retirementPayOption(newItem.retirementPayOption());
            oldItem.taxCalculationMethod(newItem.taxCalculationMethod());
            oldItem.incomeTaxMoney(newItem.incomeTaxMoney());
            oldItem.cityTaxMoney(newItem.cityTaxMoney());
            oldItem.prefectureTaxMoney(newItem.prefectureTaxMoney());
            oldItem.totalDeclarationMoney(newItem.totalDeclarationMoney());
            oldItem.actualRecieveMoney(newItem.actualRecieveMoney());
            oldItem.bankTransferOption1(newItem.bankTransferOption1());
            oldItem.option1Money(newItem.option1Money());
            oldItem.bankTransferOption1(newItem.bankTransferOption2());
            oldItem.option1Money(newItem.option2Money());
            oldItem.bankTransferOption1(newItem.bankTransferOption3());
            oldItem.option1Money(newItem.option3Money());
            oldItem.bankTransferOption1(newItem.bankTransferOption4());
            oldItem.option1Money(newItem.option4Money());
            oldItem.bankTransferOption1(newItem.bankTransferOption5());
            oldItem.option1Money(newItem.option5Money());
            oldItem.withholdingMeno(newItem.withholdingMeno());
            oldItem.statementMemo(newItem.statementMemo());
        }
        
        // calculate UI event
        manualCalculateEvent(retirementPayment: RetirementPayment): void {
            qrm001.a.viewmodel.ScreenModel.manualCalculate(retirementPayment);    
        }
    }
    
    // Person Base Object
    class EmployeeInfo {
        personId: KnockoutObservable<string>;
        employeeCode: KnockoutObservable<string>;
        name: KnockoutObservable<string>;
        constructor(personId: string, employeeCode: string, name: string){
            this.personId = ko.observable(personId);
            this.employeeCode = ko.observable(employeeCode);
            this.name = ko.observable(name);    
        }   
    }
    
    // Person Com Object
    class PersonCom {
        scd: KnockoutObservable<string>; 
        startDate: KnockoutObservable<string>;
        endDate: KnockoutObservable<string>;
        adoptType: KnockoutObservable<number>;
        quitFiringAtr: KnockoutObservable<number>;
        quitFiringReason_atr: KnockoutObservable<number>;
        constructor(scd: string, startDate: string, endDate: string, adoptType: number, quitFiringAtr: number, quitFiringReason_atr: number){
            this.scd = ko.observable(scd);
            this.startDate = ko.observable(startDate);
            this.endDate = ko.observable(endDate);  
            this.adoptType = ko.observable(AdoptType["TYPE"+adoptType]);
            this.quitFiringAtr = ko.observable(QuitFiringAtr["QUITFIRING"+quitFiringAtr]);
            this.quitFiringReason_atr = ko.observable(QuitFiringReasonAtr["QUITFIRINGREASON"+quitFiringReason_atr]);
        }   
    }
    
    // Bank Account Object
    class PersonBankAccount {
        bankName: KnockoutObservable<string>;
        branchName: KnockoutObservable<string>;
        accountNumber: KnockoutObservable<string>;
        constructor(bankName: string, branchName: string, accountNumber: string){
            this.bankName = ko.observable(bankName);
            this.branchName = ko.observable(branchName);    
            this.accountNumber = ko.observable(accountNumber);
        }   
    }
    
    // Retirement Payment Key Object
    class RetirementPaymentKey {
        personId: KnockoutObservable<string>;
        payDate: string;
        constructor(personId: string, payDate: string){
            this.personId = ko.observable(personId);
            this.payDate = payDate;  
        }   
    }
    
    // Retirement Payment Object
    class RetirementPayment {
        personId: KnockoutObservable<string>;
        payDate: KnockoutObservable<string>;
        trialPeriodSet: KnockoutObservable<number>;
        age: KnockoutObservable<number>;
        serviceYear: KnockoutObservable<number>;
        exclusionYears: KnockoutObservable<number>;
        additionalBoardYears: KnockoutObservable<number>;
        boardYears: KnockoutObservable<number>;
        totalPaymentMoney: KnockoutObservable<number>;
        deduction1Money: KnockoutObservable<number>;
        deduction2Money: KnockoutObservable<number>;
        deduction3Money: KnockoutObservable<number>;
        retirementPayOption: KnockoutObservable<number>;
        taxCalculationMethod: KnockoutObservable<number>;
        incomeTaxMoney: KnockoutObservable<number>;
        cityTaxMoney: KnockoutObservable<number>;
        prefectureTaxMoney: KnockoutObservable<number>;
        totalDeclarationMoney: KnockoutObservable<number>;
        actualRecieveMoney: KnockoutObservable<number>;
        bankTransferOption1: KnockoutObservable<number>;
        option1Money: KnockoutObservable<number>;
        bankTransferOption2: KnockoutObservable<number>;
        option2Money: KnockoutObservable<number>;
        bankTransferOption3: KnockoutObservable<number>;
        option3Money: KnockoutObservable<number>;
        bankTransferOption4: KnockoutObservable<number>;
        option4Money: KnockoutObservable<number>;
        bankTransferOption5: KnockoutObservable<number>;
        option5Money: KnockoutObservable<number>;
        withholdingMeno: KnockoutObservable<string>;
        statementMemo: KnockoutObservable<string>;
        constructor(
            personId: string,
            payDate: string,
            trialPeriodSet: number,
            exclusionYears: number,
            additionalBoardYears: number,
            boardYears: number,
            totalPaymentMoney: number,
            deduction1Money: number,
            deduction2Money: number,
            deduction3Money: number,
            retirementPayOption: number,
            taxCalculationMethod: number,
            incomeTaxMoney: number,
            cityTaxMoney: number,
            prefectureTaxMoney: number,
            totalDeclarationMoney: number,
            actualRecieveMoney: number,
            bankTransferOption1: number,
            option1Money: number,
            bankTransferOption2: number,
            option2Money: number,
            bankTransferOption3: number,
            option3Money: number,
            bankTransferOption4: number,
            option4Money: number,
            bankTransferOption5: number,
            option5Money: number,
            withholdingMeno: string,
            statementMemo: string ){
                var self = this;
                self.personId = ko.observable(personId);
                self.payDate = ko.observable(payDate);
                self.trialPeriodSet = ko.observable(trialPeriodSet);
                self.exclusionYears = ko.observable(exclusionYears);
                self.additionalBoardYears = ko.observable(additionalBoardYears);
                self.boardYears = ko.observable(boardYears);
                self.totalPaymentMoney = ko.observable(totalPaymentMoney);
                self.deduction1Money = ko.observable(deduction1Money);
                self.deduction2Money = ko.observable(deduction2Money);
                self.deduction3Money = ko.observable(deduction3Money);
                self.retirementPayOption = ko.observable(retirementPayOption);
                self.taxCalculationMethod = ko.observable(taxCalculationMethod);
                self.incomeTaxMoney = ko.observable(incomeTaxMoney);
                self.cityTaxMoney = ko.observable(cityTaxMoney);
                self.prefectureTaxMoney = ko.observable(prefectureTaxMoney);     
                self.totalDeclarationMoney = ko.observable(totalDeclarationMoney);
                self.actualRecieveMoney = ko.observable(actualRecieveMoney);
                self.bankTransferOption1 = ko.observable(bankTransferOption1);
                self.option1Money = ko.observable(self.bankTransferOption1()?option1Money:null);
                self.bankTransferOption2 = ko.observable(bankTransferOption2);
                self.option2Money = ko.observable(self.bankTransferOption2()?option2Money:null);
                self.bankTransferOption3 = ko.observable(bankTransferOption3);
                self.option3Money = ko.observable(self.bankTransferOption3()?option3Money:null);
                self.bankTransferOption4 = ko.observable(bankTransferOption4);
                self.option4Money = ko.observable(self.bankTransferOption4()?option4Money:null);
                self.bankTransferOption5 = ko.observable(bankTransferOption5);
                self.option5Money = ko.observable(self.bankTransferOption5()?option5Money:null);
                self.withholdingMeno = ko.observable(withholdingMeno);
                self.statementMemo = ko.observable(statementMemo);
                self.serviceYear = ko.observable(20);
                self.serviceYear.subscribe(function(value){qrm001.a.viewmodel.ScreenModel.autoCalculate(self, value, self.age());});     
                self.age = ko.observable(30);
                self.age.subscribe(function(value){qrm001.a.viewmodel.ScreenModel.autoCalculate(self, self.serviceYear(), value);});      
                qrm001.a.viewmodel.ScreenModel.changeColor(self.taxCalculationMethod(), self, self.serviceYear(), self.age());
                self.taxCalculationMethod.subscribe(function(value){qrm001.a.viewmodel.ScreenModel.changeColor(value, self, self.serviceYear(), self.age());});
                self.totalPaymentMoney.subscribe(function(valueinp5){if(!self.taxCalculationMethod()){qrm001.a.viewmodel.ScreenModel.autoCalculate(self, self.serviceYear(), self.age());}});
                self.deduction1Money.subscribe(function(valueinp6){if(!self.taxCalculationMethod()){qrm001.a.viewmodel.ScreenModel.autoCalculate(self, self.serviceYear(), self.age());}});
                self.deduction2Money.subscribe(function(valueinp7){if(!self.taxCalculationMethod()){qrm001.a.viewmodel.ScreenModel.autoCalculate(self, self.serviceYear(), self.age());}});
                self.deduction3Money.subscribe(function(valueinp8){if(!self.taxCalculationMethod()){qrm001.a.viewmodel.ScreenModel.autoCalculate(self, self.serviceYear(), self.age());}});
                self.retirementPayOption.subscribe(function(valuesel4){if(!self.taxCalculationMethod()){qrm001.a.viewmodel.ScreenModel.autoCalculate(self, self.serviceYear(), self.age());}}); 
        }
    }
    
    enum AdoptType {
        TYPE0 = <any>'なし',
        TYPE1 = <any>'定期採用',
        TYPE2 = <any>'中途採用',
        TYPE3 = <any>'臨時採用',
        TYPE4 = <any>'転籍受入'    
    }

    export enum QuitFiringAtr {
        QUITFIRING1 = <any>'退職',
        QUITFIRING2 = <any>'転籍',
        QUITFIRING3 = <any>'解雇',
        QUITFIRING4 = <any>'定年'
    }
    
    export enum QuitFiringReasonAtr {
        QUITFIRINGREASON1 = <any>'自己都合による退職',
        QUITFIRINGREASON2 = <any>'会社の勧奨による退職',
        QUITFIRINGREASON3 = <any>'定年による退職',
        QUITFIRINGREASON4 = <any>'契約期間の満了による退職',
        QUITFIRINGREASON5 = <any>'移籍出向による退職',
        QUITFIRINGREASON6 = <any>'その他理由による退職',
        QUITFIRINGREASON7 = <any>'天災その他やむを得ない理由による解雇',
        QUITFIRINGREASON8 = <any>'会社都合による解雇',
        QUITFIRINGREASON9 = <any>'職務命令に対する重大違反行為による解雇',
        QUITFIRINGREASON10 = <any>'業務について不正な行為による解雇',
        QUITFIRINGREASON11 = <any>'勤務不良であることによる解雇',
        QUITFIRINGREASON12 = <any>'その他理由による解雇'
    }

    export enum Error {
        ER001 = <any>"が入力されていません。",
        ER007 = <any>"が選択されていません。",
        ER010 = <any>"対象データがありません。",
    }
}
