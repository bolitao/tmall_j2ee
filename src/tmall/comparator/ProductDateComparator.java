package tmall.comparator;

import tmall.bean.Product;

import java.util.Comparator;

/**
 * @author Boli Tao
 */
public class ProductDateComparator implements Comparator<Product> {
    @Override
    public int compare(Product product, Product t1) {
        return product.getCreateDate().compareTo(t1.getCreateDate());
    }
}
