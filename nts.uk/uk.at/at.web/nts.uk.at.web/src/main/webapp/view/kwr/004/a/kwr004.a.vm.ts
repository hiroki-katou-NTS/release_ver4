/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />

module nts.uk.at.view.kwr004.a {
  import common = nts.uk.at.view.kwr004.common;
  //import ComponentOption = kcp.share.list.ComponentOption;

  const WORK_STATUS = 'WorkStatus';
  const KWR004_B_INPUT = 'KWR004_WORK_STATUS_DATA';
  const KWR004_B_OUTPUT = 'KWR004_WORK_STATUS_RETURN';

  @bean()
  class ViewModel extends ko.ViewModel {

    // start variable of CCG001
    ccg001ComponentOption: common.GroupOption;
    // end variable of CCG001

    //panel left
    startDate: KnockoutObservable<Date> = ko.observable(new Date());
    endDate: KnockoutObservable<Date> = ko.observable(new Date());
    periodDate: KnockoutObservable<any> = ko.observable({});
    yearMonth: KnockoutObservable<number> = ko.observable(202010);;

    //panel right
    rdgSelectedId: KnockoutObservable<number> = ko.observable(0);
    standardSelectedCode: KnockoutObservable<string> = ko.observable(null);
    freeSelectedCode: KnockoutObservable<string> = ko.observable(null);

    isEnableSelectedCode: KnockoutObservable<boolean> = ko.observable(true);
    zeroDisplayClassification: KnockoutObservable<number> = ko.observable(0);
    pageBreakSpecification: KnockoutObservable<number> = ko.observable(0);
    isWorker: KnockoutObservable<boolean> = ko.observable(true);
    settingListItems: KnockoutObservableArray<any> = ko.observableArray([]);

    // start declare KCP005
    listComponentOption: any;
    selectedCode: KnockoutObservable<string>;
    multiSelectedCode: KnockoutObservableArray<string>;
    isShowAlreadySet: KnockoutObservable<boolean>;
    alreadySettingList: KnockoutObservableArray<common.UnitAlreadySettingModel>;
    isDialog: KnockoutObservable<boolean>;
    isShowNoSelectRow: KnockoutObservable<boolean>;
    isMultiSelect: KnockoutObservable<boolean>;
    isShowWorkPlaceName: KnockoutObservable<boolean>;
    isShowSelectAllButton: KnockoutObservable<boolean>;
    disableSelection: KnockoutObservable<boolean>;

    employeeList: KnockoutObservableArray<common.UnitModel>;
    baseDate: KnockoutObservable<Date>;
    // end KCP005

    mode: KnockoutObservable<common.UserSpecificInformation> = ko.observable(null);

    constructor(params: any) {
      super();
      let vm = this;

      vm.periodDate({
        startDate: moment(new Date()),
        endDate: moment(new Date()).add(1, 'year').subtract(1, 'month')
      });

      vm.getSettingListItems();

      vm.rdgSelectedId.subscribe((value) => {
        vm.isEnableSelectedCode(value === common.StandardOrFree.Standard);
      });

      vm.CCG001_load();
      vm.KCP005_load();
      vm.initialWorkStatusInformation();
    }

    created(params: any) {
      let vm = this;
    }

    mounted() {
      let vm = this;

      $('#kcp005 table').attr('tabindex', '-1');
      $('#btnExportExcel').focus();
    }

    CCG001_load() {
      let vm = this;
      // Set component option
      vm.ccg001ComponentOption = {
        /** Common properties */
        systemType: 2,
        showEmployeeSelection: true,
        showQuickSearchTab: true,
        showAdvancedSearchTab: true,
        showBaseDate: true,
        showClosure: true,
        showAllClosure: true,
        showPeriod: true,
        periodFormatYM: false,

        /** Required parameter */
        baseDate: moment().toISOString(), //基準日
        //periodStartDate: periodStartDate, //対象期間開始日
        //periodEndDate: periodEndDate, //対象期間終了日
        //dateRangePickerValue: vm.datepickerValue
        inService: true, //在職区分 = 対象
        leaveOfAbsence: true, //休職区分 = 対象
        closed: true, //休業区分 = 対象
        retirement: false, // 退職区分 = 対象外

        /** Quick search tab options */
        showAllReferableEmployee: true,
        showOnlyMe: false,
        showSameDepartment: false,
        showSameDepartmentAndChild: false,
        showSameWorkplace: true,
        showSameWorkplaceAndChild: true,

        /** Advanced search properties */
        showEmployment: true,
        showDepartment: false,
        showWorkplace: true,
        showClassification: true,
        showJobTitle: true,
        showWorktype: true,
        isMutipleCheck: true,

        tabindex: - 1,
        /**
        * vm-defined function: Return data from CCG001
        * @param: data: the data return from CCG001
        */
        returnDataFromCcg001: function (data: common.Ccg001ReturnedData) {
          vm.getListEmployees(data);
        }
      }
      // Start component
      $('#CCG001').ntsGroupComponent(vm.ccg001ComponentOption);
    }

