// module nts.uk.com.view.cmf005.c.viewmodel {
//     import getText = nts.uk.resource.getText;
//     import close = nts.uk.ui.windows.close;
//     import setShared = nts.uk.ui.windows.setShared;
//     import getShared = nts.uk.ui.windows.getShared;
//     import model = cmf005.share.model;
//     import alertError = nts.uk.ui.dialog.alertError;

//     export class ScreenModel extends ko.ViewModel {

//         // swapList category
//         listCategory: KnockoutObservableArray<model.ItemCategory> = ko.observableArray([]);
//         listCategoryChosed: KnockoutObservableArray<model.ItemCategory> = ko.observableArray([]);
//         columns: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
//         currentCategorySelected: KnockoutObservableArray<any>;
//         listCateIdIgnore: KnockoutObservableArray<string> = ko.observableArray([]);


//         // comboBox system type
//         systemTypes: KnockoutObservableArray<any>;
//         selectedCode: KnockoutObservable<string>;
//         currentItem: KnockoutObservable<model.ItemModel>;
//         isEnable: KnockoutObservable<boolean>;
//         isEditable: KnockoutObservable<boolean>;
//         headerCodeCategories: string = getText("CMF005_19");
//         headerNameCategories: string = getText("CMF005_20");


//         constructor() {
//             var self = this;

//             // get param from screen B 
//             let listCategoryB = getShared('CMF005CParams_ListCategory');
//             let systemTypeB = getShared('CMF005CParams_SystemType');


//             self.currentCategorySelected = ko.observableArray([]);
//             var systemIdSelected;
//             self.systemTypes = ko.observableArray([]);
            
//             self.isEnable = ko.observable(true);
//             self.isEditable = ko.observable(true);
//             self.listCategory = ko.observableArray([]);
//             self.selectedCode = ko.observable('');

//             service.getSysTypes().done(function(data: Array<any>) {
//                     if (data && data.length) {
//                         _.forOwn(data, function(index) {
//                             self.systemTypes.push(new model.ItemModel(index.systemTypeValue+'', index.systemTypeName));
//                         });

//                         if (systemTypeB != undefined) { 
//                             systemIdSelected = systemTypeB.code; 
//                         } else { 
//                             systemIdSelected = self.systemTypes()[0].code; 
//                         }
//                         self.selectedCode(systemIdSelected);
//                     } else {

//                     }

//                 }).fail(function(error) {
//                     alertError(error);

//                 }).always(() => {

//                 });

//             self.isEnable = ko.observable(true);
//                 self.isEditable = ko.observable(true);
//                 self.listCategory = ko.observableArray([]);
//                 self.selectedCode.subscribe(value => {
//                     if (value && value.length > 0) {
//                     self.currentItem = _.find(self.systemTypes(), a => a.code === value);
//                         service.getConditionList(parseInt(self.selectedCode())).done(function(data: Array<any>) {
                            
//                             data = _.sortBy(data, ["categoryId"]);
//                             if (systemTypeB != undefined) {
//                                 _.forOwn(listCategoryB, function(index) {
//                                      _.remove(data, function (e) {
//                                             return e.categoryId == index.categoryId;
//                                         });
//                                 });
//                                 self.currentCategorySelected(listCategoryB);
//                            }
//                             self.listCategory(data);
//                             $("#swap-list-grid1 tr:first-child").focus();
                            
//                         }).fail(function(error) {
//                             alertError(error);
//                         }).always(() => {
//                             _.defer(() => {
//                                 $("#grd_Condition tr:first-child").focus();
//                             });
//                         });
//                     }
//                 });

//                 self.columns = ko.observableArray([
//                     { headerText: self.headerCodeCategories, key: 'categoryId', width: 70 },
//                     { headerText: self.headerNameCategories, key: 'categoryName', width: 250 }
//                 ]);
//                 self.listCategoryChosed = self.currentCategorySelected;
//         }

//         closePopup() {
//             close();
//         }

//         remove() {
//             let self = this;
//             self.listCategory.shift();
//         }

