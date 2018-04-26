ptBossApp.controller('RulesController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	
	toastr.options = {
            "debug": false,
            "newestOnTop": false,
            "positionClass": "toast-top-center",
            "closeButton": true,
            "toastClass": "animated fadeInDown",
        };
	
	$scope.isCreditCardCommission=false;
	$scope.creditCardCommissionRate=0;
	
	
	
	function createICheckEvents(){
		$('#ruleNoClassBeforePayment').on('ifChanged', function(event) {
			   
			    var ruleV=0;
			    if(event.target.checked)
			    	ruleV=1;
			    
			      var ruleObj=new Object();
				  ruleObj.ruleId=1;
				  ruleObj.ruleValue=ruleV;
				  ruleObj.ruleName='noClassBeforePayment';
				 
				   $http({
					  method:'POST',
					  url: "/bein/settings/rule/create",
					  data: angular.toJson(ruleObj),
					}).then(function successCallback(response) {
						toastr.success($translate.instant('success'));
					}, function errorCallback(response) {
						$location.path("/login");
					});
			    
			    
			});
			
			$('#ruleNoChangeAfterBonusPayment').on('ifChanged', function(event) {
			    
			    var ruleV=0;
			    if(event.target.checked)
			    	ruleV=1;
			    
			    
				 
				var ruleObj=new Object();
				  ruleObj.ruleId=2;
				  ruleObj.ruleValue=ruleV;
				  ruleObj.ruleName='noChangeAfterBonusPayment';
				 
				   $http({
					  method:'POST',
					  url: "/bein/settings/rule/create",
					  data: angular.toJson(ruleObj),
					}).then(function successCallback(response) {
						toastr.success($translate.instant('success'));
					}, function errorCallback(response) {
						$location.path("/login");
					});
			    
			});
			 
			
			
			$('#rulePayBonusForConfirmedPayment').on('ifChanged', function(event) {
				var ruleV=0;
			    if(event.target.checked)
			    	ruleV=1;
			    
			    
				  
				 
				var ruleObj=new Object();
				  ruleObj.ruleId=3;
				  ruleObj.ruleValue=ruleV;
				  ruleObj.ruleName='payBonusForConfirmedPayment';
				 
				   $http({
					  method:'POST',
					  url: "/bein/settings/rule/create",
					  data: angular.toJson(ruleObj),
					}).then(function successCallback(response) {
						toastr.success($translate.instant('success'));
					}, function errorCallback(response) {
						$location.path("/login");
					});
			});
			
			
			
			
			$('#ruleBonusPaymentFullPacket').on('ifChanged', function(event) {
				var ruleV=0;
			    if(event.target.checked)
			    	ruleV=1;
			    
			    
				  
				 
				var ruleObj=new Object();
				  ruleObj.ruleId=10;
				  ruleObj.ruleValue=ruleV;
				  ruleObj.ruleName='bonusPaymentFullPacket';
				 
				   $http({
					  method:'POST',
					  url: "/bein/settings/rule/create",
					  data: angular.toJson(ruleObj),
					}).then(function successCallback(response) {
						toastr.success($translate.instant('success'));
					}, function errorCallback(response) {
						$location.path("/login");
					});
			});
			
			$('#ruleLocation').on('ifChanged', function(event) {
				   
			    var ruleV=0;
			    if(event.target.checked)
			    	ruleV=1;
			    
			    
				
				  
				 
				var ruleObj=new Object();
				  ruleObj.ruleId=5;
				  ruleObj.ruleValue=ruleV;
				  ruleObj.ruleName='location';
				 
				   $http({
					  method:'POST',
					  url: "/bein/settings/rule/create",
					  data: angular.toJson(ruleObj),
					}).then(function successCallback(response) {
						toastr.success($translate.instant('success'));
					}, function errorCallback(response) {
						$location.path("/login");
					});
			    
			});
			
			$('#noticeRule').on('ifChanged', function(event) {
				   
			    var ruleV=0;
			    if(event.target.checked)
			    	ruleV=1;
			    
			    
				
				var ruleObj=new Object();
				  ruleObj.ruleId=6;
				  ruleObj.ruleValue=ruleV;
				  ruleObj.ruleName='notice';
				 
				   $http({
					  method:'POST',
					  url: "/bein/settings/rule/create",
					  data: angular.toJson(ruleObj),
					}).then(function successCallback(response) {
						toastr.success($translate.instant('success'));
					}, function errorCallback(response) {
						$location.path("/login");
					});
			    
			});
			
			
			
		
			$('#creditCardCommissionRule').on('ifChanged', function(event) {
				   
			    var ruleV=0;
			    if(event.target.checked){
			    	$scope.isCreditCardCommission=true;
			    	ruleV=1;
			    }else{
			    	$scope.isCreditCardCommission=false;
			    }
			    	
			    
				  var ruleObj=new Object();
				  ruleObj.ruleId=7;
				  ruleObj.ruleValue=ruleV;
				  ruleObj.ruleName='creditCardCommission';
				 
				   $http({
					  method:'POST',
					  url: "/bein/settings/rule/create",
					  data: angular.toJson(ruleObj),
					}).then(function successCallback(response) {
						toastr.success($translate.instant('success'));
					}, function errorCallback(response) {
						$location.path("/login");
					});
			    
			});
			
			
			
			$('#ruleNoSaleToPlanning').on('ifChanged', function(event) {
				var ruleV=0;
			    if(event.target.checked)
			    	ruleV=1;
			    
			    
			  
				var ruleObj=new Object();
				  ruleObj.ruleId=9;
				  ruleObj.ruleValue=ruleV;
				  ruleObj.ruleName='noSaleToPlanning';
				 
				   $http({
					  method:'POST',
					  url: "/bein/settings/rule/create",
					  data: angular.toJson(ruleObj),
					}).then(function successCallback(response) {
						toastr.success($translate.instant('success'));
					}, function errorCallback(response) {
						$location.path("/login");
					});
			});
			
			
	}
	
	
	
	$scope.saveCreditCardCommissionRate=function(){
		  var ruleObj=new Object();
			  ruleObj.ruleId=8;
			  ruleObj.ruleValue=$scope.creditCardCommissionRate;
			  ruleObj.ruleName='creditCardCommissionRate';
			 
			   $http({
				  method:'POST',
				  url: "/bein/settings/rule/create",
				  data: angular.toJson(ruleObj),
				}).then(function successCallback(response) {
					toastr.success($translate.instant('success'));
				}, function errorCallback(response) {
					$location.path("/login");
				});
		   
	}
	
	$scope.init=function(){
		$('#ruleNoClassBeforePayment').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		
		$('#ruleNoChangeAfterBonusPayment').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		
		
		$('#ruleBonusPaymentFullPacket').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		
		
		$('#rulePayBonusForConfirmedPayment').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		
		$('#ruleLocation').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		
		$('#noticeRule').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		
		$('#ruleNoSaleToPlanning').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		
		
		$('#creditCardCommissionRule').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		
		
		$http({
			  method:'POST',
			  url: "/bein/settings/rule/findAll",
			}).then(function successCallback(response) {
				
				var res=response.data;
				
				$.each(res,function(i,data){
					if(data.ruleId==1){
						if(data.ruleValue==0){
							$("#ruleNoClassBeforePayment").iCheck('uncheck');
						}else{
							$("#ruleNoClassBeforePayment").iCheck('check');
						}
					}else if(data.ruleId==2){
						if(data.ruleValue==0){
							$("#ruleNoChangeAfterBonusPayment").iCheck('uncheck');
						}else{
							$("#ruleNoChangeAfterBonusPayment").iCheck('check');
						}
						
					}else if(data.ruleId==3){
						
						if(data.ruleValue==0){
							$("#rulePayBonusForConfirmedPayment").iCheck('uncheck');
						}else{
							$("#rulePayBonusForConfirmedPayment").iCheck('check');
						}
					}else if(data.ruleId==5){
						   if(data.ruleValue==0){
								$("#ruleLocation").iCheck('uncheck');
							}else{
								$("#ruleLocation").iCheck('check');
							}
					}else if(data.ruleId==6){
						   if(data.ruleValue==0){
								$("#noticeRule").iCheck('uncheck');
							}else{
								$("#noticeRule").iCheck('check');
							}
					}else if(data.ruleId==7){
						   if(data.ruleValue==0){
								$("#creditCardCommissionRule").iCheck('uncheck');
								$scope.isCreditCardCommission=false;
							}else{
								$("#creditCardCommissionRule").iCheck('check');
								$scope.isCreditCardCommission=true;
							}
					}else if(data.ruleId==8){
						$scope.creditCardCommissionRate=data.ruleValue;
					}else if(data.ruleId==9){
						 if(data.ruleValue==0){
								$("#ruleNoSaleToPlanning").iCheck('uncheck');
							}else{
								$("#ruleNoSaleToPlanning").iCheck('check');
							}
					}else if(data.ruleId==10){
						 if(data.ruleValue==0){
								$("#ruleBonusPaymentFullPacket").iCheck('uncheck');
							}else{
								$("#ruleBonusPaymentFullPacket").iCheck('check');
							}
					}
					
				});	
				createICheckEvents();
				
			});
		
		
	}

});