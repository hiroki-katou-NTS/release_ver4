/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />

module nts.uk.at.view.kwr004.b {

  const NUM_ROWS = 20;
  const KWR004_B_INPUT = 'KWR004_WORK_STATUS_DATA';
  const KWR004_B_OUTPUT = 'KWR004_WORK_STATUS_RETURN';
  const KWR004_C_INPUT = 'KWR004_C_DATA';
  const KWR004_C_OUTPUT = 'KWR004_C_RETURN';

  @bean()
  class ViewModel extends ko.ViewModel {

    settingListItems: KnockoutObservableArray<ItemModel> = ko.observableArray([]);
    columns: KnockoutObservableArray<any>;
    currentCode: KnockoutObservable<any>;
    currentCodeList: KnockoutObservableArray<any> = ko.observableArray([]);
    currentSettingCodeList: KnockoutObservableArray<any>;
    settingRules: KnockoutObservableArray<any>;
    attendance: KnockoutObservable<any> = ko.observable(null);
    attendanceCode: KnockoutObservable<string> = ko.observable(null);
    attendanceName: KnockoutObservable<string> = ko.observable(null);
    settingRuleCode: KnockoutObservable<number> = ko.observable(0);
    settingListItemsDetails: KnockoutObservableArray<SettingForPrint> = ko.observableArray([]);
    model: KnockoutObservable<Model> = ko.observable(new Model());

    isSelectAll: KnockoutObservable<boolean> = ko.observable(false);
    isEnableAddButton: KnockoutObservable<boolean> = ko.observable(false);
    isEnableAttendanceCode: KnockoutObservable<boolean> = ko.observable(false);
    isEnableDeleteButton: KnockoutObservable<boolean> = ko.observable(false);
    isEnableDuplicateButton: KnockoutObservable<boolean> = ko.observable(false);

    //KDL 047, 048
    shareParam = new SharedParams();

    position: KnockoutObservable<number> = ko.observable(1);
    attendanceItemName: KnockoutObservable<string> = ko.observable('勤務種類');
    //attendanceCode: KnockoutObservable<string> = ko.observable('02');
    //attendanceName: KnockoutObservable<string> = ko.observable('出勤簿');
    columnIndex: KnockoutObservable<number> = ko.observable(1);
    isDisplayItemName: KnockoutObservable<boolean> = ko.observable(true);
    isDisplayTitle: KnockoutObservable<boolean> = ko.observable(true);
    isEnableComboBox: KnockoutObservable<number> = ko.observable(2);
    isEnableTextEditor: KnockoutObservable<number> = ko.observable(2);
    comboSelected: KnockoutObservable<any> = ko.observable(null);
    tableSelected: KnockoutObservable<any> = ko.observable(null);

    isNewMode: KnockoutObservable<boolean> = ko.observable(false);

    constructor(params: any) {
      super();

      const vm = this;

      vm.getSettingList();

      vm.currentCodeList.subscribe((code: any) => {
        nts.uk.ui.errors.clearAll();
        vm.getSettingListForPrint(code);
      });

      vm.getSettingListItemsDetails();
      vm.currentSettingCodeList = ko.observableArray([]);

      vm.settingRules = ko.observableArray([
        { code: 0, name: vm.$i18n('KWR004_68') },
        { code: 1, name: vm.$i18n('KWR004_69') }
      ]);

      vm.settingListItemsDetails.subscribe((newList) => {
        if (!newList || newList.length <= 0) {
          vm.isSelectAll(false);
          return;
        }
        //Check if all the values in the settingListItemsDetails array are true:
        let isSelectedAll: any = vm.settingListItemsDetails().every(item => item.isChecked() === true);
        //there is least one item which is not checked
        if (isSelectedAll === false) isSelectedAll = null;
        vm.isSelectAll(isSelectedAll);
      });

      // subscribe isSelectAll
      vm.isSelectAll.subscribe(newValue => {
        vm.selectAllChange(newValue);
      });

      //KDL 047, 048      
      vm.shareParam.titleLine.displayFlag = vm.isDisplayTitle();
      vm.shareParam.titleLine.layoutCode = vm.attendanceCode();
      vm.shareParam.titleLine.layoutName = vm.attendanceName();

      const positionText = vm.position() === 1 ? "上" : "下";
      vm.shareParam.titleLine.directText = vm.$i18n('KWR002_131') + vm.columnIndex() + vm.$i18n('KWR002_132') + positionText + vm.$i18n('KWR002_133');
      vm.shareParam.itemNameLine.displayFlag = vm.isDisplayItemName();
      vm.shareParam.itemNameLine.displayInputCategory = vm.isEnableTextEditor();
      vm.shareParam.itemNameLine.name = vm.attendanceItemName();
      vm.shareParam.attribute.selectionCategory = vm.isEnableComboBox();
      vm.shareParam.attribute.selected = vm.comboSelected();
      vm.shareParam.selectedTime = vm.tableSelected();

      vm.shareParam.attribute.attributeList = [
        new AttendaceType(1, vm.$i18n('KWR002_141')),
        new AttendaceType(2, vm.$i18n('KWR002_142')),
        new AttendaceType(3, vm.$i18n('KWR002_143')),
        /* new AttendaceType(4, vm.$i18n('KWR002_180')),
        new AttendaceType(5, vm.$i18n('KWR002_181')),
        new AttendaceType(6, vm.$i18n('KWR002_182')),
        new AttendaceType(7, vm.$i18n('KWR002_183')) */
      ]

      vm.shareParam.attendanceItems = vm.getDiligenceProject();
      vm.shareParam.diligenceProjectList = vm.getDiligenceProject();
    }

