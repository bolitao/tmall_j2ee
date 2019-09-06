package tmall.comparator;

import tmall.bean.Product;

import java.util.Comparator;

/**
 * 把(销量 * 评价)高的放前面
 *
 * @author Boli Tao
 */
public class ProductAllComparator implements Comparator<Product> {
    @Override
    public int compare(Product product, Product t1) {
        return ((t1.getReviewCount() * t1.getSaleCount()) - (product.getReviewCount() * product.getSaleCount()));
    }
}
