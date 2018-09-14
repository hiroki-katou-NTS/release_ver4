module nts.uk.pr.view.qmm005.a.viewmodel {
    import close = nts.uk.ui.windows.close;
    import getText = nts.uk.resource.getText;
    import model = nts.uk.pr.view.qmm005.share.model;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import block = nts.uk.ui.block;
    import CurrentProcessDate = nts.uk.pr.view.qmm005.share.model.CurrentProcessDate;
    export class ScreenModel {
        //A2_2
        itemTable:Array<ItemTable>;
        //A3_4 対象雇用
        targetEmployment:KnockoutObservableArray<number>;
        processCategoryNO:number;


        processInfomations:KnockoutObservableArray<model.ProcessInfomation>;
        setDaySupports:KnockoutObservableArray<model.SetDaySupport>;
        currentProcessDates:KnockoutObservableArray<model.CurrentProcessDate>;
        empTiedProYear:KnockoutObservableArray<model.EmpTiedProYear>;

        itemBinding:KnockoutObservableArray<ItemBinding>;
        itemTable:ItemTable;

        constructor() {
            var self = this;
            self.itemTable=new Array<ItemTable>(5);
            //A3_4 対象雇用
            self.targetEmployment=ko.observable([]);
            self.itemTable=ko.observableArray();

            self.processInfomations=ko.observableArray([]);
            self.currentProcessDates=ko.observableArray([]);
            self.setDaySupports=ko.observableArray([]);
            self.empTiedProYear=ko.observableArray([]);


            self.itemTable=new ItemTable(self.processInfomations,self.setDaySupports,self.currentProcessDates,self.empTiedProYear);
            self.itemBinding=ko.observableArray([]);
            for(let i:number=0;i<5;i++){
                self.itemBinding.push(new ItemBinding(
                    self.itemTable.processInfomations()[i],
                    _.filter(self.itemTable.setDaySupports(),function (o) {
                        return o.processCategoryNO==i;
                    }),
                    self.itemTable.currentProcessDates()[i],
                    self.itemTable.empTiedProYear()[i])
                )
            }





        }




        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();

            dfd.resolve();
            return dfd.promise();
        }

    }

    export class ItemBinding{
        processInfomations:model.ProcessInfomation;
        setDaySupports:Array<model.SetDaySupport>;
        currentProcessDates:model.CurrentProcessDate;
        empTiedProYear:model.EmpTiedProYear;

        constructor(
            processInfomations:model.ProcessInfomation,
            setDaySupports:Array<model.SetDaySupport>,
            currentProcessDates:model.CurrentProcessDate,
            empTiedProYear:model.EmpTiedProYear

        ){
            this.processInfomations=processInfomations;
            this.currentProcessDates=currentProcessDates;
            this.setDaySupports=setDaySupports;
            this.empTiedProYear=empTiedProYear;
        }




    }


    export class ItemTable{
        processInfomations:KnockoutObservableArray<model.ProcessInfomation>;
        setDaySupports:KnockoutObservableArray<model.SetDaySupport>;
        currentProcessDates:KnockoutObservableArray<model.CurrentProcessDate>;
        empTiedProYear:KnockoutObservableArray<model.EmpTiedProYear>;

        constructor(
            processInfomations:KnockoutObservableArray<model.ProcessInfomation>,
            setDaySupports:KnockoutObservableArray<model.SetDaySupport>,
            currentProcessDates:KnockoutObservableArray<model.CurrentProcessDate>,
            empTiedProYear:KnockoutObservableArray<model.EmpTiedProYear>

        ){
            this.processInfomations=ko.observableArray(processInfomations);
            this.currentProcessDates=ko.observableArray(currentProcessDates);
            this.setDaySupports=ko.observableArray(setDaySupports);
            this.empTiedProYear=ko.observableArray(empTiedProYear);
        }


    }







}
