package com.learning.twitterfeed.twitterFeedExample;

import java.util.ArrayList;
import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.Query.Unit;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class App 
{
	private static String CONSUMERKEY="kEOtSXvW9obUyOtAVa5qF2HOB";
	private static String CONSUMERKEY_SECRET="Nyki4IUD3pVqRR5dJEHFKErWNv1LJpdVVYUlDwHcerRhfjyaXm";
	private static String ACCESSTOKEN="1081024273245003776-WcqxxWOLOTU6Oi2MfMAPNXzJkCsGoM";
	private static String ACCESSTOKEN_SECRET="p351nNGsrWM1e7tPJefSRvRRWDOE8GSiTEMOJ5J3IEkfQ";
	
    public static void main( String[] args )
    {
    	List<Status> responseStatuses= new ArrayList<Status>();
    	ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(CONSUMERKEY)
    	.setOAuthConsumerSecret(CONSUMERKEY_SECRET)
    	.setOAuthAccessToken(ACCESSTOKEN)
    	.setOAuthAccessTokenSecret(ACCESSTOKEN_SECRET)
    	.setDebugEnabled(true)
    	.setJSONStoreEnabled(true)
    	.setTweetModeExtended(true);
    	Query query = new Query("Job AND {bigdata OR apache spark OR hive}");
    	query.setCount(100);
    	query.setLocale("en-US");
    	
//    	query.setGeoCode(new GeoLocation(42.3601, 71.0589), 500, Unit.mi);
    	Twitter twitter = new TwitterFactory(cb.build()).getInstance();
    	
    	try {
//			responseStatuses=twitter.getHomeTimeline();
    		responseStatuses = twitter.search(query).getTweets();
			for (Status status: responseStatuses) {
				String message = status.isRetweet()? status.getRetweetedStatus().getText(): status.getText();
				System.out.println(status.getUser().getName() + "  tweeted at "+ status.getCreatedAt()+ " Message: "+ message);
			}
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    		
    			
    }
}
