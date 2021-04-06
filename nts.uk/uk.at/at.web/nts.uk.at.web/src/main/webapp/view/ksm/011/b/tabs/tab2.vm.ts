/// <reference path="../../../../../lib/nittsu/viewcontext.d.ts" />
module nts.uk.at.view.ksm011.b.tabs.tab2 {
  const PATH = {
    registerRulesOrgShiftTable: 'at/schedule/shift/table/organization/register',
    getRulesOfOrgShiftTable: 'screen/at/shift/table/retrievingOrgShiftTableRule',
    getAllRulesOfOrgShiftTable: 'screen/at/shift/table/setTissueList',
  };

  @bean()
  export class ViewModel extends ko.ViewModel {
    switchItems: KnockoutObservableArray<any>;
    publicMethod: KnockoutObservable<number> = ko.observable(1);
    workRequestSelected: KnockoutObservable<number> = ko.observable(1);
    workRequest: KnockoutObservable<number> = ko.observable(1);
    maxDesiredHolidays: KnockoutObservable<number> = ko.observable(1);

    daysList: KnockoutObservableArray<any>;
    workRequestInput: KnockoutObservableArray<any>;
    workRequestInputSelected: KnockoutObservable<number> = ko.observable(1);
    deadlineSelected: KnockoutObservable<number> = ko.observable(0);
    deadlineWorkSelected: KnockoutObservable<number> = ko.observable(0);

    selectedWkpId: KnockoutObservable<any>;
    selectedWkpGroupId: KnockoutObservable<any>;
    baseDate: KnockoutObservable<Date>;
    alreadySettingWorkplaces: KnockoutObservableArray<UnitAlreadySettingModel>;
    alreadySettingWorkplaceGroups: KnockoutObservableArray<string>;
    treeGrid: any;

    workplaceName: KnockoutObservable<string> = ko.observable('');
    kcp004Data: KnockoutObservable<any> = ko.observable(null);

    replaceThisVar: KnockoutObservable<number> = ko.observable(0);
    replaceThisName: KnockoutObservable<string> = ko.observable(null);
    currentNames: KnockoutObservable<string> = ko.observable(null);

    active: KnockoutObservable<boolean> = ko.observable(false);
    constructor(params: any) {
      super();
      const vm = this;
      vm.switchItems = ko.observableArray([
        { code: 1, name: vm.$i18n('KSM011_21') },
        { code: 0, name: vm.$i18n('KSM011_22') }
      ]);

      vm.workRequestInput = ko.observableArray([
        { code: 1, name: vm.$i18n('KSM011_21') },
        { code: 0, name: vm.$i18n('KSM011_22') }
      ]);

      vm.replaceThisVar.subscribe( code => {
        let findIt = _.find(vm.switchItems(), (x) => { return x.code === code; });
        vm.replaceThisName(findIt.name);
        if (vm.replaceThisVar() == 0) vm.selectedWkpId.valueHasMutated();
        else vm.selectedWkpGroupId.valueHasMutated();
      });

      vm.daysList = ko.observableArray([]);
      let days = [];
      for (let day = 1; day <= 31; day++) {
          days.push({ day: day, name: day + vm.$i18n('KSM011_105') });
      }
      days.push({ day: 32, name: vm.$i18n('KSM011_106') });
      vm.daysList(days);
      vm.baseDate = ko.observable(new Date());
      vm.selectedWkpId = ko.observable(null);
      vm.selectedWkpGroupId = ko.observable(null);
      vm.alreadySettingWorkplaces = ko.observableArray([]);
      vm.alreadySettingWorkplaceGroups = ko.observableArray([]);

      vm.selectedWkpId.subscribe(value => {
        if (vm.replaceThisVar() == 0) {
            if (vm.active()) vm.findElement($('#workplace-tree-grid').getDataList(), value);
            vm.getRulesOfOrgShiftTable();
        }
      });
      vm.selectedWkpGroupId.subscribe(value => {
          if (vm.replaceThisVar() == 1) {
              if (vm.active()) {
                  const wkpGroups: Array<any> = ko.dataFor($("#workplace-group-pannel")[0]).workplaceGroups();
                  const selectedWkpg = _.find(wkpGroups, (i: any) => i.id == value);
                  vm.workplaceName(selectedWkpg ? selectedWkpg.name : "");
              }
              vm.getRulesOfOrgShiftTable();
          }
      });
      vm.currentNames.subscribe(value => {
        console.log(value);
      });
      vm.getAlreadySetting();
    }

