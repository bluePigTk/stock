package com.tk.stock.service;

import com.tk.stock.model.PageBean;
import com.tk.stock.model.Stock;

import java.util.List;
import java.util.Map;

public interface StockService {
    void initAllStockData();
    PageBean findAll(PageBean pageBean);
    void addStocks(List<Stock> stockList);
    PageBean search(PageBean pageBean,Map map);
}
