app.controller("itemController", function ($scope, $http) {

    $scope.itemNum =3;
    $scope.addItem = function () {
        $scope.itemNum = $scope.itemNum -1;
        if($scope.itemNum<0){
            $scope.itemNum =0;
        }

    }
    $scope.deleteItem = function () {
        $scope.itemNum = $scope.itemNum +1;
        if($scope.itemNum>3){
            $scope.itemNum =3;
        }
    }
    // 选择规格选项
    $scope.specificationItems ={};
    $scope.selectSpecification = function (attrName, attrValue) {
        $scope.specificationItems[attrName] = attrValue;
    }
    // 判断是否选择规格选项
    $scope.isSelected = function (attrName, attrValue) {
        if($scope.specificationItems[attrName] == attrValue){
            return true;
        }
        return false;
    }

});