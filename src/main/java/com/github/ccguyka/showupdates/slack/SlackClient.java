package com.github.ccguyka.showupdates.slack;

import java.io.IOException;
import java.util.Arrays;

import com.github.ccguyka.showupdates.objects.ProjectUpdates;
import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.model.block.LayoutBlock;
import com.github.seratch.jslack.api.model.block.SectionBlock;
import com.github.seratch.jslack.api.model.block.composition.MarkdownTextObject;
import com.github.seratch.jslack.api.webhook.Payload;
import com.github.seratch.jslack.api.webhook.WebhookResponse;

public class SlackClient {

    private ProjectUpdates projectUpdates;
    private String token;
    private String artifact;

    public SlackClient(String artifact) {
        this.artifact = artifact;
    }

    public static SlackClient forArtifact(String artifact) {
        return new SlackClient(artifact);
    }

    public SlackClient updates(ProjectUpdates projectUpdates) {
        this.projectUpdates = projectUpdates;

        return this;
    }

    public SlackClient token(String token) {
        this.token = token;

        return this;
    }

    public Response send() throws IOException {
        String markdownText = SlackBody.artifact(artifact)
                .updates(projectUpdates)
                .build();
        LayoutBlock block = SectionBlock.builder()
                .text(MarkdownTextObject.builder()
                        .text(markdownText)
                        .build())
                .build();
        Payload payload = Payload.builder()
                .blocks(Arrays.asList(block))
                .build();

        Slack slack = Slack.getInstance();
        return Response.response(slack.send(token, payload));
    }

    public static class Response {

        private Integer code;
        private String message;

        private Response(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        static Response response(WebhookResponse response) {
            return new Response(response.getCode(), response.getBody());
        }

        public Integer getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

}
