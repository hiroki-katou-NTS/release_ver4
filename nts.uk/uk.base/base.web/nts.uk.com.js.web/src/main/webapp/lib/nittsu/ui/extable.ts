/// <reference path="../reference.ts"/>

module nts.uk.ui.exTable {
     
    let NAMESPACE = "extable";
    let DISTANCE: number = 3;
    let SPACE: number = 10;
    let HEADER = "xheader";
    let HEADER_PRF = "ex-header-";
    let BODY_PRF = "ex-body-";
    let HEADER_TBL_PRF = "extable-header-";
    let BODY_TBL_PRF = "extable-body-";
    let H_BTN_CLS = "ex-height-btn";
    let LEFTMOST = "leftmost";
    let MIDDLE = "middle";
    let DETAIL = "detail";
    let VERTICAL_SUM = "vert-sum";
    let HORIZONTAL_SUM = "horz-sum";
    let LEFT_HORZ_SUM = "left-horz-sum";
    let CRUD = "crud";
    let ADD_ROW = "add-row";
    let DEL_ROWS = "delete-rows";
    let H_BTN_HEIGHT = "24px";
    // Body height setting mode
    let DYNAMIC = "dynamic";
    let FIXED = "fixed";
    // Update mode
    let COPY_PASTE = "copyPaste";
    let EDIT = "edit";
    let STICK = "stick";
    let Connector = {};
    let _scrollWidth;
    
    export class ExTable {
        $container: HTMLElement;
        $commander: HTMLElement;
        $follower: HTMLElement;
        headerHeight: string;
        bodyHeight: string;
        bodyRowHeight: string;
        horzSumHeaderHeight: string;
        horzSumBodyHeight: string;
        horzSumBodyRowHeight: string;
        leftmostHeader: any;
        leftmostContent: any;
        middleHeader: any;
        middleContent: any;
        detailHeader: any
        detailContent: any;
        verticalSumHeader: any;
        verticalSumContent: any;
        leftHorzSumHeader: any;
        leftHorzSumContent: any;
        horizontalSumHeader: any;
        horizontalSumContent: any;
        headers: any;
        bodies: any;
        features: any;
        // Settings
        areaResize: boolean;
        heightSetter: any;
        // dynamic or fixed
        bodyHeightSetMode: string = DYNAMIC;
        windowXOccupation: number = 0;
        windowYOccupation: number = 0;
        updateMode: string = EDIT;
        pasteOverWrite: boolean = true;
        stickOverWrite: boolean = true;
        viewMode: string;
        determination: any;
        modifications: any;
        
        constructor($container: JQuery, options: any) {
            this.$container = $container[0];
            this.$commander = options.primaryTable ? options.primaryTable[0] : null;
            this.$follower = options.secondaryTable ? options.secondaryTable[0] : null;
            this.bodyRowHeight = options.bodyRowHeight;
            this.headerHeight = options.headerHeight;
            this.bodyHeight = options.bodyHeight;
            this.horzSumHeaderHeight = options.horizontalSumHeaderHeight;
            this.horzSumBodyHeight = options.horizontalSumBodyHeight;
            this.horzSumBodyRowHeight = options.horizontalSumBodyRowHeight;
            this.areaResize = options.areaResize;
            this.heightSetter = options.heightSetter;
            this.bodyHeightSetMode = options.bodyHeightMode;
            this.windowXOccupation = options.windowXOccupation;
            this.windowYOccupation = options.windowYOccupation;
            if (options.updateMode) { 
                this.updateMode = options.updateMode; 
            }
            this.pasteOverWrite = options.pasteOverWrite;
            this.stickOverWrite = options.stickOverWrite;
            this.viewMode = options.viewMode;
            this.determination = options.determination;
            this.features = options.features;
            $.data(this.$container, internal.X_OCCUPY, this.windowXOccupation);
            $.data(this.$container, internal.Y_OCCUPY, this.windowYOccupation);
            helper.makeConnector();
        }
        setUpdateMode(updateMode: any) {
            this.updateMode = updateMode;
            this.detailContent.updateMode = updateMode;
        }
        setViewMode(mode: any) {
            this.viewMode = mode;
            this.detailContent.viewMode = mode;
        }
        LeftmostHeader(leftmostHeader: any) {
            this.leftmostHeader = _.cloneDeep(leftmostHeader);
            this.setHeaderClass(this.leftmostHeader, LEFTMOST);
            return this;
        }
        LeftmostContent(leftmostContent: any) {
            this.leftmostContent = _.cloneDeep(leftmostContent);
            this.setBodyClass(this.leftmostContent, LEFTMOST);
            this.leftmostContent.updateMode = this.updateMode;
            if (feature.isEnable(this.features, feature.UPDATING)) {
                this.leftmostHeader.width = parseInt(this.leftmostHeader.width) + controls.CHECKBOX_COL_WIDTH + "px";
                controls.addCheckBoxDef([ this.leftmostHeader, this.leftmostContent ]);
            }
            return this;
        }
        MiddleHeader(middleHeader: any) {
            this.middleHeader = _.cloneDeep(middleHeader);
            this.setHeaderClass(this.middleHeader, MIDDLE);
            this.middleHeader.updateMode = this.updateMode;
            return this;
        }
        MiddleContent(middleContent: any) {
            this.middleContent = _.cloneDeep(middleContent);
            this.setBodyClass(this.middleContent, MIDDLE);
            return this;
        }
        DetailHeader(detailHeader: any) {
            this.detailHeader = _.cloneDeep(detailHeader);
            this.setHeaderClass(this.detailHeader, DETAIL);
            return this;
        }
        DetailContent(detailContent: any) {
            this.detailContent = _.cloneDeep(detailContent);
            this.setBodyClass(this.detailContent, DETAIL);
            this.detailContent.updateMode = this.updateMode;
            this.detailContent.viewMode = this.viewMode;
            return this;
        }
        VerticalSumHeader(verticalSumHeader: any) {
            this.verticalSumHeader = _.cloneDeep(verticalSumHeader);
            this.setHeaderClass(this.verticalSumHeader, VERTICAL_SUM);
            return this;
        }
        VerticalSumContent(verticalSumContent: any) {
            this.verticalSumContent = _.cloneDeep(verticalSumContent);
            this.setBodyClass(this.verticalSumContent, VERTICAL_SUM);
            return this;
        }
        LeftHorzSumHeader(leftHorzSumHeader: any) {
            this.leftHorzSumHeader = _.cloneDeep(leftHorzSumHeader);
            this.setHeaderClass(this.leftHorzSumHeader, LEFT_HORZ_SUM);
            return this;
        }
        LeftHorzSumContent(leftHorzSumContent: any) {
            this.leftHorzSumContent = _.cloneDeep(leftHorzSumContent);
            this.setBodyClass(this.leftHorzSumContent, LEFT_HORZ_SUM);
            return this;
        }
        HorizontalSumHeader(horizontalSumHeader: any) {
            this.horizontalSumHeader = _.cloneDeep(horizontalSumHeader);
            this.setHeaderClass(this.horizontalSumHeader, HORIZONTAL_SUM);
            return this;
        }
        HorizontalSumContent(horizontalSumContent: any) {
            this.horizontalSumContent = _.cloneDeep(horizontalSumContent);
            this.setBodyClass(this.horizontalSumContent, HORIZONTAL_SUM);
            return this;
        }
        setHeaderClass(options: any, part: string) {
            options.tableClass = HEADER_TBL_PRF + part;
            options.containerClass = HEADER_PRF + part;
        }
        setBodyClass(options: any, part: string) {
            options.tableClass = BODY_TBL_PRF + part;
            options.containerClass = BODY_PRF + part;
        }
        
        /**
         * Create.
         */
        create() {
            let self = this;
            let left: string = "0px";
            let top: string = "0px";
            if (!self.satisfyPrebuild()) return;
            self.headers = _.filter([ self.leftmostHeader, self.middleHeader, self.detailHeader, self.verticalSumHeader ], (h) => {
                return !util.isNullOrUndefined(h);
            });
            self.bodies = _.filter([ self.leftmostContent, self.middleContent, self.detailContent, self.verticalSumContent ], (b) => {
                return !util.isNullOrUndefined(b);
            });
            
            // Get part widths
            let widthParts, gridHeight;
            storage.area.getPartWidths(self.$container).ifPresent(function(parts) {
                widthParts = JSON.parse(parts);
                return null;
            });
            // Get grid height
            storage.tableHeight.get(self.$container).ifPresent(function(height) {
                gridHeight = JSON.parse(height);
                return null;
            });
            
            self.$container.classList.add(NAMESPACE);
            $.data(self.$container, NAMESPACE, self);
            let pTable = $.data(self.$container, NAMESPACE); 
            pTable.owner = { headers: [], bodies: [], 
            find: function(name, where) {
                let o = this;
                let elm = o[where].filter((e, i) => e.classList.contains(name));
                if (!elm || elm.length === 0) return null;
                return elm;
            }};
            
            let scrollWidth = helper.getScrollWidth();
            let headerWrappers = [], bodyWrappers = [];
            for (let i = 0; i < self.headers.length; i++) {
                if (!util.isNullOrUndefined(self.headers[i])) {
                    self.headers[i].overflow = "hidden";
                    self.headers[i].height = self.headerHeight;
                    self.headers[i].isHeader = true;
                    self.setWrapperWidth(self.headers[i], widthParts);
                    let $headerWrapper = render.createWrapper("0px", left, self.headers[i]);
                    if (!util.isNullOrUndefined($headerWrapper.style.maxWidth)
                        && parseFloat(self.headers[i].width) > parseFloat($headerWrapper.style.maxWidth)) {
                        self.headers[i].width = $headerWrapper.style.maxWidth;
                    }
                    pTable.owner.headers.push($headerWrapper); 
                    $headerWrapper.classList.add(HEADER);
                    self.$container.appendChild($headerWrapper);
                    render.process($headerWrapper, self.headers[i]);
                    left = (parseInt(left) + parseInt(self.headers[i].width) + DISTANCE) + "px";
                    top = (parseInt(self.headers[i].height) + DISTANCE) + "px";
                    headerWrappers.push($headerWrapper);
                }
            }
            left = "0px";
            for (let i = 0; i < self.bodies.length; i++) {
                let $bodyWrapper: HTMLElement;
                if (!util.isNullOrUndefined(self.bodies[i])) {
                    self.bodies[i].rowHeight = self.bodyRowHeight;
                    self.bodies[i].height = gridHeight ? (parseFloat(gridHeight) + "px") : self.bodyHeight;
                    self.bodies[i].width = self.headers[i].width;
                    self.setWrapperWidth(self.bodies[i], widthParts);
                    $bodyWrapper = render.createWrapper(top, left, self.bodies[i]);
                    pTable.owner.bodies.push($bodyWrapper);
                    self.$container.appendChild($bodyWrapper);
                    if (i === self.bodies.length - 1 && !util.isNullOrUndefined($bodyWrapper)) {
                        self.bodies[i].overflow = "scroll";
                        self.bodies[i].width = (parseFloat($bodyWrapper.style.width) + scrollWidth) + "px";
                        self.bodies[i].height = (parseFloat($bodyWrapper.style.height) + scrollWidth) + "px";
                        if (!util.isNullOrUndefined($bodyWrapper.style.maxWidth)) { 
                            $bodyWrapper.style.maxWidth = (parseFloat($bodyWrapper.style.maxWidth) + scrollWidth) + "px";
                        }
                        scroll.syncDoubDirVerticalScrolls(_.concat(bodyWrappers, $bodyWrapper));
                        scroll.bindVertWheel($bodyWrapper, true);
                    } else if (i > 0 && i < self.bodies.length - 1) {
                        self.bodies[i].overflowX = "scroll";
                        self.bodies[i].overflowY = "hidden";
                        self.bodies[i].height = (parseFloat($bodyWrapper.style.height) + scrollWidth) + "px";
                        scroll.bindVertWheel($bodyWrapper);
                    } else {
                        scroll.bindVertWheel($bodyWrapper);
                    }
                    render.process($bodyWrapper, self.bodies[i]);
                    left = (parseInt(left) + parseInt(self.bodies[i].width) + DISTANCE) + "px";
                    if (self.bodies[i].containerClass !== BODY_PRF + DETAIL) {
                        scroll.syncHorizontalScroll(headerWrappers[i], $bodyWrapper);
                    }
                    bodyWrappers.push($bodyWrapper);
                    if (feature.isEnable(self.headers[i].features, feature.COLUMN_RESIZE)) {
                        new resize.ColumnAdjuster(headerWrappers[i].find("table"), $bodyWrapper.find("table")).handle();
                    }
                }
            }
            
            self.createHorzSums(pTable);
            self.setupCrudArea();
            self.generalSettings(headerWrappers, bodyWrappers);
        }
        
        /**
         * Create horizontal sums.
         */
        createHorzSums(table: any) {
            let self = this;
            let $detailHeader = self.$container.querySelector("." + HEADER_PRF + DETAIL);
            let $detailContent = self.$container.querySelector("." + BODY_PRF + DETAIL);
            let headerTop = parseFloat($detailHeader.style.height) + parseFloat($detailContent.style.height) + DISTANCE + helper.getScrollWidth() + SPACE;
            let bodyTop = headerTop + parseFloat(self.horzSumHeaderHeight) + DISTANCE + "px";
            let sumPosLeft = $detailHeader.style.left;
            let leftHorzWidth = parseInt(sumPosLeft) - DISTANCE;
            let $leftSumHeaderWrapper, $leftSumContentWrapper, $sumHeaderWrapper, $sumContentWrapper;
            // Items summary
            if (self.leftHorzSumHeader) {
                self.leftHorzSumHeader.height = self.horzSumHeaderHeight;
                self.leftHorzSumHeader.width = leftHorzWidth + "px";
                self.leftHorzSumHeader.overflow = "hidden";
                self.leftHorzSumHeader.isHeader = true;
                $leftSumHeaderWrapper = render.createWrapper(headerTop + "px", "0xp", self.leftHorzSumHeader);
                table.owner.headers.push($leftSumHeaderWrapper);
                $leftSumHeaderWrapper.classList.add(HEADER);
                self.$container.appendChild($leftSumHeaderWrapper);
                render.process($leftSumHeaderWrapper, self.leftHorzSumHeader);
            }
            if (self.leftHorzSumContent) {
                self.leftHorzSumContent.rowHeight = self.horzSumBodyRowHeight;
                self.leftHorzSumContent.height = parseFloat(self.horzSumBodyHeight) + helper.getScrollWidth() + "px";
                self.leftHorzSumContent.width = leftHorzWidth + "px";
                $leftSumContentWrapper = render.createWrapper(bodyTop, "0px", self.leftHorzSumContent);
                table.owner.bodies.push($leftSumContentWrapper);
                self.leftHorzSumContent.overflowX = "scroll";
                self.leftHorzSumContent.overflowY = "hidden";
                self.$container.appendChild($leftSumContentWrapper);
                render.process($leftSumContentWrapper, self.leftHorzSumContent);
                scroll.bindVertWheel($leftSumContentWrapper);
            }
            
            // Main summary
            if (self.horizontalSumHeader) {
                self.horizontalSumHeader.height = self.horzSumHeaderHeight;
                self.horizontalSumHeader.width = $detailHeader.style.width;
                self.horizontalSumHeader.overflow = "hidden";
                self.horizontalSumHeader.isHeader = true;
                $sumHeaderWrapper = render.createWrapper(headerTop + "px", sumPosLeft, self.horizontalSumHeader);
                table.owner.headers.push($sumHeaderWrapper);
                $sumHeaderWrapper.classList.add(HEADER);
                self.$container.appendChild($sumHeaderWrapper);
                render.process($sumHeaderWrapper, self.horizontalSumHeader);
            }
            if (self.horizontalSumContent) {
                self.horizontalSumContent.rowHeight = self.horzSumBodyRowHeight;
                self.horizontalSumContent.height = parseInt(self.horzSumBodyHeight) + helper.getScrollWidth() + "px";
                let detailOverflow = $detailContent.style.overflow;
                let horzSumWidth = parseFloat($detailContent.style.width) 
                        + ((!util.isNullOrUndefined(detailOverflow) && !_.isEmpty(detailOverflow) && detailOverflow !== "hidden") 
                            ? 0 : helper.getScrollWidth());  
                self.horizontalSumContent.width = horzSumWidth + "px"; 
                $sumContentWrapper = render.createWrapper(bodyTop, sumPosLeft, self.horizontalSumContent);
                table.owner.bodies.push($sumContentWrapper);
                self.horizontalSumContent.overflow = "scroll";
                self.$container.appendChild($sumContentWrapper);
                render.process($sumContentWrapper, self.horizontalSumContent);
                scroll.syncHorizontalScroll($leftSumHeaderWrapper, $leftSumContentWrapper);
                scroll.syncDoubDirVerticalScrolls([ $leftSumContentWrapper, $sumContentWrapper ]);
                scroll.bindVertWheel($sumContentWrapper, true);
            }
            if (self.$commander) {
                self.$commander.addXEventListener(events.MOUSEIN_COLUMN, function(evt: any) {
                    let colIndex = evt.detail;
                    helper.highlightColumn(self.$container, colIndex);
                });
                self.$commander.addXEventListener(events.MOUSEOUT_COLUMN, function(evt: any) {
                    let colIndex = evt.detail;
                    helper.unHighlightColumn(self.$container, colIndex);
                });
                let pHorzHeader = self.$commander.querySelector("." + HEADER_PRF + HORIZONTAL_SUM);
                let pHorzBody = self.$commander.querySelector("." + BODY_PRF + HORIZONTAL_SUM);
                let cmdTbls = Array.prototype.slice.call(self.$commander.querySelectorAll("div[class*='" + DETAIL + "']"));
                let stream = _.concat(cmdTbls, pHorzHeader, pHorzBody, $detailHeader, $detailContent, $sumHeaderWrapper, $sumContentWrapper);
                scroll.syncDoubDirHorizontalScrolls(stream);
            } else if (self.$follower) { 
            } else {
                scroll.syncDoubDirHorizontalScrolls([ $detailHeader, $detailContent, $sumHeaderWrapper, $sumContentWrapper ]);
            }
        }
        
        /**
         * Setup crud area.
         */
        setupCrudArea() {
            let self = this;
            let updateF = feature.find(self.features, feature.UPDATING);
            if (updateF) {
                let $area = document.createElement("div");
                $area.className = NAMESPACE + "-" + CRUD;
                let $rowAdd = document.createElement("button");
                $rowAdd.className = NAMESPACE + "-" + ADD_ROW;
                $rowAdd.addXEventListener(events.CLICK_EVT, function() {
                    update.insertNewRow(self.$container);
                });
                $area.appendChild($rowAdd);
                let $rowDel = document.createElement("button");
                $rowDel.className = NAMESPACE + "-" + DEL_ROWS;
                $rowDel.addXEventListener(events.CLICK_EVT, function() {
                    update.deleteRows(self.$container);
                });
                $area.appendChild($rowDel);
                let dftRowAddTxt = "新規行の追加";
                let dftRowDelTxt = "行の削除";
                if (updateF.addNew) {
                    $rowAdd.classList.add(updateF.addNew.buttonClass || "proceed");
                    $rowAdd.textContent = updateF.addNew.buttonText || dftRowAddTxt;    
                } else {
                    $rowAdd.classList.add("proceed");
                    $rowAdd.textContent = dftRowAddTxt;
                }
                if (updateF.delete) {
                    $rowDel.classList.add(updateF.delete.buttonClass || "danger");
                    $rowDel.textContent = updateF.delete.buttonText || dftRowDelTxt;
                } else {
                    $rowDel.classList.add("danger");
                    $rowDel.textContent = dftRowDelTxt;
                }
                self.$container.insertAdjacentElement("beforebegin", $area);
            }
        }
        
        /**
         * General settings.
         */
        generalSettings(headerWrappers: Array<HTMLElement>, bodyWrappers: Array<HTMLElement>) {
            let self = this;
            self.$container.addXEventListener(events.BODY_HEIGHT_CHANGED, resize.onBodyHeightChanged);
//            if (self.heightSetter && self.heightSetter.showBodyHeightButton) {
//                
//                let $lastHeader = headerWrappers[headerWrappers.length - 1];
//                let btnPosTop = $lastHeader.height() - parseInt(H_BTN_HEIGHT) - DISTANCE;
//                let btnPosLeft = parseInt($lastHeader.css("left")) + $lastHeader.outerWidth();
//                let $heightSetBtn = $("<button/>").addClass(H_BTN_CLS)
//                    .css({ position: "absolute", height: H_BTN_HEIGHT, top: btnPosTop + "px", left: btnPosLeft + "px" })
//                    .text("H").on(events.CLICK_EVT, self.heightSetter.click);
//                $lastHeader.after($heightSetBtn);
//            }
            resize.fitWindowWidth(self.$container);
            window.addXEventListener(events.RESIZE, resize.fitWindowWidth.bind(self, self.$container));
            let horzSumExists = !util.isNullOrUndefined(self.horizontalSumHeader);
            if (self.bodyHeightSetMode === DYNAMIC) {
                resize.fitWindowHeight(self.$container, bodyWrappers, horzSumExists);
                window.addXEventListener(events.RESIZE, resize.fitWindowHeight.bind(self, self.$container, bodyWrappers, horzSumExists));
            } else {
                let cHeight = 0;
                let stream = self.$container.querySelectorAll("div[class*='" + DETAIL + "'], div[class*='" + LEFT_HORZ_SUM + "']");
                Array.prototype.slice.call(stream).forEach(function(e) {
                    cHeight += e.style.height;
                });
                if (stream.length === 4) {
                    cHeight += (SPACE + DISTANCE);
                }
                self.$container.style.height = (cHeight + SPACE) + "px";
            }
            
            if (self.$follower) {
                self.$follower.addXEventListener(events.COMPLETED, function() {
                    if (self.areaResize) {
                        new resize.AreaAdjuster(self.$container, headerWrappers, bodyWrappers, self.$follower).handle();
                        self.$container.addXEventListener(events.AREA_RESIZE_END, resize.onAreaComplete.bind(self));
                    }
                    let formerWidth = 0, latterWidth = 0;
                    _.forEach(headerWrappers, function(header) {
                        if (header.classList.contains(HEADER_PRF + LEFTMOST)) {
                            formerWidth += parseFloat(header.style.width);
                        } else if (header.classList.contains(HEADER_PRF + MIDDLE)) {
                            formerWidth += parseFloat(header.style.width) + DISTANCE;   
                        } else if (header.classList.contains(HEADER_PRF + DETAIL)) {
                            latterWidth += parseFloat(header.style.width);
                        }
                    });
                    let $lm = self.$follower.querySelectorAll("div[class*='" + LEFTMOST + "']");
                    let diff = formerWidth - parseInt($lm[0].style.width);
                    for (let i = 0; i < $lm.length; i++) {
                        $lm[i].style.width = formerWidth + "px";
                    }
                    let $depDetailHeader = self.$follower.querySelector("." + HEADER_PRF + DETAIL);
                    $depDetailHeader.style.width = latterWidth + "px";
                    let $depDetail = self.$follower.querySelector("." + BODY_PRF + DETAIL);
                    let left = parseInt($depDetail.style.left) + diff;
                    $depDetailHeader.style.left = left + "px";
                    $depDetail.style.left = left + "px";
                    $depDetail.style.width = (latterWidth + helper.getScrollWidth()) + "px";
                    let depLmHeader = _.filter($lm, function(e) {
                        return e.classList.contains(HEADER_PRF + LEFTMOST);
                    });
                    resize.saveSizes(self.$follower, depLmHeader[0], $depDetailHeader, formerWidth, latterWidth);
                });
            } else if (self.areaResize) {
                new resize.AreaAdjuster(self.$container, headerWrappers, bodyWrappers, self.$follower).handle();
                self.$container.addXEventListener(events.AREA_RESIZE_END, resize.onAreaComplete.bind(self));
            }
            storage.area.init(self.$container, headerWrappers);
            storage.tableHeight.init(self.$container);
            
            // Edit done
            update.editDone(self.$container);
            document.addXEventListener(events.CLICK_EVT, function(evt: any) {
                update.outsideClick(self.$container, evt.target);
            });
            events.onModify(self.$container);
            selection.checkUp(self.$container);
            copy.on(self.$container.querySelector("." + BODY_PRF + DETAIL), self.updateMode);
            self.$container.addXEventListener(events.OCCUPY_UPDATE, function(evt: any) {
                if (self.bodyHeightSetMode === FIXED) return;
                let reserve: any = evt.detail;
                if (reserve && reserve.x) {
                    $.data(self.$container, internal.X_OCCUPY, reserve.x);
                    resize.fitWindowWidth(self.$container);
                }
                if (reserve && reserve.y) {
                    $.data(self.$container, internal.Y_OCCUPY, reserve.y);
                    resize.fitWindowHeight(self.$container, bodyWrappers, horzSumExists);
                }
            });
            
            let containerWidth = headerWrappers.map(h => parseFloat(h.style.width)).reduce((a, v) => a + v);
            self.$container.style.width = (containerWidth + 34) + "px";
            if (self.$commander) {
                events.trigger(self.$container, events.COMPLETED);
            }
        }

        /**
         * Satisfy prebuild.
         */
        satisfyPrebuild() {
            if (util.isNullOrUndefined(this.$container) || util.isNullOrUndefined(this.headerHeight) 
                || util.isNullOrUndefined(this.bodyHeight) || util.isNullOrUndefined(this.bodyRowHeight)
                || util.isNullOrUndefined(this.horzSumBodyRowHeight)) 
                return false;
            return true;
        }
        
        /**
         * Set wrapper width.
         */
        setWrapperWidth(options: any, widthParts: any) {
            if (!widthParts) return;
            let width = widthParts[options.containerClass];
            if (!util.isNullOrUndefined(width)) {
                options.width = width + "px";
            } 
        }
    }
    
    module render {
        export let HIGHLIGHT_CLS: string = "highlight";
        export let CHILD_CELL_CLS: string = "child-cell";
        export let COL_ICON_CLS: string = "column-icon";
        
        /**
         * Process.
         */
        export function process($container: HTMLElement, options: any, isUpdate?: boolean) {
            let levelStruct = synthesizeHeaders(options);
            options.levelStruct = levelStruct;
            if (isUpdate && !util.isNullOrUndefined($container.style.maxWidth)) {
                let maxWidth = calcWidth(options.columns);
                if (!options.isHeader && options.overflow === "scroll") {
                    $container.style.maxWidth = (maxWidth + helper.getScrollWidth()) + "px";
                } else {
                    $container.style.maxWidth = maxWidth + "px";
                }
            }
            if (options.isHeader) {
                if (Object.keys(levelStruct).length > 1) {
                    groupHeader($container, options, isUpdate);
                    return;
                }
            } else {
                options.float = options.float === false ? false : true;
            }
            table($container, options, isUpdate);
        }
        
        /**
         * Group header.
         */
        function groupHeader($container: HTMLElement, options: any, isUpdate: boolean) {
            let $table = selector.create("table").html("<tbody></tbody>").addClass(options.tableClass)
                        .css({ position: "relative", "table-layout": "fixed", width: "100%", "border-collapse": "separate" }).getSingle();
            $container.appendChild($table);
            let $tbody = $table.getElementsByTagName("tbody")[0];
            if (!isUpdate) {
                $container.style.height = options.height;
                $container.style.width = options.width;
            }
            if (!util.isNullOrUndefined(options.overflow)) $container.style.overflow = options.overflow;
            else if (!util.isNullOrUndefined(options.overflowX) && !util.isNullOrUndefined(options.overflowY)) {
                $container.style.overflowX = options.overflowX;
                $container.style.overflowY = options.overflowY;
            }
            let $colGroup = document.createElement("colgroup"); 
            $table.insertBefore($colGroup, $tbody);
            generateColGroup($colGroup, options.columns);
            let painter: GroupHeaderPainter = new GroupHeaderPainter(options);
            painter.rows($tbody);
        }
        
        /**
         * Generate column group.
         */
        function generateColGroup($colGroup: HTMLElement, columns: any) {
            _.forEach(columns, function(col: any) {
                if (!util.isNullOrUndefined(col.group)) {
                    generateColGroup($colGroup, col.group);
                    return;
                }
                let $col = document.createElement("col");
                $col.style.width = col.width;
                $colGroup.appendChild($col);
                if (col.visible === false) $col.style.display = "none";
            });
        }
        
        /**
         * Table.
         */
        export function table($container: HTMLElement, options: any, isUpdate: boolean) {
            let $table = document.createElement("table");
            $table.innerHTML = "<tbody></tbody>";
            $table.className = options.tableClass;
            $table.style.position = "relative";
            $table.style.tableLayout = "fixed";
            $table.style.width = "100%";
            $table.style.borderCollapse = "separate";
            $container.appendChild($table);
            let $tbody = $table.getElementsByTagName("tbody")[0];
            if (!isUpdate) {
                $container.style.height = options.height;
                $container.style.width = options.width;
            }
            if (!util.isNullOrUndefined(options.overflow)) $container.style.overflow = options.overflow;
            else if (!util.isNullOrUndefined(options.overflowX) && !util.isNullOrUndefined(options.overflowY)) {
                $container.style.overflowX = options.overflowX;
                $container.style.overflowY = options.overflowY;
            }
            let $colGroup = document.createElement("colgroup");
            $table.insertBefore($colGroup, $tbody);
            generateColGroup($colGroup, options.columns);
            
            let dataSource;
            if (!util.isNullOrUndefined(options.dataSource)) {
                 dataSource = options.dataSource;   
            } else {
                let item = {};
                _.forEach(options.columns, function(col: any) {
                    item[col.key] = col.headerText;
                });
                dataSource = [item]; 
            }
            begin($container, dataSource, options);
        }
        
        /**
         * Begin.
         */
        export function begin($container: HTMLElement, dataSource: Array<any>, options: any) {
            if (options.float) {
                let cloud: intan.Cloud = new intan.Cloud($container, dataSource, options);
                $.data($container, internal.TANGI, cloud);
                cloud.renderRows(true);   
                return;
            }
            normal($container, dataSource, options);
        }
        
        /**
         * Normal.
         */
        export function normal($container: HTMLElement, dataSource: Array<any>, options: any) {
            let rowConfig = { css: { height: options.rowHeight }};
            let headerRowHeightFt;
            if (options.isHeader) {
                headerRowHeightFt = feature.find(options.features, feature.HEADER_ROW_HEIGHT);
            }
            let painter: Painter = new Painter($container, options);
            $.data($container, internal.CANON, { _origDs: _.cloneDeep(dataSource), dataSource: dataSource, primaryKey: options.primaryKey, painter: painter });
            let $tbody = $container.querySelector("tbody");
            _.forEach(dataSource, function(item: any, index: number) {
                if (!util.isNullOrUndefined(headerRowHeightFt)) {
                    rowConfig = { css: { height: headerRowHeightFt.rows[index] }};
                }
                $tbody.appendChild(painter.row(item, rowConfig, index));
            });
        }
        
        /**
         * Synthesize headers.
         */
        export function synthesizeHeaders(options: any) {
            let level: any = {};
            peelStruct(options.columns, level, 0);
            let rowCount = Object.keys(level).length; 
            if (rowCount > 1) {
                _.forEach(Object.keys(level), function(key: any) {
                    _.forEach(level[key], function(col: any) {
                        if (util.isNullOrUndefined(col.colspan) || col.colspan === 1) {
                            col.rowspan = rowCount - parseInt(key);
                        }
                    });
                });
            }
            return level;
        }
        
        /**
         * Peel struct.
         */
        function peelStruct(columns: Array<any>, level: any, currentLevel: number) {
            let colspan = 0, noGroup = 0;
            _.forEach(columns, function(col: any) {
                let clonedCol = _.clone(col);
                let colCount = 0;
                if (!util.isNullOrUndefined(col.group)) {
                    colCount = col.group.length;
                    noGroup++;
                    let ret: any = peelStruct(col.group, level, currentLevel + 1);
                    if (!util.isNullOrUndefined(ret)) {
                        colCount += ret;
                    }
                    clonedCol.colspan = colCount;
                }
                if (util.isNullOrUndefined(level[currentLevel])) {
                    level[currentLevel] = [];
                }
                level[currentLevel].push(clonedCol);
                colspan += colCount;
            });
            return colspan !== 0 ? (colspan - noGroup) : undefined;
        }
        
        abstract class Conditional {
            options: any;
            columnsMap: {[ key: string ]: any };
            visibleColumns: Array<any>;
            hiddenColumns: Array<any>;
            visibleColumnsMap: {[ key: string ]: any };
            hiddenColumnsMap: {[ key: string ]: any };
            constructor(options: any) {
                this.options = options;
                let columns = helper.classifyColumns(options);
                this.visibleColumns = columns.visibleColumns;
                this.hiddenColumns = columns.hiddenColumns;
                this.visibleColumnsMap = helper.getColumnsMap(this.visibleColumns);
                this.hiddenColumnsMap = helper.getColumnsMap(this.hiddenColumns);
            }
        }
        
        export class Painter extends Conditional {
            $container: HTMLElement;
            constructor($container: HTMLElement, options: any) {
                super(options);
                this.$container = $container;
                if (!util.isNullOrUndefined(options.levelStruct)) {
                    this.columnsMap = helper.columnsMapFromStruct(options.levelStruct);
                } else {
                    this.columnsMap = _.groupBy(options.columns, "key");
                }
            }
            
