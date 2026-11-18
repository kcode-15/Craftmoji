# Craftmoji

Craftmoji is a client-side Fabric mod for Minecraft 1.21.1. Run `/craftmoji` to open a paginated emoji picker. The mod downloads the emoji catalog in the background. Selecting an emoji opens the normal Minecraft chat input with that Unicode character prefilled. Nothing is sent until you review and submit the message yourself.

## Download

Download published Craftmoji releases from [Modrinth](https://modrinth.com/project/craftmoji).

1. Download the JAR for Minecraft 1.21.1.
2. Install [Fabric Loader](https://fabricmc.net/use/installer/) for Minecraft 1.21.1.
3. Install [Fabric API](https://modrinth.com/mod/fabric-api).
4. Place the Craftmoji JAR and Fabric API JAR in your Minecraft `mods` folder.
5. Launch the Fabric 1.21.1 profile.

The GitHub Actions build also produces JAR artifacts. GitHub artifacts are build outputs and may be development builds; use the published Modrinth release for a stable distribution.

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

## Major-server compatibility and ban risk

Craftmoji is a client-side utility that uses Minecraft’s normal chat screen. It does not automatically send chat, execute commands, use macros, alter network packets, bypass chat filters, or provide gameplay advantages.

Copying or typing a Unicode emoji manually is not identified as cheating by the public rules of Hypixel or CubeCraft. However, no server can guarantee that an emoji is accepted, correctly rendered, or permitted in every chat channel.

- **Hypixel:** Unicode/emoji text is not explicitly prohibited. Automated chat, macros, and disruptive chat behavior may be punishable. Avoid spam, excessive repeated use, and automatic messages. Official rules: https://hypixel.net/rules
- **CubeCraft:** Unicode/emoji text is not explicitly prohibited. Mods or tools that provide a gameplay advantage and macros or chat automation are prohibited. Avoid spam and disruptive chat. Official rules: https://www.cubecraft.net/rules
- Manual emoji insertion is materially different from an auto-chat or spam macro. This mod requires you to open normal chat, review, edit, and manually press Enter.

Server staff may still reject or filter any text. Rules and moderation decisions can change without notice, so check the current policy and server chat rules before use.

**There is no guarantee of zero ban risk.** Use only permitted emoji, never automate sending, and do not use the mod to bypass restrictions or evade punishment.