    created(params: any) {
      const vm = this;
    }

    mounted() {
      const vm = this;
      /* if (!!navigator.userAgent.match(/Trident.*rv\:11\./))
        $("#multiGridList").ntsFixedTable({ height: 486 });
      else
        $("#multiGridList").ntsFixedTable({ height: 488 }); */

      $("#multiGridList").ntsFixedTable({ height: 365 });
      $('#KWR004_B33').focus();
    }

    addRowItem(newRow?: SettingForPrint) {
      let vm = this,
        row: SettingForPrint = newRow;

      if (!newRow) {
        let lastItem: any = _.last(vm.settingListItemsDetails());
        let id = lastItem ? lastItem.id : 1;
        row = new SettingForPrint(id, null, 0, null, false);
      }

      row.isChecked.subscribe((value: boolean) => {
        vm.settingListItemsDetails.valueHasMutated();
      });

      vm.settingListItemsDetails.push(row);
    }

    addNewRow() {
      const vm = this;
      //vm.addRowItem();
      vm.creatDefaultSettingDetails();

      vm.isEnableDuplicateButton(false);
      vm.isEnableDeleteButton(false);

      vm.attendanceCode(null);
      vm.attendanceName(null);
      vm.isEnableAttendanceCode(true);
      vm.isNewMode(true);
      $('#KWR004_B32').focus();
    }

    registerSetting() {
      const vm = this; 
      /* 
      if (vm.isNewMode()) {
        //コードが重複しているため、登録できません。 Msg_1753        
        let checkExist = _.find(vm.settingListItems(), ['code', _.trim(vm.attendanceCode())]);        
        if( !_.isNil(checkExist) ) {
          vm.$dialog.error({messageId: 'Msg_1753'}).then(() => {
            $('#KWR003_B42').focus();           
          });
          return;
        }
      } else {
        //出力項目が削除されています。 ＃Msg_1903
        let temp = vm.settingListItemsDetails();
        temp=  _.filter(temp, (x) => x.id !== 1);
        if( temp.length !== vm.settingListItemsDetails().length ) {
          vm.$dialog.error({messageId: 'Msg_1903'}).then(() => {
            $('#btnB11').focus();
          });
          return;
        }
      } */
      //order
      let twoItemList = _.dropRight(vm.settingListItemsDetails(), vm.settingListItemsDetails().length - 2);
      twoItemList = vm.orderListItemsByField(twoItemList);

      let eightItemList = _.drop(vm.settingListItemsDetails(), 2);
      eightItemList = vm.orderListItemsByField(eightItemList);
      //register
      let listItemsDetails = _.concat(twoItemList, eightItemList);
      vm.createListItemAfterSorted(listItemsDetails);

      let returnAttendance: AttendanceItem = {
        code: vm.attendanceCode(),
        name: vm.attendanceName(),
        status: vm.isNewMode() ? 1 : 0 // 0: Update, 1: Addnew, 2: Remove
      };
      vm.attendance(returnAttendance);

      //change to update status
      vm.isNewMode(false);
    }

    deteleSetting() {
      const vm = this;
      //get all items that will be remove
      let listCheckedItems: Array<any> = vm.settingListItemsDetails().filter((row) => row.isChecked() === true);
      if (listCheckedItems.length <= 0) return;

      //get all items that will be not remove
      let listNotCheckedItems: Array<any> = vm.settingListItemsDetails().filter((row) => row.isChecked() === false);
      vm.settingListItemsDetails(listNotCheckedItems);
    }

