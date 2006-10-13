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
This directory contains a multi-channel Music Store application.

This application can be used as a Voice -or- Graphical User Interface
for a music store application, allowing users to browse through the music
albums available on Amazon, assemble a shopping cart and then retrieve 
the shopping cart on the Amazon web site.
This application uses the Amazon Web Services, and hence, a real backend.

The two channels are:
1) The Voice channel which uses RDC based JSPs [ in ./voice ]
The starting point for the voice channel is:
./voice/mainmenu.do

2) The GUI channel for small devices [ in ./gui ]
The starting point for the GUI channel is:
./gui/mainmenu.do

#####
YOU NEED A AMAZON WEB SERVICES SUBSCRIPTION ID 
TO TRY OUT THIS SAMPLE APPLICATION.

This was the URI for AWS when this application was authored:
http://www.amazon.com/gp/aws/landing.html
Click on "Register for AWS" on the top left.

ONCE YOU GET THE ID, UPDATE THE ../WEB-INF/web.xml
FILE SUCH THAT THE <context-param> with name
com.amazon.ecs.SubscriptionId
CONTAINS YOUR ID AS THE <param-value>
#####


This is the overall control flow:
1) Browse - Asks user to select the category and/or genre of the
   music the user is interested in. Categories include types such as
   Top Sellers, New Releases etc. Genres include Rock, Country, Dance
   etc.
2) Select / list similar - Once a list of top 10 results matching
   the query in (1) are presented to the user, the user can select
   one of these and carry out further interaction such as getting
   the album description and looking for similar music albums.
3) Add to cart - The user can add music albums of interest to the user's
   shopping cart. This is a shopping cart created at Amazon using the
   web services API. Steps (1) through (3) can be repeated as many times
   as needed.
4) Check out - On the voice side, the user is provided with cart ID.
   The user then uses a web/HTML browser to visit ../getcart.jsp,
   and uses the above cart ID in the displayed form to be redirected to
   the Amazon web site where the user can complete the transaction and buy
   the items added via the VUI to the user's shopping cart. On the GUI
   side, the user is simply redirected to the Amazon web site to complete
   the transaction.


   