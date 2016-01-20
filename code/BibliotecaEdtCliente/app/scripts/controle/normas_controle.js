'use strict';

app.controller('NormasCtrl', function ($scope, $rootScope, $location, $routeParams, ServicoNormas) {

    var NORMA_EMPTY = " Não foram encontradas normas para os filtros indicados ",
        CHOOSE_STATE = "Escolha um estado";

    $scope.mostrarBack = false;
    $scope.mostrarAnos = false;
    $scope.mostrarNormas = false;
    $scope.mostrarPaginacao = false;
    $scope.mostrarEstados = $routeParams.tipoEsfera === EsferaTipo.ESTADUAL;

    $scope.showErro = false;
    $scope.messagemDeErro = NORMA_EMPTY;

    $scope.tipo = -1;
    $scope.numero = "";
    $scope.index = 0;

    $scope.loading = false;

    var tipo = -1;

    $scope.buscar = function(){
       
        $scope.messagemDeErro = NORMA_EMPTY;
        $scope.showErro = false;

        tipo = $scope.tipo;
        $scope.index = 0;

        if ($routeParams.tipoEsfera === EsferaTipo.FEDERAL){
            buscarNormasFederais();        
        } else if ($routeParams.tipoEsfera === EsferaTipo.ESTADUAL){
            buscarNormasEstaduais();        
        } else if ($routeParams.tipoEsfera === EsferaTipo.MUNICIPAL){

        } else {
            console.log("Norma inválida");
        }
    };

    function buscarNormasFederais (){
        $scope.anos = [];
        $scope.mostrarBack = false;
        $scope.loading = true;

        if ($scope.tipo === -1 && $scope.numero === ""){
          
            $scope.mostrarAnos = true;
            $scope.mostrarNormas = false;
            $scope.mostrarPaginacao = false;

            ServicoNormas.getNormasFederaisAnos(function(data, status){
                if (status === HttpStatus.OK){
                    if (data !== undefined && data.length > 0){
                        preparaTabelasDeAnos(data);
                        $scope.loading = false;
                    } else{
                        $scope.loading = false;
                        $scope.mostrarAnos = false;
                        $scope.showErro = true;
                    }
                } else {
                    $scope.loading = false;
                    $scope.mostrarAnos = false;
                    $scope.showErro = true;
                }
            }, function(data, status){
                $scope.loading = false;
                $scope.mostrarAnos = false;
                $scope.showErro = true;
            });

        } else {
            
            $scope.normas = [];
            var limite = 50, inicio = 0;
            var data = {
                tipo: $scope.tipo,
                numero: $scope.numero,
                limite: limite,
                inicio: inicio
            };

            $scope.mostrarAnos = false;
            $scope.mostrarNormas = true;
            $scope.mostrarPaginacao = false;

            ServicoNormas.getNormasFederais(data, function(data, status){
                if (status === HttpStatus.OK){
                    if (data !== undefined && 
                        data.quantidade !== undefined && 
                        data.normas !== undefined && 
                        data.normas.length > 0){

                        preparaNormas(data);

                        $scope.loading = false;
                    
                    } else {
                        $scope.loading = false;
                        $scope.mostrarNormas = false;
                        $scope.showErro = true;
                    }
                } else {
                    $scope.loading = false;
                    $scope.mostrarNormas = false;
                    $scope.showErro = true;    
                }
            }, function(data, status){
                $scope.loading = false;
                $scope.mostrarNormas = false;
                $scope.showErro = true;
            });

        }
    };

    function buscarNormasEstaduais (){
        if ($scope.estado === undefined || $scope.estado.identificador < 0){

            $scope.messagemDeErro = CHOOSE_STATE;
            $scope.showErro = true;

            return;
        }    

        $scope.loading = true;
        $scope.anos = [];

        if ($scope.tipo === -1 && $scope.numero === ""){

            $scope.mostrarBack = false;
            $scope.mostrarAnos = true;
            $scope.mostrarNormas = false;
            $scope.mostrarPaginacao = false;

            var data = {
                estado: $scope.estado.identificador
            };

            ServicoNormas.getNormasEstaduaisAnos(data, function(data, status){
                if (status === HttpStatus.OK){
                    if (data !== undefined && data.length > 0){
                        preparaTabelasDeAnos(data);
                        $scope.loading = false;
                    } else{
                        $scope.loading = false;
                        $scope.mostrarAnos = false;
                        $scope.showErro = true;
                    }
                } else {
                    $scope.loading = false;
                    $scope.mostrarAnos = false;
                    $scope.showErro = true;
                }
            }, function(data, status){
                $scope.loading = false;
                $scope.mostrarAnos = false;
                $scope.showErro = true;
            });

        } else {
            
            $scope.normas = [];
            var limite = 50, inicio = 0;
            var data = {
                estado: $scope.estado.identificador,
                tipo: $scope.tipo,
                numero: $scope.numero,
                limite: limite,
                inicio: inicio
            };

            $scope.mostrarBack = false;
            $scope.mostrarAnos = false;
            $scope.mostrarNormas = true;
            $scope.mostrarPaginacao = false;

            ServicoNormas.getNormasEstaduais(data, function(data, status){
                if (status === HttpStatus.OK){
                    if (data !== undefined && 
                        data.quantidade !== undefined && 
                        data.normas !== undefined && 
                        data.normas.length > 0){

                        preparaNormas(data);

                        $scope.loading = false;
                    
                    } else {
                        $scope.loading = false;
                        $scope.mostrarNormas = false;
                        $scope.showErro = true;
                    }
                } else {
                    $scope.loading = false;
                    $scope.mostrarNormas = false;
                    $scope.showErro = true;    
                }
            }, function(data, status){
                $scope.loading = false;
                $scope.mostrarNormas = false;
                $scope.showErro = true;
            });

        }
    };

    $scope.buscarPorPaginacao = function(offset){
        
        $scope.loading = true;
        $scope.showErro = false;
        $scope.normas = [];

        var data = {
            tipo: tipo,
            numero: undefined,
            limite: 50,
            inicio: offset
        };

        ServicoNormas.getNormasFederais(data, function(data, status){
            if (status === HttpStatus.OK){
                if (data !== undefined && 
                    data.normas !== undefined &&
                    data.normas.length > 0){
                    $scope.normas = data.normas;
                    $scope.loading = false;
                } else {
                    $scope.loading = false;
                    $scope.showErro = true;        
                }
            } else {
                $scope.loading = false;
                $scope.showErro = true;        
            }
        }, function(data, status){
            $scope.loading = false;
            $scope.showErro = true;
        });
    };

    function preparaNormas(data){
        if (data.quantidade > 50){
            
            $scope.paginas = [];
            var subPaginas = [];
            var paginas = Math.ceil(data.quantidade / 50);
            var inicio = 50;

            for (var i = 0; i < paginas; i++) {   
                
                inicio= i > 0 ?  inicio += 50 : 0;

                subPaginas.push(
                    {
                        numero: i + 1,
                        inicio: inicio
                    }
                );  

                if (subPaginas.length === 10){
                    $scope.paginas.push({subPaginas: subPaginas});
                    subPaginas = [];
                }

                if (i === paginas - 1){
                    $scope.paginas.push({subPaginas: subPaginas});
                    break;
                }   
            }  
            $scope.mostrarPaginacao = true;
        }
        $scope.normas = data.normas;
    };

    $scope.buscarNormasPorAno = function(ano){

        $scope.loading = true;
        $scope.showErro = false;
        $scope.normas = [];
        $scope.mostrarPaginacao = false;
        $scope.mostrarBack = false;
        $scope.mostrarAnos = false;
        $scope.mostrarNormas = true;

        if ($routeParams.tipoEsfera === EsferaTipo.FEDERAL){
            
            var data = {
                ano: ano
            };

            ServicoNormas.getNormasFederaisPorAno(data, function(data, status){
                if (status == HttpStatus.OK){
                    if (data !==  undefined && data.length > 0){
                        $scope.normas = data;
                        $scope.mostrarBack = true;
                        $scope.loading = false;
                    } else{
                        $scope.mostrarBack = false;
                        $scope.showErro = true;
                        $scope.loading = false;
                    }
                } else {
                    $scope.mostrarBack = false;
                    $scope.showErro = true;
                    $scope.loading = false;
                }
            }, function(data, status){
                $scope.mostrarBack = false;
                $scope.showErro = true;
            });

        } else if ($routeParams.tipoEsfera === EsferaTipo.ESTADUAL){
            
            var data = {
                estado: $scope.estado.identificador,
                ano: ano
            };
            
            ServicoNormas.getNormasEstaduaisPorAno(data, function(data, status){
                if (status == HttpStatus.OK){
                    if (data !== undefined && data.length > 0){
                        $scope.normas = data;
                        $scope.mostrarBack = true;
                        $scope.loading = false;
                    } else{
                        $scope.mostrarBack = false;
                        $scope.showErro = true;
                        $scope.loading = false;
                    }
                } else {
                    $scope.loading = false;
                    $scope.mostrarBack = false;
                    $scope.showErro = true;
                }
            }, function(data, status){
                $scope.loading = false;
                $scope.mostrarBack = false;
                $scope.showErro = true;
            });

        } else if ($routeParams.tipoEsfera === EsferaTipo.MUNICIPAL){

        } else{
            console.log("Norma inválida");
        }
        
    };
   
    function preparaTabelasDeAnos (data){
        var colunas = [];
        for (var i = 0; i < data.length; i++) {
           
            if (colunas.length < 5){
                colunas.push(data[i].ano); 
            }

            if (colunas.length === 5 || i === data.length -1){
                $scope.anos.push(
                    {
                        colunas: colunas  
                    }
                );  
                colunas = [];
            } 
        }
    };


    //pagination behavior
    $scope.paginaAnterior = function(){
        if ($scope.index > 0){
            $scope.index -= 1;
        }    
    };

    $scope.proximaPagina = function(){
        if ($scope.index < $scope.paginas.length - 1){
            $scope.index += 1;
        }    
    };

    $scope.abrirDetalhe = function(link){
       $rootScope.url = link;
       $location.path('/detalhe');
    };

    $scope.buscarEstados = function(){
        
        if ($routeParams.tipoEsfera === EsferaTipo.ESTADUAL){

            $scope.loading = true;
            $scope.showErro = false;
            $scope.estados = [];
            
            ServicoNormas.getEstados(function(data, status){
                if (status == HttpStatus.OK){
                    if (data.length > 0){
                        $scope.estados = data;
                        $scope.loading = false;
                    } else{
                        $scope.loading = false;
                        console.log("Erro");
                    }
                } else {
                    $scope.loading = false;
                }
            }, function(data, status){
                $scope.loading = false;
                console.log("Erro");
            });
        }
    };

});

