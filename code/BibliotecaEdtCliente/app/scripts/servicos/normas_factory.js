'use strict';

app.factory('ServicoNormas', function($http) {

  var URL_NORMAS_FEDERAIS = Host.PATH + '/normas/federais';
  var URL_NORMAS_FEDERAIS_ANOS = Host.PATH + '/normas/federais/anos';
  var URL_NORMAS_FEDERAIS_POR_ANO = Host.PATH + '/normas/federais/ano';

  var URL_NORMAS_ESTADOS = Host.PATH + '/normas/estados';
  var URL_NORMAS_ESTADUAIS = Host.PATH + '/normas/estaduais';
  var URL_NORMAS_ESTADUAIS_ANOS = Host.PATH + '/normas/estaduais/anos';
  var URL_NORMAS_ESTADUAIS_POR_ANO = Host.PATH + '/normas/estaduais/ano';

  var config = {
    headers : {
      'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
    }
  };

  var factory = {};
   
  factory.getNormasFederaisAnos = function (callback) { 

    $http.get(URL_NORMAS_FEDERAIS_ANOS)
      .success(callback).error(callback);
  };

  factory.getNormasFederaisPorAno = function (data, callback) { 

    $http.get(URL_NORMAS_FEDERAIS_POR_ANO, {params: data})
      .success(callback).error(callback);
  };

  factory.getNormasFederais = function (data, callback) { 

    $http.get(URL_NORMAS_FEDERAIS, {params: data})
      .success(callback).error(callback);
  };

  factory.getEstados = function (callback) { 

    $http.get(URL_NORMAS_ESTADOS)
      .success(callback).error(callback);
  };

  factory.getNormasEstaduaisAnos = function (data, callback) { 

    $http.get(URL_NORMAS_ESTADUAIS_ANOS, {params: data})
      .success(callback).error(callback);
  };

  factory.getNormasEstaduaisPorAno = function (data, callback) { 

    $http.get(URL_NORMAS_ESTADUAIS_POR_ANO, {params: data})
      .success(callback).error(callback);
  };

  factory.getNormasEstaduais = function (data, callback) { 

    $http.get(URL_NORMAS_ESTADUAIS, {params: data})
      .success(callback).error(callback);
  };
  
  return factory;
}); 