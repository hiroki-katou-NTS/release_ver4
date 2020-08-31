module nts.uk.at.view.kaf002_ref.b.viewmodel {
    import Application = nts.uk.at.view.kaf000_ref.shr.viewmodel.Application;
    import AppType = nts.uk.at.view.kaf000_ref.shr.viewmodel.model.AppType;
    import Kaf000AViewModel = nts.uk.at.view.kaf000_ref.a.viewmodel.Kaf000AViewModel;
    @bean()
    class Kaf002BViewModel extends Kaf000AViewModel {
		appType: KnockoutObservable<number> = ko.observable(AppType.STAMP_APPLICATION);
        dataSource: KnockoutObservableArray<ItemModel>;
        dataSourceReason: KnockoutObservableArray<ItemModel>;
        selectedCode: KnockoutObservable<string>;
        selectedCodeReason: KnockoutObservable<string>;
        time: KnockoutObservable<number>;
        application: KnockoutObservable<Application>;
        
        created() {
            
            const self = this;
            
            let itemModelList = [];
            let itemModelReasonList = [];
            _.forEach(new EngraveAtrObject(), prop => {
                itemModelList.push(new ItemModel(String(prop.value), prop.name))
            });
            _.forEach(new GoOutReasonAtrObject(), prop => {
                itemModelReasonList.push(new ItemModel(String(prop.value), prop.name))
            });
            self.dataSource = ko.observableArray(itemModelList);
            self.dataSourceReason = ko.observableArray(itemModelReasonList);
            
            self.selectedCode = ko.observable('1');
            self.selectedCodeReason = ko.observable('0');
            
            // initial time 
            self.time = ko.observable(null);
            
            self.application = ko.observable(new Application(self.appType()));

            self.loadData([], [], self.appType())
            .then((loadDataFlag: any) => {
                if(loadDataFlag) {
                    let ApplicantEmployeeID: null,
                        ApplicantList: null,
                        appDispInfoStartupOutput = ko.toJS(self.appDispInfoStartupOutput),
                        command = { ApplicantEmployeeID, ApplicantList, appDispInfoStartupOutput };
                }
            })
            
        }
        
        mounted() {
            
        }
        
        
        
        
        
    }
    class ItemModel {
        code: string;
        name: string;

        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }
    }
    enum GoOutReasonAtr {
        /**
         * 私用
         */
        PRIVATE,
        
        /**
         * 公用
         */
        PUBLIC,
        
        /**
         * 有償
         */
        COMPENSATION,
        
        /**
         * 組合
         */
        UNION
    }
    enum EngraveAtr {
        /**
         * 出勤
         */
        ATTENDANCE,
        
        /**
         * 退勤
         */
        OFFICE_WORK,
        
        /**
         * 退勤（残業）
         */
        OVERTIME,
        
        /**
         * 外出
         */
        GO_OUT,
        
        /**
         * 戻り
         */
        RETURN,
        
        /**
         * 早出
         */
        EARLY,
        
        /**
         * 休出
         */
        HOLIDAY
    }
    
    class EngraveAtrObject {
        
        ATTENDANCE = {value : EngraveAtr.ATTENDANCE, name : '出勤'};
        OFFICE_WORK = {value : EngraveAtr.OFFICE_WORK, name : '退勤'};
        OVERTIME = {value : EngraveAtr.OVERTIME, name : '退勤（残業）'};
        GO_OUT = {value : EngraveAtr.GO_OUT, name : '外出'};
        RETURN = {value : EngraveAtr.RETURN, name : '戻り'};
        EARLY = {value : EngraveAtr.EARLY, name : '早出'};
        HOLIDAY = {value : EngraveAtr.HOLIDAY, name : '休出'};
        
    }
    class GoOutReasonAtrObject {
        
        PRIVATE = {value: GoOutReasonAtr.PRIVATE, name: '私用'};
        PUBLIC = {value: GoOutReasonAtr.PUBLIC, name: '公用'};
        COMPENSATION = {value: GoOutReasonAtr.COMPENSATION, name: '有償'};
        UNION = {value: GoOutReasonAtr.UNION, name: '組合'};
        
    }
}