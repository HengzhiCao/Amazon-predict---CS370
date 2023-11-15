package com.cs370.group4.goodspredict;

import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToNominal;
import weka.filters.unsupervised.attribute.NominalToBinary;

import java.io.File;

public class WekaCSVStringToNumeric {
    public static void main(String[] args) throws Exception {
        // 加载 CSV 数据
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File("C:\\Users\\cao10\\Downloads\\CS370\\amazon_product_only_product_ID.csv"));
        Instances data = loader.getDataSet();

        // 将字符串属性转换为名义属性
        StringToNominal stringToNominal = new StringToNominal();
        stringToNominal.setAttributeRange("2"); // 指定要转换的属性
        stringToNominal.setInputFormat(data);
        Instances nominalData = Filter.useFilter(data, stringToNominal);

        // 将名义属性转换为二进制数值属性
        NominalToBinary nominalToBinary = new NominalToBinary();
        nominalToBinary.setInputFormat(nominalData);
        Instances numericData = Filter.useFilter(nominalData, nominalToBinary);

        // numericData 现在包含转换后的数值类型数据
    }
}
