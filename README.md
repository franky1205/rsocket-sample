# rsocket-sample
This is the sample codes for establishing different types of RSocket server with corresponding RSocket client.<br />
All the sample codes use the API from RSocket version 1.1.0 directly is listed as below.<br /><br />
RSocket Servers established with RSocket APIs under the package of "org.franwork.studio.rsocket": <br />
<li />RSocketFireAndForgetServerImpl<br />
<li />RSocketReqResServerImpl<br />
<li />RSocketReqStreamServerImpl<br />
<li />RSocketRequestChannelServerImpl<br /><br />

RSocket Clients implemented with RSocket APIs under the package of "org.franwork.studio.rsocket.client":<br />
<li />FireAndForgetClientMain<br />
<li />ReqChannelClientMain<br />
<li />ReqResClientMain<br />
<li />ReqStreamClientMain<br /><br />

All the RSocket servers and clients will not automatically terminated unless timeout.
