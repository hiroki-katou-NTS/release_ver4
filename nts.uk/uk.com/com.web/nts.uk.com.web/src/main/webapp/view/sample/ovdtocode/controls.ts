module nts.uk.ui.sample.controls {
    
    let docs = {
        accordion: { name: "ntsAccordion", path: "component/accordion/accordion.xhtml" },
        formlabel: { name: "ntsFormLabel", path: "component/label/form-label.xhtml" },
        linkbutton: { name: "ntsLinkButton", path: "component/linkbutton/linkbutton.xhtml" },
        checkbox: { name: "ntsCheckBox", path: "component/checkbox/checkbox.xhtml" },
        radio: { name: "ntsRadioBoxGroup", path: "component/radioboxgroup/radioboxgroup.xhtml" },
        combobox: { name: "ntsComboBox", path: "component/combobox/combobox.xhtml" },
        switchbutton: { name: "ntsSwitchButton", path: "component/switch/switch.xhtml" },
        listbox: { name: "ntsListBox", path: "component/listbox/listbox.xhtml" },
        gridlist: { name: "ntsGridList", path: "component/gridlist/gridlist.xhtml" },
        treegrid: { name: "ntsTreeGridView", path: "component/treegrid/treegrid.xhtml" },
        tree: { name: "ntsTreeDragAndDrop", path: "component/treedraganddrop/tree.xhtml" },
        searchbox: { name: "ntsSearchBox", path: "component/searchbox/searchbox.xhtml" },
        wizard: { name: "ntsWizard", path: "component/wizard/wizard.xhtml" },
        datepicker: { name: "ntsDatePicker", path: "component/datepicker/datepicker.xhtml" },
        tabpanel: { name: "ntsTabPanel", path: "component/tabpanel/tabpanel.xhtml" },
        helpbutton: { name: "ntsHelpButton", path: "component/helpbutton/help-button.xhtml" },
        texteditor: { name: "ntsTextEditor", path: "component/editor/text-editor.xhtml" },
        multieditor: { name: "ntsMultilineEditor", path: "component/editor/multiline-editor.xhtml" },
        numbereditor: { name: "ntsNumberEditor", path: "component/editor/number-editor.xhtml" },
        timedayeditor: { name: "ntsTimeWithDayEditor", path: "component/editor/timewithday-editor.xhtml" },
        timeeditor: { name: "ntsTimeEditor", path: "component/editor/time-editor.xhtml" },
        functionpanel: { name: "ntsFunctionPanel", path: "component/functionpanel/functionpanel.xhtml" },
        colorpicker: { name: "ntsColorPicker", path: "component/colorpicker/colorpicker.xhtml" },
        monthdaypicker: { name: "ntsMonthDays", path: "component/monthdays/monthdays.xhtml" },
        daterange: { name: "ntsDateRangePicker", path: "component/daterange/daterange.xhtml" },
        legendbutton: { name: "ntsLegendButton", path: "component/legendbutton/legendbutton.xhtml" },
        sidebar: { name: "Sidebar", path: "functionwrap/sidebar/sidebar.xhtml" },
        button: { name: "Button", path: "component/commoncss/button.xhtml" },
        label: { name: "Label", path: "commoncss/label.xhtml" },
        caret: { name: "Caret", path: "commoncss/caret.xhtml" },
        fileupload: { name: "ntsFileUpload", path: "utilities/fileupload/index.xhtml" },
        swaplist: { name: "ntsSwapList", path: "component/swaplist/swaplist.xhtml" },
        userguide: { name: "UserGuide", path: "component/functionwrap/userguide/user-guide.xhtml" },
        popup: { name: "ntsPopup", path: "component/functionwrap/popup/popup.xhtml" },
        panel: { name: "Panel", path: "component/component/panel/panel.xhtml" },
        none: { name: "-", path: "-" }
    };
    
    
    let controlsMap = {
        "テキストボックス（文字列）": { doc: docs.texteditor, props: {
            "PrimitiveValue": { api: "constraint" },
            "項目名": { api: "name" },
            "透かし文字": { api: "option.placeholder" }
        }},
        "テキストボックス（整数）": { doc: docs.numbereditor, props: {
            "PrimitiveValue": { api: "constraint" },
            "項目名": { api: "name" },
            "カンマ編集": { value: "○", api: "option.grouplength: 3" },
            "単位": { api: "option.unitID" },
            "透かし文字": { api: "option.placeholder" }
        }},
        "テキストボックス（実数）": { doc: docs.numbereditor, props: {
            "PrimitiveValue": { api: "constraint" },
            "項目名": { api: "name" },
            "カンマ編集": { value: "○", api: "option.grouplength: 3" },
            "単位": { api: "option.unitID" },
            "透かし文字": { api: "option.placeholder" }
        }},
        "テキストボックス（時間）": { doc: docs.timeeditor, props: {
            "PrimitiveValue": { api: "constraint" },
            "項目名": { api: "name" },
            "透かし文字": { api: "option.placeholder" }
        }},
        "テキストボックス（時刻）": { doc: docs.timeeditor, props: {
            "PrimitiveValue": { api: "constraint" },
            "項目名": { api: "name" },
            "透かし文字": { api: "option.placeholder" }
        }},
        "テキストボックス（社員コード）": { doc: docs.texteditor, props: {
            "Enterマーク": { api: "enterkey" },
            "項目名": { api: "name" },
            "透かし文字": { api: "option.placeholder" }
        }, remarks: "constraint: 'EmployeeCode'" },
        "テキストボックス（パスワード）": { doc: docs.texteditor, props: {
            "PrimitiveValue": { api: "constraint" },
            "項目名": { api: "name" },
            "透かし文字": { api: "option.placeholder" }
        }},
        "テキストボックス（複数行）": { doc: docs.multieditor, props: {
            "PrimitiveValue": { api: "constraint" },
            "項目名": { api: "name" },
            "透かし文字": { api: "option.placeholder" }
        }},
        "テキストボックス（日付）": { doc: docs.datepicker, props: {
            "精度": [
                { value: "年月日", api: "(default)" },
                { value: "年月", api: "dateFormat: 'yearmonth'\nvalueFormat: 'YYYYMM'" },
                { value: "年", api: "dateFormat: 'YYYY'\nvalueFormat: 'YYYY'" },
                { value: "年度", api: "dateFormat: 'YYYY'\nvalueFormat: 'YYYY'\nfiscalYear: true" },
            ],
            "項目名": { api: "name" },
            "曜日表示": { api: "dateFormat: 'YYYY/MM/DD ddd'" },
            "日付送り": { value: "○", api: "showJumpButtons: true" }
        }},
        "テキストボックス（検索）": { doc: docs.texteditor, props: {
            "Enterマーク": { api: "enterkey" },
            "項目名": { api: "name" },
            "透かし文字": { api: "option.placeholder" }
        }},
        "日区分時刻入力フォーム": { doc: docs.timedayeditor, props: {
            "日区分表示": [
                { value: "自動", api: "(default)" },
                { value: "非表示", api: "option.timeWithDay: true" },
            ],
            "項目名": { api: "name" },
        }},
        "期間入力フォーム": { doc: docs.daterange, props: {
            "精度": [
                { value: "年月日", api: "(default)" },
                { value: "年月", api: "type: 'yearmonth'" },
            ],
            "項目名": { api: "name" },
            "最長期間": { api: "maxRange" },
            "期間送り": { value: "○", api: "showNextPrevious: true" }
        }},
        "期間入力フォーム開始": { doc: docs.daterange, props: {
            "項目名": { api: "startName" }
        }},
        "期間入力フォーム終了": { doc: docs.daterange, props: {
            "項目名": { api: "endName" }
        }},
        "月日入力フォーム": { doc: docs.monthdaypicker, props: {
            "項目名": { api: "name" }
        }},
        "ファイル参照フォーム": { doc: docs.fileupload, props: {
            "項目名": { api: "name" },
            "ボタンのラベル": { api: "text" },
            "スタイル": [
                { value: "テキストボックス", api: "(default)" },
                { value: "リンクラベル", api: "asLink: true" },
            ],
            "ファイル種別": { api: "stereoType" },
            "アップロード": [
                { value: "任意", api: "(default)" },
                { value: "即時", api: "immediateUpload: true" },
            ]
        }},
        "igGrid": { doc: docs.none, props: {
            "-": { api: "" }
        }},
        "テーブル": { doc: docs.none, props: {
            "-": { api: "" },
        }},
        "ドロップダウンリスト": { doc: docs.combobox, props: {
            "最大表示数": { api: "visibleItemsCount" },
            "項目名": { api: "name" },
            "ソート": { api: "(bind sorted datasource)" }
        }},
        "グリッドリスト": { doc: docs.gridlist, props: {
            "選択モード": [
                { value: "複数選択", api: "multiple: true" },
                { value: "単一選択", api: "multiple: false" },
            ],
            "項目名": { api: "" },
            "ソート": { api: "(bind sorted datasource)" },
            "任意列ソート": { api: "" },
            "列幅可変": { api: "" },
            "ページング": { api: "" }
        }},
        "シンプルリスト": { doc: docs.listbox, props: {
            "選択モード": [
                { value: "複数選択", api: "multiple: true" },
                { value: "単一選択", api: "multiple: false" },
            ],
            "項目名": { api: "" },
            "ソート": { api: "(bind sorted datasource)" }
        }},
        "ツリーリスト": { doc: docs.treegrid, props: {
            "選択モード": [
                { value: "複数選択", api: "multiple: true" },
                { value: "単一選択", api: "multiple: false" },
            ],
            "項目名": { api: "" },
            "ソート": { api: "(bind sorted datasource)" }
        }},
        "親子リスト": { doc: docs.treegrid, props: {
            "選択モード": { api: "" },
            "項目名": { api: "" }
        }},
        "親子リスト親ノード": { doc: docs.treegrid, props: {
            "ソート": { api: "(bind sorted datasource)" }
        }},
        "親子リスト子ノード": { doc: docs.treegrid, props: {
            "ソート": { api: "(bind sorted datasource)" },
            "選択可能": { api: "" }
        }},
        "ツリーリスト階層列項目": { doc: docs.treegrid, props: {
            "-": { api: "" }
        }},
        "リスト列項目": { doc: docs.none, props: {
            "Enum": { api: "" }
        }},
        "ツリー": { doc: docs.tree, props: {
            "選択モード": [
                { value: "複数選択", api: "multiple: true" },
                { value: "単一選択", api: "multiple: false" },
            ],
            "項目名": { api: "" },
            "深さ上限": { api: "maxDeepLeaf" },
            "兄弟数上限": { api: "maxChildInNode" }
        }}, 
        "カラーピッカー": { doc: docs.colorpicker, props: {
            "項目名": { api: "" }
        }},
        "ラジオボタングループ": { doc: docs.radio, props: {
            "項目名": { api: "" },
            "Enum": { api: "" }
        }},
        "ラジオボタン": { doc: docs.radio, props: {
            "テキスト": { api: "" },
        }},
        "チェックボックス": { doc: docs.checkbox, props: {
            "テキスト": { api: "(text content in tag)" },
            "スタイル": [
                { value: "一般", api: "(default)" },
                { value: "ボタン", api: "button" },
                { value: "警告パネル", api: "warnpanel" },
            ]
        }},
        "スイッチボタングループ": { doc: docs.switchbutton, props: {
            "項目名": { api: "" },
            "Enum": { api: "" }
        }},
        "スイッチボタン": { doc: docs.switchbutton, props: {
            "テキスト": { api: "" },
        }},
        "スワップリスト": { doc: docs.swaplist, props: {
            "項目名": { api: "" },
            "移動上限": { api: "itemsLimit.right" },
            "並べ替えボタン": { value: "○", api: "showSort" },
            "検索フォーム": { value: "○", api: "showSearchBox" }
        }},
        "ボタン": { doc: docs.button, props: {
            "テキスト": { api: "(text content in tag)" },
            "色": [
                { value: "一般", api: "(default)" },
                { value: "実行", api: "class=\"proceed\"" },
                { value: "危険", api: "class=\"danger\"" },
            ],
            "サイズ": [
                { value: "特大", api: "class=\"x-large\"" },
                { value: "大", api: "class=\"large\"" },
                { value: "中", api: "(default)" },
                { value: "小", api: "class=\"small\"" },
            ],
            "アイコン": { api: "" },
            "キャレット": { api: "" }
        }},
        "ファンクションボタン": { doc: docs.button, props: {
            "テキスト": { api: "(text content in tag)" },
            "色": [
                { value: "一般", api: "(default)" },
                { value: "実行", api: "class=\"proceed\"" },
                { value: "危険", api: "class=\"danger\"" },
            ],
            "アイコン": { api: "" }
        }},
        "画像ボタン": { doc: docs.button, props: {
            "画像ファイル": { api: "" },
            "影": { api: "" },
        }},
        "タブグループ": { doc: docs.tabpanel, props: {
            "配置方向": [
                { value: "横", api: "(default)" },
                { value: "縦", api: "direction: 'vertical'" },
            ]
        }},
        "タブ": { doc: docs.tabpanel, props: {
            "テキスト": { api: "" },
        }},
        "サイドバー": { doc: docs.sidebar, props: {
            "-": { api: "" }
        }},
        "サイドバーリンクラベル": { doc: docs.sidebar, props: {
            "テキスト": { api: "" },
        }},
        "リンクラベル": { doc: docs.linkbutton, props: {
            "テキスト": { api: "" },
            "スタイル": { api: "" },
        }},
        "アコーディオン": { doc: docs.accordion, props: {
            "ヘッダテキスト": { api: "" },
        }},
        "はてなアイコン": { doc: docs.helpbutton, props: {
            "画像ファイル": { api: "image" },
            "テキスト": { api: "textId" },
        }},
        "凡例ボタン": { doc: docs.legendbutton, props: {
            "内容": { api: "" },
            "テキスト": { api: "" },
        }},
        "ウィザード": { doc: docs.wizard, props: {
            "-": { api: "" },
        }},
        "ウィザードタイトル": { doc: docs.wizard, props: {
            "アイコン": { api: "" },
            "テキスト": { api: "" },
        }},
        "ウィザードステップ": { doc: docs.wizard, props: {
            "テキスト": { api: "" },
        }},
        "ウィザード終端": { doc: docs.wizard, props: {
            "テキスト": { api: "" },
        }},
        "マスタ言語切替ボタン": { doc: docs.none, props: {
            "-": { api: "" }
        }},
        "ラベル": { doc: docs.label, props: {
            "テキスト": { api: "" },
        }},
        "警告ラベル": { doc: docs.label, props: {
            "テキスト": { api: "" },
        }},
        "フォームラベル": { doc: docs.formlabel, props: {
            "テキスト": { api: "" },
            "項目": { api: "" },
            "スタイル": { api: "" },
            "制約表示": { api: "" }
        }},
        "グリッドヘッダラベル": { doc: docs.none, props: {
            "テキスト": { api: "" },
            "制約表示": { api: "" },
        }},
        "表示専用テキストボックス": { doc: docs.none, props: {
            "項目名": { api: "" },
        }},
        "ガイドメッセージ": { doc: docs.userguide, props: {
            "テキスト": { api: "" },
            "対象項目": { api: "" },
        }},
        "キャレット": { doc: docs.caret, props: {
            "スタイル": { api: "" },
        }},
        "画像": { doc: docs.none, props: {
            "画像ファイル": { api: "" },
        }},
        "罫線": { doc: docs.none, props: {
            "-": { api: "" },
        }},
        "パネル": { doc: docs.panel, props: {
            "スタイル": { api: "" },
            "スクロールバー": { api: "" },
        }},
        "ポップアップパネル": { doc: docs.popup, props: {
            "トリガー": { api: "" },
        }},
        "マスタリスト選択パネル": { doc: docs.panel, props: {
            "スタイル": { api: "" },
        }},
        "ファンクションパネル": { doc: docs.functionpanel, props: {
            "ボタンテキスト": { api: "" },
        }},
        "リスト検索フォーム": { doc: docs.searchbox, props: {
            "対象リスト": { api: "" },
            "対象列": { api: "" },
            "検索モード": { api: "" }
        }},
        "受入ファイル形式指定フォーム": { doc: docs.none, props: {
            "-": { api: "" },
        }},
    };
    
    let controlList = [];
    for (let controlNameDesign in controlsMap) {
        let control = controlsMap[controlNameDesign];
        controlList.push({ dn: controlNameDesign, doc: control.doc, props: control.props });
    }
    
    class ControlProperty {
        nameInDesign: string;
        valueInDesign: string;
        api: string;
        
        constructor(nameInDesign: string, valueInDesign: string, api: string) {
            this.nameInDesign = nameInDesign;
            this.valueInDesign = valueInDesign;
            this.api = api;
        }
    }
    
    class Control {
        nameInDesign: string;
        nameInCode: string;
        pathToDocument: string;
        properties: ControlProperty[];
        remarks: string;
        
        constructor(nameInDesign: string, doc: any, props: any, remarks: string) {
            
            this.nameInDesign = nameInDesign;
            this.nameInCode = doc.name;
            this.pathToDocument = nts.uk.request.location.appRoot.mergeRelativePath("view/sample/" + doc.path).serialize();
            this.properties = [];
            for (let name in props) {
                let prop = props[name];
                if (!_.isArray(prop)) {
                    prop = [prop];
                }
                prop.map((p, i) => new ControlProperty(i === 0 ? name : "", p.value, p.api))
                    .forEach(p => this.properties.push(p));
            }
            
            this.remarks = remarks || "";
        }
        
        firstProperty() {
            return this.properties[0];
        }
        
        propertiesAfterSecond() {
            return this.properties.slice(1);
        }
    }
    
    class ScreenModel {
        controls: any[];
        
        constructor() {
            this.controls = [];
            
            for (let controlNameDesign in controlsMap) {
                let control = controlsMap[controlNameDesign];
                this.controls.push(new Control(controlNameDesign, control.doc, control.props, control.remarks));
            }
        }
    }
    
    __viewContext.ready(function() {
    
        
        this.bind(new ScreenModel());
    
    });
}

