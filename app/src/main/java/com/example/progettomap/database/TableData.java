package com.example.progettomap.database;

import com.example.progettomap.database.TableSchema.Column;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TableData {

    private DbAccess db;


    public TableData(DbAccess db) {
        this.db = db;
    }

    public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException {
        TableSchema ts = new TableSchema(db, table);
        Statement s = db.getConnection().createStatement();
        ResultSet rs = s.executeQuery("SELECT DISTINCT * " + "FROM " + table + ";");
        List<Example> list = new ArrayList<>();
        while (rs.next()) {
            Example ex = new Example();
            for (int i = 0; i < ts.getNumberOfAttributes(); i++) {
                if (ts.getColumn(i).isNumber())
                    ex.add(rs.getDouble(ts.getColumn(i).getColumnName()));
                else ex.add(rs.getString(ts.getColumn(i).getColumnName()));
            }
            list.add(ex);
        }
        s.close();
        rs.close();
        if (list.isEmpty()) throw new EmptySetException("La tabella " + table + " è vuota");
        return list;
    }


    public Set<Object> getDistinctColumnValues(String table, Column column) throws SQLException {
        Statement s = db.getConnection().createStatement();
        ResultSet rs = s.executeQuery("SELECT DISTINCT " + column.getColumnName() + " " + "FROM " + table + " " + "ORDER BY " + column.getColumnName() + ";");
        HashSet<Object> set = new HashSet<>();
        while (rs.next()) {
            if (column.isNumber()) set.add(rs.getDouble(column.getColumnName()));
            else set.add(rs.getString(column.getColumnName()));
        }
        s.close();
        rs.close();
        return set;
    }

    public Object getAggregateColumnValue(String table, Column column, QUERY_TYPE aggregate) throws SQLException, NoValueException {
        Object ret;
        Statement s = db.getConnection().createStatement();
        ResultSet rs = s.executeQuery("SELECT " + aggregate + "(" + column.getColumnName() + ") AS aggregata " + "FROM " + table + ";");
        try {
            if (rs.next()) {
                if (column.isNumber()) ret = rs.getDouble("aggregata");
                else ret = rs.getString("aggregata");
            } else
                throw new NoValueException("Nessun valore per la colonna " + column.getColumnName());
        } finally {
            s.close();
            rs.close();
        }
        return ret;
    }

}