            cell(rData: any, rowIdx: number, key: string): HTMLElement {
                let self = this;
                let cData = rData[key]; 
                let data = cData && _.isObject(cData) && cData.constructor !== Array && _.isFunction(self.options.view) ? 
                            helper.viewData(self.options.view, self.options.viewMode, cData) : cData;
                let column: any = self.columnsMap[key];
                if (util.isNullOrUndefined(column)) return;
                let ws = column.css && column.css.whiteSpace ? column.css.whiteSpace : "nowrap";
                let td = document.createElement("td");
                $.data(td, internal.VIEW, rowIdx + "-" + key);
                
                let tdStyle = "";
                tdStyle += "; border-width: 1px; overflow: hidden; white-space: " 
                            + ws + "; position: relative;";
                self.highlight(td);
                
                if (!self.visibleColumnsMap[key]) tdStyle += "; display: none;"; //td.style.display = "none";
                if (!util.isNullOrUndefined(data) && data.constructor === Array) {
                    let incellHeight = parseInt(self.options.rowHeight) / 2 - 3;
                    let borderStyle = "solid 1px transparent";
                   _.forEach(data, function(item: any, idx: number) {
                       let divStyle = "";
                       let div: HTMLDivElement = document.createElement("div");
                       div.classList.add(CHILD_CELL_CLS);
                       div.innerText = util.isNullOrUndefined(item) ? "" : item;
                       if (idx < data.length - 1) {
                           divStyle += "; border-top: " + borderStyle + "; border-left: "
                                    + borderStyle + "; border-right: " + borderStyle 
                                    + "; border-bottom: dashed 1px #AAB7B8; top: 0px";
                       } else {
                           divStyle += "; border: " + borderStyle + "; top: " 
                                    + (incellHeight + 2) + "px";
                       }
                       
                       divStyle += "; position: absolute; left: 0px; height: " 
                                + incellHeight 
                                + "px; width: 98%; text-align: center;"
                       
                       div.style.cssText += divStyle;
                       td.appendChild(div);
                       
                       if (column.handlerType) {
                           let handler = cellHandler.get(column.handlerType);
                           if (handler) handler(div, self.options, helper.call(column.supplier, rData, rowIdx, key));
                       }
//                       cellHandler.rClick(div, column, helper.call(column.rightClick, rData, rowIdx, key));
//                       spread.bindSticker(div, rowIdx, key, self.options);
                   });
                   style.detCell(self.$container, td, rowIdx, key);
                   tdStyle += "; padding: 0px;";
                   td.style.cssText += tdStyle;
                   return td;
                }
                
                if (!util.isNullOrUndefined(column.handlerType) && !self.options.isHeader) {
                    let handler = cellHandler.get(column.handlerType);
                    if (!util.isNullOrUndefined(handler)) {
                        handler(td, self.options, helper.call(column.supplier, rData, rowIdx, key));
                    }
                }
                if (self.options.isHeader) {
                    if (!util.isNullOrUndefined(column.icon) && column.icon.for === "header") {
                        let icon = document.createElement("span");
                        icon.className = COL_ICON_CLS + " " + column.icon.class;
                        tdStyle += "; padding-left: " + column.icon.width + ";";
                        td.appendChild(icon);
                        if (column.icon.popup && typeof column.icon.popup === "function") {
                            icon.style.cursor = "pointer";
                            new widget.PopupPanel(icon, column.icon.popup, "bottom right");
                        }
                        let innerDiv = document.createElement("div");
                        innerDiv.innerHTML = data;
                        td.appendChild(innerDiv);
                    } else if (helper.containsBr(data)) { 
                        td.innerHTML = data; 
                    } else {
                        td.innerText = data;
                    }
                } else if (!self.options.isHeader) {
                    if (!util.isNullOrUndefined(column.icon) && column.icon.for === "body") {
                        let icon = document.createElement("span");
                        icon.className = COL_ICON_CLS + " " + column.icon.class;
                        tdStyle += "; padding-left: " + column.icon.width + ";";
                        td.appendChild(icon);
                    } else if (!column.control) {
                        td.innerText = data;
                    }
                    controls.check(td, column, data, helper.call(column.handler, rData, rowIdx, key));
//                    cellHandler.rClick(td, column, helper.call(column.rightClick, rData, rowIdx, key));
                } 
//                spread.bindSticker(td, rowIdx , key, self.options);
                style.detCell(self.$container, td, rowIdx, key);
                td.style.cssText += tdStyle;
                return td;
            }
            
            row(data: any, config: any, rowIdx: number): HTMLElement {
                let self = this;
                let tr: any = document.createElement("tr");
                tr.style.height = parseInt(config.css.height) + "px";
                let headerCellStyleFt, headerPopupFt, bodyCellStyleFt;
                if (self.options.isHeader) {
                    headerCellStyleFt = feature.find(self.options.features, feature.HEADER_CELL_STYLE);
                    headerPopupFt = feature.find(self.options.features, feature.HEADER_POP_UP);
                } else {
                    bodyCellStyleFt = feature.find(self.options.features, feature.BODY_CELL_STYLE);
                }
                
                let onChecked = function(checked, rowIndex) {
                    let $grid = self.options.isHeader ? selector.classSiblings(self.$container, BODY_PRF + LEFTMOST)[0]
                                : self.$container;
                    controls.tick(checked, $grid, self.options.isHeader, rowIndex);
                };
                if (!data[controls.CHECKED_KEY] && self.options.columns[0].key === controls.CHECKED_KEY) {
                    let td = document.createElement("td");
                    $.data(td, internal.VIEW, rowIdx + "-" + controls.CHECKED_KEY);
                    td.style.padding = "1px 1px";
                    td.style.textAlign = "center";
                    td.appendChild(controls.createCheckBox(self.$container, { initValue: false, onChecked: onChecked }));
                    tr.appendChild(td);
                }
                _.forEach(Object.keys(data), function(key: any, index: number) {
                    if (!self.visibleColumnsMap[key] && !self.hiddenColumnsMap[key]) return;
                    if (key === controls.CHECKED_KEY) {
                        let td = document.createElement("td");
                        td.style.padding = "1px 1px";
                        td.style.textAlign = "center";
                        td.appendChild(controls.createCheckBox(self.$container, { initValue: false, onChecked: onChecked }));
                        tr.appendChild(td);
                        return;
                    }
                    let cell = self.cell(data, rowIdx, key);
                    tr.appendChild(cell);
                    // Styles
                    if (!util.isNullOrUndefined(headerCellStyleFt)) {
                        _.forEach(headerCellStyleFt.decorator, function(colorDef: any) {
                            if (key === colorDef.columnKey) {
                                if ((!util.isNullOrUndefined(colorDef.rowId) && colorDef.rowId === rowIdx)
                                        || util.isNullOrUndefined(colorDef.rowId)) {
                                    helper.addClassList(cell, colorDef.clazz);
                                    return false;
                                }
                            }
                        });
                    } else if (!util.isNullOrUndefined(bodyCellStyleFt)) {
                        _.forEach(bodyCellStyleFt.decorator, function(colorDef: any) {
                            if (key === colorDef.columnKey && data[self.options.primaryKey] === colorDef.rowId) {
                                let childCells = cell.querySelectorAll("." + CHILD_CELL_CLS);
                                if (!util.isNullOrUndefined(colorDef.innerIdx) && childCells.length > 0) {
                                    let child = childCells[colorDef.innerIdx];
                                    helper.addClassList(child, colorDef.clazz);
                                    if (colorDef.clazz === style.HIDDEN_CLS) {
                                        $.data(child, "hide", child.textContent);
                                        child.innerHTML = "";
                                    }
                                } else {
                                    helper.addClassList(cell, colorDef.clazz);
                                    if (colorDef.clazz == style.HIDDEN_CLS) {
                                        $.data(cell, "hide", cell.innerText);
                                        cell.innerText = "";
                                    }
                                    return false;
                                }
                            }
                        });
                    }
                    if (util.isNullOrUndefined(self.columnsMap[key])) return;
                    let cellStyle = self.columnsMap[key].style;
                    if (!util.isNullOrUndefined(cellStyle)) {
                        cellStyle(new style.CellStyleParam($cell, data[key], data, rowIdx, key));
                    }
                });
                widget.bind(tr, rowIdx, headerPopupFt);
                style.detColumn(self.$container, tr, rowIdx);
                
                spread.bindRowSticker(tr, rowIdx, self.options);
                if (!self.options.isHeader) {
                    cellHandler.rRowClick(tr, self.columnsMap, { rData: data, rowIdx: rowIdx });
                }
                return tr;
            }
            
            /**
             * Highlight.
             */
            highlight(td: HTMLElement) {
                let self = this;
                if (self.options.isHeader || self.options.containerClass !== BODY_PRF + DETAIL) return;
                let $targetContainer = self.$container;
                let targetHeader = helper.firstSibling($targetContainer, self.options.containerClass.replace(BODY_PRF, HEADER_PRF));
                let extable = helper.getExTableFromGrid($targetContainer);
                let horzSumHeader, horzSumContent; 
                td.addXEventListener(events.MOUSE_OVER, function() {
                    let colIndex = helper.indexInParent(td);
                    let tr = helper.closest(td, "tr"); 
                    let rowIndex = helper.indexInParent(tr);
                    helper.addClass1n(tr.children, HIGHLIGHT_CLS);
                    if (!horzSumHeader || !horzSumContent) {
                        horzSumHeader = extable.owner.find(HEADER_PRF + HORIZONTAL_SUM, "headers");
                        if (horzSumHeader) horzSumHeader = horzSumHeader[0];
                        horzSumContent = extable.owner.find(BODY_PRF + HORIZONTAL_SUM, "bodies");
                        if (horzSumContent) horzSumContent = horzSumContent[0];
                    }
                    let bodies = extable.owner.bodies;
                    for (let i = 0; i < bodies.length; i++) {
                        if (!helper.hasClass(bodies[i], BODY_PRF + LEFT_HORZ_SUM)
                            && !helper.hasClass(bodies[i], BODY_PRF + HORIZONTAL_SUM)) {
                            let rowElm = bodies[i].getElementsByTagName("tr")[rowIndex];
                            if (rowElm) {
                                helper.addClass1n(rowElm.getElementsByTagName("td"), HIGHLIGHT_CLS);
                            }
                        }
                    }
                    helper.consumeSiblings(tr, (elm) => { 
                        let tds = elm.getElementsByTagName("td");
                        if (!tds || tds.length === 0) return;
                        helper.addClass1n(tds[colIndex], HIGHLIGHT_CLS); 
                    });
                    _.forEach(targetHeader.getElementsByTagName("tr"), function(t) {
                        let tds = t.getElementsByTagName("td");
                        if (!tds || tds.length === 0) return;
                        helper.addClass1n(tds[colIndex], HIGHLIGHT_CLS);
                    });
                    if (horzSumHeader && horzSumHeader.style.display !== "none") {
                        _.forEach(horzSumHeader.getElementsByTagName("tr"), function(t) {
                            let tds = t.getElementsByTagName("td");
                            if (!tds || tds.length === 0) return;
                            helper.addClass1n(tds[colIndex], HIGHLIGHT_CLS);
                        });
                        _.forEach(horzSumContent.getElementsByTagName("tr"), function(t) {
                            let tds = t.getElementsByTagName("td");
                            if (!tds || tds.length === 0) return;
                            helper.addClass1n(tds[colIndex], HIGHLIGHT_CLS);
                        });
                    }
                    $.data(self.$container, internal.COLUMN_IN, colIndex);
                    events.trigger(helper.closest(self.$container, "." + NAMESPACE), events.MOUSEIN_COLUMN, colIndex);
                });
                
                td.addXEventListener(events.MOUSE_OUT, function() {
                    helper.removeClass1n(td, HIGHLIGHT_CLS);
                    let colIndex = helper.indexInParent(td);
                    let tr = helper.closest(td, "tr"); 
                    let rowIndex = helper.indexInParent(tr);
                    helper.removeClass1n(tr.children, HIGHLIGHT_CLS);
                    if (!horzSumHeader || !horzSumContent) {
                        horzSumHeader = extable.owner.find(HEADER_PRF + HORIZONTAL_SUM, "headers");
                        if (horzSumHeader) horzSumHeader = horzSumHeader[0];
                        horzSumContent = extable.owner.find(BODY_PRF + HORIZONTAL_SUM, "bodies");
                        if (horzSumContent) horzSumContent = horzSumContent[0];
                    }
                    let bodies = extable.owner.bodies;
                    for (let i = 0; i < bodies.length; i++) {
                        if (!helper.hasClass(bodies[i], BODY_PRF + LEFT_HORZ_SUM)
                            && !helper.hasClass(bodies[i], BODY_PRF + HORIZONTAL_SUM)) {
                            let rowElm = bodies[i].getElementsByTagName("tr")[rowIndex];
                            if (rowElm) {
                                helper.removeClass1n(rowElm.getElementsByTagName("td"), HIGHLIGHT_CLS);
                            }
                        }
                    }
                    helper.consumeSiblings(tr, (elm) => { 
                        let tds = elm.getElementsByTagName("td");
                        if (!tds || tds.length === 0) return;
                        helper.removeClass1n(tds[colIndex], HIGHLIGHT_CLS); 
                    });
                    _.forEach(targetHeader.getElementsByTagName("tr"), function(t) { 
                        let tds = t.getElementsByTagName("td");
                        if (!tds || tds.length === 0) return;
                        helper.removeClass1n(tds[colIndex], HIGHLIGHT_CLS);
                    });
                    if (horzSumHeader && horzSumHeader.style.display !== "none") {
                        _.forEach(horzSumHeader.getElementsByTagName("tr"), function(t) {
                            let tds = t.getElementsByTagName("td");
                            if (!tds || tds.length === 0) return;
                            helper.removeClass1n(tds[colIndex], HIGHLIGHT_CLS);
                        });
                        _.forEach(horzSumContent.getElementsByTagName("tr"), function(t) {
                            let tds = t.getElementsByTagName("td");
                            if (!tds || tds.length === 0) return;
                            helper.removeClass1n(tds[colIndex], HIGHLIGHT_CLS);
                        });
                    }
                    $.data(self.$container, internal.COLUMN_IN, -1);
                    events.trigger(helper.closest(self.$container, "." + NAMESPACE), events.MOUSEOUT_COLUMN, colIndex);
                });
            }
        }
        
        export class GroupHeaderPainter extends Conditional {
            levelStruct: any;
            constructor(options: any) {
                super(options);
                this.levelStruct = options.levelStruct;
                this.columnsMap = helper.columnsMapFromStruct(this.levelStruct);
            }
            
            /**
             * Cell.
             */
            cell(text: any, rowIdx: number, cell: any): HTMLElement {
                let self = this;
                let $td = document.createElement("td");
                $.data($td, internal.VIEW, rowIdx + "-" + cell.key);
                let tdStyle = "; border-width: 1px; overflow: hidden; white-space: nowrap; border-collapse: collapse;";
                if (!util.isNullOrUndefined(cell.rowspan) && cell.rowspan > 1) $td.setAttribute("rowspan", cell.rowspan);
                if (!util.isNullOrUndefined(cell.colspan) && cell.colspan > 1) $td.setAttribute("colspan", cell.colspan);
                else if (!self.visibleColumnsMap[cell.key]) tdStyle += "; display: none;";
                
                if (!util.isNullOrUndefined(cell.icon) && cell.icon.for === "header") {
                    let $icon = document.createElement("span");
                    $icon.className = COL_ICON_CLS + " " + cell.icon.class;
                    $icon.style.top = "20%";
                    
                    tdStyle += "; padding-left: " + cell.icon.width + ";";
                    $td.appendChild($icon);
                    if (cell.icon.popup && typeof cell.icon.popup === "function") {
                        $icon.style.cursor = "pointer";
                        new widget.PopupPanel($icon, cell.icon.popup, "bottom right");
                    }
                    let $content = document.createElement("div");
                    $content.innerHTML = text;
                    $td.appendChild($content);
                } else if (helper.containsBr(text)) { 
                    $td.innerHTML = text; 
                } else {
                    $td.textContent = text;
                }
                $td.style.cssText += tdStyle;
                return $td;
            }
            
            /**
             * Rows.
             */
            rows($tbody: HTMLElement) {
                let self = this;
                let height = self.options.rowHeight;
                let headerRowHeightFt = feature.find(self.options.features, feature.HEADER_ROW_HEIGHT);
                let headerCellStyleFt = feature.find(self.options.features, feature.HEADER_CELL_STYLE);
                _.forEach(Object.keys(self.levelStruct), function(rowIdx: any) {
                    if (!util.isNullOrUndefined(headerRowHeightFt)) {
                        height = headerRowHeightFt.rows[rowIdx];
                    }
                    
                    let $tr = document.createElement("tr");
                    $tr.style.height = height;
                    let oneLevel = self.levelStruct[rowIdx];
                    _.forEach(oneLevel, function(cell: any) {
                        if (!self.visibleColumnsMap[cell.key] && !self.hiddenColumnsMap[cell.key]
                            && (util.isNullOrUndefined(cell.colspan) || cell.colspan == 1)) return;
                        let $cell = self.cell(cell.headerText, rowIdx, cell);
                        $tr.appendChild($cell);
                        if (!util.isNullOrUndefined(headerCellStyleFt)) {
                            _.forEach(headerCellStyleFt.decorator, function(colorDef: any) {
                                if (colorDef.columnKey === cell.key) {
                                    if ((!util.isNullOrUndefined(colorDef.rowId) && colorDef.rowId === rowIdx)
                                        || util.isNullOrUndefined(colorDef.rowId)) {
                                        helper.addClassList($cell, colorDef.clazz);   
                                    } 
                                    return false;
                                }
                            });
                        }
                        if (util.isNullOrUndefined(self.columnsMap[cell.key])) return;
                        let cellStyle = self.columnsMap[cell.key].style;
                        if (!util.isNullOrUndefined(cellStyle)) {
                            cellStyle(new style.CellStyleParam($cell, cell.headerText, undefined, rowIdx, cell.key)); 
                        }
                    });
                    $tbody.appendChild($tr);
                });
            }
        }
        
        /**
         * Extra.
         */
        export function extra(className: string, height: number) {
            let element = document.createElement("tr");
            element.style.height = height + "px";
            helper.addClass(element, "extable-" + className);
            return element;
        }
        
        /**
         * Wrapper styles.
         */
        export function wrapperStyles(top: string, left: string, width: string, height: string, maxWidth?: string) {
            let style: any = { 
                position: "absolute",
                overflow: "hidden",
                top: top,
                left: left,
                width: width,
                height: height,
                border: "solid 1px #CCC"
            };
            
            if (maxWidth) {
                style.maxWidth = maxWidth;
                if (parseFloat(maxWidth) < parseFloat(width)) style.width = maxWidth;
            }
            return style;   
        }
        
        /**
         * Create wrapper.
         */
        export function createWrapper(top: string, left: string, options: any) {
            let style;
            if (options.containerClass === HEADER_PRF + DETAIL
                || options.containerClass === BODY_PRF + DETAIL) {
                let maxWidth = calcWidth(options.columns);
                style = wrapperStyles(top, left, options.width, options.height, maxWidth + "px"); 
            } else {
                style = wrapperStyles(top, left, options.width, options.height);
            }
            return selector.create("div").data(internal.EX_PART, options.containerClass)
                        .addClass(options.containerClass)
                        .css(style).getSingle();
        }
        
        /**
         * CalcWidth.
         */
        function calcWidth(columns: Array<any>) {
            let width = 0;
            columns.forEach(function(c, i) {
                if (c.group) {
                    width += calcWidth(c.group);
                    return;
                }
                width += parseFloat(c.width);
            });
            return width;
        }
        
        /**
         * Grid cell.
         */
        export function gridCell($grid: HTMLElement, rowIdx: any, columnKey: any, innerIdx: any, valueObj: any, isRestore?: boolean) {
            let $exTable = helper.closest($grid, "." + NAMESPACE);
            let updateMode = helper.getExTableFromGrid($grid).updateMode;
            let $cell = selection.cellAt($grid, rowIdx, columnKey);
            if ($cell === intan.NULL) return;
            let origDs = helper.getOrigDS($grid);
            let gen = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
            let viewFn = gen.painter.options.view;
            let viewMode = gen.painter.options.viewMode;
            let value = valueObj;
            let fields = gen.painter.options.fields;
            if (_.isFunction(viewFn)) {
                value = helper.viewData(viewFn, viewMode, valueObj);
            }
            let touched = false;
            let $childCells = $cell.querySelectorAll("." + CHILD_CELL_CLS);
            if ($childCells.length > 0) {
                if (value.constructor === Array) {
                    _.forEach(value, function(val: any, i: number) {
                        let $c = $childCells[i];
                        $c.textContent = val;
                        let mTouch = trace(origDs, $c, rowIdx, columnKey, i, valueObj, fields);
                        if (!touched) touched = mTouch;
                        if (updateMode === EDIT) {
                            validation.validate($grid, $c, rowIdx, columnKey, i, val);
                        }
                    });
                } else {
                    let $c = $childCells[innerIdx];
                    $c.textContent = value;
                    touched = trace(origDs, $c, rowIdx, columnKey, innerIdx, valueObj, fields);
                    if (updateMode === EDIT) {
                        validation.validate($grid, $c, rowIdx, columnKey, innerIdx, value);
                    }
                }
            } else {
                $cell.textContent = value;
                touched = trace(origDs, $cell, rowIdx, columnKey, -1, valueObj, fields);
                if (updateMode === EDIT) {
                    validation.validate($grid, $cell, rowIdx, columnKey, -1, value);
                }
            }
            return touched;
        }
        
        /**
         * Grid row.
         */
        export function gridRow($grid: HTMLElement, rowIdx: any, data: any, isRestore?: boolean) {
            let $exTable = helper.closest($grid, "." + NAMESPACE);
            let updateMode = helper.getExTableFromGrid($grid).updateMode;
            let $row = selection.rowAt($grid, rowIdx);   
            let $cells = Array.prototype.slice.call($row.querySelectorAll("td")).filter(function(e) {
                return e.style.display !== "none";
            });
            let visibleColumns = helper.gridVisibleColumns($grid);
            let origDs = helper.getOrigDS($grid);
            let gen = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
            let viewFn = gen.painter.options.view;
            let viewMode = gen.painter.options.viewMode;
            let fields = gen.painter.options.fields;
            _.forEach(Object.keys(data), function(key: any) {
                _.forEach(visibleColumns, function(col: any, index: number) {
                    if (col.key === key) {
                        let $target = $cells[index];
                        let childCells = $target.querySelectorAll("." + CHILD_CELL_CLS);
                        if (childCells.length > 0) {
                            let cData = data[key];
                            if (_.isFunction(viewFn)) {
                                cData = helper.viewData(viewFn, viewMode, data[key]);
                            }
                            if (cData.constructor === Array) {
                                _.forEach(cData, function(d, i) {
                                    let $c = childCells[i];
                                    $c.textContent = d;
                                    if (updateMode === EDIT) {
                                        validation.validate($exTable, $grid, $c, rowIdx, key, i, d);
                                    }
                                    trace(origDs, $c, rowIdx, key, i, data[key], fields);
                                });
                                return false;
                            }
                            childCells[1].textContent = data[key];
                            trace(origDs, childCells[1], rowIdx, key, 1, data[key], fields);
                            if (updateMode === EDIT) {
                                validation.validate($exTable, $grid, childCells[1], rowIdx, key, 1, data[key]);
                            }
                        } else {
                            let cData = data[key];
                            if (_.isFunction(viewFn)) {
                                cData = helper.viewData(viewFn, viewMode, data[key]);
                            }
                            $target.textContent = cData;
                            trace(origDs, $target, rowIdx, key, -1, data[key], fields);
                            if (updateMode === EDIT) {
                                validation.validate($exTable, $grid, $target, rowIdx, key, -1, data[key]);
                            }
                        }
                        return false;
                    } 
                });
            });
        }
        
        /**
         * Trace.
         */
        function trace(ds: Array<any>, $cell: HTMLElement, rowIdx: any, key: any, innerIdx: any, value: any, fields?: any) {
            if (!ds || ds.length === 0) return;
            let oVal = ds[rowIdx][key];
            
            if (!util.isNullOrUndefined(oVal) && helper.isEqual(oVal, value, fields)) {
                helper.removeClass($cell, update.EDITED_CLS);
                return false;
            }
            helper.addClass($cell, update.EDITED_CLS);
            return true;
        }
    }
    
    module intan {
        export let TOP_SPACE = "top-space";
        export let BOTTOM_SPACE = "bottom-space";
        export let NULL = null;
        export class Cloud {
            $container: HTMLElement;
            options: any;
            rowsOfBlock: number;
            blocksOfCluster: number;
            rowHeight: number;
            blockHeight: number;
            clusterHeight: number;
            _origDs: Array<any>;
            dataSource: Array<any>;
            primaryKey: string;
            topOffset: number;
            bottomOffset: number;
            currentCluster: number;
            startIndex: number;
            endIndex: number;
            painter: render.Painter;
            
            constructor($container: HTMLElement, dataSource: Array<any>, options: any) {
                this.$container = $container;
                this.options = options;
                this.primaryKey = options.primaryKey;
                this.rowsOfBlock = options.rowsOfBlock || 30;
                this.blocksOfCluster = options.blocksOfCluster || 3;
                this.rowHeight = parseInt(options.rowHeight);
                this.blockHeight = this.rowsOfBlock * this.rowHeight;
                this.clusterHeight = this.blockHeight * this.blocksOfCluster;
                this.dataSource = dataSource;
                this._origDs = _.cloneDeep(dataSource);
                this.painter = new render.Painter($container, options);
                this.setCellsStyle();
                this.onScroll();
            }
            
            /**
             * Set cells style.
             */
            setCellsStyle() {
                let self = this;
                let bodyStylesFt = feature.find(self.options.features, feature.BODY_CELL_STYLE);
                if (!bodyStylesFt) return;
                $.data(self.$container, internal.CELLS_STYLE, bodyStylesFt.decorator);
            }
            
            /**
             * Get cluster no.
             */
            getClusterNo() {
                return Math.floor(this.$container.scrollTop / (this.clusterHeight - this.blockHeight));
            }
            
            /**
             * Render rows.
             */
            renderRows(manual?: boolean) {
                let self = this;
                let clusterNo = self.getClusterNo();
                if (manual) self.currentCluster = clusterNo;
                if (self.dataSource.length < self.rowsOfBlock) {
                    self.topOffset = 0;
                    self.bottomOffset = 0;
                }
                
                let rowsOfCluster = self.blocksOfCluster * self.rowsOfBlock;
                let startRowIdx = self.startIndex = Math.max((rowsOfCluster - self.rowsOfBlock) * clusterNo, 0);
                let endRowIdx = self.endIndex = startRowIdx + rowsOfCluster;
                self.topOffset = Math.max(startRowIdx * self.rowHeight, 0);
                self.bottomOffset = Math.max((self.dataSource.length - endRowIdx) * self.rowHeight, 0);
                let rowConfig = { css: { height: self.rowHeight } };
                
                let containerElm = self.$container;
                let tbody = document.createElement("tbody");
                tbody.appendChild(render.extra(TOP_SPACE, self.topOffset));
                for (let i = startRowIdx; i < endRowIdx; i++) {
                    if (util.isNullOrUndefined(this.dataSource[i])) continue;
                    tbody.appendChild(self.painter.row(this.dataSource[i], rowConfig, i));
                } 
                tbody.appendChild(render.extra(BOTTOM_SPACE, self.bottomOffset));
                containerElm.querySelector("table").replaceChild(tbody, containerElm.getElementsByTagName("tbody")[0]); 
                
                if (self.$container.classList.contains(BODY_PRF + DETAIL)) {
                    self.selectCellsIn();
                    self.dirtyCellsIn();
                    self.errorCellsIn();
                    self.detCellsIn();
                    self.editCellIn();
                } else if (self.$container.classList.contains(BODY_PRF + LEFTMOST)) {
                    self.selectedRowsIn();
                    self.dirtyCellsIn();
                    self.errorCellsIn();
                    self.editCellIn();
                }
                setTimeout(function() {
                    events.trigger(self.$container, events.RENDERED);
                }, 0);
            }
            
            /**
             * OnScroll.
             */
            onScroll() {
                let self = this;
                self.$container.removeXEventListener(events.SCROLL_EVT + ".detail");
                self.$container.addXEventListener(events.SCROLL_EVT + ".detail", function() {
                    let inClusterNo = self.getClusterNo();
                    if (self.currentCluster !== inClusterNo) {
                        self.currentCluster = inClusterNo;
                        if (self.$container.classList.contains(BODY_PRF + DETAIL)) {
                            let colIn = $.data(self.$container, internal.COLUMN_IN);
                            if (!util.isNullOrUndefined(colIn) && colIn !== -1) {
                                helper.unHighlightGrid(selector.classSiblings(self.$container, HEADER_PRF + DETAIL)[0], colIn);
                                let $sumHeader = selector.classSiblings(self.$container, HEADER_PRF + HORIZONTAL_SUM);
                                let $sumBody = selector.classSiblings(self.$container, BODY_PRF + HORIZONTAL_SUM);
                                if ($sumHeader.length > 0 && $sumHeader[0].style.display !== "none") {
                                    helper.unHighlightGrid($sumHeader[0], colIn);
                                    helper.unHighlightGrid($sumBody[0], colIn);
                                }
                                events.trigger(helper.closest(self.$container, "." + NAMESPACE), events.MOUSEOUT_COLUMN, colIn);
                            }
                        }
                        self.renderRows();
                    }
                });
            }
            
            /**
             * Roll to.
             */
            rollTo(cell: any) {
                let self = this;
                if (self.startIndex <= cell.rowIndex && self.endIndex >= cell.rowIndex) {
                    let $cell = selection.cellAt(self.$container, cell.rowIndex, cell.columnKey);
                    let tdIndex = selector.index($cell);
                    let tdPosLeft = 0, tdPosTop = 0;
                    selector.siblingsLt($cell, tdIndex).forEach(function(e) {
                        if (e.style.display !== "none") {
                            tdPosLeft += e.offsetWidth;
                        }
                    });
                    let $tr = $cell.parentElement;
                    let trIndex = selector.index($tr);
                    selector.siblingsLt($tr, trIndex).forEach(function(e) {
                        tdPosTop += e.offsetHeight;
                    });
                    if ((self.$container.scrollTop + parseFloat(self.$container.style.height)) < (tdPosTop + 100)
                        || self.$container.scrollTop > tdPosTop) {
                        self.$container.scrollTop = tdPosTop;
                    }
                    if ((self.$container.scrollLeft + parseFloat(self.$container.style.width)) < (tdPosLeft + 100)
                        || self.$container.scrollLeft > tdPosLeft) {
                        self.$container.scrollLeft = tdPosLeft;
                    }
                } else {
                    self.$container.scrollTop = cell.rowIndex * self.rowHeight;
                    let $cell = selection.cellAt(self.$container, cell.rowIndex, cell.columnKey);
                    let tdPosLeft = 0;
                    selector.siblingsLt($cell, selector.index($cell)).forEach(function(e) {
                        if (e.style.display !== "none") {
                            tdPosLeft += e.offsetWidth;
                        }
                    });
                    self.$container.scrollLeft = tdPosLeft;
                }
            }
            
            /**
             * Edit cell in.
             */
            editCellIn() {
                let self = this;
                let $exTable = helper.closest(self.$container, "." + NAMESPACE);
                let updateMode = $.data($exTable, NAMESPACE).updateMode;
                let editor = $.data($exTable, update.EDITOR);
                if (updateMode !== EDIT || util.isNullOrUndefined(editor) || editor.land !== self.options.containerClass) return;
                let editorRowIdx = parseInt(editor.rowIdx);
                if (util.isNullOrUndefined(editor) || editorRowIdx < self.startIndex || editorRowIdx > self.endIndex) return;
                let $editRow = self.$container.querySelectorAll("tr")[editorRowIdx - self.startIndex + 1];
                let editorColumnIdx;
                _.forEach(self.painter.visibleColumns, function(c: any, idx: number) {
                    if (c.key === editor.columnKey) {
                        editorColumnIdx = idx;
                        return false;
                    }
                });
                if (!util.isNullOrUndefined(editorColumnIdx)) {
                    let $editorCell = Array.prototype.slice.call($editRow.getElementsByTagName("td")).filter(function(e) {
                        return e.style.display !== "none";
                    })[editorColumnIdx]; 
                    let $childCells = $editorCell.querySelectorAll("." + render.CHILD_CELL_CLS);
                    update.edit($exTable, !util.isNullOrUndefined(editor.innerIdx) 
                                            && editor.innerIdx > -1
                                            && $childCells.length > 0 
                                            ? $childCells[editor.innerIdx] : $editorCell, editor.land, editor.value, true);
                }
            }
            
            /**
             * Select cells in.
             */
            selectCellsIn() {
                let self = this;
                let $exTable = helper.closest(self.$container, "." + NAMESPACE);
                let updateMode = $.data($exTable, NAMESPACE).updateMode;
                if (updateMode !== COPY_PASTE) return;
                let selectedCells = $.data(self.$container, internal.SELECTED_CELLS);
                if (util.isNullOrUndefined(selectedCells) || selectedCells.length === 0) return;
                _.forEach(Object.keys(selectedCells), function(rowIdx: any, index: number) {
                    if (rowIdx >= self.startIndex && rowIdx <= self.endIndex) {
                        _.forEach(selectedCells[rowIdx], function(colKey: any) {
                            let $cell = selection.cellAt(self.$container, rowIdx, colKey);
                            if ($cell === intan.NULL || !$cell) return;
                            selection.markCell($cell);
                        });
                    }
                });
            }
            
            /**
             * Select rows in.
             */
            selectedRowsIn() {
                let self = this;
                let selectedRows = $.data(self.$container, internal.SELECTED_ROWS);
                if (!selectedRows || !selectedRows.items || selectedRows.items.length === 0) return;
                for (let i = self.startIndex; i <= self.endIndex; i++) {
                    if (selectedRows.items[i]) {
                        controls.tick(true, self.$container, false, i);
                    }
                }
            }
            
            /**
             * Dirty cells in.
             */
            dirtyCellsIn() {
                let self = this;
                let $exTable = helper.closest(self.$container, "." + NAMESPACE);
                let updateMode = $.data($exTable, NAMESPACE).updateMode;
                let histories;
                if (self.options.containerClass === BODY_PRF + LEFTMOST) {
                    histories = $.data(self.$container, internal.EDIT_HISTORY);
                    if (!histories) return;
                    self.each(histories);
                    return;
                }
                if (updateMode === COPY_PASTE) {
                    histories = $.data(self.$container, internal.COPY_HISTORY);
                    if (!histories) return;
                    for(let i = histories.length - 1; i >= 0; i--) {
                        self.each(histories[i].items);
                    }
                } else if (updateMode === EDIT) {
                    histories = $.data(self.$container, internal.EDIT_HISTORY);
                    if (!histories) return;
                    self.each(histories);
                } else if (updateMode === STICK) {
                    histories = $.data(self.$container, internal.STICK_HISTORY);
                    if (!histories) return;
                    _.forEach(histories, function(items: any) {
                        self.each(items);
                    });
                }
            }
            
