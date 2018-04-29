//定义处理器
app.controller("baseController", function ($scope) {


    // 初始化分页参数
    $scope.paginationConf = {
        currentPage: 1,// 当前页号
        totalItems: 10,// 总记录数
        itemsPerPage: 10,// 页大小
        perPageOptions: [10, 20, 30, 40, 50],// 可选择的每页大小
        onChange: function () {// 当上述的参数发生变化了后触发
            $scope.reloadList();
        }
    };

    $scope.reloadList = function () {
        //$scope.findPage($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };


    //当前选择了那些记录id
    $scope.selectedIds = [];
    //设置选择的id
    $scope.updateSelection = function ($event, id) {

        if ($event.target.checked) {
            //如果当前的复选框是选中的则需要将id加入到id集合
            $scope.selectedIds.push(id);
        } else {
            //如果当前的复选框是未选中的则需要将id从id集合中移除
            //查询要删除的元素在集合中的索引号
            var index = $scope.selectedIds.indexOf(id);
            //删除集合中的元素：参数一：要删除的元素的索引号，参数二：删除的个数
            $scope.selectedIds.splice(index, 1);
        }
    };



});