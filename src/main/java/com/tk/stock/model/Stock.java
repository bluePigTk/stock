package com.tk.stock.model;



import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.math.BigDecimal;

@Document(indexName = "stock",type = "stocks")
public class Stock implements Serializable {
    private Long id;
    private String stock_code;
    private String stock_name;
    private String  stock_date;
    private BigDecimal start_price;
    private BigDecimal end_price;
    private BigDecimal max_price;
    private BigDecimal min_price;
    private String total_money;

    public Stock() {
    }

    public Stock(String stock_code, String stock_name, String stock_date, BigDecimal start_price, BigDecimal end_price, BigDecimal max_price, BigDecimal min_price, String total_money) {
        this.stock_code = stock_code;
        this.stock_name = stock_name;
        this.stock_date = stock_date;
        this.start_price = start_price;
        this.end_price = end_price;
        this.max_price = max_price;
        this.min_price = min_price;
        this.total_money = total_money;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public String getStock_date() {
        return stock_date;
    }

    public void setStock_date(String stock_date) {
        this.stock_date = stock_date;
    }

    public BigDecimal getStart_price() {
        return start_price;
    }

    public void setStart_price(BigDecimal start_price) {
        this.start_price = start_price;
    }

    public BigDecimal getEnd_price() {
        return end_price;
    }

    public void setEnd_price(BigDecimal end_price) {
        this.end_price = end_price;
    }

    public BigDecimal getMax_price() {
        return max_price;
    }

    public void setMax_price(BigDecimal max_price) {
        this.max_price = max_price;
    }

    public BigDecimal getMin_price() {
        return min_price;
    }

    public void setMin_price(BigDecimal min_price) {
        this.min_price = min_price;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getStock_code() {
        return stock_code;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", stock_code='" + stock_code + '\'' +
                ", stock_name='" + stock_name + '\'' +
                ", stock_date='" + stock_date + '\'' +
                ", start_price=" + start_price +
                ", end_price=" + end_price +
                ", max_price=" + max_price +
                ", min_price=" + min_price +
                ", total_money=" + total_money +
                '}';
    }
}

