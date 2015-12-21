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

<!--
  function XDRAdapter(){
    //Core properties
    this.handler = new XDomainRequest();

    /*
    //Common core events
    this.handler.open
    this.handler.abort
    this.handler.send
    this.handler.onerror

    //Exclusive events
    this.handler.onload
    this.handler.ontimeout
    this.handler.onprogress
    */

    //Added events (CXF-Utils missing ones)
    this.handler.getAllResponseHeaders = this.foo;
    this.handler.getResponseHeader = this.foo;
    this.handler.overrideMimeType = this.foo;
    this.handler.sendAsBinary = this.foo;
    this.handler.setRequestHeader = this.foo;
    this.handler.onreadystatechange = this.handler.onload;
  }

  XDRAdapter.prototype.foo = function(){
    return(null);
  }

  XDRAdapter.prototype.getHandler = function(){
    return(this.handler);
  }
-->