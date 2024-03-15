package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.dto.StackOverflowItem;
import edu.java.client.dto.StackOverflowResponse;
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
    }

    public void setupStubServerError() {
        stubFor(get(urlEqualTo(URL_SCENARIO))
            .willReturn(aResponse()
                .withStatus(404)
            ));
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
        StackOverflowResponse response = client.fetchQuestion(QUESTION_ID);
        assertThat(response).isNull();
    }
}
