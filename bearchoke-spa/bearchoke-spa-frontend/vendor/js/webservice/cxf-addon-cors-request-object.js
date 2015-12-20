/**
 * MailArchiver is an application that provides services for storing and managing e-mail messages through a Web Services SOAP interface.
 * Copyright (C) 2012  Marcio Andre Scholl Levien and Fernando Alberto Reuter Wendt and Jose Ronaldo Nogueira Fonseca Junior
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/******************************************************************************\
*
*  This product was developed by
*
*        SERVIÇO FEDERAL DE PROCESSAMENTO DE DADOS (SERPRO),
*
*  a government company established under Brazilian law (5.615/70),
*  at Department of Development of Porto Alegre.
*
\******************************************************************************/

  function cxf_cors_request_object(){
    this.clientid = null;
    this.clientversion = null;
    this.clientagent = null;
    this.clientcompatible = false;
    this.type = null;
    this.handler = null;
    this.data = null;
    this.timeout = 10000;
  }

  //Initialize object method
  cxf_cors_request_object.prototype.init = function(){
    try{
        this.getClient();
        this.validateClient();
        if(this.clientcompatible){
          this.setCoreHandler();
          //window.alert('this.handler native type = ' + typeof(this.handler) + '\nthis.type = ' + this.type);
          //this.setHandlers(callbackOK, callbackFailure, arrayargs);
        }
        else
          throw "00-CLIENT_INCOMPATIBLE";
    }
    catch(e){
      window.alert('cxf_cors_request_object "init" exception caught: ' + ((e.message)?e.message:e));
    }
  }

  //Get client browser data for CORS/XHR level2 support
  cxf_cors_request_object.prototype.getClient = function(){
    try{
      this.clientagent = navigator.userAgent;
      //IE match
      if (/MSIE (\d+\.\d+);/.test(this.clientagent)){
        this.clientid = 'IE';
        var ieversion=new Number(RegExp.$1)
        this.clientversion = ieversion;
      }
      //Firefox match
      else{
        if (/Firefox[\/\s](\d+\.\d+)/.test(this.clientagent)){
          this.clientid = 'FF';
          var ffversion=new Number(RegExp.$1)
          this.clientversion = ffversion;
        }
        //Chrome match
        else{
          if (/Chrome[\/\s](\d+\.\d+)/.test(this.clientagent)){
            this.clientid = 'CR';
            var crversion=new Number(RegExp.$1)
            this.clientversion = crversion;
          }
          //Safari match
          else{
            if (/Safari[\/\s](\d+\.\d+)/.test(this.clientagent)){
              this.clientid = 'SF';
              var sfversion=new Number(RegExp.$1)
              this.clientversion = sfversion;
            }
            //Other browser with CORS2/xdomain support, should be added here
            else
              throw "01-GET_CLIENT_FAIL";
          }
        }
      }
    }
    catch(e){
      window.alert('cxf_cors_request_object "getClient" exception caught = ' + ((e.message)?e.message:e));
    }
  }

  //Validate client browser support method
  cxf_cors_request_object.prototype.validateClient = function(){
    try{
      switch(this.clientid){
        case 'IE':
          if (this.clientversion>=8)
            this.clientcompatible = true; //only ie 8/9 supports cors2/xdomain
          else
            this.clientcompatible = false;
          break;
        case 'FF':
          if(this.clientversion>=3)
            this.clientcompatible = true; //only ff 3.5/4/5 supports cors2/xdomain
          else
            this.clientcompatible = false;
          break;
        case 'CR':
          if(this.clientversion>=13)
            this.clientcompatible = true; //only cr 13/14 supports cors2/xdomain
          else
            this.clientcompatible = false;
          break;
        case 'SF':
          if(this.clientversion>=5)
            this.clientcompatible = true; //only sf 5 supports cors2/xdomain
          else
            this.clientcompatible = false;
          break;
        default:
          this.clientcompatible = false;
          throw "02-CLIENT_VALIDATE_FAIL";
      }
    }
    catch(e){
        window.alert('cxf_cors_request_object "validateClient" exception caught: ' + ((e.message)?e.message:e));
    }
  }

  //Sets core request handler object
  cxf_cors_request_object.prototype.setCoreHandler = function(){
    try{
      //MS IE-8.0, IE-9.0 & compatibles
      if (window.XDomainRequest){
        var xdrhandler = new XDRAdapter();
        this.handler = xdrhandler.getHandler();
        this.type = 'MS-XDR';
      }
      else
        throw "03-SET_COREHANDLER_MS_FAIL";
    }
    catch (e){
      try{
        //Mozilla 3.5 >=  and derivates api compatibles
        if (window.XMLHttpRequest){
            var xhr2 = new XMLHttpRequest();
            if ("withCredentials" in xhr2){//XMLHttpRequest Level 2, cors support on
                this.handler = xhr2;
                this.type='MZ-XHR';
            }
            else{
                this.type = 'MZ-LV1';//XMLHttpRequest Level 1 object, no cors support
                this.handler = null;
            }
        }
        //Ops: no CORS suported on client browser
        else
          throw "03-SET_COREHANDLER_MZ_FAIL";
      }
      catch (e){
        window.alert('\nO seu navegador nao esta configurado para utilizar os recursos Ajax da plataforma. Habilite-o ou atualize seu navegador.\nErro = ' + ((e.message)?e.message:e));
        this.handler = null;
        this.type = null;
      }
    }
  }
