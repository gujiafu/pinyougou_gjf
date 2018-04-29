//定义处理器
app.controller("brandController", function ($scope, $controller, brandService) {

    // 继承$controller
    $controller("baseController",{$scope:$scope});

    //定义一个查询全部数据列表的方法
    $scope.findAll = function () {
        //到后台查询品牌数据
        brandService.findAll().success(function (response) {
            //品牌的数据列表
            $scope.list = response;
        });
    };



    //分页查询列表
    $scope.findPage = function (page, rows) {
        brandService.findPage(page,rows).success(function (response) {
            //设置列表
            $scope.list = response.rows;
            //总记录数
            $scope.paginationConf.totalItems = response.total;

        });
    };

    //保存数据
    $scope.save = function () {
        var obj;
        if ($scope.entity.id != null) {
            obj = brandService.update($scope.entity)
        }else{
            obj = brandService.add($scope.entity)
        }
        obj.success(function (response) {
            if (response.success) {
                //刷新列表
                $scope.reloadList();
            } else {
                alert(response.message);
            }
        });

    };

    //根据id查询
    $scope.findOne = function (id) {
        brandService.findOne(id).success(function (response) {
            $scope.entity = response;
        });
    };


    //删除记录
    $scope.delete = function () {
        if ($scope.selectedIds.length == 0) {
            alert("请先选择要删除的记录！");
            return;
        }

        if (confirm("确定要删除选中的记录吗？")) {
            brandService.delete($scope.selectedIds).success(function (response) {
                if (response.success) {
                    //重新加载列表
                    $scope.reloadList();
                    //清空id集合
                    $scope.selectedIds = [];
                } else {
                    alert(response.message);
                }

            });
        }
    };

    //分页条件查询列表
    $scope.searchEntity = {};
    $scope.search = function (page, rows) {
        brandService.search(page,rows,$scope.searchEntity).success(function (response) {
            //设置列表
            $scope.list = response.rows;
            //总记录数
            $scope.paginationConf.totalItems = response.total;

        });
    };

});