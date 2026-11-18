# Craftmoji

Craftmoji is a client-side Fabric mod for Minecraft 1.21.1. Run `/craftmoji` to open a paginated emoji picker. The mod downloads the emoji catalog in the background. Selecting an emoji opens the normal Minecraft chat input with that Unicode character prefilled. Nothing is sent until you review and submit the message yourself.

## Build

Use Java 21 and Gradle 8.8:

```sh
gradle build
```

The mod JAR is written to `build/libs/craftmoji-1.0.0.jar`. GitHub Actions also builds the project on pushes, pull requests, or manual dispatch and uploads the JAR files as the `craftmoji-fabric` workflow artifact. If there are multiple JARs in the artifact, install the one that does not end in `-sources.jar`.

## Use

Install Fabric Loader and Fabric API for Minecraft 1.21.1, then place the built mod JAR in your `mods` folder. Join a world or server and run `/craftmoji` in chat. The selected emoji is inserted into the standard chat box, where you can edit or send it normally.

The picker retrieves the emoji codepoint/name list from the public `iamcal/emoji-data` GitHub repository. A network connection is needed to fetch the list. Rendering depends on Minecraft's available fonts and the handling of Unicode by the client and server.

## Server compatibility and limitations

This is a client-side picker only. It uses the ordinary Minecraft chat input and does not change, disguise, or automate network traffic. It cannot guarantee that a particular server accepts a Unicode character or that using it complies with that server's rules. Check server rules and use only characters permitted by the server; server moderation and chat filters still apply.
