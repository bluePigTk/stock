package com.tk.stock.utils;

import com.tk.stock.model.Stock;
import com.tk.stock.service.StockServiceImpl.StockServiceImpl;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StockCrawler {
    public static String getOriginHtml(String url){
        HttpClient httpClient=HttpClients.custom().build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");
        HttpResponse httpResponse=null;
        try {
            httpResponse=httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity httpEntity = httpResponse.getEntity();
        String htmlText="";
        try {
            htmlText = EntityUtils.toString(httpEntity, "gbk");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlText;
    }
    public static List<Stock> getAllNameAndCode(String html){
        Document document = Jsoup.parse(html);
        List<Stock> stockList=new ArrayList<>();
        //获得tr的class属性不为head的
        Elements elements = document.select("tr:not(.head)");
        for (Element ele:elements) {
            //获取每个tr元素下的第一个和第二个字元素
            String code = ele.select("td:nth-child(1)").text();
            String name = ele.select("td:nth-child(2)").text();
            Stock stock = new Stock();
            stock.setStock_code(code);
            stock.setStock_name(name);
            stockList.add(stock);
        }
        return stockList;
    };
    public static List<Stock> parsePriceDate(String html,Stock stock){
        System.out.println(stock);
        Document document = Jsoup.parse(html);
        //解析大于1的tr元素
        Elements elements = document.select("table[id=FundHoldSharesTable]").select("tr:gt(0)");
       List<Stock> listStock=new ArrayList<>();
        for (Element element:elements) {
            //将股票的名称和代码赋给new出来的对象
            Stock newStock = new Stock();
            newStock.setStock_name(stock.getStock_name());
            newStock.setStock_code(stock.getStock_code());
            String date = element.select("td:nth-child(1)").text();
            newStock.setStock_date(date);
            String startPrice=element.select("td:nth-child(2)").text();
            newStock.setStart_price(new BigDecimal(startPrice));
            String maxPrice=element.select("td:nth-child(3)").text();
            newStock.setMax_price(new BigDecimal(maxPrice));
            String endPrice=element.select("td:nth-child(4)").text();
            newStock.setEnd_price(new BigDecimal(endPrice));
            String minPrice=element.select("td:nth-child(5)").text();
            newStock.setMin_price(new BigDecimal(minPrice));
            String totalMoney=element.select("td:nth-child(7)").text();
            newStock.setTotal_money(totalMoney);
            listStock.add(newStock);
        }
        return listStock ;

    }

    public static void main(String[] args) {
        String result = getOriginHtml("https://hq.sinajs.cn/list=sh600703");
        System.out.println(result);
        System.out.println(result.split(",")[30]);
    }



}
