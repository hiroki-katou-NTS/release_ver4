/// <reference path='../../../../lib/nittsu/viewcontext.d.ts' />

module nts.uk.at.view.ccg005.e.screenModel {

  const API = {
    getGoOutInformation: "screen/com/ccg005/get-go-out-information",
    delete: "ctx/office/goout/employee/information/delete",
    saveOrUpdate: "ctx/office/goout/employee/information/save",
  };

  interface createProps {
    //社員ID				
    sid: string,
    //ビジネスネーム		
    businessName: string,
    //年月日		
    goOutDate: string
  }

  interface GoOutEmployeeInformationDto {
    // 外出時刻
    goOutTime: number;

    // 外出理由
    goOutReason: string;

    // 年月日
    goOutDate: string;

    // 戻り時刻
    comebackTime: number;

    // 社員ID
    sid: string;
  }
  @bean()
  export class ViewModel extends ko.ViewModel {
    goOutDate: KnockoutObservable<string> = ko.observable(moment().utc().format("YYYY/MM/DD"));
    goOutTime: KnockoutObservable<number> = ko.observable();
    comebackTime: KnockoutObservable<number> = ko.observable();
    goOutReason: KnockoutObservable<string> = ko.observable();
    businessName: KnockoutObservable<string> = ko.observable();
    sid: KnockoutObservable<string> = ko.observable();
    disableBusinessName: KnockoutObservable<boolean> = ko.observable(true);
    isGoOut: KnockoutObservable<boolean> = ko.observable(false);


    created(props: createProps) {
      const vm = this;
      vm.sid(props.sid);
      vm.disableBusinessName(!(props.sid === __viewContext.user.employeeId));
      vm.businessName(props.businessName);
      vm.goOutDate(props.goOutDate);
      vm.$blockui('grayout');
      vm.$ajax(API.getGoOutInformation, {
        sid: props.sid,
        date: moment(props.goOutDate),
      }).then((data: GoOutEmployeeInformationDto) => {
        if(data) {
          vm.goOutReason(data.goOutReason);
          vm.goOutTime(data.goOutTime);
          vm.comebackTime(data.comebackTime);
        }
      }).always(() => {
        vm.$blockui('clear');
      });
    }

    public closeDialog() {
      const vm = this;
      vm.isGoOut(false);
      vm.$window.close(vm.isGoOut());
    }

    public deleteGoOut() {
      const vm = this;
      vm.$dialog.confirm({ messageId: "Msg_18" }).then((result) => {
        if (result === "yes") {
          const gouOutInfo = new GoOutEmployeeInformationDel({
            goOutDate: moment(vm.goOutDate()),
            sid: vm.sid(),
          });
          vm.$blockui('grayout');
          vm.$ajax(API.delete, gouOutInfo)
            .then(() => {
              vm.$blockui('clear');
              vm.goOutReason("");
              vm.goOutTime(undefined);
              vm.comebackTime(undefined);
              return vm.$dialog.info({ messageId: "Msg_16" });
            })
            .always(() => {
              vm.$blockui('clear');
            });
        }
      });
    }

    public saveGoOut() {
      const vm = this;
      vm.$validate().then((valid: boolean) => {
        if (valid) {
          const gouOutInfo = new GoOutEmployeeInformation({
            goOutTime: vm.goOutTime(),
            goOutReason: vm.goOutReason(),
            goOutDate: moment(vm.goOutDate()),
            comebackTime: vm.comebackTime(),
            sid: vm.sid(),
          });
          vm.$blockui('grayout');
          vm.$ajax(API.saveOrUpdate, gouOutInfo)
            .then(() => {
              vm.$blockui('clear');
              vm.$dialog.info({ messageId: "Msg_15" });
              vm.isGoOut(true);
              vm.$window.close(vm.isGoOut());
            })
            .always(() => {
              vm.$blockui('clear');
            });
        }
      });
    }
  }

  //社員の外出情報 Save or Update
  class GoOutEmployeeInformation {
    // 外出時刻
    goOutTime: number;

    // 外出理由
    goOutReason: string;

    // 年月日
    goOutDate: moment.Moment;

    // 戻り時刻
    comebackTime: number;

    // 社員ID
    sid: string;

    constructor(init?: Partial<GoOutEmployeeInformation>) {
      $.extend(this, init);
    }
  }

  //社員の外出情報 Delete
  class GoOutEmployeeInformationDel {
    // 年月日
    goOutDate: moment.Moment;

    // 社員ID
    sid: string;

    constructor(init?: Partial<GoOutEmployeeInformationDel>) {
      $.extend(this, init);
    }
  }
}
