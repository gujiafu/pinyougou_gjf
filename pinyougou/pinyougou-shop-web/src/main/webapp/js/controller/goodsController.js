app.controller("goodsController", function ($scope, $controller, goodsService, uploadService) {

    //加载baseController控制器并传入1个作用域，与angularJs运行时作用域相同.
    $controller("baseController",{$scope:$scope});

    //加载列表数据
    $scope.findAll = function(){
        goodsService.findAll().success(function (response) {
            $scope.list = response;
        });
    };

    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        });
    };

    $scope.save = function () {
        var object;
        $scope.entity.goodsDesc.introduction = editor.html();
        if($scope.entity.goods.id != null){//更新
            object = goodsService.update($scope.entity);
        } else {//新增
            object = goodsService.add($scope.entity);
        }
        object.success(function (response) {
            if(response.success){
                alert(response.message);
                $scope.entity= {};
                editor.html("");
            } else {
                alert(response.message);
            }
        });
    };

    $scope.findOne = function (id) {
        goodsService.findOne(id).success(function (response) {
            $scope.entity = response;
        });
    };

    $scope.delete = function () {
        if($scope.selectedIds.length < 1){
            alert("请先选择要删除的记录");
            return;
        }
        if(confirm("确定要删除已选择的记录吗")){
            goodsService.delete($scope.selectedIds).success(function (response) {
                if(response.success){
                    $scope.reloadList();
                    $scope.selectedIds = [];
                } else {
                    alert(response.message);
                }
            });
        }
    };

    $scope.searchEntity = {};//初始为空
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        });
    };

    $scope.uploadFile = function () {
        uploadService.uploadFile().success(function (response) {
            if(response.success){
                $scope.image_entity.url = response.message;
            }else{
                alter(response.message);
            }
        }).error(function () {
            alert(" 上传图片失败");
        })
    }

});