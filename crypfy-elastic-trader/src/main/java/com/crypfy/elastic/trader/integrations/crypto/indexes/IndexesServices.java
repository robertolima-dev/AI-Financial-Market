package com.crypfy.elastic.trader.integrations.crypto.indexes;

import com.crypfy.elastic.trader.integrations.crypto.indexes.exceptions.CryptoIndexesException;
import com.crypfy.elastic.trader.integrations.crypto.indexes.json.Index;

public interface IndexesServices {

    public Index getLastUpdate(String source,String asset) throws CryptoIndexesException;

}
