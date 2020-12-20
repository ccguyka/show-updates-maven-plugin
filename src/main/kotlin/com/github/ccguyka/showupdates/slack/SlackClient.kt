package com.github.ccguyka.showupdates.slack

import com.github.ccguyka.showupdates.objects.ProjectUpdates
import com.github.ccguyka.showupdates.slack.SlackBody.Companion.artifact
import com.slack.api.Slack
import com.slack.api.model.block.LayoutBlock
import com.slack.api.model.block.SectionBlock
import com.slack.api.model.block.composition.MarkdownTextObject
import com.slack.api.webhook.Payload
import com.slack.api.webhook.WebhookResponse
import java.io.IOException

class SlackClient(private val artifact: String?) {

    private var projectUpdates: ProjectUpdates? = null
    private var token: String? = null

    fun updates(projectUpdates: ProjectUpdates?): SlackClient {
        this.projectUpdates = projectUpdates
        return this
    }

    fun token(token: String?): SlackClient {
        this.token = token
        return this
    }

    @Throws(IOException::class)
    fun send(): Response {
        val markdownText = artifact(artifact)
                .updates(projectUpdates)
                .build()
        val block: LayoutBlock = SectionBlock.builder()
                .text(MarkdownTextObject.builder()
                        .text(markdownText)
                        .build())
                .build()
        val payload = Payload.builder()
                .blocks(listOf(block))
                .build()
        val slack = Slack.getInstance()
        return Response.response(slack.send(token, payload))
    }

    class Response private constructor(val code: Int, val message: String) {

        companion object {
            fun response(response: WebhookResponse): Response {
                return Response(response.code, response.body)
            }
        }
    }

    companion object {
        @JvmStatic
        fun forArtifact(artifact: String?): SlackClient {
            return SlackClient(artifact)
        }
    }
}
