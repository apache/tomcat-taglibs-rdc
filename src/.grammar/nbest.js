/*
 *  Copyright 2004 The Apache Software Foundation.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

// Serialize application.lastresult$ array
// if application.lastresult$[m].interpretation is an object use serializeObject
// to convert it to a comma delimited attribute=value string

function serializeNBest()
{
    var n, out = "";
    for (n = 0; n < application.lastresult$.length; n++) {
        if (n > 0) {
            out += ";";
        }
        out += application.lastresult$[n].confidence + ";" 
            + application.lastresult$[n].utterance + ";" 
            + serializeObject( "", application.lastresult$[n].interpretation);
    }
    return escape(out);
}               
                
function serializeObject ( s, o )
{
    var prop, name, out = "";
    if ( typeof ( o ) == "object" ) {
        for ( prop in o ) {
            name = s + prop;
            if (out != "") {
                out += ",";
            }             
            if ( typeof ( o [ prop ] ) == "object" ) {
                out += serializeObject( name+".", o [ prop ] );
            } else {
                out += name + "=" + o [ prop ];
            }
        }
    } else {
        out += "=" + o;
    }
    return out;
}