    mounted() {
      const vm = this;
    }

    initialLoadPage() {
      const vm = this;
      vm.active(true);
      vm.replaceThisVar.valueHasMutated();
    }

    //会社のシフト表のルールを登録する
    registerRulesCompanyShiftTable() {
      const vm = this;

      vm.$blockui('show');
      let params = {
          organizationSelection: {
              UnitSelected: vm.replaceThisVar(),
              OrganizationIdSelected: vm.replaceThisVar() == 0 ? vm.selectedWkpId() : vm.selectedWkpGroupId(),
          },
          usePublicAtr: vm.publicMethod(),//公開機能の利用区分
          useWorkAvailabilityAtr: vm.workRequest(),//勤務希望の利用区分
          holidayMaxDays: vm.maxDesiredHolidays(),//希望休日の上限日数入力
          closureDate: vm.deadlineSelected(),//締め日
          availabilityDeadLine: vm.deadlineWorkSelected(),//締切日
          availabilityAssignMethod: vm.workRequestInputSelected()//入力方法の利用区分
      };

      vm.$ajax(PATH.registerRulesOrgShiftTable, params).done((data) => {
        vm.$dialog.info({ messageId: 'Msg_15' }).then(() => {
            vm.getAlreadySetting();
        });
      }).fail((error) => {
        vm.$dialog.error(error);
      }).always(() => {
        vm.$blockui('hide');
      });

    }

    //Get the rules of the company shift table
    getRulesOfOrgShiftTable() {
      const vm = this;
      vm.$blockui('show');
      vm.$ajax(PATH.getRulesOfOrgShiftTable, {
        UnitSelected: vm.replaceThisVar(),
        OrganizationIdSelected: vm.replaceThisVar() == 0 ? vm.selectedWkpId() : vm.selectedWkpGroupId()
      }).done((data) => {
        if (data) {
          vm.publicMethod(data.usePublicAtr);//Ba3_2	 公開機能の利用区分
          vm.workRequest(data.useWorkAvailabilityAtr);//Ba4_2 勤務希望の利用区分
          vm.maxDesiredHolidays(data.holidayMaxDays); //Ba4_6				希望休日の上限日数入力
          vm.deadlineSelected(data.closureDate); //Ba4_9				締め日
          vm.deadlineWorkSelected(data.availabilityDeadLine); //Ba4_11				締切日
          vm.workRequestInputSelected(data.availabilityAssignMethod); //Ba4_13				入力方法の利用区分
        } else {
          vm.publicMethod(1);//Ba3_2	 公開機能の利用区分
          vm.workRequest(1);//Ba4_2 勤務希望の利用区分
          vm.maxDesiredHolidays(1); //Ba4_6				希望休日の上限日数入力
          vm.deadlineSelected(1); //Ba4_9				締め日
          vm.deadlineWorkSelected(1); //Ba4_11				締切日
          vm.workRequestInputSelected(1); //Ba4_13				入力方法の利用区分
        }
        $(".switchButton-wrapper")[4].focus();
      }).fail((error) => {
        vm.$dialog.error(error);
      }).always(() => {
        vm.$blockui('hide');
      });
    }

    getAlreadySetting() {
      const vm = this;
      vm.$blockui('show');
      vm.$ajax(PATH.getAllRulesOfOrgShiftTable).done((data) => {
          if (data) {
              vm.alreadySettingWorkplaces((data.workPlaceList || []).map((i: string) => ({
                  workplaceId: i,
                  isAlreadySetting: true
              })));
              vm.alreadySettingWorkplaceGroups(data.workPlaceGroupList || []);
          }
      }).fail((error) => {
          vm.$dialog.error(error);
      }).always(() => {
          vm.$blockui('hide');
      });
    }

    findElement(listItems: Array<any>, wpId: string) {
      const vm = this;
      _.forEach(listItems, (x) => {
        if (wpId === x.id) {
          vm.workplaceName(x.name);
        } else if (x.children.length > 0) {
          vm.findElement(x.children, wpId);
        }
      });
    }

  }

  export interface UnitAlreadySettingModel {
    workplaceId: string;
    isAlreadySetting: boolean;
  }
}