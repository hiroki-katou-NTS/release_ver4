/*!@license
 * Infragistics.Web.ClientUI Grid Responsive 19.1.20
 *
 * Copyright (c) 2011-2019 Infragistics Inc.
 *
 * http://www.infragistics.com/
 *
 * Depends on:
 *	jquery-1.9.1.js
 *	jquery.ui.core.js
 *	jquery.ui.widget.js
 *	infragistics.ui.grid.framework.js
 *	infragistics.ui.tree.js
 *	infragistics.ui.shared.js
 *	infragistics.dataSource.js
 *	infragistics.util.js
 */
(function(factory){if(typeof define==="function"&&define.amd){define(["./infragistics.ui.grid.framework"],factory)}else{return factory(jQuery)}})(function($){"use strict";$.widget("ui.igGridResponsive",$.ui.igWidget,{localeWidgetName:"igGridResponsive",options:{columnSettings:[{columnKey:null,columnIndex:null,classes:"",configuration:null}],reactOnContainerWidthChanges:true,forceResponsiveGridWidth:true,responsiveSensitivity:20,responsiveModes:null,enableVerticalRendering:true,windowWidthToRenderVertically:null,propertiesColumnWidth:"50%",valuesColumnWidth:"50%",allowedColumnWidthPerType:{string:120,number:50,bool:50,date:80,object:150},singleColumnTemplate:null,inherit:false},events:{responsiveColumnHiding:"responsiveColumnHiding",responsiveColumnHidden:"responsiveColumnHidden",responsiveColumnShowing:"responsiveColumnShowing",responsiveColumnShown:"responsiveColumnShown",responsiveModeChanged:"responsiveModeChanged"},css:{verticalContainerCssClass:"ui-iggrid-responsive-vertical"},_createWidget:function(){this.options.columnSettings=[];$.Widget.prototype._createWidget.apply(this,arguments)},_create:function(){this._callBackId=null;this._responsive=null;this._exclusiveContainer=false;this._mode="";this._modes=this.options.responsiveModes||{desktop:"infragistics",tablet:"infragistics",phone:"infragistics"};this._modes=this._initializeModeRecognizers(this._modes);this._hiddenByClass=this._flagClassConfiguration();this._defaultColumnTemplates=null;this._defaultColumnFormatters=null;this._footersDisplacement=.1;this._originalRenderRecord=null;this._newRenderRecord=null;this._vrw=typeof this.options.windowWidthToRenderVertically==="string"?parseInt(this.options.windowWidthToRenderVertically,10):this.options.windowWidthToRenderVertically;this._vr=null},_registerWidget:$.noop,_unregisterWidget:$.noop,_setOption:function(key,value){switch(key){case"propertiesColumnWidth":case"valuesColumnWidth":throw new Error(this._getLocaleValue("optionChangeNotSupported").replace("{optionName}",key));case"responsiveModes":this._modes=value;this._modes=this._initializeModeRecognizers(this._modes);break;case"reactOnContainerWidthChanges":if(value===true){this._activateContainer()}else{this._deactivateContainer()}break;case"enableVerticalRendering":this.options.enableVerticalRendering=value;if(this._shouldRenderVertically()){if(!this._vr){this._enableNoHeaderLayout(true,false)}}else{if(this._vr){this._disableNoHeaderLayout()}}break;case"windowWidthToRenderVertically":this._vrw=typeof value==="string"?parseInt(value,10):value;break}$.Widget.prototype._setOption.apply(this,arguments);this._hiddenByClass=this._flagClassConfiguration()},destroy:function(){this._deactivateContainer();if(this._initialGridRenderedHandler){this.grid.element.unbind("iggridrendered",this._initialGridRenderedHandler);this.grid.element.unbind("iggridheaderrendering",this._gridHeaderRenderingHandler)}this.grid.element.unbind("iggrid_heightchanged",this._gridContainerHeightHandler);$("#"+this.grid.element[0].id+"_responsive_test_container").remove();this.grid._renderRecord=this._originalRenderRecord;this.grid._renderColgroup=this._originalRenderColgroup;this.grid._renderCell=this._originalRenderCell;this.grid.renderNewRow=this._originalRenderNewRow;this._superApply(arguments);return this},getCurrentResponsiveMode:function(){return this._mode},_activateContainer:function(){if(!this._responsive||typeof this._responsive.addCallback!=="function"){this._responsive=this.grid.element.closest(".ui-widget").igResponsiveContainer().data("igResponsiveContainer");this._exclusiveContainer=true}this._callBackId=this._responsive.addCallback(this._containerResized,this,this.options.responsiveSensitivity,"x")},_deactivateContainer:function(){if(typeof this._callBackId==="number"){this._responsive.removeCallback(this._callBackId);this._callBackId=null}if(this._exclusiveContainer===true){this._responsive.destroy();delete this._responsive;this._exclusiveContainer=false}},_renderTestElement:function(){$("<div></div>").attr("id",this.grid.element[0].id+"_responsive_test_container").css("position","fixed").css("height","0px").css("top","-100px").text("&nbsp;").appendTo(this.grid.container())},_initializeModeRecognizers:function(){var self=this,transformed={},nval;$.each(this._modes,function(key,value){if(typeof value==="string"){nval=value.substring(0,1).toUpperCase()+value.substring(1);nval=new $.ig[nval+"Mode"]({key:key,visibilityTester:$.proxy(self._checkVisibilityByClass,self)})}else if(typeof value==="object"&&!value.isActive){nval=new $.ig.ResponsiveMode({minWidth:value.minWidth||-1,maxWidth:value.maxWidth||Number.MAX_VALUE,minHeight:value.minHeight||-1,maxHeight:value.maxHeight||Number.MAX_VALUE})}else{nval=value}transformed[key]=nval});return transformed},_flagClassConfiguration:function(){var byClass=false;$.each(this.options.columnSettings,function(){if(this.classes){byClass=true;return false}});return byClass},_getDefaultColumnTemplates:function(){var cache={},col,i;for(i=0;i<this.grid.options.columns.length;i++){col=this.grid.options.columns[i];if(col.template){if(col.key){cache[col.key]=col.template}else{cache[i]=col.template}}}return cache},_getDefaultColumnFormatters:function(){var cache={},col,i;for(i=0;i<this.grid.options.columns.length;i++){col=this.grid.options.columns[i];if(col.formatter){if(col.key){cache[col.key]=col.formatter}else{cache[i]=col.formatter}}}return cache},_checkVisibilityByClass:function(classes){var testContainer=$("#"+this.grid.element[0].id+"_responsive_test_container");testContainer.removeClass();testContainer.addClass(classes);return testContainer.is(":visible")},_gridReady:function(){return!(this.grid._loadingIndicator&&this.grid._loadingIndicator._indicator.is(":visible"))},_shouldRenderVertically:function(){if(!this.options.enableVerticalRendering){return false}if(this._vrw===null||this._vrw===undefined){return this._shouldRenderVerticallyByDefault()}if($(window).width()<this._vrw){return true}if($(window).width()>=this._vrw){return false}return false},_shouldRenderVerticallyByDefault:function(){var i,gw=this.grid.element.width(),gp=100,cw,cols=[],col,tcocww=0,p,aw;for(i=0;i<this.grid._visibleColumns().length;i++){col=this.grid._visibleColumns()[i];if(col.width){cw=-1;if(typeof col.width==="string"){if(col.width.endsWith("%")){cw=parseInt(col.width,10);gp-=cw}else{gw-=parseInt(col.width,10)}}else{gw-=col.width}if(cw!==-1){cols.push({key:col.key||i,width:cw,type:col.dataType})}continue}tcocww++;cols.push({key:col.key||i,width:-1,type:col.dataType})}for(i=0;i<cols.length;i++){p=cols[i].width===-1?gp/tcocww:cols[i].width;aw=p/100*gw;if(aw<=this.options.allowedColumnWidthPerType[cols[i].type]){return true}}return false},_isSingleColumn:function(){if(this.options.singleColumnTemplate&&this.options.singleColumnTemplate[this._mode]){return true}},_updateGridSync:function(initial,full){var self=this;if(!this._gridReady()){setTimeout(function(){self._updateGridSync(initial,full)},50);return}return self._updateGrid(initial,full)},_updateGrid:function(initial,full){var result=[],applyTemplatesFormatters=false,colsToShow,colsToHide,self=this,i;this._vr=this._vr===null?this._shouldRenderVertically():this._vr;delete this.grid._visibleColumnsArray;if(this._vr){this.grid.element.addClass(this.css.verticalContainerCssClass)}if(this._hiddenByClass===true){result=this._updateColumnsByClass()}if(full===true){if(this._hiddenByClass===false){result=this._updateColumns()}applyTemplatesFormatters=this._updateTemplatesFormatters()}colsToShow=result[0]||[];colsToHide=result[1]||[];if(colsToShow.length===0&&colsToHide.length===0){if(applyTemplatesFormatters===true&&initial===false){return true}return}if(initial===false){this.grid._loadingIndicator.show();setTimeout(function(){self.grid._setHiddenColumns(colsToShow,false,initial);for(i=0;i<colsToShow.length;i++){self._trigger(self.events.responsiveColumnShown,null,self._getArgsByColumn(colsToShow[i]))}self.grid._setHiddenColumns(colsToHide,true,initial);for(i=0;i<colsToHide.length;i++){self._trigger(self.events.responsiveColumnHidden,null,self._getArgsByColumn(colsToHide[i]))}if(initial===false){self.grid._loadingIndicator.hide()}self._setMaxWidthOnGrid()},0)}else{$.each(colsToShow,function(){this.hidden=false});$.each(colsToHide,function(){this.hidden=true})}},_updateColumnsByClass:function(){var i,cs=this.options.columnSettings,col,colsToHide=[],colsToShow=[],noCancel,hidden,visible;for(i=0;i<cs.length;i++){if(cs[i].classes){col=this._getGridColumnBySetting(cs[i]);if(!col){continue}hidden=col.hidden||false;visible=this._checkVisibilityByClass(cs[i].classes);if(hidden===true&&visible===true){noCancel=this._trigger(this.events.responsiveColumnShowing,null,this._getArgsByColumnSetting(cs[i]));if(noCancel){colsToShow.push(col)}}if(hidden===false&&visible===false){noCancel=this._trigger(this.events.responsiveColumnHiding,null,this._getArgsByColumnSetting(cs[i]));if(noCancel){colsToHide.push(col)}}}}return[colsToShow,colsToHide]},_updateColumns:function(){var i,cs=this.options.columnSettings,col,colsToHide=[],colsToShow=[],noCancel,m=this._mode,hidden,visible;for(i=0;i<cs.length;i++){if(cs[i].configuration){col=this._getGridColumnBySetting(cs[i]);if(!col){continue}hidden=col.hidden||false;visible=!(cs[i].configuration[m]&&cs[i].configuration[m].hidden);if(hidden===true&&visible===true){noCancel=this._trigger(this.events.responsiveColumnShowing,null,this._getArgsByColumnSetting(cs[i]));if(noCancel){colsToShow.push(col)}}if(hidden===false&&visible===false){noCancel=this._trigger(this.events.responsiveColumnHiding,null,this._getArgsByColumnSetting(cs[i]));if(noCancel){colsToHide.push(col)}}}}return[colsToShow,colsToHide]},_updateTemplatesFormatters:function(){var i,cs=this.options.columnSettings,nt,nf,updated=false,col,m=this._mode,jsRndr=String(this.grid.options.templatingEngine).toLowerCase()==="jsrender";for(i=0;i<cs.length;i++){col=this._getGridColumnBySetting(cs[i]);if(!col){continue}if(cs[i].configuration&&cs[i].configuration[m]&&cs[i].configuration[m].template){nt=cs[i].configuration[m].template}else{nt=col.key?this._defaultColumnTemplates[col.key]:this._defaultColumnTemplates[$.inArray(col,this.grid.options.columns)]}if(cs[i].configuration&&cs[i].configuration[m]&&cs[i].configuration[m].formatter){nf=cs[i].configuration[m].formatter;nf=$.type(nf)==="string"?window[nf]:nf}else{nf=col.key?this._defaultColumnFormatters[col.key]:this._defaultColumnFormatters[$.inArray(col,this.grid.options.columns)]}if(col.template!==nt){col.template=nt;updated=true}if(col.formatter!==nf){col.formatter=nf;updated=true}}if(updated===true){if(!this.grid._tmplWrappers){this.grid._tmplWrappers=jsRndr?$.render:{}}this.grid._setTemplateDefinition(jsRndr)}return updated},_executeTemplate:function(data){if(this._jsr){return $.render[this.grid.id()+"_responsiveSct_"+this._mode](data).replace("<td","").replace("</td>","")}return $.ig.tmpl(this.options.singleColumnTemplate[this._mode],data).replace("<td","").replace("</td>","")},_renderRecord:function(data,index){if(this._vr){return this._renderRecordVerticalGrid(data,index)}if(this._scr){return this._renderRecordSingleColumnGrid(data,index)}return this._originalRenderRecord(data,index)},_renderRecordVerticalGrid:function(data,index){var i=0,str="",tstr,alt,vc=this.grid._visibleColumns(),key=this.grid.options.primaryKey;alt=index%2!==0&&this.grid.options.alternateRowStyles;for(i=0;i<vc.length;i++){str+="<tr";if(alt){str+=' class="'+this.grid.css.recordAltClass+'"'}if(key!==undefined&&key!==null&&data[key]!==null&&data[key]!==undefined){str+=' data-id="'+data[key]+'"'}str+=' data-col-key="'+vc[i].key+'"';str+="><td>";str+=vc[i].headerText+"</td>";str+='<td aria-readonly="true"';if(vc[i].template&&vc[i].template.length){tstr=this.grid._renderTemplatedCell(data,vc[i]);if(tstr.indexOf("<td")===0){str+=tstr.substring(3)}else{str+=">"+tstr}}else{str+=' aria-describedby="'+this.grid.id()+"_"+vc[i].key+'">'+this.grid._renderCell(data[vc[i].key],vc[i])}str+="</td></tr>"}return str},_renderRecordSingleColumnGrid:function(data,index){var str="<tr",pk=this.grid.options.primaryKey,formattedData,key,tmplRes,dtype;if(index%2!==0&&this.options.alternateRowStyles){str+=' class="'+this.grid.css.recordAltClass+'"'}if(pk!==null&&pk!==undefined){str+=' data-id="'+this.grid._kval_from_key(pk,data)+'"'}else if(data.ig_pk!==null&&pk!==undefined){str+=' data-id="'+data.ig_pk+'"'}str+='><td aria-readonly="true"';formattedData=$.extend(true,{},data);for(key in formattedData){if(formattedData.hasOwnProperty(key)){dtype=$.type(data[key]);formattedData[key]=this.grid._renderCell(data[key],this.grid.columnByKey(key)||{},formattedData,null,dtype==="object"||dtype==="array")}}tmplRes=this._executeTemplate(formattedData);if(tmplRes.indexOf("<td")===0){str+=tmplRes.substring(3)}else{str+=">"+tmplRes}str+="</td></tr>";return str},_renderNewRow:function(rec){var tbody,go,index,virt;if(this._vr){tbody=this.element.children("tbody");go=this.grid.options;virt=go.virtualization===true||go.rowVirtualization===true;if(virt){this._renderVirtualRecordsContinuous();this._startRowIndex=0;this.virtualScrollTo(this._totalRowCount)}else{index=this.grid._getDataView().length-1;tbody.append(this.grid._renderRecord(rec,index))}}else{this._originalRenderNewRow(rec)}},_renderCell:function(val,col,record,displayStyle,returnObject){var type=col.dataType,format=col.format,o=this.grid.options,auto=o.autoFormat;if(record){val=this.grid.dataSource.getCellValue(col.key,record)}val=this.grid._fixDate(val,col);if(col.formatter){return col.formatter(val,record,this._mode)}if(!format&&type==="bool"&&o.renderCheckboxes){format="checkbox"}if(format==="checkbox"&&type!=="bool"){format=null}type=type==="date"||type==="number"?type:"";if(format||(auto===true||auto==="dateandnumber")&&type||auto&&auto===type){return $.ig.formatter(val,type,format,true,col.dateDisplayType==="utc",this.grid._getOffsetForCol(record,col),displayStyle,col.headerText,this.grid.options.tabIndex)}if(returnObject){return val}return val||val===0||val===false?val.toString():"&nbsp;"},_renderColgroup:function(table,isHeader,isFooter,autofitLastColumn){var colgroup,fcw,scw;this._vr=this._vr===null?this._shouldRenderVertically():this._vr;this._scr=this._scr===undefined||this._scr===null?this._isSingleColumn():this._scr;if(!this._vr&&!this._scr){this._originalRenderColgroup(table,isHeader,isFooter,autofitLastColumn);return}colgroup=$(table).find("colgroup");if(colgroup.length===0){colgroup=$("<colgroup></colgroup>").prependTo(table)}colgroup.empty();if(this._vr){fcw=typeof this.options.propertiesColumnWidth==="string"?parseInt(this.options.propertiesColumnWidth,10):this.options.propertiesColumnWidth;scw=typeof this.options.valuesColumnWidth==="string"?parseInt(this.options.valuesColumnWidth,10):this.options.valuesColumnWidth;colgroup.append('<col width="'+fcw+'%"></col><col width="'+scw+'%"></col>')}else if(this._scr){colgroup.append('<col width="100%"></col>')}},_enableNoHeaderLayout:function(vr,scr){this.grid.element.addClass(this.css.verticalContainerCssClass);if(!this.grid.options.showHeader||!this.grid.options.fixedHeaders||this.grid.options.height===null){this.grid.headersTable().children("thead").css("display","none")}else{this.grid.headersTable().css("position","absolute");this.grid.headersTable().css("top","-100px")}this._vr=vr;this._scr=scr;this._modifySortingStyles();this._disableUpdating();this.grid._rerenderColgroups();this.grid._renderData()},_disableNoHeaderLayout:function(){this.grid.element.removeClass(this.css.verticalContainerCssClass);if(!this.grid.options.showHeader||!this.grid.options.fixedHeaders||this.grid.options.height===null){this.grid.headersTable().children("thead").css("display","")}else{this.grid.headersTable().css("position","");this.grid.headersTable().css("top","")}this._scr=false;this._vr=false;this._modifySortingStyles(true);this._enableUpdating();this.grid._rerenderColgroups();this.grid._renderData()},_disableUpdating:function(){if(this.grid.element.data("igGridUpdating")){this._em=this.grid.element.igGridUpdating("option","editMode");this._dm=this.grid.element.igGridUpdating("option","enableDeleteRow");this.grid.element.igGridUpdating("option","editMode","none");this.grid.element.igGridUpdating("option","enableDeleteRow",false)}},_enableUpdating:function(){if(this.grid.element.data("igGridUpdating")){if(this._em){this.grid.element.igGridUpdating("option","editMode",this._em)}if(this._dm){this.grid.element.igGridUpdating("option","enableDeleteRow",true)}}},_getGridColumnBySetting:function(cs){var col;if(cs.columnKey&&typeof cs.columnKey==="string"){col=this.grid.columnByKey(cs.columnKey)}else if(cs.columnIndex!==null&&cs.columnIndex!==undefined&&typeof cs.columnIndex==="number"&&cs.columnIndex>=0&&cs.columnIndex<this.grid.options.columns.length){col=this.grid.options.columns[cs.columnIndex]}return col},_getArgsByColumnSetting:function(cs){return{owner:this,columnIndex:cs.columnIndex||null,columnKey:cs.columnKey||null}},_getArgsByColumn:function(col){return{owner:this,columnIndex:$.inArray(col,this.grid.options.columns),columnKey:col.key||null}},_getCurrentMode:function(){var env;$.each(this._modes,function(key){if(this.isActive()===true){env=key;return false}});return env},_containerResized:function(nw,nh){var mode=this._getCurrentMode(),prevMode,shouldUpdate=false,shouldRerender=false;if(nw<=0&&nh<=0){return}this._setMaxWidthOnGrid();if(mode&&mode!==this._mode){prevMode=this._mode;this._mode=mode;this._trigger(this.events.responsiveModeChanged,null,{owner:this,previousMode:prevMode,mode:mode});shouldUpdate=true}if(shouldUpdate||this._hiddenByClass){shouldRerender=this._updateGridSync(false,shouldUpdate)}if(this._isSingleColumn()){if(!this._scr){this._enableNoHeaderLayout(false,true)}else if(shouldUpdate){this.grid._renderData()}}else{if(this._scr){this._disableNoHeaderLayout();this._scr=false}else if(shouldRerender){this.grid._renderData()}}if(this._shouldRenderVertically()){if(!this._vr){this._enableNoHeaderLayout(true,false)}}else{if(this._vr){this._disableNoHeaderLayout();this._vr=false}else if(shouldRerender){this.grid._renderData()}}},_gridHeaderRendering:function(evt,ui){var i;if(ui.owner.id()!==this.grid.id()){return}this._renderTestElement();this._mode=this._getCurrentMode();this._defaultColumnTemplates=this._getDefaultColumnTemplates();this._defaultColumnFormatters=this._getDefaultColumnFormatters();if($.isArray(this.grid._initialHiddenColumns)){for(i=0;i<this.grid._initialHiddenColumns.length;i++){this.grid._initialHiddenColumns[i].hidden=true}}this._updateGrid(true,true);this.grid._captureInitiallyHiddenColumns()},_initialGridRendered:function(){if(this.options.reactOnContainerWidthChanges){this._activateContainer()}if(this._vr||this._scr){if(!this.grid.options.showHeader||!this.grid.options.fixedHeaders||this.grid.options.height===null){this.grid.headersTable().children("thead").css("display","none")}else{this.grid.headersTable().css("position","absolute");this.grid.headersTable().css("top","-100px")}this._disableUpdating();this._modifySortingStyles()}this._alreadyRendered=true;this._setMaxWidthOnGrid()},_dataRendered:function(){this._setMaxWidthOnGrid()},_modifySortingStyles:function(restore){var sorting=this.grid.element.data("igGridSorting");if(sorting){if(restore){sorting.options.applySortedColumnCss=this._srs}else{this._srs=sorting.options.applySortedColumnCss;sorting.options.applySortedColumnCss=false}}},_heightChanged:function(){var newHeight=$("#"+this.grid.element[0].id+"_scroll").height();if(this._height!==newHeight){this._height=newHeight;this.grid._adjustLastColumnWidth(false);this._setMaxWidthOnGrid()}},_setMaxWidthOnGrid:function(){var sbw=this.grid._hasVerticalScrollbar===true?this.grid._scrollbarWidth():0,npw;if(this.options.forceResponsiveGridWidth){this.grid._gridContentWidth=0;this.grid.element.css("width","100%");if(!this.grid._allColumnWidthsInPercentage){npw=100-sbw/this.grid.container().width()*100;if(this.grid.options.fixedHeaders===true){this.grid.headersTable().css("width",npw+"%")}if(this.grid.options.fixedFooters===true){this.grid.footersTable().css("width",npw+this._footersDisplacement+"%")}}}},_createHandlers:function(){this._gridHeaderRenderingHandler=$.proxy(this._gridHeaderRendering,this);this._initialGridRenderedHandler=$.proxy(this._initialGridRendered,this);this._gridContainerHeightHandler=$.proxy(this._heightChanged,this)},_injectGrid:function(gridInstance,isRebind){var key,sct;if(isRebind===true){return}this.grid=gridInstance;this._checkGridNotSupportedFeatures();this._createHandlers();if(this.options.forceResponsiveGridWidth===true){this.grid.options.width=typeof this.grid.options.width==="string"&&this.grid.options.width.endsWith("%")?this.grid.options.width:"100%";this.grid.options.autoFitLastColumn=false}this.grid.element.bind("iggridheaderrendering",this._gridHeaderRenderingHandler);this.grid.element.bind("iggridrendered",this._initialGridRenderedHandler);if(this.grid.options.height!==null&&this.grid.options.height.indexOf&&this.grid.options.height.indexOf("%")!==-1){this.grid.element.bind("iggrid_heightchanged",this._gridContainerHeightHandler)}this._originalRenderRecord=$.proxy(this.grid._renderRecord,this.grid);this._newRenderRecord=$.proxy(this._renderRecord,this);this._originalRenderColgroup=$.proxy(this.grid._renderColgroup,this.grid);this._newRenderColgroup=$.proxy(this._renderColgroup,this);this._originalRenderCell=this.grid._renderCell;this._newRenderCell=$.proxy(this._renderCell,this);this._originalRenderNewRow=$.proxy(this.grid.renderNewRow,this.grid);this._newRenderNewRow=$.proxy(this._renderNewRow,this);this.grid._renderRecord=this._newRenderRecord;this.grid._renderColgroup=this._newRenderColgroup;this.grid._renderCell=this._newRenderCell;this.grid.renderNewRow=this._newRenderNewRow;if(String(this.grid.options.templatingEngine).toLowerCase()==="jsrender"){this._jsr=true;sct=this.options.singleColumnTemplate;if(sct&&typeof sct==="object"){for(key in sct){if(sct.hasOwnProperty(key)){$.templates(this.grid.id()+"_responsiveSct_"+key,sct[key])}}}}},_checkGridNotSupportedFeatures:function(){var gridOptions=this.grid.options;if((gridOptions.virtualization===true||gridOptions.rowVirtualization===true||gridOptions.columnVirtualization===true)&&gridOptions.virtualizationMode==="fixed"){throw new Error(this._getLocaleValue("fixedVirualizationNotSupported"))}}});$.extend($.ui.igGridResponsive,{version:"19.1.20"});$.ig=$.ig||{};$.ig.ResponsiveMode=$.ig.ResponsiveMode||Class.extend({settings:{minWidth:-1,maxWidth:Number.MAX_VALUE,minHeight:-1,maxHeight:Number.MAX_VALUE},init:function(options){if(options){this.settings=$.extend(true,{},$.ig.ResponsiveMode.prototype.settings,options)}return this},isActive:function(){return window.innerWidth>=this.settings.minWidth&&window.innerWidth<=this.settings.maxWidth&&window.innerHeight>=this.settings.minHeight&&window.innerHeight<=this.settings.maxHeight}});$.ig.InfragisticsMode=$.ig.InfragisticsMode||$.ig.ResponsiveMode.extend({settings:{key:"",visibilityTester:null},init:function(options){this._hc="ui-hidden-"+options.key;this._vc="ui-visible-"+options.key;this._super(options);return this},isActive:function(){if(typeof this.settings.visibilityTester==="function"){return this.settings.visibilityTester(this._hc)===false&&this.settings.visibilityTester(this._vc)===true}return this._super()}});$.ig.BootstrapMode=$.ig.BootstrapMode||$.ig.ResponsiveMode.extend({settings:{key:"",visibilityTester:null},init:function(options){this._hc="hidden-"+options.key;this._vc="visible-"+options.key;this._super(options);return this},isActive:function(){if(typeof this.settings.visibilityTester==="function"){return this.settings.visibilityTester(this._hc)===false&&this.settings.visibilityTester(this._vc)===true}return this._super()}});return $});