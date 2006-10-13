/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Deserialize value from return namelist into an object

function deserializeReturnValue( s )
{
    return new RDCValue( s );
}

function RDCValue( s ) 
{
    var arr = s.split(";");
    var len = arr.length;
    this.serialization = s;
    this.avps = new Array( len );
    var idx = 0;
    for (var i = 0; i < len; i++) {
        if (arr[i].length == 0) {
            continue;
        }
        var avp = arr[i].split("=");
        var val = "";
        if (avp.length < 2) {
            continue;
        } else if (avp.length == 2) {
            val = avp[1];
        } else {
            for (var j = 1; j < avp.length; j++) {
                val += avp[j];
            }
        }
        this.avps[idx++] = new AVP( avp[0], val );
    }
}

RDCValue.prototype.getValue = function( ) 
{
    return this.serialization;
}

RDCValue.prototype.get = function( attr ) 
{
    for (var i = 0; i < this.avps.length; i++) {
        var avp = this.avps[i];
        if (avp == undefined) {
            break;
        }
        if (avp.attribute == attr) {
            return avp.value;
        }
    }
    return undefined;
}

function AVP( a, v )
{
    this.attribute = a;
    this.value = v;
}

