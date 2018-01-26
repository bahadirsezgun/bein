ptBossApp.controller('PtExpensesController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	$scope.year;
	$scope.month="0";
	
	$scope.ptExpenseses;
	$scope.ptCurrency;
	$scope.dateFormat;
	$scope.dateTimeFormat;
	
	$scope.ptExpenses=new Object();
	$scope.ptExpenses.peId=0;
	$scope.ptExpenses.peAmount=0;
	$scope.ptExpenses.peComment="";
	$scope.ptExpenses.peDate=new Date();
	$scope.ptExpenses.peInOut="0";
	
	$scope.query=true;
	$scope.newExpense=false;
	
	$scope.totalExpenseAmount=0;
	$scope.totalIncomeAmount=0;
	
	$scope.months=new Array({value:"0",name:$translate.instant("pleaseSelect")}
								    ,{value:"1",name:$translate.instant("january")}
								   ,{value:"2",name:$translate.instant("february")}
								   ,{value:"3",name:$translate.instant("march")}
								   ,{value:"4",name:$translate.instant("april")}
								   ,{value:"5",name:$translate.instant("may")}
								   ,{value:"6",name:$translate.instant("june")}
								   ,{value:"7",name:$translate.instant("july")}
								   ,{value:"8",name:$translate.instant("august")}
								   ,{value:"9",name:$translate.instant("september")}
								   ,{value:"10",name:$translate.instant("october")}
								   ,{value:"11",name:$translate.instant("november")}
								   ,{value:"12",name:$translate.instant("december")}
								   );

	
	$scope.years=new Array();
	
	
	
	$scope.initPEC=function(){
		commonService.pageName=$translate.instant("ptExpensesTitle");
		commonService.pageComment=$translate.instant("ptExpensesTitleComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		
		var date=new Date();
		var year=date.getFullYear();
		
		for(var i=-10;i<10;i++){
			$scope.years.push(year+i);
		}
		
		$scope.year=year;
		$scope.month=""+(date.getMonth()+1);
		
		
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptDateTimeFormat;
			$scope.ptCurrency=global.ptCurrency;
			
		});
		
	}
	
	
	
	
	$scope.editExpense=function(ptExpenses){
		$scope.ptExpenses=ptExpenses;
		$scope.ptExpenses.peInOut=""+ptExpenses.peInOut;
		$scope.query=false;
		$scope.newExpense=true;
	}
	
	$scope.showQuery=function(){
		$scope.query=true;
		$scope.newExpense=false;
	}
	$scope.hideQuery=function(){
		$scope.query=false;
		$scope.newExpense=false;
	}
	
	$scope.enterExpense=function(){
		$scope.query=false;
		$scope.newExpense=true;
		$scope.ptExpenses.peId=0;
		$scope.ptExpenses.peDate=new Date();
		$scope.ptExpenses.peAmount=0;
		$scope.ptExpenses.peComment="";
		$scope.ptExpenses.peInOut="0";
		
	}
	
	$scope.queryPtExpenses=function(){
		 $http({
			  method:'POST',
			  url: "/bein/income/findPtExpensesForMonth/"+$scope.year+"/"+$scope.month,
			}).then(function successCallback(response) {
				$scope.ptExpenseses=response.data;
				$scope.totalIncomeAmount=0;
				$scope.totalExpenseAmount=0;
				
				$.each($scope.ptExpenseses,function(i,data){
					if(data.peInOut==1){
						$scope.totalIncomeAmount+=data.peAmount;
					}else{
						$scope.totalExpenseAmount+=data.peAmount;
					}
				});
				
				
				
				$scope.query=false;
				$scope.newExpense=false;
			})
		 
		
	}
	
	

	$scope.createPtExpenses=function(income){
		$http({
			  method:'POST',
			  url: "/bein/income/createPtExpenses",
			  data: angular.toJson($scope.ptExpenses),
			}).then(function successCallback(response) {
				toastr.success($translate.instant("incomeExpenseRecorded"));
				$scope.queryPtExpenses();
			}, function errorCallback(response) {
				$location.path("/login");
			});
	}
	
	$scope.cancelPtExpenses=function(expense){
		swal({
            title: $translate.instant("warning"),
            text: $translate.instant("deleteExpenses"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesContinue"),
            cancelButtonText: $translate.instant("no"),
            closeOnConfirm: true,
            closeOnCancel: true },
        function (isConfirm) {
            if (isConfirm) {
		
            	$http({
            		method:'POST',
      			  	url: "/bein/income/deletePtExpenses",
      			  	data: angular.toJson(expense),
      			}).then(function successCallback(response) {
      				toastr.success($translate.instant("incomeExpenseDeleted"));
      				$scope.queryPtExpenses();
      			}, function errorCallback(response) {
      				$location.path("/login");
      			});
           }
       });
	}
	
	
	
	function findPtExpensesByDate(){
		$.ajax({
			  type:'POST',
			  url: "../pt/incomeController/findPtExpensesByDate",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify($scope.ptExpenses),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.ptExpenseses=res;
				$scope.$apply();
			}).fail  (function(jqXHR, textStatus, errorThrown) {
				$scope.$apply();
			});
		
		
	}
	
	
	
	
});