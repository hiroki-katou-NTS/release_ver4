/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />
module nts.uk.at.view.kwr005.b {
  const NUM_ROWS = 10;
  const KWR005_B_INPUT = 'KWR005_WORK_STATUS_DATA';
  const KWR005_B_OUTPUT = 'KWR005_WORK_STATUS_RETURN';
  const KWR005_C_INPUT = 'KWR005_C_DATA';
  const KWR005_C_OUTPUT = 'KWR005_C_RETURN';

  const PATH = {
    getSettingListWorkStatus: 'at/function/kwr/005/a/listworkledger',
    getSettingLitsWorkStatusDetails: 'at/function/kwr/005/b/detailworkledger',
    deleteSettingItemDetails: 'at/function/kwr/005/b/delete',
    createSettingItemDetails: 'at/function/kwr/005/b/create',
    updateSettingItemDetails: 'at/function/kwr/005/b/update',
    getFormInfo: 'at/screen/kwr/005/b/getinfor',
  };

  @bean()
  class ViewModel extends ko.ViewModel {

    settingListItems: KnockoutObservableArray<ItemModel> = ko.observableArray([]);
    columns: KnockoutObservableArray<any>;
    currentCode: KnockoutObservable<any>;
    currentCodeList: KnockoutObservable<string> = ko.observable(null);
    currentSettingCodeList: KnockoutObservableArray<any>;
    //属性
    printProperties: KnockoutObservableArray<any> = ko.observableArray(null);
    printPropertyCode: KnockoutObservable<any> = ko.observable(-1);
    //current setting   
    settingCategory: KnockoutObservable<number> = ko.observable(0);
    isSelectAll: KnockoutObservable<boolean> = ko.observable(false);
    isEnableAddButton: KnockoutObservable<boolean> = ko.observable(false);
    isEnableAttendanceCode: KnockoutObservable<boolean> = ko.observable(false);
    isEnableDeleteButton: KnockoutObservable<boolean> = ko.observable(false);
    isEnableDuplicateButton: KnockoutObservable<boolean> = ko.observable(false);
    isNewMode: KnockoutObservable<boolean> = ko.observable(false);

    //swapList
    currentCodeListSwap: KnockoutObservableArray<AttendanceDto> = ko.observableArray([]);
    keepListItemsSwap: KnockoutObservableArray<AttendanceDto> = ko.observableArray([]);
    listItemsSwap: KnockoutObservableArray<AttendanceDto> = ko.observableArray([]);
    gridHeight: KnockoutObservable<number> = ko.observable(331);

    mode: KnockoutObservable<ModelData> = ko.observable(new ModelData());

    constructor(params: any) {
      super();

      const vm = this;

      vm.getWorkStatusTableOutput();
      vm.getSettingList(params);
      vm.printAttributes();

      vm.currentCodeList.subscribe((newValue: any) => {
        nts.uk.ui.errors.clearAll();
        if (!newValue) return;
        vm.getSettingListForPrint(newValue);
      });

      vm.printPropertyCode.subscribe((newValue: any) => {
        $('#swapList-search-area-clear-btn').trigger('click');
        $('.ntsSwapSearchRight #swapList-search-area-input').val(null);
        vm.filterListMonthly(newValue);
      });

      vm.columns = ko.observableArray([
        { headerText: vm.$i18n('KWR005_107'), key: 'attendanceItemId', width: 80, formatter: _.escape },
        { headerText: vm.$i18n('KWR005_108'), key: 'attendanceItemName', width: 180, formatter: _.escape },
      ]);

      
    }

    created(params: any) {
      const vm = this;
      const userAgent = window.navigator.userAgent;
      let msie = userAgent.match(/Trident.*rv\:11\./);
      if (!_.isNil(msie) && msie.index > -1) vm.gridHeight(337);
    }

    mounted() {
      const vm = this;     
      //$("#swapList-grid1").igGrid("container").focus();
    }


