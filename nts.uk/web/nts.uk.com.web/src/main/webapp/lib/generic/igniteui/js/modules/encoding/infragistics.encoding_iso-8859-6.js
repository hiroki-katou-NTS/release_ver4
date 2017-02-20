﻿/*!@license
* Infragistics.Web.ClientUI infragistics.encoding_iso-8859-6.js 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
* Depends:
*     jquery-1.4.4.js
*     jquery.ui.core.js
*     jquery.ui.widget.js
*     infragistics.util.js
*/
(function(factory){if(typeof define==="function"&&define.amd){define(["jquery","jquery-ui","./infragistics.util","./infragistics.dv.simple.core"],factory)}else{factory(jQuery)}})(function($){(function($){$.ig=$.ig||{};var $$t={};$.ig.globalDefs=$.ig.globalDefs||{};$.ig.globalDefs.$$3=$$t;$.ig.$currDefinitions=$$t;$.ig.util.bulkDefine(["Object:b","Type:c","Boolean:d","ValueType:e","Void:f","IConvertible:g","IFormatProvider:h","Number:i","String:j","IComparable:k","Number:l","IComparable$1:m","IEquatable$1:n","Number:o","Number:p","Number:q","NumberStyles:r","Enum:s","Array:t","IList:u","ICollection:v","IEnumerable:w","IEnumerator:x","NotSupportedException:y","Error:z","Number:aa","String:ab","StringComparison:ac","RegExp:ad","CultureInfo:ae","DateTimeFormatInfo:af","Calendar:ag","Date:ah","Number:ai","DayOfWeek:aj","DateTimeKind:ak","CalendarWeekRule:al","NumberFormatInfo:am","CompareInfo:an","CompareOptions:ao","IEnumerable$1:ap","IEnumerator$1:aq","IDisposable:ar","StringSplitOptions:as","Number:at","Number:au","Number:av","Number:aw","Number:ax","Number:ay","Assembly:az","Stream:a0","SeekOrigin:a1","RuntimeTypeHandle:a2","MethodInfo:a3","MethodBase:a4","MemberInfo:a5","ParameterInfo:a6","TypeCode:a7","ConstructorInfo:a8","PropertyInfo:a9","MulticastDelegate:bu","IntPtr:bv","Array:ce","AbstractEnumerable:ic","Func$1:id","AbstractEnumerator:ie","GenericEnumerable$1:ih","GenericEnumerator$1:ii"]);var $a=$.ig.intDivide,$b=$.ig.util.cast,$c=$.ig.util.defType,$d=$.ig.util.getBoxIfEnum,$e=$.ig.util.getDefaultValue,$f=$.ig.util.getEnumValue,$g=$.ig.util.getValue,$h=$.ig.util.intSToU,$i=$.ig.util.nullableEquals,$j=$.ig.util.nullableIsNull,$k=$.ig.util.nullableNotEquals,$l=$.ig.util.toNullable,$m=$.ig.util.toString$1,$n=$.ig.util.u32BitwiseAnd,$o=$.ig.util.u32BitwiseOr,$p=$.ig.util.u32BitwiseXor,$q=$.ig.util.u32LS,$r=$.ig.util.unwrapNullable,$s=$.ig.util.wrapNullable,$t=String.fromCharCode,$u=$.ig.util.castObjTo$t,$v=$.ig.util.compare,$w=$.ig.util.replace,$x=$.ig.util.stringFormat,$y=$.ig.util.stringFormat1,$z=$.ig.util.stringFormat2,$0=$.ig.util.stringCompare1,$1=$.ig.util.stringCompare2,$2=$.ig.util.stringCompare3,$3=$.ig.util.compareSimple,$4=$.ig.util.tryParseNumber,$5=$.ig.util.tryParseNumber1,$6=$.ig.util.numberToString,$7=$.ig.util.numberToString1,$8=$.ig.util.parseNumber,$9=$.ig.util.isDigit,$aa=$.ig.util.isDigit1,$ab=$.ig.util.isLetter,$ac=$.ig.util.isNumber,$ad=$.ig.util.isLetterOrDigit,$ae=$.ig.util.isLower,$af=$.ig.util.toLowerCase,$ag=$.ig.util.toUpperCase,$ah=$.ig.util.equalsSimple,$ai=$.ig.util.tryParseInt32_1,$aj=$.ig.util.tryParseInt32_2,$ak=$.ig.util.intToString1,$al=$.ig.util.parseInt32_1,$am=$.ig.util.parseInt32_2})(jQuery);(function($){$.ig=$.ig||{};var $$t={};$.ig.globalDefs=$.ig.globalDefs||{};$.ig.globalDefs.$$a3=$$t;$.ig.$currDefinitions=$$t;$.ig.util.bulkDefine(["Encoding:b","Object:c","Type:d","Boolean:e","ValueType:f","Void:g","IConvertible:h","IFormatProvider:i","Number:j","String:k","IComparable:l","Number:m","IComparable$1:n","IEquatable$1:o","Number:p","Number:q","Number:r","NumberStyles:s","Enum:t","Array:u","IList:v","ICollection:w","IEnumerable:x","IEnumerator:y","NotSupportedException:z","Error:aa","Number:ab","String:ac","StringComparison:ad","RegExp:ae","CultureInfo:af","DateTimeFormatInfo:ag","Calendar:ah","Date:ai","Number:aj","DayOfWeek:ak","DateTimeKind:al","CalendarWeekRule:am","NumberFormatInfo:an","CompareInfo:ao","CompareOptions:ap","IEnumerable$1:aq","IEnumerator$1:ar","IDisposable:as","StringSplitOptions:at","Number:au","Number:av","Number:aw","Number:ax","Number:ay","Number:az","Assembly:a0","Stream:a1","SeekOrigin:a2","RuntimeTypeHandle:a3","MethodInfo:a4","MethodBase:a5","MemberInfo:a6","ParameterInfo:a7","TypeCode:a8","ConstructorInfo:a9","PropertyInfo:ba","UTF8Encoding:bb","InvalidOperationException:bc","NotImplementedException:bd","Script:be","Decoder:bf","UnicodeEncoding:bg","Math:bh","AsciiEncoding:bi","ArgumentNullException:bj","DefaultDecoder:bk","ArgumentException:bl","IEncoding:bm","Dictionary$2:bn","IDictionary$2:bo","ICollection$1:bp","IDictionary:bq","IEqualityComparer$1:br","EqualityComparer$1:bs","IEqualityComparer:bt","DefaultEqualityComparer$1:bu","KeyValuePair$2:bv","Thread:bw","ThreadStart:bx","MulticastDelegate:by","IntPtr:bz","StringBuilder:b0","Environment:b1","SingleByteEncoding:b2","RuntimeHelpers:b5","RuntimeFieldHandle:b6","Array:cd","Iso8859Dash6:cw","AbstractEnumerable:df","Func$1:dg","AbstractEnumerator:dh","GenericEnumerable$1:di","GenericEnumerator$1:dj"]);var $a=$.ig.intDivide,$b=$.ig.util.cast,$c=$.ig.util.defType,$d=$.ig.util.getBoxIfEnum,$e=$.ig.util.getDefaultValue,$f=$.ig.util.getEnumValue,$g=$.ig.util.getValue,$h=$.ig.util.intSToU,$i=$.ig.util.nullableEquals,$j=$.ig.util.nullableIsNull,$k=$.ig.util.nullableNotEquals,$l=$.ig.util.toNullable,$m=$.ig.util.toString$1,$n=$.ig.util.u32BitwiseAnd,$o=$.ig.util.u32BitwiseOr,$p=$.ig.util.u32BitwiseXor,$q=$.ig.util.u32LS,$r=$.ig.util.unwrapNullable,$s=$.ig.util.wrapNullable,$t=String.fromCharCode,$u=$.ig.util.castObjTo$t;$c("SingleByteEncoding:b2","Encoding",{ae:null,ab:null,af:0,ag:null,ac:function(){},init:function(a,b){if(a>0){switch(a){case 1:this.init1.apply(this,arguments);break}return}$$t.$b.init.call(this);this.ah(b)},init1:function(a,b,c){$$t.$b.init.call(this);this.ah(b);this.ag=c},ah:function(a){this.af=a;this.ab=this.ac();if(this.ab==null){return}this.ae=new $$t.bn($$t.$ac.$type,$.ig.Number.prototype.$type,0);for(var b=0;b<this.ab.length;b++){var c=this.ab[b];if(c!="￿"){this.ae.add(c,b)}}},fallbackCharacter:function(){return"?"},codePage:function(){return this.af},name:function(){return this.ag},getByteCount:function(a,b,c){return c},getBytes2:function(a,b,c,d,e){for(var f=b;f<b+c;f++){if(this.ae.containsKey(a[f])){d[e+f-b]=this.ae.item(a[f])}else{d[e+f-b]=this.getBytes1(this.fallbackCharacter().toString())[0]}}return c},getString1:function(a,b,c){var d=this.ab;var e=new $$t.b0(0);for(var f=b;f<b+c;f++){if(d[a[f]]!="￿"){e.h(d[a[f]])}}return e.toString()},$type:new $.ig.Type("SingleByteEncoding",$$t.$b.$type,[$$t.$bm.$type])},true);$c("Iso8859Dash6:cw","SingleByteEncoding",{ai:null,ac:function(){return this.ai},init:function(){this.ai=["\0","","","","","","","","\b","	","\n","","\f","\r","","","","","","","","","","","","","","","","","",""," ","!",'"',"#","$","%","&","'","(",")","*","+",",","-",".","/","0","1","2","3","4","5","6","7","8","9",":",";","<","=",">","?","@","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","[","\\","]","^","_","`","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","{","|","}","~","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""," ","","","","¤","","","","","","","","،","­","","","","","","","","","","","","","","؛","","","","؟","","ء","آ","أ","ؤ","إ","ئ","ا","ب","ة","ت","ث","ج","ح","خ","د","ذ","ر","ز","س","ش","ص","ض","ط","ظ","ع","غ","","","","","","ـ","ف","ق","ك","ل","م","ن","ه","و","ى","ي","ً","ٌ","ٍ","َ","ُ","ِ","ّ","ْ","","","","","","","","","","","","",""];$$t.$b2.init1.call(this,1,28596,"iso-8859-6")},$type:new $.ig.Type("Iso8859Dash6",$$t.$b2.$type)},true);$$t.$b2.ad="?"})(jQuery)});