import java.util.HashMap;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import scala.Tuple2;

@Controller
public class MyRandomForestController {

    @RequestMapping("/runRandomForest")
    public static void runRandomForest() {
        SparkConf sparkConf = new SparkConf().setAppName("app").setMaster("local[1]");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        //load data
        String path = "D:\\IdeaProjects\\SparkMLlib\\src\\test\\java\\data4";
        JavaRDD<String> javaRDD = jsc.textFile(path);
        JavaRDD<LabeledPoint> data = javaRDD.map(new Function<String, LabeledPoint>() {
            @Override
            public LabeledPoint call(String line) throws Exception {
                String[] split = line.split(",");
                String[] arr = split[1].split(" ");
                double[] vectors = new double[arr.length];
                for (int i = 0; i < arr.length; i++) {
                    vectors[i] = Double.parseDouble(arr[i]);
                }
                LabeledPoint labeledPoint = new LabeledPoint(Double.parseDouble(split[0]), Vectors.dense(vectors));
                return labeledPoint;
            }
        });
        // 将数据集划分为训练数据和测试数据
        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.7, 0.3});//将数据分成7:3
        JavaRDD<LabeledPoint> training = splits[0];
        JavaRDD<LabeledPoint> testData = splits[1];
        // 随机森林模型训练
        Integer numClasses = 2;//划分的类型数量
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
        Integer numTrees = 1; // 树的数量
        String featureSubsetStrategy = "auto"; //算法自动选择 auto/all
        String impurity = "gini";//随机森林有三种方式，entropy，gini,variance,回归肯定就是variance
        Integer maxDepth = 10;//深度
        Integer maxBins = 32;//数据最大分端数
        Integer seed = 1000000;//采样种子，种子不变，采样结果不变
        //训练模型
        RandomForestModel model = RandomForest.trainClassifier(
                training,
                numClasses,
                categoricalFeaturesInfo,
                numTrees,
                featureSubsetStrategy,
                impurity,
                maxDepth,
                maxBins,
                seed
        );
        //测试数据预测
        JavaPairRDD<Double, Double> predictionAndLabel = testData
                .mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));
        //计算错误率
        double testErr = predictionAndLabel.filter(pl -> !pl._1.equals(pl._2())).count() / (double) testData.count();
        System.out.println("Test err:" + testErr);
        //打印树形结构
        System.out.println(model.toDebugString());
        //新数据预测
        Vector v = Vectors.dense(new double[]{3, 8});
        System.out.println("预测为" + model.predict(v));
    }
}
