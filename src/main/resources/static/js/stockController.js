app.controller('stockController', function ($scope,stockService) {
    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){
            $scope.reloadList();//重新加载
        }
    };
    //重新加载列表 数据
    $scope.reloadList=function(){
        //切换页码
        $scope.search( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }

    $scope.findAll=function (pageNum) {
        stockService.findAll(pageNum).success(function (response) {
            console.log(response.data)
                $scope.stockList=response.data.data;
            $scope.paginationConf.totalItems=response.data.totalPage;//更新总记录数

        })
    }
    $scope.search=function (pageNum,pageSize) {
        stockService.search($scope.stockKey,$scope.time,pageNum,pageSize).success(function (response) {
            $scope.stockList=response.data.data;
            $scope.paginationConf.totalItems=response.data.totalPage;//更新总记录数
        })
    }

})