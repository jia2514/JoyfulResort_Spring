package com.joyfulresort.ool.cart;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cart {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {
        items.add(item);
    }

    public void removeItem(String cartId) {
        items.removeIf(item -> item.getCartId().equals(cartId));
    }
    public List<CartItem> getItems() {
        return items;
    }

    public Integer getTotalPrice() {
        return items.stream().mapToInt(CartItem::getMeetingRoomPrice).sum();
    }

    public void clear() {
        items.clear();
    }
}