            /**
             * Error cells in.
             */
            errorCellsIn() {
                let self = this;
                let $exTable = helper.closest(self.$container, "." + NAMESPACE);
                let updateMode = $.data($exTable, NAMESPACE).updateMode;
                let errs = $.data($exTable, errors.ERRORS);
                if (!errs || errs.length === 0) return;
                self.each(errs, errors.ERROR_CLS);
            }
            
            /**
             * Det cells in.
             */
            detCellsIn() {
                let self = this;
                let det = $.data(self.$container, internal.DET);
                if (!det) return;
                _.forEach(Object.keys(det), function(rIdx: any) {
                    if (rIdx >= self.startIndex && rIdx <= self.endIndex) {
                        _.forEach(det[rIdx], function(key: any) {
                            let $cell = selection.cellAt(self.$container, rIdx, key);
                            if ($cell === intan.NULL || !$cell) return;
                            helper.markCellWith(style.DET_CLS, $cell);
                        });
                    }
                });
            }
            
            /**
             * Each.
             */
            each(items: any, styler?: any) {
                let self = this;
                styler = styler || update.EDITED_CLS;
                _.forEach(items, function(item: any) {
                    if (item.rowIndex >= self.startIndex && item.rowIndex <= self.endIndex) {
                        let $cell = selection.cellAt(self.$container, item.rowIndex, item.columnKey);
                        if ($cell === intan.NULL || !$cell) return;
                        helper.markCellWith(styler, $cell, item.innerIdx, item.value);
                    }
                });
            }
        }
    }
    
    module cellHandler {
        export let ROUND_GO: string = "ex-round-go";
        
        export function get(handlerType: string) {
            switch (handlerType.toLowerCase()) {
                case "input": 
                    return cellInput;
                case "tooltip":
                    return tooltip;
                case "roundtrip":
                    return roundGo;
            }
        }
        
        /**
         * Cell input.
         */
        export function cellInput($cell: HTMLElement, options: any, supplier?: any) {
            if (util.isNullOrUndefined(options.updateMode) || options.updateMode !== EDIT) return;
            $cell.classList.add(update.EDITABLE_CLS);
            $cell.addXEventListener(events.CLICK_EVT, function(evt: any) {
                if ($cell.getElementsByTagName("input").length > 0) {
                    evt.stopImmediatePropagation();
                    return;
                }
                let $exTable = helper.closest($cell, "." + NAMESPACE);
                if (evt.ctrlKey && $.data($exTable, NAMESPACE).determination) return;
                update.edit($exTable, $cell, options.containerClass);
            });
        }
        
        /**
         * Tooltip.
         */
        export function tooltip($cell: HTMLElement, options: any, supplier?: any) {
            let $content = supplier();
            if (util.isNullOrUndefined($content)) return;
            new widget.Tooltip($cell, { sources: $content });
        }
        
        /**
         * Round go.
         */
        export function roundGo($cell: HTMLElement, options: any, supplier?: any) {
            if (!supplier || typeof supplier !== "function") return;
            $cell.classList.add(ROUND_GO);
            $cell.addXEventListener(events.CLICK_EVT, function(evt: any) {
                let $grid = helper.closest($cell, "table").parentElement;
                if (errors.occurred(helper.closest($grid, "." + NAMESPACE))) return false;
                let coord = helper.getCellCoord($cell);
                helper.closest($cell, "." + NAMESPACE).addXEventListener(events.ROUND_RETREAT, function(evt: any) {
                    if (evt.currentTarget.dataset.triggered) return;
                    let value: any = evt.detail;
                    let ds = helper.getDataSource($grid);
                    if (!ds || ds.length === 0) return;
                    if (ds[coord.rowIdx][coord.columnKey] !== value) {
                        update.gridCell($grid, coord.rowIdx, coord.columnKey, -1, value);
                        update.pushEditHistory($grid, new selection.Cell(coord.rowIdx, coord.columnKey, value, -1));
                    }
                    evt.currentTarget.dataset.triggered = true;
                });
                supplier();
            });
        }
        
        /**
         * rClick.
         */
        export function rClick(cell: HTMLElement, column: any, cb: any) {
            if (util.isNullOrUndefined(column.rightClick) || typeof column.rightClick !== "function") return;
            cell.addXEventListener(events.MOUSE_DOWN, function(evt: any) {
                if (evt.which === 3 || evt.button === 2) {
                    evt.preventDefault();
                    cb();
                }
            });
            cell.addXEventListener(events.CM, function() {
                return false;
            });
        }
        
        /**
         * rRowClick.
         */
        export function rRowClick(row: HTMLElement, columnsMap: any, args: any) {
            row.addXEventListener(events.MOUSE_DOWN, function(evt: any) {
                let coord = helper.getCellCoord(evt.target);
                if (!coord) return;
                let column = columnsMap[coord.columnKey];
                if (util.isNullOrUndefined(column.rightClick) || typeof column.rightClick !== "function") return;
                if (evt.which === 3 || evt.button === 2) {
                    evt.preventDefault();
                    column.rightClick(args.rData, args.rowIdx, coord.columnKey);
                }
            });
            row.addXEventListener(events.CM, function() {
                return false;
            });
        }
    }
    
    module update {
        export let EDITOR = "editor";
        export let EDITED_CLS = "edited";
        export let EDIT_CELL_CLS = "edit-cell";
        export let EDITOR_CLS = "ex-editor";
        export let EDITABLE_CLS = "ex-editable";
        let EMPTY_TBL_HEIGHT: string = "1px";
        
        export class Editor {
            $editor: HTMLElement;
            land: any;
            rowIdx: any;
            columnKey: any;
            innerIdx: any;
            value: any;
            constructor($editor: HTMLElement, land: any, rowIdx: any, columnKey: any, innerIdx: any, value: any) {
                this.$editor = $editor;
                this.land = land;
                this.rowIdx = rowIdx;
                this.columnKey = columnKey;
                this.innerIdx = innerIdx;
                this.value = value;
            }
        }
        
        /**
         * Edit.
         */
        export function edit($exTable: HTMLElement, $cell: HTMLElement, land: any, value?: any, forced?: boolean) {
            let $grid = $exTable.querySelector("." + BODY_PRF + DETAIL);
            let $body = !land ? $grid : helper.getTable($exTable, land);
            let exTable = $.data($exTable, NAMESPACE);
            if (!forced && (errors.occurred($exTable) || selector.is($cell, "." + style.DET_CLS)
                || (land === BODY_PRF + DETAIL && exTable.detailContent.banEmptyInput 
                    && exTable.detailContent.banEmptyInput.some(m => m === exTable.viewMode)
                    && $cell.textContent === "")
                || selector.is($cell, "." + style.HIDDEN_CLS) || selector.is($cell, "." + style.SEAL_CLS))) {
                outsideClick($exTable, $cell, true);
                return;
            }
            let editor = $.data($exTable, EDITOR);
            let $editor: HTMLDivElement, $input: HTMLInputElement, inputVal, innerIdx = -1;
            let coord = helper.getCellCoord($cell);
            if (util.isNullOrUndefined(editor)) {
                let content = $cell.textContent;
                inputVal = value ? value : content;
                $input = document.createElement("input");
                $input.style.border = "none";
                $input.style.width = "96%";
                $input.style.height = "92%";
                $input.style.outline = "none";
                $input.value = inputVal;
                
                $editor = document.createElement("div");
                $editor.className = EDITOR_CLS;
                $editor.style.height = ($cell.offsetHeight - 4) + "px";
                $editor.style.width = ($cell.offsetWidth - 4) + "px";
                $editor.style.backgroundColor = "#FFF";
                $editor.style.border = "solid 1px #E67E22";
                $editor.appendChild($input);
                if (selector.is($cell, "div")) {
                    $editor.style.height = ($cell.offsetHeight - 4) + "px";
                    $editor.style.width = ($cell.offsetWidth - 4) + "px";
                    innerIdx = selector.index($cell);
                }
                $.data($exTable, EDITOR, new Editor($editor, land, coord.rowIdx, coord.columnKey, innerIdx, inputVal));
                events.trigger($exTable, events.START_EDIT, [ $editor, content ]);
                helper.addClass($cell, EDIT_CELL_CLS);
                $cell.innerHTML = "";
                $cell.appendChild($editor);
                editing($exTable, $editor, land);
                $input.select();
                validation.validate($body, $cell, coord.rowIdx, coord.columnKey, innerIdx, inputVal);
                selection.tickRows($exTable.querySelector("." + BODY_PRF + LEFTMOST), false);
            } else {
                $editor = editor.$editor;
                $input = $editor.querySelector("input");
                let content = $input.value;
                let editingLand = editor.land;
                let cont = function(cb?: any) {
                    if ($editor.style.display === "none") $editor.style.display = "";
                    if (selector.is($cell, "div")) {
                        $editor.style.height = ($cell.offsetHeight - 4) + "px";
                        $editor.style.width = ($cell.offsetWidth - 4) + "px";
                        innerIdx = selector.index($cell);
                    } else {
                        $editor.style.height = ($cell.offsetHeight - 4) + "px";
                        $editor.style.width = ($cell.offsetWidth - 4) + "px";
                    }
                    let $editingCell = helper.closest($editor, "." + EDIT_CELL_CLS);
                    helper.removeClass($editingCell, EDIT_CELL_CLS);
                    let cellText = $cell.textContent;
                    inputVal = value ? value : cellText;
                    $input.value = inputVal;
                    triggerStopEdit($exTable, $editingCell, editingLand, content);
                    if (cb && _.isFunction(cb)) {
                        cb();
                    }
                    // Update editing cell coord
                    editor.land = land;
                    editor.rowIdx = coord.rowIdx;
                    editor.columnKey = coord.columnKey;
                    editor.innerIdx = innerIdx;
                    editor.value = inputVal;
                    helper.addClass($cell, EDIT_CELL_CLS)
                    $cell.innerHTML = "";
                    $cell.appendChild($editor);
                    editing($exTable, $editor, land);
                    $input.select();
                    validation.validate($body, $cell, coord.rowIdx, coord.columnKey, innerIdx, inputVal);
                    selection.tickRows($exTable.querySelector("." + BODY_PRF + LEFTMOST), false);
                };
                
                let $editingGrid = !editingLand ? helper.getMainTable($exTable) : helper.getTable($exTable, editor.land);
                let visibleColumns = helper.getVisibleColumnsOn($editingGrid); 
                let columnDf;
                _.forEach(visibleColumns, function(col: any) {
                    if (col.key === editor.columnKey) {
                        columnDf = col;
                        return false;
                    }
                });
                if (!columnDf) return;
                if (columnDf.ajaxValidate && _.isFunction(columnDf.ajaxValidate.request)) {
                    helper.block($exTable);
                    columnDf.ajaxValidate.request(content).done(function(res) {
                        cont(helper.call(columnDf.ajaxValidate.onValid, 
                            { rowIndex: editor.rowIdx, columnKey: editor.columnKey, innerIdx: editor.innerIdx }, res));
                    }).fail(function(res) {
                        let $target = selection.cellAt($editingGrid, editor.rowIdx, editor.columnKey);
                        if ($target !== intan.NULL) {
                            errors.add($exTable, $target, editor.rowIdx, editor.columnKey, editor.innerIdx, editor.value);
                        }
                        if (_.isFunction(columnDf.ajaxValidate.onFailed)) {
                            columnDf.ajaxValidate.onFailed({ rowIndex: editor.rowIdx, columnKey: editor.columnKey, innerIdx: editor.innerIdx }, res);
                        }
                    }).always(function() {
                        helper.unblock($exTable);
                    });
                    return;
                }
                cont();
            }
        }
        
        /**
         * Editing.
         */
        function editing($exTable: HTMLElement, $editor: HTMLElement, land: any) {
            let $input = $editor.querySelector("input");
            $input.removeXEventListener(events.KEY_UP);
            $input.addXEventListener(events.KEY_UP, function(evt: any) {
                let value = $input.value;
                if (evt.keyCode === $.ui.keyCode.ENTER) {
                    let $grid;
                    if (!land) { 
                        $grid = helper.getMainTable($exTable);
                    } else {
                        $grid = $exTable.querySelector("." + land);
                    }
                    let editor = $.data($exTable, EDITOR);
                    if (errors.occurred($exTable) || !editor) return;
                    let visibleColumns = helper.getVisibleColumnsOn(!editor.land ? helper.getMainTable($exTable) : helper.getTable($exTable, editor.land)); 
                    let columnDf;
                    _.forEach(visibleColumns, function(col: any) {
                        if (col.key === editor.columnKey) {
                            columnDf = col;
                            return false;
                        }
                    });
                    if (!columnDf) return;
                    if (columnDf.ajaxValidate && _.isFunction(columnDf.ajaxValidate.request)) {
                        helper.block($exTable);
                        columnDf.ajaxValidate.request(value).done(function(res) {
                            let $parent = $editor.parentElement;
                            helper.removeClass($parent, EDIT_CELL_CLS);
                            let currentCell = new selection.Cell(editor.rowIdx, editor.columnKey, undefined, editor.innerIdx);
                            $.data($exTable, EDITOR, null);
                            triggerStopEdit($exTable, $parent, land, value);
                            if (_.isFunction(columnDf.ajaxValidate.onValid)) {
                                columnDf.ajaxValidate.onValid({ rowIndex: editor.rowIdx, columnKey: editor.columnKey, innerIdx: editor.innerIdx }, res);
                            }
                            if (land !== BODY_PRF + DETAIL) return;
                            let cell = helper.nextCellOf($grid, currentCell);
                            internal.getGem($grid).rollTo(cell);
                            _.defer(function() {
                                let $cell = selection.cellAt($grid, cell.rowIndex, cell.columnKey);
                                if (util.isNullOrUndefined(cell.innerIdx) || cell.innerIdx === -1) {
                                    edit($exTable, $cell, land);
                                    return;
                                }
                                edit($exTable, $cell.querySelectorAll("." + render.CHILD_CELL_CLS)[cell.innerIdx], land);
                            });
                        }).fail(function(res) {
                            let $target = selection.cellAt($grid, editor.rowIdx, editor.columnKey);
                            if ($target !== intan.NULL) {
                                errors.add($exTable, $target, editor.rowIdx, editor.columnKey, editor.innerIdx, editor.value);
                            }
                            if (_.isFunction(columnDf.ajaxValidate.onFailed)) {
                                columnDf.ajaxValidate.onFailed({ rowIndex: editor.rowIdx, columnKey: editor.columnKey, innerIdx: editor.innerIdx }, res);
                            }
                        }).always(function() {
                            helper.unblock($exTable);
                        });
                    } else {
                        let $parent = $editor.parentElement;
                        helper.removeClass($parent, EDIT_CELL_CLS);
                        let currentCell = new selection.Cell(editor.rowIdx, editor.columnKey, undefined, editor.innerIdx);
                        $.data($exTable, EDITOR, null);
                        triggerStopEdit($exTable, $parent, land, value);
                        if (land !== BODY_PRF + DETAIL) return;
                        let cell = helper.nextCellOf($grid, currentCell);
                        internal.getGem($grid).rollTo(cell);
                        _.defer(function() {
                            let $cell = selection.cellAt($grid, cell.rowIndex, cell.columnKey);
                            if (util.isNullOrUndefined(cell.innerIdx) || cell.innerIdx === -1) {
                                edit($exTable, $cell, land);
                                return;
                            }
                            edit($exTable, $cell.querySelectorAll("." + render.CHILD_CELL_CLS)[cell.innerIdx], land);
                        });
                    }
                } else {
                    let editor = $.data($exTable, EDITOR);
                    if (util.isNullOrUndefined(editor)) return;
                    editor.value = value;
                    let $grid = !editor.land ? helper.getMainTable($exTable) : helper.getTable($exTable, editor.land);
                    validation.validate($grid, helper.closest(editor.$editor, "." + EDIT_CELL_CLS), 
                                editor.rowIdx, editor.columnKey, editor.innerIdx, editor.value);
                }
            });
        }
        
        /**
         * Trigger stop edit.
         */
        export function triggerStopEdit($exTable: HTMLElement, $cell: HTMLElement, land: any, value: any) {
            if ($cell.length === 0) return;
            let innerIdx = -1;
            if (selector.is($cell, "div")) {
                innerIdx = selector.index($cell);
            }
            let coord = helper.getCellCoord($cell);
            if (!coord) return;
            events.trigger($exTable, events.STOP_EDIT, 
                            { land: land, rowIndex: coord.rowIdx, columnKey: coord.columnKey, innerIdx: innerIdx, value: value });
        }
        
        /**
         * Edit done.
         */
        export function editDone($exTable: HTMLElement) {
            let $grid = $exTable.querySelector("." + BODY_PRF + DETAIL);
            let fts = $.data($exTable, NAMESPACE).detailContent.features;
            let timeRangeFt = feature.find(fts, feature.TIME_RANGE);
            let timeRangerDef;
            if (!util.isNullOrUndefined(timeRangeFt)) {
                timeRangerDef = _.groupBy(timeRangeFt.ranges, "rowId");
                $.data($grid, internal.TIME_VALID_RANGE, timeRangerDef);
            }
            $exTable.addXEventListener(events.STOP_EDIT, function(evt: any) {
                let ui: any = evt.detail;
                postEdit($exTable, ui, timeRangerDef);
            });
        }
        
        /**
         * Post edit.
         */
        function postEdit($exTable: HTMLElement, ui: any, timeRangerDef?: any) {
            let $body = !ui.land ? $exTable.querySelector("." + BODY_PRF + DETAIL) : $exTable.querySelector("." + ui.land);
            let $cell = selection.cellAt($body, ui.rowIndex, ui.columnKey);
            let result = validation.validate($body, $cell, ui.rowIndex, ui.columnKey, ui.innerIdx, ui.value, timeRangerDef);
            if (!result.isValid) return;
            ui.value = result.value;
            
            let res = cellData($exTable, ui);
            if (!util.isNullOrUndefined(res)) {
                let newValObj = ui.value;
                if (_.isObject(res)) {
                    let $main = helper.getMainTable($exTable);
                    let gen = $.data($main, internal.TANGI) || $.data($main, internal.CANON);
                    let upperInput = gen.painter.options.upperInput;
                    let lowerInput = gen.painter.options.lowerInput;
                    newValObj = _.cloneDeep(res);
                    if (ui.innerIdx === 0) {
                        newValObj[upperInput] = ui.value;
                    } else if (ui.innerIdx === 1) {
                        newValObj[lowerInput] = ui.value;
                    }
                }
                pushEditHistory($body, new selection.Cell(ui.rowIndex, ui.columnKey, res, ui.innerIdx));
                helper.markCellWith(EDITED_CLS, $cell, ui.innerIdx);
                events.trigger($exTable, events.CELL_UPDATED, new selection.Cell(ui.rowIndex, ui.columnKey, newValObj, ui.innerIdx));
            }
            setText($cell, ui.innerIdx, ui.value);
        }
        
        /**
         * Set text.
         */
        export function setText($cell: HTMLElement, innerIdx: any, value: any) {
            let $childCells = $cell.querySelectorAll("." + render.CHILD_CELL_CLS);
            if (!util.isNullOrUndefined(innerIdx) && innerIdx > -1 && $childCells.length > 0) {
                $childCells[innerIdx].textContent = value;
            } else {
                $cell.textContent = value;
            }
        }
        
        /**
         * Outside click.
         */
        export function outsideClick($exTable: HTMLElement, $target: HTMLElement, immediate?: boolean) {
            if (immediate || !selector.is($target, "." + update.EDITABLE_CLS)) {
                if ($.data($exTable, "blockUI.isBlocked") === 1 || errors.occurred($exTable)) return;
                let editor = $.data($exTable, update.EDITOR);
                if (util.isNullOrUndefined(editor)) return;
                
                let $input = editor.$editor.querySelector("input");
                let content = $input.value;
                let mo = function(cb?: any) {
                    let innerIdx = -1;
                    let $parent = helper.closest(editor.$editor, "." + update.EDITABLE_CLS);
                    helper.removeClass($parent, update.EDIT_CELL_CLS);
                    let $g = helper.closest($parent, "table").parentElement;
                    if (!$parent || !$g) return; 
                    if (selector.is($parent, "div")) innerIdx = selector.index($parent);
                    $parent.textContent = content;
                    postEdit($exTable, { rowIndex: editor.rowIdx, columnKey: editor.columnKey, innerIdx: innerIdx, 
                                        value: content, land: ($.data($g, internal.TANGI) || $.data($g, internal.CANON)).painter.options.containerClass });
                    if (cb && _.isFunction(cb)) {
                        cb();
                    }
                    $.data($exTable, update.EDITOR, null);
                };
                let $grid = !editor.land ? helper.getMainTable($exTable) : helper.getTable($exTable, editor.land);
                let visibleColumns = helper.getVisibleColumnsOn($grid); 
                let columnDf;
                _.forEach(visibleColumns, function(col: any) {
                    if (col.key === editor.columnKey) {
                        columnDf = col;
                        return false;
                    }
                });
                if (!columnDf) return;
                if (!selector.is($target, "." + cellHandler.ROUND_GO) 
                    && columnDf.ajaxValidate && _.isFunction(columnDf.ajaxValidate.request)) {
                    helper.block($exTable);
                    columnDf.ajaxValidate.request(content).done(function(res) {
                        mo(helper.call(columnDf.ajaxValidate.onValid, 
                            { rowIndex: editor.rowIdx, columnKey: editor.columnKey, innerIdx: editor.innerIdx }, res))
                    }).fail(function(res) {
                        let $target = selection.cellAt($grid, editor.rowIdx, editor.columnKey);
                        if ($target !== intan.NULL) {
                            errors.add($exTable, $target, editor.rowIdx, editor.columnKey, editor.innerIdx, editor.value);
                        }
                        if (_.isFunction(columnDf.ajaxValidate.onFailed)) {
                            columnDf.ajaxValidate.onFailed({ rowIndex: editor.rowIdx, columnKey: editor.columnKey, innerIdx: editor.innerIdx }, res);
                        }
                    }).always(function() {
                        helper.unblock($exTable);
                    });
                    return;
                }
                mo();
            }
        }
        
        /**
         * Cell data.
         */
        export function cellData($exTable: HTMLElement, ui: any) {
            let exTable = $.data($exTable, NAMESPACE);
            if (!exTable) return;
            let oldVal, innerIdx = ui.innerIdx, f;
            if (ui.land === BODY_PRF + LEFTMOST) {
                f = "leftmostContent";
            } else if (ui.land === BODY_PRF + MIDDLE) {
                f = "middleContent";
            } else {
                f = "detailContent";
            }
            if (util.isNullOrUndefined(ui.innerIdx)) {
                innerIdx = exTable[f].dataSource[ui.rowIndex][ui.columnKey].constructor === Array ? 1 : -1;     
            }
            let currentVal = exTable[f].dataSource[ui.rowIndex][ui.columnKey];
            if (innerIdx === -1) {
                if (currentVal !== ui.value) {
                    oldVal = _.cloneDeep(currentVal);
                    if (exTable[f].primaryKey === ui.columnKey) {
                        if (exTable.leftmostContent) {
                            exTable.leftmostContent.dataSource[ui.rowIndex][ui.columnKey] = ui.value;
                        }
                        if (exTable.middleContent) {
                            exTable.middleContent.dataSource[ui.rowIndex][ui.columnKey] = ui.value;
                        }
                        if (exTable.detailContent) {
                            exTable.detailContent.dataSource[ui.rowIndex][ui.columnKey] = ui.value;
                        }
                    } else {
                        exTable[f].dataSource[ui.rowIndex][ui.columnKey] = ui.value;
                    }
                    return oldVal;
                } 
                return null;
            }
            let $main = !ui.land ? helper.getMainTable($exTable) : helper.getTable($exTable, ui.land);
            let gen = $.data($main, internal.TANGI) || $.data($main, internal.CANON);
            let upperInput = gen.painter.options.upperInput;
            let lowerInput = gen.painter.options.lowerInput;
            let field;
            if (ui.innerIdx === 0) {
                field = upperInput;
            } else if (ui.innerIdx === 1) {
                field = lowerInput;
            }
            if (currentVal[field] !== ui.value && (!util.isNullOrUndefined(currentVal[field])
                || ui.value !== "")) {
                oldVal = _.cloneDeep(currentVal);
                exTable[f].dataSource[ui.rowIndex][ui.columnKey][field] = ui.value;
                return oldVal;
            }
            return null;
        }
        
        /**
         * Row data.
         */
        export function rowData($exTable: HTMLElement, rowIdx: any, data: any) {
            let exTable = $.data($exTable, NAMESPACE);
            if (!exTable) return;
            _.assignInWith(exTable.detailContent.dataSource[rowIdx], data, function(objVal, srcVal) {
                if (objVal.constructor === Array && srcVal.constructor !== Array) {
                    objVal[1] = srcVal;
                    return objVal;
                }
                return srcVal;
            });
        }
        
        /**
         * Grid cell.
         */
        export function gridCell($grid: HTMLElement, rowIdx: any, columnKey: any, innerIdx: any, value: any, isRestore?: boolean) {
            let gen = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
            if (!gen) return;
            let cData = gen.dataSource[rowIdx][columnKey];
            let origDs = gen._origDs;
            let $table = helper.closest($grid, "." + NAMESPACE);
            if (cData.constructor === Array) {
                if (value.constructor === Array) {
                    _.forEach(cData, function(d: any, i: number) {
                        gen.dataSource[rowIdx][columnKey][i] = value[i];
                        if (!helper.isEqual(origDs[rowIdx][columnKey][i], value[i])) {
                            events.trigger($table, events.CELL_UPDATED, new selection.Cell(rowIdx, columnKey, value[i], i));
                        }
                    });
                } else {
                    gen.dataSource[rowIdx][columnKey][innerIdx] = value;
                    if (!helper.isEqual(origDs[rowIdx][columnKey][innerIdx], value)) {
                        events.trigger($table, events.CELL_UPDATED, new selection.Cell(rowIdx, columnKey, value, innerIdx));
                    }
                }
            } else {
                gen.dataSource[rowIdx][columnKey] = value;
                if (!helper.isEqual(origDs[rowIdx][columnKey], value)) {
                    events.trigger($table, events.CELL_UPDATED, new selection.Cell(rowIdx, columnKey, value, -1));
                }
            }
            render.gridCell($grid, rowIdx, columnKey, innerIdx, value, isRestore);
        }
        
        /**
         * Grid row.
         */
        export function gridRow($grid: HTMLElement, rowIdx: any, data: any, isRestore?: boolean) {
            let gen = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
            if (!gen) return;
            _.assignInWith(gen.dataSource[rowIdx], data, function(objVal, srcVal) {
                if (objVal.constructor === Array && srcVal.constructor !== Array) {
                    objVal[1] = srcVal;
                    return objVal;
                }
                return srcVal;
            });
            render.gridRow($grid, rowIdx, data, isRestore);
        }
        
        /**
         * Grid cell ow.
         */
        export function gridCellOw($grid: HTMLElement, rowIdx: any, columnKey: any, innerIdx: any, value: any, txId: any) {
            let $exTable = helper.closest($grid, "." + NAMESPACE);
            let exTable = $.data($exTable, NAMESPACE);
            let gen = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
            let pk = helper.getPrimaryKey($grid);
            if (!gen || helper.isDetCell($grid, rowIdx, columnKey)
                || helper.isXCell($grid, gen.dataSource[rowIdx][pk], columnKey, style.HIDDEN_CLS, style.SEAL_CLS)) return;
            let cData = gen.dataSource[rowIdx][columnKey];
            let opt = gen.options;
            if (!exTable.pasteOverWrite 
                && !helper.isEmpty(helper.viewData(opt.view, opt.viewMode, cData))) return;
            let changedData;
            if (cData.constructor === Array) {
                if (value.constructor === Array) {
                    changedData = _.cloneDeep(cData);
                    _.forEach(cData, function(d: any, i: number) {
                        gen.dataSource[rowIdx][columnKey][i] = value[i];
                    });
                } else {
                    changedData = cData[innerIdx];
                    gen.dataSource[rowIdx][columnKey][innerIdx] = value;
                }
            } else if (_.isObject(cData) && !_.isObject(value)) {
                return;
            } else {
                changedData = cData;
                gen.dataSource[rowIdx][columnKey] = value;
            }
            let touched = render.gridCell($grid, rowIdx, columnKey, innerIdx, value);
            if (touched) {
                pushHistory($grid, [ new selection.Cell(rowIdx, columnKey, changedData) ], txId);
                events.trigger($exTable, events.CELL_UPDATED, new selection.Cell(rowIdx, columnKey, value, innerIdx));
            }
        }
        
        /**
         * Grid row ow.
         */
        export function gridRowOw($grid: HTMLElement, rowIdx: any, data: any, txId: any) {
            let $exTable = helper.closest($grid, "." + NAMESPACE);
            let exTable = $.data($exTable, NAMESPACE);
            let gen = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
            let pk = helper.getPrimaryKey($grid);
            if (!gen) return;
            // Create history
            let changedCells = [];
            let origData = _.cloneDeep(data);
            let clonedData = _.cloneDeep(data);
            let opt = gen.options;
            _.assignInWith(gen.dataSource[rowIdx], clonedData, function(objVal, srcVal, key, obj, src) {
                if ((!exTable.pasteOverWrite 
                    && !helper.isEmpty(helper.viewData(opt.view, opt.viewMode, objVal)))
                    || helper.isDetCell($grid, rowIdx, key)
                    || helper.isXCell($grid, gen.dataSource[rowIdx][pk], key, style.HIDDEN_CLS, style.SEAL_CLS)) {
                    src[key] = objVal;
                    return objVal;
                }
                if (!util.isNullOrUndefined(src[key]) && !helper.isEqual(src[key], obj[key])) {
                    changedCells.push(new selection.Cell(rowIdx, key, objVal));
                } else {
                    delete origData[key];
                }
                if (objVal.constructor === Array && srcVal.constructor !== Array) {
                    objVal[1] = srcVal;
                    return objVal;
                }
                return srcVal;
            });
            _.forEach(Object.keys(clonedData), function(k: any) {
                if (!helper.isEqual(clonedData[k], origData[k])) {
                    delete origData[k];
                }
            });
            render.gridRow($grid, rowIdx, origData);
            if (changedCells.length > 0) {
                pushHistory($grid, changedCells, txId);
                events.trigger($exTable, events.ROW_UPDATED, events.createRowUi(rowIdx, origData));
            }
        }
        
        /**
         * Stick grid cell ow.
         */
        export function stickGridCellOw($grid: HTMLElement, rowIdx: any, columnKey: any, innerIdx: any, value: any) {
            let $exTable = helper.closest($grid, "." + NAMESPACE);
            let exTable = $.data($exTable, NAMESPACE);
            let gen = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
            let pk = helper.getPrimaryKey($grid);
            if (!gen || helper.isDetCell($grid, rowIdx, columnKey)
                || helper.isXCell($grid, gen.dataSource[rowIdx][pk], columnKey, style.HIDDEN_CLS, style.SEAL_CLS)) return;
            let cData = gen.dataSource[rowIdx][columnKey];
            let opt = gen.options;
            if (!exTable.stickOverWrite 
                && !helper.isEmpty(helper.viewData(opt.view, opt.viewMode, cData))) return;
            let changedData = _.cloneDeep(cData);
            gen.dataSource[rowIdx][columnKey] = value;
            let touched = render.gridCell($grid, rowIdx, columnKey, innerIdx, value);
            if (touched) {
                pushStickHistory($grid, [ new selection.Cell(rowIdx, columnKey, changedData, innerIdx) ]);
                events.trigger($exTable, events.CELL_UPDATED, new selection.Cell(rowIdx, columnKey, value, innerIdx));
            }
        }
        
        /**
         * Stick grid row ow.
         */
        export function stickGridRowOw($grid: HTMLElement, rowIdx: any, data: any) {
            let $exTable = helper.closest($grid, "." + NAMESPACE);
            let exTable = $.data($exTable, NAMESPACE);
            let gen = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
            let pk = helper.getPrimaryKey($grid);
            if (!gen) return;
            // Create history
            let changedCells = [];
            let origData = _.cloneDeep(data);
            let clonedData = _.cloneDeep(data);
            let opt = gen.options;
            _.assignInWith(gen.dataSource[rowIdx], clonedData, function(objVal, srcVal, key, obj, src) {
                if ((!exTable.stickOverWrite 
                    && !helper.isEmpty(helper.viewData(opt.view, opt.viewMode, objVal)))
                    || helper.isDetCell($grid, rowIdx, key)
                    || helper.isXCell($grid, gen.dataSource[rowIdx][pk], key, style.HIDDEN_CLS, style.SEAL_CLS)) {
                    src[key] = objVal;
                    return objVal;
                }
                if (!util.isNullOrUndefined(src[key]) && !helper.isEqual(src[key], obj[key])) {
                    changedCells.push(new selection.Cell(rowIdx, key, _.cloneDeep(objVal)));
                } else {
                    delete origData[key];
                }
                if (objVal.constructor === Array && srcVal.constructor !== Array) {
                    objVal[1] = srcVal;
                    return objVal;
                }
                return srcVal;
            });
            _.forEach(Object.keys(clonedData), function(k: any) {
                if (!helper.isEqual(clonedData[k], origData[k])) {
                    delete origData[k];
                }
            });
            render.gridRow($grid, rowIdx, origData);
            if (changedCells.length > 0) {
                pushStickHistory($grid, changedCells);
                events.trigger($exTable, events.ROW_UPDATED, events.createRowUi(rowIdx, origData));
            }
        }
        
        /**
         * Push history.
         */
        export function pushHistory($grid: HTMLElement, cells: Array<any>, txId: any) {
            let history = $.data($grid, internal.COPY_HISTORY);
            if (!history || history.length === 0) {
                history = [{ txId: txId, items: cells }];
                $.data($grid, internal.COPY_HISTORY, history);
                return;
            }
            let latestHistory = history[history.length - 1];
            if (latestHistory.txId === txId) {
                _.forEach(cells, function(cell) {
                    latestHistory.items.push(cell);
                });
            } else {
                let newHis = { txId: txId, items: cells };
                history.push(newHis);
            }
        }
        