    newSetting() {
      const vm = this;

      nts.uk.ui.errors.clearAll();         
      vm.currentCodeList(null);
      vm.currentCodeListSwap([]);
      vm.printPropertyCode(-1);   
      vm.isEnableDuplicateButton(false);
      vm.isEnableDeleteButton(false);
      vm.isEnableAttendanceCode(true);
      vm.createDefaultSettingDetails();
      vm.isNewMode(true);
      $('#KWR005_B52').focus();
    }

    /**
     * Registers setting
     */
    registerSetting() {
      const vm = this;

      $('.output-item').trigger('validate');
      if (nts.uk.ui.errors.hasError()) return;

      //Msg_1943
      if (vm.currentCodeListSwap().length <= 0) {
        vm.$dialog.error({ messageId: 'Msg_1943' }).then(() => {
          $("#swapList-grid2").igGrid("container").focus();
        });
        return;
      }

      let selectedItems: Array<any> = [];
      _.forEach(vm.currentCodeListSwap(), (x, index) => {
        selectedItems.push({ ranking: index + 1, attendanceId: x.attendanceItemId });
      });

      let params = {
        code: vm.mode().code(),
        name: vm.mode().name(),
        settingCategory: vm.settingCategory(),
        outputItemList: selectedItems,
        id: vm.mode().settingId()
      };

      //update
      let path_api = PATH.updateSettingItemDetails;

      //register
      if (vm.isNewMode()) {
        params.id = null;
        path_api = PATH.createSettingItemDetails;
      }

      vm.$blockui('show');
      vm.$ajax(path_api, params).done(() => {
        vm.$dialog.info({ messageId: 'Msg_15' }).then(() => {
          if (vm.isNewMode()) {
            vm.loadSettingList({ standOrFree: params.settingCategory, code: params.code });
          } else vm.isNewMode(false); //edit

        });
        vm.$blockui('hide');
      }).fail((error) => {
        let ctrlFocus = error.messageId === 'Msg_1927' ? '#KWR005_B52' : '#btnB11';
        $(ctrlFocus).ntsError('set', { messageId: error.messageId });
      }).always(() => vm.$blockui('hide'));

    }

    /**
     * Delete setting
     */
    deleteSetting() {
      const vm = this;

      vm.$blockui('show');

      const params = {
        settingId: vm.mode().settingId() //該当する設定ID
      };

      vm.$dialog.confirm({ messageId: 'Msg_18' }).then((answer: string) => {
        if (answer === 'yes') {
          vm.$ajax(PATH.deleteSettingItemDetails, params)
            .done(() => {
              vm.$dialog.info({ messageId: 'Msg_16' }).then(() => {
                vm.getPositionBeforeDelete();
                vm.$blockui('hide');
              })
            })
            .always(() => {
              vm.$blockui('hide');
            })
            .fail((error) => {
              vm.$dialog.error({ messageId: error.messageId }).then(() => {
                vm.$blockui('hide');
              })
            });
        }
      });
    }

    /**
     * Duplicate Setting
     * */
    /**
     * Shows dialog C
     */
    showDialogC() {
      const vm = this;

      let selectedObj = _.find(vm.settingListItems(), (x) => x.code === vm.currentCodeList());
      let params = {
        code: selectedObj.code,
        name: selectedObj.name,
        settingCategory: vm.settingCategory(),
        settingId: selectedObj.id //複製元の設定ID 
      }

      vm.$window.modal('/view/kwr/005/c/index.xhtml', ko.toJS(params)).then((data: any) => {
        if (_.isNil(data)) {
          return;
        }

        let params = {
          standOrFree: vm.settingCategory(),
          code: data.code
        };
        vm.loadSettingList(params);

      });

      $('#KWR005_B53').focus();
    }

    /**
     * Close dialog
     */
    closeDialog() {
      const vm = this;      
      vm.$window.close({ code: vm.mode().code() });
    }

