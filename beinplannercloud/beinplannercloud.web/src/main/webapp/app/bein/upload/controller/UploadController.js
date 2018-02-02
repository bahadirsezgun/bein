ptBossApp.controller('UploadController', function($scope,$http,$translate,$rootScope,commonService) {
	
	$scope.init=function(){
		commonService.normalHeaderVisible=false;
		
	}
	
	
	
	
	$scope.progressValue=0;
	
	$scope.sendFile=function(objId){
		$scope.progressValue=0;
		
		sendExcelFiles(objId);
		
	};
	
	$scope.fileName;
	
	


	$("#excelFileUploadUrl").bind("change",function(e){
		$scope.fileName=e.target.files[0].name;
		$scope.$apply();
	});
	
	
	
	$scope.fileOpen=function(){
		$("#excelFileUploadUrl").click();
	}
	
	
	$scope.downloadFile=function(){
		$("#form2").submit();
	}
	
	
	function sendExcelFiles(objectId){
	    var url = "/bein/upload/uploadMembers";
	    $scope.progressValue=10;
	    var fileInputElement=document.getElementById(objectId);
		// HTML file input user's choice...
		//formData.append("fileId", fileInputElement.files[0].name+"-ali");
		var files = fileInputElement.files;
		
		for(var i=0;i<files.length;i++){
			var formData = new FormData();
			formData.append("userfile", files[i]);
			var fileId=Math.floor((Math.random() * 1000000) + 1)
			var fileName=files[i].name;
			var request = new XMLHttpRequest();
			request.open("POST", url+"/"+fileId);//+"&usrId="+usrRId+"&firmIdx="+$("#firmIdxF").val());
			//addExcelFile(fileName, fileId);
			request.send(formData);
		}
		
		window.setTimeout(function(){
			dataStatu();}, 800);
		
	}
	
	
	
	var locationChange=false;
	
	$rootScope.$on("$routeChangeStart", function (event, next, current) {
		locationChange=true;
	});
	
	function dataStatu(){
		if(!locationChange){
		$http({
		  	  method:'POST',
		  	  url: "/bein/upload/progressListener",
		  	}).then(function successCallback(response) {
		  		if(response.data!=null){
		  			
		  			$scope.progressValue=response.data.loadedValue;
		  			if(response.data.resultStatu!="finished"){
		  				window.setTimeout(function(){dataStatu();}, 100);
		  			}
		  			
		  		}
		  	}, function errorCallback(response) {
				$location.path("/login");
			});
		}
	}
	
	
	

	
	
});