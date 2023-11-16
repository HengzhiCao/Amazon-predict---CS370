package com.cs370.group4.goodspredict;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataPreprocessing_rating {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\cao10\\Downloads\\CS370\\amazon_product.xlsx"; // 替换为你的文件路径

        try (InputStream in = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(in);
            Sheet sheet = workbook.getSheetAt(0); // 获取工作簿中的第一个工作表

            // 遍历每一行
            for (Row row : sheet) {
                // 从第二行开始读取数据，假设第一行是标题
                if (row.getRowNum() == 0) {
                    continue; // 跳过标题行
                }

                // 获取单元格中的值
                double discountedPrice = getNumericCellValue(row.getCell(12));
                double actualPrice = getNumericCellValue(row.getCell(13));
                double discountPercentage = getNumericCellValue(row.getCell(14));
                double rating = getNumericCellValue(row.getCell(15));
                double ratingCount = getNumericCellValue(row.getCell(16));

                // 计算评分
                double score = calculateScore(discountedPrice, actualPrice, discountPercentage, ratingCount,rating);

                // 在第17栏写入评分
                Cell scoreCell = row.createCell(17); // 在POI中索引从0开始
                scoreCell.setCellValue(score);
            }

            // 将更改写回文件
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double getNumericCellValue(Cell cell) {
        if (cell != null && cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else {
            return 0.0; // 如果单元格为空或不是数字类型，返回0.0
        }
    }

    private static double calculateScore(double discountedPrice, double actualPrice, double discountPercentage, double ratingCount, double rating) {

        // 处理丢失的discountedPrice
        if (Double.isNaN(discountedPrice) || discountedPrice <= 0) {
            if (Double.isNaN(discountPercentage) || discountPercentage <= 0) {
                discountedPrice = actualPrice; // 如果折扣百分比也丢失了，则使用actualPrice
            } else {
                discountedPrice = actualPrice * (1 - discountPercentage / 100); // 用actualPrice和discountPercentage来估算discountedPrice
            }
        }

        // 处理丢失的ratingCount
        if (Double.isNaN(ratingCount) || ratingCount <= 0) {
            ratingCount = 0;
        }

        // 处理丢失的rating
        if (Double.isNaN(rating) || rating <= 0) {
            rating = defaultRatingValue(); // 使用一个函数来获取默认评分值
        }


        // Make sure that the weights sum up to 1 (or 100%)
        double weightRatingCount = 0.3; // For example, give 30% weight to ratingCount
        double weightDiscount = 0.2;    // 20% weight to discountPercentage
        double weightPrice = 0.2;       // 20% weight to price
        double weightRating = 0.3;      // 30% weight to rating

        double score = (weightRatingCount * normalizeRatingCount(ratingCount)) +
                (weightDiscount * normalizeDiscount(discountPercentage)) +
                (weightPrice * normalizePrice(discountedPrice, actualPrice)) +
                (weightRating * normalizeRating(rating));



        // Clamp the score between 0 and 10
        score = Math.max(score, 0);
        score = Math.min(score, 10);

        if(score >5){
            score =1;
        }else {
            score =0;
        }

        return score;
    }

    private static double defaultRatingValue() {
        return 1.0;
    }

    private static double normalizeRating(double rating) {
        return (rating / 5) * 10;
    }


    private static double normalizeRatingCount(double ratingCount) {
        // 假设评价数量最多的商品有10000条评价
        return (ratingCount / 10000) * 10;
    }

    private static double normalizeDiscount(double discountPercentage) {
        // 假设折扣力度最多为100%
        return (discountPercentage / 100) * 10;
    }

    private static double normalizePrice(double discountedPrice, double actualPrice) {
        // 假设商品的正常价格范围是10到1000
        double normalizedPrice = (actualPrice - 10) / (100000 - 10);
        double priceScore = 10 - (normalizedPrice * 10); // 价格越低得分越高
        return priceScore;
    }
}
