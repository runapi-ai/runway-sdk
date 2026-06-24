package ai.runapi.runway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ai.runapi.core.RequestOptions;
import ai.runapi.core.errors.ValidationException;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpResponse;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.http.JsonRequestBody;
import ai.runapi.core.json.Json;
import ai.runapi.runway.types.CompletedTextToVideoResponse;
import ai.runapi.runway.types.TextToVideoResponse;
import ai.runapi.runway.types.CompletedExtendVideoResponse;
import ai.runapi.runway.types.CompletedTextToVideoResponse;
import ai.runapi.runway.types.ExtendVideoModel;
import ai.runapi.runway.types.ExtendVideoParams;
import ai.runapi.runway.types.ExtendVideoResponse;
import ai.runapi.runway.types.TextToVideoModel;
import ai.runapi.runway.types.TextToVideoParams;
import ai.runapi.runway.types.TextToVideoResponse;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class RunwayClientTest {
  @Test
  void builderCreatesClientAndUniversalResources() {
    RunwayClient client = RunwayClient.builder().apiKey("sk-test").build();

    assertNotNull(client.textToVideo());
    assertNotNull(client.files());
    assertNotNull(client.account());
  }

  @Test
  void openValueClassesSerializeAsScalarStrings() throws Exception {
    String json = Json.mapper().writeValueAsString(new TextToVideoModel("runway"));

    assertEquals("\"runway\"", json);
    assertEquals(new TextToVideoModel("runway"), Json.mapper().readValue(json, TextToVideoModel.class));
  }

  @Test
  void createSendsExpectedRequestShape() throws Exception {
    CapturingTransport transport = new CapturingTransport("{\"id\":\"task_123\",\"status\":\"processing\"}");
    RunwayClient client = RunwayClient.builder().apiKey("sk-test").transport(transport).build();

    client.textToVideo().create(
        TextToVideoParams.builder()
            .model(TextToVideoModel.RUNWAY)
            .prompt("A small red cube on a plain white table, studio product photo")
            .durationSeconds(5)
            .outputResolution("720p")
            .aspectRatio("16:9")
            .build()
    );

    assertEquals("POST", transport.request.getMethod().name());
    assertEquals("/api/v1/runway/text_to_video", transport.request.getPath());
    JsonNode body = bodyJson(transport.request);
    assertNotNull(body);
  }

  @Test
  void getDecodesTaskResponseAndExtraFields() {
    CapturingTransport transport = new CapturingTransport("{\"id\":\"task_456\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}],\"custom\":\"kept\"}");
    RunwayClient client = RunwayClient.builder().apiKey("sk-test").transport(transport).build();

    TextToVideoResponse response = client.textToVideo().get("task_456");

    assertEquals("GET", transport.request.getMethod().name());
    assertEquals("/api/v1/runway/text_to_video/task_456", transport.request.getPath());
    assertEquals("completed", response.getStatus().value());
    assertNotNull(response.getVideos());
    assertEquals("kept", response.extraFields().get("custom").asText());
  }

  @Test
  void runPollsUntilCompletedAndKeepsExtraFields() {
    SequenceTransport transport = new SequenceTransport(
        "{\"id\":\"task_789\",\"status\":\"processing\"}",
        "{\"id\":\"task_789\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}],\"custom\":\"kept\"}");
    RunwayClient client = RunwayClient.builder().apiKey("sk-test").transport(transport).build();

    CompletedTextToVideoResponse response = client.textToVideo().run(
        TextToVideoParams.builder()
            .model(TextToVideoModel.RUNWAY)
            .prompt("A small red cube on a plain white table, studio product photo")
            .durationSeconds(5)
            .outputResolution("720p")
            .aspectRatio("16:9")
            .build(),
        RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());

    assertEquals("completed", response.getStatus().value());
    assertNotNull(response.getVideos());
    assertEquals("kept", response.extraFields().get("custom").asText());
    assertEquals(2, transport.calls);
  }

  @Test
  void runRejectsCompletedResponseMissingResultField() {
    SequenceTransport transport = new SequenceTransport(
        "{\"id\":\"task_missing\",\"status\":\"processing\"}",
        "{\"id\":\"task_missing\",\"status\":\"completed\"}");
    RunwayClient client = RunwayClient.builder().apiKey("sk-test").transport(transport).build();

    assertThrows(
        ValidationException.class,
        () -> client.textToVideo().run(
                TextToVideoParams.builder()
                    .model(TextToVideoModel.RUNWAY)
                    .prompt("A small red cube on a plain white table, studio product photo")
                    .durationSeconds(5)
                    .outputResolution("720p")
                    .aspectRatio("16:9")
                    .build(),
            RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
  }

    @Test
    void coversExtendvideoResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_extend_video\",\"status\":\"processing\"}");
      RunwayClient createClient = RunwayClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.extendVideo().create(
              ExtendVideoParams.builder()
                  .model(ExtendVideoModel.RUNWAY)
                  .sourceTaskId("sample")
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("720p")
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_extend_video_options\",\"status\":\"processing\"}");
      RunwayClient createWithOptionsClient = RunwayClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.extendVideo().create(
              ExtendVideoParams.builder()
                  .model(ExtendVideoModel.RUNWAY)
                  .sourceTaskId("sample")
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("720p")
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_extend_video\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      RunwayClient getClient = RunwayClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.extendVideo().get("task_extend_video"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_extend_video_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      RunwayClient getWithOptionsClient = RunwayClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.extendVideo().get("task_extend_video_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_extend_video_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_extend_video_run\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      RunwayClient runClient = RunwayClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedExtendVideoResponse runResponse = runClient.extendVideo().run(
              ExtendVideoParams.builder()
                  .model(ExtendVideoModel.RUNWAY)
                  .sourceTaskId("sample")
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("720p")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_extend_video_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_extend_video_run_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      RunwayClient runWithOptionsClient = RunwayClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.extendVideo().run(
              ExtendVideoParams.builder()
                  .model(ExtendVideoModel.RUNWAY)
                  .sourceTaskId("sample")
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("720p")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

    @Test
    void coversTexttovideoResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_text_to_video\",\"status\":\"processing\"}");
      RunwayClient createClient = RunwayClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.textToVideo().create(
              TextToVideoParams.builder()
                  .model(TextToVideoModel.RUNWAY)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .durationSeconds(5)
                  .outputResolution("720p")
                  .aspectRatio("16:9")
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_text_to_video_options\",\"status\":\"processing\"}");
      RunwayClient createWithOptionsClient = RunwayClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.textToVideo().create(
              TextToVideoParams.builder()
                  .model(TextToVideoModel.RUNWAY)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .durationSeconds(5)
                  .outputResolution("720p")
                  .aspectRatio("16:9")
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_text_to_video\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      RunwayClient getClient = RunwayClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.textToVideo().get("task_text_to_video"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_text_to_video_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      RunwayClient getWithOptionsClient = RunwayClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.textToVideo().get("task_text_to_video_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_text_to_video_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_text_to_video_run\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      RunwayClient runClient = RunwayClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedTextToVideoResponse runResponse = runClient.textToVideo().run(
              TextToVideoParams.builder()
                  .model(TextToVideoModel.RUNWAY)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .durationSeconds(5)
                  .outputResolution("720p")
                  .aspectRatio("16:9")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_text_to_video_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_text_to_video_run_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      RunwayClient runWithOptionsClient = RunwayClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.textToVideo().run(
              TextToVideoParams.builder()
                  .model(TextToVideoModel.RUNWAY)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .durationSeconds(5)
                  .outputResolution("720p")
                  .aspectRatio("16:9")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

  private static JsonNode bodyJson(HttpRequest request) throws Exception {
    JsonRequestBody body = (JsonRequestBody) request.getBody();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    body.writeTo(out);
    return Json.mapper().readTree(out.toByteArray());
  }

  private static final class CapturingTransport implements HttpTransport {
    private final String body;
    private HttpRequest request;

    private CapturingTransport(String body) {
      this.body = body;
    }

    public HttpResponse send(HttpRequest request) {
      this.request = request;
      return new HttpResponse(200, body, Collections.<String, java.util.List<String>>emptyMap());
    }

    public void close() {}
  }

  private static final class SequenceTransport implements HttpTransport {
    private final String[] responses;
    private int calls;

    private SequenceTransport(String... responses) {
      this.responses = responses;
    }

    public HttpResponse send(HttpRequest request) {
      String response = responses[Math.min(calls, responses.length - 1)];
      calls++;
      return new HttpResponse(200, response, Collections.<String, java.util.List<String>>emptyMap());
    }

    public void close() {}
  }
}