    /**
     * Duplicate Setting
     * */

    showDialogC() {
      const vm = this;
      let lastItem = _.last(vm.settingListItems());

      let params = {
        code: vm.attendanceCode(), //複製元の設定ID
        name: vm.attendanceName(),
        lastCode: !_.isNil(lastItem) ? lastItem.code : null,
        settingListItems: vm.settingListItemsDetails() //設定区分
      }
      console.log(params);
      vm.$window.storage(KWR004_C_INPUT, params).then(() => {
        vm.$window.modal('/view/kwr/004/c/index.xhtml').then(() => {
          vm.$window.storage(KWR004_C_OUTPUT).then((data) => {
            console.log(data);
            if (_.isNil(data)) {
              return;
            }

            /* let duplicateItem = _.find(vm.settingListItems(), (x) => x.code === data.code);
            if (!_.isNil(duplicateItem)) {
              vm.$dialog.error({ messageId: 'Msg_1903' }).then(() => { });
              return;
            }

            vm.settingListItems.push(data);
            vm.currentCodeList(data.code);    */         
          });
        });
      });
    }

    closeDialog() {
      const vm = this;
      //KWR004_B_OUTPUT
      vm.$window.storage(KWR004_B_OUTPUT, vm.attendance());
      vm.$window.close();
    }

    getSettingListItemsDetails() {
      const vm = this;

      //vm.creatDefaultSettingDetails();
      for (let i = 0; i < NUM_ROWS; i++) {
        let setting = i < 2 ? 0 : null;
        let newIitem = new SettingForPrint(i + 1, '予定勤務種類', setting, '予定勤務種類', false);
        vm.addRowItem(newIitem);
      }
      _.orderBy(vm.settingListItemsDetails(), ['id', 'name'], ['asc', 'asc']);
    }

    /**
     *
    */
    creatDefaultSettingDetails() {
      const vm = this;
      //clear
      vm.settingListItemsDetails([])
      for (let i = 0; i < NUM_ROWS; i++) {
        let setting = i < 2 ? 0 : 9999;
        let newIitem = new SettingForPrint(i + 1, null, setting, null, false);
        vm.addRowItem(newIitem);
      }
    }

    createDataSelection(selectedTimeList: Array<any>) {
      let vm = this,
        dataSelection: string = '',
        selectionItem: Array<string> = [];

      _.forEach(selectedTimeList, (item, index: number) => {
        if (index === 0 && item.operator.substring(0, 1) === '+') {
          selectionItem.push(item.name);
        } else {
          selectionItem.push(item.operator + ' ' + item.name);
        }
      });

      if (selectionItem.length > 0) {
        dataSelection = _.join(selectionItem, ' ');
        if (dataSelection.length > 20) {
          dataSelection = dataSelection.substring(0, 19) + vm.$i18n('KWR003_219');
        }
      }

      return dataSelection;
    }

    getSettingListForPrint(code: string) {
      const vm = this;
      if (!_.isNil(code)) {
        let selectedObj = _.find(vm.settingListItems(), (x: any) => x.code === code);
        if (!_.isNil(selectedObj)) {
          vm.attendanceCode(selectedObj.code);
          vm.attendanceName(selectedObj.name);
          //KDL 047, 048
          vm.shareParam.titleLine.layoutCode = vm.attendanceCode();
          vm.shareParam.titleLine.layoutName = vm.attendanceName();

          vm.isEnableAttendanceCode(false);
          vm.isEnableAddButton(true);
          vm.isEnableDeleteButton(true);
          vm.isEnableDuplicateButton(true);
        }
      }
    }

    getListItems() {
      let lisItems: Array<any> = [
        new ItemModel('001', '予定勤務種類'),
        new ItemModel('003', '予定勤務種類'),
        new ItemModel('004', '予定勤務種類'),
        new ItemModel('005', '予定勤務種類'),
        new ItemModel('002', 'Seoul Korea'),
        new ItemModel('006', 'Paris France'),
        new ItemModel('007', '予定勤務種類'),
        new ItemModel('008', '予定勤務種類'),
        new ItemModel('009', '予定勤務種類'),
        new ItemModel('010', '予定勤務種類'),
        new ItemModel('011', '予定勤務種類'),
        new ItemModel('013', '予定勤務種類'),
        new ItemModel('014', '予定勤務種類'),
        new ItemModel('015', '予定勤務種類'),
        new ItemModel('012', 'Seoul Korea'),
        new ItemModel('016', 'Paris France'),
        new ItemModel('017', '予定勤務種類'),
        new ItemModel('018', '予定勤務種類'),
        new ItemModel('019', '予定勤務種類'),
        new ItemModel('020', '予定勤務種類'),
      ];

      return lisItems;

    }

