app.service("stockService", function ($http) {
    this.findAll = function (pageNum) {
        return $http.get('api/v1/stock/findAll?pageNum='+pageNum);
    }
    this.findPage=function (pageNum,pageSize) {
        return $http.get('api/v1/stock/findAll?pageNum='+pageNum+"&pageSize="+pageSize)
    }
    this.search=function (keywords) {
        return $http.get("../scenery/search?keywords="+keywords)
    }
});