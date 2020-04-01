<template>
<div class="cmms45componentsapp1">
    <div class="row" v-if="$app"> 
      <div class="col-12">
        <div class="row mt-1 pl-2 pt-1 pb-1 uk-bg-headline border-top uk-border-light-gray">{{'CMMS45_21' | i18n}}</div>
        <div class="row pl-2 pt-1 pb-1 border-top uk-border-light-gray text-break">
            <span>{{ $app.applicant | i18n }}</span> 
            <span v-if="$app.representer" class="uk-text-dark-gray child-font-size">{{ 'CMMS45_22' | i18n($app.representer) }}</span></div>
        <div class="row pl-2 pt-1 pb-1 uk-bg-headline border-top uk-border-light-gray">{{'CMMS45_23' | i18n}}</div>
        <div class="row pl-2 pt-1 pb-1 border-top uk-border-light-gray">
            <div class="col-12">
                <div class="row">{{ $app.appDate | i18n }} {{ appType }} {{'CMMS45_24' | i18n(prePost)}}</div>
                <div class="row uk-text-dark-gray child-font-size">{{'CMMS45_25' | i18n($app.inputDate)}}</div>
            </div>
        </div>
        <div class="row pl-2 pt-1 pb-1 uk-bg-headline border-top uk-border-light-gray" v-if="$app.displayCaculationTime">{{'CMMS45_26' | i18n}}</div>
        <div class="row pl-2 pt-1 pb-1 border-top uk-border-light-gray" v-if="$app.displayCaculationTime">
            <div class="col-12">
                <div class="row uk-text-dark-gray child-font-size">{{'CMMS45_81' | i18n}}</div>
                <div class="row ml-n5 pl-3" v-if="$app.workTypeCD">
                    <div class="col-1 mr-2">{{ $app.workTypeCD | i18n }}</div>
                    <div class="col-10">{{ ($app.workTypeName || 'CMMS45_87') | i18n }}</div>
                </div>
                <div class="row ml-n5 pl-3" v-if="!$app.workTypeCD">
                    <div class="col">{{ 'CMMS45_15' | i18n }}</div>
                </div>
                <div class="row uk-text-dark-gray child-font-size">{{'CMMS45_82' | i18n}}</div>
                <div class="row ml-n5 pl-3" v-if="$app.workTimeCD">
                    <div class="col-1 mr-2">{{ $app.workTimeCD | i18n }}</div>
                    <div class="col-10">{{ ($app.workTimeName || 'CMMS45_87') | i18n }}</div>
                </div>
                <div class="row ml-n5 pl-3" v-if="!$app.workTimeCD">
                    <div class="col">{{ 'CMMS45_15' | i18n }}</div>
                </div>
            </div>
        </div>
        <div class="row pl-2 pt-1 pb-1 uk-bg-headline border-top uk-border-light-gray" v-if="$app.displayCaculationTime">{{'CMMS45_83' | i18n}}</div>
        <div class="row pl-2 pt-1 pb-1 border-top uk-border-light-gray" v-if="$app.displayCaculationTime">
            <div class="col-12">
                <div class="row">
                    <span v-if="$app.startTime!=null">{{ $app.startTime | timewd}} {{ 'CMMS45_12' | i18n}} {{ $app.endTime | timewd}}</span>  
                    <span v-else>{{'CMMS45_15' | i18n}}</span>
                </div>
            </div>
        </div>
        <div class="row pl-2 pt-1 pb-1 uk-bg-headline border-top uk-border-light-gray"
            v-if="$app.displayCaculationTime && $app.displayRestTime">{{'CMMS45_27' | i18n}}</div>
        <div class="row" v-if="$app.displayCaculationTime && $app.displayRestTime">
            <div class="col-12" v-if="breakTimeLst.length > 0">
                <div class="row pl-2 pt-1 pb-1 border-top uk-border-light-gray" v-for="(breakTime, breakTimeIndex) in breakTimeLst" v-bind:key="breakTimeIndex">
                    <div class="col-12">
                        <div class="row uk-text-dark-gray child-font-size">{{ 'CMMS45_85' | i18n(breakTime.frameNo.toString()) }}</div>
                        <div class="row">
                            <span>{{ breakTime.startTime | timewd}} {{ 'CMMS45_12' | i18n}} {{ breakTime.endTime | timewd}}</span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-12" v-else>
                <div class="row pl-2 pt-1 pb-1 border-top uk-border-light-gray">
                    <div class="col-12">
                        <div class="row uk-text-dark-gray child-font-size">{{ 'CMMS45_85' | i18n('1') }}</div>
                        <div class="row">{{'CMMS45_15' | i18n}}</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row pl-2 pt-1 pb-1 uk-bg-headline border-top uk-border-light-gray">{{'CMMS45_86' | i18n}}</div>
        <div class="row">
            <div class="col-12">
                <div class="row pl-2 pt-1 pb-1 border-top uk-border-light-gray" v-for="(overTime, overTimeIndex) in overTimeLst" 
                    v-bind:key="overTimeIndex" v-show="overTime.getDisplay">
                    <div class="col-12" v-if="overTime.frameNo <= 10">
                        <div class="row">
                            <div class="col-6 uk-text-dark-gray child-font-size">{{ overTime.frameName | i18n }}</div>
                            <div class="col-6">
                                <span v-if="overTime.appTime!=null">{{ overTime.appTime | timedr }}</span>
                                <span v-else>{{'CMMS45_15' | i18n}}</span>
                            </div>
                        </div>
                        <div class="row" v-if="isPostApp">
                            <div class="col-6 pl-4">
                                <span v-if="overTime.preAppTime!=null" class="far fa-clock display-time child-font-size" 
                                    v-bind:class="{ 'uk-text-excess-time-alarm': overTime.preAppError==2, 'uk-text-excess-time-none': overTime.preAppError!=2 }">
                                    {{'KAFS00_1' | i18n}}[{{ overTime.preAppTime | timedr }}]
                                </span>
                                <span v-else class="far fa-clock display-time child-font-size" 
                                    v-bind:class="{ 'uk-text-excess-time-alarm': overTime.preAppError==2, 'uk-text-excess-time-none': overTime.preAppError!=2 }">
                                    {{'KAFS00_1' | i18n}}[--:--]
                                </span>
                            </div>
                            <div class="col-6">
                                <span v-if="overTime.actualTime!=null" class="far fa-clock display-time child-font-size" 
                                    v-bind:class="{ 'uk-text-excess-time-alarm': overTime.actualError==3, 'uk-text-excess-time-error': overTime.actualError==4,
                                                    'uk-text-excess-time-none': overTime.actualError!=3 && overTime.actualError!=4 }">
                                    {{'KAFS00_2' | i18n}}[{{ overTime.actualTime | timedr }}]
                                </span>
                                <span v-else class="far fa-clock display-time child-font-size" 
                                    v-bind:class="{ 'uk-text-excess-time-alarm': overTime.actualError==3, 'uk-text-excess-time-error': overTime.actualError==4,
                                                    'uk-text-excess-time-none': overTime.actualError!=3 && overTime.actualError!=4 }">
                                    {{'KAFS00_2' | i18n}}[--:--]
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="col-12" v-if="overTime.frameNo == 11 && $app.appOvertimeNightFlg == 1">
                        <div class="row">
                            <div class="col-6 uk-text-dark-gray child-font-size">{{ 'CMMS45_30' | i18n }}</div>
                            <div class="col-6">
                                <span v-if="overTime.appTime!=null">{{ overTime.appTime | timedr }}</span>
                                <span v-else>{{'CMMS45_15' | i18n}}</span>
                            </div>
                        </div>
                        <div class="row" v-if="isPostApp">
                            <div class="col-6 pl-4">
                                <span v-if="overTime.preAppTime!=null" class="far fa-clock display-time child-font-size" 
                                    v-bind:class="{ 'uk-text-excess-time-alarm': overTime.preAppError==2, 'uk-text-excess-time-none': overTime.preAppError!=2 }">
                                    {{'KAFS00_1' | i18n}}[{{ overTime.preAppTime | timedr }}]
                                </span>
                                <span v-else class="far fa-clock display-time child-font-size" 
                                    v-bind:class="{ 'uk-text-excess-time-alarm': overTime.preAppError==2, 'uk-text-excess-time-none': overTime.preAppError!=2 }">
                                    {{'KAFS00_1' | i18n}}[--:--]
                                </span>
                            </div>
                            <div class="col-6">
                                <span v-if="overTime.actualTime!=null" class="far fa-clock display-time child-font-size" 
                                    v-bind:class="{ 'uk-text-excess-time-alarm': overTime.actualError==3, 'uk-text-excess-time-error': overTime.actualError==4,
                                                    'uk-text-excess-time-none': overTime.actualError!=3 && overTime.actualError!=4 }">
                                    {{'KAFS00_2' | i18n}}[{{ overTime.actualTime | timedr }}]
                                </span>
                                <span v-else class="far fa-clock display-time child-font-size" 
                                    v-bind:class="{ 'uk-text-excess-time-alarm': overTime.actualError==3, 'uk-text-excess-time-error': overTime.actualError==4,
                                                    'uk-text-excess-time-none': overTime.actualError!=3 && overTime.actualError!=4 }">
                                    {{'KAFS00_2' | i18n}}[--:--]
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="col-12" v-if="overTime.frameNo == 12 && $app.flexFLag">
                        <div class="row">
                            <div class="col-6 uk-text-dark-gray child-font-size">{{ 'CMMS45_31' | i18n }}</div>
                            <div class="col-6">
                                <span v-if="overTime.appTime!=null">{{ overTime.appTime | timedr }}</span>
                                <span v-else>{{'CMMS45_15' | i18n}}</span>
                            </div>
                        </div>
                        <div class="row" v-if="isPostApp">
                            <div class="col-6 pl-4">
                                <span v-if="overTime.preAppTime!=null" class="far fa-clock display-time child-font-size" 
                                    v-bind:class="{ 'uk-text-excess-time-alarm': overTime.preAppError==2, 'uk-text-excess-time-none': overTime.preAppError!=2 }">
                                    {{'KAFS00_1' | i18n}}[{{ overTime.preAppTime | timedr }}]
                                </span>
                                <span v-else class="far fa-clock display-time child-font-size" 
                                    v-bind:class="{ 'uk-text-excess-time-alarm': overTime.preAppError==2, 'uk-text-excess-time-none': overTime.preAppError!=2 }">
                                    {{'KAFS00_1' | i18n}}[--:--]
                                </span>
                            </div>
                            <div class="col-6">
                                <span v-if="overTime.actualTime!=null" class="far fa-clock display-time child-font-size" 
                                    v-bind:class="{ 'uk-text-excess-time-alarm': overTime.actualError==3, 'uk-text-excess-time-error': overTime.actualError==4,
                                                    'uk-text-excess-time-none': overTime.actualError!=3 && overTime.actualError!=4 }">
                                    {{'KAFS00_2' | i18n}}[{{ overTime.actualTime | timedr }}]
                                </span>
                                <span v-else class="far fa-clock display-time child-font-size" 
                                    v-bind:class="{ 'uk-text-excess-time-alarm': overTime.actualError==3, 'uk-text-excess-time-error': overTime.actualError==4,
                                                    'uk-text-excess-time-none': overTime.actualError!=3 && overTime.actualError!=4 }">
                                    {{'KAFS00_2' | i18n}}[--:--]
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row pl-2 pt-1 pb-1 uk-bg-headline border-top uk-border-light-gray" v-if="$app.displayBonusTime">{{'CMMS45_33' | i18n}}</div>
        <div class="row" v-if="$app.displayBonusTime">
            <div class="col-12">
                <div class="row pl-2 pt-1 pb-1 border-top uk-border-light-gray" v-for="(payTime, payTimeIndex) in payTimeLst" v-bind:key="payTimeIndex">
                    <div class="col-12">
                        <div class="row">
                            <div class="col-5 uk-text-dark-gray child-font-size">{{ payTime.frameName | i18n }}</div>
                            <div class="col-3 pl-0">
                                <span v-if="payTime.appTime!=null">{{ payTime.appTime | timedr }}</span>
                                <span v-else>{{'CMMS45_15' | i18n}}</span>
                            </div>
                            <div class="col-4 pl-1" v-if="isPostApp">
                                <span v-if="payTime.preAppTime!=null" class="far fa-clock display-time child-font-size" 
                                    v-bind:class="{ 'uk-text-excess-time-alarm': payTime.preAppError==2, 'uk-text-excess-time-none': payTime.preAppError!=2 }">
                                    {{'KAFS00_1' | i18n}}[{{ payTime.preAppTime | timedr }}]
                                </span>
                                <span v-else class="far fa-clock display-time child-font-size" 
                                    v-bind:class="{ 'uk-text-excess-time-alarm': payTime.preAppError==2, 'uk-text-excess-time-none': payTime.preAppError!=2 }">
                                    {{'KAFS00_1' | i18n}}[--:--]
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row pl-2 pt-1 pb-1 uk-bg-headline border-top uk-border-light-gray"
            v-if="$app.displayAppReasonContentFlg || $app.typicalReasonDisplayFlg">{{'CMMS45_34' | i18n}}</div>
        <div class="row pl-2 pt-1 pb-1 border-top uk-border-light-gray text-break"
            v-if="$app.displayAppReasonContentFlg || $app.typicalReasonDisplayFlg">{{ $app.appReason }}</div>
        <div class="row pl-1 pt-1 pb-1 uk-bg-headline border-top uk-border-light-gray"
            v-if="$app.prePostAtr==1 && $app.displayDivergenceReasonForm && $app.displayDivergenceReasonInput">
            {{'CMMS45_35' | i18n}}
        </div>
        <div class="row pl-2 pt-1 pb-1 border-top uk-border-light-gray text-break"
            v-if="$app.prePostAtr==1 && $app.displayDivergenceReasonForm && $app.displayDivergenceReasonInput">
            {{ $app.divergenceReasonContent }}
        </div>
      </div>
    </div>
</div>
</template>