<!-- 

VoiceXML 2.0 RDC FRAGMENT DTD (200410805)

-->

<!ENTITY % audio " #PCDATA | audio | value">

<!ENTITY % bargeintype "( speech | hotword )">

<!ENTITY % boolean "(true|false)">

<!ENTITY % duration "CDATA">

<!ENTITY % expression "CDATA">

<!ENTITY % integer "CDATA">

<!ENTITY % uri "CDATA">

<!ENTITY % event.handler.content "%audio; | prompt ">

<!ENTITY % cache.attrs "fetchhint	(prefetch|safe)	#IMPLIED

	fetchtimeout	%duration;	#IMPLIED

	maxage		%integer;	#IMPLIED

	maxstale	%integer;	#IMPLIED"

>

<!ENTITY % event.handler.attrs "count	%integer;	#IMPLIED

	cond		%expression;	#IMPLIED"

>

<!--================================ Prompts ==============================-->

<!-- definitions adapted from SSML 1.0 DTD -->

<!ENTITY % structure "p | s">

<!ENTITY % sentence-elements "break | emphasis | mark | phoneme | prosody | 

                              say-as | voice | sub">

<!ENTITY % allowed-within-sentence " %audio; | %sentence-elements; ">



<!-- Prompt is modelled on SSML 1.0 DTD speak element:

- addition of 'bargein', 'bargeintype', 'cond', 'count' and 'timeout' attributes

- removal of xmlns, xmlns:xsi, and xsi:schemaLocation attributes

- version attribute fixed as "1.0"

-->

<!ELEMENT prompt (%allowed-within-sentence; | %structure;)*>

<!ATTLIST prompt

	bargein %boolean; #IMPLIED

	bargeintype %bargeintype; #IMPLIED

	cond %expression; #IMPLIED

	count %integer; #IMPLIED

	xml:lang NMTOKEN #IMPLIED

	timeout %duration; #IMPLIED

	xml:base %uri; #IMPLIED

	version CDATA #FIXED "1.0"

>

<!--================================ Audio Output ==============================-->

<!-- definitions adapted from SSML 1.0 DTD -->

<!ELEMENT p (%allowed-within-sentence; | s)*>

<!ATTLIST p

	xml:lang NMTOKEN #IMPLIED

>

<!ELEMENT s (%allowed-within-sentence;)*>

<!ATTLIST s

	xml:lang NMTOKEN #IMPLIED

>

<!ELEMENT voice (%allowed-within-sentence; | %structure;)*>

<!ATTLIST voice

	xml:lang NMTOKEN #IMPLIED

	gender (male | female | neutral) #IMPLIED

	age %integer; #IMPLIED

	variant %integer; #IMPLIED

	name CDATA #IMPLIED

>

<!ELEMENT prosody (%allowed-within-sentence; | %structure;)*>

<!ATTLIST prosody

	pitch CDATA #IMPLIED

	contour CDATA #IMPLIED

	range CDATA #IMPLIED

	rate CDATA #IMPLIED

	duration %duration; #IMPLIED

	volume CDATA #IMPLIED

>

<!-- Changes to SSML 1.0 DTD audio element:

- src not obligatory, addition of 'expr' and caching attributes

-->

<!ELEMENT audio (%allowed-within-sentence; | %structure; | desc)*>

<!ATTLIST audio

	src %uri; #IMPLIED

	expr %expression; #IMPLIED

	%cache.attrs; 

>

<!ELEMENT desc (#PCDATA)>

<!ATTLIST desc

	xml:lang NMTOKEN #IMPLIED

>

<!ELEMENT emphasis (%allowed-within-sentence;)*>

<!ATTLIST emphasis

	level (strong | moderate | none | reduced) "moderate"

>

<!ELEMENT say-as (#PCDATA | value )*>

<!ATTLIST say-as

	interpret-as NMTOKEN #REQUIRED

	format NMTOKEN #IMPLIED

	detail NMTOKEN #IMPLIED

>

<!ELEMENT sub (#PCDATA)>

<!ATTLIST sub

	alias CDATA #REQUIRED

>

<!ELEMENT phoneme (#PCDATA)>

<!ATTLIST phoneme

	ph CDATA #REQUIRED

	alphabet CDATA #IMPLIED

>

<!ELEMENT break EMPTY>

<!ATTLIST break

	time CDATA #IMPLIED

	strength  (none | x-weak | weak  | medium | strong | x-strong) "medium"

>

<!ELEMENT mark EMPTY>

<!ATTLIST mark

	name CDATA #REQUIRED

>

<!ELEMENT value EMPTY>

<!ATTLIST value

	expr %expression; #REQUIRED

>

<!--================================== Events =============================-->

<!ELEMENT help (%event.handler.content;)* >

<!ATTLIST help

	%event.handler.attrs; 

>

<!ELEMENT noinput (%event.handler.content;)* >

<!ATTLIST noinput

	%event.handler.attrs; 

>

<!ELEMENT nomatch (%event.handler.content;)* >

<!ATTLIST nomatch

	%event.handler.attrs; 

>

<!--======== catch ==================== -->

<!ELEMENT catch (%event.handler.content;)*>

<!ATTLIST catch

        event NMTOKENS  #IMPLIED

        %event.handler.attrs; 

>



<!--============================ Property =================================-->



<!ELEMENT property EMPTY>

<!ATTLIST property

	name NMTOKEN #REQUIRED

	value CDATA #REQUIRED

>



