/// <reference path='../../../../lib/nittsu/viewcontext.d.ts' />
module nts.uk.at.view.kbt002.k {

  const API = {
    exportCSV: "screen/at/outputexechistory/exportCSV",
  };

  @bean()
  export class KBT002KViewModel extends ko.ViewModel {
    empNames: KnockoutObservableArray<any> = ko.observableArray([]);

    selectEmpName: KnockoutObservable<number> = ko.observable(2);
    dateValue: KnockoutObservable<any> = ko.observable({});
    startDateString: KnockoutObservable<string> = ko.observable('');
    endDateString: KnockoutObservable<string> = ko.observable('');

    created() {
      const vm = this;
      let today = moment().utc();
      let currentDate = today.format("YYYY/MM/DD");
      
      vm.dateValue().startDate = currentDate;
      vm.dateValue().endDate = currentDate;

      vm.empNames = ko.observableArray([
        { code: 1, name: vm.$i18n('KBT002_275') },
        { code: 2, name: vm.$i18n('KBT002_276') }
      ]);

      vm.startDateString.subscribe(function(value){
        vm.dateValue().startDate = value;
        vm.dateValue.valueHasMutated();        
      });
      
      vm.endDateString.subscribe(function(value){
          vm.dateValue().endDate = value;   
          vm.dateValue.valueHasMutated();      
      });
    }

    getDataExportCsv() {
      const vm = this;
      const command: GetDataExportCSVModel = new GetDataExportCSVModel({
        isExportEmployeeName: vm.selectEmpName() == 1 ? false : true,
        startDate: moment.utc(vm.dateValue().startDate, 'YYYY/MM/DD').toISOString(),
        endDate: moment.utc(vm.dateValue().endDate, 'YYYY/MM/DD').toISOString()
      });
      
      vm.$blockui('grayout');
      vm.$ajax(API.exportCSV, command)
        .then((data: any) => {
          
        })
        .always(() => {
          vm.$blockui('clear');
        })
    }

    closeDialog() {
      const vm = this;
      vm.$window.close();
    }
  }

  export class GetDataExportCSVModel {
    isExportEmployeeName: boolean;
    startDate: string;
    endDate: string;
    constructor(init?: Partial<GetDataExportCSVModel>) {
      $.extend(this, init)
    }
  }
}