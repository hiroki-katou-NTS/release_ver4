__viewContext.ready(function () {
    var ScreenModel = (function () {
        function ScreenModel() {
            var _this = this;
            //----------------------------------------------------------------------------
            this.autoComplete = ko.observableArray([
                new ItemModel2('001', '基本給', "description 1"),
                new ItemModel2('150', '役職手当', "description 2"),
                new ItemModel2('ABC', '基12本ghj給', "description 3"),
                new ItemModel2('002', '基本給', "description 4"),
                new ItemModel2('153', '役職手当', "description 5"),
                new ItemModel2('AB4', '基12本ghj給', "description 6"),
                new ItemModel2('003', '基本給', "description 7"),
                new ItemModel2('155', '役職手当', "description 8"),
                new ItemModel2('AB5', '基12本ghj給', "description 9")
            ]);
            this.showAutoComplete = ko.observable(false);
            this.autoSelected = ko.observable("");
            this.row = ko.observable(1);
            this.col = ko.observable(1);
            this.autoSelected.subscribe(function (value) {
                //                $("#auto-complete-containner").show()
                if (value !== undefined) {
                    var currentString = $("#input-text").val();
                    var index = this.getIndex(currentString, this.row(), this.col()) + 1;
                    var selectValue = value.name();
                    var inserted = this.insertString(currentString, selectValue, index);
                    this.textArea(inserted);
                    $("#input-text").focus();
                    $("#input-text")[0].selectionStart = index + selectValue.length;
                    $("#input-text")[0].selectionEnd = index + selectValue.length;
                    this.testError();
                    $("#auto-complete-containner").hide();
                    this.autoSelected(undefined);
                }
            }, this);
            this.textArea = ko.observable("");
            this.divValue = ko.observable("");
            $("#input-text").keyup(function (event) {
                var start = $("#input-text")[0].selectionStart;
                var end = $("#input-text")[0].selectionEnd;
                var maxWidthCharacter = 15;
                //                if(event.keyCode === 8){
                //                      
                //                }else 
                if ((event.shiftKey && event.keyCode === 50) || event.keyCode === 192) {
                    //                    $("#auto-complete-containner").css("top": start);
                    var currentRow = _this.getCurrentRows(end);
                    var currentCol = _this.getCurrentColumn(currentRow, end);
                    _this.row(currentRow);
                    _this.col(currentCol);
                    $("#auto-complete-containner").show();
                    $("#auto-complete-containner").css({ "top": (currentRow * 17) + "px", "left": (currentCol * maxWidthCharacter) + "px" });
                }
                else {
                    $("#auto-complete-containner").hide();
                    _this.testError();
                }
            });
        }
        ScreenModel.prototype.insertString = function (original, sub, position) {
            if (original.length === position) {
                return original + sub;
            }
            return original.substr(0, position) + sub + original.substr(position);
        };
        ScreenModel.prototype.testError = function () {
            var value = $("#input-text").val();
            var count = 1;
            var toChar = value.split('');
            var html = "<span class='editor-line'>";
            html += "<span id='span-" + count + "'>";
            count++;
            for (var i = 0; i < toChar.length; i++) {
                if (toChar[i] === "\n") {
                    html += "</span></span>";
                    html += "<span class='editor-line'>";
                }
                else {
                    if (this.checkAlphaOrEmpty(toChar[i])) {
                        if (this.checkAlphaOrEmpty(toChar[i - 1])) {
                            html += toChar[i];
                        }
                        else {
                            if (toChar[i - 1] !== "\n") {
                                html += "</span>";
                            }
                            html += "<span id='span-" + count + "'>" + toChar[i];
                            count++;
                        }
                    }
                    else {
                        if (toChar[i - 1] !== "\n" && this.checkAlphaOrEmpty(toChar[i - 1])) {
                            html += "</span>";
                        }
                        html += "<span id='span-" + count + "' class='special-char'>" + toChar[i] + "</span>";
                        count++;
                    }
                }
            }
            html += "</span></span>";
            this.divValue(html);
            this.testGachChan($(".special-char"));
            this.divValue($("#input-content-area").html());
        };
        ScreenModel.prototype.getIndex = function (str, row, col) {
            if (row === 1) {
                return col - 1;
            }
            var rowValues = str.split("\n");
            var index = 0;
            for (var i = 0; i < row - 1; i++) {
                index = rowValues[i].length + 1;
            }
            return index + col - 1;
        };
        ScreenModel.prototype.getCurrentColumn = function (currentRow, position) {
            if (currentRow === 1) {
                return position;
            }
            var rowValues = $("#input-text").val().split("\n");
            var count = 1;
            var x = position;
            for (var i = 0; i < currentRow - 1; i++) {
                count = x - rowValues[i].length - 1;
                x = count;
            }
            return count + 1;
        };
        ScreenModel.prototype.getCurrentRows = function (position) {
            var currentText = $("#input-text").val();
            var count = 1;
            var start = currentText.indexOf("\n", 0);
            while (start !== -1 && start < position) {
                count++;
                start = currentText.indexOf("\n", start + 1);
            }
            ;
            return count;
        };
        ScreenModel.prototype.checkAlphaOrEmpty = function (char) {
            var speChar = new RegExp(/[~`!#$%\^&*+=\-\[\]\\';,/{}|\\\":<>\?\(\)]/);
            return !speChar.test(char) || char === " " || char === undefined;
        };
        ScreenModel.prototype.testGachChan = function (specialChar) {
            var openSpecial = {
                "&gt;": "&lt;",
                "\)": "\(",
                "\]": "\[",
                "\}": "\{"
            };
            var singleSpecial = {
                "+": "+",
                "-": "-",
                ".": ".",
                "*": "*",
                "/": "/",
                "\\": "\\",
                "!": "!",
                "#": "#",
                "$": "$",
                "%": "%"
            };
            var doubleSpecial = {
                "&": "&",
                "|": "|"
            };
            var element = this.toArrayChar(specialChar);
            for (var i = 0; i < specialChar.length; i++) {
                var $data = $(specialChar[i]);
                var charCount = parseInt($data.attr("id").split("-")[1]);
                var char = $data.text();
                var openComa = openSpecial[nts.uk.text.htmlEncode(char)];
                var single = singleSpecial[char];
                var double = doubleSpecial[char];
                if (openComa !== undefined) {
                    var x2 = this.countPreviousElement(element, nts.uk.text.htmlEncode(char), i) + 1;
                    var x = this.countPreviousElement(element, openComa, i);
                    if (x2 > x) {
                        $data.addClass("error-char");
                    }
                }
                else if (single !== undefined) {
                    var neighborCount = this.countNeighbor(charCount, specialChar, true, true);
                    if (neighborCount > 0) {
                        $data.addClass("error-char");
                    }
                }
                else if (double !== undefined) {
                    var neighborCount = this.countNeighbor(charCount, specialChar, true, true);
                    if (neighborCount > 1) {
                        $data.addClass("error-char");
                    }
                }
                else if (double !== "@") {
                    $data.addClass("error-char");
                }
            }
        };
        ScreenModel.prototype.countNeighbor = function (index, array, countNext, countPrev) {
            var previous = _.find(array, function (a) { return $(a).attr("id") === "span-" + (index - 1); });
            var next = _.find(array, function (a) { return $(a).attr("id") === "span-" + (index + 1); });
            if (previous === undefined && next === undefined) {
                return 0;
            }
            var previousCount = 0;
            var nextCount = 0;
            if (countNext && next !== undefined) {
                nextCount++;
                nextCount += this.countNeighbor(index + 1, array, countNext, false);
            }
            if (countPrev && previous !== undefined) {
                previousCount++;
                previousCount += this.countNeighbor(index - 1, array, false, countPrev);
            }
            return previousCount + nextCount;
        };
        ScreenModel.prototype.countPreviousElement = function (element, x, index) {
            //        var count = 0;
            var x2 = element.slice(0, index);
            return _.filter(x2, function (d) {
                return d === x;
            }).length;
            //        for(var i = index; i >= 0; i--){
            //            if(element[i] === x){
            //                count++;             
            //            }                       
            //        }     
        };
        ScreenModel.prototype.toArrayChar = function (element) {
            return _.map(element, function (data) {
                return $(data).html();
            });
        };
        return ScreenModel;
    }());
    var ItemModel2 = (function () {
        function ItemModel2(code, name, description) {
            this.code = ko.observable(code);
            this.name = ko.observable(name);
            this.description = ko.observable(description);
            this.text = ko.computed(function () {
                return this.code() + "  " + this.name();
            }, this).extend({ deferred: true });
        }
        return ItemModel2;
    }());
    var ItemModel = (function () {
        function ItemModel(code, name, description) {
            this.code = code;
            this.name = name;
            this.description = description;
        }
        return ItemModel;
    }());
    this.bind(new ScreenModel());
});