    getSettingList() {
      const vm = this;

      let lisItems: Array<any> = vm.getListItems();

      //sort by code with asc
      lisItems = _.orderBy(lisItems, ['code'], ['asc']);
      vm.settingListItems(lisItems);

      vm.$window.storage(KWR004_B_INPUT).then((data: any) => {
        let code = !_.isNil(data) ? data.code : null;
        if (vm.settingListItems().length > 0) {
          let firstItem: any = _.head(vm.settingListItems());
          if (!code) code = firstItem.code;
        }

        vm.currentCodeList.push(code);
        vm.getSettingListForPrint(code);
      });
    }

    openDialogKDL(data: SettingForPrint) {
      const vm = this;

      if (data.setting() === 1 || _.isNull(data.setting()))
        vm.openDialogKDL048(data);
      else
        vm.openDialogKDL047(data);
    }

    openDialogKDL047(row: any) {
      const vm = this;

      vm.shareParam.itemNameLine.name = row.name();

      nts.uk.ui.windows.setShared('attendanceItem', vm.shareParam, true);
      nts.uk.ui.windows.sub.modal('/view/kdl/047/a/index.xhtml').onClosed(() => {
        const attendanceItem = nts.uk.ui.windows.getShared('attendanceRecordExport');
        if (_.isNil(attendanceItem)) {
          return;
        }

        let index = _.findIndex(vm.settingListItemsDetails(), (o: any) => { return o.id === row.id; });
        vm.settingListItemsDetails()[index].name(attendanceItem.attendanceItemName);

        let findAttedenceName = _.find(vm.shareParam.attendanceItems, (x: any) => { return x.attendanceItemId === parseInt(attendanceItem.attendanceId); });
        if (!_.isNil(findAttedenceName)) {
          vm.settingListItemsDetails()[index].selectionItem(findAttedenceName.attendanceItemName);

          let listItem: selectedTimeList = {};
          listItem.itemId = attendanceItem.attendanceId;
          listItem.name = findAttedenceName.attendanceItemName;
          vm.settingListItemsDetails()[index].selectedTimeList.push(listItem);
        } else {
          vm.settingListItemsDetails()[index].selectionItem(null);
          vm.settingListItemsDetails()[index].selectedTimeList([]);
        }
      });
    }

    openDialogKDL048(row: any) {
      let vm = this,
        selectionItem: Array<string> = [];

      vm.shareParam.attribute.attributeList = [
        new AttendaceType(4, vm.$i18n('KWR002_180')),
        new AttendaceType(5, vm.$i18n('KWR002_181')),
        //new AttendaceType(6, vm.$i18n('KWR002_182')),
        new AttendaceType(7, vm.$i18n('KWR002_183'))
      ]

      vm.shareParam.itemNameLine.name = row.name();

      nts.uk.ui.windows.setShared('attendanceItem', vm.shareParam, true);
      nts.uk.ui.windows.sub.modal('/view/kdl/048/index.xhtml').onClosed(() => {
        const attendanceItem = nts.uk.ui.windows.getShared('attendanceRecordExport');
        console.log(attendanceItem);
        if (!attendanceItem) {
          return;
        }

        if (attendanceItem.selectedTimeList.length > 0) {
          let index = _.findIndex(vm.settingListItemsDetails(), (o: any) => { return o.id === row.id; });
          let dataSelection: string = vm.createDataSelection(attendanceItem.selectedTimeList);
          if (index > -1) {
            vm.settingListItemsDetails()[index].name(attendanceItem.itemNameLine.name);
            vm.settingListItemsDetails()[index].selectionItem(dataSelection);
            vm.settingListItemsDetails()[index].selectedTimeList(attendanceItem.selectedTimeList);
          }
        }
      });
    }

    checkItem(data: SettingForPrint) {
      console.log(data);
      return true
    }

    selectAllChange(newValue: boolean) {
      const vm = this;

      if (newValue === null) return;

      _.forEach(vm.settingListItemsDetails(), (row, index) => {
        row.isChecked(newValue);
      })
    }

