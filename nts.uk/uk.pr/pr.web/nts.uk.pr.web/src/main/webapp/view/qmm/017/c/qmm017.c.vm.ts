module nts.qmm017 {

    export class CScreen {
        itemsTab: KnockoutObservableArray<any>;
        formulaCode: KnockoutObservable<string>;
        formulaName: KnockoutObservable<string>;
        selectedDifficultyAtr: KnockoutObservable<any>;
        selectedConditionAtr: KnockoutObservable<any>;
        selectedTabCSel006: KnockoutObservable<string>;
        easyFormulaName: KnockoutObservable<string>;
        useMasterName: KnockoutObservable<string>;
        useMasterCode: KnockoutObservable<string>;
        historyId: KnockoutObservable<string>;

        //formula easy
        noneConditionalEasyFormula: KnockoutObservable<EasyFormula>;
        defaultEasyFormula: KnockoutObservable<EasyFormula>;
        monthlyEasyFormula: KnockoutObservable<EasyFormula>;
        dailyMonthlyEasyFormula: KnockoutObservable<EasyFormula>;
        dailyEasyFormula: KnockoutObservable<EasyFormula>;
        hourlyEasyFormula: KnockoutObservable<EasyFormula>;

        //formula manual
        formulaManualContent: KnockoutObservable<TextEditor>;
        comboBoxReferenceMonthAtr: KnockoutObservable<ComboBox>;
        comboBoxRoudingMethod: KnockoutObservable<ComboBox>;
        comboBoxRoudingPosition: KnockoutObservable<ComboBox>;


        constructor(data) {
            var self = this;
            self.selectedDifficultyAtr = ko.observable(data.viewModel017b().selectedDifficultyAtr());
            self.selectedConditionAtr = ko.observable(data.viewModel017b().selectedConditionAtr());
            self.formulaCode = ko.observable('');
            self.formulaName = ko.observable('');
            data.viewModel017b().selectedDifficultyAtr.subscribe(function(val) {
                self.selectedDifficultyAtr(val);
            });
            data.viewModel017b().selectedConditionAtr.subscribe(function(val) {
                self.selectedConditionAtr(val);
            });
            data.viewModel017b().formulaCode.subscribe(function(val) {
                self.formulaCode(val);
            });
            data.viewModel017b().formulaName.subscribe(function(val) {
                self.formulaName(val);
            });

            self.useMasterCode = ko.observable(data.viewModel017b().comboBoxUseMaster().selectedCode());
            self.useMasterName = ko.observable('');
            data.viewModel017b().comboBoxUseMaster().selectedCode.subscribe(function(codeChange) {
                let useMasterFound = _.find(data.viewModel017b().comboBoxUseMaster().itemList(), (item) => {
                    return item.code == codeChange;
                });
                self.useMasterCode(data.viewModel017b().comboBoxUseMaster().selectedCode());
                if (useMasterFound) {
                    self.useMasterName(useMasterFound.name);
                }
            });
            var lstReferenceMonthAtr = [
                { code: 0, name: '当月' },
                { code: 1, name: '1ヶ月前' },
                { code: 2, name: '2ヶ月前' },
                { code: 3, name: '3ヶ月前' },
                { code: 4, name: '4ヶ月前' },
                { code: 5, name: '5ヶ月前' },
                { code: 6, name: '6ヶ月前' },
                { code: 7, name: '7ヶ月前' },
                { code: 8, name: '8ヶ月前' },
                { code: 9, name: '9ヶ月前' },
                { code: 10, name: '10ヶ月前' },
                { code: 11, name: '11ヶ月前' },
                { code: 12, name: '12ヶ月前' },
            ];
            var lstRoudingMethod = [
                { code: 0, name: '切上げ' },
                { code: 1, name: '切捨て' },
                { code: 2, name: '一捨二入' },
                { code: 3, name: '二捨三入' },
                { code: 4, name: '三捨四入' },
                { code: 5, name: '四捨五入' },
                { code: 6, name: '五捨六入' },
                { code: 7, name: '六捨七入' },
                { code: 8, name: '七捨八入' },
                { code: 9, name: '八捨九入' },
            ];

            var lstRoudingPostion = [
                { code: 0, name: '1円丸め' },
                { code: 1, name: '10円丸め' },
                { code: 2, name: '100円丸め' },
                { code: 3, name: '1000円丸め' }
            ]

            self.formulaManualContent = ko.observable(new TextEditor());
            self.itemsTab = ko.observableArray([
                { id: 'tab-1', title: '明細・勤怠', content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true) },
                { id: 'tab-2', title: '単価', content: '.tab-content-2', enable: ko.observable(true), visible: ko.observable(true) },
                { id: 'tab-3', title: '関数', content: '.tab-content-3', enable: ko.observable(true), visible: ko.observable(true) },
                { id: 'tab-4', title: 'システム変数', content: '.tab-content-4', enable: ko.observable(true), visible: ko.observable(true) },
                { id: 'tab-5', title: '個人情報', content: '.tab-content-5', enable: ko.observable(false), visible: ko.observable(true) },
                { id: 'tab-6', title: '計算式', content: '.tab-content-6', enable: ko.observable(true), visible: ko.observable(true) },
                { id: 'tab-7', title: '賃金テーブル', content: '.tab-content-7', style: 'margin-left: 50px;', enable: ko.observable(true), visible: ko.observable(true) }
            ]);
            self.comboBoxReferenceMonthAtr = ko.observable(new ComboBox(lstReferenceMonthAtr, true, false));
            self.comboBoxRoudingMethod = ko.observable(new ComboBox(lstRoudingMethod, true, false));
            self.comboBoxRoudingPosition = ko.observable(new ComboBox(lstRoudingPostion, true, false));
            self.selectedTabCSel006 = ko.observable('tab-1');

            self.noneConditionalEasyFormula = ko.observable(new EasyFormula(0, data.viewModel017b));
            self.defaultEasyFormula = ko.observable(new EasyFormula(0, data.viewModel017b));
            self.monthlyEasyFormula = ko.observable(new EasyFormula(1, data.viewModel017b));
            self.dailyMonthlyEasyFormula = ko.observable(new EasyFormula(1, data.viewModel017b));
            self.dailyEasyFormula = ko.observable(new EasyFormula(1, data.viewModel017b));
            self.hourlyEasyFormula = ko.observable(new EasyFormula(1, data.viewModel017b));
        }

        undo() {
            document.execCommand("undo", false, null);
        }

        redo() {
            document.execCommand("redo", false, null);
        }

        validateTextArea() {
            var self = this;
            self.formulaManualContent().testError();
        }

        setAllFixValued() {
            var self = this;
            self.monthlyEasyFormula().selectedRuleCodeEasySettings('0');
            self.dailyMonthlyEasyFormula().selectedRuleCodeEasySettings('0');
            self.dailyEasyFormula().selectedRuleCodeEasySettings('0');
            self.hourlyEasyFormula().selectedRuleCodeEasySettings('0');
        }

        setAllDetail() {
            var self = this;
            self.monthlyEasyFormula().selectedRuleCodeEasySettings('1');
            self.dailyMonthlyEasyFormula().selectedRuleCodeEasySettings('1');
            self.dailyEasyFormula().selectedRuleCodeEasySettings('1');
            self.hourlyEasyFormula().selectedRuleCodeEasySettings('1');
        }

        pasteValue(targetString) {
            var self = this;
            let currentTextArea = self.formulaManualContent().textArea();
            self.formulaManualContent().textArea(self.formulaManualContent().insertString(currentTextArea, targetString, $("#input-text")[0].selectionStart));
        }
    }

    export class EasyFormula {
        roundingRulesEasySettings: KnockoutObservableArray<any>;
        selectedRuleCodeEasySettings: KnockoutObservable<any>;
        easyFormulaFixMoney: KnockoutObservable<number>;
        easyFormulaDetail: KnockoutObservable<nts.qmm017.model.FormulaEasyDetailDto>;
        easyFormulaName: KnockoutObservable<string>;
        easyFormulaCode: KnockoutObservable<string>;
        formulaCode: KnockoutObservable<string>;
        startYm: KnockoutObservable<any>;
        historyId: KnockoutObservable<string>;
        constructor(mode: number, root) {
            var self = this;
            self.startYm = ko.observable(root().startYearMonth());
            root().startYearMonth.subscribe(function(yearMonth) {
                self.startYm(yearMonth);
            });
            if (mode == 0) {
                self.roundingRulesEasySettings = ko.observableArray([
                    { code: '0', name: '固定値' },
                    { code: '1', name: '計算式' }
                ]);
                self.selectedRuleCodeEasySettings = ko.observable('0');
            } else if (mode == 1) {
                self.roundingRulesEasySettings = ko.observableArray([
                    { code: '0', name: '固定値' },
                    { code: '1', name: '計算式' },
                    { code: '2', name: '既定計算式' }
                ]);
                self.selectedRuleCodeEasySettings = ko.observable('2');
            }
            self.easyFormulaFixMoney = ko.observable(0);
            self.easyFormulaDetail = ko.observable(new nts.qmm017.model.FormulaEasyDetailDto());
            self.easyFormulaName = ko.observable('');
            self.easyFormulaCode = ko.observable('');
            self.formulaCode = ko.observable(root().formulaCode());
            root().formulaCode.subscribe(function(formulaCode) {
                self.formulaCode(formulaCode);
            });
            self.historyId = ko.observable(root().historyId());
            root().historyId.subscribe(function(historyId) {
                self.historyId(historyId);
            });
        }

        openDialogL() {
            var self = this;
            let param = {
                isUpdate: (self.easyFormulaName() !== '' && self.easyFormulaName() !== null),
                dirtyData: self.easyFormulaDetail(),
                startYm: self.startYm()
            };
            nts.uk.ui.windows.setShared('paramFromScreenC', param);
            nts.uk.ui.windows.sub.modal('/view/qmm/017/l/index.xhtml', { title: 'かんたん計算式の登録', width: 650, height: 750 }).onClosed(() => {
                if (nts.uk.ui.windows.getShared('easyFormulaDetail')) {
                    self.easyFormulaDetail(nts.uk.ui.windows.getShared('easyFormulaDetail'));
                    self.easyFormulaName(self.easyFormulaDetail().easyFormulaName);
                }
            });

        }
    }

    export class ComboBox {
        itemList: KnockoutObservableArray<any>;
        itemName: KnockoutObservable<string>;
        currentCode: KnockoutObservable<number>
        selectedCode: KnockoutObservable<any>;
        isEnable: KnockoutObservable<boolean>;
        isEditable: KnockoutObservable<boolean>;
        constructor(data, isEnable, isEditable) {
            var self = this;
            self.itemList = ko.observableArray(data);
            self.itemName = ko.observable('');
            self.currentCode = ko.observable(3);
            self.selectedCode = ko.observable(0);
            self.isEnable = ko.observable(isEnable);
            self.isEditable = ko.observable(isEditable);
        }
    }

    function showError(event) {
        var self = this;
        var currentString = $("#input-text").val();
        var selectValue = $(self).attr("message");
        if (selectValue !== undefined) {
            $("#error-message").text(selectValue);
            var position = $("#input-containner").position();
            $("#error-containner").css({
                "top": (event.data.pageY - position.top - 93) + "px",
                "left": (event.data.pageX - position.left - 398) + "px"
            });
            $("#error-containner").show();
        }
    }

    export class ItemModelTextEditor {
        code: KnockoutObservable<string>;
        name: KnockoutObservable<string>;
        description: KnockoutObservable<string>;
        text: KnockoutObservable<string>;
        constructor(code: string, name: string, description: string) {
            this.code = ko.observable(code);
            this.name = ko.observable(name);
            this.description = ko.observable(description);
            this.text = ko.computed(function() {
                return this.code() + "  " + this.name();
            }, this).extend({ deferred: true });
        }
    }

    export class TextEditor {
        //list error messsage
        ERROR_BRACKET = "カッコ()の数に誤りがあります。";
        ERROR_CONSECUTIVELY = "構文に誤りがあります。{0}と{1}が連続して入力されています。";
        ERROR_MUST_CONTAIN_ATSIGN = "「{0}」は利用できない文字列です。";
        ERROR_BEFORE_ATSIGN = "「{0}＠」は利用できない文字列です。";
        ERROR_DIVIDE_ZERO = "計算式中に「÷0」となる部分が含まれています。";
        ERROR_EMPTY_INPUT = "計算式が入力されていません。";
        ERROR_NESTED_MORE_THAN_10 = "関数を10回を超えて入れ子にすることはできません。";
        ERROR_DIGITS_AFTER_DECIMAL = "「{0}」は小数点以下の桁数が多すぎます。 計算式で利用できる小数は小数点以下5桁までです。";

        ERROR_TOO_MUCH_PARAM = "「{0}」の引数が多く指定されています。";
        ERROR_NOT_ENOUGH_PARAM = "「{0}」の引数が不足しています。";
        ERROR_AFTER_ATSIGN = "「{0}」は利用できない文字列です。";
        ERROR_PARAM_TYPE = "「{0}」の第{1}引数の型が不正です。";

        listSpecialChar = ["+", "-", "×", "÷", "＾", "（", "）", "＜", "＞", "≦", "≧", "＝", "≠"];
        listOperatorChar = ["+", "-", "×", "÷", "＾"];
        listComparator = ["＜", "＞", "≦", "≧", "＝", "≠"];

        autoComplete: KnockoutObservableArray<any>;
        textArea: KnockoutObservable<string>;
        contentValue: KnockoutObservable<any>;
        autoSelected: KnockoutObservable<any>;
        showAutoComplete: KnockoutObservable<boolean>;
        row: KnockoutObservable<number>;
        col: KnockoutObservable<number>;
        index: KnockoutObservable<number>;
        error: KnockoutObservable<string>;
        lstError: KnockoutObservableArray<string>;
        itemsBag: any;

        constructor() {
            var self = this;
            //----------------------------------------------------------------------------
            self.autoComplete = ko.observableArray([
                new ItemModelTextEditor('001', '基本給', "description 1"),
                new ItemModelTextEditor('150', '役職手当', "description 2"),
                new ItemModelTextEditor('ABC', '基12本ghj給', "description 3"),
                new ItemModelTextEditor('002', '基本給', "description 4"),
                new ItemModelTextEditor('153', '役職手当', "description 5"),
                new ItemModelTextEditor('AB4', '基12本ghj給', "description 6"),
                new ItemModelTextEditor('003', '基本給', "description 7"),
                new ItemModelTextEditor('155', '役職手当', "description 8"),
                new ItemModelTextEditor('AB5', '基12本ghj給', "description 9")
            ]);
            self.itemsBag = [];
            self.lstError = ko.observableArray([]);
            self.showAutoComplete = ko.observable(false);
            self.autoSelected = ko.observable("");
            self.row = ko.observable(1);
            self.col = ko.observable(1);
            self.index = ko.observable(1);
            self.error = ko.observable("");
            self.textArea = ko.observable("");
            self.autoSelected.subscribe(function(value) {
                if (value) {
                    var currentString = $("#input-text").val();
                    var index = self.index() + 1;
                    var selectValue = value.name();
                    var inserted = self.insertString(currentString, selectValue, index);
                    self.textArea(inserted);
                    $("#input-text").focus();
                    $("#input-text")[0].selectionStart = index + selectValue.length;
                    $("#input-text")[0].selectionEnd = index + selectValue.length;
                    self.testError();
                    $("#auto-complete-containner").hide();
                    self.autoSelected(undefined);
                }
            }, self);
            self.contentValue = ko.observable("");
            $("#error-containner").hide();
            $(document).on("keyup", "#input-text", (event) => {
                if (!event.shiftKey && event.keyCode === 16 && event.key === "@") {
                    return;
                }
                $("#error-containner").hide();
                var start = $("#input-text")[0].selectionStart;
                var end = $("#input-text")[0].selectionEnd;
                var maxWidthCharacter = 15;
                if (((event.shiftKey && event.keyCode === 50) || event.keyCode === 192) && event.key === "@") {
                    var currentRow = self.getCurrentPosition(end);
                    self.index(end);
                    $("#auto-complete-containner").show();
                    $("#auto-complete-containner").css({ "top": (currentRow.top + 17) + "px", "left": (currentRow.left + 15) + "px" });
                } else {
                    $("#auto-complete-containner").hide();
                }
            });
            $(document).on("mouseleave", "#error-containner", (event) => {
                $("#error-containner").hide();
            });

            $(document).on("click", "#input-area", (event) => {
                $("#error-containner").hide();
                self.observeError($(".error-char"));
            });

        }

        observeError(subjectTags) {
            var currentClickedTag = _.findLast(subjectTags, function(tag) {
                var x = $(tag).offset();
                return x.top <= event.pageY && x.left <= event.pageX
                    && (x.left + $(tag).outerWidth()) >= event.pageX
                    && (x.top + $(tag).outerHeight()) >= event.pageY;
            });
            if (currentClickedTag) {
                $(currentClickedTag).click({ pageX: event.pageX, pageY: event.pageY }, showError);
                $(currentClickedTag).click();
            }
        }

        validateBracket(bracketTags) {
            var self = this;
            if (bracketTags.length > 0) {
                let openBracket = _.remove(bracketTags, function(n) {
                    return $(n).html() === "\（";
                });

                let closeBracket = _.remove(bracketTags, function(n) {
                    return $(n).html() === "\）";
                });

                if (closeBracket.length === 0) {
                    self.markError($(openBracket), self.ERROR_BRACKET, []);
                } else if (openBracket.length === 0) {
                    self.markError($(closeBracket), self.ERROR_BRACKET, []);
                } else {
                    var openError = [];
                    for (var i = openBracket.length - 1; i >= 0; i--) {
                        var currentOpen = openBracket[i];
                        var id = parseInt($(currentOpen).attr("id").split("-")[1]);
                        var currentClose = _.find(closeBracket, function(a) {
                            return parseInt($(a).attr("id").split("-")[1]) > id;
                        });
                        if (currentClose === undefined) {
                            openError.unshift(currentOpen);
                        } else {
                            closeBracket.splice(closeBracket.indexOf(currentClose), 1);
                        }
                    }
                    self.markError($(openError), self.ERROR_BRACKET, []);
                    self.markError($(closeBracket), self.ERROR_BRACKET, []);
                }
            }
        }

        validateConsecutively(specialChar) {
            var self = this;

            var singleSpecial = {
                "+": "+",
                "-": "-",
                "＾": "＾",
                "×": "×",
                "＝": "＝",
                "≠": "≠",
                "÷": "÷",
                "＜": "＜",
                "＞": "＞",
                "≦": "≦",
                "≧": "≧",
                "＠": "＠"
            };

            for (let i = 0; i < specialChar.length; i++) {
                var $data = $(specialChar[i]);
                var charCount = parseInt($data.attr("id").split("-")[1]);
                var char = $data.text();
                var single = singleSpecial[char];
                if (single !== undefined) {
                    var neighborCount = self.countNeighbor(charCount, specialChar, false, true);
                    if (neighborCount > 0) {
                        self.markError($data, self.ERROR_CONSECUTIVELY, [specialChar[i].innerText, specialChar[i - 1].innerText]);
                    }
                }
            }
        }

        validateContainAtSign(tagsJapaneseChar) {
            var self = this;
            for (let tagOrder = 0; tagOrder < tagsJapaneseChar.length; tagOrder++) {
                if (tagsJapaneseChar[tagOrder].innerText.indexOf('＠') === -1) {
                    let contentToChars = tagsJapaneseChar[tagOrder].innerText.split('');
                    if (contentToChars[0] !== '”' || contentToChars[contentToChars.length - 1] !== '”') {
                        self.markError($(tagsJapaneseChar[tagOrder]), self.ERROR_MUST_CONTAIN_ATSIGN, [tagsJapaneseChar[tagOrder].innerText]);
                    }
                }
            }
        }

        validateBeforeAtSign(tagsJapaneseChar) {
            var self = this;
            let lstSyntaxBeforeAtSign = ["支給", "控除", "勤怠", "会社単価", "個人単価", "関数", "変数", "個人", "計算式", "賃金TBL"];
            for (let tagOrder = 0; tagOrder < tagsJapaneseChar.length; tagOrder++) {
                if (tagsJapaneseChar[tagOrder].innerText.indexOf('＠') !== -1) {
                    let splitByAtSign = tagsJapaneseChar[tagOrder].innerText.split('＠');
                    if (!self.checkEqualInArray(splitByAtSign[0].trim(), lstSyntaxBeforeAtSign)) {
                        self.markError($(tagsJapaneseChar[tagOrder]), self.ERROR_BEFORE_ATSIGN, [splitByAtSign[0]]);
                    }
                }
            }
        }

        validateAfterAtSign(tagsJapaneseChar) {
            var self = this;
            for (let tagOrder = 0; tagOrder < tagsJapaneseChar.length; tagOrder++) {
                if (tagsJapaneseChar[tagOrder].innerText.indexOf('＠') !== -1) {
                    let splitByAtSign = tagsJapaneseChar[tagOrder].innerText.split('＠');
                    let correctItemName = _.find(self.itemsBag, function(item) { return item.name == tagsJapaneseChar[tagOrder].innerText.trim() });
                    if (!correctItemName) {
                        self.markError($(tagsJapaneseChar[tagOrder]), self.ERROR_AFTER_ATSIGN, [splitByAtSign[1]]);
                    }
                }
            }
        }

        validateDivideZero(tagsSpecialChar) {
            var self = this;
            for (let tagOrder = 0; tagOrder < tagsSpecialChar.length; tagOrder++) {
                if (tagsSpecialChar[tagOrder].innerText === '÷') {
                    let nextTag = $(tagsSpecialChar[tagOrder]).next();
                    if (nextTag[0]) {
                        let contentNextTag = nextTag[0].innerText;
                        if (contentNextTag.trim() === '0') {
                            self.markError($(tagsSpecialChar[tagOrder]), self.ERROR_DIVIDE_ZERO, []);
                        }
                    }

                }
            }

        }

        validateEmptyInput() {
            var self = this;
            let contentInput = $("#input-text").val();
            if (!contentInput || contentInput.trim() === '') {
                let html = "<span class='editor-line'><span id='span-1' class='error-char' message='" + self.ERROR_EMPTY_INPUT + "'>  </span></span>";
                self.contentValue(html);
            }
        }

        validateNestedMoreThan10(tagsSpecialChar) {
            var self = this;
            var countChar = 0;
            for (let tagOrder = 0; tagOrder < tagsSpecialChar.length; tagOrder++) {
                if (tagsSpecialChar[tagOrder].innerText === '（') {
                    countChar++;
                    if (countChar > 10) {
                        self.markError($(tagsSpecialChar[tagOrder]), self.ERROR_NESTED_MORE_THAN_10, []);
                    }
                } else {
                    countChar = 0;
                }
            }
        }

        validateDigitsAfterDecimal(tagsUnknownChar) {
            var self = this;
            for (let tagOrder = 0; tagOrder < tagsUnknownChar.length; tagOrder++) {
                if (tagsUnknownChar[tagOrder].innerText >= '0' && tagsUnknownChar[tagOrder].innerText <= '9' && tagsUnknownChar[tagOrder].innerText.indexOf('.') !== -1) {
                    let splitContentTag = tagsUnknownChar[tagOrder].innerText.split('.');
                    if (splitContentTag[1].length > 5) {
                        self.markError($(tagsUnknownChar[tagOrder]), self.ERROR_DIGITS_AFTER_DECIMAL, [tagsUnknownChar[tagOrder].innerText]);
                    }
                }
            }
        }

        isContainsComparator(content): boolean {
            var self = this;
            for (let i = 0; i < self.listComparator.length; i++) {
                if (content.indexOf(self.listComparator[i]) !== -1) {
                    return true;
                }
            };
            return false;
        }

        isBooleanType(content) {
            var self = this;
            return (content.indexOf('関数＠かつ') !== -1 || content.indexOf('関数＠または') !== -1 || self.isContainsComparator(content));
        }

        isYearMonthType(content) {
            return (content.indexOf('関数＠月加算') !== -1 || (content.length === 6 && nts.uk.ntsNumber.isNumber("123.222", false)));
        }

        // return 2 if too much param
        // return 1 if not enough param
        // return 0 if OK
        validateNumberOfParam(treeObject: any) {
            let functionName = treeObject.value.trim();
            let numberOfParam = treeObject.children.length;
            if (functionName.indexOf("関数＠条件式") !== -1) {
                if (numberOfParam === 3) return 1;
                else if (numberOfParam > 3) return 2;
                else if (numberOfParam < 3) return 0;
            } else if (functionName.indexOf("関数＠かつ") !== -1) {
                if (numberOfParam === 2) return 1;
                else if (numberOfParam > 2) return 1;
                else if (numberOfParam < 2) return 0;
            } else if (functionName.indexOf("関数＠または") !== -1) {
                if (numberOfParam === 2) return 1;
                else if (numberOfParam > 2) return 1;
                else if (numberOfParam < 2) return 0;
            } else if (functionName.indexOf("関数＠四捨五入") !== -1) {
                if (numberOfParam === 1) return 1;
                else if (numberOfParam > 1) return 2;
                else if (numberOfParam < 1) return 0;
            } else if (functionName.indexOf("関数＠切捨て") !== -1) {
                if (numberOfParam === 1) return 1;
                else if (numberOfParam > 1) return 2;
                else if (numberOfParam < 1) return 0;
            } else if (functionName.indexOf("関数＠切上げ") !== -1) {
                if (numberOfParam === 1) return 1;
                else if (numberOfParam > 1) return 2;
                else if (numberOfParam < 1) return 0;
            } else if (functionName.indexOf("関数＠最大値") !== -1) {
                if (numberOfParam === 2) return 1;
                else if (numberOfParam > 2) return 1;
                else if (numberOfParam < 2) return 0;
            } else if (functionName.indexOf("関数＠最小値") !== -1) {
                if (numberOfParam === 2) return 1;
                else if (numberOfParam > 2) return 1;
                else if (numberOfParam < 2) return 0;
            } else if (functionName.indexOf("関数＠家族人数") !== -1) {
                if (numberOfParam === 2) return 1;
                else if (numberOfParam > 2) return 2;
                else if (numberOfParam < 2) return 0;
            } else if (functionName.indexOf("関数＠月加算") !== -1) {
                if (numberOfParam === 2) return 1;
                else if (numberOfParam > 2) return 2;
                else if (numberOfParam < 2) return 0;
            } else if (functionName.indexOf("関数＠年抽出") !== -1) {
                if (numberOfParam === 1) return 1;
                else if (numberOfParam > 1) return 2;
                else if (numberOfParam < 1) return 0;
            } else if (functionName.indexOf("関数＠月抽出") !== -1) {
                if (numberOfParam === 1) return 1;
                else if (numberOfParam > 1) return 2;
                else if (numberOfParam < 1) return 0;
            }
            return 1;
        }

        validateTypeOfParams(treeObject: any) {
            var self = this;
            let functionName = treeObject.value.trim();
            let param = treeObject.children;
            if (functionName.indexOf("関数＠条件式") !== -1 && param.length == 3) {
                if (!self.isBooleanType(param[0].value.trim()) || param[0].value.trim() === '') {
                    return 1;
                } else if (self.isBooleanType(param[1].value.trim()) || param[0].value.trim() === '') {
                    return 2;
                } else if (self.isBooleanType(param[2].value.trim()) || param[0].value.trim() === '') {
                    return 3;
                } else {
                    return 0;
                }
            } else if (functionName.indexOf("関数＠かつ") !== -1 && param.length >= 2) {
                for (let i = 1; i <= param.length; i++) {
                    if (!self.isBooleanType(param[i - 1].value.trim()) || param[0].value.trim() === '') {
                        return i;
                    }
                }
                return 0;
            } else if (functionName.indexOf("関数＠または") !== -1 && param.length >= 2) {
                for (let i = 1; i <= param.length; i++) {
                    if (!self.isBooleanType(param[i - 1].value.trim()) || param[0].value.trim() === '') {
                        return i;
                    }
                }
                return 0;
            } else if (functionName.indexOf("関数＠四捨五入") !== -1 && param.length == 1) {
                if (self.isBooleanType(param[0].value.trim()) || param[0].value.trim() === '') {
                    return 1;
                } else {
                    return 0;
                }
            } else if (functionName.indexOf("関数＠切捨て") !== -1 && param.length == 1) {
                if (self.isBooleanType(param[0].value.trim()) || param[0].value.trim() === '') {
                    return 1;
                } else {
                    return 0;
                }
            } else if (functionName.indexOf("関数＠切上げ") !== -1 && param.length == 1) {
                if (self.isBooleanType(param[0].value.trim()) || param[0].value.trim() === '') {
                    return 1;
                } else {
                    return 0;
                }
            } else if (functionName.indexOf("関数＠最大値") !== -1 && param.length >= 2) {
                for (let i = 1; i <= param.length; i++) {
                    if (self.isBooleanType(param[i - 1].value.trim()) || param[0].value.trim() === '') {
                        return i;
                    }
                }
                return 0;
            } else if (functionName.indexOf("関数＠最小値") !== -1 && param.length >= 2) {
                for (let i = 1; i <= param.length; i++) {
                    if (self.isBooleanType(param[i - 1].value.trim()) || param[0].value.trim() === '') {
                        return i;
                    }
                }
                return 0;
            } else if (functionName.indexOf("関数＠家族人数") !== -1 && param.length == 2) {
                if (self.isBooleanType(param[0].value.trim()) || param[0].value.trim() === '') {
                    return 1;
                } else if (self.isBooleanType(param[1].value.trim()) || param[0].value.trim() === '') {
                    return 2;
                } else return 0;
            } else if (functionName.indexOf("関数＠月加算") !== -1 && param.length == 2) {
                if (self.isYearMonthType(param[0].value.trim()) || param[0].value.trim() === '') {
                    return 1;
                } else if (self.isBooleanType(param[1].value.trim()) || param[0].value.trim() === '') {
                    return 2;
                } else return 0;
            } else if (functionName.indexOf("関数＠年抽出") !== -1 && param.length == 1) {
                if (self.isYearMonthType(param[0].value.trim()) || param[0].value.trim() === '') {
                    return 1;
                } else return 0;
            } else if (functionName.indexOf("関数＠月抽出") !== -1 && param.length == 1) {
                if (self.isYearMonthType(param[0].value.trim()) || param[0].value.trim() === '') {
                    return 1;
                } else return 0;
            }
        }

        validateFunction(contentFunction) {
            var self = this;
            var inputContent = [];
            let splitedContent = contentFunction.split('');
            let startIndex = 0;
            let endIndex = 0;
            let openCount = 0;
            let closeCount = 0;
            let lstFunctionString = [];
            for (let position = 0; position < splitedContent.length; position++) {
                if (splitedContent[position] === '（') {
                    openCount += 1;
                } else if (splitedContent[position] === '）') {
                    closeCount += 1;
                }
                if (openCount > 0 && (closeCount - openCount) == 0) {
                    endIndex = position;
                    lstFunctionString.push(contentFunction.slice(startIndex, endIndex + 1));
                    startIndex = endIndex + 1;
                    openCount = 0;
                    closeCount = 0;
                }
            }
            _.forEach(lstFunctionString, function(functionString) {
                self.validateContentFunction(functionString);
            });
        }

        validateContentFunction(contentFunction) {
            var self = this;
            let treeFunction = nts.uk.util.createTreeFromString(contentFunction, "（", "）", ",", []);
            self.validateTreeFunction(treeFunction[0]);
        }

        validateTreeFunction(treeObject) {
            var self = this;
            let params = treeObject.children;
            if (params.length > 0) {
                if (self.validateNumberOfParam(treeObject) === 1 && self.validateTypeOfParams(treeObject) === 0) {
                    for (let i = 0; i < params.length; i++) {
                        self.validateTreeFunction(params[i]);
                    }
                } else if (self.validateNumberOfParam(treeObject) === 2) {
                    self.markErrorTreeObject(treeObject, self.ERROR_TOO_MUCH_PARAM, []);
                } else if (self.validateNumberOfParam(treeObject) === 0) {
                    self.markErrorTreeObject(treeObject, self.ERROR_NOT_ENOUGH_PARAM, []);
                } else if (self.validateTypeOfParams(treeObject) !== 0) {
                    self.markErrorTreeObject(treeObject, self.ERROR_PARAM_TYPE, [self.validateTypeOfParams(treeObject)]);
                }
            }
        }

        markErrorTreeObject(treeObject, message, param) {
            var self = this;
            let indexTree = treeObject.index;
            let specialCharTags = $(".special-char");
            var countOpenBrackets = 0;
            for (let orderTag = 0; orderTag < specialCharTags.length; orderTag++) {
                if (specialCharTags[orderTag].innerText === '（') {
                    countOpenBrackets += 1;
                }
                //if found the bracket of the function
                if (countOpenBrackets === indexTree) {
                    let functionNameTag = specialCharTags[orderTag].previousSibling;
                    self.markError($(functionNameTag), message, [functionNameTag.innerText].concat(param));
                    return true;
                }
            }
            return true;
        }

        checkEqualInArray(target: string, array: Array<string>) {
            for (let count = 0; count < array.length; count++) {
                if (target === array[count]) {
                    return true;
                }
            }
            return false;
        }

        checkContainsInArray(target: string, array: Array<string>) {
            for (let count = 0; count < array.length; count++) {
                if (target.indexOf(array[count]) !== -1) {
                    return true;
                }
            }
            return false;
        }

        markError(tag, message: string, param: Array<string>) {
            var self = this;
            var errorContent = message;
            if (tag) {
                if (param && param.length > 0) {
                    for (let paramOrder = 0; paramOrder < param.length; paramOrder++) {
                        errorContent = errorContent.replace("{" + paramOrder + "}", param[paramOrder]);
                    }
                }
                tag.addClass("error-char").attr("message", errorContent);
                if (tag.length > 0) {
                    self.lstError.push(errorContent);
                }
            }
        }

        insertString(original, sub, position) {
            if (original.length === position) {
                return original + sub;
            }
            return original.substr(0, position) + sub + original.substr(position);
        }

        testError() {
            var self = this;
            var value = $("#input-text").val();
            var count = 1;
            var toChar = value.split('');
            var html = "<span class='editor-line'>";
            for (var i = 0; i < toChar.length; i++) {
                if (toChar[i] === "\n") {
                    html += "</span>";
                    html += "<span class='editor-line'>";
                } else {
                    if (toChar[i] === "@") {
                        html += "<span id='span-" + count + "' class='element-content autocomplete-char'>" + toChar[i] + "</span>";
                        count++;
                    } else if (self.checkEqualInArray(toChar[i], self.listSpecialChar)) {
                        html += "<span id='span-" + count + "' class='element-content special-char'>" + toChar[i] + "</span>";
                        count++;
                    } else if (self.checkJapanese(toChar[i])) {
                        if (toChar[i - 1] === undefined || toChar[i - 1] === "\n") {
                            html += "<span id='span-" + count + "' class='element-content japanese-character'>" + toChar[i] + "</span>";
                            count++;
                        } else if (self.checkJapanese(toChar[i - 1]) && !self.checkEqualInArray(toChar[i - 1], self.listSpecialChar)) {
                            html = self.insertString(html, toChar[i], html.length - 7);
                        } else {
                            html += "<span id='span-" + count + "' class='element-content japanese-character'>" + toChar[i] + "</span>";
                            count++;
                        }
                    } else if (self.checkAlphaOrEmpty(toChar[i])) {
                        if (toChar[i - 1] === undefined || toChar[i - 1] === "\n") {
                            html += "<span id='span-" + count + "' class='element-content unknown-char'>" + toChar[i] + "</span>";
                            count++;
                        } else if (self.checkAlphaOrEmpty(toChar[i - 1]) && toChar[i - 1] !== "@") {
                            html = self.insertString(html, toChar[i], html.length - 7);
                        } else {
                            html += "<span id='span-" + count + "' class='element-content unknown-char'>" + toChar[i] + "</span>";
                            count++;
                        }
                    } else {
                        html += "<span id='span-" + count + "' class='element-content unknown-char'>" + toChar[i] + "</span>";
                        count++;
                    }
                }
            }
            html += "</span>";
            self.contentValue(html);
            self.lstError.removeAll();
            self.validateConsecutively($(".special-char"));
            self.validateBracket($(".special-char"));
            self.validateContainAtSign($(".japanese-character"));
            self.validateAfterAtSign($(".japanese-character"));
            self.validateBeforeAtSign($(".japanese-character"));
            self.validateDivideZero($(".special-char"));
            self.validateEmptyInput();
            self.validateNestedMoreThan10($(".special-char"));
            self.validateDigitsAfterDecimal($(".unknown-char"));
            self.validateFunction(self.textArea());

            self.contentValue($("#input-content-area").html());
        }

        getCurrentPosition(position) {
            var uiPosition = {};
            var $lines = $("#input-content-area").find(".editor-line");
            var index = 0;
            $lines.each(function(index, line) {
                var $line = $(line);
                var char = _.find($line.children(), function(text) {
                    var current = index + $(text).text().length;
                    index += $(text).text().length;
                    return current === position;
                });
                if (char !== undefined) {
                    uiPosition = $(char).position();
                    return;
                }
            });
            return uiPosition;
        }

        checkAlphaOrEmpty(char) {
            var speChar = new RegExp(/[~`!#$×%\（）＜＞≦≧＝≠^＾÷&*+=\-\[\]\\;\',/{}|\\\":<>\?\(\)]/g);
            return !speChar.test(char) || char === " " || char === undefined;
        }

        checkJapanese(char) {
            return !nts.uk.text.allHalf(char);
        }


        countNeighbor(index, array, countNext, countPrev) {
            var self = this;
            var current = _.find(array, function(a) { return $(a).attr("id") === "span-" + (index); });
            var previous = _.find(array, function(a) { return $(a).attr("id") === "span-" + (index - 1); });
            var next = _.find(array, function(a) { return $(a).attr("id") === "span-" + (index + 1); });
            if (previous === undefined && next === undefined) {
                return 0;
            }
            var previousCount = 0;
            var nextCount = 0;
            if (countNext && next) {
                if (next.innerText !== '（' && next.innerText !== '(') {
                    nextCount++;
                    nextCount += self.countNeighbor(index + 1, array, countNext, false);
                }
            }
            if (countPrev && previous) {
                if (previous.innerText !== '）' && previous.innerText !== ')') {
                    previousCount++;
                    previousCount += self.countNeighbor(index - 1, array, false, countPrev);
                }
            }
            return previousCount + nextCount;
        }

        countPreviousElement(element: Array<string>, x, index) {
            var x2 = element.slice(0, index);
            return _.filter(x2, function(d) {
                return d === x;
            }).length;
        }

        toArrayChar(element) {
            return _.map(element, function(data) {
                return $(data).html();
            });
        }
    }

}