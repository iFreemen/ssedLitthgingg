/**
 * Created by heqichao on 2018-12-9.
 */
demoApp.service( "fileUpload", ["$http", function( $http ){
    this.uploadFileToUrl = function( file, uploadUrl ){
        var fd = new FormData();
        fd.append( "file", file )
        $http.post( uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: { "Content-Type": undefined }
        }).success(function(){
                // blabla...
        }).error( function(){
                // blabla...
        })
    }
}]);