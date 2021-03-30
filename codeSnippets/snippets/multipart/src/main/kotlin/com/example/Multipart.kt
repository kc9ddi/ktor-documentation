package com.example

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.File

fun Application.main() {
    install(AutoHeadResponse)
    routing {
        var fileDescription = ""
        var fileName = ""

        post("/post") {
            val multiPartData = call.receiveMultipart()

            multiPartData.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        fileDescription = part.value
                    }
                    is PartData.FileItem -> {
                        fileName = part.originalFileName as String
                        var fileBytes = part.streamProvider().readBytes()
                        File("files/$fileName").writeBytes(fileBytes)
                    }
                }
            }

            call.respondText("$fileDescription is uploaded to files/$fileName")
        }
    }
}