        /**
         * Push edit history.
         */
        export function pushEditHistory($grid: HTMLElement, cell: any) {
            let history = $.data($grid, internal.EDIT_HISTORY);
            if (!history || history.length === 0) {
                $.data($grid, internal.EDIT_HISTORY, [ cell ]);
                return;
            }
            history.push(cell);   
        }
        
        /**
         * Push stick history.
         */
        export function pushStickHistory($grid: HTMLElement, cells: Array<any>) {
            let history = $.data($grid, internal.STICK_HISTORY);
            if (!history || history.length === 0) {
                $.data($grid, internal.STICK_HISTORY, [ cells ]);
                return;
            }
            history.push(cells);
        }
        
        /**
         * Remove stick history.
         */
        export function removeStickHistory($grid: HTMLElement, cells: Array<any>) {
            let history = $.data($grid, internal.STICK_HISTORY);
            if (!history || history.length === 0) return;
            _.remove(history, h => cells.some(c => helper.areSameCells(c, h)));
        }
        
        /**
         * Insert new row.
         */
        export function insertNewRow($container: HTMLElement) {
            let rowIndex; 
            Array.prototype.slice.call($container.querySelectorAll("div[class*='" + BODY_PRF + "']")).filter(function(e) {
                return !e.classList.contains(BODY_PRF + HORIZONTAL_SUM) && !e.classList.contains(BODY_PRF + LEFT_HORZ_SUM);
            }).forEach(function(e) {
                let ds = internal.getDataSource(e);
                let origDs = helper.getOrigDS(e);
                let newRec = {};
                rowIndex = ds.length;
                let columns = helper.gridColumnsMap(e);
                if (!ds || !columns) return;
                _.forEach(columns, function(value: any, key: any) {
                    if (key === controls.CHECKED_KEY) return;
                    newRec[key] = "";
                });
                let gen = $.data(e, internal.TANGI) || $.data(e, internal.CANON);
                let newRow = gen.painter.row(newRec, { css: { height: gen.painter.options.rowHeight } }, ds.length);
                if (!util.isNullOrUndefined(gen.startIndex)) {
                    let trList = e.querySelectorAll("tbody tr");
                    trList[trList.length - 1].insertAdjacentElement("beforebegin", newRow);
                } else {
                    e.querySelector("tbody").appendChild(newRow);
                }
                origDs[ds.length] = _.cloneDeep(newRec);
                ds[ds.length] = newRec;
            });
            let $grid = helper.getMainTable($container);
            $grid.scrollTop = $grid.scrollHeight;
            
            let $leftmost = $container.querySelector("." + BODY_PRF + LEFTMOST);
            let gen = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
            if (rowIndex >= gen.startIndex && rowIndex <= gen.endIndex) {
                controls.tick(true, $leftmost, false, rowIndex);
                return;
            }
            $leftmost.addXEventListener(events.RENDERED + ".once", function(evt: any) {
                controls.tick(true, $leftmost, false, rowIndex);
                $leftmost.removeXEventListener(events.RENDERED + ".once");
            });
        }
        
        /**
         * Delete rows.
         */
        export function deleteRows($container: HTMLElement) {
            let $grid = helper.getLeftmostTable($container);
            let rows = $.data($grid, internal.SELECTED_ROWS);
            if (!rows || !rows.items || rows.items.length === 0) return;
            if (rows.count > 0) {
                rows.selectAll = false;
                let $allBox = selector.classSiblings($grid, HEADER_PRF + LEFTMOST).map(function(e) {
                    let trList = e.querySelectorAll("table tr");
                    return trList[0].querySelectorAll("td")[0].querySelector("input");
                })[0];
                if (selector.is($allBox, ":checked")) $allBox.checked = false;
            }
            retrackEditor($container, rows.items);
            let $t = Array.prototype.slice.call($container.querySelectorAll("div[class*='" + BODY_PRF + "']")).filter(function(e) {
                return !e.classList.contains(BODY_PRF + HORIZONTAL_SUM) && !e.classList.contains(BODY_PRF + LEFT_HORZ_SUM);
            });
            $t.forEach(function(elm) {
                _.forEach([ internal.EDIT_HISTORY, internal.COPY_HISTORY, internal.STICK_HISTORY, 
                            internal.SELECTED_CELLS, errors.ERRORS ], function(name) {
                    retrackRecords(elm, rows.items, name);
                });
            });
            $t.forEach(function(elm: HTMLElement, index: number) {
                let ds = internal.getDataSource(elm);
                let origDs = helper.getOrigDS(elm);
                if (!ds || ds.length === 0) return;
                if (ds.length > rows.items.length) rows.items[ds.length - 1] = false;
                let count = rows.count;
                for (let i = rows.items.length - 1; i >= 0; i--) {
                    let $row = selection.rowAt(elm, i);
                    if (rows.items[i]) {
                        if ($row !== intan.NULL && !$row.classList.contains(NAMESPACE + "-" + intan.BOTTOM_SPACE)) {
                            $row.parentNode.removeChild($row);
                        }
                        ds.splice(i, 1);
                        origDs.splice(i, 1);
                        if (index === $t.length - 1) {
                            rows.items[i] = false;
                            rows.items.splice(i, 1);
                            rows.count--;
                        }
                        count--;
                    } else {
                        if ($row !== intan.NULL) {
                            selector.queryAll($row, "td").forEach(function(e) {
                                let view = $.data(e, internal.VIEW);
                                if (view) {
                                    let coord = view.split("-");
                                    $.data(e, internal.VIEW, parseInt(coord[0]) - count + "-" + coord[1]);
                                }
                            });
                        }
                    }
                }
                
                if (ds.length === 0) {
                    elm.querySelector("." + NAMESPACE + "-" + intan.BOTTOM_SPACE).style.height = EMPTY_TBL_HEIGHT;
                }
            });
        }
        
        /**
         * Retrack editor.
         */
        function retrackEditor($container: HTMLElement, rows: any) {
            let count = 0;
            let editor: any = $.data($container, update.EDITOR);
            if (!editor) return;    
            if (rows[editor.rowIdx]) {
                $.data($container, update.EDITOR, null);
                return;
            }
            for (let i = 0; i < editor.rowIdx; i++) {
                if (rows[i]) count++;
            }
            if (count > 0) editor.rowIdx = editor.rowIdx - count;
        }
        
        /**
         * Retrack records.
         */
        function retrackRecords($grid: HTMLElement, rows: any, name: any) {
            let histories: any = $.data($grid, name);
            if (!histories) return;
            if (name === internal.COPY_HISTORY) {
                for (let i = histories.length - 1; i >= 0; i--) {
                    each(histories[i].items, rows);
                }
            } else if (name === internal.SELECTED_CELLS) {
                _.forEach(Object.keys(histories).sort((a: any, b: any) => a - b), function(key: any, index: any) {
                    let count = 0;
                    if (rows[key]) {
                        delete histories[key];
                        return;
                    }
                    for (let i = 0; i < key; i++) {
                        if (rows[i]) count++;
                    }
                    if (count > 0) {
                        let newKey = key - count;
                        histories[newKey] = histories[key];
                        delete histories[key];
                    }
                });
            } else {
                each(histories, rows);
            }
        }
        
        /**
         * Each.
         */
        function each(histories: any, rows: any) {
            let removes = [];
            _.forEach(histories, function(his: any, index: any) {
                let count = 0;
                if (rows[his.rowIndex]) {
                    removes.push(index);
                    return;
                }
                for (let i = 0; i < his.rowIndex; i++) {
                    if (rows[i]) count++;
                }
                if (count > 0) his.rowIndex = his.rowIndex - count;
            });
            
            while (removes.length > 0) {
                histories.splice(removes.pop(), 1);
            }
        }
    }
    
    module copy {
        export let PASTE_ID = "pasteHelper";
        export let COPY_ID = "copyHelper";
        enum Mode {
            SINGLE,
            MULTIPLE
        }
        
        export class History {
            cells: Array<selection.Cell>;
            constructor(cells: Array<any>) {
                this.cells = cells;
            }
        }
        
        export class Printer {
            $grid: HTMLElement;
            options: any;
            mode: Mode;
            constructor(options?: any) {
                this.options = options;
            }
            
            /**
             * Hook.
             */
            hook($grid: HTMLElement) {
                var self = this;
                self.$grid = $grid;
                self.$grid.setAttribute("tabindex", 0);
                self.$grid.style.outline = "none";
                self.$grid.addXEventListener(events.FOCUS_IN, function(evt: any) {
                    if (document.querySelector("#pasteHelper") && document.querySelector("#copyHelper")) return;
                    var pasteArea = document.createElement("textarea");
                    pasteArea.setAttribute("id", PASTE_ID);
                    pasteArea.style.opacity = "0";
                    pasteArea.style.overflow = "hidden";
                    pasteArea.addXEventListener(events.PASTE, self.paste.bind(self));
                    var copyArea = document.createElement("textarea"); 
                    copyArea.setAttribute("id", COPY_ID);
                    copyArea.style.opacity = "0";
                    copyArea.style.overflow = "hidden";
                    let $div = document.createElement("div");
                    $div.style.position = "fixed";
                    $div.style.top = "-10000px";
                    $div.style.left = "-10000px";
                    document.body.appendChild($div);
                    $div.appendChild(pasteArea);
                    $div.appendChild(copyArea);
                });
                
                self.$grid.addXEventListener(events.KEY_DOWN, function(evt: any) {
                    if (evt.ctrlKey && helper.isPasteKey(evt)) {
                        document.querySelector("#pasteHelper").focus();
                    } else self.getOp(evt);
                    _.defer(function() {
                        selection.focus(self.$grid);
                    });
                });
            }
            
            /**
             * Get op.
             */
            getOp(evt: any) {
                let self = this;
                if (evt.ctrlKey && helper.isCopyKey(evt)) {
                    self.copy();
                } else if (evt.ctrlKey && helper.isCutKey(evt)) {
//                    self.cut();   
                } else if (evt.ctrlKey && helper.isUndoKey(evt)) {
                    self.undo();
                }
            }
            
            /**
             * Copy.
             */
            copy(cut?: boolean) {
                let self = this;
                let selectedCells: Array<any> = selection.getSelectedCells(this.$grid);
                let copiedData;
                if (selectedCells.length === 1) {
                    let cell = selectedCells[0];
                    let ds = internal.getDataSource(self.$grid);
                    let pk = helper.getPrimaryKey(self.$grid);
                    if (helper.isDetCell(self.$grid, cell.rowIndex, cell.columnKey)
                        || helper.isXCell(self.$grid, ds[cell.rowIndex][pk], cell.columnKey, style.HIDDEN_CLS, style.SEAL_CLS)) return;
                    this.mode = Mode.SINGLE;
                    copiedData = _.isObject(selectedCells[0].value) ? JSON.stringify(selectedCells[0].value) : selectedCells[0].value;
                } else {
                    this.mode = Mode.MULTIPLE;
                    copiedData = this.converseStructure(selectedCells, cut);
                }
                let $copyHelper = document.querySelector("#copyHelper");
                $copyHelper.value = copiedData;
                $copyHelper.select();
                document.execCommand("copy");
                return selectedCells;
            }
            
            /**
             * Convert structure.
             */
            converseStructure(cells: Array<any>, cut: boolean): string {
                let self = this;
                let maxRow = 0;
                let minRow = 0;
                let maxColumn = 0;
                let minColumn = 0;
                let structure = [];
                let structData: string = "";
                _.forEach(cells, function(cell: any, index: number) {
                    let rowIndex = parseInt(cell.rowIndex);
                    let columnIndex = helper.getDisplayColumnIndex(self.$grid, cell.columnKey);
                    if (index === 0) {
                        minRow = maxRow = rowIndex;
                        minColumn = maxColumn = columnIndex;
                    }
                    if (rowIndex < minRow) minRow = rowIndex;
                    if (rowIndex > maxRow) maxRow = rowIndex;
                    if (columnIndex < minColumn) minColumn = columnIndex;
                    if (columnIndex > maxColumn) maxColumn = columnIndex;
                    if (util.isNullOrUndefined(structure[rowIndex])) {
                        structure[rowIndex] = {};
                    }
                    let ds = internal.getDataSource(self.$grid);
                    let pk = helper.getPrimaryKey(self.$grid);
                    if (helper.isDetCell(self.$grid, rowIndex, cell.columnKey)
                        || helper.isXCell(self.$grid, ds[rowIndex][pk], cell.columnKey, style.HIDDEN_CLS, style.SEAL_CLS)) {
                        structure[rowIndex][columnIndex] = undefined;
                    } else structure[rowIndex][columnIndex] = helper.stringValue(cell.value);
                });
                
                for (var i = minRow; i <= maxRow; i++) {
                    for (var j = minColumn; j <= maxColumn; j++) {
                        if (util.isNullOrUndefined(structure[i]) || util.isNullOrUndefined(structure[i][j])
                            || util.isNullOrEmpty(structure[i][j])) {
                            structData += "null";
                        } else {
                            structData += structure[i][j];
                        }
                        
                        if (j === maxColumn) structData += "\n";
                        else structData += "\t";
                    }
                }
                return structData;
            }
            
            /**
             * Cut.
             */
            cut() {
                var self = this;
                var selectedCells = this.copy(true);
                _.forEach(selectedCells, function(cell: any) {
                    let $cell = selection.cellAt(self.$grid, cell.rowIndex, cell.columnKey);
                    let value: any = "";
                    if ($cell.querySelectorAll("." + render.CHILD_CELL_CLS).length > 0) {
                        value = ["", ""];
                    }
                    update.gridCell(self.$grid, cell.rowIndex, cell.columnKey, -1, value);
                });
            } 
                
            /**
             * Paste.
             */
            paste(evt: any) {
                if (this.mode === Mode.SINGLE) {
                    this.pasteSingleCell(evt);
                } else {
                    this.pasteRange(evt);
                }
            }
            
            /**
             * Paste single cell.
             */
            pasteSingleCell(evt: any) {
                let self = this;
                let cbData = this.getClipboardContent(evt);
                cbData = helper.getCellData(cbData);
                let selectedCells = selection.getSelectedCells(this.$grid);
                let txId = util.randomId();
                _.forEach(selectedCells, function(cell: any, index: number) {
                    update.gridCellOw(self.$grid, cell.rowIndex, cell.columnKey, -1, cbData, txId);
                });
            }
                
            /**
             * Paste range.
             */
            pasteRange(evt: any) {
                var cbData = this.getClipboardContent(evt);
                cbData = this.process(cbData);
                this.updateWith(cbData);
            }
                
            /**
             * Process.
             */
            process(data: string) {
                var dataRows = _.map(data.split("\n"), function(row) {
                    return _.map(row.split("\t"), function(cData) {
                        return cData.indexOf(",") > 0 ? cData.split(",") : cData;
                    });
                });
                
                var rowsCount = dataRows.length;
                if ((dataRows[rowsCount - 1].length === 1 && dataRows[rowsCount - 1][0] === "")
                    || (dataRows.length === 1 && dataRows[0].length === 1 
                        && (dataRows[0][0] === "" || dataRows[0][0] === "\r"))) {
                    dataRows.pop();
                }
                return dataRows;
            }
                
            /**
             * Update with.
             */
            updateWith(data: any) {
                let self = this;
                let selectedCell: any = selection.getSelectedCells(self.$grid)[0];
                if (selectedCell === undefined) return;
                let visibleColumns = helper.gridVisibleColumns(self.$grid);
                let rowIndex = selectedCell.rowIndex;
                let startColumnIndex = helper.indexOf(selectedCell.columnKey, visibleColumns);
                if (startColumnIndex === -1) return;
                let txId = util.randomId();
                let ds = internal.getDataSource(self.$grid);
                let size = ds ? ds.length : 0;
                _.forEach(data, function(row: any, idx: number) {
                    let rowData = {};
                    let columnKey = selectedCell.columnKey;
                    let columnIndex = startColumnIndex;
                    for (var i = 0; i < row.length; i++) {
                        if (!util.isNullOrUndefined(row[i]) && row[i].constructor !== Array && row[i].trim() === "null") {
                            columnKey = helper.nextKeyOf(columnIndex++, visibleColumns);
                            if (!columnKey) break; 
                            continue;
                        }
                        rowData[columnKey] = helper.getCellData(row[i]);
                        columnKey = helper.nextKeyOf(columnIndex++, visibleColumns);
                        if (!columnKey) break;
                    }
                    if (rowIndex >= size) return false; 
                    update.gridRowOw(self.$grid, rowIndex, rowData, txId);
                    rowIndex++;
                });
            }
            
            /**
             * Undo.
             */
            undo() {
                let self = this;
                let histories = $.data(self.$grid, internal.COPY_HISTORY);
                if (!histories || histories.length === 0) return;
                let tx = histories.pop();
                _.forEach(tx.items, function(item: any) {
                    update.gridCell(self.$grid, item.rowIndex, item.columnKey, -1, item.value, true);
                    internal.removeChange(self.$grid, item);
                });
            }
            
            /**
             * Get clipboard content.
             */
            getClipboardContent(evt: any) {
                if (window.clipboardData) {
                    window.event.returnValue = false;
                    return window.clipboardData.getData("text");
                } else {
                    return evt.clipboardData.getData("text/plain");
                }
            }
        }
            
        /**
         * On.
         */
        export function on($grid: HTMLElement, updateMode: any) {
            if (updateMode === COPY_PASTE) {
                new Printer().hook($grid);
            }
        }
        
        /**
         * Off.
         */
        export function off($grid: HTMLElement, updateMode: any) {
            if (updateMode !== COPY_PASTE) {
                $grid.removeXEventListener(events.FOCUS_IN);
                $grid.removeXEventListener(events.KEY_DOWN);
                let $copy = document.querySelector("#" + COPY_ID);
                let $paste = document.querySelector("#" + PASTE_ID);
                if (util.isNullOrUndefined($copy) || util.isNullOrUndefined($paste)) return;
                $copy.parentNode.removeChild($copy);
                $paste.parentNode.removeChild($paste);
            }
        }
    }
    
    module spread {
        export let SINGLE = "single";
        export let MULTIPLE = "multiple";
        export class Sticker {
            mode: any = MULTIPLE;
            data: any;
            validate: any = () => true;
            constructor(data?: any) {
                this.data = data;
            }
        }
        
        /**
         * Bind sticker.
         */
        export function bindSticker($cell: HTMLElement, rowIdx: any, columnKey: any, options: any) {
            if (options.containerClass !== BODY_PRF + DETAIL || util.isNullOrUndefined(options.updateMode) 
                || options.updateMode !== STICK) return;
            $cell.addXEventListener(events.CLICK_EVT, function(evt: any) {
                if (evt.ctrlKey) return;
                let $grid = helper.closest($cell, "." + BODY_PRF + DETAIL);
                let sticker = $.data($grid, internal.STICKER);
                if (!sticker || util.isNullOrUndefined(sticker.data) 
                    || util.isNullOrUndefined(sticker.validate)) return;
                let gen = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
                let visibleColumns = gen.painter.visibleColumns;
                let data = {};
                let key = columnKey;
                let colIndex = helper.indexOf(key, visibleColumns);
                if (sticker.mode === SINGLE) {
                    let result;
                    if ((result = sticker.validate(rowIdx, columnKey, sticker.data)) !== true) {
                        // TODO: show error
                        result();
                        return;
                    }
                    update.stickGridCellOw($grid, rowIdx, columnKey, -1, sticker.data);
                    return;
                }
                _.forEach(sticker.data, function(cData: any) {
                    data[key] = cData;
                    key = helper.nextKeyOf(colIndex++, visibleColumns);
                    if (!key) return false;
                });
                update.stickGridRowOw($grid, rowIdx, data);
            });
        }
        
        /**
         * Bind row sticker.
         */
        export function bindRowSticker($row: HTMLElement, rowIdx: any, options: any) {
            if (options.containerClass !== BODY_PRF + DETAIL || util.isNullOrUndefined(options.updateMode) 
                || options.updateMode !== STICK) return;
            $row.addXEventListener(events.CLICK_EVT, function(evt: any) {
                if (evt.ctrlKey) return;
                let $grid = helper.closest($row, "." + BODY_PRF + DETAIL);
                let sticker = $.data($grid, internal.STICKER);
                if (!sticker || util.isNullOrUndefined(sticker.data) 
                    || util.isNullOrUndefined(sticker.validate)) return;
                let gen = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
                let visibleColumns = gen.painter.visibleColumns;
                let data = {};
                let $cell = evt.target;
                if (selector.is(evt.target, "." + render.CHILD_CELL_CLS)) {
                    $cell = helper.closest(evt.target, "td");
                }
                let coord = helper.getCellCoord($cell);
                let key = coord.columnKey;
                let colIndex = helper.indexOf(key, visibleColumns);
                if (sticker.mode === SINGLE) {
                    let result;
                    if ((result = sticker.validate(rowIdx, key, sticker.data)) !== true) {
                        // TODO: show error
                        result();
                        return;
                    }
                    update.stickGridCellOw($grid, rowIdx, key, -1, sticker.data);
                    return;
                }
                _.forEach(sticker.data, function(cData: any) {
                    data[key] = cData;
                    key = helper.nextKeyOf(colIndex++, visibleColumns);
                    if (!key) return false;
                });
                update.stickGridRowOw($grid, rowIdx, data);
            });
        }
    }
    
    module validation {
        export let TIME_SPLIT = ":";
        export let TIME_PTN = /^-?\d+:\d{2}$/;
        export let SHORT_TIME_PTN = /^-?\d+$/;
        export let NUMBER_PTN = /^\d+$/;
        export let MINUTE_MAX = 59;
        export let HOUR_MAX = 24;
        export let DEF_HOUR_MAX = 9999;
        export let DEF_HOUR_MIN = 0;
        export let DEF_MIN_MAXMIN = 0;
        export let DAY_MINS = 1439;
        
        class Result {
            isValid: boolean;
            value: any
            constructor(isValid: boolean, value?: any) {
                this.isValid = isValid;
                this.value = value;
            }
            
            static fail() {
                return new Result(false);  
            }
            static ok(value: any) {
                return new Result(true, value);
            }
        }
        
        /**
         * Validate.
         */
        export function validate($body: HTMLElement, $cell: HTMLElement, rowIdx: any, columnKey: any, 
                                innerIdx: any, value: any, timeRangerDef?: any) {
            let vtor = validation.mandate($body, columnKey, innerIdx);
            let gen = $.data($body, internal.TANGI) || $.data($body, internal.CANON);
            let rowId = gen.dataSource[rowIdx][gen.primaryKey];
            timeRangerDef = timeRangerDef || $.data($body, internal.TIME_VALID_RANGE);
            let formatValue;
            if (vtor) {
                if ((vtor.actValid === internal.TIME || vtor.actValid === internal.DURATION) && timeRangerDef) {
                    let ranges = timeRangerDef[rowId];
                    _.forEach(ranges, function(range) {
                        if (range && range.columnKey === columnKey && range.innerIdx === innerIdx) { 
                            vtor.max = range.max;
                            vtor.min = range.min;
                            return Result.fail();
                        }
                    });
                }
                
                let isValid;
                if (vtor.required && (_.isUndefined(value) || _.isEmpty(value))) {
                    isValid = false;
                } else if (!_.isUndefined(value) && !_.isEmpty(value)) {
                    if (vtor.actValid === internal.TIME) {
                        isValid = isTimeClock(value);
                        formatValue = formatTime(value); 
                    } else if (vtor.actValid === internal.DURATION) {
                        isValid = isTimeDuration(value, vtor.max, vtor.min);
                        formatValue = formatTime(value);
                    } else if (vtor.actValid === internal.NUMBER) {
                        isValid = isNumber(value, vtor.max, vtor.min);
                        formatValue = (_.isUndefined(value) || _.isEmpty(value)) ? "" : Number(value);
                    } else {
                        isValid = true;
                        formatValue = value;
                    }
                } else {
                    isValid = true;
                    formatValue = "";
                }
                let $tbl = helper.closest($body, "." + NAMESPACE);
                if (!isValid) {
                    errors.add($tbl, $cell, rowIdx, columnKey, innerIdx, value);
    //                cellData($exTable, rowIdx, columnKey, innerIdx, value);
//                    update.setText($cell, innerIdx, value);
                    return Result.fail();
                }
                if (errors.any($cell, innerIdx)) errors.remove($tbl, $cell, rowIdx, columnKey, innerIdx);
            }
            return Result.ok(formatValue);
        }
        
        /**
         * Mandate.
         */
        export function mandate($grid: HTMLElement, columnKey: any, innerIdx: any) {
            let visibleColumns = helper.getVisibleColumnsOn($grid);
            let actValid, dataType, max, min, required;
            _.forEach(visibleColumns, function(col: any) {
                if (col.key === columnKey) {
                    if (!col.dataType) return false;
                    dataType = col.dataType.toLowerCase();
                    actValid = which(innerIdx, dataType, internal.TIME) 
                                || which(innerIdx, dataType, internal.DURATION)
                                || which(innerIdx, dataType, internal.NUMBER)
                                || which(innerIdx, dataType, internal.TEXT);
                    
                    let constraints = ui.validation.getConstraint(col.primitiveValue);
                    if (constraints && (constraints.valueType === "Time" || constraints.valueType === "Clock")) {
                        max = constraints.max ? constraints.max : col.max;
                        min = constraints.min ? constraints.min : col.min;
                        required = constraints.required ? constraints.required : col.required;
                        return false;
                    }
                    max = col.max;
                    min = col.min;
                    required = col.required;
                    return false; 
                }
            });
            if (actValid)
                return {
                    actValid: actValid,
                    max: max,
                    min: min,
                    required: required
                };
        }
        
        /**
         * Which.
         */
        function which(innerIdx: any, dataType: any, type: string) {
            let actValid;
            if (dataType && dataType.indexOf(type) !== -1) {
                if (!util.isNullOrUndefined(innerIdx) && innerIdx > -1) {
                    _.forEach(dataType.split(internal.DT_SEPARATOR), function(p: any, index: any) {
                        if (p === type && index === innerIdx) {
                            actValid = type;
                            return false;
                        }
                    });
                } else {
                    actValid = type;
                }
            }
            return actValid;
        }
        
        /**
         * Check time clock.
         */
        export function isTimeClock(time: string) {
            if (util.isNullOrUndefined(time)) return false;
            time = time.trim();
            let hour, minute;
            if (TIME_PTN.test(time)) {
                let parts = time.split(TIME_SPLIT);
                hour = parseInt(parts[0]);
                minute = parseInt(parts[1]);
            } else if (SHORT_TIME_PTN.test(time)) {
                let totalTime = parseInt(time); 
                minute = totalTime % 100;
                hour = Math.floor(totalTime / 100);
            }
            if (((hour !== NaN && hour >= 0 && hour <= HOUR_MAX) || hour === NaN) 
                && minute !== NaN && minute >= 0 && minute <= MINUTE_MAX) return true;
            return false;   
        }
        
        /**
         * Check time duration.
         */
        export function isTimeDuration(time: string, max?: string, min?: string) {
            if (util.isNullOrUndefined(time)) return false;
            time = time.trim();
            let hour, minute, negative, minMM, maxHour = DEF_HOUR_MAX, minHour = DEF_HOUR_MIN;
            let maxMM = minMM = DEF_MIN_MAXMIN;
            let maxTime = parse(max) || { hour: DEF_HOUR_MAX, minute: DEF_MIN_MAXMIN, negative: false };
            let minTime = parse(min) || { hour: DEF_HOUR_MIN, minute: DEF_MIN_MAXMIN, negative: false };
            if (TIME_PTN.test(time)) {
                let parts = time.split(TIME_SPLIT);
                hour = Math.abs(parseInt(parts[0]));
                minute = parseInt(parts[1]);
                negative = time.charAt(0) === '-';
            } else if (SHORT_TIME_PTN.test(time)) {
                let totalTime = Math.abs(parseInt(time));
                minute = totalTime % 100;
                hour = Math.floor(totalTime / 100);
                negative = time.charAt(0) === '-';
            }
            if (((util.isNullOrUndefined(hour) || hour === NaN) && (util.isNullOrUndefined(minute) || minute === NaN))
                || minute > MINUTE_MAX) return false;
            let targetTime = getComplement({ hour: hour, minute: minute, negative: negative });
            if (!targetTime) return false;
            if (compare(targetTime, maxTime) > 0 || compare(targetTime, minTime) < 0) return false;
            return true; 
        }
        
        /**
         * Check number.
         */
        function isNumber(value: any, max: any, min: any) {
            if (!NUMBER_PTN.test(value)) return false;
            let int = parseInt(value);
            if ((!util.isNullOrUndefined(max) && int > parseInt(max))
                || (!util.isNullOrUndefined(min) && int < parseInt(min))) return false;
            return true;
        }
        
        /**
         * Compare.
         */
        function compare(one: any, other: any) {
            if (one.negative && !other.negative) return -1;
            else if (!one.negative && other.negative) return 1;
            else if (one.negative && other.negative) {
                return compareAbs(one, other) * (-1);
            } else return compareAbs(one, other);
        }
        
        /**
         * Compare abs.
         */
        function compareAbs(one: any, other: any) {
            if (one.hour > other.hour) {
                return 1;
            } else if (one.hour < other.hour) {
                return -1;
            } else if (one.minute > other.minute) {
                return 1;
            } else if (one.minute < other.minute) {
                return -1;
            }
            return 0;
        }
        
        /**
         * Get complement.
         */
        function getComplement(time: any) {
            if (!time.negative) return time;
            let oTime = DAY_MINS - (time.hour * 60 + time.minute);
            if (oTime < 0) return;
            let hour = Math.floor(oTime / 60);
            let minute = oTime - hour * 60;
            return { hour: hour, minute: minute, negative: true };
        }
        
        /**
         * Parse.
         */
        function parse(time: string) {
            if (TIME_PTN.test(time)) {
                let parts = time.split(TIME_SPLIT);
                let hour = Math.abs(parseInt(parts[0]));
                let minute = parseInt(parts[1]); 
                return { 
                    hour: hour,
                    minute: minute,
                    negative: time.charAt(0) === '-'
                };
            }
        }
        
        /**
         * Format time.
         */
        export function formatTime(time: string) {
            let minute, hour;
            if (SHORT_TIME_PTN.test(time)) {
                let totalTime = Math.abs(parseInt(time));
                minute = totalTime % 100;
                hour = Math.floor(totalTime / 100);
            }
            if (!util.isNullOrUndefined(hour) && hour !== NaN 
                && !util.isNullOrUndefined(minute) && minute !== NaN) {
                if (minute < 10) minute = "0" + minute;
                return (time.charAt(0) === '-' ? "-" : "") + hour + TIME_SPLIT + minute;
            }
            if (!util.isNullOrUndefined(hour) && hour === NaN 
                && !util.isNullOrUndefined(minute) && minute !== NaN) {
                return (time.charAt(0) === '-' ? "-" : "") + minute;
            }
            return time;
        }
    }
    
    module errors {
        export let ERROR_CLS = "x-error"; 
        export let ERRORS = "errors";
        
        /**
         * Add.
         */
        export function add($grid: HTMLElement, $cell: HTMLElement, rowIdx: any, columnKey: any, innerIdx: any, value: any) {
            if (any($cell, innerIdx)) return;
            let errors = $.data($grid, ERRORS);
            let newErr = new selection.Cell(rowIdx, columnKey, value, innerIdx);
            if (!errors) {
                errors = [ newErr ];
                $.data($grid, ERRORS, errors);
            } else {
                errors.push(newErr);
            }
            if (selector.is($cell, "td") && !util.isNullOrUndefined(innerIdx) && innerIdx > -1) {
                $cell.querySelectorAll("div")[innerIdx].classList.add(ERROR_CLS);
            } else {
                $cell.classList.add(ERROR_CLS);
            }
        }
        
        /**
         * Remove.
         */
        export function remove($grid: HTMLElement, $cell: HTMLElement, rowIdx: any, columnKey: any, innerIdx: any) {
            let errors = $.data($grid, ERRORS);
            if (!errors) return;
            let idx;
            _.forEach(errors, function(err: any, index: any) {
                if (helper.areSameCells(err, { rowIndex: rowIdx, columnKey: columnKey, innerIdx: innerIdx })) {
                    idx = index;
                    return false;
                }
            });
            if (!util.isNullOrUndefined(idx)) {
                errors.splice(idx, 1);
                if (selector.is($cell, "td") && !util.isNullOrUndefined(innerIdx) && innerIdx > -1) {
                    $cell.querySelector("div")[innerIdx].classList.remove(ERROR_CLS);
                } else {
                    $cell.classList.remove(ERROR_CLS);
                }
            }
        }
        
        /**
         * Clear.
         */
        export function clear($grid: HTMLElement) {
            $.data($grid, ERRORS, null);
        } 
        
        /**
         * Any.
         */
        export function any($cell: HTMLElement, innerIdx: any) {
            let $childCells = $cell.querySelectorAll("." + render.CHILD_CELL_CLS);
            if (!util.isNullOrUndefined(innerIdx) && $childCells.length > 0) {
                return $childCells[innerIdx].classList.contains(ERROR_CLS);
            }
            return $cell.classList.contains(ERROR_CLS);
        }
        
        /**
         * Occurred.
         */
        export function occurred($grid: HTMLElement) {
            let errs = $.data($grid, ERRORS);
            if (!errs) return false;
            return errs.length > 0;
        }
    }
    
    module selection {
        export let CELL_SELECTED_CLS = "x-cell-selected";
        export let ROW_SELECTED_CLS = "x-row-selected";
        export class Cell {
            rowIndex: any;
            columnKey: any;
            innerIdx: any;
            value: any;
            constructor(rowIdx: any, columnKey: any, value: any, innerIdx?: any) {
                this.rowIndex = rowIdx;
                this.columnKey = columnKey;
                this.value = value;
                this.innerIdx = innerIdx;
            }
        }
        
