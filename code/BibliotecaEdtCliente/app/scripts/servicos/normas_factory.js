'use strict';

app.factory('ServicoNormas', function($http) {

  var URL_NORMAS_FEDERAIS_ANOS = 'http://localhost:8080/BibliotecaEdtWebService/normas/federais/anos';
  var URL_NORMAS_FEDERAIS_POR_ANO = 'http://localhost:8080/BibliotecaEdtWebService/normas/federais/ano';

  var config = {
    headers : {
      'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
    }
  };

  var factory = {};
   
  factory.getAnos = function (callback) { 

    $http.get(URL_NORMAS_FEDERAIS_ANOS)
      .success(callback).error(callback);
  };

  factory.getNormasPorAno = function (data, callback) { 

    $http.get(URL_NORMAS_FEDERAIS_POR_ANO, {params: data})
      .success(callback).error(callback);
  };
  
  return factory;

}); 