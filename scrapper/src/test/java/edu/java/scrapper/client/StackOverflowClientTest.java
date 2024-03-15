package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.dto.stackoverflow.StackOverflowCommentsResponse;
import edu.java.client.dto.stackoverflow.StackOverflowItem;
import edu.java.client.dto.stackoverflow.StackOverflowQuestionsResponse;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.client.stackoverflow.StackOverflowWebClient;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@WireMockTest(httpPort = 9876)
public class StackOverflowClientTest {
    private static final String QUESTION_TITLE = "Different date in local and the server java date time";
    private static final Long QUESTION_ID = 77971313L;
    private static final String URL_SCENARIO = "/questions/" + QUESTION_ID + "?site=stackoverflow";
    private static final String URL_COMMENTS_SCENARIO = "/questions/" + QUESTION_ID + "/comments?site=stackoverflow";
    private static final OffsetDateTime LAST_MODIFIED = OffsetDateTime.of(2024, 2, 18, 23, 59, 59, 0, ZoneOffset.UTC);

    private final StackOverflowClient client = new StackOverflowWebClient("http://localhost:9876");

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
                .withStatus(404)
            ));
        stubFor(get(urlEqualTo(URL_COMMENTS_SCENARIO))
            .willReturn(aResponse()
                .withStatus(404)
            ));
    }

    @Test
    public void testSuccess() {
        setupStubSuccess();
        StackOverflowQuestionsResponse response = client.fetchQuestion(QUESTION_ID);
        StackOverflowItem responseItem = response.items()[0];
        assertThat(responseItem.title()).isEqualTo(QUESTION_TITLE);
        assertThat(responseItem.lastModified()).isEqualTo(LAST_MODIFIED);
    }

    @Test
    public void testNoResponse() {
        setupStubServerError();
        StackOverflowQuestionsResponse response = client.fetchQuestion(QUESTION_ID);
        assertThat(response).isNull();
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
        StackOverflowCommentsResponse response = client.fetchComments(QUESTION_ID);
        assertThat(response).isNull();
    }
}