        /**
         * Check up.
         */
        export function checkUp($exTable: HTMLElement) {
            if ($.data($exTable, NAMESPACE).updateMode !== COPY_PASTE) return;
            let $detailContent = $exTable.querySelector("." + BODY_PRF + DETAIL);
            let isSelecting;
            $detailContent.onselectstart = function() {
                return false;
            };
            $detailContent.addXEventListener(events.MOUSE_DOWN, function(evt: any) {
                let $target = evt.target;
                isSelecting = true;
                if (!selector.is($target, "." + render.CHILD_CELL_CLS) && !selector.is($target, "td")) return;
                if (evt.shiftKey) {
                    selectRange($detailContent, $target);
                    return;
                }
                if (!evt.ctrlKey) {
                    clearAll($detailContent);
                }
                selectCell($detailContent, $target);
            });
            $detailContent.addXEventListener(events.MOUSE_UP, function(evt: any) {
                isSelecting = false;
            });
            $detailContent.addXEventListener(events.MOUSE_MOVE, function(evt: any) {
                if (isSelecting) {
                    selectRange($detailContent, evt.target);
                }
            });
        }
        
        /**
         * Select range.
         */
        function selectRange($grid: HTMLElement, $cell: HTMLElement) {
            if (util.isNullOrUndefined($cell) || selector.is($cell, "." + BODY_PRF + DETAIL)) return;
            let lastSelected = $.data($grid, internal.LAST_SELECTED);
            if (!lastSelected) { 
                selectCell($grid, $cell);
                return;
            }
            clearAll($grid);
            let toCoord = helper.getCellCoord($cell); 
            let minRowIdx = Math.min(lastSelected.rowIdx, toCoord.rowIdx);
            let maxRowIdx = Math.max(lastSelected.rowIdx, toCoord.rowIdx);
            for (let i = minRowIdx; i < maxRowIdx + 1; i++) { 
                cellInRange($grid, i, lastSelected.columnKey, toCoord.columnKey);
            }
        }
        
        /**
         * Mark cell.
         */
        export function markCell($cell: HTMLElement) {
            if (selector.is($cell, "." + render.CHILD_CELL_CLS)) {
                $cell.classList.add(CELL_SELECTED_CLS);
                selector.classSiblings($cell, render.CHILD_CELL_CLS).forEach(function(e) {
                    e.classList.add(CELL_SELECTED_CLS);
                });
                return true;
            } else if (selector.is($cell, "td")) {
                let childCells = $cell.querySelectorAll("." + render.CHILD_CELL_CLS);
                if (childCells.length > 0) {
                    helper.addClass(childCells, CELL_SELECTED_CLS);
                } else {
                    $cell.classList.add(CELL_SELECTED_CLS);
                }
                return true;
            }
            return false;
        }
        
        /**
         * Select cell.
         */
        export function selectCell($grid: HTMLElement, $cell: HTMLElement, notLast?: boolean) {
            if (!markCell($cell)) return;
            let coord = helper.getCellCoord($cell);
            addSelect($grid, coord.rowIdx, coord.columnKey, notLast);
            tickRows(selector.classSiblings($grid, BODY_PRF + LEFTMOST), false);
        }
        
        /**
         * Add select.
         */
        export function addSelect($grid: HTMLElement, rowIdx: any, columnKey: any, notLast?: boolean) {
            let selectedCells = $.data($grid, internal.SELECTED_CELLS);
            if (!notLast) $.data($grid, internal.LAST_SELECTED, { rowIdx: rowIdx, columnKey: columnKey });
            if (!selectedCells) {
                selectedCells = {};
                selectedCells[rowIdx] = [ columnKey ];
                $.data($grid, internal.SELECTED_CELLS, selectedCells);
                return;
            }
            if (!selectedCells[rowIdx]) {
                selectedCells[rowIdx] = [ columnKey ];
                return;
            }
            if (_.find(selectedCells[rowIdx], function(key) {
                return key === columnKey;
            }) === undefined) {
                selectedCells[rowIdx].push(columnKey);
            }
        }
        
        /**
         * Clear.
         */
        export function clear($grid: HTMLElement, rowIdx: any, columnKey: any) {
            let selectedCells = $.data($grid, internal.SELECTED_CELLS);
            if (!selectedCells) return;
            let row = selectedCells[rowIdx];
            if (!row || row.length === 0) return;
            let colIdx;
            _.forEach(row, function(key: any, index: number) {
                if (key === columnKey) {
                    colIdx = index;
                    return false;
                }
            });
            if (util.isNullOrUndefined(colIdx)) return;
            row.splice(colIdx, 1);
            let selectedCell = cellAt($grid, rowIdx, columnKey);
            if (selectedCell === intan.NULL) return;
            if (selectedCell) {
                let $childCells = selectedCell.querySelectorAll("." + render.CHILD_CELL_CLS);
                if ($childCells.length > 0) {
                    helper.removeClass($childCells, CELL_SELECTED_CLS);
                } else {
                    helper.removeClass(selectedCell, CELL_SELECTED_CLS);
                }
            }
        }
        
        /**
         * Clear all.
         */
        export function clearAll($grid: HTMLElement) {
            let selectedCells = $.data($grid, internal.SELECTED_CELLS);
            if (!selectedCells) return;
            _.forEach(Object.keys(selectedCells), function(rowIdx: any, index: number) {
                if (!rowExists($grid, rowIdx)) return;
                _.forEach(selectedCells[rowIdx], function(col: any, i: number) {
                    let $cell = cellAt($grid, rowIdx, col);
                    if ($cell) {
                        let childCells = $cell.querySelectorAll("." + render.CHILD_CELL_CLS);
                        if (childCells.length > 0) {
                            helper.removeClass(childCells, CELL_SELECTED_CLS);
                        } else {
                            helper.removeClass($cell, CELL_SELECTED_CLS);
                        }
                    }
                });
            });
            $.data($grid, internal.SELECTED_CELLS, null);
        }
        
        /**
         * Cell at.
         */
        export function cellAt($grid: HTMLElement, rowIdx: any, columnKey: any) {
            let $row = rowAt($grid, rowIdx);
            return getCellInRow($grid, $row, columnKey);
        }
        
        /**
         * Row at.
         */
        export function rowAt($grid: HTMLElement, rowIdx: any) {
            let virt = $.data($grid, internal.TANGI);
            let rows = $grid.getElementsByTagName("tr");
            let idx;
            if (!virt) {
                idx = (parseInt(rowIdx) + 1);
                if (!rows || rows.length <= idx) return intan.NULL;
                return rows[idx];
            }
            if (virt.startIndex > rowIdx || virt.endIndex < rowIdx)
                return intan.NULL;
            idx = (parseInt(rowIdx) - virt.startIndex + 1);
            return rows[idx];
        }
        
        /**
         * Cell of.
         */
        export function cellOf($grid: HTMLElement, rowId: any, columnKey: any) {
            let $row = rowOf($grid, rowId);
            return getCellInRow($grid, $row, columnKey);
        }
        
        /**
         * Row of.
         */
        export function rowOf($grid: HTMLElement, rowId: any) {
            let gen = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
            if (!gen) return;
            let start = gen.startIndex || 0;
            let end = gen.endIndex || gen.dataSource.length - 1;
            for (let i = start; i <= end; i++) {
                if (gen.dataSource[i][gen.primaryKey] === rowId) {
                    return rowAt($grid, i);
                }
            }
        }
        
        /**
         * Row exists.
         */
        export function rowExists($grid: HTMLElement, rowIdx: any) {
            let virt = $.data($grid, internal.TANGI);
            if (virt && (virt.startIndex > rowIdx || virt.endIndex < rowIdx))
                return false;
            return true;
        }
        
        /**
         * Cell in range.
         */
        export function cellInRange($grid: HTMLElement, rowIdx: any, startKey: any, endKey: any) {
            let range = [];
            let $row = rowAt($grid, rowIdx);
            if ($row === intan.NULL) return;
            let colRange = columnIndexRange($grid, startKey, endKey);
            if (colRange.start === -1 || colRange.end === -1) return;
            let min = Math.min(colRange.start, colRange.end);
            let max = Math.max(colRange.start, colRange.end);
            let $td = selector.queryAll($row, "td").filter(function(e) {
                return e.style.display !== "none";
            }).forEach(function(e, index) {
                if (index >= min && index <= max) {
                    let childCells = e.querySelectorAll("." + render.CHILD_CELL_CLS);
                    if (childCells.length > 0) {
                        helper.addClass(childCells, CELL_SELECTED_CLS);
                    } else {
                        helper.addClass(e, CELL_SELECTED_CLS);
                    }
                    addSelect($grid, rowIdx, colRange.columns[index].key, true);
                    range.push($(this));
                } else if (index > max) return false;
            });
            return range;
        }
        
        /**
         * Get cell in row.
         */
        export function getCellInRow($grid: HTMLElement, $row: HTMLElement, columnKey: any) {
            if ($row === intan.NULL || !$row) return intan.NULL;
            let gen = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
            let visibleColumns = gen.painter.visibleColumns
            let columnIdx;
            _.forEach(visibleColumns, function(c: any, idx: number) {
                if (c.key === columnKey) {
                    columnIdx = idx;
                    return false;
                }
            });
            if (util.isNullOrUndefined(columnIdx)) return intan.NULL;
            let cells = Array.prototype.slice.call($row.getElementsByTagName("td")).filter(function(c) {
                return c.style.display !== "none";
            });
            return cells[columnIdx];
        }
        
        /**
         * Column index range.
         */
        export function columnIndexRange($grid: HTMLElement, startKey: any, endKey: any) {
            let cloud: intan.Cloud = $.data($grid, internal.TANGI);
            let canon: any = $.data($grid, internal.CANON);
            let visibleColumns;
            if (!util.isNullOrUndefined(cloud)) {
                visibleColumns = cloud.painter.visibleColumns;
            } else {
                visibleColumns = canon.painter.visibleColumns;
            }
            let startColumnIdx = -1, endColumnIdx = -1;
            _.forEach(visibleColumns, function(c: any, idx: number) {
                if (c.key === startKey) {
                    startColumnIdx = idx;
                } 
                if (c.key === endKey) {
                    endColumnIdx = idx;
                }
                if (startColumnIdx !== -1 && endColumnIdx !== -1) return false;
            });
            return {
                start: startColumnIdx,
                end: endColumnIdx,
                columns: visibleColumns
            };
        }
        
        /**
         * Get selected cells.
         */
        export function getSelectedCells($grid: HTMLElement) {   
            let selectedCells = $.data($grid, internal.SELECTED_CELLS);
            let generator = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
            let dataSource = generator.dataSource;
            let cells = [];
            _.forEach(Object.keys(selectedCells), function(rowIdx: any) {
                _.forEach(selectedCells[rowIdx], function(colKey: any) {
                    cells.push(new Cell(rowIdx, colKey, dataSource[rowIdx][colKey]));
                });
            });
            return cells;
        }
        
        /**
         * Off.
         */
        export function off($exTable: HTMLElement) {
            if ($.data($exTable, NAMESPACE).updateMode === COPY_PASTE) return;
            let $detailContent = $exTable.querySelector("." + BODY_PRF + DETAIL);
            $detailContent.onselectstart = function() {
                return true;
            };
            $detailContent.removeXEventListener(events.MOUSE_DOWN);
            $detailContent.removeXEventListener(events.MOUSE_UP);
            $detailContent.removeXEventListener(events.MOUSE_MOVE);
        }
        
        /**
         * Focus.
         */
        export function focus($grid: HTMLElement) {
            $grid.focus();
        }
        
        /**
         * Focus latest.
         */
        export function focusLatest($grid: HTMLElement) {
            let latest = $.data($grid, internal.LAST_SELECTED);
            if (!latest) return;
            let $cell = selection.cellAt($grid, latest.rowIdx, latest.columnKey);
            if ($cell === intan.NULL) return;
            $cell.focus();
        }
        
        /**
         * Select row.
         */
        export function selectRow($grid: HTMLElement, rowIndex: any) {
            let $row = selection.rowAt($grid, rowIndex);
            if ($row !== intan.NULL && !$row.classList.contains(NAMESPACE + "-" + intan.BOTTOM_SPACE)) {
                helper.addClass($row, ROW_SELECTED_CLS);
            }
            setTimeout(() => {
                let tbls = helper.classSiblings($grid, BODY_PRF).filter(function(e) {
                    return !e.classList.contains(BODY_PRF + HORIZONTAL_SUM) && !e.classList.contains(BODY_PRF + LEFT_HORZ_SUM);
                });
                tbls.forEach(function(t) {
                    $row = selection.rowAt(t, rowIndex);
                    if ($row !== intan.NULL && !$row.classList.contains(NAMESPACE + "-" + intan.BOTTOM_SPACE)) {
                        helper.addClass($row, ROW_SELECTED_CLS);
                    }
                });
            }, 60);
            let selectedRows = $.data($grid, internal.SELECTED_ROWS);
            if (!selectedRows) {
                selectedRows = {};
                selectedRows.items = [];
                selectedRows.items[rowIndex] = true;
                selectedRows.count = (selectedRows.count || 0) + 1;
                $.data($grid, internal.SELECTED_ROWS, selectedRows);
                return;
            } 
            if (!selectedRows.items) {
                selectedRows.items = [];
                selectedRows.items[rowIndex] = true;
                selectedRows.count = (selectedRows.count || 0) + 1; 
                return;   
            } 
            if (!selectedRows.items[rowIndex]) {
                selectedRows.items[rowIndex] = true;
                selectedRows.count = (selectedRows.count || 0) + 1;
            }
        }
        
        /**
         * Deselect row.
         */
        export function deselectRow($grid: HTMLElement, rowIndex: any) {
            let selectedRows = $.data($grid, internal.SELECTED_ROWS);
            if (!selectedRows || !selectedRows.items || selectedRows.items.length === 0) return;
            selectedRows.items[rowIndex] = false;
            selectedRows.count--;
            let row = selection.rowAt($grid, rowIndex);
            if (!row) return;
            row.classList.remove(ROW_SELECTED_CLS);
            let tbls = helper.classSiblings($grid, BODY_PRF).filter(function(e) {
                return !e.classList.contains(BODY_PRF + HORIZONTAL_SUM) && !e.classList.contains(BODY_PRF + LEFT_HORZ_SUM);
            });
            tbls.forEach(function(t) {
                let bodies = selection.rowAt(t, rowIndex);
                helper.removeClass(bodies, ROW_SELECTED_CLS);
            });
        }
        
        /**
         * Tick rows.
         */
        export function tickRows($grid: HTMLElement, flag: boolean, limit?: boolean) {
            let selectedRows = $.data($grid, internal.SELECTED_ROWS);
            if (!selectedRows || !selectedRows.items || selectedRows.items.length === 0) return;
            let gen = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
            let start = limit ? gen.startIndex : 0;
            let end = limit ? gen.endIndex : selectedRows.items.length - 1;
            for (let i = start; i <= end; i++) {
                if (selectedRows.items[i]) {
                    controls.tick(flag, $grid, false, i);
                }
            }
        }
    }
    
    module resize {
        export let AGENCY = "ex-agency";
        export let LINE = "ex-line";
        export let RESIZE_COL = "resize-column";
        export let AREA_AGENCY = "ex-area-agency";
        export let RESIZE_AREA = "resize-area";
        export let AREA_LINE = "ex-area-line";
        export let STAY_CLS = "x-stay";
        export class ColumnAdjuster {
            $headerTable: HTMLElement;
            $contentTable: HTMLElement;
            $colGroup: NodeListOf<Element>;
            $agency: HTMLElement;
            $ownerDoc: HTMLElement;
            // Resizable td list
            standardCells: Array<any>;
            actionDetails: any;
            
            constructor($headerTable: HTMLElement, $contentTable: HTMLElement, options?: any) {
                this.$headerTable = $headerTable;
                this.$contentTable = $contentTable;
                this.$ownerDoc = $headerTable.ownerDocument;
                this.standardCells = [];
            }
            
            /**
             * Handle.
             */
            handle() {
                let self = this;
                self.$agency = document.createElement("div"); 
                self.$agency.className = AGENCY;
                self.$agency.style.position = "relative";
                self.$agency.style.width = self.$headerTable.offsetWidth;
                self.$headerTable.insertAdjacentElement("beforebegin", self.$agency);
                self.$colGroup = self.$headerTable.querySelectorAll("colgroup > col");
                let trList = self.$headerTable.querySelectorAll("tbody > tr");
                let targetColumnIdx = 0;
                _.forEach(trList, function(tr) {
                    let tdList = tr.querySelectorAll("td");
                    for (let i = 0; i < tdList.length; i++) {
                        if (self.standardCells.length >= self.$colGroup.length) return false;
                        let $td = tdList[i];
                        let colspan = $td.getAttribute("colspan");
                        if (!util.isNullOrUndefined(colspan) && parseInt(colspan) > 1) continue;
                        self.standardCells.push($td);
                        
                        let $targetCol = self.$colGroup[targetColumnIdx];
                        let $line = document.createElement("div");
                        $line.className = LINE;
                        $.data($line, RESIZE_COL, $targetCol);
                        $line.style.position = "absolute";
                        $line.style.cursor = "ew-resize";
                        $line.style.width = "4px";
                        $line.style.zIndex = 2, 
                        $line.style.marginLeft = "-3px";  
                        self.$agency.appendChild($line);
                        
                        // Line positions
                        let height = self.$headerTable.style.height;
                        let left = $td.offsetWidth + (selector.offset($td).left - selector.offset(self.$agency).left);
                        $line.style.left = left + "px";
                        $line.style.height = height;
                        targetColumnIdx++;
                    } 
                });
                self.$agency.addXEventListener(events.MOUSE_DOWN, self.cursorDown.bind(self));  
            }
            
            /**
             * Cursor down.
             */
            cursorDown(event: any) {
                let self = this;
                if (self.actionDetails) {
                    self.cursorUp(event);
                }
                let $targetGrip = event.target;
                let gripIndex = selector.index($targetGrip);
                let $leftCol = $.data($targetGrip, RESIZE_COL);
                let $rightCol = self.$colGroup[gripIndex + 1];
                let leftWidth = $leftCol.style.width;
                let rightWidth = $rightCol.style.width;
                self.actionDetails = {
                    $targetGrip: $targetGrip,
                    gripIndex: gripIndex,
                    $leftCol: $leftCol,
                    $rightCol: $rightCol,
                    xCoord: getCursorX(event),
                    widths: {
                        left : parseFloat(leftWidth),
                        right: parseFloat(rightWidth)
                    },
                    changedWidths: {
                        left: parseFloat(leftWidth),
                        right: parseFloat(rightWidth)
                    }
                };
                self.$ownerDoc.addXEventListener(events.MOUSE_MOVE, self.cursorMove.bind(self));
                self.$ownerDoc.addXEventListener(events.MOUSE_UP, self.cursorUp.bind(self));
                event.preventDefault();
            }
            
            /**
             * Cursor move.
             */
            cursorMove(event: any) {
                let self = this;
                if (!self.actionDetails) return;
                let distance = getCursorX(event) - self.actionDetails.xCoord;
                if (distance === 0) return;
                let leftWidth, rightWidth;
                if (distance > 0) {
                    leftWidth = self.actionDetails.widths.left + distance;
                    rightWidth = self.actionDetails.widths.right - distance;
                } else {
                    leftWidth = self.actionDetails.widths.left + distance;
                    rightWidth = self.actionDetails.widths.right - distance;
                } 
                if (leftWidth <= 20 || rightWidth <= 20) return;
                
                self.actionDetails.changedWidths.left = leftWidth;
                self.actionDetails.changedWidths.right = rightWidth;
                if (self.actionDetails.$leftCol) {
                    self.setWidth(self.actionDetails.$leftCol, leftWidth);
                    let $contentLeftCol = self.$contentTable.querySelectorAll("colgroup > col")[self.actionDetails.gripIndex];
                    self.setWidth($contentLeftCol, leftWidth);
                }
                if (self.actionDetails.$rightCol) {
                    self.setWidth(self.actionDetails.$rightCol, rightWidth);
                    let $contentRightCol = self.$contentTable.querySelectorAll("colgroup > col")[self.actionDetails.gripIndex + 1];
                    self.setWidth($contentRightCol, rightWidth);
                }
            }
            
            /**
             * Cursor up.
             */
            cursorUp(event: any) {
                let self = this;
                self.$ownerDoc.removeXEventListener(events.MOUSE_MOVE);
                self.$ownerDoc.removeXEventListener(events.MOUSE_UP);
                self.syncLines();
                self.actionDetails = null;
            }
            
            /**
             * Set width.
             */
            setWidth($col: HTMLElement, width: any) {
                $col.style.width = parseFloat(width) + "px";
            }
            
            /**
             * Sync lines.
             */
            syncLines() {
                let self = this;
                self.$agency.style.width = self.$headerTable.style.width;
                _.forEach(self.standardCells, function($td: HTMLElement, index: number) {
                    let height = self.$headerTable.style.height; 
                    let left = $td.offsetWidth + (selector.offset($td).left - selector.offset(self.$agency).left);
                    let div = self.$agency.querySelectorAll("div")[index];
                    div.style.left = left + "px";
                    div.style.height = height;
                });
            }
        }
        
        export class AreaAdjuster {
            $container: HTMLElement;
            $ownerDoc: HTMLElement;
            headerWrappers: Array<HTMLElement>;
            bodyWrappers: Array<HTMLElement>;
            $leftHorzSumHeader: HTMLElement;
            $leftHorzSumContent: HTMLElement;
            $horzSumHeader: HTMLElement;
            $horzSumContent: HTMLElement;
            $areaAgency: HTMLElement;
            $depLeftmostHeader: HTMLElement;
            $depLeftmostBody: HTMLElement;
            $depDetailHeader: HTMLElement;
            $depDetailBody: HTMLElement;
            actionDetails: any;
            
            constructor($container: HTMLElement, headerWrappers: Array<HTMLElement>, bodyWrappers: Array<HTMLElement>, $follower: HTMLElement) {
                this.$container = $container;
                this.headerWrappers = headerWrappers;
                this.bodyWrappers = bodyWrappers;
                this.$ownerDoc = $container.ownerDocument;
                this.$leftHorzSumHeader = this.$container.querySelector("." + HEADER_PRF + LEFT_HORZ_SUM);
                this.$leftHorzSumContent = this.$container.querySelector("." + BODY_PRF + LEFT_HORZ_SUM);
                this.$horzSumHeader = this.$container.querySelector("." + HEADER_PRF + HORIZONTAL_SUM);
                this.$horzSumContent = this.$container.querySelector("." + BODY_PRF + HORIZONTAL_SUM);
                if ($follower) {
                    this.$depLeftmostHeader = $follower.querySelector("." + HEADER_PRF + LEFTMOST);
                    this.$depLeftmostBody = $follower.querySelector("." + BODY_PRF + LEFTMOST);
                    this.$depDetailHeader = $follower.querySelector("." + HEADER_PRF + DETAIL);
                    this.$depDetailBody = $follower.querySelector("." + BODY_PRF + DETAIL); 
                }
            }
            
            /**
             * Handle.
             */
            handle() {
                let self = this;
                self.$areaAgency = document.createElement("div");
                self.$areaAgency.className = AREA_AGENCY;
                self.$areaAgency.style.position = "relative";
                self.$areaAgency.style.width = self.$container.style.width;
                self.headerWrappers[0].insertAdjacentElement("beforebegin", self.$areaAgency);
                _.forEach(self.headerWrappers, function($wrapper: HTMLElement, index: number) {
                    if (index === self.headerWrappers.length - 1) return;
                    let $line = document.createElement("div");
                    $line.className = AREA_LINE;
                    $.data($line, RESIZE_AREA, $wrapper);
                    $line.style.position = "absolute";
                    $line.style.cursor = "ew-resize";
                    $line.style.width = "4px";
                    $line.style.zIndex = 2;
                    $line.style.marginLeft = "0px";  
                    self.$areaAgency.appendChild($line);
                    // Line positions
                    let height = parseFloat($wrapper.style.height) + parseFloat(self.bodyWrappers[index].style.height); 
                    let left = $wrapper.offsetWidth + (selector.offset($wrapper).left - selector.offset(self.$areaAgency).left);
                    $line.style.left = left + "px";
                    $line.style.height = height + "px";
                    if ($wrapper.classList.contains(HEADER_PRF + LEFTMOST) 
                        && self.headerWrappers[index + 1].classList.contains(HEADER_PRF + MIDDLE)) 
                        helper.addClass($line, STAY_CLS);
                });
                self.$areaAgency.addXEventListener(events.MOUSE_DOWN, self.cursorDown.bind(self)); 
            }
            
            /**
             * Cursor down.
             */
            cursorDown(event: any) {
                let self = this;
                if (self.actionDetails) {
                    self.cursorUp(event);
                }
                let $targetGrip = event.target;
                if ($targetGrip.classList.contains(STAY_CLS)) return;
                let gripIndex = selector.index($targetGrip);
                let $leftArea = $.data($targetGrip, RESIZE_AREA);
                let $rightArea = self.headerWrappers[gripIndex + 1];
                let leftWidth = $leftArea.style.width;
                let rightWidth = !util.isNullOrUndefined($rightArea) ? $rightArea.style.width : 0;
                let leftHorzSumWidth;
                if (self.$leftHorzSumHeader) {
                    leftHorzSumWidth = self.$leftHorzSumHeader.style.width;
                } else if (self.headerWrappers[0].classList.contains(HEADER_PRF + LEFTMOST)) {
                    leftHorzSumWidth = parseFloat(self.headerWrappers[0].style.width);
                    if (self.headerWrappers[1].classList.contains(HEADER_PRF + MIDDLE)) {
                        leftHorzSumWidth += (parseFloat(self.headerWrappers[1].style.width) + DISTANCE);
                    }
                }
                self.actionDetails = {
                    $targetGrip: $targetGrip,
                    gripIndex: gripIndex,
                    $leftArea: $leftArea,
                    $rightArea: $rightArea,
                    xCoord: getCursorX(event),
                    rightAreaPosLeft: $rightArea ? $rightArea.style.left : 0,
                    widths: {
                        left : parseFloat(leftWidth),
                        right: parseFloat(rightWidth),
                        leftHorzSum: parseFloat(leftHorzSumWidth)
                    },
                    changedWidths: {
                        left: parseFloat(leftWidth),
                        right: parseFloat(rightWidth),
                        leftHorzSum: parseFloat(leftHorzSumWidth)
                    }
                };
                self.$ownerDoc.addXEventListener(events.MOUSE_MOVE, self.cursorMove.bind(self));
                self.$ownerDoc.addXEventListener(events.MOUSE_UP, self.cursorUp.bind(self));
                events.trigger(self.$container, events.AREA_RESIZE_STARTED, [ $leftArea, $rightArea, leftWidth, rightWidth ]);
                event.preventDefault();
            }
            
            /**
             * Cursor move.
             */
            cursorMove(event: any) {
                let self = this;
                if (!self.actionDetails) return;
                let distance = getCursorX(event) - self.actionDetails.xCoord;
                if (distance === 0) return;
                let $bodyLeftArea, $bodyRightArea, leftWidth, rightWidth;
                // TODO: Use if here because there may be changes later.
                if (distance > 0) {
                    leftWidth = self.actionDetails.widths.left + distance;
                    rightWidth = self.actionDetails.widths.right - distance;
                } else {
                    leftWidth = self.actionDetails.widths.left + distance;
                    rightWidth = self.actionDetails.widths.right - distance;
                }
                if (!self.isResizePermit(leftWidth, rightWidth)) return;
                self.actionDetails.changedWidths.left = leftWidth;
                self.actionDetails.changedWidths.right = rightWidth;
                if (self.actionDetails.$leftArea) {
                    self.setWidth(self.actionDetails.$leftArea, leftWidth);
                    $bodyLeftArea = self.bodyWrappers[self.actionDetails.gripIndex];
                    if (self.actionDetails.gripIndex === self.bodyWrappers.length - 1) {
                        self.setWidth($bodyLeftArea, leftWidth + helper.getScrollWidth());
                    } else {
                        self.setWidth($bodyLeftArea, leftWidth);
                    }
                }
                let newPosLeft;
                if (self.actionDetails.$rightArea) {
                    self.setWidth(self.actionDetails.$rightArea, rightWidth);
                    newPosLeft = (parseInt(self.actionDetails.rightAreaPosLeft) + distance) + "px";
                    self.actionDetails.$rightArea.style.left = newPosLeft;
                    $bodyRightArea = self.bodyWrappers[self.actionDetails.gripIndex + 1];
                    if (self.actionDetails.gripIndex === self.bodyWrappers.length - 2) {
                        self.setWidth($bodyRightArea, rightWidth + helper.getScrollWidth());    
                    } else {
                        self.setWidth($bodyRightArea, rightWidth);
                    }
                    $bodyRightArea.style.left = newPosLeft;
                }
                self.reflectSumTblsSize(distance, leftWidth, rightWidth, newPosLeft);
                events.trigger(self.$container, events.AREA_RESIZE, [ self.actionDetails.$leftArea, self.actionDetails.$rightArea, leftWidth, rightWidth ]);
            }
            
            /**
             * Reflect size.
             */
            reflectSumTblsSize(diff: number, leftWidth: number, rightWidth: number, posLeft: string) {
                let self = this;
                let $leftArea = self.actionDetails.$leftArea;
                let $rightArea = self.actionDetails.$rightArea;
                let scrollWidth = helper.getScrollWidth();
                if ($rightArea && $rightArea.classList.contains(HEADER_PRF + DETAIL)) {
                    let horzLeftWidth = self.actionDetails.widths.leftHorzSum + diff;
                    self.setWidth(self.$leftHorzSumHeader, horzLeftWidth);
                    self.setWidth(self.$leftHorzSumContent, horzLeftWidth);
                    self.setWidth(self.$horzSumHeader, rightWidth);
                    self.setWidth(self.$horzSumContent, rightWidth + scrollWidth);
                    if (self.$horzSumHeader) {
                        self.$horzSumHeader.style.left = posLeft;
                        self.$horzSumContent.style.left = posLeft;
                    }
                    if (self.$depLeftmostHeader) {
                        self.setWidth(self.$depLeftmostHeader, horzLeftWidth);
                        self.setWidth(self.$depLeftmostBody, horzLeftWidth);
                        self.setWidth(self.$depDetailHeader, rightWidth);
                        self.setWidth(self.$depDetailBody, rightWidth + scrollWidth);
                        self.$depDetailHeader.style.left = posLeft;
                        self.$depDetailBody.style.left = posLeft;
                    }
                } else if ($leftArea && $leftArea.classList.contains(HEADER_PRF + DETAIL)) {
                    self.setWidth(self.$horzSumHeader, leftWidth);
                    self.setWidth(self.$horzSumContent, leftWidth + scrollWidth);
                    if (self.$depDetailHeader) {
                        self.setWidth(self.$depDetailHeader, leftWidth);
                        self.setWidth(self.$depDetailBody, leftWidth + scrollWidth);
                    }
                }
            }
            
            /**
             * Check resize permit.
             */
            isResizePermit(leftWidth: number, rightWidth: number) {
                let self = this;
                let leftAreaMaxWidth = 0, rightAreaMaxWidth = 0;
                if (leftWidth <= 20 || (self.actionDetails.widths.right > 0 && rightWidth <= 20)) return false;
                if (self.actionDetails.$leftArea) {
                    let leftAreaColGroup = self.actionDetails.$leftArea.querySelectorAll("table > colgroup > col");
                    let size = leftAreaColGroup.length;
                    for (let i = 0; i < size; i++) {
                        leftAreaMaxWidth += (parseFloat(leftAreaColGroup[i].style.width) + 1);
                    }
                    let maxWidth = parseFloat(self.actionDetails.$leftArea.style.maxWidth);
                    if (leftWidth >= leftAreaMaxWidth
                        || (!isNaN(maxWidth) && leftWidth >= maxWidth)) return false;               
                }
                if (self.actionDetails.$rightArea) {
                    let rightAreaColGroup = self.actionDetails.$rightArea.querySelectorAll("table > colgroup > col");
                    let size = rightAreaColGroup.length;
                    for (let i = 0; i < size; i++) {
                        rightAreaMaxWidth += (parseFloat(rightAreaColGroup[i].style.width) + 1);
                    }
                    let maxWidth = parseFloat(self.actionDetails.$rightArea.style.maxWidth);
                    if (rightWidth >= rightAreaMaxWidth
                        ||(!isNaN(maxWidth) && rightWidth >= maxWidth) return false;
                }
                return true;
            }
            
            /**
             * Cursor up.
             */
            cursorUp(event: any) {
                let self = this;
                if (!self.actionDetails) return;
                self.$ownerDoc.removeXEventListener(events.MOUSE_MOVE);
                self.$ownerDoc.removeXEventListener(events.MOUSE_UP);
                self.syncLines();
                events.trigger(self.$container, events.AREA_RESIZE_END, 
                                [ self.actionDetails.$leftArea, self.actionDetails.$rightArea, 
                                  self.actionDetails.changedWidths.left, self.actionDetails.changedWidths.right ]);
                self.actionDetails = null;
            }
            
            /**
             * Set width.
             */
            setWidth($wrapper: HTMLElement, width: any) {
                if (util.isNullOrUndefined($wrapper)) return;
                $wrapper.style.width = parseFloat(width) + "px";
            }
            
            /**
             * Sync lines.
             */
            syncLines() {
                let self = this;
                self.$areaAgency.style.width = self.$container.style.width;
                
                _.forEach(self.headerWrappers, function($wrapper: HTMLElement, index: number) {
                    let height = parseFloat($wrapper.style.height) + parseFloat(self.bodyWrappers[index].style.height); 
                    let left = $wrapper.offsetWidth + (selector.offset($wrapper).left - selector.offset(self.$areaAgency).left);
                    let div = self.$areaAgency.querySelectorAll("div")[index];
                    if (!div) return false;
                    div.style.left = left + "px";
                    div.style.height = height + "px";
                });
            }
        }
        
        /**
         * Get cursorX.
         */
        function getCursorX(event: any) {
            return event.pageX;
        }
        
        /**
         * Line styles.
         */
        function lineStyles(marginLeft: string) {
            return { position: "absolute", cursor: "ew-resize", width: "4px", zIndex: 2, marginLeft: marginLeft };
        }
        
