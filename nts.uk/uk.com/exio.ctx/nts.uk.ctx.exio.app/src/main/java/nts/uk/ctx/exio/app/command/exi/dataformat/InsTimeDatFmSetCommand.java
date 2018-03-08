package nts.uk.ctx.exio.app.command.exi.dataformat;

import lombok.Value;

@Value
public class InsTimeDatFmSetCommand
{
    
    /**
    * 区切り文字設定
    */
    private int delimiterSet;
    
    /**
    * 固定値
    */
    private int fixedValue;
    
    /**
    * 時分/分選択
    */
    private int hourMinSelect;
    
    /**
    * 有効桁長
    */
    private int effectiveDigitLength;
    
    /**
    * 端数処理
    */
    private int roundProc;
    
    /**
    * 進数選択
    */
    private int decimalSelect;
    
    /**
    * 固定値の値
    */
    private String valueOfFixedValue;
    
    /**
    * 有効桁数開始桁
    */
    private int startDigit;
    
    /**
    * 有効桁数終了桁
    */
    private int endDigit;
    
    /**
    * 端数処理区分
    */
    private int roundProcCls;

}
