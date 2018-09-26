module nts.uk.pr.view.qmm011.e.viewmodel {
    import close = nts.uk.ui.windows.close;
    import getText = nts.uk.resource.getText;
    import dialog  = nts.uk.ui.dialog;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import model = qmm011.share.model;
    import error = nts.uk.ui.errors;
    export class ScreenModel {
        startYearMonth:              KnockoutObservable<number> = ko.observable();
        takeOver:                    KnockoutObservable<number> = ko.observable(0);
        startLastYearMonth:          KnockoutObservable<number> = ko.observable();
        listTakeOver:                KnockoutObservableArray<any> = ko.observableArray(getListtakeOver());
        
        constructor() {
            let params = getShared('QMM011_E_PARAMS_INPUT');
            if (params && params.startYearMonth) {
                this.startLastYearMonth(params.startYearMonth);
                this.startYearMonth(Number(this.startLastYearMonth()));
                this.listTakeOver()[0] = new model.ItemModel(0,getText('QMM011_48', [this.convertMonthYearToString(this.startYearMonth())]));
            }
        }

        register(){
            let self = this;
            if (self.validate()) {
                return;
            }
            setShared('QMM011_E_PARAMS_OUTPUT', {
                startYearMonth: self.startYearMonth(),
                transferMethod: self.takeOver()
            });
            close();
        }
        
        validate (){
            let self = this;
            nts.uk.ui.errors.clearAll();
            $("#E1_5").trigger("validate");
            if (self.startLastYearMonth() > self.startYearMonth() || self.startLastYearMonth() == self.startYearMonth()){
                dialog.alertError({ messageId: "Msg_79" });
                return true;
            }
            return error.hasError(); 
        }
        
        cancel(){
            close();
        }
        
        convertMonthYearToString(yearMonth: any) {
            let self = this;
            let year: string, month: string;
            yearMonth = yearMonth.toString();
            year = yearMonth.slice(0, 4);
            month = yearMonth.slice(4, 6);
            return year + "/" + month;
        }
        
        convertStringToYearMonth(yearMonth: any){
            let self = this;
            let year: string, month: string;
            yearMonth = yearMonth.slice(0, 4) + yearMonth.slice(5, 7);
            return Number(yearMonth);
        }
        
     
    }
    

    
    export function getListtakeOver(): Array<model.ItemModel> {
        return [
            new model.ItemModel(0, getText('QMM011_48')),
            new model.ItemModel(1, getText('QMM011_49'))
        ];
    }
    
}