        /**
         * Fit window height.
         */
        export function fitWindowHeight($container: HTMLElement, wrappers: Array<HTMLElement>, horzSumExists: boolean) {
            let height = window.innerHeight - parseInt($.data($container, internal.Y_OCCUPY)) - 100;
            let $horzSumHeader, $horzSumBody, decreaseAmt; 
            wrappers = wrappers || selector.queryAll($container, "div[class*='" + BODY_PRF + "']").filter(function(e) {
                return !e.classList.contains(BODY_PRF + HORIZONTAL_SUM) && !e.classList.contains(BODY_PRF + LEFT_HORZ_SUM);
            });
            
            if (horzSumExists) {
                $horzSumHeader = $container.querySelector("." + HEADER_PRF + HORIZONTAL_SUM);
                $horzSumBody = $container.querySelector("." + BODY_PRF + HORIZONTAL_SUM);
                decreaseAmt = parseFloat($horzSumHeader.style.height) + parseFloat($horzSumBody.style.height) + DISTANCE + SPACE;
                height -= decreaseAmt;
            }
            _.forEach(wrappers, function($wrapper: JQuery) {
                if (($wrapper.style.overflowX && $wrapper.style.overflowX === "scroll") 
                    || ($wrapper.style.overflow && $wrapper.style.overflow === "scroll")) {
                    $wrapper.style.height = height + "px";
                } else {
                    $wrapper.style.height = (height - helper.getScrollWidth()) + "px";
                }
            });
            
            if (horzSumExists) {
                repositionHorzSum($container, $horzSumHeader, $horzSumBody);
            }
            
            let cHeight = 0, showCount = 0;
            let stream = selector.queryAll($container, "div[class*='" + DETAIL + "'], div[class*='" + LEFT_HORZ_SUM + "']"); 
            stream.forEach(function(e) {
                if (e.style.display !== "none") {
                    showCount++;
                    cHeight += parseFloat(e.style.height);
                }
            });
            if (showCount === 4) {
                cHeight += (SPACE + DISTANCE);
            }
            $container.style.height = (cHeight + SPACE) + "px";
            events.trigger($container, events.BODY_HEIGHT_CHANGED, height);
        }
        
        /**
         * Fit window width.
         */
        export function fitWindowWidth($container: HTMLElement) {
            let table = $.data($container, NAMESPACE);
            if (table.$commander) return;
            let $vertSumHeader = $container.querySelector("." + HEADER_PRF + VERTICAL_SUM);
            let $vertSumContent = $container.querySelector("." + BODY_PRF + VERTICAL_SUM);
            let $detailHeader = $container.querySelector("." + HEADER_PRF + DETAIL);
            let $detailBody = $container.querySelector("." + BODY_PRF + DETAIL);
            let width = window.innerWidth - selector.offset($detailHeader).left;
            let scrollWidth = helper.getScrollWidth();
            let $sup = table.$follower;
            if ($vertSumHeader && $vertSumHeader.style.display !== "none") {
                width = width - parseFloat($.data($container, internal.X_OCCUPY)) - parseFloat($vertSumContent.style.width);
                if (!util.isNullOrUndefined($detailHeader.style.maxWidth)
                    && width >= parseFloat($detailHeader.style.maxWidth)) return;
                $container.style.width = (parseFloat($container.style.width) + (width - parseFloat($detailBody.style.width))) + "px";
                $detailHeader.style.width = width + "px";
                $detailBody.style.width = width + "px";
                let $horzSumHeader = $container.querySelector("." + HEADER_PRF + HORIZONTAL_SUM);
                if ($horzSumHeader) {
                    $horzSumHeader.style.width = width + "px";
                }
                let $horzSumContent = $container.querySelector("." + BODY_PRF + HORIZONTAL_SUM);
                if ($horzSumContent) {
                    $horzSumContent.style.width = (width + helper.getScrollWidth()) + "px";
                }
                repositionVertSum($container, $vertSumHeader, $vertSumContent);
                syncDetailAreaLine($container, $detailHeader, $detailBody);
                if ($sup) {
                    let $supHeader = $sup.querySelector("." + HEADER_PRF + DETAIL);
                    if ($supHeader) {
                        $supHeader.style.width = width + "px";
                        $sup.querySelector("." + BODY_PRF + DETAIL).style.width = (width + scrollWidth) + "px";  
                    }
                }
                return;
            }
            
            let $horzSumHeader = $container.querySelector("." + HEADER_PRF + HORIZONTAL_SUM);
            let $horzSumContent = $container.querySelector("." + BODY_PRF + HORIZONTAL_SUM);
            width = width - parseFloat($.data($container, internal.X_OCCUPY));
            if (!util.isNullOrUndefined($detailHeader.style.maxWidth) 
                && width >= parseFloat($detailHeader.style.maxWidth)) return;
            $detailHeader.style.width = (width - scrollWidth) + "px";
            $container.style.width = (parseFloat($container.style.width) 
                    + (width - parseFloat($detailBody.style.width))) + "px";
            $detailBody.style.width = width + "px";
            if ($horzSumHeader && $horzSumHeader.style.display !== "none") {
                $horzSumHeader.style.width = (width - scrollWidth) + "px";
                $horzSumContent.style.width = width + "px";
            }
            if ($sup) {
                let $supHeader = $sup.querySelector("." + HEADER_PRF + DETAIL);
                if ($supHeader) {
                    $supHeader.style.width = (width - scrollWidth) + "px";
                    $sup.querySelector("." + BODY_PRF + DETAIL).style.width = width + "px";
                }
            }
        }
        
        /**
         * Sync detail area line.
         */
        function syncDetailAreaLine($container: HTMLElement, $detailHeader: HTMLElement, $detailBody: HTMLElement) {
            let $agency = $container.querySelector("." + resize.AREA_AGENCY);
            if (!$agency) return;
            let height = parseFloat($detailHeader.style.height) + parseFloat($detailBody.style.height); 
            let left = $detailHeader.offsetWidth + (selector.offset($detailHeader).left - selector.offset($agency).left);
            let index;
            selector.queryAll($container, "div[class*='" + HEADER_PRF + "']").forEach(function(e: HTMLElement, idx: number) {
                if (e.classList.contains(HEADER_PRF + DETAIL)) {
                    index = idx;
                    return false;
                }
            });
            let div = $agency.querySelectorAll("div")[index];
            div.style.left = left + "px";
            div.style.height = height + "px";
        }
        
        /**
         * Reposition horzSum.
         */
        export function repositionHorzSum($container: HTMLElement, $horzSumHeader?: HTMLElement, $horzSumBody?: HTMLElement) {
            $horzSumHeader = $horzSumHeader || $container.querySelector("." + HEADER_PRF + HORIZONTAL_SUM);
            $horzSumBody = $horzSumBody || $container.querySelector("." + BODY_PRF + HORIZONTAL_SUM);
            if (!$horzSumHeader) return;
            let headerTop = parseFloat($container.querySelector("." + HEADER_PRF + DETAIL).style.height) 
                            + parseFloat($container.querySelector("." + BODY_PRF + DETAIL).style.height) + DISTANCE + SPACE;
            let bodyTop = headerTop + DISTANCE + parseFloat($horzSumHeader.style.height);
            $container.querySelector("." + HEADER_PRF + LEFT_HORZ_SUM).style.top = headerTop + "px";
            $container.querySelector("." + BODY_PRF + LEFT_HORZ_SUM).style.top = bodyTop + "px";
            $horzSumHeader.style.top = headerTop + "px";
            $horzSumBody.style.top = bodyTop + "px";
        }
        
        /**
         * Reposition vertSum.
         */
        export function repositionVertSum($container: HTMLElement, $vertSumHeader?: HTMLElement, $vertSumContent?: HTMLElement) {
            $vertSumHeader = $vertSumHeader || $container.querySelector("." + HEADER_PRF + VERTICAL_SUM);
            $vertSumContent = $vertSumContent || $container.querySelector("." + BODY_PRF + VERTICAL_SUM);
            let $detailHeader = $container.querySelector("." + HEADER_PRF + DETAIL);
            let posLeft = $detailHeader.style.left;
            let vertSumLeft = parseFloat(posLeft) + parseFloat($detailHeader.style.width) + DISTANCE;
            $vertSumHeader.style.left = vertSumLeft + "px";
            $vertSumContent.style.left = vertSumLeft + "px";
        }
        
        /**
         * Set height.
         */
        export function setHeight($container: HTMLElement, height: any) {
            selector.queryAll($container, "div[class*='" + BODY_PRF + "']").forEach(function(e) {
                if (e.classList.contains(BODY_PRF + HORIZONTAL_SUM) || e.classList.contains(BODY_PRF + LEFT_HORZ_SUM)) return;
                e.style.height = height + "px";
            });
            
            let cHeight = 0, showCount = 0;
            let stream = selector.queryAll($container, "div[class*='" + DETAIL + "'], div[class*='" + LEFT_HORZ_SUM + "']"); 
            stream.forEach(function(e) {
                if (e.style.display !== "none") {
                    showCount++;
                    cHeight += parseFloat(e.style.height);
                }
            });
            if (showCount === 4) {
                cHeight += (SPACE + DISTANCE);
            }
            $container.style.height = (cHeight + SPACE) + "px";
            events.trigger($container, events.BODY_HEIGHT_CHANGED, height);
        }
        
        /**
         * On area complete.
         */
        export function onAreaComplete(event: any, $leftArea: HTMLElement, $rightArea: HTMLElement, 
                                        leftWidth: number, rightWidth: number) {
            let self = this;
            saveSizes(self.$container, $leftArea, $rightArea, leftWidth, rightWidth);
        }
        
        /**
         * Save sizes.
         */
        export function saveSizes($container: HTMLElement, $leftArea: HTMLElement, $rightArea: HTMLElement, 
                                    leftWidth: number, rightWidth: number) {
            if ($leftArea) {
                storage.area.save($container, $.data($leftArea, internal.EX_PART), leftWidth);
            }
            if ($rightArea) {
                storage.area.save($container, $.data($rightArea, internal.EX_PART), rightWidth); 
            }
        }
        
        /**
         * On body height changed.
         */
        export function onBodyHeightChanged(event: any) {
            let $container = event.target;
            let height = event.detail;
            storage.tableHeight.save($container, height);
            repositionHorzSum($container);
        }
    }
    
    module storage {
        export let AREA_WIDTHS = "areawidths";
        export let TBL_HEIGHT = "tableheight";
        
        abstract class Store {
            /**
             * Get storage key.
             */
            abstract getStorageKey($container: HTMLElement);
            
            /**
             * Check value exists.
             */
            initValueExists($container: HTMLElement) {
                let self = this;
                let storeKey = self.getStorageKey($container);
                let value = uk.localStorage.getItem(storeKey);
                return value.isPresent();
            }
            
            /**
             * Get store item.
             */
            getStoreItem($container: HTMLElement, item: string) {
                return request.location.current.rawUrl + "/" + $container.id + "/" + item;
            }
            
            /**
             * Get value.
             */
            getValue($container: HTMLElement) {
                let storeKey = this.getStorageKey($container);
                return uk.localStorage.getItem(storeKey);
            }
        }
        
        export module area {
            class Cache extends Store {
                getStorageKey($container: HTMLElement) {
                    return this.getStoreItem($container, AREA_WIDTHS);
                } 
            }
            let cache = new Cache();
            
            /**
             * Init.
             */
            export function init($container: HTMLElement, parts: Array<HTMLElement>) {
                if (cache.initValueExists($container)) {
                    return;
                }
                let partWidths: {[ key: string ]: number } = {};
                _.forEach(parts, function(part: HTMLElement, index: number) {
                    let key = helper.getClassOfHeader(part);
                    partWidths[key] = parseFloat(part.style.width);
                });
                saveAll($container, partWidths);
            }
            
            /**
             * Load.
             */
            export function load($container: HTMLElement) {
                let storeKey = cache.getStorageKey($container);
                uk.localStorage.getItem(storeKey).ifPresent((parts) => {
                    let widthParts: any = JSON.parse(parts);
                    setWidths($container, widthParts);
                    return null;
                });
            }
            
            /**
             * Save.
             */
            export function save($container: HTMLElement, keyClass: string, partWidth: number) {
                let storeKey = cache.getStorageKey($container);
                let partsWidth = uk.localStorage.getItem(storeKey);
                let widths = {};
                if (partsWidth.isPresent()) {
                    widths = JSON.parse(partsWidth.get());
                    widths[keyClass] = partWidth;
                } else {
                    widths[keyClass] = partWidth;
                }
                uk.localStorage.setItemAsJson(storeKey, widths);
            }
            
            /**
             * Save all.
             */
            function saveAll($container: HTMLElement, widths: {[ key: string ]: number }) {
                let storeKey = cache.getStorageKey($container);
                let partWidths = uk.localStorage.getItem(storeKey);
                if (!partWidths.isPresent()) {
                    uk.localStorage.setItemAsJson(storeKey, widths);
                }
            }
            
            /**
             * Get part widths.
             */
            export function getPartWidths($container: HTMLElement) {
                return cache.getValue($container);
            }            
            
            /**
             * Set widths.
             */
            function setWidths($container: JQuery, parts: {[ key: string ]: number }) {
                let partKeys = Object.keys(parts);
                _.forEach(partKeys, function(keyClass: any, index: number) {
                    setWidth($container, keyClass, parts[keyClass]);
                });
            }
            
            /**
             * Set width.
             */
            function setWidth($container: HTMLElement, keyClass: string, width: number) {
                selector.find($container, "." + keyClass).width(width);
                selector.find($container, "." + Connector[keyClass]).width(width);
            }
              
        }
        
        export module tableHeight {
            class Cache2 extends Store {
                getStorageKey($container: HTMLElement) {
                    return this.getStoreItem($container, TBL_HEIGHT);
                }
            }
            let cache = new Cache2();
            
            /**
             * Init.
             */
            export function init($container: HTMLElement) {
                if (cache.initValueExists($container)) {
                    return;
                }
                let $bodies = $container.querySelectorAll("div[class*='" + BODY_PRF + "']");
                if ($bodies.length === 0) return;
                save($container, $bodies[0].style.height);
            }
            
            /**
             * Load.
             */
            export function load($container: HTMLElement) {
                let storeKey = cache.getStorageKey($container);
                uk.localStorage.getItem(storeKey).ifPresent((height) => {
                    let h = JSON.parse(height);
                    resize.setHeight($container, height);
                    return null;
                });
            }
            
            /**
             * Get.
             */
            export function get($container: HTMLElement) {
                return cache.getValue($container);
            }
            
            /**
             * Save.
             */
            export function save($container: HTMLElement, height: number) {
                let storeKey = cache.getStorageKey($container);
                uk.localStorage.setItemAsJson(storeKey, height);
            }
        }
    }
    
    module scroll {
        export let SCROLL_SYNCING = "scroll-syncing";
        export let VERT_SCROLL_SYNCING = "vert-scroll-syncing";
        
        /**
         * Bind vertWheel.
         */
        export function bindVertWheel($container: HTMLElement, showY?: boolean) {
            let $_container = $($container);
            $container.addXEventListener(events.MOUSE_WHEEL, function(event: any) {
                let delta = event.deltaY;
                let direction = delta > 0 ? -1 : 1;
                let value = $_container.scrollTop() + Math.round(event.deltaY);
//                $container.stop().animate({ scrollTop: value }, 10);
                let os = helper.isIE() ? 110 : 50;
                $_container.scrollTop(value + direction * os);
                event.preventDefault();
                event.stopImmediatePropagation();
            });
            if (!showY && $container.style.overflowY !== "hidden") {
                $container.style.overflowY = "hidden";
            }
        }
        
        /**
         * Unbind vertWheel.
         */
        export function unbindVertWheel($container: HTMLElement) {
            $container.removeXEventListener(events.MOUSE_WHEEL);
            $container.style.overflowY = "scroll";
        }
        
        /**
         * Sync scrolls.
         */
        export function syncDoubDirHorizontalScrolls(wrappers: Array<HTMLElement>) {
            _.forEach(wrappers, function($main: HTMLElement, index: number) {
                if (!$main) return;
                $main.addXEventListener(events.SCROLL_EVT, function() {
                    _.forEach(wrappers, function($depend: HTMLElement, i: number) {
                        if (i === index || !$depend) return;
                        let mainSyncing = $.data($main, SCROLL_SYNCING);
                        if (!mainSyncing) {
                            $.data($depend, SCROLL_SYNCING, true);
                            $depend.scrollLeft = $main.scrollLeft;
                        }
                    });
                    $.data($main, SCROLL_SYNCING, false);
                });
            });
        }
        
        /**
         * Sync scrolls.
         */
        export function syncDoubDirVerticalScrolls(wrappers: Array<HTMLElement>) {
            _.forEach(wrappers, function($main: HTMLElement, index: number) {
                    $main.addXEventListener(events.SCROLL_EVT, function(event: any) {
                        _.forEach(wrappers, function($depend: HTMLElement, i: number) {
                            if (i === index) return;
                            let mainSyncing = $.data($main, VERT_SCROLL_SYNCING);
                            if (!mainSyncing) {
                                $.data($depend, VERT_SCROLL_SYNCING, true);
                                $depend.scrollTop = $main.scrollTop;
                            }
                        });
                        $.data($main, VERT_SCROLL_SYNCING, false);
                    });
            });
        }
        
        /**
         * Sync scroll.
         */
        export function syncHorizontalScroll($headerWrap: HTMLElement, $bodyWrap: HTMLElement) {
            $bodyWrap.addXEventListener(events.SCROLL_EVT, function() {
                $headerWrap.scrollLeft = $bodyWrap.scrollLeft;
            });
        }
        
        /**
         * Sync scroll.
         */
        export function syncVerticalScroll($pivotBody: HTMLElement, bodyWraps: Array<HTMLElement>) {
            $pivotBody.addXEventListener(events.SCROLL_EVT, function() {
                _.forEach(bodyWraps, function(body: HTMLElement) {
                    body.scrollTop = $pivotBody.scrollTop;
                });
            });
        }
    }
    
    module controls {
        export let LINK_BUTTON: string = "link";
        export let LINK_CLS: string = "x-link";
        export let CHECKED_KEY: string = "xCheckbox";
        export let CHECKBOX_COL_WIDTH = 40;
        
        /**
         * Check.
         */
        export function check(td: HTMLElement, column: any, data: any, action: any) {
            if (!util.isNullOrUndefined(column.control)) {
                switch(column.control) {
                    case LINK_BUTTON:
                        let a = document.createElement("a");
                        a.classList.add(LINK_CLS);
                        a.addXEventListener(events.CLICK_EVT, function(evt) {
                            action();
                        });
                        a.innerText = data;
                        td.appendChild(a);
                        break;
                }
            }
        }
        
        /**
         * Add checkbox def.
         */
        export function addCheckBoxDef(arr: any) {
            _.forEach(arr, function(opt: any) {
                opt.columns.unshift({ key: CHECKED_KEY, headerText: CHECKED_KEY, width: CHECKBOX_COL_WIDTH + "px" });
            });
        }
        
        /**
         * Create checkbox.
         */
        export function createCheckBox($grid: HTMLElement, ui: any) {
            var checkBoxText: string;
            var $wrapper = document.createElement("div");
            $wrapper.className = "nts-checkbox-container";
            $wrapper.addXEventListener(events.CLICK_EVT, function(e) {
                if ($grid && errors.occurred(helper.closest($grid, "." + NAMESPACE))) e.preventDefault();
            });

            var $checkBoxLabel = document.createElement("label");
            $checkBoxLabel.className = "ntsCheckBox";
            var $checkBox = document.createElement("input");
            $checkBox.type = "checkbox";
            $checkBox.addXEventListener("change", function() {
                let cellCoord = helper.getCellCoord(helper.closest($checkBox, "td"));
                let rowIndex = 0;
                if (cellCoord) rowIndex = cellCoord.rowIdx;
                ui.onChecked(selector.is($checkBox, ":checked"), rowIndex);
            });
            $checkBoxLabel.appendChild($checkBox);
            var $box = document.createElement("span");
            $box.className = "box";
            $checkBoxLabel.appendChild($box);
            if (ui.text && ui.text.length > 0) {
                var label = document.createElement("span");
                label.className = "label";
                label.textContent = ui.text;
                $checkBoxLabel.appendChild(label); 
            }
            $wrapper.appendChild($checkBoxLabel);

            var checked = ui.initValue !== undefined ? ui.initValue : true;
            var $checkBox = $wrapper.querySelector("input[type='checkbox']");

            if (checked === true) $checkBox.checked = true;
            else $checkBox.checked = false;
            return $wrapper;
        }
        
        /**
         * Checkbox cell styles.
         */
        export function checkBoxCellStyles() {
            return { padding: "1px 1px", textAlign: "center" };
        }
        
        /**
         * Tick.
         */
        export function tick(checked: boolean, $grid: HTMLElement, isHeader: boolean, rowIdx?: any) {
            let $checkBox;
            let ds = internal.getDataSource($grid);
            if (isHeader) {
                selector.queryAll($grid, "tr").forEach(function(r) {
                    let td = r.querySelectorAll("td")[0];
                    if (!td) return;
                    $checkBox = td.querySelector("input");
                    if (checked) {
                        $checkBox.checked = true;
                    } else {
                        $checkBox.checked = false;
                    }
                });
                let rows = $.data($grid, internal.SELECTED_ROWS);
                if (checked) {
                    if (!rows) {
                        rows = {};
                        rows.selectAll = true;
                        $.data($grid, internal.SELECTED_ROWS, rows);
                    } else {
                        rows.selectAll = true;
                    }
                    for (let i = 0; i < ds.length; i++) {
                        selection.selectRow($grid, i);
                    }
                } else {
                    rows.selectAll = false;
                    for (let i = 0; i < ds.length; i++) {
                        selection.deselectRow($grid, i);
                    }
                }
            } else {
                let $row = selection.rowAt($grid, rowIdx);
                if (!$row) return;
                let $cells = $row.querySelectorAll("td");
                if (!$cells || $cells.length === 0) return;
                $checkBox = $cells[0].querySelector("input");
                if (checked) {
                    $checkBox.checked = true;
                    selection.selectRow($grid, rowIdx);
                } else {
                    $checkBox.checked = false;
                    selection.deselectRow($grid, rowIdx);
                }
                
                let rows = $.data($grid, internal.SELECTED_ROWS);
                let $allBox = selector.classSiblings($grid, HEADER_PRF + LEFTMOST)[0].querySelectorAll("table tr")[0]
                                .querySelectorAll("td")[0].querySelector("input");
                if (rows.count === ds.length) {
                    rows.selectAll = true;
                    if (!selector.is($allBox, ":checked")) $allBox.checked = true;
                } else {
                    rows.selectAll = false;
                    if (selector.is($allBox, ":checked")) $allBox.checked = false;
                }
            }
        }
    }
    
    module events {
        export let SCROLL_EVT = "scroll";
        export let CLICK_EVT = "click";
        export let MOUSE_DOWN = "mousedown";
        export let MOUSE_MOVE = "mousemove";
        export let MOUSE_UP = "mouseup";
        export let MOUSE_OVER = "mouseover";
        export let MOUSE_ENTER = "mouseenter";
        export let MOUSE_OUT = "mouseout";
        export let FOCUS_IN = "focusin";
        export let PASTE = "paste";
        export let MOUSE_WHEEL = "wheel";
        export let RESIZE = "resize";
        export let KEY_DOWN = "keydown";
        export let KEY_UP = "keyup";
        export let CM = "contextmenu";
        export let AREA_RESIZE_STARTED = "extablearearesizestarted";
        export let AREA_RESIZE = "extablearearesize";
        export let AREA_RESIZE_END = "extablearearesizeend";
        export let BODY_HEIGHT_CHANGED = "extablebodyheightchanged";
        export let OCCUPY_UPDATE = "extableoccupyupdate";
        export let START_EDIT = "extablestartedit";
        export let STOP_EDIT = "extablestopedit";
        export let CELL_UPDATED = "extablecellupdated";
        export let ROW_UPDATED = "extablerowupdated";
        export let POPUP_SHOWN = "xpopupshown";
        export let POPUP_INPUT_END = "xpopupinputend";
        export let ROUND_RETREAT = "extablecellretreat";
        export let CHECK_ALL = "extableselectallrows";
        export let CHECK_ROW = "extableselectrow";
        export let MOUSEIN_COLUMN = "extablemouseincolumn";
        export let MOUSEOUT_COLUMN = "extablemousoutcolumn";
        export let RENDERED = "extablerowsrendered";
        export let COMPLETED = "extablecompleted";
        
        window.addXEventListener = document.addXEventListener = Element.prototype.addXEventListener = addEventListener;
        window.removeXEventListener = document.removeXEventListener = Element.prototype.removeXEventListener = removeEventListener;
        
        /**
         * Trigger.
         */
        export function trigger($target: HTMLElement, eventName: string, args?: any) {
            let event;
            if (window.CustomEvent) {
                event = new CustomEvent(eventName, { detail: args });
            } else {
                event = document.createEvent('CustomEvent');
                event.initCustomEvent(eventName, true, true, args);
            }
            $target.dispatchEvent(event);
        }
        
        function addEventListener(event, cb, opts) {
            let self = this;
            if (!self.ns) self.ns = {};
            if (!self.ns[event]) self.ns[event] = [ cb ];
            else self.ns[event].push(cb);
            self.addEventListener(event.split(".")[0], cb, opts);  
        };
        
        function removeEventListener(event, cb?) {
            let self = this;
            if (!self.ns) return;
            if (cb) {
                let keys = Object.keys(self.ns).filter(function(k) {
                    return (k === event || k === event.split(".")[0])
                            && self.ns[k].indexOf(cb) > -1;
                });
                
                let key;
                if (keys.length > 0) {
                    key = keys[0];
                    self.ns[key].splice(self.ns[key].indexOf(cb), 1);
                    if (self.ns[key].length === 0) delete self.ns[key];
                }
                self.removeEventListener(event.split(".")[0], cb);
                return;
            }
            
            if (!self.ns[event]) return;
            self.ns[event].forEach(function(e) {
                self.removeEventListener(event.split(".")[0], e); 
            });
            delete self.ns[event];
        }
        
        /**
         * On modify.
         */
        export function onModify($exTable: HTMLElement) {
            $exTable.addXEventListener(CELL_UPDATED, function(evt: any) {
                let exTable = $.data($exTable, NAMESPACE);
                if (!exTable) return;
                let ui: any = evt.detail;
                if (ui.value.constructor === Array && (util.isNullOrUndefined(ui.innerIdx) || ui.innerIdx === -1)) {
                    pushChange(exTable, ui.rowIndex, new selection.Cell(ui.rowIndex, ui.columnKey, ui.value[0], 0));
                    pushChange(exTable, ui.rowIndex, new selection.Cell(ui.rowIndex, ui.columnKey, ui.value[1], 1));
                    return;
                }
                pushChange(exTable, ui.rowIndex, ui); 
            });
            
            $exTable.addXEventListener(ROW_UPDATED, function(evt: any) {
                let exTable = $.data($exTable, NAMESPACE);
                if (!exTable) return;
                let ui: any = evt.detail;
                let cells = [];
                _.forEach(Object.keys(ui.data), function(k, i) {
                    if (ui.data[k].constructor === Array && ui.data[k].length === 2) {
                        cells.push(new selection.Cell(ui.rowIndex, k, ui.data[k][0], 0));
                        cells.push(new selection.Cell(ui.rowIndex, k, ui.data[k][1], 1));
                        return;
                    }
                    cells.push(new selection.Cell(ui.rowIndex, k, ui.data[k], -1));
                });
                _.forEach(cells, function(c, i) {
                    pushChange(exTable, c.rowIndex, c);
                });
            });
        }
        
        /**
         * Push change.
         */
        function pushChange(exTable: any, rowIdx: any, cell: any) {
            let modifies = exTable.modifications;
            if (!modifies) {
                exTable.modifications = {};
                exTable.modifications[rowIdx] = [ cell ];
                return;
            }
            if (!modifies[rowIdx]) {
                modifies[rowIdx] = [ cell ];
                return;
            }
            let rData = modifies[rowIdx];
            let cellUpdated = false;
            _.forEach(rData, function(c, i) {
                if (helper.areSameCells(c, cell)) {
                    rData[i].value = cell.value;
                    cellUpdated = true;
                    return false;
                }
            });
            if (!cellUpdated) {
                modifies[rowIdx].push(cell);
            }
        }
        
        /**
         * Create row ui.
         */
        export function createRowUi(rowIdx: any, data: any) {
            return {
                rowIndex: rowIdx,
                data: data
            };
        }
    }
    
    module feature {
        export let UPDATING = "Updating";
        export let HEADER_ROW_HEIGHT = "HeaderRowHeight";
        export let HEADER_CELL_STYLE = "HeaderCellStyle";
        export let HEADER_POP_UP = "HeaderPopups";
        export let BODY_CELL_STYLE = "BodyCellStyle";
        export let COLUMN_RESIZE = "ColumnResize";
        export let TIME_RANGE = "TimeRange"
        
        /**
         * Is enable.
         */
        export function isEnable(features: any, name: string) {
            return _.find(features, function(feature: any) {
                return feature.name === name;
            }) !== undefined;
        }
        
        /**
         * Find.
         */
        export function find(features: any, name: string) {
            return _.find(features, function(feature: any) {
                return feature.name === name;
            });
        }
    }
    
    module style {
        export let DET_CLS: string = "xdet";
        export let HIDDEN_CLS: string = "xhidden";
        export let SEAL_CLS: string = "xseal";
        
        export class CellStyleParam {
            $cell: HTMLElement;
            cellData: any;
            rowData: any;
            rowIdx: any;
            columnKey: any;
            constructor($cell, cellData, rowData, rowIdx, columnKey) {
                this.$cell = $cell;
                this.cellData = cellData;
                this.rowData = rowData;
                this.rowIdx = rowIdx;
                this.columnKey = columnKey;
            }
        }
        
        export class Cell {
            rowId: any;
            columnKey: any;
            constructor(rowId: any, columnKey: any) {
                this.rowId = rowId;
                this.columnKey = columnKey;
            }
        }
        
        export function detColumn($grid: HTMLElement, row: HTMLElement, rowIdx: any) {
            let $tbl = helper.closest($grid, "." + NAMESPACE);
            let detOpt = $.data($tbl, NAMESPACE).determination;
            if (!detOpt || !$grid.classList.contains(HEADER_PRF + DETAIL)) return;
            _.forEach(detOpt.rows, function(i: any) {
                if (i === rowIdx) {
                    row.addXEventListener(events.MOUSE_DOWN, function(evt: any) {
                        if (!evt.ctrlKey) return;
                        let $main = helper.getMainTable($tbl);
                        let gen = $.data($main, internal.TANGI) || $.data($main, internal.CANON);
                        let ds = gen.dataSource;
                        let primaryKey = helper.getPrimaryKey($main);
                        let start = gen.startIndex || 0;
                        let end = gen.endIndex || ds.length - 1;
                        let $hCell = evt.target;
                        let coord = helper.getCellCoord($hCell);
                        let det = $.data($main, internal.DET);
                        if (!det) {
                            det = {};
                        }
                        
                        let xRows = [];
                        let xCellsInColumn = _.filter(ds, (r, i) => {
                            if (helper.isXCell($main, r[primaryKey], coord.columnKey, style.HIDDEN_CLS, style.SEAL_CLS)) {
                                xRows.push(i);
                                return true;
                            }
                            return false; 
                        });
                        
                        let rows = Object.keys(det);
                        if (rows.length >= (ds.length - xCellsInColumn.length)) {
                            let flaw = false;
                            let indices = {};
                            _.forEach(rows, function(k, i) {
                                let found = false;
                                _.forEach(det[k], (c, j) => { 
                                    if (c === coord.columnKey) {
                                        indices[k] = j;
                                        found = true;
                                        return false;
                                    }
                                });
                                if (!found && !xRows.some((val) => parseInt(k) === val)) {
                                    flaw = true;
                                    return false;
                                }
                            });
                            if (!flaw) {
                                let rKeys = Object.keys(indices); 
                                _.forEach(rKeys, function(k, i) {
                                    let col = det[k].splice(indices[k], 1);
                                    if (det[k].length === 0) delete det[k];
                                    let $c = selection.cellAt($main, k, col[0]);
                                    if ($c) helper.stripCellWith(DET_CLS, $c);
                                });
                                return;
                            }
                        }
                        _.forEach(ds, function(item: any, index: number) {
                            if (index >= start && index < end) {
                                let $c = selection.cellAt($main, index, coord.columnKey);
                                if ($c === intan.NULL || !$c || !helper.isDetable($c)) return;
                                helper.markCellWith(DET_CLS, $c);
                            } else if (helper.isXCell($main, item[primaryKey], coord.columnKey, style.HIDDEN_CLS, style.SEAL_CLS)) return;
                            
                            if (!det[index]) {
                                det[index] = [ coord.columnKey ];
                                $.data($main, internal.DET, det);
                            } else {
                                let dup;
                                _.forEach(det[index], function(key) {
                                    if (key === coord.columnKey) {
                                        dup = true;
                                        return false;
                                    }
                                });
                                if (!dup) {
                                    det[index].push(coord.columnKey);
                                }
                            }
                        });
                    });
                    return false;
                }
            });
        }
        