//         submit() {
//             let self = this;
//             if (self.currentCategorySelected().length == 0) {
//                 alertError({ messageId: "Msg_471" });
//             } else {
//                 setShared("CMF005COutput_ListCategoryChose", self.currentCategorySelected());
//                 setShared("CMF005COutput_SystemTypeChose", self.currentItem);
//                 close();
//             }
//         }

//     }
// }

module nts.uk.com.view.cmf005.c {
  import getText = nts.uk.resource.getText;
  import TextEditorOption = nts.uk.ui.option.TextEditorOption;
  import NtsGridListColumn = nts.uk.ui.NtsGridListColumn;

  @bean()
  export class ViewModel extends ko.ViewModel {
    screenMode: KnockoutObservable<number> = ko.observable();
    isNewMode: KnockoutObservable<boolean> = ko.computed(() => {
      const vm = this;
      return vm.screenMode() === ScreenMode.NEW;
    });

    //Pattern list
    patternList: KnockoutObservableArray<Pattern> = ko.observableArray([]);
    selectedPatternCode: KnockoutObservable<string> = ko.observable('');
    patternColumns: KnockoutObservableArray<any> = ko.observableArray([
      { headerText: getText('CMF005_19'), key: 'displayCode', width: 75 },
      { headerText: getText('CMF005_224'), key: 'patternName', width: 250 }
    ]);

    //Pattern
    codeValue: KnockoutObservable<string> = ko.observable('');
    nameValue: KnockoutObservable<string> = ko.observable('');

    options: TextEditorOption = new TextEditorOption({
      width: '150px'
    });

    //Category list
    categoriesDefault: KnockoutObservableArray<Category> = ko.observableArray([]);
    categoriesFiltered: KnockoutObservableArray<Category> = ko.observableArray([]);
    leftColumns: KnockoutObservableArray<NtsGridListColumn> = ko.observableArray([
      { headerText: getText('CMF005_19'), key: 'categoryId', width: 70 },
      { headerText: getText('CMF005_226'), key: 'categoryName', width: 250 }
    ]);
    rightColumns: KnockoutObservableArray<NtsGridListColumn> = ko.observableArray([
      { headerText: getText('CMF005_19'), key: 'categoryId', width: 70 },
      { headerText: getText('CMF005_226'), key: 'categoryName', width: 180 },
      { headerText: getText('CMF003_636'), key: 'retentionPeriod', width: 100 }
    ]);
    currentCateSelected: KnockoutObservableArray<Category> = ko.observableArray([]);
    systemTypes: KnockoutObservableArray<ItemModel> = ko.observableArray([
      new ItemModel(0, ' '),
    ]);
    selectedSystemType: KnockoutObservable<number> = ko.observable(0);

    //Auto execution
    saveFormatChecked: KnockoutObservable<boolean> = ko.observable(false);
    usePasswordChecked: KnockoutObservable<boolean> = ko.observable(false);
    targetYearDD: KnockoutObservableArray<ItemModel> = ko.observableArray([
      new ItemModel(0, '参照年'),
      new ItemModel(1, getText('CMF003_405')),
      new ItemModel(2, getText('CMF003_406')),
      new ItemModel(3, getText('CMF003_407'))
    ]);
    targetMonthDD: KnockoutObservableArray<ItemModel> = ko.observableArray([
      new ItemModel(0, '参照月'),
      new ItemModel(1, getText('CMF003_408')),
      new ItemModel(2, getText('CMF003_409')),
      new ItemModel(3, getText('CMF003_410')),
      new ItemModel(4, getText('CMF003_411')),
      new ItemModel(5, getText('CMF003_412')),
      new ItemModel(6, getText('CMF003_413')),
      new ItemModel(7, getText('CMF003_414')),
      new ItemModel(8, getText('CMF003_415')),
      new ItemModel(9, getText('CMF003_416')),
      new ItemModel(10, getText('CMF003_417')),
      new ItemModel(11, getText('CMF003_418')),
      new ItemModel(12, getText('CMF003_419'))
    ]);
    selectedDailyTargetYear: KnockoutObservable<string> = ko.observable('');
    selectedDailyTargetMonth: KnockoutObservable<string> = ko.observable('');
    selectedMonthlyTargetYear: KnockoutObservable<string> = ko.observable('');
    selectedMonthlyTargetMonth: KnockoutObservable<string> = ko.observable('');
    selectedAnnualTargetYear: KnockoutObservable<string> = ko.observable('');
    password: KnockoutObservable<string> = ko.observable('');
    confirmPassword: KnockoutObservable<string> = ko.observable('');
    isCheckboxActive: KnockoutObservable<boolean> = ko.observable(false);
    explanation: KnockoutObservable<string> = ko.observable('');

