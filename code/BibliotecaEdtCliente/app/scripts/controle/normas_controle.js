'use strict';

app.controller('NormasCtrl', function ($scope, $rootScope, $location, $routeParams, ServicoNormas) {

    $scope.mostrarBack = false;
    $scope.mostrarAnos = false;
    $scope.mostrarNormas = false;
    $scope.mostrarPaginacao = false;
    $scope.mostrarEstados = $routeParams.tipoEsfera === EsferaTipo.ESTADUAL;

    $scope.tipo = -1;
    $scope.numero = "";
    $scope.index = 0;

    var tipo = -1;

    $scope.buscar = function(){
       
        tipo = $scope.tipo;
        $scope.index = 0;

        if ($routeParams.tipoEsfera === EsferaTipo.FEDERAL){
            buscarNormasFederais();        
        } else if ($routeParams.tipoEsfera === EsferaTipo.ESTADUAL){
            buscarNormasEstaduais();        
        } else if ($routeParams.tipoEsfera === EsferaTipo.MUNICIPAL){

        } else{
            console.log("Norma inválida");
        }
    };

    function buscarNormasFederais (){
        $scope.anos = [];
        if ($scope.tipo === -1 && $scope.numero === ""){
          
            $scope.mostrarBack = false;
            $scope.mostrarAnos = true;
            $scope.mostrarNormas = false;
            $scope.mostrarPaginacao = false;

            ServicoNormas.getNormasFederaisAnos(function(data, status){
                if (status === HttpStatus.OK){
                    if (data.length > 0){
                        preparaTabelasDeAnos(data);
                    } else{
                        console.log("Erro");
                    }
                }
            }, function(data, status){
                console.log("Erro");
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

            $scope.mostrarBack = false;
            $scope.mostrarAnos = false;
            $scope.mostrarNormas = true;
            $scope.mostrarPaginacao = false;

            ServicoNormas.getNormasFederais(data, function(data, status){
                if (status === HttpStatus.OK){
                    preparaNormas(data);
                }
            }, function(data, status){
                console.log("Erro");
            });

        }
    };

    function buscarNormasEstaduais (){
        if ($scope.estado === undefined || $scope.estado.identificador < 0){
            console.log("Erro");
            return;
        }    

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
                    if (data.length > 0){
                        preparaTabelasDeAnos(data);
                    } else{
                        console.log("Erro");
                    }
                }
            }, function(data, status){
                console.log("Erro");
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
                    preparaNormas(data);
                }
            }, function(data, status){
                console.log("Erro");
            });

        }
    };

    $scope.buscarPorPaginacao = function(offset){
        $scope.normas = [];
        var data = {
            tipo: tipo,
            numero: undefined,
            limite: 50,
            inicio: offset
        };

        ServicoNormas.getNormasFederais(data, function(data, status){
            if (status === HttpStatus.OK){
                $scope.normas = data.normas;
            }
        }, function(data, status){
            console.log("Erro");
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
        $scope.normas = [];
        $scope.mostrarPaginacao = false;
        $scope.mostrarBack = true;
        $scope.mostrarAnos = false;
        $scope.mostrarNormas = true;

        if ($routeParams.tipoEsfera === EsferaTipo.FEDERAL){
            
            var data = {
                ano: ano
            };

            ServicoNormas.getNormasFederaisPorAno(data, function(data, status){
                if (status == HttpStatus.OK){
                    if (data.length > 0){
                        $scope.normas = data;
                    } else{
                        console.log("Erro");
                    }
                }
            }, function(data, status){
                console.log("Erro");
            });

        } else if ($routeParams.tipoEsfera === EsferaTipo.ESTADUAL){
            
            var data = {
                estado: $scope.estado.identificador,
                ano: ano
            };
            
            ServicoNormas.getNormasEstaduaisPorAno(data, function(data, status){
                if (status == HttpStatus.OK){
                    if (data.length > 0){
                        $scope.normas = data;
                    } else{
                        console.log("Erro");
                    }
                }
            }, function(data, status){
                console.log("Erro");
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

            $scope.estados = [];
            
            ServicoNormas.getEstados(function(data, status){
                if (status == HttpStatus.OK){
                    if (data.length > 0){
                        $scope.estados = data;
                    } else{
                        console.log("Erro");
                    }
                }
            }, function(data, status){
                console.log("Erro");
            });
        }
    };

});

