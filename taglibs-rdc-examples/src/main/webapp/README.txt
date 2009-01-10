***************************************************************************
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
****************************************************************************
The examples in this directory (with the exception of getcart.jsp) are
meant to be accessed using a browser that supports VoiceXML 2.0 in
order to interact with the VUIs. Accessing with a web/HTML browser
will only allow you to inspect the VoiceXML markup generated.

This directory holds <component-name>-test.jsp files -- one for
each component in the RDC framework. Documentation about the tags
available and the list of attributes supported by each is
available in rdc-doc.war that comes with this distribution.

Examples using the RDC group container are also available as
group-*.jsp or rulebased-*.jsp

There are two sample applications:
1) Mortgage application - This application implements a fictitious
   scenario for getting quotes/setting up a home mortgage. There is no
   backend access and the values returned are placeholders.
   For more information, view the README in ./mortgage-app

2) MusicStore application - This application implements a VUI for
   a music store application, allowing users to browse through the
   music albums available on Amazon, assemble a shopping cart and then
   retrieve the shopping cart on the Amazon web site via ./getcart.jsp
   This application uses the Amazon Web Services, and hence, a real
   backend.
   For more information, view the README in ./musicstore-app