    mounted() {
      const vm = this;
      vm.usePasswordChecked.subscribe(value => {
        if (value) {
          $(".password-input").trigger("validate");
        } else {
          $('.password-input').ntsError('clear');
        }
      });

      vm.selectedSystemType.subscribe(value => {
        if (Number(value) !== 0) {
          vm.categoriesFiltered(_.filter(vm.categoriesDefault(), category => category.systemType === Number(value)));
        } else {
          vm.categoriesFiltered(vm.categoriesDefault());
        };
        vm.categoriesFiltered.valueHasMutated();
      });

      vm.usePasswordChecked.subscribe(value => {
        if (!value) {
          vm.password('');
          vm.confirmPassword('');
        }
      });

      vm.selectedPatternCode.subscribe(value => {
        const pattern = vm.getPatternById(value);
        if (pattern) {
          vm.selectPattern(pattern.code, pattern.patternClassification);
        }
      });

      vm.screenMode.subscribe(value => {
        if (Number(value) === ScreenMode.NEW) {
          vm.selectedPatternCode('');
          $("#C10_2 input").focus();
        } else {
          $("#C10_4 input").focus();
        }
      });

      vm.initDisplay();
    }

    private initDisplay() {
      const vm = this;
      vm.screenMode(ScreenMode.NEW);
      vm.$blockui("grayout");
      service.initDisplay().then((res) => {
        vm.checkInCharge(res.pic);
        _.map(res.patterns, (x: any) => {
          let p = new Pattern();
          p.code = x.patternCode;
          p.patternName = x.patternName;
          p.patternClassification = x.patternClassification;
          p.displayCode = x.patternClassification + x.patternCode;
          vm.patternList.push(p);
        });
        _.map(res.categories, (x :any) => {
          let c = vm.convertToCategory(x);
          vm.categoriesDefault.push(c);
          vm.categoriesFiltered.push(c);
        });
      }).always(() => {
        vm.$blockui("clear");
      });
    }

    public refreshNew() {
      const vm = this;
      vm.screenMode(ScreenMode.NEW);
      vm.selectedAnnualTargetYear(vm.getDefaultItem(vm.targetYearDD()).code);
      vm.selectedDailyTargetYear(vm.getDefaultItem(vm.targetYearDD()).code);
      vm.selectedMonthlyTargetYear(vm.getDefaultItem(vm.targetYearDD()).code);
      vm.selectedDailyTargetMonth(vm.getDefaultItem(vm.targetMonthDD()).code);
      vm.selectedMonthlyTargetMonth(vm.getDefaultItem(vm.targetMonthDD()).code);
      vm.codeValue('');
      vm.nameValue('');
      vm.selectedSystemType(0);
      vm.saveFormatChecked(false);
      vm.usePasswordChecked(false);
      vm.password('');
      vm.confirmPassword('');
      vm.explanation('');
      vm.categoriesFiltered(vm.categoriesDefault());
      vm.currentCateSelected([]);
      vm.$errors("clear");
    }