    /**
     * Get setting list items details
     */
    getSettingListItemsDetails(settingId: string): Array<AttendanceDto> {
      const vm = this;     
      
      vm.currentCodeListSwap([]);
      vm.resetListItemsSwap();
      //call to server
      vm.$blockui('show');
      vm.$ajax(PATH.getSettingLitsWorkStatusDetails, { settingId: settingId })
        .done((result) => {
          if (result) {
            _.forEach(result.outputItemList, (x) => {
              let foundAttendance = _.find(vm.listItemsSwap(), (o) => parseInt(o.attendanceItemId) === parseInt(x.attendanceId));
              if (!_.isNil(foundAttendance)) {
                vm.currentCodeListSwap.push(new AttendanceDto(
                  foundAttendance.attendanceItemId,
                  foundAttendance.attendanceItemName
                ));
              }
            });
          }
          vm.$blockui('hide');
        })
        .fail()
        .always(() => vm.$blockui('hide'));

      return vm.currentCodeListSwap();
    }

    /**
     * Create default setting details
     */
    createDefaultSettingDetails() {
      const vm = this;

      let newMode: ModelData = new ModelData();
      vm.mode(newMode);

    }

    getSettingListForPrint(code: string) {
      const vm = this;

      if (!_.isNil(code)) {
        let selectedObj = _.find(vm.settingListItems(), (x: any) => x.code === code);

        if (!_.isNil(selectedObj)) {

          vm.isEnableAttendanceCode(false);
          vm.isEnableAddButton(true);
          vm.isEnableDeleteButton(true);
          vm.isEnableDuplicateButton(true);
          vm.isNewMode(false);

          let newMode: ModelData = new ModelData();
          newMode.code(selectedObj.code);
          newMode.name(selectedObj.name);
          newMode.settingId(selectedObj.id);
          //load details          
          let selectedItems = vm.getSettingListItemsDetails(selectedObj.id);
          newMode.selectedItems(selectedItems);
          vm.mode(newMode);
        }
      }

      $('#KWR005_B53').focus();
    }

    resetSettingListItems(): JQueryPromise<any> {
      const vm = this;
      const dfd = $.Deferred<any>();

      vm.printPropertyCode(-1);
      dfd.resolve();
      return dfd.promise();
    }

    getWorkStatusTableOutput() {
      const vm = this;

      vm.$blockui('show');

      vm.$ajax(PATH.getFormInfo, { formNumberDisplay: 8 }).done((result) => {

        if (result && result.listMonthly) {

          let listItemsSwap: Array<AttendanceDto> = [];
          _.forEach(result.listMonthly, (item) => {
            let Item = new AttendanceDto(
              item.attendanceItemId,
              item.attendanceItemName,
              item.attributes,
              item.attendanceItemDisplayNumber
            );
            listItemsSwap.push(Item);
          });

          listItemsSwap = _.orderBy(listItemsSwap, ['attendanceItemId', 'asc']);

          vm.keepListItemsSwap(listItemsSwap); //base
          vm.resetListItemsSwap();
          //vm.listItemsSwap(_.cloneDeep(vm.keepListItemsSwap()));
        }
        vm.$blockui('hide');
      }).always(() => vm.$blockui('hide'));
    }

    getSettingList(params: any) {
      const vm = this;

      if (!params) return;
      vm.settingCategory(params.standOrFree);
      vm.loadSettingList(params);
    }

    loadSettingList(params: any) {
      const vm = this;
      let listWorkStatus: Array<any> = [];

      vm.$blockui('grayout');

      vm.$ajax(PATH.getSettingListWorkStatus, { setting: params.standOrFree }).done((data) => {
        if (!_.isNil(data) && data.length > 0) {
          _.forEach(data, (item) => {
            let code = _.padStart(item.settingDisplayCode, 2, '0');
            listWorkStatus.push(new ItemModel(code, _.trim(item.settingName), item.settingId));
          });

          //sort by code with asc
          vm.settingListItems([]);
          listWorkStatus = _.orderBy(listWorkStatus, ['code', 'asc']);
          vm.settingListItems(listWorkStatus);

          let code = (!_.isNil(data) && !_.isNil(params.code)) ? _.padStart(params.code, 2, '0') : null;
          if (vm.settingListItems().length > 0) {
            let firstItem: any = _.head(vm.settingListItems());
            if (!code) code = firstItem.code;
          }

          vm.currentCodeList(code);
          vm.isNewMode(false);

        } else {
          //create new the settings list
          vm.newSetting();
        }
        vm.$blockui('hide');
      });
    }

