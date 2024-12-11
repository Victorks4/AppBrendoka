package com.example.appproject05.utils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.appproject05.models.CartItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gerenciador do carrinho de compras usando Firebase Realtime Database
 */
public class CartManager {
    private static final String CART_PATH = "users/%s/cart";
    private DatabaseReference cartRef;
    private FirebaseUser currentUser;
    private List<CartItem> cartItems = new ArrayList<>();
    private CartListener cartListener;

    public interface CartListener {
        void onCartUpdated(List<CartItem> items, double total);
        void onError(String message);
    }

    public CartManager() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String cartPath = String.format(CART_PATH, currentUser.getUid());
            cartRef = FirebaseDatabase.getInstance().getReference(cartPath);
        }
    }

    public void setCartListener(CartListener listener) {
        this.cartListener = listener;
        if (cartRef != null) {
            listenToCartChanges();
        }
    }

    private void listenToCartChanges() {
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                cartItems.clear();
                double total = 0;

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    CartItem item = itemSnapshot.getValue(CartItem.class);
                    if (item != null) {
                        cartItems.add(item);
                        total += item.getTotal();
                    }
                }

                if (cartListener != null) {
                    cartListener.onCartUpdated(cartItems, total);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                if (cartListener != null) {
                    cartListener.onError(error.getMessage());
                }
            }
        });
    }

    public Task<Void> addItem(CartItem item) {
        if (currentUser == null || cartRef == null) {
            return null;
        }

        return cartRef.child(item.getProductId()).setValue(item);
    }

    public Task<Void> updateItemQuantity(String productId, int newQuantity) {
        if (currentUser == null || cartRef == null) {
            return null;
        }

        return cartRef.child(productId).child("quantity").setValue(newQuantity);
    }

    public Task<Void> removeItem(String productId) {
        if (currentUser == null || cartRef == null) {
            return null;
        }

        return cartRef.child(productId).removeValue();
    }

    public Task<Void> clearCart() {
        if (currentUser == null || cartRef == null) {
            return null;
        }

        return cartRef.removeValue();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double getCartTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotal();
        }
        return total;
    }
}