    public register() {
      const vm = this;
      if (vm.validateBeforeRegister()) {
        vm.$blockui("grayout");

        let param: any = {};
        param.screenMode = Number(vm.screenMode());
        param.patternCode = vm.codeValue();
        param.patternName = vm.nameValue();
        param.categoriesMaster = vm.currentCateSelected();
        param.idenSurveyArch = vm.saveFormatChecked();
        param.dailyReferYear = Number(vm.selectedDailyTargetYear());
        param.dailyReferMonth = Number(vm.selectedDailyTargetMonth());
        param.monthlyReferMonth = Number(vm.selectedMonthlyTargetMonth());
        param.monthlyReferYear = Number(vm.selectedMonthlyTargetYear());
        param.annualReferYear = Number(vm.selectedAnnualTargetYear());
        param.patternCompressionPwd = vm.password();
        param.withoutPassword = Boolean(vm.usePasswordChecked()) ? 1 : 0;
        param.patternSuppleExplanation = vm.explanation();

        service.addPattern(param).then(() => {
          vm.$dialog.info({ messageId: "Msg_15" });
          if (vm.screenMode() === ScreenMode.NEW) {
            let pattern: Pattern = new Pattern();
            pattern.code = param.patternCode;
            pattern.patternClassification = 0;
            pattern.patternName = param.patternName;
            pattern.displayCode = pattern.patternClassification + pattern.code;
            vm.patternList.push(pattern);
            vm.selectPattern(pattern.code, pattern.patternClassification);

            if (vm.selectedPatternCode() === '') {
              vm.selectedPatternCode(pattern.displayCode);
            }
          } else {
            _.find(vm.patternList(), { 'code': param.patternCode }).patternName = param.patternName;
            vm.patternList.valueHasMutated();
          }
        }).fail((err) => {
          vm.$errors("#C7_2 input", err);
        })
          .always(() => {
            vm.$blockui("clear");
          })
      }
    }

    public duplicate() {
      const vm = this;
      vm.screenMode(ScreenMode.NEW);
      vm.codeValue('');
      vm.nameValue('');
    }

    public deletePattern() {
      const vm = this;
      /**
       * 確認メッセージ（Msg_18）を表示する
       */
      vm.$dialog.confirm({ messageId: "Msg_18" }).then((result: 'no' | 'yes' | 'cancel') => {
        /**
         * 「いいえ」（ID：Msg_36）をクリック
         */
        if (result === 'no') {
          /**
           * 終了状態：削除処理をキャンセル
           */
          return;
        }
        /**
         * 「はい」（ID：Msg_35）をクリック
         */
        if (result === 'yes') {
          /**
           * 終了状態：削除処理を実行
           */
          let param: any = {};
          vm.$blockui("grayout");
          const pattern = vm.getPatternById(vm.selectedPatternCode());
          param.patternCode = pattern.code;
          param.patternClassification = pattern.patternClassification;

          service.deletePattern(param).then(() => {
            const index: number = vm.patternList().indexOf(pattern);
            if (index > -1) {
              vm.patternList().splice(index, 1);
              vm.patternList.valueHasMutated();
              vm.selectedPatternCode('');
              if (vm.patternList().length === 0) {
                vm.refreshNew();
              } else if (vm.patternList().length === index) {
                vm.selectPatternByIndex(index - 1);
              } else {
                vm.selectPatternByIndex(index);
              }
            }
            vm.$dialog.info({ messageId: "Msg_16" });
          }).always(() => {
            vm.$blockui("clear");
          });
        }
      });
    }

    public selectPattern(patternCode: string, patternClassification: number) {
      const vm = this;
      vm.$blockui("grayout");
      let param: any = {};
      param.patternCode = patternCode;
      param.patternClassification = patternClassification;
      param.categories = vm.categoriesDefault();
      service.selectPattern(param).then((res) => {
        const pattern: any = res.selectedCategories[0].pattern;
        vm.screenMode(ScreenMode.UPDATE);
        vm.codeValue(pattern.patternCode);
        vm.nameValue(pattern.patternName);
        vm.categoriesFiltered([]);
        _.forEach(res.selectableCategories, c => {
          let category = vm.convertToCategory(c);
          vm.categoriesFiltered().push(category);
        });
        vm.currentCateSelected([]);
        _.forEach(res.selectedCategories, c => {
          let category: Category = new Category();
          category.categoryId = c.categoryId;
          category.categoryName = c.categoryName;
          category.retentionPeriod = getText(c.retentionPeriod);
          category.systemType = c.systemType;
          vm.currentCateSelected.push(category);
        });
        vm.saveFormatChecked(pattern.idenSurveyArch === 1);
        vm.selectedDailyTargetMonth(vm.getReferValue(pattern.dailyReferMonth));
        vm.selectedDailyTargetYear(vm.getReferValue(pattern.dailyReferYear));
        vm.selectedMonthlyTargetMonth(vm.getReferValue(pattern.monthlyReferYear));
        vm.selectedMonthlyTargetYear(vm.getReferValue(pattern.monthlyReferMonth));
        vm.selectedAnnualTargetYear(vm.getReferValue(pattern.annualReferYear));
        vm.usePasswordChecked(pattern.withoutPassword === 1);
        vm.password(pattern.patternCompressionPwd);
        vm.confirmPassword(pattern.patternCompressionPwd);
        vm.explanation(pattern.patternSuppleExplanation);

        //revalidate
        vm.$errors("clear");
      }).always(() => {
        vm.$blockui("clear");
      });
    }