        export function detCell($grid: HTMLElement, $cell: HTMLElement, rowIdx: any, columnKey: any) {
            let $tbl = helper.closest($grid, "." + NAMESPACE);
            let detOpt = $.data($tbl, NAMESPACE).determination;
            if (!detOpt) return;
            if ($grid.classList.contains(BODY_PRF + LEFTMOST)) {
                _.forEach(detOpt.columns, function(key: any) {
                    if (key === columnKey) {
                        $cell.addXEventListener(events.MOUSE_DOWN, function(evt: any) {
                            if (!evt.ctrlKey) return;
                            let $main = helper.getMainTable($tbl);
                            let coord = helper.getCellCoord($cell);
                            let $targetRow = selection.rowAt($main, coord.rowIdx);
                            if ($targetRow === intan.NULL || !$targetRow) return;
                            
                            let colKeys = _.map(helper.gridVisibleColumns($main), "key");
                            let det = $.data($main, internal.DET);
                            let rowDet;
                            
                            let undetables = [];
                            let detables = selector.queryAll($targetRow, "td").filter(function(e) {
                                return e.style.display !== "none";
                            }).filter(function(e, i) {
                                if (!helper.isDetable(e)) {
                                    undetables.push(i);
                                    return false;
                                }
                                return true;
                            });
                            for (var i = undetables.length - 1; i >= 0; i--) {
                                colKeys.splice(undetables[i], 1);
                            }
                            
                            if (det && (rowDet = det[coord.rowIdx]) && rowDet.length === colKeys.length) {
                                helper.stripCellsWith(DET_CLS, selector.queryAll($targetRow, "td").filter(function(e) {
                                    return e.style.display !== "none";
                                }));
//                                det[coord.rowIdx] = [];
                                delete det[coord.rowIdx];
                                return;
                            }
                            helper.markCellsWith(DET_CLS, detables);
                            
                            if (!det) {
                                det = {};
                                det[coord.rowIdx] = colKeys;
                                $.data($main, internal.DET, det);
                            } else if (!det[coord.rowIdx]) {
                                det[coord.rowIdx] = colKeys;
                            } else {
                                let dup;
                                _.forEach(colKeys, function(k) {
                                    dup = false;
                                    _.forEach(det[coord.rowIdx], function(existedKey) {
                                        if (existedKey === k) {
                                            dup = true;
                                            return false;
                                        }
                                    });
                                    if (!dup) {
                                        det[coord.rowIdx].push(k);
                                    }
                                });
                                
                            }
                        });
                        return false;
                    }
                });
            } else if ($grid.classList.contains(BODY_PRF + DETAIL)) {
                let childCells = $cell.querySelectorAll("." + render.CHILD_CELL_CLS);
                let target = $cell;
                if (childCells.length > 0) {
                    target = childCells;
                    _.forEach(Array.prototype.slice.call(childCells), function(c) {
                        c.addXEventListener(events.MOUSE_DOWN, function(evt: any) {
                            onDetSingleCell(evt, $tbl, $cell, rowIdx, columnKey);
                        });
                    });
                    return;
                }
                target.addXEventListener(events.MOUSE_DOWN, function(evt: any) {
                    onDetSingleCell(evt, $tbl, $cell, rowIdx, columnKey);
                });
            }
        }
        
        /**
         * On det single cell.
         */
        function onDetSingleCell(evt: any, $tbl: HTMLElement, $cell: HTMLElement, rowIdx: any, columnKey: any) {
            if (!evt.ctrlKey || !helper.isDetable($cell)) return;
            let $main = helper.getMainTable($tbl);
            let det = $.data($main, internal.DET);
            if (!det) {
                det = {};
                det[rowIdx] = [ columnKey ];
                $.data($main, internal.DET, det);
            } else if (!det[rowIdx]) {
                det[rowIdx] = [ columnKey ];
            } else {
                let dup = -1;
                _.forEach(det[rowIdx], function(key: any, index: any) {
                    if (key === columnKey) {
                        dup = index;
                        return false;
                    }
                });
                if (dup > -1) {
                    let a = [];
                    det[rowIdx].splice(dup, 1);
                    if (det[rowIdx].length === 0) delete det[rowIdx];
                    helper.stripCellWith(DET_CLS, $cell);
                    return;
                }
                det[rowIdx].push(columnKey);
            }
            helper.markCellWith(DET_CLS, $cell);
        }
    }
    
    module func {
        let LEFT_TBL: string = "leftmost";
        let HORZ_SUM: string = "horizontalSummaries";
        let VERT_SUM: string = "verticalSummaries";
        $.fn.exTable = function(name: any, ...params: Array<any>) {
            let self = this;
            switch(name) {
                case "setHeight":
                    resize.setHeight(self, params[0]); 
                    break;
                case "gridHeightMode":
                    changeGridHeightMode(self, params[0]);
                    break;
                case "hideHorizontalSummary": 
                    hideHorzSum(self);
                    break;
                case "showHorizontalSummary":
                    showHorzSum(self);
                    break;
                case "hideVerticalSummary":
                    hideVertSum(self);
                    break;
                case "showVerticalSummary":
                    showVertSum(self);
                    break;
                case "updateTable": 
                    updateTable(self, params[0], params[1], params[2], params[3]);
                    break;
                case "updateMode":
                    return setUpdateMode(self, params[0], params[1]);
                case "viewMode":
                    return setViewMode(self, params[0], params[1]);
                case "mode":
                    setMode(self, params[0], params[1], params[2]);
                    break;
                case "pasteOverWrite":
                    setPasteOverWrite(self, params[0]);
                    break;
                case "stickOverWrite":
                    setStickOverWrite(self, params[0]);
                    break;
                case "stickMode":
                    setStickMode(self, params[0]);
                    break;
                case "stickData":
                    setStickData(self, params[0]);
                    break;
                case "stickValidate":
                    setStickValidate(self, params[0]);
                    break;
                case "stickUndo":
                    undoStick(self);
                    break;
                case "clearHistories":
                    clearHistories(self, params[0]);
                    break;
                case "lockCell":
                    lockCell(self, params[0], params[1]);
                    break;
                case "unlockCell":
                    unlockCell(self, params[0], params[1]);
                    break;
                case "popupValue":
                    returnPopupValue(self, params[0]);
                    break;
                case "cellValue":
                    setCellValue(self, params[0], params[1], params[2], params[3]);
                    break;
                case "cellValueByIndex":
                    setCellValueByIndex(self, params[0], params[1], params[2], params[3]);
                    break;
                case "roundGet":
                    roundRetreat(self, params[0]);
                    break;
                case "dataSource":
                    return getDataSource(self, params[0]);
                case "cellByIndex":
                    return getCellByIndex(self, params[0], params[1]);
                case "cellById":
                    return getCellById(self, params[0], params[1]);
                case "updatedCells":
                    return getUpdatedCells(self);
                case "addNewRow":
                    break;
                case "removeRows":
                    break;
                case "rowId":
                    setRowId(self, params[0], params[1]);
                    break;
                case "saveScroll":
                    saveScroll(self);
                    break;
                case "scrollBack":
                    scrollBack(self, params[0]);
                    break;
            }
        };
        
        /**
         * Change grid height mode.
         */
        function changeGridHeightMode($container: JQuery, mode: string) {
            if (mode === DYNAMIC) {
                let bodyWrappers = [], horzSumExists = false;
                let $bodyWrappers = $container.find("div[class*='" + BODY_PRF + "']").each(function() {
                    if ($(this).hasClass(BODY_PRF + HORIZONTAL_SUM) || $(this).hasClass(BODY_PRF + LEFT_HORZ_SUM)) {
                        horzSumExists = true;
                        return;
                    }
                    bodyWrappers.push($(this));
                });
                
                $(window).on(events.RESIZE, $.proxy(resize.fitWindowHeight, undefined, $container[0], bodyWrappers, horzSumExists));
            } else {
                $(window).off(events.RESIZE, resize.fitWindowHeight);
            }
        }
        
        /**
         * Hide horzSum.
         */
        function hideHorzSum($container: JQuery) {
            $container.find("." + HEADER_PRF + LEFT_HORZ_SUM).hide();
            $container.find("." + BODY_PRF + LEFT_HORZ_SUM).hide();
            $container.find("." + HEADER_PRF + HORIZONTAL_SUM).hide();
            $container.find("." + BODY_PRF + HORIZONTAL_SUM).hide();
            resize.fitWindowHeight($container[0], undefined, false);
        }
        
        /**
         * Show horzSum.
         */
        function showHorzSum($container: JQuery) {
            $container.find("." + HEADER_PRF + LEFT_HORZ_SUM).show();
            $container.find("." + BODY_PRF + LEFT_HORZ_SUM).show();
            $container.find("." + HEADER_PRF + HORIZONTAL_SUM).show();
            $container.find("." + BODY_PRF + HORIZONTAL_SUM).show();
            resize.fitWindowHeight($container[0], undefined, true);
        }
        
        /**
         * Hide vertSum.
         */
        function hideVertSum($container: JQuery) {
            $container.find("." + HEADER_PRF + VERTICAL_SUM).hide();
            $container.find("." + BODY_PRF + VERTICAL_SUM).hide();
            resize.fitWindowWidth($container[0]);
            scroll.unbindVertWheel($container.find("." + BODY_PRF + DETAIL)[0]);
        }
        
        /**
         * Show vertSum.
         */
        function showVertSum($container: JQuery) {
            let $vertSumBody = $container.find("." + BODY_PRF + VERTICAL_SUM);
            let $detailBody = $container.find("." + BODY_PRF + DETAIL);
            $container.find("." + HEADER_PRF + VERTICAL_SUM).show();
            $vertSumBody.show();
            resize.fitWindowWidth($container[0]);
            scroll.bindVertWheel($detailBody[0]);
            $vertSumBody.scrollTop($detailBody.scrollTop());
        }
        
        /**
         * Update table.
         */
        function updateTable($container: JQuery, name: string, header: any, body: any, keepStates?: boolean) {
            switch (name) {
                case "leftmost":
                    updateLeftmost($container, header, body);
                    break;
                case "middle": 
                    updateMiddle($container, header, body);
                    break;
                case "detail":
                    updateDetail($container, header, body, keepStates);
                    break;
                case "verticalSummaries":
                    updateVertSum($container, header, body); 
                    break;
                case "leftHorizontalSummaries":
                    updateLeftHorzSum($container, header, body);
                    break;
                case "horizontalSummaries":
                    updateHorzSum($container, header, body);
                    break;
            }
        }
        
        /**
         * Update leftmost.
         */
        function updateLeftmost($container: JQuery, header: any, body: any) {
            let exTable: any = $container.data(NAMESPACE);
            if (header) {
                _.assignIn(exTable.leftmostHeader, header);
                let $header = $container.find("." + HEADER_PRF + LEFTMOST);
                let pu = $header.find("table").data(internal.POPUP);
                $header.empty();
                render.process($header[0], exTable.leftmostHeader, true);
                if (pu && pu.css("display") !== "none") pu.hide();
            }
            if (body) {
                _.assignIn(exTable.leftmostContent, body);
                let $body = $container.find("." + BODY_PRF + LEFTMOST);
                $body.empty();
                render.process($body[0], exTable.leftmostContent, true);
            }
        }
        
        /**
         * Update middle.
         */
        function updateMiddle($container: JQuery, header: any, body: any) {
            let exTable: any = $container.data(NAMESPACE);
            if (header) {
                _.assignIn(exTable.middleHeader, header);
                let $header = $container.find("." + HEADER_PRF + MIDDLE);
                let pu = $header.find("table").data(internal.POPUP);
                $header.empty();
                render.process($header[0], exTable.middleHeader, true);
                if (pu && pu.css("display") !== "none") pu.hide();
            }
            if (body) {
                _.assignIn(exTable.middleContent, body);
                let $body = $container.find("." + BODY_PRF + MIDDLE);
                $body.empty();
                render.process($body[0], exTable.middleContent, true);
            }
        }
        
        /**
         * Update detail.
         */
        function updateDetail($container: JQuery, header: any, body: any, keepStates: boolean) {
            let exTable: any = $container.data(NAMESPACE);
            if (header) {
                _.assignIn(exTable.detailHeader, header);
                let $header = $container.find("." + HEADER_PRF + DETAIL);
                let pu = $header.find("table").data(internal.POPUP);
                $header.empty();
                render.process($header[0], exTable.detailHeader, true);
                if (pu && pu.css("display") !== "none") pu.hide();
            }
            if (body) {
                _.assignIn(exTable.detailContent, body);
                let $body = $container.find("." + BODY_PRF + DETAIL);
                $body.empty();
                if (!keepStates) internal.clearStates($body[0]);
                render.process($body[0], exTable.detailContent, true);
            }
        }
        
        /**
         * Update vertSum.
         */
        function updateVertSum($container: JQuery, header: any, body: any) {
            let exTable: any = $container.data(NAMESPACE);
            if (header) {
                _.assignIn(exTable.verticalSumHeader, header);
                let $header = $container.find("." + HEADER_PRF + VERTICAL_SUM);
                let pu = $header.find("table").data(internal.POPUP);
                $header.empty();
                render.process($header[0], exTable.verticalSumHeader, true);
                if (pu && pu.css("display") !== "none") pu.hide();
            }
            if (body) {
                _.assignIn(exTable.verticalSumContent, body);
                let $body = $container.find("." + BODY_PRF + VERTICAL_SUM);
                $body.empty();
                render.process($body[0], exTable.verticalSumContent, true);
            }
        }
        
        /**
         * Update leftHorzSum.
         */
        function updateLeftHorzSum($container: JQuery, header: any, body: any) {
            let exTable: any = $container.data(NAMESPACE);
            if (header) {
                _.assignIn(exTable.leftHorzSumHeader, header);
                let $header = $container.find("." + HEADER_PRF + LEFT_HORZ_SUM);
                let pu = $header.find("table").data(internal.POPUP);
                $header.empty();
                render.process($header[0], exTable.leftHorzSumHeader, true);
                if (pu && pu.css("display") !== "none") pu.hide();
            }
            if (body) {
                _.assignIn(exTable.leftHorzSumContent, body);
                let $body = $container.find("." + BODY_PRF + LEFT_HORZ_SUM);
                $body.empty();
                render.process($body[0], exTable.leftHorzSumContent, true);
            }
        }
        
        /**
         * Update horzSum.
         */
        function updateHorzSum($container: JQuery, header: any, body: any) {
            let exTable: any = $container.data(NAMESPACE);
            if (header) {
                _.assignIn(exTable.horizontalSumHeader, header);
                let $header = $container.find("." + HEADER_PRF + HORIZONTAL_SUM);
                let pu = $header.find("table").data(internal.POPUP);
                $header.empty();
                render.process($header[0], exTable.horizontalSumHeader, true);
                if (pu && pu.css("display") !== "none") pu.hide();
            }
            if (body) {
                _.assignIn(exTable.horizontalSumContent, body);
                let $body = $container.find("." + BODY_PRF + HORIZONTAL_SUM);
                $body.empty();
                render.process($body[0], exTable.horizontalSumContent, true);
            }
        }
        
        /**
         * Set update mode.
         */
        function setUpdateMode($container: JQuery, mode: string, occupation?: any) {
            let exTable: any = $container.data(NAMESPACE);
            if (!mode) return exTable.updateMode;
            if (exTable.updateMode === mode) return;
            exTable.setUpdateMode(mode);
            if (occupation) {
                events.trigger($container[0], events.OCCUPY_UPDATE, occupation);
            }
            let $grid = $container.find("." + BODY_PRF + DETAIL);
            render.begin($grid[0], internal.getDataSource($grid[0]), exTable.detailContent);
            selection.tickRows($container.find("." + BODY_PRF + LEFTMOST)[0], true);
            if (mode === COPY_PASTE) {
                selection.checkUp($container[0]);
                copy.on($grid[0], mode);
                return;
            }
            selection.off($container[0]);
            copy.off($grid[0], mode);
        }
        
        /**
         * Set view mode.
         */
        function setViewMode($container: JQuery, mode: string, occupation?: any) {
            let exTable: any = $container.data(NAMESPACE);
            if (!mode) return exTable.viewMode;
            if (occupation) {
                events.trigger($container[0], events.OCCUPY_UPDATE, occupation);
            }
            
            if (exTable.viewMode === mode) return;
            let table = helper.getMainTable($container[0]);
            if (exTable.updateMode === EDIT) {
//                if (errors.occurred($container[0])) return;
                let editor = $container.data(update.EDITOR);
                if (editor) {
                    let $editor = editor.$editor;
                    let $input = $editor.querySelector("input");
                    let $editingCell = helper.closest($editor, "." + update.EDIT_CELL_CLS);
                    $editingCell.classList.remove(update.EDIT_CELL_CLS);
                    update.triggerStopEdit($container[0], $editingCell, editor.land, $input.value);
                    $container.data(update.EDITOR, null);
                }
                $.data(table, internal.EDIT_HISTORY, null);
            } else if (exTable.updateMode === COPY_PASTE) {
                $.data(table, internal.COPY_HISTORY, null);
            } else if (exTable.updateMode === STICK) {
                $.data(table, internal.STICK_HISTORY, null);
            }
            $.data(table, internal.DET, null);
            $container.data(errors.ERRORS, null);
            exTable.setViewMode(mode);
            let ds = helper.getOrigDS(table);
            render.begin(table, _.cloneDeep(ds), exTable.detailContent);
        }
        
        /**
         * Set mode.
         */
        function setMode($container: JQuery, viewMode: any, updateMode: any, occupation?: any) {
            let exTable: any = $container.data(NAMESPACE);
            if (occupation) {
                events.trigger($container[0], events.OCCUPY_UPDATE, occupation);
            }
            
            let table = helper.getMainTable($container[0]), 
                updateViewMode = false;
            let ds = helper.getOrigDS(table);
            if (viewMode && exTable.viewMode !== viewMode) {
                if (exTable.updateMode === EDIT) {
    //                if (errors.occurred($container[0])) return;
                    let editor = $container.data(update.EDITOR);
                    if (editor) {
                        let $editor = editor.$editor;
                        let $input = $editor.querySelector("input");
                        let $editingCell = helper.closest($editor, "." + update.EDIT_CELL_CLS);
                        $editingCell.classList.remove(update.EDIT_CELL_CLS);
                        update.triggerStopEdit($container[0], $editingCell, editor.land, $input.value);
                        $container.data(update.EDITOR, null);
                    }
                    $.data(table, internal.EDIT_HISTORY, null);
                } else if (exTable.updateMode === COPY_PASTE) {
                    $.data(table, internal.COPY_HISTORY, null);
                } else if (exTable.updateMode === STICK) {
                    $.data(table, internal.STICK_HISTORY, null);
                }
                $.data(table, internal.DET, null);
                $container.data(errors.ERRORS, null);
                exTable.setViewMode(viewMode);
                let $grid = $container.find("." + BODY_PRF + DETAIL);
                updateViewMode = true;
            }
            
            if (updateMode && exTable.updateMode !== updateMode) {
                exTable.setUpdateMode(updateMode);
                render.begin(table, ds, exTable.detailContent);
                selection.tickRows($container.find("." + BODY_PRF + LEFTMOST)[0], true);
                if (updateMode === COPY_PASTE) {
                    selection.checkUp($container[0]);
                    copy.on(table, updateMode);
                    return;
                }
                selection.off($container[0]);
                copy.off(table, updateMode);
            } else if (updateViewMode) {
                render.begin(table, ds, exTable.detailContent);
            }
        }
        
        /**
         * Set paste overwrite.
         */
        function setPasteOverWrite($container: JQuery, overwrite: boolean) {
            let exTable: any = $container.data(NAMESPACE);
            exTable.pasteOverWrite = overwrite;
        }
        
        /**
         * Set stick overwrite.
         */
        function setStickOverWrite($container: JQuery, overwrite: boolean) {
            let exTable: any = $container.data(NAMESPACE);
            exTable.stickOverWrite = overwrite;
        }
        
        /**
         * Set stick mode.
         */
        function setStickMode($container: JQuery, mode: any) {
            let $grid = $container.find("." + BODY_PRF + DETAIL);
            let sticker = $grid.data(internal.STICKER);
            if (!sticker) {
                sticker = new spread.Sticker();
                sticker.mode = mode;
                $grid.data(internal.STICKER, sticker);
            } else {
                sticker.mode = mode;
            }
        }
        
        /**
         * Set stick data.
         */
        function setStickData($container: JQuery, data: any) {
            let $grid = $container.find("." + BODY_PRF + DETAIL);
            let sticker = $grid.data(internal.STICKER);
            if (!sticker) {
                sticker = new spread.Sticker(data);
                $grid.data(internal.STICKER, sticker);
            } else {
                sticker.data = data;
            }
        }
        
        /**
         * Set stick validate.
         */
        function setStickValidate($container: JQuery, validate: any) {
            let $grid = $container.find("." + BODY_PRF + DETAIL);
            let sticker = $grid.data(internal.STICKER);
            if (!sticker) {
                sticker = new spread.Sticker();
                sticker.validate = validate;
                $grid.data(internal.STICKER, sticker);
            } else {
                sticker.validate = validate;
            }
        }
        
        /**
         * Undo stick.
         */
        function undoStick($container: JQuery) {
            let exTable = $container.data(NAMESPACE);
            if (!exTable || exTable.updateMode !== STICK) return;
            let $grid = $container.find("." + BODY_PRF + DETAIL);
            let histories = $grid.data(internal.STICK_HISTORY);
            if (!histories) return;
            let items = histories.pop();
            _.forEach(items, function(i: any) {
                update.gridCell($grid[0], i.rowIndex, i.columnKey, -1, i.value, true);
                internal.removeChange($grid[0], i);
            });
        }
        
        /**
         * Clear histories.
         */
        function clearHistories($container: JQuery, type: string) {
            let $grid = $container.find("." + BODY_PRF + DETAIL);
            let histType;
            switch (type) {
                case "edit":
                    histType = internal.EDIT_HISTORY;
                    break;
                case "copyPaste":
                    histType = internal.COPY_HISTORY;
                    break;
                case "stick":
                    histType = internal.STICK_HISTORY;
                    break;
            }
            $grid.data(histType, null);
        }
        
        /**
         * Lock cell.
         */
        function lockCell($container: JQuery, rowId: any, columnKey: any) {
            let $table = helper.getMainTable($container[0]);
            let ds = helper.getDataSource($table);
            let pk = helper.getPrimaryKey($table);
            let i = -1;
            _.forEach(ds, function(r, j) {
                if (r[pk] === rowId) {
                    i = j;
                    return false;
                }
            });
            if (i === -1) return;
            let locks = $.data($table, internal.DET);
            let found = -1;
            if (locks && locks[i] && locks[i].length > 0) { 
                _.forEach(locks[i], function(c, j) {
                    if (c === columnKey) {
                        found = j;
                        return false;
                    }
                });
            }
            
            if (found === -1) {
                let $cell = selection.cellAt($table, i, columnKey);
                if (!locks) {
                    locks = {};
                    locks[i] = [ columnKey ];
                    $.data($table, internal.DET, locks);
                } else if (locks && !locks[i]) {
                    locks[i] = [ columnKey ];
                } else locks[i].push(columnKey);
                helper.markCellWith(style.DET_CLS, $cell);
            }
        }
        
        /**
         * Unlock cell.
         */
        function unlockCell($container: JQuery, rowId: any, columnKey: any) {
            let $table = helper.getMainTable($container[0]);
            let ds = helper.getDataSource($table);
            let pk = helper.getPrimaryKey($table);
            let i = -1;
            _.forEach(ds, function(r, j) {
                if (r[pk] === rowId) {
                    i = j;
                    return false;
                }
            });
            if (i === -1) return;
            let locks = $.data($table, internal.DET);
            let found = -1;
            if (locks && locks[i] && locks[i].length > 0) {
                _.forEach(locks[i], function(c, j) {
                    if (c === columnKey) {
                        found = j;
                        return false;
                    }
                });
            }
            
            if (found > -1) {
                let $cell = selection.cellAt($table, i, columnKey);
                locks[i].splice(found, 1);
                if (locks[i].length === 0) delete locks[i];
                helper.stripCellWith(style.DET_CLS, $cell[0]);
            }
        }
        
        /**
         * Return popup value.
         */
        function returnPopupValue($container: JQuery, value: any) {
            if (!$container) return;
            let header = helper.getMainHeader($container[0]);
            if (!header) return;
            let headerTbl = header.querySelector("table");
            if (!headerTbl) return;
            let $pu = $.data(headerTbl, internal.POPUP);
            if (!$pu) return;
            events.trigger($pu[0], events.POPUP_INPUT_END, { value: value });
        }
        
        /**
         * Get data source.
         */
        function getDataSource($container: JQuery, name: any) {
            switch (name) {
                case "leftmost":
                    return helper.getPartialDataSource($container[0], LEFTMOST);
                case "middle": 
                    return helper.getPartialDataSource($container[0], MIDDLE);
                case "detail":
                    return helper.getPartialDataSource($container[0], DETAIL);
                case "verticalSummaries":
                    return helper.getPartialDataSource($container[0], VERTICAL_SUM);
                case "leftHorizontalSummaries":
                    return helper.getPartialDataSource($container[0], LEFT_HORZ_SUM);
                case "horizontalSummaries":
                    return helper.getPartialDataSource($container[0], HORIZONTAL_SUM);
            }
        }
        
        /**
         * Get cell by index.
         */
        function getCellByIndex($container: JQuery, rowIndex: any, columnKey: any) {
            let $tbl = helper.getMainTable($container[0]);
            if (!$tbl) return;
            return selection.cellAt($tbl, rowIndex, columnKey);
        }
        
        /**
         * Get cell by Id.
         */
        function getCellById($container: JQuery, rowId: any, columnKey: any) {
            let $tbl = helper.getMainTable($container[0]);
            if (!$tbl) return;
            return selection.cellOf($tbl, rowId, columnKey);
        }
        
        /**
         * Get updated cells.
         */
        function getUpdatedCells($container: JQuery) {
            let data = $container.data(NAMESPACE).modifications;
            if (!data) return [];
            return helper.valuesArray(data);
        }
        
        /**
         * Round retreat.
         */
        function roundRetreat($container: JQuery, value: any) {
            if (!value) return;
            events.trigger($container[0], events.ROUND_RETREAT, value);
        }
        
        /**
         * Set cell value.
         */
        function setCellValue($container: JQuery, name: any, rowId: any, columnKey: any, value: any) {
            switch (name) {
                case LEFT_TBL:
                    setValue($container, BODY_PRF + LEFTMOST, rowId, columnKey, value);
                    break;
                case HORZ_SUM:
                    setValue($container, BODY_PRF + HORIZONTAL_SUM, rowId, columnKey, value);
                    break;
                case VERT_SUM:
                    setValue($container, BODY_PRF + VERTICAL_SUM, rowId, columnKey, value);
                    break;
            }
        }
        
        /**
         * Set cell value by index.
         */
        function setCellValueByIndex($container: JQuery, name: any, rowIdx: any, columnKey: any, value: any) {
            switch(name) {
                case LEFT_TBL:
                    setValueByIndex($container, BODY_PRF + LEFTMOST, rowIdx, columnKey, value);
                    break;
                case HORZ_SUM:
                    setValueByIndex($container, BODY_PRF + HORIZONTAL_SUM, rowIdx, columnKey, value);
                    break;
                case VERT_SUM:
                    setValueByIndex($container, BODY_PRF + VERTICAL_SUM, rowIdx, columnKey, value);
                    break;
            }   
        }
        
        /**
         * Set value.
         */
        function setValue($container: JQuery, selector: any, rowId: any, columnKey: any, value: any) {
            let $grid = $container.find("." + selector);
            if ($grid.length === 0) return;
            let rowIdx = helper.getRowIndex($grid[0], rowId);
            let ds = helper.getDataSource($grid[0]);
            if (rowIdx === -1 || !ds || ds.length === 0) return;
            if (selector === BODY_PRF + LEFTMOST) {
                if (ds[rowIdx][columnKey] !== value) {
                    update.gridCell($grid[0], rowIdx, columnKey, -1, value);
                    update.pushEditHistory($grid[0], new selection.Cell(rowIdx, columnKey, value, -1));
                }
            } else {
                ds[rowIdx][columnKey] = value;
                refreshCell($grid, rowId, columnKey, value);
            }
        }
        
        /**
         * Set value by index.
         */
        function setValueByIndex($container: JQuery, selector: any, rowIdx: any, columnKey, value: any) {
            let $grid = $container.find("." + selector);
            if ($grid.length === 0) return;
            let ds = helper.getDataSource($grid[0]);
            if (!ds || ds.length === 0) return;
            if (selector === BODY_PRF + LEFTMOST) {
                if (ds[rowIdx][columnKey] !== value) {
                    update.gridCell($grid[0], rowIdx, columnKey, -1, value);
                    update.pushEditHistory($grid[0], new selection.Cell(rowIdx, columnKey, value, -1));
                }
            } else {
                ds[rowIdx][columnKey] = value;
                refreshCellByIndex($grid, rowIdx, columnKey, value);
            }
        }
        
        /**
         * Refresh cell.
         */
        function refreshCell($grid: JQuery, rowId: any, columnKey: any, value?: any) {
            let $c = selection.cellOf($grid[0], rowId, columnKey);
            if ($c === intan.NULL || !$c) return;
            if (util.isNullOrUndefined(value)) {
                let ds = helper.getClonedDs($grid[0]);
                if (!ds || ds.length === 0) return;
                let rIdx = helper.getRowIndex($grid[0], rowId);
                if (rIdx === -1) return;
                value = ds[rIdx][columnKey];
            }
            $c.textContent = value;
        }
        
        /**
         * Refresh cell by index.
         */
        function refreshCellByIndex($grid: JQuery, rowIdx: any, columnKey: any, value?: any) {
            let $c = selection.cellAt($grid[0], rowIdx, columnKey);
            if ($c === intan.NULL || !$c) return;
            if (util.isNullOrUndefined(value)) {
                let ds = helper.getClonedDs($grid);
                if (!ds || ds.length === 0) return;
                value = ds[rowIdx][columnKey];
            }
            $c.textContent = value;
        }
        
        /**
         * Set row Id.
         */
        function setRowId($container: JQuery, rowIndex: any, value: any) {
            $container.find("div[class*='" + BODY_PRF + "']").filter(function() {
                return !$(this).hasClass(BODY_PRF + HORIZONTAL_SUM) && !$(this).hasClass(BODY_PRF + LEFT_HORZ_SUM);
            }).each(function() {
                let key = helper.getPrimaryKey(this);
                let ds = internal.getDataSource(this);
                if (!ds || ds.length === 0 || !ds[rowIndex]) return;
                if (ds[rowIndex][key] !== value) {
                    update.gridCell(this, rowIndex, key, -1, value);
                    update.pushEditHistory(this, new selection.Cell(rowIndex, key, value, -1));
                }
            });
        }
        
        /**
         * Save scroll.
         */
        function saveScroll($container: JQuery) {
            let key = request.location.current.rawUrl + "/" + $container.attr("id") + "/scroll";
            let scroll: any = {};
            let $tbl = $container.find("." + BODY_PRF + DETAIL);
            scroll.v = $tbl.scrollTop();
            scroll.h = $tbl.scrollLeft();
            uk.localStorage.setItemAsJson(key, scroll);
        }
        
        /**
         * Scroll back.
         */
        function scrollBack($container: JQuery, where: number) {
            let key = request.location.current.rawUrl + "/" + $container.attr("id") + "/scroll";
            let item = uk.localStorage.getItem(key);
            if (!item.isPresent()) return; 
            let $tbl = $container.find("." + BODY_PRF + DETAIL);
            let scroll = JSON.parse(item.get());
            switch (where) {
                case 0:
                    $tbl.scrollLeft(scroll.h);
                    break;
                case 1: 
                    $tbl.scrollTop(scroll.v);
                    break;
                case 2:
                    $tbl.scrollLeft(scroll.h);
                    $tbl.scrollTop(scroll.v);
                    break;
            }
        }
    }
    
    module internal {
        export let X_OCCUPY: string = "ex-x-occupy";
        export let Y_OCCUPY: string = "ex-y-occupy";
        export let TANGI: string = "x-tangi";
        export let CANON: string = "x-canon";
        export let STICKER: string = "x-sticker";
        export let DET: string = "x-det";
        export let PAINTER: string = "painter";
        export let CELLS_STYLE: string = "body-cells-style";
        export let VIEW: string = "view";
        export let EX_PART: string = "expart";
        export let TIME_VALID_RANGE = "time-validate-range";
        export let SELECTED_CELLS: string = "selected-cells";
        export let LAST_SELECTED: string = "last-selected";
        export let SELECTED_ROWS: string = "selected-rows";
        export let COPY_HISTORY: string = "copy-history";
        export let EDIT_HISTORY: string = "edit-history";
        export let STICK_HISTORY: string = "stick-history";
        export let TOOLTIP: string = "tooltip";
        export let CONTEXT_MENU: string = "context-menu";
        export let POPUP: string = "popup";
        export let TEXT: string = "text";
        export let TIME: string = "time";
        export let DURATION: string = "duration";
        export let NUMBER: string = "number";
        export let DT_SEPARATOR: string = "/";
        export let COLUMN_IN: string = "column-in";
        
        /**
         * Get gem.
         */
        export function getGem($grid: HTMLElement) {
            return $.data($grid, internal.TANGI)|| $.data($grid, internal.CANON);
        }
        
        /**
         * Get data source.
         */
        export function getDataSource($grid: HTMLElement) {
            let gen = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
            if (!gen) return;
            return gen.dataSource;
        }
        
        /**
         * Remove change.
         */
        export function removeChange($grid: HTMLElement, cell: any) {
            let origDs = helper.getOrigDS($grid);
            let exTable = helper.getExTableFromGrid($grid);
            if (!origDs || !exTable) return;
            let oVal = origDs[cell.rowIndex][cell.columnKey];
            let cells = exTable.modifications[cell.rowIndex];
            if (!cells) return;
            let index = -1;
            _.forEach(cells, function(c: any, i: number) {
                if (helper.areSameCells(cell, c) && cell.value === oVal) {
                    index = i;
                    return false;
                }
            });
            exTable.modifications[cell.rowIndex].splice(index, 1);
        }
        
        /**
         * Clear states.
         */
        export function clearStates($grid: HTMLElement) {
            $.data($grid, SELECTED_CELLS, null);
            $.data($grid, LAST_SELECTED, null);
            $.data($grid, COPY_HISTORY, null);
            $.data($grid, EDIT_HISTORY, null);
            $.data($grid, STICK_HISTORY, null);
            let exTable = helper.getExTableFromGrid($grid);
            if (!exTable) return;
            exTable.modifications = {};
        }
    }
    
    module selector {
        
        export function find(p: HTMLElement, sel: any) {
            return new Manipulator().addNodes(p.querySelectorAll(sel));
        }
        
        export function create(str: any) {
            return new Manipulator().addElement(document.createElement(str));
        }
        
        export function is(el, sel) {
            let matches = el.matches || el.matchesSelector || el.msMatchesSelector || el.mozMatchesSelector || el.webkitMatchesSelector || el.oMatchesSelector;
            if (matches) return matches.call(el, sel);
            return $(el).is(sel);
        }
        
        export function index(el) {
            return Array.prototype.slice.call(el.parentNode.children).indexOf(el);
        }
        
