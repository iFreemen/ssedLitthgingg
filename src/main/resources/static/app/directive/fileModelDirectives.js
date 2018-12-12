/**
 * Created by heqichao on 2018-12-9.
 */
demoApp.directive( "fileUpload", [ "$parse","$http", function( $parse ,$http){
    return {
        restrict: "E",
        scope: {
            url: '@',
            name:'@',
            fileAccept:'@',
            successFunction: '&'
        },
        template:'<div class="fileupload btn btn-purple waves-effect waves-light">'
        +'<span><i class="ion-upload m-r-5"></i>{{name}}</span>'
        +'<input type="file" class="upload" accept="fileAccept" >'
        +'</div>',
        link: function( scope, element, attrs ){
            scope.name = scope.name || "上传";
            var inputE =element[0].children[0].children[1];
            scope.fileAccept = scope.fileAccept || "*";
            inputE.setAttribute('accept',scope.fileAccept );
            element.bind( "change", function(){
                scope.$apply( function(){

                    var fd = new FormData();
                    fd.append( "file", inputE.files[0] );
                    $http.post( scope.url, fd, {
                        transformRequest: angular.identity,
                        headers: { "Content-Type": undefined }
                    }).success(function(data){
                        inputE.value='';
                        if(data.success){
                            if (attrs.successFunction) {
                                scope.successFunction({data:data});
                            }
                        }
                    }).error( function(data){
                        inputE.value='';
                    })

                } )
            } )
        }
    }
}]);