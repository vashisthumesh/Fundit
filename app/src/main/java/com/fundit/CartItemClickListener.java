package com.fundit;

/**
 * Created by prince on 4/8/2017.
 */

public interface CartItemClickListener {
    void onAddToCartClick();
    void onProductDeleted();

    void onProductAdded(int cartCount);
}
