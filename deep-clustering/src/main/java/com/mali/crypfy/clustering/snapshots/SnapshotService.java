package com.mali.crypfy.clustering.snapshots;

import com.mali.crypfy.clustering.integrations.mali.coin.data.exception.CoinException;
import com.mali.crypfy.clustering.integrations.mali.coin.data.json.CoinHistoryJson;
import com.mali.crypfy.clustering.snapshots.entity.SnapshotData;
import java.awt.image.BufferedImage;
import java.util.List;

public interface SnapshotService {

    public List<SnapshotData> getClusterAndImgData(int shapeArraySize, int maximumAnalizedCoins,int timeFrame,int discountedCoins) throws CoinException;
    public void generateAndSaveSimplifiedSnapshotImage(double[] shape, String imageName, int label);
    public double[] generateShapeData(SnapshotData data);
    public double[] getShape(List<CoinHistoryJson> history);
    public double[] getvShape(List<CoinHistoryJson> history);

    //image utilities
    public void makeItWhite(BufferedImage img, int width, int height);
    public int getColor(String colorName);
}
