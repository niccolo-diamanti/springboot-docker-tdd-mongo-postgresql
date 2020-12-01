package com.niccolodiamanti.challenge.service.impl;

import com.niccolodiamanti.challenge.data.mongodb.model.Bundle;
import com.niccolodiamanti.challenge.data.mongodb.model.Cart;
import com.niccolodiamanti.challenge.data.mongodb.repository.CartRepository;
import com.niccolodiamanti.challenge.data.postgre.model.Product;
import com.niccolodiamanti.challenge.service.BundleService;
import com.niccolodiamanti.challenge.service.CartService;
import com.niccolodiamanti.challenge.service.ProductService;
import com.niccolodiamanti.challenge.web.error.CartNotFoundException;
import com.niccolodiamanti.challenge.web.error.ProductNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository _cartRepository;
    private final BundleService _bundleService;
    private final ProductService _productService;

    public CartServiceImpl(CartRepository cartRepository, BundleService bundleService, ProductService productService) {
        _cartRepository = cartRepository;
        _bundleService = bundleService;
        _productService = productService;
    }

    @Override
    public Optional<Cart> get(UUID id) {
        return _cartRepository.findById(id.toString());
    }

    @Override
    public Cart create(@NotNull Cart cart) {
        return _cartRepository.save(cart);
    }

    @Override
    public Cart update(UUID cartId, Product product) {

        //Get cart by id from database
        final Optional<Cart> cart = get(cartId);

        //If cart is present do the update, else throw exception
        if (cart.isPresent()) {
            final Cart cartFromDb = cart.get();

            //We assume that only one product of the same type can be added
            if (cartFromDb.getProducts().contains(product.getSku()))
                return cartFromDb;

            //Get added product from database
            final Optional<Product> optionalProduct = _productService.get(product.getSku());

            //If product is present add it to cart, else throw exception
            if (optionalProduct.isPresent()) {

                final Set<String> products = cartFromDb.getProducts();
                final Product productFromDb = optionalProduct.get();

                //Add product to set
                products.add(productFromDb.getSku());

                //Calculate new cart price
                cartFromDb.setPrice(cartFromDb.getPrice() + productFromDb.getPrice());

                //Calculate new discount value
                cartFromDb.setDiscount(calculateDiscount(cartFromDb, productFromDb));

                //Save and return updated cart
                return _cartRepository.save(cartFromDb);
            } else {
                throw new ProductNotFoundException();
            }
        } else {
            throw new CartNotFoundException();
        }
    }

    @Override
    public double calculateDiscount(Cart cart, Product lastAddedProduct) {
        double discount = cart.getDiscount();

        //If cart contains only 1 product, discount will be 0 (bundles are for 2 products)
        final Set<String> cartProducts = cart.getProducts();
        if (cartProducts.size() == 1) {
            return 0;
        } else {
            //Get all bundles that contains the new product to minimize the number of bundles to compare
            final List<Bundle> productBundles = _bundleService.getBundlesByProductIn(Collections.singleton(lastAddedProduct.getSku()));
            for (Bundle bundle : productBundles) {
                final String firstProductSku = bundle.getProducts().get(0);
                final String secondProductSku = bundle.getProducts().get(1);

                //If cart contains both bundle's products a new discount is applied
                if (cartProducts.contains(firstProductSku) && cartProducts.contains(secondProductSku)) {

                    //Find products to get prices
                    final Product firstProduct = _productService.get(firstProductSku).orElseThrow(ProductNotFoundException::new);
                    final Product secondProduct = _productService.get(secondProductSku).orElseThrow(ProductNotFoundException::new);

                    //Calculate new discount
                    final double currentBundleDiscount = bundle.getDiscount();
                    final double currentDiscount = (firstProduct.getPrice() + secondProduct.getPrice()) * currentBundleDiscount;

                    //If current discount is greater, update the value
                    if (currentDiscount > discount)
                        discount = currentDiscount;
                }
            }

            //When a cart contains more than 5 products without any bundle, then a 6% discount is applied
            if (cartProducts.size() > 5 && discount == 0) {
                discount = cart.getPrice() * 0.06;
            }
            return discount;
        }
    }
}
