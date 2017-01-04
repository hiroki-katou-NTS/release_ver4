var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var option;
            (function (option_1) {
                var EditorOptionBase = (function () {
                    function EditorOptionBase() {
                    }
                    return EditorOptionBase;
                }());
                var TextEditorOption = (function (_super) {
                    __extends(TextEditorOption, _super);
                    function TextEditorOption(option) {
                        _super.call(this);
                        // Default value
                        this.textmode = (option && option.textmode) ? option.textmode : "text";
                        this.placeholder = (option && option.placeholder) ? option.placeholder : "";
                        this.width = (option && option.width) ? option.width : "";
                        this.textalign = (option && option.textalign) ? option.textalign : "left";
                        this.filldirection = (option && option.filldirection) ? option.filldirection : "right";
                        this.fillcharacter = (option && option.fillcharacter) ? option.fillcharacter : "0";
                    }
                    return TextEditorOption;
                }(EditorOptionBase));
                option_1.TextEditorOption = TextEditorOption;
                var TimeEditorOption = (function (_super) {
                    __extends(TimeEditorOption, _super);
                    function TimeEditorOption(option) {
                        _super.call(this);
                        // Default value
                        this.inputFormat = (option && option.inputFormat) ? option.inputFormat : "yearmonthdate";
                        this.placeholder = (option && option.placeholder) ? option.placeholder : "";
                        this.width = (option && option.width) ? option.width : "";
                        this.textalign = (option && option.textalign) ? option.textalign : "left";
                    }
                    return TimeEditorOption;
                }(EditorOptionBase));
                option_1.TimeEditorOption = TimeEditorOption;
                var NumberEditorOption = (function (_super) {
                    __extends(NumberEditorOption, _super);
                    function NumberEditorOption(option) {
                        _super.call(this);
                        // Default value
                        this.groupseperator = (option && option.groupseperator) ? option.groupseperator : ",";
                        this.grouplength = (option && option.grouplength) ? option.grouplength : 0;
                        this.decimalseperator = (option && option.decimalseperator) ? option.decimalseperator : ".";
                        this.decimallength = (option && option.decimallength) ? option.decimallength : 0;
                        this.placeholder = (option && option.placeholder) ? option.placeholder : "";
                        this.width = (option && option.width) ? option.width : "";
                        this.textalign = (option && option.textalign) ? option.textalign : "left";
                    }
                    return NumberEditorOption;
                }(EditorOptionBase));
                option_1.NumberEditorOption = NumberEditorOption;
                var CurrencyEditorOption = (function (_super) {
                    __extends(CurrencyEditorOption, _super);
                    function CurrencyEditorOption(option) {
                        _super.call(this);
                        // Default value
                        this.groupseperator = (option && option.groupseperator) ? option.groupseperator : ",";
                        this.grouplength = (option && option.grouplength) ? option.grouplength : 0;
                        this.decimalseperator = (option && option.decimalseperator) ? option.decimalseperator : ".";
                        this.decimallength = (option && option.decimallength) ? option.decimallength : 0;
                        this.currencyformat = (option && option.currencyformat) ? option.currencyformat : "JPY";
                        this.currencyposition = (option && option.currencyposition) ? option.currencyposition : getCurrencyPosition(this.currencyformat);
                        this.placeholder = (option && option.placeholder) ? option.placeholder : "";
                        this.width = (option && option.width) ? option.width : "";
                        this.textalign = (option && option.textalign) ? option.textalign : "left";
                    }
                    return CurrencyEditorOption;
                }(NumberEditorOption));
                option_1.CurrencyEditorOption = CurrencyEditorOption;
                function getCurrencyPosition(currencyformat) {
                    return currenryPosition[currencyformat] === null ? "right" : currenryPosition[currencyformat];
                }
                var MultilineEditorOption = (function (_super) {
                    __extends(MultilineEditorOption, _super);
                    function MultilineEditorOption(option) {
                        _super.call(this);
                        // Default value
                        this.resizeable = (option && option.resizeable !== undefined) ? option.resizeable : false;
                        this.placeholder = (option && option.placeholder) ? option.placeholder : "";
                        this.width = (option && option.width) ? option.width : "";
                        this.textalign = (option && option.textalign) ? option.textalign : "left";
                    }
                    return MultilineEditorOption;
                }(EditorOptionBase));
                option_1.MultilineEditorOption = MultilineEditorOption;
                var currenryPosition = {
                    "JPY": "left",
                    "USD": "right"
                };
            })(option = ui.option || (ui.option = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