    getPositionBeforeDelete() {
      const vm = this;
      let newSelectedCode = null;

      let index = _.findIndex(vm.settingListItems(), (x) => x.code === vm.currentCodeList());
      if (vm.settingListItems().length > 1) {
        if (index === vm.settingListItems().length - 1)
          index = index - 1;
        else
          index = index + 1;
        newSelectedCode = vm.settingListItems()[index].code;
      }

      let newSettingListItems = _.filter(vm.settingListItems(), (x) => x.code !== vm.currentCodeList());
      vm.settingListItems([]);
      if (newSettingListItems.length > 0) {
        vm.settingListItems(newSettingListItems);
        vm.currentCodeList(newSelectedCode);
      } else {
        vm.newSetting(); //create new
      }
    }

    filterListMonthly(value: number) {
      const vm = this;
      vm.resetListItemsSwap();
      if (value !== -1) {
        let newListSwap: Array<AttendanceDto> = _.filter(vm.listItemsSwap(), (x) => x.attributes === value);
        vm.listItemsSwap(newListSwap);
      }
    }

    printAttributes() {
      const vm = this;
      const printAttributes = [
        { code: -1, name: vm.$i18n('KWR005_114') }, //全件
        { code: 4, name: vm.$i18n('KWR005_115') }, //時間
        { code: 5, name: vm.$i18n('KWR005_116') }, //回数
        { code: 6, name: vm.$i18n('KWR005_117') }, //日数
        { code: 7, name: vm.$i18n('KWR005_118') }, //金額
        { code: -2, name: vm.$i18n('KWR005_119') }, //その他
      ];
      vm.printProperties = ko.observableArray(printAttributes);
    }

    resetListItemsSwap() {
      const vm = this;
      vm.listItemsSwap.removeAll();
      vm.listItemsSwap(_.cloneDeep(vm.keepListItemsSwap()));
    }

    afterMoveFromRightToLeft(toRight, oldSource, newI){      
      const vm = this;
      console.log(vm);
      let newListItems: Array<AttendanceDto> = _.orderBy(newI, 'attendanceItemId', 'asc');  
      nts.uk.ui._viewModel.content.listItemsSwap.removeAll();
      nts.uk.ui._viewModel.content.listItemsSwap(newListItems);
    }
  }

  //================================================================= 
  export class ItemModel {
    id: string;
    code: string;
    name: string;
    constructor(code?: string, name?: string, id?: string) {
      this.code = code;
      this.name = name;
      this.id = id;
    }
  }

  export class ModelData {
    code?: KnockoutObservable<string>;
    name?: KnockoutObservable<string>;
    settingId?: KnockoutObservable<string>;
    selectedItems?: KnockoutObservableArray<AttendanceDto>;

    constructor(code?: string, name?: string, settingId?: string, selectedItems?: Array<AttendanceDto>) {
      this.code = ko.observable(code);
      this.name = ko.observable(name);
      this.settingId = ko.observable(settingId);
      this.selectedItems = ko.observableArray(selectedItems);
    }
  }

  export class AttendanceDto {
    attendanceItemId: string;
    attendanceItemName: string;
    attributes: number;
    displayNumber: string;
    constructor(id?: string, name?: string, attributes?: number, displayNumber?: string) {
      this.attendanceItemId = id;
      this.attendanceItemName = name;
      this.attributes = attributes;
      this.displayNumber = displayNumber;
    }
  }
}