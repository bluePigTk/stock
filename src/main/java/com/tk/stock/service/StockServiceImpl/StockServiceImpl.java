package com.tk.stock.service.StockServiceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tk.stock.mapper.StockMapper;
import com.tk.stock.model.PageBean;
import com.tk.stock.model.Stock;
import com.tk.stock.service.StockService;
import com.tk.stock.utils.StockCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class StockServiceImpl implements StockService {
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public PageBean findAll(PageBean pageBean) {
        //数据经常变化不太适合redis
//       //先从缓存中找，到不到再将数据库中数据同步到redis,当数据库中数据有相应的更新时，同时也应更新redis
//        List<Stock> stockList = (List<Stock>) redisTemplate.opsForValue().get("stockList");
//        // TODO: 2019/7/6  可能会出现缓存穿透问题
//            if(null!=stockList){
//                return stockList;
//            }else {
//
//                List<Stock> all = stockMapper.findAll();
//                redisTemplate.opsForValue().set("stockList",all);
//                return all;
//            }
        PageHelper.startPage(pageBean.getPageNum(),pageBean.getPageSize());
        List<Stock> stockList = stockMapper.findAll();
        PageInfo<Stock> pageInfo = new PageInfo<>(stockList);
        List<Stock> stocks = pageInfo.getList();
        pageBean.setData(stocks);
        pageBean.setTotalPage((int)pageInfo.getTotal());
        return pageBean;
    }
    @Override
    public void initAllStockData() {
        List<Stock> stockList = codeAndNameStockList();
        //将获取到的数据放到redis中，后面定时刷新时许要用到该数据，不用重复解析。
        redisTemplate.opsForValue().set("stockList",stockList);
        //获取当前的年份
        Date date = new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        System.out.println(year);
        //获取当前季度
        int month = calendar.get(Calendar.MONTH) + 1;
        int jidu = (int)Math.ceil(month / 3.0);
        //遍历列表，获取股票的编号
        for (Stock stock:stockList) {
            String stock_code=stock.getStock_code();
            //最近10年
            for(int i=year;i>year-10;i--) {
                //季度
                for (int j = 1; j <= 4; j++) {
                    //当时间大于当前是跳出循环
                    if (i == year && j > jidu) {
                        break;
                    } else {
                        String url="http://money.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/"+stock_code+".phtml?year="+i+"&jidu="+j;
                        String originHtml = StockCrawler.getOriginHtml(url);
                        List<Stock> fullDataStockList = StockCrawler.parsePriceDate(originHtml, stock);

                        if(fullDataStockList!=null&&fullDataStockList.size()>0){
                            stockMapper.addBatch(fullDataStockList);
                        }
                        //设置休眠时间
                        try {
                            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }

    }
    @Override
    public void addStocks(List<Stock> stockList) {
        stockMapper.addBatch(stockList);
    }

    @Override
    public PageBean search(PageBean pageBean,Map map) {
        PageHelper.startPage(pageBean.getPageNum(),pageBean.getPageSize());
        List<Stock> stockList = stockMapper.search(map);
        PageInfo<Stock> pageInfo = new PageInfo<>(stockList);
        List<Stock> stocks = pageInfo.getList();
        pageBean.setData(stocks);
        pageBean.setTotalPage((int)pageInfo.getTotal());
        return pageBean;
    }

    //只封装了名字和代号的股票list
    public List<Stock> codeAndNameStockList(){
        //根据所有的股票代码代码，查询对应的价格信息
        //每页40，根据页面上总数居得出页码最大值
        String originHtml = StockCrawler.getOriginHtml("http://vip.stock.finance.sina.com.cn/q/go.php/vIR_CustomSearch/index.phtml?sr_p=-1&order=code%7C1&p=1");
        Document document = Jsoup.parse(originHtml);
        //解析网页获得总数据
        String str= document.select("div[class=number]").get(0).text();
        Pattern p=Pattern.compile("[^0-9]");
        Matcher matcher = p.matcher(str);
        Integer totalData = Integer.parseInt(matcher.replaceAll(""));
        int totalPages = (int)(Math.ceil(totalData/40.0));
        //分别从第一页到最后一页爬出所有名称及编号
        List<Stock> allStockList=new ArrayList<>();
        for(int i=1;i<=totalPages;i++){
            String url = "http://vip.stock.finance.sina.com.cn/q/go.php/vIR_CustomSearch/index.phtml?sr_p=-1&order=code%7C1&p="+i;
            String htmlText = StockCrawler.getOriginHtml(url);
            //System.out.println(htmlText);
            //将html解析，提取出来股票编号和股票名称,并封装进实体
            List<Stock> stockList = StockCrawler.getAllNameAndCode(htmlText);
            //依次将每页的数据追加到集合
            allStockList.addAll(stockList);
          try {
                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000));
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
        }
        System.out.println(allStockList.size());
        return allStockList;
    }

}
