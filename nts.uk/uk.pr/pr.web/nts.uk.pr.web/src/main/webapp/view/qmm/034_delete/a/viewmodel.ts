module qmm034.a.viewmodel {
    export class ScreenModel {
        constraint: string = 'LayoutCode';
        date: KnockoutObservable<Date>;
        //list era;
        items: KnockoutObservableArray<EraModel>;
        columns: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
        currentCode: KnockoutObservable<any>;
        //layouts: KnockoutObservableArray<SingleSelectedCode>;
        currentCodeList: KnockoutObservableArray<any>;
        currentEra: KnockoutObservable<EraModel>;
        isUpdate: KnockoutObservable<boolean> = ko.observable(true);

        constructor() {
            let self = this;
            self.date = ko.observable(new Date('2016/01/02'));
            self.items = ko.observableArray([
                new EraModel('明明', 'M', "1999/01/25"),
                new EraModel('大正', 'T', "1912/07/30"),
                new EraModel('大明', 'S', "1926/12/25"),
                new EraModel('大元', 'H', "1989/01/08"),
                new EraModel('大記', 'N', "2016/02/18"),
            ]);
            self.columns = ko.observableArray([
                { headerText: '元号', prop: 'code', width: 50 },
                { headerText: '記号', prop: 'name', width: 50 },
                { headerText: '開始年月日', prop: 'startDateText', width: 80 },
            ]);
            self.currentCode = ko.observable(null);
            self.currentCodeList = ko.observableArray([]);
            //Tim object dau tien
            self.currentEra = ko.observable(_.first(self.items()));

            self.currentCode.subscribe(function(codeChanged) {
                self.currentEra(self.getEra(codeChanged));
            });

        }

        register() {

            let self = this;
            if (self.isUpdate() === false) {
                self.insertData();
            } else {
                self.update;
            }
        }
        refreshLayout(): void {
            let self = this;
            self.currentEra(new EraModel('', '', new Date().toString()));
            self.isUpdate = ko.observable(false);
        }
        insertData() {
            let self = this;
            let newData = self.currentEra();
            let newEradata = self;
            // var x = self.items();
            //x.push(newData);
            if (self.isUpdate() === false) {
                self.items.push(newData);
                self.isUpdate = ko.observable(true);
            }
            // alert('insert ok');
        }
        alertDelete(){
            let self= this;
            if(confirm("do you wanna delete")=== true){
                    self.deleteData();
            }else{
                    alert("you didnt delete!");
            }
        }
        deleteData() {
            let self = this;
            let newDel = self.currentEra();
            self.items.splice(self.items().indexOf(newDel), 1);
        }

        getEra(codeNew): EraModel {
            let self = this;
            let era: EraModel = _.find(self.items(), function(item) {
                return item.code === codeNew;
            });

            return era;
        }
        update(eraCodeNew): EraModel {
            let self = this;
            //let newCurrentEra = self.currentEra;
            self.currentCode.subscribe(function(codeChanged) {
                self.currentEra(self.getEra(codeChanged));
            });
            let newCurrentEra: EraModel = _.findIndex(self.items(), function(item) {
                return item.code === eraCodeNew;
            });
            //            var x = self.items();
            //            x.push(newCurrentEra);
            //            self.items(x);
            return eraCodeNew;

        }


        selectSomeItems() {
            this.currentCode('150');
            this.currentCodeList.removeAll();
            this.currentCodeList.push('001');
            this.currentCodeList.push('ABC');
        }

        deselectAll() {
            this.currentCode(null);
            this.currentCodeList.removeAll();
        }

    }

    class EraModel {
        code: string;
        name: string;
        startDateText: string;
        startDate: KnockoutObservable<Date>;

        constructor(code: string, name: string, startDate: string) {
            this.code = code;
            this.name = name;
            this.startDate = ko.observable(new Date(startDate));
            this.startDateText = this.startDate().toDateString();
        }
    }
    //    class SingleSelectedCode {
    //        layout: KnockoutObservable<any>;
    //        strName: string;
    //        strShortcutName: string;
    //        strDate: string;
    //        constructor(shortCutName: string, Date: any) {
    //            let self = this;
    //            self.strName = name;
    //            self.strShortcutName = shortCutName;
    //            self.strDate = Date;
    //        }
    //    }


}