    private selectPatternByIndex(index: number) {
      const vm = this;
      const pattern: Pattern = vm.patternList()[index];
      if (pattern) {
        if (vm.selectedPatternCode() === '') {
          vm.selectedPatternCode(pattern.displayCode);
        }
        vm.selectPattern(pattern.code, pattern.patternClassification);
      }
    }

    private getDefaultItem(arr: ItemModel[]): ItemModel {
      return arr[0];
    }

    private getReferValue(value: any): string {
      if (value) {
        return String(value);
      }
      return '0';
    }

    private getPatternById(id: string): Pattern {
      const vm = this;
      return _.filter(vm.patternList(), p => p.code === id.substring(1)).pop();
    }

    private convertToCategory(c: any): Category {
      let category = new Category();
      category.categoryId = c.categoryId;
      category.categoryName = c.categoryName;
      category.retentionPeriod = getText(c.retentionPeriod);
      category.systemType = c.systemType;
      category.contractCode = c.contractCode;
      category.patternCode = c.patternCode;
      category.patternClassification = c.patternClassification;
      return category;
    }

    private checkInCharge(pic: LoginPersionInCharge) {
      const vm = this;
      if (pic.personnel)
        vm.systemTypes.push(new ItemModel(1, getText('CMF003_400')));
      if (pic.attendance)
        vm.systemTypes.push(new ItemModel(1, getText('CMF003_401')));
      if (pic.payroll)
        vm.systemTypes.push(new ItemModel(1, getText('CMF003_402')));
      if (pic.officeHelper)
        vm.systemTypes.push(new ItemModel(1, getText('CMF003_403')));
    }

    private validateBeforeRegister(): boolean {
      const vm = this;
      $("#C7_2 input").trigger("validate");
      $("#C7_4 input").trigger("validate");

      if (vm.usePasswordChecked()) {
        $("#C9_3 input").trigger("validate");
        $("#C9_6 input").trigger("validate");
        if (!nts.uk.ui.errors.hasError()) {
          if (vm.password() !== vm.confirmPassword()) {
            vm.$dialog.error({ messageId: 'Msg_566' });
            return false;
          }
        }
      }

      if (Number(vm.currentCateSelected().length) === 0) {
        vm.$dialog.error({ messageId: 'Msg_577' });
        return false;
      }

      if (nts.uk.ui.errors.hasError()) {
        return false;
      }
      return true;
    }
  }

  export class Pattern {
    code: string;
    patternName: string;
    patternClassification: number;
    displayCode?: string;
  }

  export class Category {
    categoryId: string;
    categoryName: string;
    retentionPeriod: string;
    systemType: number;
    patternCode?: string;
    patternClassification?: number;
    contractCode?: string;
  }

  export class ItemModel {
    code: string;
    name: string;

    constructor(code: number, name: string) {
      this.code = code.toString();
      this.name = name;
    }
  }

  export interface LoginPersionInCharge {
    attendance: boolean;
    employeeInfo: boolean;
    officeHelper: boolean;
    payroll: boolean;
    personnel: boolean;
  }

  export class ScreenMode {
    static NEW = 0;
    static UPDATE = 1;
  }
}