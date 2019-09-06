package tmall.comparator;

import tmall.bean.Product;

import java.util.Comparator;

/**
 * @author Boli Tao
 */
public class ProductReviewComparator implements Comparator<Product> {
    @Override
    public int compare(Product product, Product t1) {
        return (t1.getReviewCount() - product.getReviewCount());
    }
}
