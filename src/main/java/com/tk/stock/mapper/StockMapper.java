package com.tk.stock.mapper;

import com.tk.stock.model.Stock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface StockMapper {
    List<Stock> findAll();
    void addBatch(List<Stock> stockList);
    List<Stock> search(Map map);
}
