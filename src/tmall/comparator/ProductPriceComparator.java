package tmall.comparator;

import tmall.bean.Product;

import java.util.Comparator;

/**
 * @author Boli Tao
 */
public class ProductPriceComparator implements Comparator<Product> {
    @Override
    public int compare(Product product, Product t1) {
        return (int) (product.getPromotePrice() - t1.getPromotePrice());
    }
}
