/**
 * Created by zulk on 23.11.16.
 */
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.lucene.index.Terms;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregatorBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static org.apache.lucene.index.TwoPhaseCommitTool.execute;
import static org.elasticsearch.action.search.SearchType.DFS_QUERY_THEN_FETCH;


/**
 * Created by zulk on 23.11.16.
 */
public class Go {

    static String q1 = "{\"match_all\": {}}";
    static String q = "{\n" +
            "  \"aggs\": {\n" +
            "    \"user\": {\n" +
            "      \"terms\": {\n" +
            "        \"field\": \"user.keyword\"\n" +
            "      },\n" +
            "      \"aggs\": {\n" +
            "        \"categories\": {\n" +
            "          \"terms\": {\n" +
            "            \"field\": \"category.keyword\"\n" +
            "          },\n" +
            "          \"aggs\": {\n" +
            "            \"qty\": {\n" +
            "              \"sum\": {\n" +
            "                \"field\": \"qty\"\n" +
            "              }\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
//        Trait tr = new Kaa().tr();
//        System.out.println(tr.s());
//        System.out.println(tr.traitt());
//        tr.mm().foreach(f -> {
//            System.out.println(f);
//            return null;
//        });
        elk();
//        jest();
    }

    private static void elk() throws IOException, ExecutionException, InterruptedException {
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));


        SearchRequestBuilder searchRequestBuilder = new SearchRequestBuilder(client, SearchAction.INSTANCE);

        QueryBuilder qb = QueryBuilders.wrapperQuery(q1);

        QueryStringQueryBuilder filter = QueryBuilders.queryStringQuery("lastname:abbott");
//        System.out.println(filter);
//        QueryBuilders.matchQuery(qb).operator(Operator.OR).fuzziness(Fuzziness.TWO);
//        AggregationBuilders.filters("aaa",AggregationBuilders.filter("gender",QueryBuilders.q))
//        TermsAggregationBuilder field = AggregationBuilders.terms("termsss").field("lastname").subAggregation(

//        );
//        SearchRequestBuilder pivot3q = searchRequestBuilder.setQuery(filter).addAggregation(field).setIndices("bank");
//        System.out.println(pivot3q);

//        SearchResponse pivot3 = pivot3q.execute().actionGet();
        SearchResponse pivot3 = client.prepareSearch("pivot3").setQuery(qb).execute().actionGet();
        System.out.println("DDDDDDDDDDDDDd---");
        Stream.of(pivot3.getHits().hits()).forEach(h -> System.out.println(h.getSourceAsString()));

        StringTerms termsss = pivot3.getAggregations().get("termsss");
        System.out.println(termsss.getBuckets().get(0).getDocCount());
    }

    public static void jest() throws IOException {
        JestClientFactory factory = new JestClientFactory();
        HttpClientConfig httpClientConfig = new HttpClientConfig
                .Builder("http://localhost:9200")
                .build();
        factory.setHttpClientConfig(httpClientConfig);
        JestClient client = factory.getObject();

        String queryString ="{\"query\":{\"match_all\": {}},\"aggs\":{\"avg1\":{\"avg\":{\"field\":\"age\"} } }}";

        Search.Builder searchBuilder = new Search.Builder(q)
                .addIndex("pivot3");


        searchBuilder.setParameter("a","b");
        System.out.println(searchBuilder.build());
        SearchResult response = client.execute(searchBuilder.build());

        System.out.println(response.getJsonString());

        client.shutdownClient();
    }
}
