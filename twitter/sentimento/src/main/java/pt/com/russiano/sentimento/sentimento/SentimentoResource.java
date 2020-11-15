package pt.com.russiano.sentimento.sentimento;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/sentimento")
public class SentimentoResource {

    @Value("${oauth.consumerKey:}")
    private String consumerKey;

    @Value("${oauth.consumerSecret:}")
    private String consumerSecret;

    @Value("${oauth.accessToken:}")
    private String accessToken;

    @Value("${oauth.accessTokenSecret:}")
    private String accessTokenSecret;

    @Value("${google_key:}")
    private String google_key;

    @GetMapping("/getKeyGoogle")
    public String getMovie() {
        return google_key;
    }

    @GetMapping("/getTwitter/{query}")
    public String getTwitter(@PathVariable String query) {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        String retorno = "";
        try {
            Query queryT = new Query(query);
            queryT.setCount(5);
            QueryResult result;
            int count = 0;
            do {
                if (count >= 3)
                    break;
                count++;

                result = twitter.search(queryT);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                    retorno += tweet.getText() + "|";
                }
            } while ((queryT = result.nextQuery()) != null);

        } catch (TwitterException te) {
            return "Failed to search tweets: " + te.getMessage();
        }
        return retorno;

    }


}
