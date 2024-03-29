package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.client.stackoverflow.StackOverflowWebClient;
import edu.java.configuration.ClientConfiguration;
import edu.java.configuration.RetryConfiguration;
import edu.java.dto.stackoverflow.StackOverflowCommentsResponse;
import edu.java.dto.stackoverflow.StackOverflowItem;
import edu.java.dto.stackoverflow.StackOverflowResponse;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpPort = 9876)
@SpringBootTest(
    classes = {RetryConfiguration.class},
    properties = {
        "app.scheduler.enable=false",
        "app.scheduler.interval=0",
        "app.scheduler.force-check-delay=0",
    }
)
@ContextConfiguration(initializers = StackOverflowClientTest.Initializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StackOverflowClientTest {
    private static final String QUESTION_TITLE = "Different date in local and the server java date time";
    private static final Long QUESTION_ID = 77971313L;
    private static final String URL_SCENARIO = "/questions/" + QUESTION_ID + "?site=stackoverflow";
    private static final String URL_COMMENTS_SCENARIO = "/questions/" + QUESTION_ID + "/comments?site=stackoverflow";
    private static final OffsetDateTime LAST_MODIFIED = OffsetDateTime.of(2024, 2, 18, 23, 59, 59, 0, ZoneOffset.UTC);
    private static final int RETRY_COUNT = 5;
    private final StackOverflowClient client;

    @Autowired
    public StackOverflowClientTest(Retry retry) {
        this.client = new StackOverflowWebClient("http://localhost:9876", retry);
    }

    public void setupStubSuccess() {
        stubFor(get(urlEqualTo(URL_SCENARIO))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody(
                    """
                        {
                        "items": [
                            {
                              "tags": [
                                "java"
                              ],
                              "owner": {
                                "account_id": 31692
                              },
                              "is_answered": true,
                              "last_activity_date": %d,
                              "title": "%s"
                            }
                          ]
                        }
                        """.formatted(LAST_MODIFIED.toEpochSecond(), QUESTION_TITLE))
            ));
        stubFor(get(urlEqualTo(URL_COMMENTS_SCENARIO))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody(
                    """
                        {
                             "items": [
                                 {
                                     "owner": {
                                         "account_id": 78868,
                                         "reputation": 345803,
                                         "user_id": 224132,
                                         "user_type": "registered",
                                         "accept_rate": 83,
                                         "profile_image": "https://i.stack.imgur.com/N4ivW.png?s=256&g=1",
                                         "display_name": "Peter Cordes",
                                         "link": "https://stackoverflow.com/users/224132/peter-cordes"
                                     },
                                     "reply_to_user": {
                                         "account_id": 321401,
                                         "reputation": 18582,
                                         "user_id": 640205,
                                         "user_type": "registered",
                                         "accept_rate": 94,
                                         "profile_image": "https://www.gravatar.com/avatar/54b992871265b6997dd53e13bb6fdc67?s=256&d=identicon&r=PG&f=y&so-version=2",
                                         "display_name": "JustBeingHelpful",
                                         "link": "https://stackoverflow.com/users/640205/justbeinghelpful"
                                     },
                                     "edited": false,
                                     "score": 0,
                                     "creation_date": 1708378290,
                                     "post_id": 11227809,
                                     "comment_id": 137550814,
                                     "content_license": "CC BY-SA 4.0"
                                 }
                             ],
                             "has_more": false,
                             "quota_max": 300,
                             "quota_remaining": 234
                        }
                        """)
            ));
    }

    public void setupStubServerError() {
        stubFor(get(urlEqualTo(URL_SCENARIO))
            .willReturn(aResponse()
                .withStatus(500)
            ));
        stubFor(get(urlEqualTo(URL_COMMENTS_SCENARIO))
            .willReturn(aResponse()
                .withStatus(500)
            ));
    }

    public void setupStubRetry() {
        for (int i = 1; i <= RETRY_COUNT - 1; i++) {
            stubFor(get(urlEqualTo(URL_SCENARIO))
                        .inScenario("RETRY")
                        .whenScenarioStateIs(i == 1 ? Scenario.STARTED : String.valueOf(i))
                        .willReturn(aResponse().withStatus(429))
                        .willSetStateTo(String.valueOf(i + 1))
            );
        }
        stubFor(get(urlEqualTo(URL_SCENARIO))
                    .inScenario("RETRY")
                    .whenScenarioStateIs(String.valueOf(RETRY_COUNT))
                    .willReturn(aResponse().withStatus(200).withBody(""))
        );
    }


    @Test
    public void testSuccess() {
        setupStubSuccess();
        StackOverflowResponse response = client.fetchQuestion(QUESTION_ID);
        StackOverflowItem responseItem = response.items()[0];
        assertThat(responseItem.title()).isEqualTo(QUESTION_TITLE);
        assertThat(responseItem.lastModified()).isEqualTo(LAST_MODIFIED);
    }

    @Test
    public void testNoResponse() {
        setupStubServerError();
        assertThrows(WebClientResponseException.class, () -> client.fetchQuestion(QUESTION_ID));

    }

    @Test
    public void testSuccessCommentsFetch() {
        setupStubSuccess();
        StackOverflowCommentsResponse response = client.fetchComments(QUESTION_ID);
        assertThat(response).isNotNull();
        assertThat(response.comments()).isNotNull();
        assertThat(response.comments().length).isEqualTo(1);
    }

    @Test
    public void testNoResponseCommentsFetch() {
        setupStubServerError();
        assertThrows(WebClientResponseException.class, () -> client.fetchComments(QUESTION_ID));
    }
    @Test
    public void testRetry() {
        setupStubRetry();

        assertDoesNotThrow(() ->  client.fetchQuestion(QUESTION_ID));
        verify(RETRY_COUNT, getRequestedFor(urlEqualTo(URL_SCENARIO)));
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of("app.retry-specification.back-off-type=exponential").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.max-attempts=" + RETRY_COUNT).applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.delay=500").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.jitter=0.5").applyTo(applicationContext);
            TestPropertyValues.of("app.retry-specification.codes=429,501,502,503,504,505").applyTo(applicationContext);
        }
    }
}
