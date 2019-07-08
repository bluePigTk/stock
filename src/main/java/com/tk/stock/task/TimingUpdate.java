package com.tk.stock.task;

import com.tk.stock.mapper.StockMapper;
import com.tk.stock.model.Stock;
import com.tk.stock.utils.StockCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//每五分钟更新一次，将爬到的数据更新到数据库
@Component
public class TimingUpdate {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StockMapper stockMapper;
   // @Scheduled(fixedRate = 1000*60*60*24)
    public void flushData (){
        //对所有的股票进行遍历获取当前的股票信息，每五分钟更新一次
        //从redis中获取初始化数据时存进去的封装所有股票编号和名称的List<Stock>数据
        List<Stock> stockList = (List<Stock>) redisTemplate.opsForValue().get("stockList");
        //for (Stock stock:stockList) {
            //String stock_code = stock.getStock_code();
            //由于之前爬的数据时爬的编号许多没有加sh,sz前缀，故与接口无法对应，若数据正确，拼接字符串即可
            String url="https://hq.sinajs.cn/list=sh600703";
            String result = StockCrawler.getOriginHtml(url);
        String[] split = result.split(",");
            Stock stock=new Stock();
            stock.setStock_code("sh600703");
            stock.setStock_name("三安光电");
            stock.setStart_price(new BigDecimal(split[1]));
             stock.setMin_price(new BigDecimal(split[5]));
            stock.setMax_price(new BigDecimal(split[4]));
             stock.setStock_date(split[30]+split[31]);
             stock.setStart_price(new BigDecimal(split[1]));
             stock.setStart_price(new BigDecimal(split[1]));
            //存入数据库
            List<Stock> stocks=new ArrayList<>();
                stocks.add(stock);
            stockMapper.addBatch(stocks);
        //}
    }
}
