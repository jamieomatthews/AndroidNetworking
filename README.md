AndroidNetworkKit
=================

Android Networking Kit is a framework for routing simple network requests with minimal code for Android OS.  It provides an easy way to integrate your activity with powerful network requests, while keeping your activities clean and readable.  

The sample MainActivity gives a simple example of using the twitter API to route a request, parse, and display it.

##Setting up your activity##
First, you must override the ANTask and ANHandler in your activity.

	public class GetData extends ANTask{
		public GetData(String URL, String action, List<NameValuePair> params,
				String type, Handler mainUIHandler, Context context) {
			super(URL, action, params, type, mainUIHandler, context);
		}

		@Override
		protected Object decode(String jsondata) throws JSONException {
			//decode json data here
		}
	}
	ANHandler asyncHandler = new ANHandler()
	{
		@Override
		public void resultOK(Message msg) 
		{
			//get your returned object here
			Object ob = msg.obj;
		}
		@Override
		public void resultFailed(Message msg) {
			//Show some error message
		}
	};
    
##Making the request##
	public ANTask(String URL, String action, List<NameValuePair> params, String type, Handler mainUIHandler, Context context)
To make a request, you must sepecify the following options:
* __URL__ - (String) The url to route to.  The base url is set in the ANTask class
* __Action__ - (String) GET or POST
* __Name Value Pairs__ - (BasicNameValuePair) Used for both get and post.  For a post, these are form encoded.  For get, they are URL encoded and appended to the get string
* __Type__ - (String) This is passed back by the handler for differentiating between requests.  Passed back as msg.what
* __Handler__ - (ANHandler) Your ANHandler created above
* __Context__ - (Context) Your activities context

##Basic Auth##

For basic auth support, use the ANUtils class to set a username and password in preferences, and it will be used for every request.
	public void setLogin(String username, String password)

##Notes##
* The decode method exposes the parsing method in your main activity, so you can have the applicable code right in your activity, while hiding all the ugly networking code
* Return any Object from the decode method, and then recieve it in the resultOk by calling msg.obj;
* If null is returned from decode, resultFailed will be called
* For json data parsing, check out google GSON for easy parsing