    getDiligenceProject() {
      let DiligenceProjects = [
        new DiligenceProject(1, '予定勤務種類', '', 0),
        new DiligenceProject(28, '勤務種類', '勤務種類', 28),
        new DiligenceProject(2, '予定就業時間帯', '予定就業時間帯', 2),
        new DiligenceProject(3, '予定出勤時刻1', '予定出勤時刻1', 3),
        new DiligenceProject(5, '予定出勤時刻5', '予定出勤時刻5', 5),
        new DiligenceProject(6, '予定出勤時刻6', '予定出勤時刻6', 6),
        new DiligenceProject(8, '予定出勤時刻8', '予定出勤時刻8', 8),
        new DiligenceProject(9, '予定出勤時刻9', '予定出勤時刻9', 9),
        new DiligenceProject(10, '予定出勤時刻10', '予定出勤時刻10', 10),
        new DiligenceProject(11, '予定出勤時111', '予定出勤時刻11', 11),
        new DiligenceProject(12, '予定出勤時刻12', '予定出勤時刻12', 12),
        new DiligenceProject(13, '予定出勤時刻13', '予定出勤時刻13', 13),
        new DiligenceProject(14, '予定出勤時刻14', '予定出勤時刻14', 14),
        new DiligenceProject(15, '予定出勤時刻15', '予定出勤時刻15', 15),
        new DiligenceProject(16, '予定出勤時刻16', '予定出勤時刻16', 16),
        new DiligenceProject(4, '予定退勤時刻1', '予定退勤時刻1', 4),
        new DiligenceProject(7, '予定休憩開始時刻1', '予定休憩開始時刻1', 7),
        new DiligenceProject(8, '予定休憩終了時刻1', '予定休憩終了時刻1', 8),
        new DiligenceProject(27, '予定時間', '予定時間', 27),
        new DiligenceProject(216, '残業１', '残業１', 216),
        new DiligenceProject(461, '勤務回数', '勤務回数', 461),
        new DiligenceProject(534, '休憩回数', '休憩回数', 534),
        new DiligenceProject(641, 'aaaaaaaaa回数', 'aaaaaaaaa回数', 641),
        new DiligenceProject(642, 'tukijikan回数', 'tukijikan回数', 642),
        new DiligenceProject(643, 'tukikin', 'tukikin', 643),
        new DiligenceProject(644, '出有ｵﾝ無ｵﾌ有ｶｳﾝﾄ（日次ﾄﾘｶﾞ）ｄ', '出有ｵﾝ無ｵﾌ有ｶｳﾝﾄ（日次ﾄﾘｶﾞ）ｄ', 644),
        new DiligenceProject(645, '出有ｵﾝ有ｵﾌ無ｶｳﾝﾄ（日次ﾄﾘｶﾞ）（bb）', '出有ｵﾝ有ｵﾌ無ｶｳﾝﾄ（日次ﾄﾘｶﾞ）（bb）', 645),
        new DiligenceProject(680, '任意項目４０', '任意項目４０', 680),
        new DiligenceProject(681, '任意項目４１', '任意項目４１', 681),
        new DiligenceProject(682, '任意項目４２月別', '任意項目４２月別', 682),
        new DiligenceProject(683, '任意項目４３', '任意項目４３', 683),
        new DiligenceProject(267, '振替休日１', '振替休日１', 267),
        new DiligenceProject(268, '計算休日出勤１', '計算休日出勤１', 268),
        new DiligenceProject(269, '計算振替休日１', '計算振替休日１', 269),
        new DiligenceProject(270, '事前休日出勤１', '事前休日出勤１', 270)
      ];

      return DiligenceProjects;
    }

    /**
     * Orders list items by field
     * @param [listItemsDetails] 
     * @param [field] 
     * @param [sort_type] 
     * @returns  
     */
    orderListItemsByField(listItemsDetails?: Array<any>, field: string = 'name', sort_type: string = 'desc') {
      let newListItemsDetails: Array<any> = [];
      _.forEach(listItemsDetails, (row, index) => {
        let temp = {
          id: row.id,
          isChecked: row.isChecked(),
          name: row.name(),
          setting: row.setting(),
          selectionItem: row.selectionItem(),
          selectedTimeList: row.selectedTimeList()
        };

        newListItemsDetails.push(temp);
      });

      newListItemsDetails = _.orderBy(newListItemsDetails, [field], [sort_type]);

      return newListItemsDetails;
    }

