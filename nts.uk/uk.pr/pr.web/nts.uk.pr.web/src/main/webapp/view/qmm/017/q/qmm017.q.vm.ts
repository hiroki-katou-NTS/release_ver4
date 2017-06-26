module nts.uk.pr.view.qmm017.q {
    export module viewmodel {
        export class ScreenModel {
            items: KnockoutObservableArray<ItemModel>;
            personalUPItems: KnockoutObservableArray<ItemModel>;
            systemVariableItems: KnockoutObservableArray<ItemModel>;
            columns: KnockoutObservableArray<any>;
            currentCodeList: KnockoutObservableArray<any>;
            formulaContent: KnockoutObservable<string>;
            itemsBag: any;
            result: KnockoutObservable<string>;

            constructor(data) {
                var self = this;
                self.items = ko.observableArray([]);
                self.personalUPItems = ko.observableArray([]);
                self.systemVariableItems = ko.observableArray([]);
                self.currentCodeList = ko.observableArray([]);
                self.formulaContent = ko.observable(data.formulaContent);
                if (self.formulaContent().indexOf('計算式＠') !== -1) {
                    self.extractOtherFormulaContents(data.itemsBag);
                } else {
                    self.replaceCodesToNames(data.itemsBag);
                    self.buildListItemModel(data.itemsBag);
                    self.bindGridListItem();
                    self.replaceSystemVariableToValue();
                }
                self.itemsBag = data.itemsBag;
                self.result = ko.observable('');
            }

            isDuplicated(itemName) {
                var self = this;
                let foundItem = _.find(self.items(), function(item) {
                    return item.name == itemName;
                });
                return foundItem !== undefined;
            }

            replaceCodesToNames(itemsBag) {
                var self = this;
                let replacedContent = self.formulaContent();
                _.forEach(itemsBag, function(item) {
                    if (replacedContent.indexOf(item.code) !== -1) {
                        replacedContent = replacedContent.replace(item.code, item.name);
                    }
                });
                self.formulaContent(replacedContent);
            }

            replaceSystemVariableToValue() {
                var self = this;
                let replacedContent = self.formulaContent();
                _.forEach(self.systemVariableItems(), function(item) {
                    if (replacedContent.indexOf(item.code) !== -1) {
                        replacedContent = replacedContent.replace(item.code, item.value);
                    }
                });
                self.formulaContent(replacedContent);
            }

            extractOtherFormulaContents(itemsBag) {
                var self = this;
                let replacedValue = self.formulaContent();
                _.forEach(itemsBag, function(item) {
                    if (item.name.indexOf('計算式＠') !== -1 && self.formulaContent().indexOf(item.name) !== -1) {
                        let itemCodeSplited = item.code.split('＠');
                        service.findLastestFormulaManual(itemCodeSplited[1])
                            .done(function(formulaManual: model.FormulaManualDto) {
                                replacedValue = replacedValue.replace(new RegExp(item.name, 'g'), "（" + formulaManual.formulaContent + "）");
                                self.formulaContent(replacedValue);
                                self.replaceCodesToNames(itemsBag);
                                self.buildListItemModel(itemsBag);
                                self.bindGridListItem();
                                self.replaceSystemVariableToValue();
                            })
                            .fail(function() {
                                replacedValue = replacedValue.replace(new RegExp(item.name, 'g'), "（ ）");
                                self.formulaContent(replacedValue);
                                self.replaceCodesToNames(itemsBag);
                                self.buildListItemModel(itemsBag);
                                self.bindGridListItem();
                                self.replaceSystemVariableToValue();
                            });
                    }
                });
            }

            buildListItemModel(itemsBag) {
                var self = this;
                self.personalUPItems([]);
                self.systemVariableItems([]);
                self.items([]);
                _.forEach(itemsBag, function(item) {
                    if (item.name.indexOf('関数') === -1 && self.formulaContent().indexOf(item.name) !== -1 && !self.isDuplicated(item.name)) {
                        if (item.name.indexOf('個人単価＠') !== -1) {
                            self.personalUPItems.push(new ItemModel(item.name, 0));
                        } else if (item.name.indexOf('変数＠') !== -1) {
                            self.systemVariableItems.push(new ItemModel(item.name, item.value));
                        } else {
                            self.items.push(new ItemModel(item.name, 0));
                        }
                    }
                });
            }

            bindGridListItem() {
                var self = this;
                $("#lstItemValue").igGrid({
                    primaryKey: "code",
                    columns: [
                        { headerText: "計算式の項目名", key: "code", dataType: "string", width: '150px' },
                        { headerText: "値", key: "value", dataType: "number", width: '150px', format: '0.00' },
                    ],
                    dataSource: self.items(),
                    height: "200px",
                    width: "330px",
                    features: [
                        {
                            name: "Updating",
                            enableAddRow: false,
                            editMode: "row",
                            enableDeleteRow: false,
                            columnSettings: [
                                { columnKey: "code", editorOptions: { type: "string", disabled: true } }
                            ],
                            editCellEnding: function(evt, ui) {
                                var foundItem = _.find(self.items(), function(item) { return item.code == ui.rowID });
                                (foundItem.code !== ui.value) ? foundItem.value = ui.value : foundItem.code = ui.value;
                            }
                        }]
                });
                $("[aria-describedby='lstItemValue_code']").css({ "backgroundColor": "#CFF1A5" });
            }

            replaceFunctionNameToCode(targetContent) {
                var self = this;
                let replaceValue = targetContent;
                _.forEach(self.itemsBag, function(item) {
                    if (replaceValue.indexOf(item.name) !== -1) {
                        replaceValue = replaceValue.replace(new RegExp(item.name, 'g'), item.code);
                    }
                });
                return replaceValue;
            }

            replaceJPCharToEN(targetContent) {
                let lstSpecialChar = [
                    { jp: '＠', en: '@' },
                    { jp: '×', en: '*' },
                    { jp: '÷', en: '/' },
                    { jp: '＾', en: '^' },
                    { jp: '（', en: '(' },
                    { jp: '）', en: ')' },
                    { jp: '＜', en: '<' },
                    { jp: '＞', en: '>' },
                    { jp: '≦', en: '<=' },
                    { jp: '≧', en: '>=' },
                    { jp: '≠', en: '!=' }
                ];
                let replaceValue = targetContent;
                _.forEach(lstSpecialChar, function(char) {
                    if (replaceValue.indexOf(char.jp) !== -1) {
                        replaceValue = replaceValue.replace(new RegExp(char.jp, 'g'), char.en);
                    }
                });
                return replaceValue;
            }

            calculationTrial() {
                var self = this;
                let replacedValue = self.formulaContent();
                _.forEach(self.items(), function(item) {
                    replacedValue = replacedValue.replace(new RegExp(item.code, 'g'), item.value);
                });
                _.forEach(self.personalUPItems(), function(item) {
                    replacedValue = replacedValue.replace(new RegExp(item.code, 'g'), item.value);
                });
                replacedValue = replacedValue.replace(new RegExp(' ', 'g'), '');
                replacedValue = self.replaceFunctionNameToCode(replacedValue);
                replacedValue = self.replaceJPCharToEN(replacedValue);
                self.result('...');
                service.trialCalculate(replacedValue).done(function(calculatorDto: model.CalculatorDto) {
                    self.result(calculatorDto.result);
                });
            }

            close() {
                nts.uk.ui.windows.close();
            }

        }
    }

    class ItemModel {
        code: string;
        value: number;
        constructor(code: string, value: number) {
            this.code = code;
            this.value = value;
        }
    }
}