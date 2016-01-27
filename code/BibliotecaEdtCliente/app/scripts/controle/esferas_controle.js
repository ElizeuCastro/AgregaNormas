app.controller('EsferasCtrl', function ($scope, $location, LocalStorage) {

	$scope.abrirEsfera = function(link){
		LocalStorage.clear();
		$location.path(link);
	}

});