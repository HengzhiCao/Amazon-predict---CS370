package Model_classifier;

public class Product {
    private String id;
    private String name;
    private double discountedPrice;
    private double actualPrice;
    private double discountPercentage;
    private double rating;
    private int ratingCount;
    private String imageUrl;
    private String description;
    private String category;

    public Product(String id, String name, double discountedPrice, double actualPrice, double discountPercentage, double rating, int ratingCount, String imageUrl, String description, String category) {
        this.id = id;
        this.name = name;
        this.discountedPrice = discountedPrice;
        this.actualPrice = actualPrice;
        this.discountPercentage = discountPercentage;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.imageUrl = imageUrl;
        this.description = description;
        this.category = category;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public double getRating() {
        return rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }
    @Override
    public String toString() {
        return name + " - " + id;
    }

    public double getPrice() {
        return discountedPrice;
    }

    public void setPrice(double v) {
        discountedPrice = v;
    }

    public void setName(String aNull) {
        name = aNull;
    }

    public void setDescription(String aNull) {
        description = aNull;
    }

    public void setRating(int i) {
        rating = i;
    }

    public void setRatingCount(int i) {
        ratingCount = i;
    }

    public void setDiscountPercentage(double calculatedDiscountPercentage) {
        discountPercentage = calculatedDiscountPercentage;
    }

    public void setActualPrice(double calculatedActualPrice) {
        actualPrice = calculatedActualPrice;
    }

    public void setImageUrl(String defaultImageUrl) {
        imageUrl = defaultImageUrl;
    }

    public void setCategory(String unknown) {
        category = unknown;
    }

    public void setProductId(String unknown) {
        id = unknown;
    }
}
