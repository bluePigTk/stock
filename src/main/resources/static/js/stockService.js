app.service("stockService", function ($http) {
    this.findAll = function (pageNum) {
        return $http.get('api/v1/stock/findAll?pageNum='+pageNum);
    }
    // this.findPage=function (pageNum,pageSize,stockKey,time) {
    //     return $http.get('api/v1/stock/findAll?pageNum='+pageNum+'&pageSize='+pageSize);
    // }
    this.search=function (stockKey,time,pageNum,pageSize) {
        return $http.get("api/v1/stock/search?stockKey="+stockKey+'&time='+time+"&pageNum="+pageNum+"&pageSize="+pageSize)
    }
});