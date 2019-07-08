package com.tk.stock.controller;
import com.tk.stock.mapper.StockMapper;
import com.tk.stock.model.PageBean;
import com.tk.stock.model.Stock;
import com.tk.stock.model.StockResult;
import com.tk.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/stock")
public class StockController {
    @Autowired
    private StockService stockService;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ElasticsearchRepository elasticsearchRepository;
    @GetMapping("/init")
    public String  insert() {
        try {
            stockService.initAllStockData();
            return "成功";
        }catch (Exception e){
            e.printStackTrace();
            return "失败";
        }

    }
    @GetMapping("/findAll")
    public StockResult findAll(int pageNum,@RequestParam(defaultValue = "20") int pageSize ){
        try {
            PageBean pageBean=new PageBean();
            pageBean.setPageNum(pageNum);
            pageBean.setPageSize(pageSize);
            PageBean pages = stockService.findAll(pageBean);
            return new StockResult(true,"查询成功",pages);
        }catch (Exception e){
            return new StockResult(false,"查询失败");
        }
    }
    @RequestMapping("/test")
    public String testEs(){
        Stock stock = new Stock();
        stock.setId(1l);
        stock.setStock_date("2019-02-12");
        stock.setStart_price(new BigDecimal("1234"));
        stock.setEnd_price(new BigDecimal("2234"));
        stock.setMax_price(new BigDecimal("3234"));
        stock.setMin_price(new BigDecimal("999"));
        stock.setStock_name("中国石油");
        elasticsearchRepository.save(stock);
        return "成功";
    }
    @RequestMapping("/search")
    public StockResult search(String stockKey, String time,int pageNum,@RequestParam(defaultValue = "20") int pageSize) {
        Map map = new HashMap<>();
        map.put("stockKey",stockKey);
        map.put("time",time);
        PageBean pageBean=new PageBean();
        pageBean.setPageNum(pageNum);
        pageBean.setPageSize(pageSize);

        try {
            PageBean pages = stockService.search(pageBean,map);
            return new StockResult(true,"查询成功",pages);
        } catch (Exception e) {
            e.printStackTrace();
            return new StockResult(false,"查询失败");
        }
    }

}
