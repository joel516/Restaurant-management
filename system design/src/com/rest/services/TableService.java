package com.rest.services;

import com.rest.models.Table;
import com.rest.repository.TableRepository;
import java.util.List;
import java.util.stream.Collectors;

public class TableService {
    private final TableRepository repo;

    public TableService(TableRepository repo) { this.repo = repo; }

    public List<Table> getAllTables() { return repo.findAll(); }

    public List<Table> getFreeTables() {
        return repo.findAll().stream().filter(Table::isFree).collect(Collectors.toList());
    }

    public Table getTable(int id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Table " + id + " not found."));
    }

    public Table addTable(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive.");
        Table t = new Table(repo.nextId(), capacity);
        repo.save(t);
        return t;
    }

    public void removeTable(int id) {
        Table t = getTable(id);
        if (!t.isFree()) throw new IllegalArgumentException("Cannot remove an occupied table.");
        repo.deleteById(id);
    }

    public void occupy(int id) {
        Table t = getTable(id);
        if (!t.isFree()) throw new IllegalArgumentException("Table " + id + " is already occupied.");
        t.occupyTable();
    }

    public void free(int id) { getTable(id).freeTable(); }
}