    KCP005_load() {
      let vm = this;

      // start define KCP005
      vm.baseDate = ko.observable(new Date());
      vm.selectedCode = ko.observable('1');
      vm.multiSelectedCode = ko.observableArray(['0', '1', '4']);
      vm.isShowAlreadySet = ko.observable(false);
      vm.alreadySettingList = ko.observableArray([
        { code: '1', isAlreadySetting: true },
        { code: '2', isAlreadySetting: true }
      ]);
      vm.isDialog = ko.observable(true);
      vm.isShowNoSelectRow = ko.observable(false);
      vm.isMultiSelect = ko.observable(true);
      vm.isShowWorkPlaceName = ko.observable(true);
      vm.isShowSelectAllButton = ko.observable(true);
      //vm.disableSelection = ko.observable(false);
      vm.employeeList = ko.observableArray<common.UnitModel>([]);

      vm.listComponentOption = {
        isShowAlreadySet: vm.isShowAlreadySet(),
        isMultiSelect: vm.isMultiSelect(),
        listType: common.ListType.EMPLOYEE,
        employeeInputList: vm.employeeList,
        selectType: common.SelectType.SELECT_BY_SELECTED_CODE,
        selectedCode: vm.multiSelectedCode,
        isDialog: vm.isDialog(),
        isShowNoSelectRow: vm.isShowNoSelectRow(),
        alreadySettingList: vm.alreadySettingList,
        isShowWorkPlaceName: vm.isShowWorkPlaceName(),
        isShowSelectAllButton: vm.isShowSelectAllButton(),
        isSelectAllAfterReload: true,
        tabindex: 5,
        maxRows: 15
      };

      $('#kcp005').ntsListComponent(vm.listComponentOption)
    }

    /**
     *  get employees from CCG001
     */

    getListEmployees(data: common.Ccg001ReturnedData) {
      let vm = this,
        employeeSearchs: Array<common.UnitModel> = [];

      _.forEach(data.listEmployee, function (value: any) {
        var employee: common.UnitModel = {
          id: value.employeeId,
          code: value.employeeCode,
          name: value.employeeName,
          affiliationName: value.affiliationName
        };
        employeeSearchs.push(employee);
      });

      vm.employeeList(employeeSearchs);
    }

    /**
     * Duplicate Setting
     * */

    showDialogScreenB() {
      let vm = this;

      let selectedItem = vm.rdgSelectedId();
      let attendenceItem = selectedItem ? vm.freeSelectedCode() : vm.standardSelectedCode();
      let attendence: any = _.find(vm.settingListItems(), (x) => x.code === attendenceItem);

      if (_.isNil(attendence)) attendence = _.head(vm.settingListItems());

      let params = {
        code: attendence.code,
        name: attendence.name,
      }

      vm.$window.storage(KWR004_B_INPUT, ko.toJS(params)).then(() => {
        vm.$window.modal('/view/kwr/004/b/index.xhtml').then(() => {
          //KWR004_B_OUTPUT
        });
      });
    }

    initialWorkStatusInformation() {
      let vm = this;

      //パラメータ.就業担当者であるか = true || false
      vm.isWorker(vm.$user.role.isInCharge.attendance);

      vm.$window.storage(WORK_STATUS).then((data: any) => {
        if (!_.isNil(data)) {
          vm.rdgSelectedId(data.itemSelection); //項目選択
          vm.standardSelectedCode(data.standardSelectedCode); //定型選択
          vm.freeSelectedCode(data.freeSelectedCode); //自由設定
          vm.zeroDisplayClassification(data.zeroDisplayClassification); //自由の選択済みコード
          vm.pageBreakSpecification(data.pageBreakSpecification); //改ページ指定
        }
      });
    }

    getSettingListItems() {
      let vm = this;

      let listItems: any = [
        new ItemModel('0001', '項目選択'),
        new ItemModel('0003', '定型選択'),
        new ItemModel('0004', '自由の選択済みコード'),
        new ItemModel('0005', '自由設定'),
        new ItemModel('0002', 'Seoul Korea'),
        new ItemModel('0006', 'Paris France'),
        new ItemModel('0007', '改ページ指定'),
        new ItemModel('0008', '就業担当者'),
        new ItemModel('0009', 'パラメータ'),
        new ItemModel('0010', '者であるか'),
      ];

      listItems = _.orderBy(listItems, 'code', 'asc');
      vm.settingListItems(listItems);
    }

    exportExcel() {

    }

    exportPdf() {

    }
  }

  //=================================================================
  export class ItemModel {
    code: string;
    name: string;
    constructor(code?: string, name?: string) {
      this.code = code;
      this.name = name;
    }
  }
}