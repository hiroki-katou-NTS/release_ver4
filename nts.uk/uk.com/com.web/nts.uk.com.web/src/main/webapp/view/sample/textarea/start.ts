__viewContext.ready(function() {

    function showError(event) {
        var currentString = $("#input-text").val();
        var selectValue = $(this).attr("messege");
        if(selectValue !== undefined){
            $(this).tooltip({ content: selectValue });
            $("#error-messege").text(selectValue);
            var position = $("#input-containner").position();
            $("#error-containner").css({
                "top": (event.data.pageY - position.top + 2) + "px",
                "left": (event.data.pageX - position.left + 2) + "px"
            });
            $("#error-containner").show();    
        }
    }

    class ScreenModel {
        autoComplete: KnockoutObservableArray<any>;
        textArea: KnockoutObservable<any>;
        divValue: KnockoutObservable<any>;
        autoSelected: KnockoutObservable<any>;
        showAutoComplete: KnockoutObservable<boolean>;
        row: KnockoutObservable<number>;
        col: KnockoutObservable<number>;
        index: KnockoutObservable<number>;
        error: KnockoutObservable<string>;

        constructor() {
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
            this.index = ko.observable(1);
            this.error = ko.observable("");
            this.autoSelected.subscribe(function(value) {
                //                $("#auto-complete-containner").show()
                if (value !== undefined) {
                    var currentString = $("#input-text").val();
                    var index = this.index() + 1;
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
            $("#error-containner").hide();
            //            $("#error-content").mouseout(function(event){
            //                $("#error-containner").hide();
            //            }
            $("#input-text").keyup((event) => {
                if (!event.shiftKey && event.keyCode === 16 && event.key === "@") {
                    return;
                }
                $("#error-containner").hide();
                var start = $("#input-text")[0].selectionStart;
                var end = $("#input-text")[0].selectionEnd;
                var maxWidthCharacter = 15;
                //                if(event.keyCode === 8){
                //                      
                //                }else 
                if (((event.shiftKey && event.keyCode === 50) || event.keyCode === 192) && event.key === "@") {
                    //                    $("#auto-complete-containner").css("top": start);
                    this.testError();
                    var currentRow = this.getCurrentPosition(end);
//                    var currentCol = this.getCurrentColumn(currentRow, end);
//                    this.row(currentRow);
//                    this.col(currentCol);
                    this.index(end);
                    $("#auto-complete-containner").show();
                    $("#auto-complete-containner").css({ "top": (currentRow.top + 17) + "px", "left": (currentRow.left + 15) + "px" });
                } else {
                    $("#auto-complete-containner").hide();
                    this.testError();
                }
            });
            $(document).on("mouseleave", "#error-containner", function(event) {
                $("#error-containner").hide();
            });

            $("#input-area").click(function(event) {
                $("#error-containner").hide();
                var y = _.findLast($(".special-char"), function(d) {
                    var x = $(d).offset();
                    return x.top <= event.pageY && x.left <= event.pageX
                        && (x.left + $(d).outerWidth()) >= event.pageX
                        && (x.top + $(d).outerHeight()) >= event.pageY;
                });
                if (y !== undefined) {
                    $(y).click({ pageX: event.pageX, pageY: event.pageY }, showError);
                    $(y).click();
                }
            });

        }

        insertString(original, sub, position) {
            if (original.length === position) {
                return original + sub;
            }
            return original.substr(0, position) + sub + original.substr(position);
        }

        testError() {
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
                        html += "<span id='span-" + count + "' class='autocomplete-char'>" + toChar[i] + "</span>";
                        count++;
                    } else if (this.checkJapanese(toChar[i])) {
                        if (toChar[i - 1] === undefined || toChar[i - 1] === "\n") {
                            html += "<span id='span-" + count + "' class='japanese-character'>" + toChar[i] + "</span>";
                            count++;
                        } else if (this.checkJapanese(toChar[i - 1])) {
                            html = this.insertString(html, toChar[i], html.length - 7);
                        } else {
                            html += "<span id='span-" + count + "' class='japanese-character'>" + toChar[i] + "</span>";
                            count++;
                        }
                    } else if (this.checkAlphaOrEmpty(toChar[i])) {
                        if (toChar[i - 1] === undefined || toChar[i - 1] === "\n") {
                            html += "<span id='span-" + count + "'>" + toChar[i] + "</span>";
                            count++;
                        } else if (this.checkAlphaOrEmpty(toChar[i - 1]) && toChar[i - 1] !== "@") {
                            html = this.insertString(html, toChar[i], html.length - 7);
                        } else {
                            html += "<span id='span-" + count + "'>" + toChar[i] + "</span>";
                            count++;
                        }
                    } else {
                        html += "<span id='span-" + count + "' class='special-char'>" + toChar[i] + "</span>";
                        count++;
                    }
                }
            }
            html += "</span>";
            this.divValue(html);
            this.testGachChan($(".special-char"));
            this.divValue($("#input-content-area").html());
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
                if(char !== undefined){
                    uiPosition = $(char).position();
                    return;
                }
            });
            return uiPosition;
        }

        checkAlphaOrEmpty(char) {
            var speChar = new RegExp(/[~`!#$%\^&*+=\-\[\]\\;\',/{}|\\\":<>\?\(\)]/g);
            return !speChar.test(char) || char === " " || char === undefined;
        }

        checkJapanese(char) {
            return !nts.uk.text.allHalf(char);
        }

        testGachChan(specialChar) {

            var singleSpecial = {
                "+": "+",
                "-": "-",
                "^": "^",
                "*": "*",
                "/": "/",
                "=": "=",
                "!": "!",
                "#": "#",
                "$": "$",
                "%": "%"
            };

            var closeTriangle = _.remove(specialChar, function(n) {
                return $(n).html() === "&gt;";
            });

            var openTriangle = _.remove(specialChar, function(n) {
                return $(n).html() === "&lt;";
            });

            var openRound = _.remove(specialChar, function(n) {
                return $(n).html() === "\(";
            });

            var closeRound = _.remove(specialChar, function(n) {
                return $(n).html() === "\)";
            });

            this.checkOpenClose(openTriangle, closeTriangle);

            this.checkOpenClose(openRound, closeRound);

            var element = this.toArrayChar(specialChar);
            for (var i = 0; i < specialChar.length; i++) {
                var $data = $(specialChar[i]);
                var charCount = parseInt($data.attr("id").split("-")[1]);
                var char = $data.text();
                var single = singleSpecial[char];
                if (single !== undefined) {
                    var neighborCount = this.countNeighbor(charCount, specialChar, true, true);
                    if (neighborCount > 0) {
                        $data.addClass("error-char").attr("messege", "test 2");
                    }
                } else if (char !== "@") {
                    $data.addClass("error-char").attr("messege", "test 4");
                }
            }
        }

        checkOpenClose(openTriangle, closeTriangle) {
            if (closeTriangle.length === 0) {
                $(openTriangle).addClass("error-char").attr("messege", "test 1");
            } else if (openTriangle.length === 0) {
                $(closeTriangle).addClass("error-char").attr("messege", "test 1");
            } else {
                var openError = [];
                for (var i = openTriangle.length - 1; i >= 0; i--) {
                    var currentOpen = openTriangle[i];
                    var id = parseInt($(currentOpen).attr("id").split("-")[1]);
                    var currentClose = _.find(closeTriangle, function(a) {
                        return parseInt($(a).attr("id").split("-")[1]) > id;
                    });
                    if (currentClose === undefined) {
                        openError.unshift(currentOpen);
                    } else {
                        closeTriangle.splice(closeTriangle.indexOf(currentClose), 1);
                    }
                }
                $(openError).addClass("error-char").attr("messege", "test 1");
                $(closeTriangle).addClass("error-char").attr("messege", "test 1");
            }
        }

        countNeighbor(index, array, countNext, countPrev) {
            var previous = _.find(array, function(a) { return $(a).attr("id") === "span-" + (index - 1); });
            var next = _.find(array, function(a) { return $(a).attr("id") === "span-" + (index + 1); });
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

        }

        countPreviousElement(element: Array<string>, x, index) {
            //        var count = 0;
            var x2 = element.slice(0, index);
            return _.filter(x2, function(d) {
                return d === x;
            }).length;
            //        for(var i = index; i >= 0; i--){
            //            if(element[i] === x){
            //                count++;             
            //            }                       
            //        }     
        }
        toArrayChar(element) {
            return _.map(element, function(data) {
                return $(data).html();
            });
        }
    }

    class ItemModel2 {
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

    this.bind(new ScreenModel());

});
