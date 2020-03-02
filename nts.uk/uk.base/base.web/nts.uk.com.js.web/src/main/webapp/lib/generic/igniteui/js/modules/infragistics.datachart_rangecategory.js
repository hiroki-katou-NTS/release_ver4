/*!@license
* Infragistics.Web.ClientUI infragistics.datachart_rangecategory.js 19.1.20191.172
*
* Copyright (c) 2011-2019 Infragistics Inc.
*
* http://www.infragistics.com/
*
* Depends:
*     jquery-1.4.4.js
*     jquery.ui.core.js
*     jquery.ui.widget.js
*     infragistics.util.js
*     infragistics.ext_core.js
*     infragistics.ext_collections.js
*     infragistics.dv_core.js
*     infragistics.dv_geometry.js
*     infragistics.datachart_categorycore.js
*     infragistics.datachart_core.js
*     infragistics.ext_ui.js
*/
(function(factory){if(typeof define==="function"&&define.amd){define(["./infragistics.util","./infragistics.ext_core","./infragistics.ext_collections","./infragistics.dv_core","./infragistics.dv_geometry","./infragistics.datachart_categorycore","./infragistics.datachart_core","./infragistics.ext_ui"],factory)}else{factory(igRoot)}})(function($){$.ig=$.ig||{};var $$t={};$.ig.globalDefs=$.ig.globalDefs||{};$.ig.globalDefs.$$h=$$t;$$0=$.ig.globalDefs.$$0;$$4=$.ig.globalDefs.$$4;$$1=$.ig.globalDefs.$$1;$$w=$.ig.globalDefs.$$w;$$r=$.ig.globalDefs.$$r;$$g=$.ig.globalDefs.$$g;$$j=$.ig.globalDefs.$$j;$$6=$.ig.globalDefs.$$6;$$a=$.ig.globalDefs.$$a;$.ig.$currDefinitions=$$t;$.ig.util.bulkDefine(["RangeCategoryBucketCalculator:a","RangeAreaSeries:c","RangeCategorySeries:h","RangeColumnSeries:i","RangeAreaSeriesView:j","RangeCategorySeriesView:k","RangeColumnSeriesView:l"]);var $a=$.ig.intDivide,$b=$.ig.util.cast,$c=$.ig.util.defType,$d=$.ig.util.defEnum,$e=$.ig.util.getBoxIfEnum,$f=$.ig.util.getDefaultValue,$g=$.ig.util.getEnumValue,$h=$.ig.util.getValue,$i=$.ig.util.intSToU,$j=$.ig.util.nullableEquals,$k=$.ig.util.nullableIsNull,$l=$.ig.util.nullableNotEquals,$m=$.ig.util.toNullable,$n=$.ig.util.toString$1,$o=$.ig.util.u32BitwiseAnd,$p=$.ig.util.u32BitwiseOr,$q=$.ig.util.u32BitwiseXor,$r=$.ig.util.u32LS,$s=$.ig.util.unwrapNullable,$t=$.ig.util.wrapNullable,$u=String.fromCharCode,$v=$.ig.util.castObjTo$t,$w=$.ig.util.compareSimple,$x=$.ig.util.tryParseNumber,$y=$.ig.util.tryParseNumber1,$z=$.ig.util.numberToString,$0=$.ig.util.numberToString1,$1=$.ig.util.parseNumber;$c("RangeCategoryBucketCalculator:a","CategoryBucketCalculator",{_j:null,init:function(a){this.i=null;this.h=null;$.ig.CategoryBucketCalculator.prototype.init.call(this,a);this._j=a},getBucketWithoutUnknowns:function(a){var b=false;var c=this.i;var d=this.h;var e=this.l;var f=this.k;var g=Math.min(e,f);var h=a*this.d;var i=Math.min(h+this.d-1,g-1);var j=1.7976931348623157e308;var k=-1.7976931348623157e308;var l=true;var m=0;var n=0;var o;var p;for(var q=h;q<=i;++q){m=c[q];n=d[q];if(m<n){o=m;p=n}else{p=m;o=n}if(!l){j=j<o?j:o;k=k>o?k:o;j=j<p?j:p;k=k>p?k:p}else{j=Math.min(j,o);k=Math.max(k,o);k=Math.max(k,p);j=Math.min(j,p);l=false}}if(b&&n<m){var r=j;j=k;k=r}if(!l){var s=new Array(3);s[0]=.5*(h+i);s[1]=j;s[2]=k;return s}var t=new Array(3);t[0]=NaN;t[1]=NaN;t[2]=NaN;return t},getBucket:function(a){var b=false;var c=this.i;var d=this.h;var e=this.l;var f=this.k;var g=Math.min(e,f);var h=a*this.d;var i=Math.min(h+this.d-1,g-1);var j=NaN;var k=NaN;var l=true;var m=0;var n=0;var o;var p;for(var q=h;q<=i;++q){m=c[q];n=d[q];if(m<n){o=m;p=n}else{p=m;o=n}if(!l){if(!$.ig.util.isNaN(o)){j=j<o?j:o;k=k>o?k:o}if(!$.ig.util.isNaN(p)){j=j<p?j:p;k=k>p?k:p}}else{if(!$.ig.util.isNaN(o)){if($.ig.util.isNaN(j)){j=o}else{j=Math.min(j,o)}if(!$.ig.util.isNaN(k)){k=Math.max(k,o)}}if(!$.ig.util.isNaN(p)){if($.ig.util.isNaN(k)){k=p}else{k=Math.max(k,p)}if(!$.ig.util.isNaN(j)){j=Math.min(j,p)}}if(!$.ig.util.isNaN(j)&&!$.ig.util.isNaN(k)){l=false}}}if(n<m&&b){var r=j;j=k;k=r}if(!l){var s=new Array(3);s[0]=.5*(h+i);s[1]=j;s[2]=k;return s}var t=new Array(3);t[0]=NaN;t[1]=NaN;t[2]=NaN;return t},l:0,k:0,i:null,h:null,cacheValues:function(){this.l=this._j._cw.lowColumn().count();this.k=this._j._cw.highColumn().count();this.i=this._j._cw.lowColumn().asArray();this.h=this._j._cw.highColumn().asArray()},unCacheValues:function(){this.i=null;this.h=null},$type:new $.ig.Type("RangeCategoryBucketCalculator",$.ig.CategoryBucketCalculator.prototype.$type)},true);$c("HorizontalRangeCategorySeriesProxy:b","Object",{init:function(){$.ig.$op.init.call(this)},matchesType:function(a){return $b($$t.$g.$type,a)!==null},setHighMemberPath:function(a,b){a.highMemberPath(b)},setLowMemberPath:function(a,b){a.lowMemberPath(b)},setXAxis:function(a,b){a.xAxis(b)},setYAxis:function(a,b){a.yAxis(b)},$type:new $.ig.Type("HorizontalRangeCategorySeriesProxy",$.ig.$ot,[$.ig.IHorizontalRangeCategorySeriesProxy.prototype.$type])},true);$c("RangeCategorySeries:h","CategorySeries",{bf:function(){return new $$t.k(this)},getHostReferenceValue:function(){return this.ee()},im:function(a){$.ig.CategorySeries.prototype.im.call(this,a);this._op=a},_op:null,init:function(){$.ig.CategorySeries.prototype.init.call(this);this._oo=new $$t.f(1,this,this._op,this,this,this._op._cn)},_oo:null,lowMemberPath:function(a){if(arguments.length===1){this.h($$t.$h.lowMemberPathProperty,a);return a}else{return this.c($$t.$h.lowMemberPathProperty)}},lowColumn:function(a){if(arguments.length===1){if(this.os!=a){var b=this.os;this.os=a;this.raisePropertyChanged("LowColumn",b,this.os)}return a}else{return this.os}},os:null,highMemberPath:function(a){if(arguments.length===1){this.h($$t.$h.highMemberPathProperty,a);return a}else{return this.c($$t.$h.highMemberPathProperty)}},highColumn:function(a){if(arguments.length===1){if(this.oq!=a){var b=this.oq;this.oq=a;this.raisePropertyChanged("HighColumn",b,this.oq)}return a}else{return this.oq}},oq:null,m8:function(){return 11},og:function(a,b){$.ig.CategorySeries.prototype.og.call(this,a,b);b._b0=true;$.ig.CategoryMarkerManager.prototype.b(this,a.m,b._cq,this.useLightweightMarkers());this.oa(b,a)},o0:function(a,b,c,d,e,f){this._op.cx(a,b,c,d,e,f)},scrollIntoView:function(a){var b=new $$a.ae(0,0,0,1,1);var c=this.view()!=null?this.view().bs():$$a.$ae.empty();c=c.copy();var d=this.view()!=null?this.view().br():$$a.$ae.empty();var e=!c.isEmpty()&&!d.isEmpty()&&this.bz()!=null?this.bz().indexOf(a):-1;var f=this.mw();var g=this.mx();var h=this.lk(this.view());var i;if(f!=null){var j=new $.ig.ScalerParams(0,b,b,f.isInverted(),h);i=f.getScaledValue(e,j)}else{i=NaN}var k=f!=null?this._oo.r($b($.ig.ICategoryScaler.prototype.$type,f),b,b,h):0;i+=k;if(e>=0&&$$a.$ae.l_op_Inequality(c,null)&&$$a.$ae.l_op_Inequality(d,null)){if(!$.ig.util.isNaN(i)){if(i<c.left()+.1*c.width()){i=i+.4*c.width()}if(i>c.right()-.1*c.width()){i=i-.4*c.width()}c.x(i-.5*c.width())}if(g!=null&&this.highColumn()!=null&&e<this.highColumn().count()){var l=new $.ig.ScalerParams(0,b,b,g.isInverted(),h);l._b=this.ee();var m=g.getScaledValue(this.highColumn().item(e),l);var n=g.getScaledValue(this.lowColumn().item(e),l);if(!$.ig.util.isNaN(m)&&!$.ig.util.isNaN(n)){var o=Math.abs(n-m);if(c.height()<o){c.height(o);c.y(Math.min(n,m))}else{if(n<c.top()+.1*c.height()){n=n+.4*c.height()}if(n>c.bottom()-.1*c.height()){n=n-.4*c.height()}c.y(n-.5*c.height())}}}if(this.syncLink()!=null){this.syncLink().ap(this.seriesViewer(),c,true)}}return e>=0},is:function(a,b,c,d){$.ig.CategorySeries.prototype.is.call(this,a,b,c,d);switch(b){case"FastItemsSource":if($b($.ig.IFastItemsSource.prototype.$type,c)!=null){c.deregisterColumn(this.lowColumn());c.deregisterColumn(this.highColumn());this.lowColumn(null);this.highColumn(null)}if($b($.ig.IFastItemsSource.prototype.$type,d)!=null){this.lowColumn(this.bw(this.lowMemberPath()));this.highColumn(this.bw(this.highMemberPath()))}if(!this.nt()){this._m7._cn.g(this.resolution());this.renderSeries(false)}break;case"LowMemberPath":if(this.bz()!=null){this.bz().deregisterColumn(this.lowColumn());this.lowColumn(this.bw(this.lowMemberPath()))}break;case"LowColumn":if(!this.nt()){this._m7._cn.g(this.resolution());this.renderSeries(false)}break;case"HighMemberPath":if(this.bz()!=null){this.bz().deregisterColumn(this.highColumn());this.highColumn(this.bw(this.highMemberPath()))}break;case"HighColumn":if(!this.nt()){this._m7._cn.g(this.resolution());this.renderSeries(false)}break}},an:function(a){if(this.lowColumn()==null||this.lowColumn().count()==0||this.highColumn()==null||this.highColumn().count()==0){return null}if(a==this.mw()){var b=Math.min(this.lowColumn().count(),this.highColumn().count());return new $.ig.AxisRange(0,b-1)}if(a==this.mx()){var c=Math.min(this.lowColumn().minimum(),this.highColumn().minimum());var d=Math.max(this.lowColumn().maximum(),this.highColumn().maximum());return new $.ig.AxisRange(Math.min(c,d),Math.max(c,d))}return null},hs:function(a,b,c,d){if(this.mw()!=null&&$b($.ig.ISortingAxis.prototype.$type,this.mw())!==null){this.mw().notifyDataChanged()}var e=this.mw();switch(a){case 3:if(d==this.lowMemberPath()||d==this.highMemberPath()){if(e!=null){e.updateRange()}if(!this.nt()){this.renderSeries(true)}}break;case 1:if(e!=null){e.updateRange()}this._m7._cn.g(this.resolution());if(!this.nt()){this.renderSeries(true)}break;case 0:if(e!=null){e.updateRange()}this._m7._cn.g(this.resolution());if(!this.nt()){this.renderSeries(true)}break;case 2:if(e!=null){e.updateRange()}if(this.lowMemberPath()!=null&&this.highMemberPath()!=null&&this._m7._cn.d>0&&!this.nt()){this.renderSeries(true)}break;case 4:if(e!=null){e.updateRange()}this._m7._cn.g(this.resolution());if(!this.nt()){this.renderSeries(true)}break}},dv:function(a,b,c){var d=$.ig.CategorySeries.prototype.dv.call(this,a,b,c);if(this.lowColumn()==null||this.lowColumn().count()==0||this.highColumn()==null||this.highColumn().count()==0){d=false}return d},od:function(a,b){$.ig.CategorySeries.prototype.od.call(this,a,b);this._oo.d(a,b)},currentCategoryMode:function(){return this.preferredCategoryMode($b($.ig.CategoryAxisBase.prototype.$type,this.mw()))},scaler:function(){return $b($.ig.ICategoryScaler.prototype.$type,this.mw())},yScaler:function(){return $b($.ig.IScaler.prototype.$type,this.mx())},bucketizer:function(){return this._m7._cn},currentMode2Index:function(){return this.n1()},provideCollisionDetector:function(){return new $.ig.CollisionAvoider},mayContainUnknowns:function(){return this.lowColumn()==null||this.lowColumn().mayContainUnknowns()||this.highColumn()==null||this.highColumn().mayContainUnknowns()},i7:function(a,b){var c=this.dk();$.ig.CategorySeries.prototype.i7.call(this,a,b);if(!c){this.view().az(b);return}this.view().az(b);if(this.cc(this._bh)){return}var d=new $$t.f(1,this,$b($.ig.ISupportsMarkers.prototype.$type,this._bh),this.seriesViewer()._bn._m,this,this._bh._cn);if(!this._de){this.m1=new $.ig.CategoryFrame(3);this.m1.u();d.d(this.m1,this._bh)}this._de=false;this.og(this.m1,this._bh);this.dk(false)},_on:null,renderAlternateView:function(a,b,c,d,e){$.ig.CategorySeries.prototype.renderAlternateView.call(this,a,b,c,d,e);var f=this.a0().alternateViews().item(d);var g=f;g._cn.g(this.resolution());f.prepAltSurface(c);if(this.cc(g)){return}var h=new $$t.f(1,this,$b($.ig.ISupportsMarkers.prototype.$type,f),f,this,f._cn);if(this._on==null){this._on=new $.ig.CategoryFrame(3)}this._on.u();h.d(this._on,g);this.og(this._on,g)},ee:function(){return $.ig.Series.prototype.ef(this.lowColumn(),$b($.ig.ISortingAxis.prototype.$type,this.mw()))},$type:new $.ig.Type("RangeCategorySeries",$.ig.CategorySeries.prototype.$type,[$.ig.IIsCategoryBased.prototype.$type,$.ig.IHasHighLowValueCategory.prototype.$type])},true);$c("HorizontalRangeCategorySeries:g","RangeCategorySeries",{init:function(){$$t.$h.init.call(this)},xAxis:function(a){if(arguments.length===1){this.h($$t.$g.xAxisProperty,a);return a}else{return this.c($$t.$g.xAxisProperty)}},yAxis:function(a){if(arguments.length===1){this.h($$t.$g.yAxisProperty,a);return a}else{return this.c($$t.$g.yAxisProperty)}},mw:function(){return this.xAxis()},mx:function(){return this.yAxis()},hn:function(){$$t.$h.hn.call(this);this.xAxis(null);this.yAxis(null)},ah:function(a){var b=this.aj(this.my(),this.mz.f,this.nz(this.view()),this.n0(this.view()),this.toWorldPosition(a),true);if(b==null){return null}var c=b[0];var d=b[1];var e=new Array(1);var f=new Array(2);f[0]={__x:c[0],__y:c[2],$type:$$a.$y.$type,getType:$.ig.$op.getType,getGetHashCode:$.ig.$op.getGetHashCode,typeName:$.ig.$op.typeName};f[1]={__x:d[0],__y:d[2],$type:$$a.$y.$type,getType:$.ig.$op.getType,getGetHashCode:$.ig.$op.getGetHashCode,typeName:$.ig.$op.typeName};e[0]=f;return e},ai:function(a){var b=this.aj(this.my(),this.mz.f,this.nz(this.view()),this.n0(this.view()),this.toWorldPosition(a),true);if(b==null){return null}var c=b[0];var d=b[1];var e=new Array(1);var f=new Array(2);f[0]={__x:c[0],__y:c[1],$type:$$a.$y.$type,getType:$.ig.$op.getType,getGetHashCode:$.ig.$op.getGetHashCode,typeName:$.ig.$op.typeName};f[1]={__x:d[0],__y:d[1],$type:$$a.$y.$type,getType:$.ig.$op.getType,getGetHashCode:$.ig.$op.getGetHashCode,typeName:$.ig.$op.typeName};e[0]=f;return e},isRange:function(){return true},bd:function(){if(this.hitTestMode()==0){return 1}else{return $$t.$h.bd.call(this)}},getOffsetValue:function(){return this._oo.r(this.xAxis(),this.view().bs(),this.view().br(),this.getEffectiveViewport1(this.view()))},getCategoryWidth:function(){return this.xAxis().getCategorySize(this.view().bs(),this.view().br(),this.getEffectiveViewport1(this.view()))},getNextOrExactIndex:function(a,b){return this.fa(a,b,this.xAxis(),this.nx.runOn(this),new $.ig.RangeValueList(this.highColumn(),this.lowColumn()))},getPreviousOrExactIndex:function(a,b){return this.fc(a,b,this.xAxis(),this.nx.runOn(this),new $.ig.RangeValueList(this.highColumn(),this.lowColumn()))},d6:function(a,b,c,d,e){if(c==null){return Number.POSITIVE_INFINITY}var f=this.xAxis().jr;return this.d7(a,b,this.xAxis(),d,e,f,this.nx.runOn(this))},getSeriesValue:function(a,b,c){if(this.seriesViewer()==null){return NaN}var d=this.getEffectiveViewport1(this.view());var e=new $.ig.ScalerParams(0,this.seriesViewer().actualWindowRect(),this.view().br(),this.xAxis().isInverted(),d);var f=this._oo.r(this.xAxis(),this.seriesViewer().actualWindowRect(),this.view().br(),d);return this.em(new $.ig.RangeValueList(this.highColumn(),this.lowColumn()),a,this.xAxis(),e,f,this.nx.runOn(this),b,c)},getSeriesLowValue:function(a,b,c){if(this.seriesViewer()==null){return NaN}var d=this.getEffectiveViewport1(this.view());var e=new $.ig.ScalerParams(0,this.seriesViewer().actualWindowRect(),this.view().br(),this.xAxis().isInverted(),d);var f=this._oo.r(this.xAxis(),this.seriesViewer().actualWindowRect(),this.view().br(),d);return this.em(this.lowColumn(),a,this.xAxis(),e,f,this.nx.runOn(this),b,c)},getSeriesHighValue:function(a,b,c){if(this.seriesViewer()==null){return NaN}var d=this.getEffectiveViewport1(this.view());var e=new $.ig.ScalerParams(0,this.seriesViewer().actualWindowRect(),this.view().br(),this.xAxis().isInverted(),d);var f=this._oo.r(this.xAxis(),this.seriesViewer().actualWindowRect(),this.view().br(),d);return this.em(this.highColumn(),a,this.xAxis(),e,f,this.nx.runOn(this),b,c)},getSeriesHighValuePosition:function(a,b,c){var $self=this;return this.lf(a,b,c,this._oo.r(this.xAxis(),this.view().bs(),this.view().br(),this.getEffectiveViewport1(this.view())),this.yAxis(),this.xAxis(),this.getSeriesHighValue.runOn(this),function(d,e){return $self.fc(d,e,$self.xAxis(),$self.nx.runOn($self),$self.highColumn())},function(d,e){return $self.fa(d,e,$self.xAxis(),$self.nx.runOn($self),$self.highColumn())})},getSeriesLowValuePosition:function(a,b,c){var $self=this;return this.lf(a,b,c,this._oo.r(this.xAxis(),this.view().bs(),this.view().br(),this.getEffectiveViewport1(this.view())),this.yAxis(),this.xAxis(),this.getSeriesLowValue.runOn(this),function(d,e){return $self.fc(d,e,$self.xAxis(),$self.nx.runOn($self),$self.lowColumn())},function(d,e){return $self.fa(d,e,$self.xAxis(),$self.nx.runOn($self),$self.lowColumn())})},getSeriesValuePosition:function(a,b,c){return this.lf(a,b,c,this._oo.r(this.xAxis(),this.view().bs(),this.view().br(),this.getEffectiveViewport1(this.view())),this.yAxis(),this.xAxis(),null,null,null)},nt:function(){return this.yAxis()!=null&&this.yAxis().updateRange()},is:function(a,b,c,d){$$t.$h.is.call(this,a,b,c,d);switch(b){case"XAxis":this.ht($b($.ig.Axis.prototype.$type,c));this.ix($b($.ig.Axis.prototype.$type,d));this._m7._cn.g(this.resolution());this.renderSeries(false);this.ic();break;case"YAxis":this.ht($b($.ig.Axis.prototype.$type,c));this.ix($b($.ig.Axis.prototype.$type,d));this._m7._cn.g(this.resolution());this.nt();this.renderSeries(false);this.ic();break}},canUseAsYAxis:function(a){if($b($.ig.NumericYAxis.prototype.$type,a)!==null){return true}return false},canUseAsXAxis:function(a){if($b($.ig.CategoryXAxis.prototype.$type,a)!==null||a.isDateTime()){return true}return false},$type:new $.ig.Type("HorizontalRangeCategorySeries",$$t.$h.$type)},true);$c("RangeAreaSeries:c","HorizontalRangeCategorySeries",{bf:function(){return new $$t.j(this)},im:function(a){$$t.$g.im.call(this,a);this._pb=a},_pb:null,isAreaOrLine:function(){return true},pc:function(a,b,c,d){if($.ig.util.isNaN(b.__x)||$.ig.util.isNaN(b.__y)){return false}if($.ig.util.isNaN(c.__x)||$.ig.util.isNaN(c.__y)){return false}if(a.__y<=b.__y&&a.__y>=c.__y){return true}return false},testHit:function(a,b){if(this.df(a,b)){return true}if(this.lw(a,b)){return true}return false},init:function(){$$t.$g.init.call(this);this._ab=$$t.$c.$type},preferredCategoryMode:function(a){return 0},hp:function(a,b){$$t.$g.hp.call(this,a,b);var c=b;c.cz()},og:function(a,b){$$t.$g.og.call(this,a,b);var c=a.f.count();var d=new $$4.x(Array,2,c);for(var e=0;e<c;e++){var f=a.f.__inner[e];var g=new Array(4);g[0]=f[0];g[1]=f[1];var h=a.f.__inner[a.f.count()-1-e];g[2]=h[0];g[3]=h[2];d.add(g)}var i=$b($$t.$j.$type,b);var j=this.getEffectiveViewport1(i);this.m6.x(this,this.nr(),this.xAxis(),this.getCategoryItems.runOn(this),this.nz(b),this.n0(b));var k=false;var l=this.m6._c;if(l!=null){k=true}if(k){var m=new $.ig.ScalerParams(0,b.bs(),b.br(),this.xAxis().isInverted(),j);this.oc(d,-1,this.lowColumn().count(),this.xAxis(),m,b.isThumbnailView())}var n=i.polyline0();var o=i.polyline1();var p=i.polygon01();this.m6.ae(n,true,false,true,true);this.m6.ae(o,true,false,true,true);this.m6.ae(p,false,true,false,false);if(b.checkFrameDirty(a)){i.c0(a.f.count(),d,false);b.updateFrameVersion(a)}i.polygon01().__opacity=this.m6.i*this.actualAreaFillOpacity()},$type:new $.ig.Type("RangeAreaSeries",$$t.$g.$type)},true);$c("HighLowValuesHolder:d","ValuesHolder",{init:function(){$.ig.ValuesHolder.prototype.init.call(this)},_f:null,_g:null,e:function(){if(this._f==null||this._g==null){return 0}return Math.min(this._f.count(),this._g.count())},$type:new $.ig.Type("HighLowValuesHolder",$.ig.ValuesHolder.prototype.$type)},true);$c("DefaultHighLowValueProvider:e","Object",{init:function(){$.ig.$op.init.call(this)},highColumn:function(){return new $$4.x(Number,0)},lowColumn:function(){return new $$4.x(Number,0)},$type:new $.ig.Type("DefaultHighLowValueProvider",$.ig.$ot,[$.ig.IHasHighLowValueCategory.prototype.$type])},true);$c("RangeCategoryFramePreparer:f","CategoryFramePreparerBase",{init:function(a,b){if(a>0){switch(a){case 1:this.init1.apply(this,arguments);break}return}$$t.$f.init1.call(this,1,b,$b($.ig.ISupportsMarkers.prototype.$type,b),$b($.ig.IProvidesViewport.prototype.$type,b),$b($.ig.ISupportsErrorBars.prototype.$type,b),$b($.ig.IBucketizer.prototype.$type,b))},init1:function(a,b,c,d,e,f){$.ig.CategoryFramePreparerBase.prototype.init1.call(this,1,b,c,d,e,f);this._aa=new $.ig.DefaultCategoryTrendlineHost;if($b($.ig.IHasCategoryTrendline.prototype.$type,b)!==null){this._aa=$b($.ig.IHasCategoryTrendline.prototype.$type,b)}this._ab=new $$t.e;if($b($.ig.IHasHighLowValueCategory.prototype.$type,b)!==null){this._ab=$b($.ig.IHasHighLowValueCategory.prototype.$type,b)}},_aa:null,_ab:null,l:function(a,b,c,d,e,f){var g=b[0];var h=b[1];var i=b[2];if(!$.ig.util.isNaN(g)&&!$.ig.util.isNaN(h)&&!$.ig.util.isNaN(i)){a.m.add({__x:g,__y:(h+i)/2,$type:$$a.$y.$type,getType:$.ig.$op.getType,getGetHashCode:$.ig.$op.getGetHashCode,typeName:$.ig.$op.typeName});this._c.updateMarkerTemplate(e,d,f);return true}return false},z:function(a,b,c,d){var e=a;var f=e._f;var g=e._g;var h=f.item(b);var i=g.item(b);var j=Math.max(h,i);var k=Math.min(h,i);if(c){e._c=k;e._d=j}else{e._b=j;e._a=k}},u:function(a,b,c){var d=a;var e=d._f;var f=d._g;var g=e.item(b);var h=f.item(b);if(!$.ig.util.isNaN(g)){d._b=Math.max(d._b,g);d._a=Math.min(d._a,g)}if(!$.ig.util.isNaN(h)){d._b=Math.max(d._b,h);d._a=Math.min(d._a,h)}},e:function(a,b){var c=b;return[a,c._a,c._b]},x:function(a,b,c,d,e,f){if(d){b[0]=b[0]+c}else{b[0]=a._b.getScaledValue(b[0],e)+c}b[1]=a._c.getScaledValue(b[1],f);b[2]=a._c.getScaledValue(b[2],f)},y:function(a,b,c,d,e,f,g){$.ig.CategoryFramePreparerBase.prototype.y.call(this,a,b,c,d,e,f,g);var h=a._h;var i=a._a.f.count();var j=a._a.f;var k=this.q();var l=k.count();var m=new $$4.x($$0.$aw.$type,0);if(!c){m.add(0)}var n=new $$4.x($$0.$aw.$type,0);n.add(1);n.add(2);a._b.getScaledBucketValueList(j,m,0,i,d);a._c.getScaledBucketValueList(j,n,0,i,e);for(var o=0;o<i;o++){j.__inner[o][0]=j.__inner[o][0]+b}if(g){a._b.getScaledBucketValueList(k,m,0,l,d);a._c.getScaledBucketValueList(k,n,0,l,e);for(var p=0;p<l;p++){k.__inner[p][0]=k.__inner[p][0]+b}}},j:function(a){var b=new $$t.d;b._f=this._ab.highColumn();b._g=this._ab.lowColumn();return b},$type:new $.ig.Type("RangeCategoryFramePreparer",$.ig.CategoryFramePreparerBase.prototype.$type)},true);$c("RangeColumnSeries:i","HorizontalRangeCategorySeries",{bf:function(){return new $$t.l(this)},im:function(a){$$t.$g.im.call(this,a);this._pb=a},_pb:null,init:function(){$$t.$g.init.call(this);this._ab=$$t.$i.$type},radiusX:function(a){if(arguments.length===1){this.h($$t.$i.radiusXProperty,a);return a}else{return this.c($$t.$i.radiusXProperty)}},radiusY:function(a){if(arguments.length===1){this.h($$t.$i.radiusYProperty,a);return a}else{return this.c($$t.$i.radiusYProperty)}},nn:function(){return true},preferredCategoryMode:function(a){return 2},getSeriesValueBoundingBox:function(a){if(this.xAxis()==null||this.yAxis()==null){return $$a.$ae.empty()}var b=this.fromWorldPosition(a);var c=this.view().bs();var d=this.view().br();var e=this.getEffectiveViewport1(this.view());var f=new $.ig.ScalerParams(0,c,d,this.yAxis().isInverted(),e);f._b=this.ee();var g=this.aj(this.my(),this.mz.f,this.nz(this.view()),this.n0(this.view()),a,true);if(g==null){return $$a.$ae.empty()}var h=g[0];var i=g[1];var j=Math.abs(b.__x-h[0]);var k=Math.abs(b.__x-i[0]);var l=this.xAxis().jk(c,d,e);if(j<k){var m=h[0]-.5*l;var n=h[1];var o=h[2];return new $$a.ae(0,m,Math.min(n,o),l,Math.max(n,o)-Math.min(n,o))}else{var p=i[0]-.5*l;var q=i[1];var r=i[2];return new $$a.ae(0,p,Math.min(q,r),l,Math.max(q,r)-Math.min(q,r))}},testHit:function(a,b){if(this.dj(a,b)){return true}if(this.lw(a,b)){return true}return false},hp:function(a,b){$$t.$g.hp.call(this,a,b);var c=b;if(a&&c._cz!=null){c._cz.count(0)}},getItemSpan:function(){return this.xAxis().jk(this.view().bs(),this.view().br(),this.getEffectiveViewport1(this.view()))},og:function(a,b){$$t.$g.og.call(this,a,b);var c=$b($$t.$l.$type,b);var d=a.f;var e=b.bs();var f=b.br();var g=this.getEffectiveViewport1(b);var h=this.xAxis().jk(e,f,g);if($.ig.util.isNaN(h)||Number.isInfinity(h)){c._cz.count(0);return}this.m6.x(this,this.nr(),this.xAxis(),this.getCategoryItems.runOn(this),this.nz(b),this.n0(b));this.m6.p=this.radiusX();this.m6.q=this.radiusY();this.m6.j=this.radiusX();this.m6.k=this.radiusY();var i=false;var j=this.m6._c;if(j!=null){i=true}var k=this.xAxis().isSorting();var l=this.lowColumn().count();var m=this.xAxis();var n=new $.ig.ScalerParams(0,e,f,this.xAxis().isInverted(),g);var o=0;for(var p=0;p<d.count();++p){var q=d.__inner[p][0]-.5*h;var r=Math.min(d.__inner[p][1],d.__inner[p][2]);var s=Math.max(d.__inner[p][1],d.__inner[p][2]);var t=Math.abs(s-r);if(Number.isInfinity(t)){continue}var u=c._cz.item(o);o++;u.width(h);u.height(t);if(i){this.oc(d,p,l,m,n,b.isThumbnailView())}this.m6.ae(u,false,false,false,false);u.al(this.m6.j);u.am(this.m6.k);c.c4(u,q,r)}c._cz.count(o);b.updateFrameVersion(a)},$type:new $.ig.Type("RangeColumnSeries",$$t.$g.$type)},true);$c("RangeCategorySeriesView:k","CategorySeriesView",{_cw:null,init:function(a){$.ig.CategorySeriesView.prototype.init.call(this,a);this._cw=a},cx:function(a,b,c,d,e,f){a._aj.clear();b._aj.clear();c._aj.clear();var g=$.ig.FastFlattener.prototype.b(d,e,true,f,this._e.resolution());var h=$.ig.FastFlattener.prototype.b(d,e,false,f,this._e.resolution());var i=g.count();var j=h.count();var k;var l;var m;var n;for(var o=0;o<i;o++){k=g.item(o);l=e.__inner[k];m=l[0];n=l[1];a._aj.add({__x:m,__y:n,$type:$$a.$y.$type,getType:$.ig.$op.getType,getGetHashCode:$.ig.$op.getGetHashCode,typeName:$.ig.$op.typeName});b._aj.add({__x:m,__y:n,$type:$$a.$y.$type,getType:$.ig.$op.getType,getGetHashCode:$.ig.$op.getGetHashCode,typeName:$.ig.$op.typeName})}var p;var q;for(var r=0;r<j;r++){k=h.item(r);l=e.__inner[k];if(f){p=l[0];q=l[2]}else{p=l[2];q=l[3]}c._aj.add({__x:p,__y:q,$type:$$a.$y.$type,getType:$.ig.$op.getType,getGetHashCode:$.ig.$op.getGetHashCode,typeName:$.ig.$op.typeName});b._aj.add({__x:p,__y:q,$type:$$a.$y.$type,getType:$.ig.$op.getType,getGetHashCode:$.ig.$op.getGetHashCode,typeName:$.ig.$op.typeName})}a._ab=a._aj.count()>0;b._ab=b._aj.count()>0;c._ab=c._aj.count()>0},co:function(){return new $$t.a(this)},$type:new $.ig.Type("RangeCategorySeriesView",$.ig.CategorySeriesView.prototype.$type)},true);$c("RangeAreaSeriesView:j","RangeCategorySeriesView",{_cy:null,init:function(a){this.c6=new $$a.bu;this.c2=new $$a.bt;this.c8=new $$a.bu;this.c5=new $$a.bu;this.c4=new $$a.bu;this.c1=new $$a.bt;$$t.$k.init.call(this,a);this._cy=a},onInit:function(){$$t.$k.onInit.call(this);if(!this.isThumbnailView()){this._e._bj=1}},c6:null,c2:null,c8:null,polyline0:function(){return this.c6},polygon01:function(){return this.c2},polyline1:function(){return this.c8},cz:function(){this.c2._aj.clear();this.c6._aj.clear();this.c8._aj.clear()},c0:function(a,b,c){this.cx(this.c6,this.c2,this.c8,a,b,c);this.an()},c5:null,c4:null,c1:null,a9:function(){$$t.$k.a9.call(this);this.c4._aj=this.c6._aj;this.c5._aj=this.c8._aj;this.c1._aj=this.c2._aj;var a=this.bl();this.c4.__stroke=a;this.c4._ac=this._e.thickness()+3;this.c5.__stroke=a;this.c5._ac=this._e.thickness()+3;this.c1.__fill=a;this.c1.__opacity=1},a3:function(a,b){$$t.$k.a3.call(this,a,b);if(a.d()){if(b){a.t(this.c1);a.u(this.c4);a.u(this.c5)}else{a.t(this.c2);a.u(this.c6);a.u(this.c8)}}},ai:function(a){$$t.$k.ai.call(this,a);this._e.a2().exportPolylineData(a,this.c6,"lowerShape",["Lower"]);this._e.a2().exportPolylineData(a,this.c8,"upperShape",["Upper","Main"]);this._e.a2().exportPolygonData(a,this.c2,"fillShape",["Fill"])},$type:new $.ig.Type("RangeAreaSeriesView",$$t.$k.$type)},true);$c("RangeColumnSeriesView:l","RangeCategorySeriesView",{_cy:null,init:function(a){var $self=this;this.c5=new $$a.bv;$$t.$k.init.call(this,a);this._cy=a;this._cz=function(){var $ret=new $.ig.Pool$1($$a.$bv.$type);$ret.create($self.c6.runOn($self));$ret.activate($self.c1.runOn($self));$ret.disactivate($self.c3.runOn($self));$ret.destroy($self.c2.runOn($self));return $ret}()},_cz:null,onInit:function(){$$t.$k.onInit.call(this);this._c0=new $$4.x($$a.$bv.$type,0);if(!this.isThumbnailView()){this._e.resolution(4);this._e._bj=1}},_c0:null,c6:function(){var a=new $$a.bv;this._c0.add(a);a.__visibility=1;return a},c1:function(a){a.__visibility=0},c3:function(a){a.__visibility=1},c2:function(a){this._c0.remove(a)},c4:function(a,b,c){if(!this.isDirty()){this.an()}a._o=c;a._n=b},z:function(a){return this._c0.__inner[a]},c5:null,y:function(a){var b=this._c0.__inner[a];this.c5.__visibility=b.__visibility;this.c5._n=b._n;this.c5._o=b._o;this.c5.width(b.width());this.c5.height(b.height());var c=this.bm(a);this.c5.__fill=c;this.c5.__stroke=c;this.c5._ac=this._e.thickness()+3;return this.c5},a3:function(a,b){$$t.$k.a3.call(this,a,b);if(a.d()){for(var c=0;c<this._c0.count();c++){var d=this.x(c,b);this.ba(d,c,b);a.v(d)}}},ai:function(a){$$t.$k.ai.call(this,a);var b=0;var c=new $$4.x($$a.$bv.$type,0);var e=this._cz.active().getEnumerator();while(e.moveNext()){var d=e.current();c.add(d)}c.sort2(function(f,g){if(f._n<g._n){return-1}else if(f._n>g._n){return 1}else{return 0}});var g=c.getEnumerator();while(g.moveNext()){var f=g.current();this._e.a2().exportRectangleData(a,f,"column"+b,["Main","Fill"])}b++},$type:new $.ig.Type("RangeColumnSeriesView",$$t.$k.$type)},true);$$t.$h.lowMemberPathProperty=$$a.$s.i("LowMemberPath",String,$$t.$h.$type,new $$a.ac(2,null,function(a,b){a.raisePropertyChanged("LowMemberPath",b.oldValue(),b.newValue())}));$$t.$h.highMemberPathProperty=$$a.$s.i("HighMemberPath",String,$$t.$h.$type,new $$a.ac(2,null,function(a,b){a.raisePropertyChanged("HighMemberPath",b.oldValue(),b.newValue())}));$$t.$g.xAxisProperty=$$a.$s.i("XAxis",$.ig.CategoryAxisBase.prototype.$type,$$t.$g.$type,new $$a.ac(2,null,function(a,b){a.raisePropertyChanged("XAxis",b.oldValue(),b.newValue())}));$$t.$g.yAxisProperty=$$a.$s.i("YAxis",$.ig.NumericYAxis.prototype.$type,$$t.$g.$type,new $$a.ac(2,null,function(a,b){a.raisePropertyChanged("YAxis",b.oldValue(),b.newValue())}));$$t.$i.radiusXProperty=$$a.$s.i("RadiusX",Number,$$t.$i.$type,new $$a.ac(2,2,function(a,b){a.raisePropertyChanged("RadiusX",b.oldValue(),b.newValue())}));$$t.$i.radiusYProperty=$$a.$s.i("RadiusY",Number,$$t.$i.$type,new $$a.ac(2,2,function(a,b){a.raisePropertyChanged("RadiusY",b.oldValue(),b.newValue())}))});