        export function queryAll(el, sel): Array<HTMLElement> {
            return Array.prototype.slice.call(el.querySelectorAll(sel));
        }
        
        export function offset(el) {
            let rect = el.getBoundingClientRect();
            return {
              top: rect.top + document.body.scrollTop,
              left: rect.left + document.body.scrollLeft
            }
        }
        
        export function classSiblings(node: any, clazz: any) {
            let parent = node.parentElement;
            if (!parent) return;
            let children = parent.children;
            let results = [];
            for (let i = 0; i < children.length; i++) {
                if (children[i] === node) continue;
                let classList = children[i].classList;
                for (let j = 0; j < classList.length; j++) {
                    if (classList.item(j) === clazz) {
                        results.push(children[i]);
                        break;
                    }
                }
            }
            return results;
        }
        
        export function siblingsLt(el, index) {
            let parent = el.parentNode;
            if (!parent) return;
            let children = parent.children;
            let results = [];
            for (let i = 0; i < children.length; i++) {
                if (i < index) {
                    if (children[i] !== el) results.push(children[i]);
                } else return results;
            }
        }
        
        export class Manipulator {
            elements: Array<HTMLElement>;
                
            addNodes(nodes: NodeList) {
                if (!nodes || nodes.length === 0) return;
                this.elements = Array.prototype.slice.call(self.elements);
                return this;
            }
            
            addElements(elements: Array<HTMLElement>) {
                this.elements = elements;
                return this;
            }
            
            addElement(element: HTMLElement) {
                if (!this.elements) this.elements = [];
                this.elements.push(element);
                return this;
            }
            
            html(str) {
                this.elements.forEach(function(e) {
                    e.innerHTML = str;
                });
                return this;
            }
            
            width(w) {
                let self = this;
                this.elements.forEach(function(e) {
                    e.style.width = parseInt(w) + "px";
                });
                return this;
            }
            
            height(h) {
                let self = this;
                this.elements.forEach(function(e) {
                    e.style.height = parseInt(h) + "px";
                });
                return this;
            }
            
            data(name: any, value: any) {
                this.elements.forEach(function(e) {
                    $.data(e, name, value);
                });
                return this;
            }
            
            addClass(clazz: any) {
                this.elements.forEach(function(e) {
                    e.classList.add(clazz);
                });
                return this;
            }
            
            css(style: any) {
                this.elements.forEach(function(e) {
                    Object.keys(style).forEach(function(k) {;
                        if (k === "maxWidth") {
                            e.style.setProperty("max-width", style[k]);
                            return;
                        }
                        e.style.setProperty(k, style[k]);
                    });
                });
                return this;
            }
            
            getSingle() {
                return this.elements[0];
            }
            
            get() {
                return this.elements;
            }
        }
    }
    
    module helper {
        
        /**
         * Is IE.
         */
        export function isIE() {
            return window.navigator.userAgent.indexOf("MSIE") > -1 || window.navigator.userAgent.match(/trident/i);
        }
        
        /**
         * Is Chrome.
         */
        export function isChrome() {
            return window.chrome;
        }
        
        /**
         * Is Edge.
         */
        export function isEdge() {
            return window.navigator.userAgent.indexOf("Edge") > -1;
        }
        
        /**
         * Get scroll width.
         */
        export function getScrollWidth() {
            if (_scrollWidth) return _scrollWidth;
            let $outer = document.body.appendChild(selector.create("div").css({ visibility: 'hidden', width: "100px", overflow: 'scroll' }).getSingle());
            let $inner = selector.create("div").css({ width: '100%' }).getSingle();
            $outer.appendChild($inner);
            let widthWithScroll = $inner.offsetWidth;
            $outer.parentNode.removeChild($outer);
            _scrollWidth = 100 - widthWithScroll;
            return _scrollWidth;
        }
        
        /**
         * Get table.
         */
        export function getTable($exTable: HTMLElement, name: string) {
            return $exTable.querySelector("." + name);
        }
        
        /**
         * Get main header.
         */
        export function getMainHeader($exTable: HTMLElement) {
            return $exTable.querySelector("." + HEADER_PRF + DETAIL);
        }
        
        /**
         * Get main table.
         */
        export function getMainTable($exTable: HTMLElement) {
            return $exTable.querySelector("." + BODY_PRF + DETAIL);
        }
        
        /**
         * Get leftmost table.
         */
        export function getLeftmostTable($exTable: HTMLElement) {
            return $exTable.querySelector("." + BODY_PRF + LEFTMOST);
        }
        
        /**
         * Get exTable from grid.
         */
        export function getExTableFromGrid($grid: HTMLElement) {
            return $.data(helper.closest($grid, "." + NAMESPACE), NAMESPACE);
        }
        
        /**
         * Get visible columns.
         */
        export function getVisibleColumnsOn($grid: HTMLElement) {
            return ($.data($grid, internal.TANGI) || $.data($grid, internal.CANON)).painter.visibleColumns;
        }
        
        /**
         * Get visible columns.
         */
        export function getVisibleColumns(options: any) {
            let visibleColumns = [];
            filterColumns(options.columns, visibleColumns, []);
            return visibleColumns;
        }
        
        /**
         * Get origDS.
         */
        export function getOrigDS($grid: HTMLElement) {
            return ($.data($grid, internal.TANGI) || $.data($grid, internal.CANON))._origDs;
        }
        
        /**
         * Get data source.
         */
        export function getDataSource($grid: HTMLElement) {
            return ($.data($grid, internal.TANGI) || $.data($grid, internal.CANON)).dataSource;
        }
        
        /**
         * Get clonedDs.
         */
        export function getClonedDs($grid: HTMLElement) {
            return _.cloneDeep(getDataSource($grid));
        }
        
        /**
         * Get primary key.
         */
        export function getPrimaryKey($grid: HTMLElement) {
            return ($.data($grid, internal.TANGI) || $.data($grid, internal.CANON)).primaryKey;
        }
        
        /**
         * Classify columns.
         */
        export function classifyColumns(options: any) {
            let visibleColumns = [];
            let hiddenColumns = [];
            filterColumns(options.columns, visibleColumns, hiddenColumns);
            return {
                        visibleColumns: visibleColumns,
                        hiddenColumns: hiddenColumns
                   };
        }
        
        /**
         * Filter columns.
         */
        function filterColumns(columns: Array<any>, visibleColumns: Array<any>, hiddenColumns: Array<any>) {
            _.forEach(columns, function(col: any) {
                if (!util.isNullOrUndefined(col.visible) && col.visible === false) {
                    hiddenColumns.push(col);
                    return;
                }
                if (!util.isNullOrUndefined(col.group) && col.group.length > 0) { 
                    filterColumns(col.group, visibleColumns, hiddenColumns);
                } else {
                    visibleColumns.push(col);
                }
            });
        }
        
        /**
         * Get columns map.
         */
        export function getColumnsMap(columns: any) {
            return _.groupBy(columns, "key");
        }
        
        /**
         * Get columns map from struct.
         */
        export function columnsMapFromStruct(levelStruct: any) {
            let map = {};
            _.forEach(Object.keys(levelStruct), function(nth: any) {
                _.forEach(levelStruct[nth], function(col: any) {
                    if (!util.isNullOrUndefined(col.key)) {
                        map[col.key] = col;
                    }
                });
            });
            return map; 
        }
        
        /**
         * Get partial data source.
         */
        export function getPartialDataSource($table: HTMLElement, name: any) {
            return {
                header: getClonedDs($table.querySelector("." + HEADER_PRF + name)),
                body: getClonedDs($table.querySelector("." + BODY_PRF + name))
            };
        }
        
        /**
         * Make connector.
         */
        export function makeConnector() {
            Connector[HEADER_PRF + LEFTMOST] = BODY_PRF + LEFTMOST;
            Connector[HEADER_PRF + MIDDLE] = BODY_PRF + MIDDLE;
            Connector[HEADER_PRF + DETAIL] = BODY_PRF + DETAIL;
            Connector[HEADER_PRF + VERTICAL_SUM] = BODY_PRF + VERTICAL_SUM;
            Connector[HEADER_PRF + HORIZONTAL_SUM] = BODY_PRF + HORIZONTAL_SUM;
        }
        
        /**
         * Get class of header.
         */
        export function getClassOfHeader($part: HTMLElement) {
            return $.data($part, internal.EX_PART);
        }
        
        /**
         * Is paste key.
         */
        export function isPasteKey(evt: any) {
            return evt.keyCode === 86;
        }
        
        /**
         * Is copy key.
         */
        export function isCopyKey(evt: any) {
            return evt.keyCode === 67;
        }
        
        /**
         * Is cut key.
         */
        export function isCutKey(evt: any) {
            return evt.keyCode === 88;
        }
        
        /**
         * Is undo key.
         */
        export function isUndoKey(evt: any) {
            return evt.keyCode === 90;
        }
        
        /**
         * Get cell coord.
         */
        export function getCellCoord($cell: HTMLElement) {
            if (!$cell) return;
            let $td = $cell;
            if (selector.is($cell, "div")) {
                $td = closest($cell, "td");
            }
            let view = $.data($td, internal.VIEW);
            if (!view) return;
            let coord = view.split("-");
            if (util.isNullOrUndefined(coord[0]) || util.isNullOrUndefined(coord[1])) return;
            return {
                rowIdx: parseFloat(coord[0]),
                columnKey: coord[1]
            };
        }
        
        /**
         * Get display column index.
         */
        export function getDisplayColumnIndex($grid: HTMLElement, key: any) {
            let generator = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
            let visibleColumns = generator.painter.visibleColumns;
            let index;
            _.forEach(visibleColumns, function(c: any, i: number) {
                if (c.key === key) {
                    index = i;
                    return false;
                }
            });
            return index;
        }
        
        /**
         * Get row index.
         */
        export function getRowIndex($grid: HTMLElement, rowId: any) {
            let gen = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
            if (!gen) return;
            let start = gen.startIndex || 0;
            let end = gen.endIndex || gen.dataSource.length - 1;
            for (let i = start; i <= end; i++) {
                if (gen.dataSource[i][gen.primaryKey] === rowId) {
                    return i;
                }
            }
            return -1;
        }
        
        /**
         * Grid visible columns.
         */
        export function gridVisibleColumns($grid: HTMLElement) {
            let gen = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
            if (!gen) return;
            return gen.painter.visibleColumns;
        }
        
        /**
         * Grid columns map.
         */
        export function gridColumnsMap($grid: HTMLElement) {
            let gen = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
            if (!gen) return;
            return gen.painter.columnsMap;
        }
        
        /**
         * Mark cell.
         */
        export function markCellWith(clazz: any, $cell: HTMLElement, nth?: any, value?: any) {
            let $childCells = $cell.querySelectorAll("." + render.CHILD_CELL_CLS);
            if (selector.is($cell, "td") && $childCells.length > 0) {
                if (!util.isNullOrUndefined(nth) && nth !== -1) {
                    $childCells[nth].classList.add(clazz);
                    if (clazz === errors.ERROR_CLS) $childCells[nth].textContent = value;
                } else {
                    helper.addClass($childCells, clazz);
                }
                return;
            }
            $cell.classList.add(clazz);
            if (clazz === errors.ERROR_CLS) $cell.textContent = value;
        }
        
        /**
         * Strip cell.
         */
        export function stripCellWith(clazz: any, $cell: HTMLElement, nth?: any) {
            let $childCells = $cell.querySelectorAll("." + render.CHILD_CELL_CLS);
            if (selector.is($cell, "td") && $childCells.length > 0) {
                if (!util.isNullOrUndefined(nth) && nth !== -1) {
                    $childCells[nth].classList.remove(clazz);
                } else helper.removeClass($childCells, clazz);
                return;
            }
            $cell.classList.remove(clazz);
        }
        
        /**
         * Mark cells.
         */
        export function markCellsWith(clazz: any, $cells: Array<HTMLElement>) {
            $cells.forEach(function(e) {
                markCellWith(clazz, e);
            });
        }
        
        /**
         * Strip cells.
         */
        export function stripCellsWith(clazz: any, $cells: Array<HTMLElement>) {
            $cells.forEach(function(e) {
                stripCellWith(clazz, e);
            });
        }
        
        /**
         * Is detable.
         */
        export function isDetable($cell: HTMLElement) {
            let children = $cell.querySelectorAll("." + render.CHILD_CELL_CLS);
            return !(selector.is($cell, "." + style.HIDDEN_CLS) || selector.is($cell, "." + style.SEAL_CLS)
                    || (children.length > 0 
                        && (selector.is(children[0], "." + style.HIDDEN_CLS) 
                            || selector.is(children[0], "." + style.SEAL_CLS))));
        }
        
        /**
         * Column index.
         */
        export function indexOf(columnKey: any, visibleColumns: any) {
            let index = -1;
            _.forEach(visibleColumns, function(column: any, i: number) {
                if (column.key === columnKey) {
                    index = i;
                    return false;
                }
            });
            return index;
        }
        
        /**
         * Next column key.
         */
        export function nextKeyOf(columnIndex: number, visibleColumns: any) {
            if (columnIndex >= visibleColumns.length - 1) return;
            return visibleColumns[columnIndex + 1].key;
        }
        
        /**
         * Next cell.
         */
        export function nextCellOf($grid: HTMLElement, cell: any) {
            let key, rowIndex, innerIdx;
            let gen = $.data($grid, internal.TANGI) || $.data($grid, internal.CANON);
            if (!gen) return;
            let visibleColumns = gen.painter.visibleColumns;
            key = nextKeyOf(indexOf(cell.columnKey, visibleColumns), visibleColumns);
            if (key) {
                return new selection.Cell(cell.rowIndex, key, undefined, cell.innerIdx); 
            }
            key = visibleColumns[0].key;
            if (cell.rowIndex >= gen.dataSource.length - 1) {
                if (cell.innerIdx === -1) {
                    rowIndex = 0;
                    innerIdx = -1;
                } else if (cell.innerIdx === 0) {
                    rowIndex = Number(cell.rowIndex);
                    innerIdx = 1;
                } else if (cell.innerIdx === 1) {
                    rowIndex = 0;
                    innerIdx = 0;
                }
            } else {
                if (cell.innerIdx === -1) {
                    rowIndex = Number(cell.rowIndex) + 1;
                    innerIdx = -1;
                } else if (cell.innerIdx === 0) {
                    rowIndex = Number(cell.rowIndex);
                    innerIdx = 1;
                } else if (cell.innerIdx === 1) {
                    rowIndex = Number(cell.rowIndex) + 1;
                    innerIdx = 0;
                }
            }
            return new selection.Cell(rowIndex, key, undefined, innerIdx);
        }
        
        /**
         * Call.
         */
        export function call(fn: any, ...args: any[]) {
            return function() {
                return fn.apply(null, args);
            };
        }
        
        /**
         * Check br.
         */
        export function containsBr(text: string) {
            return text && text.indexOf("<br/>") > -1;
        }
        
        /**
         * Compare cells.
         */
        export function areSameCells(one: any, other: any) {
            if (parseInt(one.rowIndex) !== parseInt(other.rowIndex)
                || one.columnKey !== other.columnKey
                || one.innerIdx !== other.innerIdx) return false;
            return true;
        }
        
        /**
         * Is det cell.
         */
        export function isDetCell($grid: HTMLElement, rowIdx: any, key: any) {
            let $cell = selection.cellAt($grid, rowIdx, key);
            let $childCells = $cell.querySelectorAll("." + render.CHILD_CELL_CLS);
            return ($childCells.length === 0 && selector.is($cell, "." + style.DET_CLS))
                    || ($childCells.length > 0 && selector.is($childCells[0], "." + style.DET_CLS));
        }
        
        /**
         * Is xcell.
         */
        export function isXCell($grid: HTMLElement, rowId: any, key: any, ...clazz: any[]) {
            let cellsStyle = $.data($grid, internal.CELLS_STYLE);
            if (!cellsStyle) return;
            let result = _.find(cellsStyle, function(deco) {
                return deco.columnKey === key && deco.rowId === rowId && clazz.some(c => deco.clazz === c); 
            });
            return result !== undefined;
        }
        
        /**
         * Is xcell shown.
         */
        export function isXCellShown($grid: HTMLElement, rowIdx: any, key: any, ...clazz: any[]) {
            let $cell = selection.cellAt($grid, rowIdx, key);
            let $childCells = $cell.querySelectorAll("." + render.CHILD_CELL_CLS);
            let returnVal = false;
            _.forEach(clazz, function(c) {
                if (($childCells.length === 0 && selector.is($cell, "." + c))
                        || ($childCells.length > 0 && selector.is($childCells[0], "." + c))) {
                    returnVal = true;
                    return false;
                }
            });
            return returnVal;
        }
        
        /**
         * Is empty.
         */
        export function isEmpty(obj: any) {
            if (obj && obj.constructor === Array) {
                let empty = true;
                _.forEach(obj, function(o) {
                    if (!util.isNullOrUndefined(o)) {
                        empty = false;
                        return false;
                    }
                });
                return empty;
            }
            
            if (!obj) return true;
            return false;
        }
        
        /**
         * Values array.
         */
        export function valuesArray(obj: any) {
            let values = [];
            _.forEach(Object.keys(obj), function(k, i) {
                values = _.concat(values, obj[k]);
            });
            return values;
        }
        
        /**
         * String value.
         */
        export function stringValue(val: any) {
            return _.isObject(val) ? JSON.stringify(val) : val;
        }
        
        /**
         * Get cell data.
         */
        export function getCellData(data: any) {
            try {
                return JSON.parse(data);
            } catch (e) {
                return data;
            }
        }
        
        /**
         * View data.
         */
        export function viewData(view: any, viewMode: any, obj: any) {
            if (!view || !viewMode) return;
            let result = [];
            _.forEach(view(viewMode), function(f) {
                if (!f) return;
                result.push(obj[f]);
            });
            return result.length === 1 ? result[0] : result;
        }
        
        /**
         * Is equal.
         */
        export function isEqual(one: any, two: any, fields?: Array<any>) {
            if (_.isObject(one) && _.isObject(two)) {
                return (fields && fields.length > 0) 
                        ? _.isEqual(_.omitBy(one, (d, p) => fields.every(f => f !== p)),
                                    _.omitBy(two, (d, p) => fields.every(f => f !== p)))
                        : _.isEqual(_.omit(one, _.isFunction), _.omit(two, _.isFunction));
            }
            return _.isEqual(one, two);
        }
        
        /**
         * Block.
         */
        export function block(exTable: HTMLElement) {
            let $exTable = $(exTable);
            (<any> $exTable).block({
                message: null,
                fadeIn: 200,
                css: {
                    width: $exTable.width(),
                    height: $exTable.height()
                }
            });
        }
        
        /**
         * Unblock.
         */
        export function unblock(exTable: HTMLElement) {
            (<any> $(exTable)).unblock();
        }
        
        /**
         * Highlight column.
         */
        export function highlightColumn($container: HTMLElement, columnIndex: any) {
            let grid = $container.querySelector("." + BODY_PRF + DETAIL);
            let header = $container.querySelector("." + HEADER_PRF + DETAIL);
            _.forEach(grid.getElementsByTagName("tr"), function(t) {
                let tds = t.getElementsByTagName("td");
                if (!tds || tds.length === 0) return;
                helper.addClass1n(tds[columnIndex], render.HIGHLIGHT_CLS);
            });
            _.forEach(header.getElementsByTagName("tr"), function(t) {
                let tds = t.getElementsByTagName("td");
                if (!tds || tds.length === 0) return;
                helper.addClass1n(tds[columnIndex], render.HIGHLIGHT_CLS);
            });
        }
        
        /**
         * Unhighlight column.
         */
        export function unHighlightColumn($container: HTMLElement, columnIndex: any) {
            let grid = $container.querySelector("." + BODY_PRF + DETAIL);
            let header = $container.querySelector("." + HEADER_PRF + DETAIL);
            unHighlightGrid(grid, columnIndex);
            unHighlightGrid(header, columnIndex);
        }
        
        /**
         * Unhighlight grid.
         */
        export function unHighlightGrid(grid: Element, columnIndex: any) {
            if (!grid) return;
            _.forEach(grid.getElementsByTagName("tr"), function(t) {
                let tds = t.getElementsByTagName("td");
                if (!tds || tds.length === 0) return;
                helper.removeClass1n(tds[columnIndex], render.HIGHLIGHT_CLS);
            });
        }
        
        /**
         * First sibling.
         */
        export function firstSibling(node: any, clazz: any) {
            let parent = node.parentElement;
            if (!parent) return;
            let children = parent.children;
            for (let i = 0; i < children.length; i++) {
                if (node !== children[i] && children[i].classList.contains(clazz)) {
                    return children[i];
                }
            }
        }
        
        /**
         * Class siblings.
         */
        export function classSiblings(node: any, partialClass: any) {
            let parent = node.parentElement;
            if (!parent) return;
            let children = parent.children;
            let results = [];
            for (let i = 0; i < children.length; i++) {
                if (children[i] === node) continue;
                let classList = children[i].classList;
                for (let j = 0; j < classList.length; j++) {
                    if (classList.item(j).indexOf(partialClass) >= 0) {
                        results.push(children[i]);
                    }
                }
            }
            return results;
        }
        
        /**
         * Consume siblings.
         */
        export function consumeSiblings(node: any, op: any) {
            let parent = node.parentElement;
            if (!parent) return;
            let children = parent.children;
            for (let i = 0; i < children.length; i++) {
                if (node !== children[i]) {
                    op(children[i]);
                }
            }
        }
        
        /**
         * Closest.
         */
        export function closest(el, selector) {
            let matches;
            ['matches', 'webkitMatchesSelector', 'mozMatchesSelector', 'msMatchesSelector', 'oMatchesSelector'].some(function(fn) {
                if (typeof document.body[fn] === 'function') {
                    matches = fn;
                    return true;
                }
                return false;
            });
        
            let parent;
            while (el) {
                parent = el.parentElement;
                if (parent && parent[matches](selector)) {
                    return parent;
                }
                el = parent;
            }
        }
        
        /**
         * Add class.
         */
        export function addClass1n(node, clazz) {
            if (node && node.constructor !== HTMLCollection) {
                let children = node.querySelectorAll("." + render.CHILD_CELL_CLS); 
                if (children.length > 0) addClass(children, clazz);
                else addClass(node, clazz);
                return;
            }
            for (let i = 0; i < node.length; i++) {
                let children = node[i].querySelectorAll("." + render.CHILD_CELL_CLS);
                if (children.length > 0) addClass(children, clazz);
                else addClass(node[i], clazz);
            }
        }
        
        /**
         * Remove class.
         */
        export function removeClass1n(node, clazz) {
            if (node && node.constructor !== HTMLCollection) {
                let children = node.querySelectorAll("." + render.CHILD_CELL_CLS);
                if (children.length > 0) removeClass(children, clazz);
                else removeClass(node, clazz);
                return;
            }
            for (let i = 0; i < node.length; i++) {
                let children = node[i].querySelectorAll("." + render.CHILD_CELL_CLS);
                if (children.length > 0) removeClass(children, clazz);
                else removeClass(node[i], clazz);
            }
        }
        
        /**
         * Add class.
         */
        export function addClass(node, clazz) {
            if (node && node.constructor !== HTMLCollection && node.constructor !== NodeList) {
                node.classList.add(clazz);
                return;
            }
            for (let i = 0; i < node.length; i++) {
                if (!node[i].classList.contains(clazz)) {
                    node[i].classList.add(clazz);
                }
            }
        }
        
        /**
         * Remove class.
         */
        export function removeClass(node, clazz) {
            if (node && node.constructor !== HTMLCollection && node.constructor !== NodeList) {
                node.classList.remove(clazz);
                return;
            }
            for (let i = 0; i < node.length; i++) {
                if (node[i].classList.contains(clazz)) {
                    node[i].classList.remove(clazz);
                }
            }
        }
        
        /**
         * Has class.
         */
        export function hasClass(node, clazz) {
            return node.classList.contains(clazz);
        }
        
        /**
         * Index.
         */
        export function indexInParent(node) {
            let parent = node.parentElement;
            if (!parent) return;
            let children = parent.children;
            let index = 0;
            for (let i = 0; i < children.length; i++) {
                 if (children[i] === node) return index;
                 if (children[i].nodeType === 1) index++;
            }
            return -1;
        }
        
        /**
         * Add class.
         */
        export function addClassList(cell: any, clazz: string) {
            if (!clazz) return;
            clazz.split(" ").forEach(function(c, i) {
                cell.classList.add(c);
            });
        }
    }
    
    module widget {
        export let MENU = "menu";
        export let POPUP = "popup";
        export let MENU_CLS = "x-context-menu";
        export let POPUP_CLS = "x-popup-panel";
        export let PARTITION_CLS = "partition";
        export let MENU_ITEM_CLS = "menu-item";
        export let MENU_ICON_CLS = "menu-icon";
        export let DISABLED_CLS = "disabled"; 
        
        /**
         * Widget.
         */
        abstract class XWidget {
            $table: HTMLElement;
            $selector: HTMLElement;
            constructor($selector: HTMLElement) {
                this.$selector = $selector;
            }
            
            /**
             * Initialize. 
             */
            abstract initialize();
            
            /**
             * Get table.
             */
            getTable() {
                this.$table = helper.closest(this.$selector, "table");
            }
        }
        
        /**
         * Tooltip.
         */
        export class Tooltip extends XWidget {
            options: any;
            defaultOpts: any;
            constructor($selector: HTMLElement, options: any) {
                super($selector);
                this.options = options;
                this.defaultOpts = { 
                    showRight: true,
                    width: "100px"
                };
                this.initialize();
            }
            
            /**
             * Initialize.
             */
            initialize() {
                let self = this;
                $.extend(true, self.options, self.defaultOpts);
                self.$selector.addXEventListener(events.MOUSE_OVER, function(evt) {
                    self.getTable();
                    if (self.$table.length === 0) return;
                    // Tooltip is jq
                    let $t2 = $.data(self.$table, internal.TOOLTIP);
                    if (!$t2) {
                        $t2 = $("<div/>").addClass(cssClass(self.options));
                        $t2.appendTo("body");
                        $.data(self.$table, internal.TOOLTIP, $t2);
                    }
                    $t2.empty().append(self.options.sources).css({ visibility: "visible" })
                        .position({ my: "left top", at: "left+" + self.$selector.offsetWidth + " top+5", of: self.$selector });
                });
                self.$selector.addXEventListener(events.MOUSE_OUT, function(evt) {
                    self.getTable();
                    if (!self.$table) return;
                    let $t2 = $.data(self.$table, internal.TOOLTIP);
                    if (!$t2 || $t2.css("display") === "none") return;
                    $t2.css({ visibility: "hidden" });
                });
            }
        }
        
        /**
         * Popup.
         */
        abstract class Popup extends XWidget {
            position: any;
            constructor($selector: HTMLElement) {
                super($selector);
                this.initialize();
            }
            
            /**
             * Initialize.
             */
            initialize() {
                let self = this;
                self.$selector.addXEventListener(events.MOUSE_DOWN, function(evt: any) {
                    self.getTable();
                    if (evt.ctrlKey && $.data(helper.closest(self.$table, "." + NAMESPACE), NAMESPACE).determination) return;
                    self.click(evt);
                });
            }
            
            /**
             * Click.
             */
            abstract click(evt: any);
        } 
        
        /**
         * Context menu.
         */
        export class ContextMenu extends Popup {
            items: Array<MenuItem>;
            constructor($selector: HTMLElement, items: Array<MenuItem>) {
                super($selector);
                this.items = items;         
            }
            
            /**
             * Click.
             */
            click(evt: any) {
                let self = this;
                let $menu = $.data(self.$table, internal.CONTEXT_MENU);
                if (!$menu) {
                    $menu = $("<ul/>").addClass(MENU_CLS).appendTo("body").hide();
                    _.forEach(self.items, function(item: MenuItem) {
                        self.createItem($menu, item);
                    });
                    $.data(self.$table, internal.CONTEXT_MENU, $menu);
                }
                if ($menu.css("display") === "none") {
                    $menu.show().css({ top: evt.pageY, left: evt.pageX });
                } else {
                    $menu.hide();
                }
                let $pu = $.data(self.$table, internal.POPUP);
                if ($pu && $pu.css("display") !== "none") {
                    $pu.hide();
                }
                update.outsideClick(helper.closest(self.$table, "." + NAMESPACE), self.$selector);
                evt.stopPropagation();
                hideIfOutside($menu); 
            }
            
            /**
             * Create item.
             */
            createItem($menu: JQuery, item: any) {
                if (item.id === PARTITION_CLS) {
                    $("<li/>").addClass(MENU_ITEM_CLS + " " + PARTITION_CLS).appendTo($menu);
                    return;
                }
                let $li = $("<li/>").addClass(MENU_ITEM_CLS).text(item.text)
                    .on(events.CLICK_EVT, function(evt: any) {
                        if (item.disabled) return;
                        item.selectHandler(item.id);
                        $menu.hide();
                }).appendTo($menu);
                if (item.disabled) {
                    $li.addClass(DISABLED_CLS);
                }
                if (item.icon) {
                    $li.append($("<span/>").addClass(MENU_ICON_CLS + " " + item.icon));
                }
            }
        }
        
        /**
         * Menu item.
         */
        export class MenuItem {
            id: string;
            text: string;
            selectHandler: (id: any) => void;
            disabled: boolean;
            icon: any;
            constructor(text: string, selectHandler: (ui: any) => void, disabled: boolean, icon: any) {
                this.text = text;
                this.selectHandler = selectHandler ? selectHandler : $.noop();
                this.disabled = disabled;
                this.icon = icon;
            }
        }
        
        /**
         * Popup panel.
         */
        export class PopupPanel extends Popup {
            $panel: JQuery;
            provider: any;
            constructor($selector: HTMLElement, provider: any, position?: any) {
                super($selector);
                this.provider = provider;
                this.position = position;
            }
            
            /**
             * Click.
             */
            click(evt: any) {
                let self = this;
                let $pu = $.data(self.$table, internal.POPUP);
                let $cell = evt.target;
                if (!selector.is(evt.target, "td")) {
                    $cell = helper.closest(evt.target, "td");
                }
                let coord = helper.getCellCoord($cell);
                self.$panel = self.provider(coord.columnKey);
                if (!$pu) {
                    $pu = self.$panel.addClass(POPUP_CLS).hide();
                    $.data(self.$table, internal.POPUP, $pu);
                }
                if ($pu.css("display") === "none") {
                    let pos = eventPageOffset(evt, false);
                    $pu.show().css(self.getPosition($pu, pos, self.position || "top left"));
                    events.trigger(self.$table, events.POPUP_SHOWN, $(evt.target));
                    self.addListener($pu, $(evt.target));
                } else {
                    $pu.hide();
                }
                let $menu = $.data(self.$table, internal.CONTEXT_MENU);
                if ($menu && $menu.css("display") !== "none") {
                    $menu.hide();
                }
                evt.stopPropagation();
                hideIfOutside($pu);
            }
            
            /**
             * Add listener.
             */
            addListener($pu: JQuery, $t: JQuery) {
                let self = this;
                $pu.off(events.POPUP_INPUT_END);
                $pu.on(events.POPUP_INPUT_END, function(evt: any) {
                    let ui = evt.detail;
                    let $header = helper.closest(self.$selector, "table").parentElement;
                    if ($header.classList.contains(HEADER)) {
                        let ds = helper.getDataSource($header);
                        if (!ds || ds.length === 0) return;
                        let coord = helper.getCellCoord($t[0]);
                        ds[coord.rowIdx][coord.columnKey] = ui.value;
                        $t.text(ui.value);
                        $pu.hide();
                    }
                });
            }
            
            /**
             * Get position.
             */
            getPosition($pu: JQuery, pos: any, my: any) {
                if (my === "top left") {
                    return { top: pos.pageY - $pu.outerHeight() - 49, left: pos.pageX - $pu.outerWidth() };
                } else if (my === "bottom left") {
                    return { top: pos.pageY - 49, left: pos.pageX - $pu.outerWidth() };
                } else if (my === "top right") {
                    return { top: pos.pageY - $pu.outerHeight() - 49, left: pos.pageX };
                } else if (my === "bottom right") {
                    return { top: pos.pageY - 49, left: pos.pageX };
                }
            }
        }
        
        export function bind(row: HTMLElement, rowIdx: any, headerPopupFt: any) {
            let wType;
            if (!headerPopupFt) return;
            if (headerPopupFt.menu) {
                _.forEach(headerPopupFt.menu.rows, function(rId: any) {
                    if (rId === rowIdx) {
                        new ContextMenu(row, headerPopupFt.menu.items);
                        wType = MENU;
                        return false;
                    }
                });
            }
            if (wType) return;
            if (headerPopupFt.popup) {
                _.forEach(headerPopupFt.popup.rows, function(rId: any) {
                    if (rId === rowIdx) {
                        new PopupPanel(row, headerPopupFt.popup.provider);
                        wType = POPUP;
                        return false;
                    }
                });
            }
            return wType;
        }
        
        /**
         * Hide.
         */
        function hideIfOutside($w: JQuery) {
            $(document).on(events.MOUSE_DOWN, function(evt) {
                if (outsideOf($w, evt.target)) {
                    $w.hide();
                }
            });
            
            var outsideOf = function($container, target) {
                return !$container.is(target) && $container.has(target).length === 0;
            };
        }
        
        /**
         * Offset.
         */
        function eventPageOffset(evt, isFixed) {
            var scrollLeft = window.pageXOffset || document.documentElement.scrollLeft;
            var scrollTop = window.pageYOffset || document.documentElement.scrollTop;
            let $contentsArea = $("#contents-area");
            return isFixed ? { pageX: evt.pageX + $contentsArea.scrollLeft() - scrollLeft, 
                               pageY: evt.pageY + $contentsArea.scrollTop() - scrollTop } 
                           : { pageX: evt.pageX + $contentsArea.scrollLeft(), 
                               pageY: evt.pageY + $contentsArea.scrollTop() };
        }
        
        /**
         * Class.
         */
        function cssClass(options) {
            var css = options.showBelow ? 'bottom' : 'top';
            css += '-';
            css += (options.showRight ? 'right' : 'left');
            css += '-tooltip';
            return css;
        }
    }
}