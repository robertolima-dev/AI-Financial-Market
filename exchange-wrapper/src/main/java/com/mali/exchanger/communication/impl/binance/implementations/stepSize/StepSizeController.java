package com.mali.exchanger.communication.impl.binance.implementations.stepSize;

import com.binance.api.client.exception.BinanceApiException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Service
public class StepSizeController {

    public double correctQuantity(double quantity,double stepSize, double minimumSize){


        if (quantity>minimumSize) {
            int stepMult = (int) ( quantity / stepSize);
            double result = stepMult*stepSize;
            DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
            df.setMaximumFractionDigits(340);
            String stringStep = df.format(stepSize), stringResult = df.format(result);
            if (stringStep.contains(".")){
                int relevantDecimals = stringStep.substring(stringStep.indexOf("."),stringStep.length()-1).length();
                stringResult = stringResult.substring(0,stringResult.indexOf("."))+stringResult.substring(stringResult.indexOf("."),stringResult.indexOf(".")+relevantDecimals);
            }
            return Double.valueOf(stringResult);
        } else throw new BinanceApiException("Quantidade de negociação/preço fora do padrao de step!");
    }

}
