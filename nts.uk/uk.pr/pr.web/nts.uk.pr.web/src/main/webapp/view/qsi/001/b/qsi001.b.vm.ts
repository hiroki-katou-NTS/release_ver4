module nts.uk.pr.view.qsi001.b.viewmodel {

    import block = nts.uk.ui.block;
    import getShared = nts.uk.ui.windows.getShared;

    export class ScreenModel {
        employeeInputList: KnockoutObservableArray<EmployeeModel>;
        //

        depNotiAttach: KnockoutObservableArray<any>;
        selectedDepNotoAttach: any;
        option:any;

        basicPension: KnockoutObservable<string>;
        salaryMonthly: KnockoutObservable<number>;
        salaryMonthlyActual: KnockoutObservable<number>;
        totalCompensation: KnockoutObservable<number>;
        depNotiAttach: KnockoutObservableArray<any>;
        selectedDepNotiAttach: any;

        applyToEmployeeOver70: KnockoutObservable<boolean>;
        twoOrMoreEmployee: KnockoutObservable<boolean>;
        shortWorkHours: KnockoutObservable<boolean>;
        continuousEmpAfterRetire: KnockoutObservable<boolean>;
        otherNotes: KnockoutObservable<boolean>;
        textOtherNotes: KnockoutObservable<string>;
        shortTermResidence: KnockoutObservable<boolean>;
        otherNotes1: KnockoutObservable<boolean>;
        textOtherNotes1: KnockoutObservable<string>;

        livingAbroad: KnockoutObservable<boolean>;
        //kcp009
        systemReference: KnockoutObservable<number>;
        isDisplayOrganizationName: KnockoutObservable<boolean>;
        targetBtnText: string;
        baseDate: KnockoutObservable<Date>;
        listComponentOption: ComponentOption;
        selectedItem: KnockoutObservable<string>;
        tabindex: number;
        //

        personalNumber: KnockoutObservableArray<ItemModel>;
        selectedCode: KnockoutObservable<string>;
        isEnable: KnockoutObservable<boolean>;
        isEditable: KnockoutObservable<boolean>;


        constructor() {
            block.invisible();
            let self = this;
            let periodCommand: any;
            let params = getShared('QSI001_PARAMS_TO_SCREEN_B');

            if (params && params.listEmpId &&  params.listEmpId.length > 0) {


                periodCommand = {
                    empId: params.listEmpId[0].employeeId,
                    startDate: params.startDate,
                    endDate: params.endDate
                };

                self.loadKCP009(self.createEmployeeModel(params.listEmpId));

                //起動する
                //load page
                service.getSocialInsurAcquisiInforById(params.listEmpId[0].employeeId).done(e => {
                    if (e) {
                        self.otherNotes(e.remarksOther == 1 ? true : false);
                        self.textOtherNotes(e.remarksAndOtherContents);
                        self.salaryMonthlyActual(e.remunMonthlyAmountKind);
                        self.salaryMonthly(e.remunMonthlyAmount);
                        self.totalCompensation(e.totalMonthlyRemun);
                        self.livingAbroad(e.livingAbroad == 1 ? true : false);
                        self.otherNotes1(e.reasonOther == 1 ? true : false);
                        self.textOtherNotes1(e.reasonAndOtherContents);
                        self.shortTermResidence(e.shortStay == 1 ? true : false);
                        self.selectedDepNotiAttach(e.depenAppoint);
                        self.shortWorkHours(e.shortTimeWorkers == 1 ? true : false);
                        self.continuousEmpAfterRetire(e.continReemAfterRetirement == 1 ? true : false);
                    } else {
                        self.otherNotes(false);
                        self.textOtherNotes(null);
                        self.salaryMonthlyActual(null);
                        self.salaryMonthly(null);
                        self.totalCompensation(null);
                        self.livingAbroad(false);
                        self.otherNotes1(false);
                        self.textOtherNotes1(null);
                        self.shortTermResidence(false);
                        self.selectedDepNotiAttach(0);
                        self.shortWorkHours(false);
                        self.continuousEmpAfterRetire(false);
                    }
                }).fail(e => {

                });

                service.getPersonInfo(params.listEmpId[0].employeeId).done(r => {
                    if (self.getAge(r, params.date) >= 70) {
                        self.applyToEmployeeOver70(true);
                        self.otherNotes(true);
                    }
                }).fail(f => {
                    console.dir(f);
                });

                service.getMultiEmpWorkInfoById(params.listEmpId[0].employeeId).done(e =>{
                    if(e){
                        self.twoOrMoreEmployee(true);
                    }else{
                        self.twoOrMoreEmployee(false);
                    }
                }).fail(e =>{

                });

                service.getEmpBasicPenNumInforById(params.listEmpId[0].employeeId).done(e =>{
                    if(e){
                        self.basicPension(e.basicPenNumber);
                    }else{
                        self.basicPension(null);
                    }
                }).fail(e =>{

                });




                service.getCorWorkFormInfo(periodCommand).done(e =>{
                    if(e){
                        if(e.insPerCls == InsPerCls.SHORT_TIME_WORKERS){
                            self.shortWorkHours(true);
                        }else{
                            self.shortWorkHours(false);
                        }
                    }

                }).fail(e =>{

                });

                //社員を切り替える
                //select employee
                self.selectedItem.subscribe(e => {
                    periodCommand = {

                    };
                    service.getSocialInsurAcquisiInforById(self.selectedItem()).done(e => {
                        if (e) {
                            self.otherNotes(e.remarksOther == 1 ? true : false);
                            self.textOtherNotes(e.remarksAndOtherContents);
                            self.salaryMonthlyActual(e.remunMonthlyAmountKind);
                            self.salaryMonthly(e.remunMonthlyAmount);
                            self.totalCompensation(e.totalMonthlyRemun);
                            self.livingAbroad(e.livingAbroad == 1 ? true : false);
                            self.otherNotes1(e.reasonOther == 1 ? true : false);
                            self.textOtherNotes1(e.reasonAndOtherContents);
                            self.shortTermResidence(e.shortStay == 1 ? true : false);
                            self.selectedDepNotiAttach(e.depenAppoint);
                            self.shortWorkHours(e.shortTimeWorkers == 1 ? true : false);
                            self.continuousEmpAfterRetire(e.continReemAfterRetirement == 1 ? true : false);
                        } else {
                            self.otherNotes(false);
                            self.textOtherNotes(null);
                            self.salaryMonthlyActual(null);
                            self.salaryMonthly(null);
                            self.totalCompensation(null);
                            self.livingAbroad(false);
                            self.otherNotes1(false);
                            self.textOtherNotes1(null);
                            self.shortTermResidence(false);
                            self.selectedDepNotiAttach(0);
                            self.shortWorkHours(false);
                            self.continuousEmpAfterRetire(false);
                        }

                    }).fail(e => {

                    });

                    service.getPersonInfo(self.selectedItem()).done(r => {
                        if (self.getAge(r, params.date) >= 70) {
                            self.applyToEmployeeOver70(true);
                            self.otherNotes(true);
                        }
                    }).fail(f => {
                        console.dir(f);
                    });

                    service.getMultiEmpWorkInfoById(self.selectedItem()).done(e =>{
                        if(e){
                            self.twoOrMoreEmployee(true);
                        }else{
                            self.twoOrMoreEmployee(false);
                        }
                    }).fail(e =>{

                    });

                    service.getEmpBasicPenNumInforById(self.selectedItem()).done(e =>{
                        if(e){
                            self.basicPension(e.basicPenNumber);
                        }else{
                            self.basicPension(null);
                        }
                    }).fail(e =>{

                    });
                });


            }


            //init
            self.option = new nts.uk.ui.option.CurrencyEditorOption({
                grouplength: 3,
                decimallength: 0,
                currencyformat: "JPY"
            });
            self.depNotiAttach = ko.observableArray([]);
            self.selectedDepNotoAttach = ko.observable({});

            self.basicPension = ko.observable(null);
            self.salaryMonthly = ko.observable(null);
            self.salaryMonthlyActual = ko.observable(null);
            self.totalCompensation = ko.observable(null);
            self.depNotiAttach = ko.observableArray([
                {code: '0', name: nts.uk.resource.getText('QSI001_B222_13')},
                {code: '1', name: nts.uk.resource.getText('QSI001_B222_14')}
            ]);
            self.selectedDepNotiAttach = ko.observable(0);

            self.applyToEmployeeOver70 = ko.observable(false);
            self.twoOrMoreEmployee = ko.observable(false);
            self.shortWorkHours = ko.observable(false);
            self.continuousEmpAfterRetire = ko.observable(false);
            self.otherNotes = ko.observable(false);
            self.textOtherNotes = ko.observable(null);

            self.livingAbroad = ko.observable(false);
            self.shortTermResidence = ko.observable(false);
            self.otherNotes1 = ko.observable(false);
            self.textOtherNotes1 = ko.observable(null);

            self.personalNumber = ko.observableArray([
                new ItemModel('0', nts.uk.resource.getText('QSI001_B222_24_0')),
                new ItemModel('1', nts.uk.resource.getText('QSI001_B222_24_1')),
                new ItemModel('2', nts.uk.resource.getText('QSI001_B222_24_2')),
                new ItemModel('3', nts.uk.resource.getText('QSI001_B222_24_3'))
            ]);

            self.selectedCode = ko.observable('1');
            self.isEnable = ko.observable(true);
            self.isEditable = ko.observable(true);


            block.clear();
        }

        getAge(DOB, date) {
            var today = new Date(date);
            var birthDate = new Date(DOB);
            var age = today.getFullYear() - birthDate.getFullYear();
            var m = today.getMonth() - birthDate.getMonth();
            if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
                age = age - 1;
            }

            return age;
        }

        cancel() {
            nts.uk.ui.windows.close();
        }

        add() {
            let self = this;
            nts.uk.ui.errors.clearAll();
            $("input").trigger("validate");
            if (nts.uk.ui.errors.hasError()) {
                return;
            }
            block.invisible();
            let data = {

                socialInsurAcquisiInforCommand : new SocialInsurAcquisiInforDTO(self.selectedItem(),
                    self.applyToEmployeeOver70() == true ? 1 : 0,
                    self.otherNotes() == true ? 1 : 0,
                    self.textOtherNotes(),
                    self.salaryMonthlyActual(),
                    self.salaryMonthly(),
                    self.totalCompensation(),
                    self.selectedDepNotiAttach(),
                    0,
                    self.shortWorkHours() == true ? 1 : 0,
                    self.continuousEmpAfterRetire() == true ? 1 : 0,
                    Number(self.selectedCode()),
                    self.textOtherNotes1()

                    ),
                empBasicPenNumInforCommand: {
                    employeeId: self.selectedItem(),
                    basicPenNumber: self.basicPension()

                },
                multiEmpWorkInfoCommand: {
                    employeeId: self.selectedItem(),
                    isMoreEmp: self.twoOrMoreEmployee() == true ? 1 : 0,

                }
            }


            service.add(data).done(e => {
                nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(e=>{
                    block.clear();
                })
            }).fail(e => {
                block.clear();
            });
        }

        createEmployeeModel(data) {
            let listEmployee = [];
            _.each(data, data => {
                listEmployee.push({
                    id: data.employeeId,
                    code: data.employeeCode,
                    businessName: data.employeeName,
                    workplaceName: data.workplaceName
                });
            });

            return listEmployee;
        }

        loadKCP009(data) {
            let self = this;
            self.employeeInputList = ko.observableArray(data);
            self.systemReference = ko.observable(SystemType.EMPLOYMENT);
            self.isDisplayOrganizationName = ko.observable(true);
            self.targetBtnText = nts.uk.resource.getText("KCP009_3");
            self.selectedItem = ko.observable(null);
            self.tabindex = 3;
            // Initial listComponentOption
            self.listComponentOption = {
                systemReference: self.systemReference(),
                isDisplayOrganizationName: self.isDisplayOrganizationName(),
                employeeInputList: self.employeeInputList,
                targetBtnText: self.targetBtnText,
                selectedItem: self.selectedItem,
                tabIndex: self.tabindex
            };
            $('#emp-component').ntsLoadListComponent(self.listComponentOption);
        }


    }


    export interface ComponentOption {
        systemReference: SystemType;
        isDisplayOrganizationName: boolean;
        employeeInputList: KnockoutObservableArray<EmployeeModel>;
        targetBtnText: string;
        selectedItem: KnockoutObservable<string>;
        tabIndex: number;
        baseDate?: KnockoutObservable<Date>;
    }

    export interface EmployeeModel {
        id: string;
        code: string;
        businessName: string;
        depName?: string;
        workplaceName?: string;
    }

    export class SystemType {
        static EMPLOYMENT = 1;
        static SALARY = 2;
        static PERSONNEL = 3;
        static ACCOUNTING = 4;
        static OH = 6;
    }

    export class InsPerCls{
        //一般被保険者
        static GEN_INS_PER = 0;
        //パート扱い
        static PART_HANDLING = 1;
        //短時間労働者
        static SHORT_TIME_WORKERS = 2;
    }

    export class ItemModel {
        code: string;
        name: string;

        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }
    }

    export class PersonalNumber {
        static Not_Applicable = 0;
        static Living_Abroad = 1;
        static Short_Stay = 2;
        static Other = 3;
    }

    export  class SocialInsurAcquisiInforDTO{

        employeeId: string;
        //70歳以上被用者
        percentOrMore: number;
        //備考その他
        remarksOther: number;
        //備考その他内容
        remarksAndOtherContents: string;
        //報酬月額（現物）
        remunMonthlyAmountKind: number;
        //報酬月額（金額）
        remunMonthlyAmount: number;
        //報酬月額合計
        totalMonthlyRemun: number;

        //被扶養者届出区分
        depenAppoint: number;

        qualifiDistin: number;
        //短時間労働者
        shortTimeWorkers: number;
        //退職後の継続再雇用者
        continReemAfterRetirement: number;


        //短期在留
        shortStay: number;
        //海外在住
        livingAbroad: number;
        //理由その他
        reasonOther: number;
        //理由その他内容
        reasonAndOtherContents: string;

        constructor(employeeId:string,
                    percentOrMore:number,
                    remarksOther:number,
                    remarksAndOtherContents: string,
                    remunMonthlyAmountKind: number,
                    remunMonthlyAmount:number,
                    totalMonthlyRemun: number,
                    depenAppoint: number,
                    qualifiDistin:number,
                    shortTimeWorkers: number,
                    continReemAfterRetirement:number,
                    personalNumber: number,
                    reasonAndOtherContents: string
                    ){

            if(personalNumber == PersonalNumber.Living_Abroad){
                this.livingAbroad = 1;
                this.shortStay = null;
                this.reasonOther = null;
                this.reasonAndOtherContents = null;
            }else if(personalNumber == PersonalNumber.Short_Stay){
                this.livingAbroad = null;
                this.shortStay = 1;
                this.reasonOther = null;
                this.reasonAndOtherContents = null;
            }else if(personalNumber == PersonalNumber.Other){
                this.livingAbroad = null;
                this.shortStay = null;
                this.reasonOther = 1;
                this.reasonAndOtherContents = reasonAndOtherContents;
            }

            this.employeeId = employeeId;
            this.percentOrMore = percentOrMore;
            this.remarksOther = remarksOther;
            this.remarksAndOtherContents = remarksAndOtherContents;
            this.remunMonthlyAmountKind = remunMonthlyAmountKind;
            this.remunMonthlyAmount = remunMonthlyAmount;
            this.totalMonthlyRemun = totalMonthlyRemun;
            this.depenAppoint = depenAppoint;
            this.qualifiDistin = qualifiDistin;
            this.shortTimeWorkers = shortTimeWorkers;
            this.continReemAfterRetirement = continReemAfterRetirement;
        }
    }
}