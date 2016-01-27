'use strict';

app.factory('LocalStorage', function($window) {

    var KEY = 'parametros_de_normas_filtradas';

	return {

		save: function(scope){
            var data = {
                mostrarBack: scope.mostrarBack,
                mostrarAnos: scope.mostrarAnos,
                mostrarNormas: scope.mostrarNormas,
                mostrarPaginacao: scope.mostrarPaginacao,
                showErro: scope.showErro,
                messagemDeErro: scope.messagemDeErro,
                tipo: scope.tipo,
                numero: scope.numero,
                index: scope.index,
                loading: scope.loading,
                anos: scope.anos,
                normas: scope.normas,
                estados: scope.estados,
                paginas: scope.paginas,
                mostrarEstados:  scope.mostrarEstados,
                estado: scope.estado,
                paginaCorrente: scope.paginaCorrente
            };

			$window.localStorage['parametros_de_normas_filtradas'] = angular.toJson(data);
		},
        clear: function(){
            $window.localStorage['parametros_de_normas_filtradas']  = '';
        },
        restore: function(scope){
            try {
                var data = $window.localStorage['parametros_de_normas_filtradas'];
                if (data !== undefined && data !== ''){
                    data = angular.fromJson(data);
                    if (data !== ''){
                        scope.mostrarBack = data.mostrarBack;
                        scope.mostrarAnos = data.mostrarAnos;
                        scope.mostrarNormas = data.mostrarNormas;
                        scope.mostrarPaginacao = data.mostrarPaginacao;
                        scope.showErro = data.showErro;
                        scope.messagemDeErro = data.messagemDeErro;
                        scope.tipo = data.tipo;
                        scope.numero = data.numero;
                        scope.index = data.index;
                        scope.loading = data.loading;
                        scope.anos = data.anos;
                        scope.normas = data.normas;
                        scope.estados = data.estados;
                        scope.paginas = data.paginas;
                        scope.mostrarEstados = data.mostrarEstados;
                        scope.estado = data.estado;
                        scope.paginaCorrente = data.paginaCorrente;
                        return true;
                    }
                }
                return false;  
            }catch(e){
               return false;  
           }
        }
	}

});