package com.rest.services;

import com.rest.models.MenuItem;
import com.rest.repository.MenuRepository;
import java.util.List;
import java.util.stream.Collectors;

public class MenuService {
    private final MenuRepository repo;

    public MenuService(MenuRepository repo) { this.repo = repo; }

    public List<MenuItem> getAll() { return repo.findAll(); }

    public List<MenuItem> getByCategory(String category) {
        return repo.findAll().stream()
                .filter(i -> i.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public MenuItem get(int id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Menu item " + id + " not found."));
    }

    public MenuItem add(String name, String category, double price) {
        if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty.");
        if (price <= 0) throw new IllegalArgumentException("Price must be positive.");
        MenuItem item = new MenuItem(repo.nextId(), name, category.isEmpty() ? "Other" : category, price);
        repo.save(item);
        return item;
    }

    public void updatePrice(int id, double price) {
        if (price <= 0) throw new IllegalArgumentException("Price must be positive.");
        get(id).setPrice(price);
    }

    public void setAvailability(int id, boolean available) { get(id).setAvailable(available); }

    public void remove(int id) {
        get(id);
        repo.deleteById(id);
    }
}