    /**
     * Create list item after sorted
     * @param [listItemsDetails] 
     */
    createListItemAfterSorted(listItemsDetails?: Array<any>) {
      let vm = this;

      vm.settingListItemsDetails([]);
      _.forEach(listItemsDetails, (x: any) => {
        let newIitem: SettingForPrint = new SettingForPrint(
          x.id, x.name, x.setting,
          x.selectionItem, x.isChecked,
          x.selectedTimeList);
        vm.addRowItem(newIitem);
      });
    }
  }

  //=================================================================
  export interface AttendanceItem {
    code?: string;
    name?: string;
    status?: number;
  }

  export class ItemModel {
    code: string;
    name: string;
    constructor(code?: string, name?: string) {
      this.code = code;
      this.name = name;
    }
  }

  export class SettingForPrint {
    id: number;
    isChecked: KnockoutObservable<boolean> = ko.observable(false);
    name: KnockoutObservable<string> = ko.observable(null);
    setting: KnockoutObservable<number> = ko.observable(0);
    selectionItem: KnockoutObservable<string> = ko.observable(null);
    selectedCode: KnockoutObservable<number> = ko.observable(0);
    selectedTimeList: KnockoutObservableArray<selectedTimeList> = ko.observableArray([]);

    constructor(
      id?: number,
      name?: string,
      setting?: number,
      selectionItem?: string,
      checked?: boolean,
      selectedCode?: number,
      selectedTimeList?: Array<any>
    ) {
      this.name(name || '');
      this.setting(setting);
      this.isChecked(checked || false);
      this.selectionItem(selectionItem || '');
      this.id = id;
      this.selectedCode(selectedCode);
      this.selectedTimeList(selectedTimeList || []);
    }
  }

  export interface selectedTimeList {
    itemId?: number;
    operator?: string;
    name?: string;
    indicatesNumber?: number
  }

  export class Model {
    code: string;
    name: string;
    settingForPrint: Array<SettingForPrint>;
    constructor(code?: string, name?: string, settings?: Array<SettingForPrint>) {
      this.code = code;
      this.name = name;
      this.settingForPrint = settings;
    }
  }

  //KDL 047, 048
  // Display object mock
  export class SharedParams {
    // タイトル行
    titleLine: TitleLineObject = new TitleLineObject();
    // 項目名行
    itemNameLine: ItemNameLineObject = new ItemNameLineObject();
    // 属性
    attribute: AttributeObject = new AttributeObject();
    // List<勤怠項目>KDL 048
    diligenceProjectList: DiligenceProject[] = [];
    // List<勤怠項目> KDL 047
    attendanceItems: DiligenceProject[] = [];
    // List<選択済み勤怠項目>
    selectedTimeList: SelectedTimeListParam[] = [];
    // 選択済み勤怠項目ID
    selectedTime: number;
  }
  export class SelectedTimeListParam {
    // 項目ID
    itemId: any | null = null;
    // 演算子
    operator: String | null = null;

    constructor(itemId: any, operator: String) {
      this.itemId = itemId;
      this.operator = operator;
    }
  }

  export class TitleLineObject {
    // 表示フラグ
    displayFlag: boolean = false;
    // 出力項目コード
    layoutCode: String | null = null;
    // 出力項目名
    layoutName: String | null = null;
    // コメント
    directText: String | null = null;
  }

  export class ItemNameLineObject {
    // 表示フラグ
    displayFlag: boolean = false;
    // 表示入力区分
    displayInputCategory: number = 1;
    // 名称
    name: String | null = null;
  }

  export class AttributeObject {
    // 選択区分
    selectionCategory: number = 2;
    // List<属性>
    attributeList: AttendaceType[] = [];
    // 選択済み
    selected: number = 1;
  }

  export class AttendaceType {
    attendanceTypeCode: number;
    attendanceTypeName: string;
    constructor(attendanceTypeCode: number, attendanceTypeName: string) {
      this.attendanceTypeCode = attendanceTypeCode;
      this.attendanceTypeName = attendanceTypeName;
    }
  }

  export class DiligenceProject {
    attendanceItemId: any;
    attendanceItemName: any;
    attributes: any;
    displayNumbers: any;
    //48
    // ID
    id: any;
    // 名称
    name: any;
    // 属性
    //attributes: any;
    // 表示番号
    indicatesNumber: any;
    constructor(id: any, name: any, attributes: any, indicatesNumber: any) {
      this.attendanceItemId = id;
      this.attendanceItemName = name;
      this.attributes = attributes;
      this.displayNumbers = indicatesNumber;
      //48
      this.id = id;
      this.name = name;
      //this.attributes = attributes;
      this.indicatesNumber = indicatesNumber;
    }
  }
}