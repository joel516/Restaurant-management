package com.rest.repository;

import com.rest.models.MenuItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuRepository {
    private final List<MenuItem> items = new ArrayList<>();

    public MenuRepository() {
        items.add(new MenuItem(1, "Veg Spring Rolls", "Starter", 120));
        items.add(new MenuItem(2, "Chicken 65", "Starter", 180));
        items.add(new MenuItem(3, "Paneer Butter Masala", "Main", 220));
        items.add(new MenuItem(4, "Chicken Biryani", "Main", 250));
        items.add(new MenuItem(5, "Butter Naan", "Bread", 40));
        items.add(new MenuItem(6, "Gulab Jamun", "Dessert", 80));
        items.add(new MenuItem(7, "Masala Chai", "Beverage", 30));
        items.add(new MenuItem(8, "Fresh Lime Soda", "Beverage", 60));
    }

    public List<MenuItem> findAll() { return items; }

    public Optional<MenuItem> findById(int id) {
        return items.stream().filter(i -> i.getId() == id).findFirst();
    }

    public void save(MenuItem item) { items.add(item); }

    public boolean deleteById(int id) { return items.removeIf(i -> i.getId() == id); }

    public int nextId() {
        return items.stream().mapToInt(MenuItem::getId).max().orElse(0) + 1;
    }
}
