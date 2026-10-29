package com.rest.repository;

import com.rest.models.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TableRepository {
    private final List<Table> tables = new ArrayList<>();

    public TableRepository() {
        tables.add(new Table(1, 2));
        tables.add(new Table(2, 2));
        tables.add(new Table(3, 4));
        tables.add(new Table(4, 4));
        tables.add(new Table(5, 6));
        tables.add(new Table(6, 8));
    }

    public List<Table> findAll() { return tables; }

    public Optional<Table> findById(int id) {
        return tables.stream().filter(t -> t.getId() == id).findFirst();
    }

    public void save(Table table) { tables.add(table); }

    public boolean deleteById(int id) { return tables.removeIf(t -> t.getId() == id); }

    public int nextId() {
        return tables.stream().mapToInt(Table::getId).max().orElse(0) + 1;
    }
}
