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
