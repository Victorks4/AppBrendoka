package com.example.appproject05.utils;

import com.example.appproject05.models.CartItem;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private List<CartItem> cartItems = new ArrayList<>();
    private CartListener cartListener;

    public interface CartListener {
        void onCartUpdated(List<CartItem> items, double total);
        void onError(String message);
    }

    public CartManager() {
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            listenToCartChanges();
        }
    }

    public void setCartListener(CartListener listener) {
        this.cartListener = listener;
        if (currentUser != null) {
            listenToCartChanges();
        }
    }

    private void listenToCartChanges() {
        db.collection("carts")
                .document(currentUser.getUid())
                .collection("items")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        if (cartListener != null) {
                            cartListener.onError(error.getMessage());
                        }
                        return;
                    }

                    if (value != null) {
                        cartItems.clear();
                        double total = 0;

                        for (DocumentSnapshot doc : value.getDocuments()) {
                            CartItem item = doc.toObject(CartItem.class);
                            if (item != null) {
                                cartItems.add(item);
                                total += item.getTotal();
                            }
                        }

                        if (cartListener != null) {
                            cartListener.onCartUpdated(cartItems, total);
                        }
                    }
                });
    }

    public Task<Void> addItem(CartItem item) {
        if (currentUser == null) return null;

        return db.collection("carts")
                .document(currentUser.getUid())
                .collection("items")
                .document(item.getProductId())
                .set(item);
    }

    public Task<Void> updateItemQuantity(String productId, int newQuantity) {
        if (currentUser == null) return null;

        return db.collection("carts")
                .document(currentUser.getUid())
                .collection("items")
                .document(productId)
                .update("quantity", newQuantity);
    }

    public Task<Void> removeItem(String productId) {
        if (currentUser == null) return null;

        return db.collection("carts")
                .document(currentUser.getUid())
                .collection("items")
                .document(productId)
                .delete();
    }

    public Task<Void> clearCart() {
        if (currentUser == null) return null;

        // Deletar todos os documentos na subcoleção items
        return db.collection("carts")
                .document(currentUser.getUid())
                .collection("items")
                .get()
                .continueWithTask(task -> {
                    List<Task<Void>> deleteTasks = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        deleteTasks.add(document.getReference().delete());
                    }
                    return Tasks.whenAll(deleteTasks);
                });
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